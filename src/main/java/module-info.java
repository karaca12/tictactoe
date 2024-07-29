module com.karacamehmet.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.karacamehmet.tictactoe to javafx.fxml;
    exports com.karacamehmet.tictactoe;
}