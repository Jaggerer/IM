package com.example.im.client.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioClient {

    /*服务器端地址*/


   private MessageService messageService;
   private InetSocketAddress remoteAddress=new InetSocketAddress("localhost", 8888);//default

    public NioClient(MessageService messageService) {
        this.messageService = messageService;
    }

    public void connect()throws IOException{
        SocketChannel socketChannel=SocketChannel.open();
        socketChannel.configureBlocking(false);
        Selector selector=Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(remoteAddress);

        Set<SelectionKey> selectionKeys;
        Iterator<SelectionKey> iterator;
        SelectionKey selectionKey;
        for(;;){
            selector.select();
            selectionKeys=selector.selectedKeys();
            iterator=selectionKeys.iterator();
            while (iterator.hasNext()){
                selectionKey=iterator.next();
                iterator.remove();
                try {
                    messageService.handleKey(selectionKey,selector);
                }catch (IOException e){
                    //ignore
                }

            }

        }

    }


//    public static void main(String[] args)throws Exception{
//        MessageService messageService=new MessageService();
//        NioClient client=new NioClient(messageService);
//        //测试发消息
//        new Thread(()->{
//            for(int i=0;i<10;i++) {
//                messageService.addMessage(new StringMessage());
//            }
//        }).start();
//        client.connect();
//    }

}
