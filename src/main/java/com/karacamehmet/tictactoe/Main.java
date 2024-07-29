package com.karacamehmet.tictactoe;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        showPrimaryStage(primaryStage);
    }

    public static void showPrimaryStage(Stage primaryStage) {
        VBox layout = new VBox(20);
        Button singlePlayerButton = new Button("Single Player");
        Button multiplayerButton = new Button("Multiplayer");

        singlePlayerButton.setOnAction(e -> SinglePlayerMenu.show(primaryStage));
        multiplayerButton.setOnAction(e -> TicTacToeGame.startGame(primaryStage, true));

        layout.getChildren().addAll(singlePlayerButton, multiplayerButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}