package com.vranec.delimitation.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Area {
    private int areaX;
    private int areaY;
    private FullGameResponse game;

    public void unfill() {
        game.descreaseScoreFor(game.getAreas().get(areaY).get(areaX));
        game.getAreas().get(areaY).set(areaX, AreaColor.EMPTY);
    }

    public void fill() {
        if (isInsideGame() && isNotFilled()) {
            AreaColor color = game.getPlayerOnMove().toAreaColor();
            game.getAreas().get(areaY).set(areaX, color);
            game.increaseScoreFor(color);
        }
    }

    public boolean isInsideGame() {
        return areaX >= 0 && areaY >= 0 && areaY < game.getAreas().size() && areaX < game.getAreas().get(0).size();
    }

    public Set<Area> fillNeighbours() {
        Set<Area> allNeighbors = getNeighbours();
        return allNeighbors.stream()
                .filter(Area::isNotFilled)
                .peek(Area::fill)
                .collect(Collectors.toSet());
    }

    private Set<Area> getNeighbours() {
        Set<Area> neighbours = new HashSet<>();

        Move bottomBorder = new Move().setAreaX(areaX).setAreaY(areaY).setBottom(true);
        if (!game.getMoves().contains(bottomBorder)) {
            Area bottomNeighbour = new Area(areaX, areaY + 1, game);
            if (bottomNeighbour.isInsideGame() && bottomNeighbour.isNotFilled()) {
                neighbours.add(bottomNeighbour);
            }
        }

        Move topBorder = new Move().setAreaX(areaX).setAreaY(areaY - 1).setBottom(true);
        if (!game.getMoves().contains(topBorder)) {
            Area topNeighbour = new Area(areaX, areaY - 1, game);
            if (topNeighbour.isInsideGame() && topNeighbour.isNotFilled()) {
                neighbours.add(topNeighbour);
            }
        }

        Move rightBorder = new Move().setAreaX(areaX).setAreaY(areaY).setRight(true);
        if (!game.getMoves().contains(rightBorder)) {
            Area rightNeighbour = new Area(areaX + 1, areaY, game);
            if (rightNeighbour.isInsideGame() && rightNeighbour.isNotFilled()) {
                neighbours.add(rightNeighbour);
            }
        }

        Move leftBorder = new Move().setAreaX(areaX - 1).setAreaY(areaY).setRight(true);
        if (!game.getMoves().contains(leftBorder)) {
            Area leftNeighbour = new Area(areaX - 1, areaY, game);
            if (leftNeighbour.isInsideGame() && leftNeighbour.isNotFilled()) {
                neighbours.add(leftNeighbour);
            }
        }

        return neighbours;
    }

    public boolean isNotFilled() {
        return game.getAreas().get(areaY).get(areaX) == AreaColor.EMPTY;
    }
}
