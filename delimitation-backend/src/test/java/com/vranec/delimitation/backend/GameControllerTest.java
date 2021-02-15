package com.vranec.delimitation.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vranec.delimitation.backend.model.FullGameResponse;
import com.vranec.delimitation.backend.model.GetGameStatusRequest;
import com.vranec.delimitation.backend.model.MakeMoveRequest;
import com.vranec.delimitation.backend.model.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@ActiveProfiles("test")
class GameControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createNewGame_givenNoArguments_generatesDefaultGame() throws Exception {
        FullGameResponse game = createDefaultGame();

        assertThat(game.getAreas()).hasSizeGreaterThan(5);
        assertThat(game.getAreas().get(0)).hasSizeGreaterThan(5);
        assertThat(game.getGameId()).isNotBlank();
        assertThat(game.getYourPlayerColor()).isNotNull();
        assertThat(game.getPlayerOnMove()).isNotNull();
        assertThat(game.getPossibleMoves()).isNotEmpty();
        // Upper left corner.
        assertThat(game.getPossibleMoves()).contains(new Move().setAreaX(0).setAreaY(0).setRight(true));
        assertThat(game.getPossibleMoves()).contains(new Move().setAreaX(0).setAreaY(0).setBottom(true));
        // Upper right corner.
        assertThat(game.getPossibleMoves()).contains(new Move().setAreaX(18).setAreaY(0).setRight(true));
        assertThat(game.getPossibleMoves()).contains(new Move().setAreaX(19).setAreaY(0).setBottom(true));
        // Lower left corner.
        assertThat(game.getPossibleMoves()).contains(new Move().setAreaX(0).setAreaY(9).setRight(true));
        assertThat(game.getPossibleMoves()).contains(new Move().setAreaX(0).setAreaY(8).setBottom(true));
        // Lower right corner.
        assertThat(game.getPossibleMoves()).contains(new Move().setAreaX(18).setAreaY(9).setRight(true));
        assertThat(game.getPossibleMoves()).contains(new Move().setAreaX(19).setAreaY(8).setBottom(true));

        assertThat(game.getPossibleMoves()).doesNotContain(new Move().setAreaX(1).setAreaY(1).setRight(true));
        assertThat(game.getPossibleMoves()).doesNotContain(new Move().setAreaX(1).setAreaY(1).setBottom(true));
        assertThat(game.getPossibleMoves()).doesNotContain(new Move().setAreaX(19).setAreaY(0).setRight(true));
        assertThat(game.getPossibleMoves()).doesNotContain(new Move().setAreaX(0).setAreaY(9).setBottom(true));
        assertThat(game.getPossibleMoves()).doesNotContain(new Move().setAreaX(19).setAreaY(9).setRight(true));
        assertThat(game.getPossibleMoves()).doesNotContain(new Move().setAreaX(19).setAreaY(9).setBottom(true));
    }

    @Test
    void getGameStatus_givenCreatedGame_returnsIt() throws Exception {
        FullGameResponse newGame = createDefaultGame();

        MvcResult mvcResult = mockMvc.perform(post("/get-game-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new GetGameStatusRequest()
                        .setGameId(newGame.getGameId())
                        .setPlayerAsking(newGame.getPlayerOnMove()))))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        String responseBody = mvcResult.getResponse().getContentAsString();
        assertThat(responseBody).isNotBlank();
        FullGameResponse game = objectMapper.readValue(responseBody, FullGameResponse.class);
        assertThat(game).isEqualTo(newGame);
    }

    @Test
    void getGameStatus_givenPlayerNotOnTurn_returnsNoPossibleMoves() throws Exception {
        FullGameResponse newGame = createDefaultGame();

        MvcResult mvcResult = mockMvc.perform(post("/get-game-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new GetGameStatusRequest()
                        .setGameId(newGame.getGameId())
                        .setPlayerAsking(newGame.getPlayerOnMove().otherPlayer()))))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        String responseBody = mvcResult.getResponse().getContentAsString();
        assertThat(responseBody).isNotBlank();
        FullGameResponse game = objectMapper.readValue(responseBody, FullGameResponse.class);
        assertThat(game.getPossibleMoves()).isEmpty();
    }

    @Test
    void makeMove_givenPlayerNotOnTurn_returnsUnchangedGame() throws Exception {
        FullGameResponse newGame = createDefaultGame();

        MvcResult mvcResult = mockMvc.perform(post("/make-move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new MakeMoveRequest()
                        .setGameId(newGame.getGameId())
                        .setMove(new Move().setAreaX(0).setAreaY(0).setRight(true))
                        .setPlayer(newGame.getPlayerOnMove().otherPlayer()))))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        String responseBody = mvcResult.getResponse().getContentAsString();
        assertThat(responseBody).isNotBlank();
        FullGameResponse game = objectMapper.readValue(responseBody, FullGameResponse.class);
        assertThat(game.getAreas()).isEqualTo(newGame.getAreas());
        assertThat(game.getMoves()).isEmpty();
        assertThat(game.getPossibleMoves()).isEmpty();
    }

    @Test
    void makeMove_givenPlayerOnTurn_returnsChanged() throws Exception {
        FullGameResponse newGame = createDefaultGame();
        Move move = new Move().setAreaX(0).setAreaY(0).setRight(true);

        MvcResult mvcResult = mockMvc.perform(post("/make-move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new MakeMoveRequest()
                        .setGameId(newGame.getGameId())
                        .setMove(move)
                        .setPlayer(newGame.getPlayerOnMove()))))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        String responseBody = mvcResult.getResponse().getContentAsString();
        assertThat(responseBody).isNotBlank();
        FullGameResponse game = objectMapper.readValue(responseBody, FullGameResponse.class);
        assertThat(game.getAreas()).isEqualTo(newGame.getAreas());
        assertThat(game.getMoves()).hasSize(1);
        assertThat(game.getMoves()).contains(move);
        assertThat(game.getPossibleMoves()).isEmpty();
    }


    @Test
    void makeMove_givenOtherPlayerTurn_returnsChangedGame() throws Exception {
        FullGameResponse newGame = createDefaultGame();
        Move move = new Move().setAreaX(0).setAreaY(0).setRight(true);

        mockMvc.perform(post("/make-move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new MakeMoveRequest()
                        .setGameId(newGame.getGameId())
                        .setMove(move)
                        .setPlayer(newGame.getPlayerOnMove()))))
                .andReturn();

        MvcResult mvcResult = mockMvc.perform(post("/get-game-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new GetGameStatusRequest()
                        .setGameId(newGame.getGameId())
                        .setPlayerAsking(newGame.getPlayerOnMove().otherPlayer()))))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        String responseBody = mvcResult.getResponse().getContentAsString();
        assertThat(responseBody).isNotBlank();
        FullGameResponse game = objectMapper.readValue(responseBody, FullGameResponse.class);
        assertThat(game.getPossibleMoves()).hasSize(2);
    }

    private FullGameResponse createDefaultGame() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/create-new-game")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        String responseBody = mvcResult.getResponse().getContentAsString();
        assertThat(responseBody).isNotBlank();
        return objectMapper.readValue(responseBody, FullGameResponse.class);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
}