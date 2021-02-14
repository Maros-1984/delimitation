package com.vranec.delimitation.backend.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PossibleMove {
    private int areaX;
    private int areaY;
    private boolean bottom;
    private boolean right;
}
