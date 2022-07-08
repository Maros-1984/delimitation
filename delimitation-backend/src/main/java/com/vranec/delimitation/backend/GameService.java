package com.vranec.delimitation.backend;

import com.vranec.delimitation.backend.model.AreaColor;
import com.vranec.delimitation.backend.model.CreateNewGameRequest;
import com.vranec.delimitation.backend.model.FullGameResponse;
import com.vranec.delimitation.backend.model.GetGameStatusRequest;
import com.vranec.delimitation.backend.model.MakeMoveRequest;
import com.vranec.delimitation.backend.model.Move;
import com.vranec.delimitation.backend.model.PlayerColor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Slf4j
@Service
class GameService {

    @Autowired
    private DatabaseGateway databaseGateway;
    @Autowired
    private ComputerPlayerApi computerPlayerApi;

    FullGameResponse createNewGame(CreateNewGameRequest request) {
        log.info("Starting creating new game " + request.getWidth() + "x" + request.getHeight());
        AreaColor[][] areas = new AreaColor[request.getHeight()][request.getWidth()];
        for (int rowIndex = 0; rowIndex < request.getHeight(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < request.getWidth(); columnIndex++) {
                areas[rowIndex][columnIndex] = AreaColor.EMPTY;
            }
        }

        FullGameResponse game = new FullGameResponse()
                .setAreas(areas)
                .setGameId(RandomStringUtils.randomAlphanumeric(5))
                .setYourPlayerColor(PlayerColor.values()[RandomUtils.nextInt(0, 1)])
                .setPlayerOnMove(PlayerColor.values()[RandomUtils.nextInt(0, 1)]);
        log.info("Asking player is " + game.getYourPlayerColor());
        log.info("Player on turn is " + game.getPlayerOnMove());
        if (request.isAgainstComputer()) {
            log.info("The game is against a computer AI.");
            game.setComputerPlayer(game.getYourPlayerColor().otherPlayer());
            startComputerThinking(game);
        }
        game.setPossibleMoves(game.genenerateNewBaseMoves());
        databaseGateway.save(game);
        if (game.getYourPlayerColor() != game.getPlayerOnMove()) {
            game.setPossibleMoves(new HashSet<>());
        }
        log.info("New game " + game.getGameId() + " created.");
        return game;
    }

    FullGameResponse getGameStatus(GetGameStatusRequest request) {
        FullGameResponse game = databaseGateway.load(request.getGameId());
        if (request.getPlayerAsking() != game.getYourPlayerColor()) {
            request.setPlayerAsking(game.getYourPlayerColor().otherPlayer());
        }
        if (request.getPlayerAsking() != game.getPlayerOnMove()) {
            if (game.isComputerOnMove()) {
                Optional<Move> move = computerPlayerApi.getComputedMove(game);
                if (move.isPresent()) {
                    makeMove(new MakeMoveRequest()
                            .setGameId(game.getGameId())
                            .setMove(move.get())
                            .setPlayer(game.getComputerPlayer()), game);
                    return getGameStatus(request);
                }
            }
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
        return makeMove(request, game);
    }

    private FullGameResponse makeMove(MakeMoveRequest request, FullGameResponse game) {
        if (!game.getPossibleMoves().contains(request.getMove())) {
            return game;
        }

        request.getMove().setColor(request.getPlayer());
        log.info("Making move {} for player {}", request.getMove(), request.getPlayer());
        game.make(request.getMove());
        databaseGateway.save(game);

        startComputerThinking(game);
        game.setPossibleMoves(new HashSet<>());
        return game;
    }

    private void startComputerThinking(FullGameResponse game) {
        if (game.isComputerOnMove()) {
            computerPlayerApi.startThinking(game.getCopy());
        }
    }
}
