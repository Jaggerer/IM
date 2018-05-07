package com.example.im.client.nio;


import android.util.Log;

import com.example.im.client.nio.domain.IdMessage;
import com.example.im.client.nio.domain.Message;
import com.example.im.client.nio.domain.MessageFactory;
import com.example.im.client.nio.domain.MessageType;
import com.example.im.client.nio.domain.VoiceMessage;
import com.example.im.client.nio.util.SerializationUtils;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


public abstract class MessageService {

    private  final BlockingQueue<Message> messageQueue=new ArrayBlockingQueue<>(20);    // thread safe 但是 没有长度限制
    protected static final Map<String,MessageService> userChannel=new ConcurrentHashMap<>(); //  userName-Channel

    private  volatile AtomicLong sleepTime=new AtomicLong(Per_SLEEP_TIME);// init sleep 1 second
    private static final long Per_SLEEP_TIME=1000L;

    public boolean doMessage(SocketChannel channel){
        ByteBuffer byteBuffer=ByteBuffer.allocate(4);
        Message message=null;
        int byteCount = 0;
        try {
            byteCount=  channel.read(byteBuffer);
            if(byteCount>0) {
                byteBuffer.flip();
                int bodyLen = byteBuffer.getInt();  // 文件长度
                byteBuffer = ByteBuffer.allocate(1);
                channel.read(byteBuffer);           // 类型
                byteBuffer.flip();
                byte messageType = byteBuffer.get();
                byteBuffer = ByteBuffer.allocate(bodyLen);
                byteBuffer.clear();
                int readIndex=0;
                while (true) {
                    int count = -1;
                    if (readIndex<bodyLen) {
                        count = channel.read(byteBuffer);
                        if (count < 0) {
                            throw new IOException("异常客服端关闭");
                        }
                        readIndex+=count;
                    }else{
                        break;
                    }
                }
                byteBuffer.flip();
                byte[] body = byteBuffer.array();   //如果觉得性能(主要的单loop 的吞吐指标)可能存在问题 从这里开始的操作都放到业务线程池重处理
                Class clazz = MessageFactory.getMessageClass(messageType);
                if (clazz == null) {
                    throw new RuntimeException("未支持的消息类型");
                }
                 message = (Message) SerializationUtils.deserialize(body, clazz);
                if(message instanceof IdMessage){
                   userChannel.put(  message.getId(),this);
                }else {
                    _doMessage(message);
                }
            }
        } catch (IOException e) {
           if(byteCount<=0){
               try {
                   channel.close();
               } catch (IOException e1) {
                   e1.printStackTrace();
               }
           }
        }
        return byteCount>0;
    }


    /**
     *
     todo  do something with your's business
     */
    protected  abstract   void _doMessage(Message message);


    public void sendMessage(SocketChannel channel){
        if(messageQueue.isEmpty()){
            //放弃阻塞sleep一下   //指数退避 最大10秒
//            if(sleepTime.get()<Per_SLEEP_TIME*10) {
//              sleepTime.addAndGet(1000L);
//            }
            try {
                Thread.sleep(sleepTime.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            Message message=messageQueue.poll();
            if(message instanceof IdMessage){   //在connect会触发这个动作
                //先保存一下channel
                userChannel.put(message.getId(),this);
            }
            _sendMessage(channel,message);
//            sleepTime.set(Per_SLEEP_TIME);;
        }

    }

    private void _sendMessage(SocketChannel channel,Message message){
        byte[] body= SerializationUtils.serialize(message);
        ByteBuffer byteBuffer=ByteBuffer.allocate(4+1+body.length);
        byteBuffer.clear();
        byteBuffer.putInt(body.length);
        byteBuffer.put(MessageType.getMessageType(message));
        byteBuffer.put(body);
        Logger.d(body);
//        Message deMessage = (Message) SerializationUtils.deserialize(body, VoiceMessage.class);
//        Logger.d(deMessage);

        byteBuffer.flip();
        try {
            while(byteBuffer.hasRemaining()){
                channel.write(byteBuffer);
            }
        } catch (IOException e) {
            System.out.println("发送消息失败"+e.getMessage());
        }
    }

    /**
     *
     //todo  是否可写 是否需要注册
     solution
     https://www.zhihu.com/question/22840801

      首先 electionKey.OP_WRITE 表达的是缓冲区是否可以写 只要注册了并且可以写就会一直触发

        对于 LT 模式下的 OP_WRITE  只有在写 数据发生 写不进去的时候需要 注册 该事件或者内存中有想要发出去的消息
        则主动触发一下（比如read完一般都要write点什么给客服端）
        比如 缓冲区 256k ，然后你写了一个 10m 的 Byte[] 进去。就会返回一个 EAGAIN 那么你需要自己
        记录写了多少然后注册写事件再下次从该offset开始写这个Byte[] 。LT 每次消耗事件需要从新注册

        对于ET 来说 则不会一直触发。它只有在 空变成有。或者有变成空这两种状态各触发一次。
        （对于 空变成有它触发 epoll_out ,对于 有变成空 则是写返回 EAGAIN）
        所以 只要在开始的时候注册一次读写后面就不需要再注册 OP_WRITE ，OP_READ事件。
     */
    public void handleKey(SelectionKey selectionKey, Selector selector)throws IOException {
        SocketChannel socketChannel;
        ServerSocketChannel serverSocketChannel;
        if (selectionKey.isAcceptable()) {
            serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ |SelectionKey.OP_WRITE);
        }
        else if (selectionKey.isConnectable()) {
            System.out.println("client connect");
            socketChannel = (SocketChannel) selectionKey.channel();
            if(socketChannel.isConnectionPending()) { //握手完成
                socketChannel.finishConnect();

                int ops = selectionKey.interestOps();
                ops &= ~SelectionKey.OP_CONNECT;
                selectionKey.interestOps(ops);

                sendMessage(socketChannel);
                socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
            }
        }
        else if (selectionKey.isReadable()) {   //在读完注册写事件 是因为一般服务器都是回射性质的
            socketChannel = (SocketChannel) selectionKey.channel();
            if(!socketChannel.isConnected()){
                return;
            }
            boolean canRead = doMessage(socketChannel);
            if (canRead) {
                socketChannel.register(selector,  SelectionKey.OP_READ|SelectionKey.OP_WRITE);
            }
        }else if (selectionKey.isWritable()) {
            socketChannel = (SocketChannel) selectionKey.channel();
            if(!socketChannel.isConnected()){
                return;
            }
            sendMessage(socketChannel);
            socketChannel.register(selector, SelectionKey.OP_READ |SelectionKey.OP_WRITE);
        }


    }


    public void addMessage(Message message){
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Set<String>  loginUser(){
        return userChannel.keySet();
    }

}
