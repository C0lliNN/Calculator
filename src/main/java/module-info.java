module Calculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires  java.datatransfer;
    requires java.desktop;

    opens com.raphaelcollin.calculator to javafx.fxml;
    exports com.raphaelcollin.calculator;
}