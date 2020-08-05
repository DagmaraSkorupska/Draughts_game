package com.kodilla.pawns;

public enum Pawn {
    PAWN, QUEEN;

    public boolean isPawn(){
        return this==PAWN;
    }

    public boolean isQueen(){
        return this==QUEEN;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
