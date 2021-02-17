package com.vranec.delimitation.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(exclude = "color")
public class Move {
    private int areaX;
    private int areaY;
    private Boolean bottom;
    private Boolean right;
    private PlayerColor color;

    @JsonIgnore
    public Set<Move> getFirstSideNeighbourMoves() {
        Set<Move> result = new HashSet<>();
        if (Boolean.TRUE.equals(right)) {
            result.add(new Move().setAreaX(areaX).setAreaY(areaY - 1).setBottom(true));
            result.add(new Move().setAreaX(areaX + 1).setAreaY(areaY - 1).setBottom(true));
            result.add(new Move().setAreaX(areaX).setAreaY(areaY - 1).setRight(true));
        } else {
            result.add(new Move().setAreaX(areaX - 1).setAreaY(areaY).setBottom(true));
            result.add(new Move().setAreaX(areaX - 1).setAreaY(areaY).setRight(true));
            result.add(new Move().setAreaX(areaX - 1).setAreaY(areaY + 1).setRight(true));
        }

        return result;
    }

    @JsonIgnore
    public Set<Move> getSecondSideNeighbourMoves() {
        Set<Move> result = new HashSet<>();
        if (Boolean.TRUE.equals(right)) {
            result.add(new Move().setAreaX(areaX).setAreaY(areaY).setBottom(true));
            result.add(new Move().setAreaX(areaX + 1).setAreaY(areaY).setBottom(true));
            result.add(new Move().setAreaX(areaX).setAreaY(areaY + 1).setRight(true));
        } else {
            result.add(new Move().setAreaX(areaX + 1).setAreaY(areaY).setBottom(true));
            result.add(new Move().setAreaX(areaX).setAreaY(areaY).setRight(true));
            result.add(new Move().setAreaX(areaX).setAreaY(areaY + 1).setRight(true));
        }

        return result;
    }

    @JsonIgnore
    public boolean isOriginalBorder(int width, int height) {
        return areaX < 0 || areaY < 0 || areaX >= width || areaY >= height;
    }

    @JsonIgnore
    public Area getFirstArea(FullGameResponse game) {
        return new Area(areaX, areaY, game);
    }

    @JsonIgnore
    public Area getSecondArea(FullGameResponse game) {
        if (Boolean.TRUE.equals(right)) {
            return new Area(areaX + 1, areaY, game);
        } else {
            return new Area(areaX, areaY + 1, game);
        }
    }

    public boolean isBetweenFreeAreas(FullGameResponse game) {
        return isNotFilled(getFirstArea(game)) && isNotFilled(getSecondArea(game));
    }

    private boolean isNotFilled(Area area) {
        return area.isInsideGame() && area.isNotFilled();
    }
}
