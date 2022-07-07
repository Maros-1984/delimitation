package com.vranec.delimitation.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class FullGameResponse {

    private List<List<AreaColor>> areas;
    private Set<Move> moves = new HashSet<>();
    private Move lastMove;
    private Set<Move> possibleMoves;
    private String gameId;
    private PlayerColor yourPlayerColor;
    private PlayerColor playerOnMove;
    private PlayerColor computerPlayer;
    private boolean over;
    private Map<AreaColor, Integer> score = new HashMap<>();
    @JsonIgnore
    private transient Set<Area> lastFilledAreas;

    public boolean connectsTo(Set<Move> moves) {
        return moves.stream().anyMatch(this::connectsTo);
    }

    public boolean connectsTo(Move move) {
        return moves.contains(move) || move.isOriginalBorder(getWidth(), getHeight());
    }

    @JsonIgnore
    public int getHeight() {
        return getAreas().size();
    }

    @JsonIgnore
    public int getWidth() {
        return getAreas().get(0).size();
    }

    @JsonIgnore
    public void increaseScoreFor(AreaColor color) {
        int oldScore = score.getOrDefault(color, 0);
        score.put(color, oldScore + 1);
    }

    @JsonIgnore
    public void descreaseScoreFor(AreaColor color) {
        int oldScore = score.getOrDefault(color, 0);
        score.put(color, oldScore - 1);
    }

    @JsonIgnore
    public boolean isComputerOnMove() {
        return playerOnMove == computerPlayer;
    }

    public void make(Move move) {
        moves.add(move);
        lastMove = move;
        lastFilledAreas = new HashSet<>();

        possibleMoves = removeNonsenseMoves(computePossibleMoves(move));
        playerOnMove = playerOnMove.otherPlayer();
        if (possibleMoves.isEmpty()) {
            over = true;
        }
    }

    private Set<Move> computePossibleMoves(Move move) {
        if (move == null) {
            return genenerateNewBaseMoves();
        }
        Set<Move> firstSideNeighbourMoves = move.getFirstSideNeighbourMoves();
        Set<Move> secondSideNeighbourMoves = move.getSecondSideNeighbourMoves();

        boolean connectsToFirstMoves = connectsTo(firstSideNeighbourMoves);
        boolean connectsToSecondMoves = connectsTo(secondSideNeighbourMoves);

        if (connectsToFirstMoves && !connectsToSecondMoves) {
            return secondSideNeighbourMoves;
        } else if (!connectsToFirstMoves && connectsToSecondMoves) {
            return firstSideNeighbourMoves;
        } else if (!connectsToFirstMoves) {
            return new HashSet<>();
        }

        if (move.isBetweenFreeAreas(this)) {
            fillAreasWithPlayerColor(move);
        }
        return genenerateNewBaseMoves();
    }

    private void fillAreasWithPlayerColor(Move move) {
        Area firstArea = move.getFirstArea(this);
        int firstAreaFillSize = computeAreaFillSize(firstArea);
        Area secondArea = move.getSecondArea(this);
        int secondAreaFillSize = computeAreaFillSize(secondArea);
        Area areaToFill;
        if (firstAreaFillSize > secondAreaFillSize) {
            areaToFill = secondArea;
        } else {
            areaToFill = firstArea;
        }
        lastFilledAreas = fillArea(areaToFill, Integer.MAX_VALUE);
    }

    private int computeAreaFillSize(Area area) {
        return computeAreaFillSize(area, Integer.MAX_VALUE);
    }

    private boolean isNoNonsenseMove(Move move) {
        Set<Move> firstSideNeighbourMoves = move.getFirstSideNeighbourMoves();
        Set<Move> secondSideNeighbourMoves = move.getSecondSideNeighbourMoves();

        boolean connectsToFirstMoves = connectsTo(firstSideNeighbourMoves);
        boolean connectsToSecondMoves = connectsTo(secondSideNeighbourMoves);

        if (connectsToFirstMoves && !connectsToSecondMoves) {
            return true;
        } else if (!connectsToFirstMoves && connectsToSecondMoves) {
            return true;
        }

        moves.add(move);
        Area firstArea = move.getFirstArea(this);
        int firstAreaFillSize = computeAreaFillSize(firstArea, 3);
        Area secondArea = move.getSecondArea(this);
        int secondAreaFillSize = computeAreaFillSize(secondArea, 3);
        moves.remove(move);
        return firstAreaFillSize > 2 && secondAreaFillSize > 2;
    }

    private Set<Move> removeNonsenseMoves(Set<Move> moves) {
        return moves.stream().filter(this::isNoNonsenseMove).collect(Collectors.toSet());
    }


    private Set<Area> fillArea(Area areaToFill, int maxAreaSizeToFill) {
        Set<Area> areasFilled = new HashSet<>();
        areaToFill.fill();
        areasFilled.add(areaToFill);
        fillAreas(areasFilled, maxAreaSizeToFill);
        return areasFilled;
    }

    private int computeAreaFillSize(Area area, int maxAreaSizeToFill) {
        Set<Area> areasFilled = fillArea(area, maxAreaSizeToFill);
        unfillAreas(areasFilled);
        return areasFilled.size();
    }

    public Set<Move> genenerateNewBaseMoves() {
        Set<Move> moves = new HashSet<>();

        for (int rowIndex = 0; rowIndex < getHeight(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < getWidth(); columnIndex++) {
                if ((rowIndex == 0 || rowIndex == getHeight() - 1) && columnIndex < getWidth() - 1) {
                    moves.add(new Move()
                            .setAreaY(rowIndex)
                            .setAreaX(columnIndex)
                            .setRight(true));
                }
                if ((columnIndex == 0 || columnIndex == getWidth() - 1) && rowIndex < getHeight() - 1) {
                    moves.add(new Move()
                            .setAreaY(rowIndex)
                            .setAreaX(columnIndex)
                            .setBottom(true));
                }
            }
        }

        this.moves.forEach(move -> {
            Set<Move> neighbourMoves = move.getFirstSideNeighbourMoves();
            neighbourMoves.addAll(move.getSecondSideNeighbourMoves());
            moves.addAll(neighbourMoves);
        });

        return moves.stream()
                .filter(move -> !this.moves.contains(move))
                .filter(move -> move.isBetweenFreeAreas(this))
                .collect(Collectors.toSet());
    }

    public void unfillAreas(Set<Area> areasFilled) {
        areasFilled.forEach(Area::unfill);
    }

    private void fillAreas(Set<Area> areasFilled, int maxAreaSizeToFill) {
        Set<Area> newAreasFilled = new HashSet<>();
        if (areasFilled.size() >= maxAreaSizeToFill) {
            return;
        }
        areasFilled.forEach(area -> {
            Set<Area> filledNeighbours = area.fillNeighbours();
            newAreasFilled.addAll(filledNeighbours);
        });
        if (newAreasFilled.isEmpty()) {
            return;
        }
        areasFilled.addAll(newAreasFilled);
        fillAreas(areasFilled, maxAreaSizeToFill);
    }

    @JsonIgnore
    public PlayerColor getHumanPlayer() {
        return computerPlayer.otherPlayer();
    }

    public Set<Move> generatePossibleMoves() {
        return removeNonsenseMoves(computePossibleMoves(lastMove));
    }

    @JsonIgnore
    public FullGameResponse getCopy() {
        FullGameResponse copy = new FullGameResponse();
        copy.setPlayerOnMove(playerOnMove);
        copy.setOver(over);
        copy.setAreas(areas.stream().map(ArrayList::new).collect(Collectors.toList()));
        copy.setMoves(new HashSet<>(moves));
        copy.setLastMove(lastMove);
        copy.setPossibleMoves(new HashSet<>(possibleMoves));
        copy.setGameId(gameId);
        copy.setYourPlayerColor(yourPlayerColor);
        copy.setPlayerOnMove(playerOnMove);
        copy.setComputerPlayer(computerPlayer);
        copy.setScore(new HashMap<>(score));
        return copy;
    }
}
