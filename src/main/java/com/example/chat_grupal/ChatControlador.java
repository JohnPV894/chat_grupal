package com.example.chat_grupal;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.ScrollPane;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;

public class ChatControlador {

    @FXML private VBox mensajesBox;
    @FXML private TextField campoMensaje;
    @FXML private ScrollPane scrollPane;

    private ClienteConexion conexion;
    private String nombreUsuario;

    @FXML
    public void initialize() {
        conexion = new ClienteConexion(this::procesarMensaje);
    }

    public void setNombreUsuario(String nombre) {
        this.nombreUsuario = nombre;
        conexion.setNombreUsuario(nombre);
    }

    @FXML
    private void enviar() {
        if (!campoMensaje.getText().isEmpty()) {
            String mensaje = nombreUsuario + ": " + campoMensaje.getText();
            conexion.enviar(mensaje);
            campoMensaje.clear();
        }
    }

    private void procesarMensaje(String msg) {
        Platform.runLater(() -> {
            if (msg.startsWith("SYSTEM:")) {
                agregarMensaje(msg.substring(7), true, false);
            } else {
                // Verificamos si el mensaje es nuestro comparando con el nombre
                String[] partes = msg.split(": ", 2);
                boolean esMio = partes.length > 0 && partes[0].equals(nombreUsuario);
                String contenido = partes.length > 1 ? partes[1] : msg;
                agregarMensaje(contenido, false, esMio);
            }
        });
    }

    private void agregarMensaje(String texto, boolean esSistema,boolean esMio) {
        HBox contenedor = new HBox();
        VBox burbuja = new VBox();
        Label label = new Label(texto);

        label.setWrapText(true);
        burbuja.getChildren().add(label);

        // Definimos estilos básicos
        if (esSistema) {
            burbuja.getStyleClass().add("msg-sistema");
            contenedor.setAlignment(Pos.CENTER);
        } else {
            if (esMio){
                burbuja.getStyleClass().add("msg-propio");
            }else {
                burbuja.getStyleClass().add("msg-ajeno");
            }
            contenedor.setAlignment(Pos.CENTER_LEFT);
        }

        contenedor.setPadding(new Insets(5, 10, 5, 10));
        contenedor.getChildren().add(burbuja);

        mensajesBox.getChildren().add(contenedor);

        // Auto-scroll hacia abajo
        mensajesBox.heightProperty().addListener((obs, oldVal, newVal) -> {
            scrollPane.setVvalue(1.0);
        });
    }
}