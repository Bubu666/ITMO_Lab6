package server;

import network.message.Message;
import network.message.MessageSender;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMessageSender implements MessageSender {
    private OutputStream sockOut;
    private String clientName;
    private final Logger logger;

    public ServerMessageSender(Socket client, Logger logger) throws IOException {
        this.logger = logger;
        sockOut = client.getOutputStream();
        clientName = client.getInetAddress().getHostName();
    }

    @Override
    public void sendMessage(Message message) throws IOException {
        sockOut.write(serializeMessage(message));
        sockOut.flush();

        logger.log(Level.INFO, "Server: sent message to client " + clientName);
    }
}
