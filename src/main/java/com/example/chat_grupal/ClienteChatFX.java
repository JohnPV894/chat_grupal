package com.example.chat_grupal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class ClienteChatFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/chat_grupal/inicio.fxml")
        );

        Scene scene = new Scene(loader.load(), 350, 300);

        stage.setScene(scene);
        stage.setTitle("Chat Grupal");
        stage.show();
    }

}
