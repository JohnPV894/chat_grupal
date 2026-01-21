package com.example.chat_grupal;

import javafx.application.Platform;
import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class ClienteConexion {

    private PrintWriter salida;

    public ClienteConexion(Consumer<String> onMensaje) {
        try {
            Socket socket = new Socket("localhost", 8080);
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);

            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = entrada.readLine()) != null) {
                        String m = msg;
                        Platform.runLater(() -> onMensaje.accept(m));
                    }
                } catch (IOException ignored) {}
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviar(String msg) {
        salida.println(msg);
    }
}
