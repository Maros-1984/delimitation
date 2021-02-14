package com.vranec.delimitation.backend;

import com.vranec.delimitation.backend.model.CreateNewGameRequest;
import com.vranec.delimitation.backend.model.FullGameResponse;
import com.vranec.delimitation.backend.model.GetGameStatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class GameController {
    @Autowired
    private GameService gameService;

    @PostMapping("/create-new-game")
    FullGameResponse createNewGame(@RequestBody CreateNewGameRequest request) {
        return gameService.createNewGame(request);
    }

    @PostMapping("/get-game-status")
    FullGameResponse createNewGame(@RequestBody GetGameStatusRequest request) {
        return gameService.getGameStatus(request);
    }
}
