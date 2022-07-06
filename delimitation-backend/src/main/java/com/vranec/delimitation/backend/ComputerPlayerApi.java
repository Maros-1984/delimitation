package com.vranec.delimitation.backend;

import com.vranec.delimitation.backend.model.FullGameResponse;
import com.vranec.delimitation.backend.model.Move;

import java.util.Optional;

public interface ComputerPlayerApi {

    void startThinking(FullGameResponse game);

    Optional<Move> getComputedMove(FullGameResponse game);
}
