package com.karacamehmet.tictactoe;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SinglePlayerMenu {
    public static void show(Stage primaryStage) {
        Button xButton = new Button("X");
        Button oButton = new Button("O");

        xButton.setOnAction(e -> TicTacToeGame.startGame(primaryStage, false, 'X'));
        oButton.setOnAction(e -> TicTacToeGame.startGame(primaryStage, false, 'O'));

        VBox layout = new VBox();
        layout.getChildren().addAll(xButton, oButton);
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
