package com.vranec.delimitation.backend;

import com.vranec.delimitation.backend.model.AreaColor;
import com.vranec.delimitation.backend.model.CreateNewGameRequest;
import com.vranec.delimitation.backend.model.FullGameResponse;
import com.vranec.delimitation.backend.model.GetGameStatusRequest;
import com.vranec.delimitation.backend.model.MakeMoveRequest;
import com.vranec.delimitation.backend.model.Move;
import com.vranec.delimitation.backend.model.PlayerColor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles("test")
class GameServiceTest {

    @Autowired
    private GameService gameService;
    @Autowired
    private DatabaseGateway databaseGateway;
    FullGameResponse game;
    PlayerColor player;
    private int expectedNumberOfMovesMade = 0;

    @Test
    void gameService_canFillAreas() {
        game = gameService.createNewGame(new CreateNewGameRequest().setAgainstComputer(false));
        player = game.getPlayerOnMove();

        makeMove(new RightMove(0, 0));
        makeMove(new RightMove(0, 1));
        makeMove(new BottomMove(1, 1));
        makeMove(new RightMove(1, 1));
        makeMove(new BottomMove(2, 0));
        makeMove(new RightMove(2, 0));

        assertCorrectAreasAreFilled();

        makeMove(new BottomMove(3, 0));
        makeMove(new BottomMove(4, 0));
    }

    private void assertCorrectAreasAreFilled() {
        AreaColor areaColor = player.otherPlayer().toAreaColor();
        assertThat(game.getAreas().get(0).get(0)).isEqualTo(AreaColor.EMPTY);
        assertThat(game.getAreas().get(0).get(1)).isEqualTo(areaColor);
        assertThat(game.getAreas().get(0).get(2)).isEqualTo(areaColor);
        assertThat(game.getAreas().get(0).get(3)).isEqualTo(AreaColor.EMPTY);
        assertThat(game.getAreas().get(1).get(0)).isEqualTo(AreaColor.EMPTY);
        assertThat(game.getAreas().get(1).get(1)).isEqualTo(areaColor);
        assertThat(game.getAreas().get(1).get(2)).isEqualTo(AreaColor.EMPTY);
        assertThat(game.getAreas().get(2).get(0)).isEqualTo(AreaColor.EMPTY);
        assertThat(game.getAreas().get(2).get(1)).isEqualTo(AreaColor.EMPTY);
        assertThat(game.getAreas().get(2).get(2)).isEqualTo(AreaColor.EMPTY);
    }

    @Test
    void gameService_givenFilledAreas_doesNotRefillThemOnComputerMove() {
        game = gameService.createNewGame(new CreateNewGameRequest().setAgainstComputer(false));
        player = game.getPlayerOnMove();

        makeMove(new RightMove(0, 0));
        makeMove(new RightMove(0, 1));
        makeMove(new BottomMove(1, 1));
        makeMove(new RightMove(1, 1));
        makeMove(new BottomMove(2, 0));
        makeMove(new RightMove(2, 0));

        changePlayerOnMoveToComputer();
        game = gameService.getGameStatus(new GetGameStatusRequest()
                .setGameId(game.getGameId())
                .setPlayerAsking(game.getHumanPlayer())
                .setDifferenceOnly(true));

        assertCorrectAreasAreFilled();
    }

    private void changePlayerOnMoveToComputer() {
        game.setComputerPlayer(game.getPlayerOnMove());
        databaseGateway.save(game);
    }

    @Test
    void gameService_canDetectGameOver() {
        game = gameService.createNewGame(new CreateNewGameRequest().setWidth(3).setHeight(2).setAgainstComputer(false));
        player = game.getPlayerOnMove();

        makeMove(new BottomMove(0, 0));
        makeMove(new BottomMove(1, 0));
        makeMove(new BottomMove(2, 0));

        assertThat(game.getScore().get(player.toAreaColor())).isEqualTo(0);
        assertThat(game.getScore().get(player.otherPlayer().toAreaColor())).isEqualTo(3);
        assertThat(game.isOver()).isTrue();
    }

    @Test
    void gameService_givenGameAgainstComputer_autoMovesForComputer() {
        game = gameService.createNewGame(new CreateNewGameRequest().setWidth(3).setHeight(2));
        player = game.getPlayerOnMove();

        makeMove(new BottomMove(0, 0));
        changePlayerOnMoveToComputer();
        assertThat(game.getPlayerOnMove()).isEqualTo(game.getComputerPlayer());
        await().atMost(5, SECONDS).until(() -> {
            FullGameResponse currentGameStatus = gameService.getGameStatus(
                    new GetGameStatusRequest().setGameId(game.getGameId()).setPlayerAsking(player.otherPlayer()));
            return currentGameStatus.getPlayerOnMove() != player;
        });
        game = gameService.getGameStatus(
                new GetGameStatusRequest().setGameId(game.getGameId()).setPlayerAsking(player.otherPlayer()));
        assertThat(game.getPlayerOnMove()).isEqualTo(game.getComputerPlayer().otherPlayer());
        assertThat(game.getLastMove().getColor()).isEqualTo(game.getComputerPlayer());
        switchPlayerOnTurn();
        makeMove(new BottomMove(2, 0));
        assertThat(game.isOver()).isTrue();
    }

    private void makeMove(Move move) {
        Move result = new Move();
        BeanUtils.copyProperties(move, result);
        game = gameService.makeMove(new MakeMoveRequest()
                .setGameId(this.game.getGameId())
                .setMove(result)
                .setPlayer(player));
        switchPlayerOnTurn();
    }

    private void switchPlayerOnTurn() {
        player = game.getPlayerOnMove();
        expectedNumberOfMovesMade++;
        assertThat(game.getMoves()).hasSize(expectedNumberOfMovesMade);
    }
}
