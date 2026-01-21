package com.example.chat_grupal;

import java.io.*;
import java.net.*;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatServer {

    private static final int PUERTO = 8080;
    private static final Set<PrintWriter> clientes =
            ConcurrentHashMap.newKeySet();
    private static final AtomicInteger contador = new AtomicInteger(0);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PUERTO);
        System.out.println("Servidor chat iniciado");

        while (true) {
            Socket socket = serverSocket.accept();
            int id = contador.incrementAndGet();
            new Thread(new ClienteHandler(socket, id)).start();
        }
    }

    static class ClienteHandler implements Runnable {
        Socket socket;
        int id;
        PrintWriter salida;

        ClienteHandler(Socket socket, int id) {
            this.socket = socket;
            this.id = id;
        }

        public void run() {
            try (
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(
                            socket.getOutputStream(), true)
            ) {
                salida = out;
                clientes.add(out);

                out.println("SYSTEM:Bienvenido Cliente #" + id);
                broadcast("SYSTEM:Cliente #" + id + " se ha conectado");

                String msg;
                while ((msg = in.readLine()) != null) {
                    broadcast("Cliente #" + id + ":" + msg);
                }
            } catch (IOException ignored) {
            } finally {
                clientes.remove(salida);
                broadcast("SYSTEM:Cliente #" + id + " se ha desconectado");
            }
        }

        void broadcast(String msg) {
            for (PrintWriter pw : clientes) pw.println(msg);
        }
    }
}
