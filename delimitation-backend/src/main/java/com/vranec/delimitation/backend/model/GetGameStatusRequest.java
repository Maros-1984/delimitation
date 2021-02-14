package com.vranec.delimitation.backend.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetGameStatusRequest {
    private String gameId;
    private PlayerColor playerAsking;
    private boolean differenceOnly;
}
