module com.example.chat_grupal {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.chat_grupal to javafx.fxml;
    exports com.example.chat_grupal;
}