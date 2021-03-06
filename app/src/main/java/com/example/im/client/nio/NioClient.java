package com.example.im.client.nio;


import com.example.im.Constant;
import com.example.im.client.nio.domain.IdMessage;
import com.example.im.client.nio.domain.Message;
import com.example.im.client.nio.domain.PicMessage;
import com.example.im.client.nio.domain.StringMessage;
import com.example.im.utils.URIUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioClient {

    /*服务器端地址*/


    private MessageService messageService;
    private InetSocketAddress remoteAddress = new InetSocketAddress(Constant.HOST_URL, Integer.valueOf(Constant.NIO_PORT));//default

    public NioClient(MessageService messageService) {
        this.messageService = messageService;
    }

    public void connect() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(remoteAddress);

        Set<SelectionKey> selectionKeys;
        Iterator<SelectionKey> iterator;
        SelectionKey selectionKey;
        for (; ; ) {
            selector.select();
            selectionKeys = selector.selectedKeys();
            iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                selectionKey = iterator.next();
                iterator.remove();
                try {
                    messageService.handleKey(selectionKey, selector);
                } catch (IOException e) {
                    //ignore
                }

            }

        }

    }

    //todo GCQ 需要在收到登陆消息后调用这个添加一个 IdMessage 然后再调用 NioClient.connect
    public void addMessage(Message message) {
        messageService.addMessage(message);
    }


//    public static void main(String[] args) throws Exception {
//        MessageService messageService = new ClientMessageService();
//        NioClient client = new NioClient(messageService);
//        //测试发消息
//        new Thread(() -> {
//            IdMessage idMessage = new IdMessage();
//            idMessage.setFrom("abc");
//            messageService.addMessage(idMessage);
//            try {
//                client.connect();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
//        Thread.sleep(2000);
//        File file = new File("/Users/ganchenqing/Downloads/数据库.png");
//        byte[] bytes = URIUtils.readFile(file);
//        PicMessage picMessage = new PicMessage();
//        picMessage.setPicName(file.getName());
//        picMessage.setPicture(bytes);
//        picMessage.setFrom("abc");
//        picMessage.setTo("abc");
//        messageService.addMessage(picMessage);
//        System.out.println("发送图片消息");
//        Thread.sleep(2000);
//        StringMessage stringMessage = new StringMessage();
//        stringMessage.setFrom("abc");
//        stringMessage.setTo("abc");
//        stringMessage.setContent("收到文字消息");
//        System.out.println("发送文字消息");
//        messageService.addMessage(stringMessage);
//
//    }




}
