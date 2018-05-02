//package com.example.im.client.socket;
//
//import com.example.im.Constant;
//import com.example.im.db.bean.MyMessage;
//import com.example.im.utils.BitcoinOutput;
//import com.example.im.utils.ByteUtils;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.net.SocketAddress;
//import java.nio.ByteBuffer;
//import java.nio.channels.SelectionKey;
//import java.nio.channels.Selector;
//import java.nio.channels.SocketChannel;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//
///**
// * Created by ganchenqing on 2018/3/23.
// */
//
public class SocketClient {
//    /**
//     * 信道选择器
//     */
//    private Selector mSelector;
//
//
//    /**
//     * 服务器通信的信道
//     */
//    private SocketChannel mChannel;
//
//    /**
//     * 远端服务器ip地址
//     */
//    private String mRemoteIp;
//
//    /**
//     * 远端服务器端口
//     */
//    private int mPort;
//
//    /**
//     * 是否加载过的标识
//     */
//    private boolean mIsInit = false;
//
//    /**
//     * 单键实例
//     */
//    private static SocketClient gTcp;
//
//    private SocketClientEventListener mEventListener;
//
//    /**
//     * 默认链接超时时间
//     */
//    public static final int TIME_OUT = 10000;
//
//    /**
//     * 读取buff的大小
//     */
//    public static final int READ_BUFF_SIZE = 1024;
//
//    /**
//     * 消息流的格式
//     */
//    public static final String BUFF_FORMAT = "utf-8";
//
//    public static BlockingQueue<MyMessage> mMyMessageQueue = new LinkedBlockingQueue<>();
//
//    public static synchronized SocketClient instance() {
//        if (gTcp == null) {
//            gTcp = new SocketClient();
//        }
//        return gTcp;
//    }
//
//    private SocketClient() {
//
//    }
//
//    /**
//     * 链接远端地址
//     *
//     * @param remoteIp
//     * @param port
//     * @return
//     */
//    public void connect(String remoteIp, int port, SocketClientEventListener tcel) {
//        mRemoteIp = remoteIp;
//        mPort = port;
//        mEventListener = tcel;
//        connect();
//    }
//
//    /**
//     * 链接远端地址
//     *
//     * @param remoteIp
//     * @param port
//     * @return
//     */
//    public void connect(String remoteIp, int port) {
//        connect(remoteIp, port, null);
//    }
//
//    private void connect() {
//        if (!mIsInit) {
//            //需要在子线程下进行链接
//            new Thread(new MyConnectRunnable()).start();
//            new Thread(new SendMsgRunnable()).start();
//        }
//    }
//
//    /**
//     * 发送数据
//     */
//    private void sendMsg() {
//        if (!mIsInit) {
//            return;
//        }
//        while (mIsInit) {
//            try {
//                MyMessage myMessage = mMyMessageQueue.take();
//                //消息类型
//                byte msgType = (byte) myMessage.getMessageType();
//                //用户ID
//                String userId = "userId";
//                //用户ID长度
//                byte userIdLen = (byte) userId.getBytes().length;
//                //业务类型
//                String remarkId = "shop";
//                //业务类型长度
//                byte remarkLen = (byte) remarkId.getBytes().length;
//                short packageLen = 0;
//                byte[] sendPackage;
//
//                switch (myMessage.getMessageType()) {
////                    文字消息一个包直接发过去
//                    case Constant.TYPE_TEXT:
//                        // 内容
//                        String content = "我爱你";
//                        //内容长度
//                        int contentLen = content.getBytes().length;
//                        //包长
//                        //todo byte携程长亮
//                        packageLen = (short) (remarkLen +userIdLen + contentLen + 1 + 1 +1);
//
//                        sendPackage = new BitcoinOutput().writeShort(packageLen)
//                                .writeByte(msgType)
//                                .writeByte(userIdLen)
//                                .writeString(userId)
//                                .writeByte(remarkLen)
//                                .writeString(remarkId)
//                                .writeString(content).toByteArray();
//                        mChannel.write(ByteBuffer.wrap(sendPackage));
//                        break;
//
//                    case Constant.TYPE_PIC:
//                    case Constant.TYPE_VOICE:
//                        //先组装一个包，告诉服务器发送什么类型
//                        //todo byte携程长亮
//                        packageLen = (short) (remarkLen + msgType + userIdLen + 1 + 1);
//                        sendPackage = new BitcoinOutput().writeShort(packageLen)
//                                .writeByte(msgType)
//                                .writeByte(userIdLen)
//                                .writeString(userId)
//                                .writeByte(remarkLen)
//                                .writeString(remarkId)
//                                .toByteArray();
//                        mChannel.write(ByteBuffer.wrap(sendPackage));
//
//                        //todo 如何添加监听
//                        //发送一个包，直接传输文件
//                        File sendFile = new File(myMessage.getFileDir());
//                        byte[] fileByte = ByteUtils.InputStream2ByteArray(sendFile);
//
//                        //把消息类型设置为传输内容
//                        msgType = Constant.TYPE_CONTENT;
//
//                        packageLen = (short) (fileByte.length + 1);
//                        sendPackage = new BitcoinOutput()
//                                .writeShort(packageLen)
//                                .writeByte(msgType)
//                                .write(fileByte)
//                                .toByteArray();
//                        mChannel.write(ByteBuffer.wrap(sendPackage));
//
//                        //发送一个包，代表结束
//                        msgType = Constant.TYPE_ENDING;
//                        packageLen = 1;
//                        sendPackage = new BitcoinOutput()
//                                .writeShort(packageLen)
//                                .writeByte(msgType)
//                                .toByteArray();
//                        mChannel.write(ByteBuffer.wrap(sendPackage));
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    public Selector getSelector() {
//        return mSelector;
//    }
//
//    /**
//     * 是否链接着
//     *
//     * @return
//     */
//    public boolean isConnect() {
//        if (!mIsInit) {
//            return false;
//        }
//        return mChannel.isConnected();
//    }
//
//    /**
//     * 关闭链接
//     */
//    public void close() {
//        mIsInit = false;
//        mRemoteIp = null;
//        mPort = 0;
//        try {
//            if (mSelector != null) {
//                mSelector.close();
//            }
//            if (mChannel != null) {
//                mChannel.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 重连
//     *
//     * @return
//     */
//    public void reConnect() {
//        close();
//        connect();
//    }
//
//    private synchronized boolean repareRead() {
//        boolean bRes = false;
//        try {
//            //打开并注册选择器到信道
//            mSelector = Selector.open();
//            if (mSelector != null) {
//                mChannel.register(mSelector, SelectionKey.OP_READ);
//                bRes = true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bRes;
//    }
//
//    public void revMsg() {
//        if (mSelector == null) {
//            return;
//        }
//        boolean bres = true;
//        while (mIsInit) {
//            if (!isConnect()) {
//                bres = false;
//            }
//            if (!bres) {
//                try {
//                    Thread.sleep(100);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                continue;
//            }
//
//            try {
//                //有数据就一直接收
//                while (mIsInit && mSelector.select() > 0) {
//                    for (SelectionKey sk : mSelector.selectedKeys()) {
//                        //如果有可读数据
//                        if (sk.isReadable()) {
//                            //使用NIO读取channel中的数据
//                            SocketChannel sc = (SocketChannel) sk.channel();
//                            //读取缓存
//                            ByteBuffer readBuffer = ByteBuffer.allocate(READ_BUFF_SIZE);
//                            //实际的读取流
//                            ByteArrayOutputStream read = new ByteArrayOutputStream();
//
//                            //todo 根据协议来确定读数据的规则
////                            //单个读取流
////                            byte[] bytes;
////                            //读完为止
////                            while ((nRead = sc.read(readBuffer)) > 0) {
////                                //整理
////                                readBuffer.flip();
////                                bytes = new byte[nRead];
////                                nLen += nRead;
////                                //将读取的数据拷贝到字节流中
////                                readBuffer.get(bytes);
////                                //将字节流添加到实际读取流中
////                                read.write(bytes);
////                                /////////////////////////////////////
////                                //@ 需要增加一个解析器,对数据流进行解析
////
////                                /////////////////////////////////////
////
////                                readBuffer.clear();
////                            }
//
////                            if (nLen > 0) {
////                                if (mEventListener != null) {
////                                    mEventListener.recvMsg(read);
////                                } else {
////                                    String info = new String(read.toString(BUFF_FORMAT));
////                                    System.out.println("rev:" + info);
////                                }
////                            }
//
//                            //为下一次读取做准备
//                            sk.interestOps(SelectionKey.OP_READ);
//                        }
////
////                        //删除此SelectionKey
//                        mSelector.selectedKeys().remove(sk);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//    public interface SocketClientEventListener {
//        void recvMsg(ByteArrayOutputStream read);
//    }
//
//    private class MyConnectRunnable implements Runnable {
//
//        @Override
//        public void run() {
//            try {
//                //打开监听信道,并设置为非阻塞模式
//                SocketAddress ad = new InetSocketAddress(mRemoteIp, mPort);
//                mChannel = SocketChannel.open(ad);
//                if (mChannel != null) {
//                    mChannel.socket().setTcpNoDelay(false);
//                    mChannel.socket().setKeepAlive(true);
//
//                    //设置超时时间
//                    mChannel.socket().setSoTimeout(TIME_OUT);
//                    mChannel.configureBlocking(false);
//
//                    mIsInit = repareRead();
//
//                    //创建读线程
//                    RevMsgRunnable rev = new RevMsgRunnable();
//                    new Thread(rev).start();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (!mIsInit) {
//                    close();
//                }
//            }
//        }
//    }
//
//    /**
//     * 对外暴露的发送消息方法
//     */
//    public void sendMessage(MyMessage myMessage) {
//        try {
//            mMyMessageQueue.put(myMessage);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private class SendMsgRunnable implements Runnable {
//
//        @Override
//        public void run() {
//            sendMsg();
//        }
//    }
//
//    private class RevMsgRunnable implements Runnable {
//        @Override
//        public void run() {
//            revMsg();
//        }
//
//    }
}
