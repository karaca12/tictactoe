package com.karacamehmet.tictactoe;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


public class TicTacToeGame {
    private static char[][] board;
    private static boolean isSinglePlayer;
    private static char player;
    private static char currentPlayer;
    private static int xScore = 0;
    private static int oScore = 0;
    private static final Button[][] buttons = new Button[3][3];
    private static Text turnText;
    private static Text scoreText;
    private static final Random random = new Random();
    private static Stage primaryStage;

    public static void startGame(Stage stage, boolean isMultiplayer) {
        startGame(stage, isMultiplayer, 'X');
    }

    public static void startGame(Stage stage, boolean isMultiplayer, char playerSymbol) {
        primaryStage = stage;
        board = new char[3][3];
        isSinglePlayer = !isMultiplayer;
        player = playerSymbol;
        currentPlayer = 'X';
        turnText = new Text("X's turn");
        scoreText = new Text("Score - X: " + xScore + " O: " + oScore);

        GridPane grid = getGridPane();

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> handleBackButton());

        HBox topLayout = new HBox(20, turnText, backButton);
        topLayout.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20);
        layout.getChildren().addAll(topLayout, grid, scoreText);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        if (isSinglePlayer && currentPlayer != player) {
            computerMove();
        }

    }

    private static GridPane getGridPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new Button("");
                buttons[i][j].setMinSize(100, 100);
                int cellRow = i;
                int cellColumn = j;
                buttons[i][j].setOnAction(e -> handleMove(buttons[cellRow][cellColumn], cellRow, cellColumn));
                grid.add(buttons[i][j], j, i);
            }
        }
        return grid;
    }

    private static void handleBackButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Are you sure you want to go back?");
        alert.setContentText("All progress will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Main.showPrimaryStage(primaryStage);
        }
    }

    private static void handleMove(Button cell, int row, int column) {
        if (board[row][column] == '\0' && currentPlayer == (isSinglePlayer ? player : currentPlayer)) {
            board[row][column] = currentPlayer;
            cell.setText(String.valueOf(currentPlayer));
            if (checkWin()) {
                updateScore(currentPlayer);
                gameOver(currentPlayer + " wins!");
                resetGame();
            } else if (isBoardFull()) {
                gameOver("It's a tie!");
                resetGame();
            } else {
                switchPlayer();
                updateBoard();
                if (isSinglePlayer && currentPlayer != player) {
                    computerMove();
                }
            }
        }
    }

    public static void gameOver(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Game Over");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void resetGame() {
        currentPlayer = 'X';
        turnText = new Text("X's turn");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '\0';
            }
        }
        updateBoard();
    }

    private static void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        turnText.setText(currentPlayer + "'s turn");
    }

    private static boolean checkWin() {
        //Columns and rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != '\0' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) return true;
            if (board[0][i] != '\0' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) return true;
        }
        //Diagonals
        if (board[0][0] != '\0' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) return true;
        if (board[0][2] != '\0' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) return true;
        return false;
    }

    private static boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') return false;
            }
        }
        return true;
    }

    private static void updateBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != '\0') {
                    buttons[i][j].setText(String.valueOf(board[i][j]));
                } else {
                    buttons[i][j].setText("");
                }
            }
        }
    }

    private static void updateScore(char player) {
        if (player == 'X') {
            xScore++;
        } else {
            oScore++;
        }
        scoreText.setText("Score - X: " + xScore + " O: " + oScore);
    }

    private static void computerMove() {
        if (random.nextInt(10) < 7) {
            int[] bestMove = findBestMove();
            board[bestMove[0]][bestMove[1]] = currentPlayer;
        } else {
            makeRandomMove();
        }
        updateBoard();
        if (checkWin()) {
            updateScore(currentPlayer);
            gameOver(currentPlayer + " wins!");
            resetGame();
        } else if (isBoardFull()) {
            gameOver("It's a tie!");
            resetGame();
        } else {
            switchPlayer();
        }
    }

    private static void makeRandomMove() {
        List<int[]> availableMoves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    availableMoves.add(new int[]{i, j});
                }
            }
        }
        int[] move = availableMoves.get(random.nextInt(availableMoves.size()));
        board[move[0]][move[1]] = currentPlayer;
    }

    private static int[] findBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] move = new int[2];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    board[i][j] = currentPlayer;
                    int score = minimax(board, 0, false);
                    board[i][j] = '\0';
                    if (score > bestScore) {
                        bestScore = score;
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }
        return move;
    }

    private static int minimax(char[][] board, int depth, boolean isMaximizing) {
        if (checkWin()) {
            return isMaximizing ? -1 : 1;
        }
        if (isBoardFull()) {
            return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '\0') {
                        board[i][j] = currentPlayer;
                        int score = minimax(board, depth + 1, false);
                        board[i][j] = '\0';
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '\0') {
                        board[i][j] = (currentPlayer == 'X') ? 'O' : 'X';
                        int score = minimax(board, depth + 1, true);
                        board[i][j] = '\0';
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

}
