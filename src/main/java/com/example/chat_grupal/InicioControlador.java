package com.example.chat_grupal;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class InicioControlador {

    @FXML private TextField campoNombre;

    @FXML
    private void ingresarChat() {
        String nombre = campoNombre.getText().trim();
        
        if (nombre.isEmpty()) {
            mostrarAlerta("tu nombr");
            return;
        }

        try {
            // Cargamos la ventana del chat
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/chat_grupal/chat.fxml")
            );

            Stage stage = (Stage) campoNombre.getScene().getWindow();
            Scene scene = new Scene(loader.load(), 350, 650);
            scene.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("/com/example/chat_grupal/chat.css")).toExternalForm()
            );

            // Pasamos el nombre al controlador del chat
            ChatControlador chatControlador = loader.getController();
            chatControlador.setNombreUsuario(nombre);

            stage.setScene(scene);
            stage.setTitle("Chat Grupal - " + nombre);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al iniciar el chat");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Atención");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
