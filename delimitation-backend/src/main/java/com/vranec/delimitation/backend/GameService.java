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
        for (int rowIndex = 0; rowIndex < request.getHeight(); rowIndex++) {
            areas.add(new ArrayList<>());
            for (int columnIndex = 0; columnIndex < request.getWidth(); columnIndex++) {
                areas.get(rowIndex).add(AreaColor.EMPTY);
            }
        }

        FullGameResponse game = new FullGameResponse()
                .setAreas(areas)
                .setGameId(RandomStringUtils.randomAlphanumeric(5))
                .setYourPlayerColor(PlayerColor.values()[RandomUtils.nextInt(0, 1)])
                .setPlayerOnMove(PlayerColor.values()[RandomUtils.nextInt(0, 1)]);
        game.setPossibleMoves(genenerateNewBaseMoves(game));
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
        game.setPossibleMoves(removeNonsenseMoves(computePossibleMoves(game, request.getMove()), game));
        game.setPlayerOnMove(game.getPlayerOnMove().otherPlayer());
        databaseGateway.save(game);

        game.setPossibleMoves(new HashSet<>());
        return game;
    }

    private Set<Move> computePossibleMoves(FullGameResponse game, Move move) {
        Set<Move> firstSideNeighbourMoves = move.getFirstSideNeighbourMoves();
        Set<Move> secondSideNeighbourMoves = move.getSecondSideNeighbourMoves();

        boolean connectsToFirstMoves = game.connectsTo(firstSideNeighbourMoves);
        boolean connectsToSecondMoves = game.connectsTo(secondSideNeighbourMoves);

        if (connectsToFirstMoves && !connectsToSecondMoves) {
            return secondSideNeighbourMoves;
        } else if (!connectsToFirstMoves && connectsToSecondMoves) {
            return firstSideNeighbourMoves;
        } else if (!connectsToFirstMoves) {
            return new HashSet<>();
        }

        fillAreasWithPlayerColor(game, move);
        return genenerateNewBaseMoves(game);
    }

    private Set<Move> genenerateNewBaseMoves(FullGameResponse game) {
        Set<Move> moves = new HashSet<>();

        for (int rowIndex = 0; rowIndex < game.getHeight(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < game.getWidth(); columnIndex++) {
                if ((rowIndex == 0 || rowIndex == game.getHeight() - 1) && columnIndex < game.getWidth() - 1) {
                    moves.add(new Move()
                            .setAreaY(rowIndex)
                            .setAreaX(columnIndex)
                            .setRight(true));
                }
                if ((columnIndex == 0 || columnIndex == game.getWidth() - 1) && rowIndex < game.getHeight() - 1) {
                    moves.add(new Move()
                            .setAreaY(rowIndex)
                            .setAreaX(columnIndex)
                            .setBottom(true));
                }
            }
        }

        game.getMoves().forEach(move -> {
            Set<Move> neighbourMoves = move.getFirstSideNeighbourMoves();
            neighbourMoves.addAll(move.getSecondSideNeighbourMoves());
            moves.addAll(neighbourMoves);
        });

        return moves.stream()
                .filter(move -> !game.getMoves().contains(move))
                .filter(move -> move.isBetweenFreeAreas(game))
                .collect(Collectors.toSet());
    }

    private void fillAreasWithPlayerColor(FullGameResponse game, Move move) {
        Area firstArea = move.getFirstArea(game);
        int firstAreaFillSize = computeAreaFillSize(firstArea);
        Area secondArea = move.getSecondArea(game);
        int secondAreaFillSize = computeAreaFillSize(secondArea);
        Area areaToFill;
        if (firstAreaFillSize > secondAreaFillSize) {
            areaToFill = secondArea;
        } else {
            areaToFill = firstArea;
        }
        fillArea(areaToFill, Integer.MAX_VALUE);
    }

    private Set<Area> fillArea(Area areaToFill, int maxAreaSizeToFill) {
        Set<Area> areasFilled = new HashSet<>();
        areaToFill.fill();
        areasFilled.add(areaToFill);
        fillAreas(areasFilled, maxAreaSizeToFill);
        return areasFilled;
    }

    private int computeAreaFillSize(Area area) {
        return computeAreaFillSize(area, Integer.MAX_VALUE);
    }

    private int computeAreaFillSize(Area area, int maxAreaSizeToFill) {
        Set<Area> areasFilled = fillArea(area, maxAreaSizeToFill);
        unfillAreas(areasFilled);
        return areasFilled.size();
    }

    private void unfillAreas(Set<Area> areasFilled) {
        areasFilled.forEach(Area::unfill);
    }

    private void fillAreas(Set<Area> areasFilled, int maxAreaSizeToFill) {
        Set<Area> newAreasFilled = new HashSet<>();
        if (areasFilled.size() >= maxAreaSizeToFill) {
            return;
        }
        areasFilled.forEach(area -> {
            Set<Area> filledNeighbours = area.fillNeighbours();
            newAreasFilled.addAll(filledNeighbours);
        });
        if (newAreasFilled.isEmpty()) {
            return;
        }
        areasFilled.addAll(newAreasFilled);
        fillAreas(areasFilled, maxAreaSizeToFill);
    }

    private Set<Move> removeNonsenseMoves(Set<Move> moves, FullGameResponse game) {
        return moves.stream().filter(move -> isNoNonsenseMove(move, game)).collect(Collectors.toSet());
    }

    private boolean isNoNonsenseMove(Move move, FullGameResponse game) {
        Set<Move> firstSideNeighbourMoves = move.getFirstSideNeighbourMoves();
        Set<Move> secondSideNeighbourMoves = move.getSecondSideNeighbourMoves();

        boolean connectsToFirstMoves = game.connectsTo(firstSideNeighbourMoves);
        boolean connectsToSecondMoves = game.connectsTo(secondSideNeighbourMoves);

        if (connectsToFirstMoves && !connectsToSecondMoves) {
            return true;
        } else if (!connectsToFirstMoves && connectsToSecondMoves) {
            return true;
        }

        game.getMoves().add(move);
        Area firstArea = move.getFirstArea(game);
        int firstAreaFillSize = computeAreaFillSize(firstArea, 3);
        Area secondArea = move.getSecondArea(game);
        int secondAreaFillSize = computeAreaFillSize(secondArea, 3);
        game.getMoves().remove(move);
        return firstAreaFillSize > 2 && secondAreaFillSize > 2;
    }
}
