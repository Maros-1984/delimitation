package com.vranec.delimitation.backend;

import com.vranec.delimitation.backend.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
class GameService {
    @Autowired
    private DatabaseGateway databaseGateway;

    FullGameResponse createNewGame(CreateNewGameRequest request) {
        List<List<AreaColor>> areas = new ArrayList<>();
        List<PossibleMove> possibleMoves = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < request.getHeight(); rowIndex++) {
            areas.add(new ArrayList<>());
            for (int columnIndex = 0; columnIndex < request.getWidth(); columnIndex++) {
                areas.get(rowIndex).add(AreaColor.EMPTY);
                if ((rowIndex == 0 || rowIndex == request.getHeight() - 1) && columnIndex < request.getWidth() - 1) {
                    possibleMoves.add(new PossibleMove()
                            .setAreaY(rowIndex)
                            .setAreaX(columnIndex)
                            .setRight(true));
                }
                if ((columnIndex == 0 || columnIndex == request.getWidth() - 1) && rowIndex < request.getHeight() - 1) {
                    possibleMoves.add(new PossibleMove()
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
        return game;
    }

    FullGameResponse getGameStatus(GetGameStatusRequest request) {
        FullGameResponse game = databaseGateway.load(request.getGameId());
        if (request.getPlayerAsking() != game.getPlayerOnMove()) {
            game.setPossibleMoves(new ArrayList<>());
        }
        return game;
    }
}
