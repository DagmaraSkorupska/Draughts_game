package com.kodilla.pawns;

public enum PawnColor {
    WHITE, BLACK;

    public boolean isWhite(){
        return this==WHITE;
    }

    public boolean isBlack(){
        return this==BLACK;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
