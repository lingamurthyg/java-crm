module javacrm {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;
    requires java.logging;

    opens javacrm to javafx.fxml;
    opens DAO to javafx.fxml;
    opens Utilities to javafx.fxml;
    opens View to javafx.fxml;
    opens Model to javafx.fxml;
    opens Controller to javafx.fxml;

    exports javacrm;
    exports DAO;
    exports Utilities;
    exports Model;
    exports Controller;
}
