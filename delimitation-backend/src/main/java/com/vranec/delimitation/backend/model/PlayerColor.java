package com.vranec.delimitation.backend.model;

public enum PlayerColor {
    RED, BLUE;

    public PlayerColor otherPlayer() {
        return this == RED ? BLUE : RED;
    }

    public AreaColor toAreaColor() {
        return this == RED ? AreaColor.RED : AreaColor.BLUE;
    }
}
