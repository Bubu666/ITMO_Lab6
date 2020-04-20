package client;

import network.message.Message;
import network.message.MessageReceiver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientMessageReceiver implements MessageReceiver {
    private final SocketChannel channel;

    public ClientMessageReceiver(SocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public Message receiveMessage() throws IOException, ClassNotFoundException {
        ByteBuffer messageBuf = ByteBuffer.allocate(1024 * 32);

        while(channel.read(messageBuf) > 0) {
            if (messageBuf.position() == messageBuf.capacity()) {
                final byte[] buf = messageBuf.array();

                messageBuf = ByteBuffer.allocate(messageBuf.capacity() * 3 / 2);
                messageBuf.put(buf);
            }
        }
        return deserializeMessage(messageBuf.array());
    }
}
