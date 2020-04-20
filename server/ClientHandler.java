package server;

import network.message.CommandMessage;
import network.message.Message;
import network.message.MessageReceiver;
import network.message.MessageSender;
import network.storage.Storage;
import network.storageCommands.Exit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {
    private final Socket client;
    private Storage storage;
    private BufferedReader storageInput;
    private static Path clientsPath;
    private Path clientPath;
    private String clientName;
    private boolean isDisconnected = false;
    private final Logger logger;

    private MessageReceiver input;
    private MessageSender output;

    static {
        try {
            clientsPath = Paths.get(
                    Paths.get(Server.class.getProtectionDomain()
                            .getCodeSource().getLocation().toURI()).getParent() + "/clients"
            );
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public ClientHandler(Socket clientSocket, Logger logger) throws IOException {
        client = clientSocket;
        this.logger = logger;

        input = new ServerMessageReceiver(client, logger);
        output = new ServerMessageSender(client, logger);

        try {
            clientName = input.receiveMessage().content;

            logger.log(Level.INFO, "Server: initialization for client " + clientName);

            clientPath = Paths.get(clientsPath + "/" + clientName);

            Files.createDirectories(clientPath);

            storage = new Storage(clientPath, "Objects");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString());
            disconnect(e);
        }

    }

    @Override
    public void run() {
        if (!isDisconnected) startDialogue();
    }

    private void startDialogue() {
        try {
            while (true) {
                final CommandMessage message = (CommandMessage) input.receiveMessage();

                if (message.command instanceof Exit)
                    disconnect(null);

                storage.apply(message.command, true);

                storageInput = new BufferedReader(new StringReader(storage.getInputBuffer().toString()));
                final String answer = storageInput.lines().reduce((s1, s2) -> s1 + "\n" + s2).orElse("");

                storage.getInputBuffer().replace(0, storage.getInputBuffer().length(), "");

                output.sendMessage(new Message("response:" + answer));
            }
        } catch (Exception e) {
            disconnect(e);
        }
    }

    private void disconnect(Exception ex) {
        if (ex != null) logger.log(Level.WARNING, ex.getMessage());
        try {
            isDisconnected = true;

            if (storage != null) {
                logger.log(Level.INFO, "Server: saving data for the client " + clientName);
                storage.save();
            }

            client.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        logger.log(Level.INFO, "client: " + clientName + ": disconnected");

        if (ex == null) {
            logger.log(Level.INFO, "Server: finish");
        }
    }
}