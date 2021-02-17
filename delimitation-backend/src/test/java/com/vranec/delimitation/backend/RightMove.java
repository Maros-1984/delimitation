package com.vranec.delimitation.backend;

import com.vranec.delimitation.backend.model.Move;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class RightMove extends Move {
    public RightMove(int x, int y) {
        setAreaX(x).setAreaY(y).setRight(true);
    }
}
