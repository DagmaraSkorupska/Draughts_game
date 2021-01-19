package com.kodilla.pawns;

import com.kodilla.board.Board;
import com.kodilla.board.Coordinates;

import java.util.HashSet;
import java.util.Set;

public class PawnMove {
    private final Coordinates coordinates;
    private final PawnClass pawn;

    private final Set<Coordinates> canGo = new HashSet<>();
    private final Set<Coordinates> canForce = new HashSet<>();
    private final Set<Coordinates> possibleTake = new HashSet<>();

    private boolean force = false;
    private Coordinates forceCoordinates = null;

    public PawnMove(Coordinates coordinates, PawnClass pawn) {
        this.coordinates = coordinates;
        this.pawn = pawn;

        calculateMove();
    }

    private void calculateMove(){
        if (pawn.getPawn().isPawn()){
            if(pawn.getColor().isBlack()){
                checkBottomLeft(true);
                checkBottomRight(true);
                checkUpLeft(false);
                checkUpRight(false);
            } else {
                checkUpLeft(true);
                checkUpRight(true);
                checkBottomLeft(false);
                checkBottomRight(false);
            }
        } else {
            checkUpLeft(true);
            checkUpRight(true);
            checkBottomLeft(true);
            checkBottomRight(true);
        }
    }

    private void checkUpLeft(boolean checkMove) {
        boolean checkUpLeft = true;
        force = false;

        for(int i = 1; i < 8; i++) {
            if(checkUpLeft) {
                checkUpLeft = checkCoordinates(new Coordinates(coordinates.getX() - i, coordinates.getY() - i), checkMove);
            }
        }
    }

    private void checkUpRight(boolean checkMove) {
        boolean checkUpRight = true;
        force = false;

        for(int i = 1; i < 8; i++) {
            if(checkUpRight) {
                checkUpRight = checkCoordinates(new Coordinates(coordinates.getX() + i, coordinates.getY() - i), checkMove);
            }
        }
    }

    private void checkBottomRight(boolean checkMove) {
        boolean checkBottomRight = true;
        force = false;

        for(int i = 1; i < 8; i++){
            if(checkBottomRight) {
                checkBottomRight = checkCoordinates(new Coordinates(coordinates.getX() + i, coordinates.getY() + i), checkMove);
            }
        }
    }

    private void checkBottomLeft(boolean checkMove) {
        boolean checkBottomLeft = true;
        force = false;

        for(int i = 1; i < 8 ; i++){
            if(checkBottomLeft){
                checkBottomLeft = checkCoordinates(new Coordinates(coordinates.getX() - i, coordinates.getY() + i), checkMove);
            }
        }

    }

    private boolean checkCoordinates(Coordinates coordinates, boolean checkMove){
        if(!coordinates.isCorrect()){
            return false;
        }

        if(Board.isEmpty(coordinates)) {
            if (!Board.theSame(coordinates, pawn.getColor()) && !force) {
                forceCoordinates = coordinates;
                force = true;
                return true;
            }
        } else {
            if((pawn.getColor().isWhite() && coordinates.getY() == 0 || pawn.getColor().isBlack() && coordinates.getY() == 7) && pawn.getPawn().isPawn()) {
                possibleTake.add(coordinates);
            }

            if(force) {
                force = false;

                canForce.add(coordinates);
                canForce.add(forceCoordinates);
            } else if(checkMove){
                canGo.add(coordinates);

                return pawn.getPawn().isQueen();
            }
        }
        return false;
    }

    public Set <Coordinates> getCanGo(){
        return canGo;
    }

    public Set<Coordinates> getCanForce() {
        return canForce;
    }

    public Set<Coordinates> getPossibleTake() {
        return possibleTake;
    }
}
