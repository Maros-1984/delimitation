package com.vranec.delimitation.backend.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
public class FullGameResponse {
    private List<List<AreaColor>> areas;
    private Set<Move> moves = new HashSet<>();
    private Set<Move> possibleMoves;
    private String gameId;
    private PlayerColor yourPlayerColor;
    private PlayerColor playerOnMove;

    public boolean connectsTo(Set<Move> moves) {
        return moves.stream().anyMatch(this::connectsTo);
    }

    public boolean connectsTo(Move move) {
        return moves.contains(move) || move.isOriginalBorder(areas.get(0).size(), areas.size());
    }
}
