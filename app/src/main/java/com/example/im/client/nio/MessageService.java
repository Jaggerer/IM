package com.example.im.client.nio;


import com.example.im.client.nio.domain.IdMessage;
import com.example.im.client.nio.domain.Message;
import com.example.im.client.nio.domain.MessageFactory;
import com.example.im.client.nio.domain.MessageType;
import com.example.im.client.nio.util.SerializationUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;


public abstract class MessageService {

    private final BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<>(20);    // thread safe 但是 没有长度限制
    protected static final Map<String, MessageService> userChannel = new ConcurrentHashMap<>(); //  userName-Channel

    private volatile long sleepTime = INIT_SLEEP_TIME;
    private static final long INIT_SLEEP_TIME = 1000L;

    public boolean doMessage(SocketChannel channel) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        Message message = null;
        int byteCount = 0;
        try {
            byteCount = channel.read(byteBuffer);
            if (byteCount > 0) {
                byteBuffer.flip();  //todo  是否需要
                int bodyLen = byteBuffer.getInt();
                byteBuffer = ByteBuffer.allocate(1);
                channel.read(byteBuffer);
                byteBuffer.flip();
                byte messageType = byteBuffer.get();
                byteBuffer = ByteBuffer.allocate(bodyLen);
                channel.read(byteBuffer);
                byteBuffer.flip();
                byte[] body = byteBuffer.array();
                Class clazz = MessageFactory.getMessageClass(messageType);
                if (clazz == null) {
                    throw new RuntimeException("未支持的消息类型");
                }
                message = (Message) SerializationUtils.deserialize(body, clazz);
                if (message instanceof IdMessage) {
                    userChannel.put(message.getId(), this);
                } else {
                    _doMessage(message);
                }
            }
        } catch (IOException e) {
            if (byteCount <= 0) {
                try {
                    channel.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return byteCount > 0;
    }


    /**
     * {
     * //todo  do something with your's business
     *
     * @param message
     */
    protected abstract void _doMessage(Message message);


    public void sendMessage(SocketChannel channel) {
        if (messageQueue.isEmpty()) {
            //放弃阻塞sleep一下   //指数退避 最大10秒
            if (sleepTime < 10) {
                sleepTime = sleepTime + 1;
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Message message = messageQueue.poll();
            if (message instanceof IdMessage) {   //在connect会触发这个动作
                //先保存一下channel
                userChannel.put(message.getId(), this);
            }
            _sendMessage(channel, message);
            sleepTime = INIT_SLEEP_TIME;
        }

    }

    private void _sendMessage(SocketChannel channel, Message message) {
        byte[] body = SerializationUtils.serialize(message);
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + 1 + body.length + 1); //todo need +1 ?  虽然最后flip会收缩
        byteBuffer.clear();
        byteBuffer.putInt(body.length);
        byteBuffer.put(MessageType.getMessageType(message));
        byteBuffer.put(body);
        byteBuffer.flip();
        try {
            channel.write(byteBuffer);
        } catch (IOException e) {
            System.out.println("发送消息失败" + e.getMessage());
        }
    }

    public void handleKey(SelectionKey selectionKey, Selector selector) throws IOException {
        SocketChannel socketChannel;
        ServerSocketChannel serverSocketChannel;
        if (selectionKey.isAcceptable()) {
            serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } else if (selectionKey.isConnectable()) {
            System.out.println("client connect");
            socketChannel = (SocketChannel) selectionKey.channel();
            if (socketChannel.isConnectionPending()) { //握手完成
                socketChannel.finishConnect();

                int ops = selectionKey.interestOps();
                ops &= ~SelectionKey.OP_CONNECT;
                selectionKey.interestOps(ops);

                sendMessage(socketChannel);
                socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            }
        } else if (selectionKey.isReadable()) {
            socketChannel = (SocketChannel) selectionKey.channel();
            if (!socketChannel.isConnected()) {
                return;
            }
            boolean canRead = doMessage(socketChannel);
            if (canRead) {
                socketChannel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);

            }
        } else if (selectionKey.isWritable()) {
            socketChannel = (SocketChannel) selectionKey.channel();
            if (!socketChannel.isConnected()) {
                return;
            }
            sendMessage(socketChannel);
            socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }


    }


    public void addMessage(Message message) {
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Set<String> loginUser() {
        return userChannel.keySet();
    }

}
