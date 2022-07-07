package com.vranec.delimitation.backend.model;

import com.vranec.minimax.Board;
import com.vranec.minimax.Color;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import static java.util.Collections.emptySet;

@RequiredArgsConstructor
public class AiBoard implements Board<Move> {

    private final FullGameResponse game;
    private final Map<Move, Set<Area>> filledAreasPerMove = new HashMap<>();
    private final Stack<Move> moves = new Stack<>();

    @Override
    public boolean isGameOver() {
        return game.isOver();
    }

    @Override
    public int getBoardValue(Color color) {
        int coefficient = color == Color.HUMAN ? 1 : -1;
        return coefficient * (game.getScore().getOrDefault(AreaColor.valueOf(game.getHumanPlayer().name()), 0) -
                game.getScore().getOrDefault(AreaColor.valueOf(game.getComputerPlayer().name()), 0));
    }

    @Override
    public Iterable<Move> getNextBoards(Color color) {
        Set<Move> possibleMoves = game.getPossibleMoves();
        if (possibleMoves.isEmpty()) {
            game.setPossibleMoves(game.generatePossibleMoves());
            return game.getPossibleMoves();
        }
        return possibleMoves;
    }

    @Override
    public Board<Move> apply(Move move) {
        game.make(move);
        filledAreasPerMove.put(move, game.getLastFilledAreas());
        moves.push(move);
        return this;
    }

    @Override
    public void undo(Move move) {
        Move lastMove = game.getLastMove();
        game.getMoves().remove(lastMove);
        game.unfillAreas(filledAreasPerMove.getOrDefault(lastMove, emptySet()));
        game.setOver(false);
        game.setPlayerOnMove(game.getPlayerOnMove().otherPlayer());
        moves.pop();
        if (moves.isEmpty()) {
            game.setLastMove(null);
        } else {
            game.setLastMove(moves.peek());
        }
    }

    @Override
    public Object getTranspositionTableKey() {
        return null;
    }

    @Override
    public boolean isTranspositionTableUsed() {
        return false;
    }

    @Override
    public boolean isNullHeuristicOn() {
        return false;
    }
}
