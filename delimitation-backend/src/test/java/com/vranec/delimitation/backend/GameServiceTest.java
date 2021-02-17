package com.vranec.delimitation.backend;

import com.vranec.delimitation.backend.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class GameServiceTest {
    @Autowired
    private GameService gameService;
    FullGameResponse game;
    PlayerColor player;
    private int expectedNumberOfMovesMade = 0;

    @Test
    void gameService_canFillAreas() {
        game = gameService.createNewGame(new CreateNewGameRequest());
        player = game.getPlayerOnMove();

        makeMove(new RightMove(0, 0));
        makeMove(new RightMove(0, 1));
        makeMove(new BottomMove(1, 1));
        makeMove(new RightMove(1, 1));
        makeMove(new BottomMove(2, 0));
        makeMove(new RightMove(2, 0));

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

        makeMove(new BottomMove(3, 0));
        makeMove(new BottomMove(4, 0));
    }

    @Test
    void gameService_canDetectGameOver() {
        game = gameService.createNewGame(new CreateNewGameRequest().setWidth(3).setHeight(2));
        player = game.getPlayerOnMove();

        makeMove(new BottomMove(0, 0));
        makeMove(new BottomMove(1, 0));
        makeMove(new BottomMove(2, 0));

        assertThat(game.getScore().get(player.toAreaColor())).isEqualTo(0);
        assertThat(game.getScore().get(player.otherPlayer().toAreaColor())).isEqualTo(3);
        assertThat(game.isOver()).isTrue();
    }

    private void makeMove(Move move) {
        Move result = new Move();
        BeanUtils.copyProperties(move, result);
        game = gameService.makeMove(new MakeMoveRequest()
                .setGameId(this.game.getGameId())
                .setMove(result)
                .setPlayer(player));
        player = game.getPlayerOnMove();
        expectedNumberOfMovesMade++;
        assertThat(game.getMoves()).hasSize(expectedNumberOfMovesMade);
    }
}
