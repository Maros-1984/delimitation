package com.vranec.delimitation.backend;

import com.vranec.delimitation.backend.model.AiBoard;
import com.vranec.delimitation.backend.model.FullGameResponse;
import com.vranec.delimitation.backend.model.Move;
import com.vranec.minimax.ArtificialIntelligence;
import com.vranec.minimax.Color;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Component
public class DefaultComputerApi implements ComputerPlayerApi {

    private final ArtificialIntelligence<Move> artificialIntelligence = new ArtificialIntelligence<>();
    private final Map<String, Move> gameMoves = new ConcurrentHashMap<>();

    public void startThinking(FullGameResponse game) {
        gameMoves.remove(game.getGameId());
        gameMoves.put(game.getGameId(),
                artificialIntelligence.getBestMoveTimedIterativeDeepeningTimed(new AiBoard(game), 10,
                        Color.COMPUTER, DateUtils.addSeconds(new Date(), 3).getTime()).getMove());
    }

    public Optional<Move> getComputedMove(FullGameResponse game) {
        Move result = gameMoves.get(game.getGameId());
        if (result == null) {
            return empty();
        }
        game.setPossibleMoves(game.generatePossibleMoves());
        return of(result);
    }
}
