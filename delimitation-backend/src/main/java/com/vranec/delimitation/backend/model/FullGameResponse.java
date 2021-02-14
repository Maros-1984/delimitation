package com.vranec.delimitation.backend.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class FullGameResponse {
    private List<List<AreaColor>> areas;
    private List<PossibleMove> possibleMoves;
    private String gameId;
    private PlayerColor yourPlayerColor;
    private PlayerColor playerOnMove;
}
