package com.vranec.delimitation.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude = "game")
public class Area {
    private int areaX;
    private int areaY;
    private FullGameResponse game;

    public void unfill() {
        game.descreaseScoreFor(game.getAreas()[areaY][areaX]);
        game.getAreas()[areaY][areaX] = AreaColor.EMPTY;
    }

    public void fill() {
        if (isInsideGame() && isNotFilled()) {
            AreaColor color = game.getPlayerOnMove().toAreaColor();
            game.getAreas()[areaY][areaX] = color;
            game.increaseScoreFor(color);
        }
    }

    public boolean isInsideGame() {
        return areaX >= 0 && areaY >= 0 && areaY < game.getHeight() && areaX < game.getWidth();
    }

    public Stream<Area> fillNeighbours() {
        Area[] allNeighbors = getNeighbours();
        return Arrays.stream(allNeighbors)
                .filter(Objects::nonNull)
                .filter(Area::isNotFilled)
                .peek(Area::fill);
    }

    private Area[] getNeighbours() {
        Area[] neighbours = new Area[4];
        int count = 0;

        Move bottomBorder = new Move().setAreaX(areaX).setAreaY(areaY).setBottom(true);
        if (!game.getMoves().contains(bottomBorder)) {
            Area bottomNeighbour = new Area(areaX, areaY + 1, game);
            if (bottomNeighbour.isInsideGame() && bottomNeighbour.isNotFilled()) {
                neighbours[count++] = bottomNeighbour;
            }
        }

        Move topBorder = new Move().setAreaX(areaX).setAreaY(areaY - 1).setBottom(true);
        if (!game.getMoves().contains(topBorder)) {
            Area topNeighbour = new Area(areaX, areaY - 1, game);
            if (topNeighbour.isInsideGame() && topNeighbour.isNotFilled()) {
                neighbours[count++] = topNeighbour;
            }
        }

        Move rightBorder = new Move().setAreaX(areaX).setAreaY(areaY).setRight(true);
        if (!game.getMoves().contains(rightBorder)) {
            Area rightNeighbour = new Area(areaX + 1, areaY, game);
            if (rightNeighbour.isInsideGame() && rightNeighbour.isNotFilled()) {
                neighbours[count++] = rightNeighbour;
            }
        }

        Move leftBorder = new Move().setAreaX(areaX - 1).setAreaY(areaY).setRight(true);
        if (!game.getMoves().contains(leftBorder)) {
            Area leftNeighbour = new Area(areaX - 1, areaY, game);
            if (leftNeighbour.isInsideGame() && leftNeighbour.isNotFilled()) {
                neighbours[count] = leftNeighbour;
            }
        }

        return neighbours;
    }

    public boolean isNotFilled() {
        return game.getAreas()[areaY][areaX] == AreaColor.EMPTY;
    }
}
