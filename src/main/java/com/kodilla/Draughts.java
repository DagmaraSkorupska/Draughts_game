package com.kodilla;

import com.kodilla.board.Board;
import com.kodilla.body.BoardLogic;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Draughts extends Application {
    BoardLogic design = new BoardLogic();
    Board board = new Board();

    public static void main(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(BoardLogic.getGridPane(), 751,650, Color.BLACK);
        scene.setOnMouseClicked(event -> board.readMouseEvent(event));

        primaryStage.setTitle("Draughts");
        primaryStage.getIcons().add(new Image("name.png"));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
