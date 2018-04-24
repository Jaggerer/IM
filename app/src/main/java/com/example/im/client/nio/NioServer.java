package com.example.im.client.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {



    private MessageService messageService;

    private int port;

    public NioServer(int port)throws Exception{
        this.port=port;
        messageService=new MessageService();

        System.out.println("Server Start----8888:");

    }

    private void listen()throws IOException{
        Selector selector;
        ServerSocketChannel serverSocketChannel=ServerSocketChannel.open(); //打开服务器套接字通道
        serverSocketChannel.configureBlocking(false);               //服务器配置非阻塞

        ServerSocket serverSocket=serverSocketChannel.socket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(port));
        selector=Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        for(;;){
            selector.select();
            Set<SelectionKey> selectionKeys=selector.selectedKeys();
            Iterator<SelectionKey> iterator=selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key=iterator.next();
                iterator.remove();
                try {
                    messageService.handleKey(key,selector);
                }catch (IOException e){
                    // ignore
                }

            }

        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8888;
        NioServer server = new NioServer(port);
        server.listen();
    }



}
