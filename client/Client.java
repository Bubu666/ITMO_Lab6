package client;

import client.animation.WaitingForServerAnimation;
import network.message.CommandMessage;
import network.message.Message;
import network.message.MessageReceiver;
import network.message.MessageSender;
import network.storageCommands.Command;
import network.storageCommands.Exit;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;

public class Client {
    private static String clientName;

    private static Selector selector;
    private static SocketChannel channel;

    private static final LinkedList<Command> queue;

    private static boolean responseReceived = true;

    private static WaitingForServerAnimation waitAnimation;

    private static boolean itWaits = false;

    private static MessageReceiver clientInput;
    private static MessageSender clientOutput;

    private static final CommandListener commandListener;

    private static ConsoleThread consoleThread;

    static {
        System.out.print("\rInitializing");
        queue = new LinkedList<>();
        commandListener = new CommandListener();
    }

    private boolean notConnected;

    public Client() throws IOException {
        selector = Selector.open();
        channel = SocketChannel.open();

        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_CONNECT);

        clientInput = new ClientMessageReceiver(channel);
        clientOutput = new ClientMessageSender(channel);

        notConnected = false;

        new Thread(() -> {
            if (!itWaits)
                System.out.print("\rConnecting to the server");

            Thread.yield();

            try {
                channel.connect(new InetSocketAddress("localhost", 13338));
                selector.wakeup();
            } catch (IOException e) {
                notConnected = true;
            }
        }).start();


        run();
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            clientName = "";
            for (String arg: args) {
                clientName += arg;
            }
        }

        while (true) {
            try {
                new Client();
            } catch (IOException e) {
            } catch (Exception e) {
                break;
            }
        }

        System.exit(0);
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                selector.select(10000);

                final Iterator<SelectionKey> it = selector.selectedKeys().iterator();

                if (!it.hasNext() && notConnected)
                    return;

                while (it.hasNext()) {
                    final SelectionKey key = it.next();
                    it.remove();

                    if (!key.isValid()) continue;

                    if (key.isConnectable()) {
                        channel.finishConnect();

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            lostConnection();
                        }

                        if (waitAnimation != null) {
                            waitAnimation.interrupt();
                            waitAnimation = null;
                        }
                        System.out.print("\r                        ");
                        System.out.print("\rConnection established");

                        if (clientName == null)
                            clientName = channel.getLocalAddress().toString().split(":")[0].replace("/", "");

                        clientOutput.sendMessage(new Message(clientName));

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            lostConnection();
                        }
                        System.out.print("\r                           ");
                        System.out.print("\r> ");

                        responseReceived = true;

                        if (consoleThread == null) {
                            consoleThread = new ConsoleThread();
                            consoleThread.start();
                        }
                        continue;
                    }

                    if (key.isWritable()) {
                        if (!queue.isEmpty())
                            clientOutput.sendMessage(new CommandMessage("command", queue.poll()));

                        key.interestOps(SelectionKey.OP_READ);
                        continue;
                    }

                    if (key.isReadable()) {
                        String answer = clientInput.receiveMessage().content;

                        if (answer.startsWith("response:")) {
                            System.out.println(answer.replaceFirst("response:", ""));

                        } else {
                            System.out.print("\rServer: " + answer + "\n> ");
                        }
                        responseReceived = true;
                    }
                }
            } catch (IOException e) {
                lostConnection();
                return;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static void lostConnection() {
        itWaits = true;
        responseReceived = false;

        if (waitAnimation == null) {
            waitAnimation = new WaitingForServerAnimation();
            waitAnimation.start();
        }
    }

    private static void disconnect(Command lastCommand) {
        try {
            clientOutput.sendMessage(new CommandMessage("command", lastCommand));
            consoleThread.interrupt();
            channel.close();
            selector.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        System.exit(0);
    }

    private static class ConsoleThread extends Thread {
        public ConsoleThread() {
            super(() -> {
                while (!interrupted()) {
                    yield();

                    if (responseReceived) {

                        System.out.print("\r> ");
                        Command curCommand = commandListener.getCommand(System.in);

                        if (curCommand == null) {
                            continue;
                        }

                        responseReceived = false;

                        if (curCommand instanceof Exit)
                            disconnect(curCommand);

                        queue.add(curCommand);

                        channel.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
                        selector.wakeup();

                    }
                }
            });
        }

        public void cancel() {
            interrupt();
        }
    }
}