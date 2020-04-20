package client;

import network.message.Message;
import network.message.MessageSender;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientMessageSender implements MessageSender {
    private SocketChannel channel;

    public ClientMessageSender(SocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void sendMessage(Message message) throws IOException {
        if (message == null) return;
        channel.write(ByteBuffer.wrap(serializeMessage(message)));
    }
}
