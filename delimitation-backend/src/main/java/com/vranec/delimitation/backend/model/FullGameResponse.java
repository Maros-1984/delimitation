package com.vranec.delimitation.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.*;

@Data
@Accessors(chain = true)
public class FullGameResponse {
    private List<List<AreaColor>> areas;
    private Set<Move> moves = new HashSet<>();
    private Set<Move> possibleMoves;
    private String gameId;
    private PlayerColor yourPlayerColor;
    private PlayerColor playerOnMove;
    private boolean over;
    private Map<AreaColor, Integer> score = new HashMap<>();

    public boolean connectsTo(Set<Move> moves) {
        return moves.stream().anyMatch(this::connectsTo);
    }

    public boolean connectsTo(Move move) {
        return moves.contains(move) || move.isOriginalBorder(getWidth(), getHeight());
    }

    @JsonIgnore
    public int getHeight() {
        return getAreas().size();
    }

    @JsonIgnore
    public int getWidth() {
        return getAreas().get(0).size();
    }

    @JsonIgnore
    public void increaseScoreFor(AreaColor color) {
        int oldScore = score.getOrDefault(color, 0);
        score.put(color, oldScore + 1);
    }

    @JsonIgnore
    public void descreaseScoreFor(AreaColor color) {
        int oldScore = score.get(color);
        score.put(color, oldScore - 1);
    }
}
