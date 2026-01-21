package com.example.chat_grupal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClienteChatFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/chat_grupal/chat.fxml")
        );

        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(
                getClass().getResource("/com/example/chat_grupal/chat.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.setTitle("Chat Grupal");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
