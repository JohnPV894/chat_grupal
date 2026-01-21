package com.example.chat_grupal;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {

    private static final int PUERTO = 8080;
    private static final Set<PrintWriter> clientes =
            ConcurrentHashMap.newKeySet();
    private static final Map<PrintWriter, String> nombresUsuarios = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PUERTO);
        System.out.println("Servidor chat iniciado");

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new ClienteHandler(socket)).start();
        }
    }

    static class ClienteHandler implements Runnable {
        Socket socket;
        PrintWriter salida;
        String nombreUsuario;

        ClienteHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                salida = out;
                clientes.add(out);

                // Leemos el primer mensaje que debe ser el nombre del usuario
                String primerMensaje = in.readLine();
                if (primerMensaje != null) {
                    broadcast("SYSTEM:" + nombreUsuario + " se ha conectado");
                    nombreUsuario = primerMensaje.split(":")[0];
                    nombresUsuarios.put(out, nombreUsuario);

                }

                String mensaje;
                while ((mensaje = in.readLine()) != null) {
                    broadcast(mensaje);
                }
            } catch (IOException ignored) {
            } finally {
                clientes.remove(salida);
                if (nombreUsuario != null) {
                    broadcast("SYSTEM:" + nombreUsuario + " se ha desconectado");
                    nombresUsuarios.remove(salida);
                }
            }
        }

        void broadcast(String msg) {
            for (PrintWriter pw : clientes) pw.println(msg);
        }
    }
}
