package com.vranec.delimitation.backend;

import com.vranec.delimitation.backend.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
class GameService {
    @Autowired
    private DatabaseGateway databaseGateway;

    FullGameResponse createNewGame(CreateNewGameRequest request) {
        List<List<AreaColor>> areas = new ArrayList<>();
        Set<Move> possibleMoves = new HashSet<>();
        for (int rowIndex = 0; rowIndex < request.getHeight(); rowIndex++) {
            areas.add(new ArrayList<>());
            for (int columnIndex = 0; columnIndex < request.getWidth(); columnIndex++) {
                areas.get(rowIndex).add(AreaColor.EMPTY);
                if ((rowIndex == 0 || rowIndex == request.getHeight() - 1) && columnIndex < request.getWidth() - 1) {
                    possibleMoves.add(new Move()
                            .setAreaY(rowIndex)
                            .setAreaX(columnIndex)
                            .setRight(true));
                }
                if ((columnIndex == 0 || columnIndex == request.getWidth() - 1) && rowIndex < request.getHeight() - 1) {
                    possibleMoves.add(new Move()
                            .setAreaY(rowIndex)
                            .setAreaX(columnIndex)
                            .setBottom(true));
                }
            }
        }

        FullGameResponse game = new FullGameResponse()
                .setAreas(areas)
                .setPossibleMoves(possibleMoves)
                .setGameId(RandomStringUtils.randomAlphanumeric(5))
                .setYourPlayerColor(PlayerColor.values()[RandomUtils.nextInt(0, 1)])
                .setPlayerOnMove(PlayerColor.values()[RandomUtils.nextInt(0, 1)]);
        databaseGateway.save(game);
        if (game.getYourPlayerColor() != game.getPlayerOnMove()) {
            game.setPossibleMoves(new HashSet<>());
        }
        return game;
    }

    FullGameResponse getGameStatus(GetGameStatusRequest request) {
        FullGameResponse game = databaseGateway.load(request.getGameId());
        if (request.getPlayerAsking() != game.getYourPlayerColor()) {
            request.setPlayerAsking(game.getYourPlayerColor().otherPlayer());
        }
        if (request.getPlayerAsking() != game.getPlayerOnMove()) {
            game.setPossibleMoves(new HashSet<>());
        }
        game.setYourPlayerColor(request.getPlayerAsking());
        return game;
    }

    FullGameResponse makeMove(MakeMoveRequest request) {
        FullGameResponse game = getGameStatus(new GetGameStatusRequest()
                .setGameId(request.getGameId())
                .setPlayerAsking(request.getPlayer())
                .setDifferenceOnly(true));
        if (!game.getPossibleMoves().contains(request.getMove())) {
            return game;
        }
        
        request.getMove().setColor(request.getPlayer());
        game.getMoves().add(request.getMove());
        game.setPlayerOnMove(game.getPlayerOnMove().otherPlayer());
        game.setPossibleMoves(removeNonsenseMoves(computePossibleMoves(game, request.getMove()), game));
        databaseGateway.save(game);

        game.setPossibleMoves(new HashSet<>());
        return game;
    }

    private Set<Move> computePossibleMoves(FullGameResponse game, Move move) {
        Set<Move> moves = new HashSet<>();
        Set<Move> firstSideNeighbourMoves = move.getFirstSideNeighbourMoves();
        Set<Move> secondSideNeighbourMoves = move.getSecondSideNeighbourMoves();

        boolean connectsToFirstMoves = game.connectsTo(firstSideNeighbourMoves);
        boolean connectsToSecondMoves = game.connectsTo(secondSideNeighbourMoves);

        if (connectsToFirstMoves && !connectsToSecondMoves) {
            return secondSideNeighbourMoves;
        } else if (!connectsToFirstMoves && connectsToSecondMoves) {
            return firstSideNeighbourMoves;
        } else {
            // TODO: Implement finishing moves.
            return moves;
        }
    }

    private Set<Move> removeNonsenseMoves(Set<Move> moves, FullGameResponse game) {
        // TODO: Do not rmeove finishing moves.
        return moves.stream().filter(move -> !computePossibleMoves(game, move).isEmpty()).collect(Collectors.toSet());
    }
}
