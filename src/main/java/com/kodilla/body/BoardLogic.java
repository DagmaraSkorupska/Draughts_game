package com.kodilla.body;

import com.kodilla.board.Coordinates;
import com.kodilla.pawns.PawnClass;
import com.kodilla.pawns.PawnColor;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


public class BoardLogic {
    private static GridPane gridPane = new GridPane();
    private static Image lightMove = new Image("possibility.png");

    public BoardLogic(){
        createBoardBacground();
        generateEmptyBoard();
    }

    public static GridPane getGridPane() {
        return gridPane;
    }

    private void createBoardBacground(){
        Image background = new Image("board.png");
        BackgroundSize backgroundSize = new BackgroundSize(750, 750, false, false, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        gridPane.setBackground(new Background(backgroundImage));
    }

    private void generateEmptyBoard(){

        for(int i = 0; i < 8 ; i++ ) {
            ColumnConstraints column = new ColumnConstraints(80);
            column.setHgrow(Priority.ALWAYS);
            column.setHalignment(HPos.CENTER);
            gridPane.getColumnConstraints().add(column);

            RowConstraints row = new RowConstraints(82);
            row.setVgrow(Priority.ALWAYS);
            row.setValignment(VPos.CENTER);
            gridPane.getRowConstraints().add(row);
        }

        gridPane.setPadding(new Insets(0,0,0,50));
        gridPane.setHgap(2.5);
        gridPane.setVgap(0);
    }

    public static void addPawn(Coordinates coordinates, PawnClass pawn) {
        gridPane.add(pawn.getImage() , coordinates.getX(), coordinates.getY());
    }

    public static void addLightPawn(Coordinates coordinates, PawnClass pawn) {
        gridPane.add(pawn.getLightImage() , coordinates.getX(), coordinates.getY());
    }

    public static void addLightMove(Coordinates coordinates) {
        gridPane.add( new ImageView(lightMove), coordinates.getX(), coordinates.getY());
    }

    public static void removePawn(Coordinates coordinates) {
        gridPane.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == coordinates.getX() && GridPane.getRowIndex(node) == coordinates.getY());
    }


}
