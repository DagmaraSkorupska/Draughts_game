package com.kodilla.board;

import com.kodilla.body.AiLogic;
import com.kodilla.body.BoardLogic;
import com.kodilla.body.EndGame;
import com.kodilla.pawns.Pawn;
import com.kodilla.pawns.PawnClass;
import com.kodilla.pawns.PawnColor;
import com.kodilla.pawns.PawnMove;
import javafx.concurrent.Task;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.*;

import static java.lang.Thread.sleep;

public class Board {
    private static HashMap<Coordinates, PawnClass> board = new HashMap<>();

    private boolean isMark = false;
    private boolean newKick = false;
    private Coordinates markCoordinates;

    private Set<Coordinates> possibleMark = new HashSet<>();
    private Set<Coordinates> possibleKick = new HashSet<>();
    private Set<Coordinates> possibleTake = new HashSet<>();

    private boolean isGameEnd = false;
    private int roundWithoutKick = 0;


    private boolean aiRound = false;
    private AiLogic computer = new AiLogic();

    public Board() {
        addPawn();
    }
    public static Map<Coordinates,PawnClass> getBoard() {
        return board;
    }




    private void addPawn() {
        board.put(new Coordinates(1,0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(3,0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(5,0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(7,0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(0,1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(2,1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(4,1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(6,1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(1,2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(3,2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(5,2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(7,2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));

        board.put(new Coordinates(0,5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(2,5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(4,5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(6,5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(1,6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(3,6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(5,6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(7,6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(0,7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(2,7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(4,7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(6,7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));

        for(Map.Entry<Coordinates,PawnClass> entry : board.entrySet()){
            BoardLogic.addPawn(entry.getKey(), entry.getValue());

        }
    }

    public void readMouseEvent(MouseEvent event) {
        if (aiRound) {
            return;
        }

        Coordinates eventCoordinates = new Coordinates((int) ((event.getX() - 5) / 85), (int) ((event.getY() - 5) / 85));

        if (isMark) {
            if (markCoordinates.equals(eventCoordinates) && !newKick) {
                notShowMark(markCoordinates);

                markCoordinates = null;
                isMark = false;
            } else if (possibleMark.contains(eventCoordinates)) {
                notShowMark(markCoordinates);
                movePawn(markCoordinates, eventCoordinates);
                markCoordinates = null;
                isMark = false;

                aiMove();
            } else if (possibleKick.contains(eventCoordinates) && !isEmpty(eventCoordinates)) {
                notShowMark(markCoordinates);

                if (!forcePawn(markCoordinates, eventCoordinates)) {
                    isMark = false;
                    newKick = false;
                    aiMove();
                } else {
                    newKick = true;
                    markCoordinates = eventCoordinates;
                }
            }
        } else if (eventCoordinates.isCorrect()) {
            if (isEmpty(eventCoordinates)) {
                if (getPawn(eventCoordinates).getColor().isWhite() && isPawn(eventCoordinates, PawnColor.WHITE)) {
                    isMark = true;
                    markCoordinates = eventCoordinates;
                    showMark(eventCoordinates);
                }
            }
        }
    }

    private void aiMove(){
        finish();

        Task<Void> computerSleep = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    sleep(1000);
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
                return null;
            }
        };

        computerSleep.setOnSucceeded(event -> {
            Coordinates moveCoordinates = computer.chooseMove(markCoordinates);
            notShowMark(markCoordinates);

            if(computer.beForceGo()){
                if(!forcePawn(markCoordinates, moveCoordinates)){
                    newKick = false;
                    aiRound = false;
                    markCoordinates = null;
                } else {
                    newKick =true;
                    markCoordinates = moveCoordinates;
                    aiMove();
                }
            } else {
                movePawn(markCoordinates, moveCoordinates);
                aiRound = false;
                markCoordinates = null;
            }
        });

        aiRound = true;
        computer.getData();

        if(!newKick) {
            markCoordinates = computer.choosePawn();
        }

        showMark(markCoordinates);

        new Thread(computerSleep).start();
    }

    private boolean isPawn(Coordinates coordinates, PawnColor color){
        Set<Coordinates> bePawn = new HashSet<>();

        for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
            if(entry.getValue().getColor() == color) {
                PawnMove pawnMove = new PawnMove(entry.getKey(), entry.getValue());

                if(pawnMove.getCanForce().size() > 0) {
                    bePawn.add(entry. getKey());
                }
            }
        }
        return bePawn.size() == 0 || bePawn.contains(coordinates);
    }

    private void movePawn(Coordinates oldCoordinates, Coordinates newCoordinates) {
        PawnClass pawn = getPawn(oldCoordinates);

        if (possibleTake.contains(newCoordinates)) {
            pawn = new PawnClass(Pawn.QUEEN, pawn.getColor());
        }

        BoardLogic.removePawn(oldCoordinates);
        BoardLogic.removePawn(newCoordinates);
        BoardLogic.addPawn(newCoordinates, pawn);

        board.remove(oldCoordinates);
        board.put(newCoordinates, pawn);
    }

    private boolean forcePawn(Coordinates oldCoordinates, Coordinates newCoordinates) {
        PawnClass pawn = getPawn(oldCoordinates);

        if (possibleTake.contains(newCoordinates)) {
            pawn = new PawnClass(Pawn.QUEEN, pawn.getColor());
        }

        Coordinates enemyCoordinates = getVs(newCoordinates);

        BoardLogic.removePawn(oldCoordinates);
        BoardLogic.removePawn(enemyCoordinates);
        BoardLogic.addPawn(newCoordinates, pawn);

        board.remove(oldCoordinates);
        board.remove(enemyCoordinates);
        board.put(newCoordinates, pawn);

        PawnMove pawnMove = new PawnMove(newCoordinates, pawn);

        if (pawnMove.getCanForce().size() > 0) {
            showNewForce(newCoordinates);
            return true;
        }
        return false;
    }

    private Coordinates getVs(Coordinates coordinates) {

        Coordinates checkUpLeft = new Coordinates(coordinates.getX() - 1, coordinates.getY() - 1);

        if (possibleKick.contains(checkUpLeft)) {
            return checkUpLeft;
        }

        Coordinates checkUpRight = new Coordinates(coordinates.getX() + 1, coordinates.getY() - 1);

        if (possibleKick.contains(checkUpRight)) {
            return checkUpRight;
        }

        Coordinates checkBottomLeft = new Coordinates(coordinates.getX() - 1, coordinates.getY() + 1);

        if (possibleKick.contains(checkBottomLeft)) {
            return checkBottomLeft;
        }

        Coordinates checkBottomRight = new Coordinates(coordinates.getX() + 1, coordinates.getY() + 1);

        if (possibleKick.contains(checkBottomRight)) {
            return checkBottomRight;
        }
        return null;
    }
    private void showMark(Coordinates coordinates) {
        PawnMove pawnMove = new PawnMove(coordinates, getPawn(coordinates));

        possibleMark = pawnMove.getCanGo();
        possibleKick = pawnMove.getCanForce();
        possibleTake = pawnMove.getPossibleTake();

        if (possibleKick.size() > 0) {
            possibleMark.clear();
        }

        possibleMark.forEach(this::showGo);
        possibleKick.forEach(this::showGo);

        showPawn(coordinates);
    }


    private void showNewForce(Coordinates coordinates) {
        PawnMove pawnMove = new PawnMove(coordinates, getPawn(coordinates));

        possibleMark.clear();
        possibleKick = pawnMove.getCanForce();
        possibleTake = pawnMove.getPossibleTake();

        possibleKick.forEach(this::showGo);

        showPawn(coordinates);
    }

    private void showPawn(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);
        BoardLogic.removePawn(coordinates);
        BoardLogic.addLightPawn(coordinates, pawn);
    }

    private void showGo(Coordinates coordinates) {
        BoardLogic.addLightMove(coordinates);
    }

    private void notShowMark(Coordinates coordinates) {
        possibleMark.forEach(this::notShowGo);
        possibleKick.forEach(this::notShowForce);

        notShowPawn(coordinates);
    }

    private void notShowPawn(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);
        BoardLogic.removePawn(coordinates);
        BoardLogic.addPawn(coordinates, pawn);
    }

    private void notShowGo(Coordinates coordinates) {
        BoardLogic.removePawn(coordinates);
    }

    private void notShowForce(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);

        if (pawn != null) {
            notShowPawn(coordinates);
        } else {
            notShowGo(coordinates);
        }
    }

    public static boolean isEmpty(Coordinates coordinates) {
        return getPawn(coordinates) != null;
    }

    public static boolean theSame(Coordinates coordinates, PawnColor color) {
        return getPawn(coordinates).getColor() == color;
    }

    public static PawnClass getPawn(Coordinates coordinates) {
        return board.get(coordinates);
    }

    public void finish() {
        Set<Coordinates> possibleMovesWhite = new HashSet<>();
        Set<Coordinates> possibleMovesBlack = new HashSet<>();
        int pawnWhiteCount = 0;
        int pawnBlackCount = 0;

        for (Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
            PawnMove moves = new PawnMove(entry.getKey(), entry.getValue());

            if (entry.getValue().getColor().isBlack()) {
                pawnBlackCount++;
                possibleMovesBlack.addAll(moves.getCanForce());
                possibleMovesBlack.addAll(moves.getCanGo());
            } else {
                pawnWhiteCount++;
                possibleMovesWhite.addAll(moves.getCanForce());
                possibleMovesWhite.addAll(moves.getCanGo());
            }
        }

        if(roundWithoutKick == 12) {
            isGameEnd = true;
            new EndGame("Draw. Maybe you try again?");
        } else if(possibleMovesWhite.size() == 0 || pawnWhiteCount <= 1) {
            isGameEnd = true;
            new EndGame("You loss. Maybe you try again?");
        } else if(possibleMovesBlack.size() == 0 || pawnBlackCount <= 1) {
            isGameEnd = true;
            new EndGame("You win! Congratulations! :)");
        }
    }

    public void readKeyboard(KeyEvent event) {
        if(event.getCode().equals(KeyCode.R) || event.getCode().equals(KeyCode.N)) {
            EndGame.restartApplication();
        }
    }
}