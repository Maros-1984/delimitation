package com.vranec.delimitation.backend;

import com.vranec.delimitation.backend.model.Move;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class BottomMove extends Move {
    public BottomMove(int x, int y) {
        setAreaX(x).setAreaY(y).setBottom(true);
    }
}
