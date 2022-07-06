package com.vranec.delimitation.backend;

import com.vranec.delimitation.backend.model.FullGameResponse;
import com.vranec.delimitation.backend.model.Move;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
public class DefaultComputerApi implements ComputerPlayerApi {

    public void startThinking(FullGameResponse game) {

    }

    public Optional<Move> getComputedMove(FullGameResponse game) {
        List<Move> possibleMoves = new ArrayList<>(game.getPossibleMoves());
        return Optional.of(possibleMoves.get(new Random().nextInt(possibleMoves.size())));
    }
}
