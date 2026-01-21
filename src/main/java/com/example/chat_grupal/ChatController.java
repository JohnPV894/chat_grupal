package com.example.chat_grupal;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.ScrollPane;

public class ChatController {

    @FXML private VBox mensajesBox;
    @FXML private TextField campoMensaje;
    @FXML private ScrollPane scrollPane;

    private ClienteConexion conexion;
    private String miId = "";

    @FXML
    public void initialize() {
        conexion = new ClienteConexion(this::procesarMensaje);
    }

    @FXML
    private void enviar() {
        if (!campoMensaje.getText().isEmpty()) {
            conexion.enviar(campoMensaje.getText());
            campoMensaje.clear();
        }
    }

    private void procesarMensaje(String msg) {
        if (msg.startsWith("SYSTEM:")) {
            agregarMensaje(msg.substring(7), false, true);
            if (msg.contains("Bienvenido")) {
                miId = msg.replace("Bienvenido ", "");
            }
            return;
        }

        String[] partes = msg.split(":", 2);
        boolean esMio = partes[0].equals(miId);
        agregarMensaje(partes[0] + ": " + partes[1], esMio, false);
    }

    private void agregarMensaje(String texto, boolean propio, boolean sistema) {
        HBox contenedor = new HBox();
        VBox burbuja = new VBox();

        burbuja.getStyleClass().add(
                sistema ? "msg-sistema" :
                        propio ? "msg-propio" : "msg-ajeno"
        );

        burbuja.getChildren().add(new javafx.scene.control.Label(texto));
        contenedor.getChildren().add(burbuja);

        contenedor.setAlignment(
                propio ? javafx.geometry.Pos.CENTER_RIGHT :
                        javafx.geometry.Pos.CENTER_LEFT
        );

        mensajesBox.getChildren().add(contenedor);

        mensajesBox.heightProperty().addListener((obs, oldVal, newVal) -> {
            scrollPane.setVvalue(1.0);
        });
    }
}
