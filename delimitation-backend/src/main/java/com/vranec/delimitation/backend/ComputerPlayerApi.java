package com.vranec.delimitation.backend;

import com.vranec.delimitation.backend.model.FullGameResponse;
import com.vranec.delimitation.backend.model.Move;
import org.springframework.scheduling.annotation.Async;

import java.util.Optional;

public interface ComputerPlayerApi {

    @Async
    void startThinking(FullGameResponse game);

    Optional<Move> getComputedMove(FullGameResponse game);
}
