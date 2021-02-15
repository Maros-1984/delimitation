package com.vranec.delimitation.backend.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MakeMoveRequest {
    private String gameId;
    private Move move;
    private PlayerColor player;
}
