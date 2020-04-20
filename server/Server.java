package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    public final ServerSocket serverSocket;

    private final static Logger logger = Logger.getLogger(Server.class.getName());

    public Server() throws IOException {
        serverSocket = new ServerSocket(13338);
        logger.log(Level.INFO, "Server: " + InetAddress.getLocalHost());
    }

    public void start() {
        logger.log(Level.INFO, "Server: start working");
        try {
            while (true) {
                Socket client = serverSocket.accept();

                logger.log(Level.INFO, "client: " + client.getInetAddress().getHostName() + ": connection established");

                new ClientHandler(client, logger).run();
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            new Server().start();
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }
}