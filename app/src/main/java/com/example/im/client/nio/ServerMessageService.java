package com.example.im.client.nio;

import com.example.im.client.nio.MessageService;
import com.example.im.client.nio.domain.Message;
import com.example.im.client.nio.domain.PicMessage;
import com.example.im.client.nio.util.FileUtils;

import java.io.IOException;

public class ServerMessageService extends MessageService {

    @Override
    protected void _doMessage(Message message) {
        System.out.println(message);

        MessageService toMessage=userChannel.get(message.getTo());
        if(toMessage !=this){   //判断是否是当前 channel的消息
            toMessage.addMessage(message);
        }else{
            //todo  做业务处理逻辑然后发送
            if(message instanceof PicMessage){
                String picName=((PicMessage) message).getPicName();
                byte[] pic=((PicMessage) message).getPicture();
                try {
                  String picPath=  FileUtils.storeFile(picName,pic);
                  PicMessage result=new PicMessage();
                  result.setCreatedTime(message.getCreatedTime());
                  result.setFrom(message.getFrom());
                  result.setTo(message.getTo());
                  result.setPicName(picName);
                  result.setPicture(null);
                  result.setPicUrl(picPath);
                  addMessage(result);
                } catch (IOException e) {
                    System.out.println("文件存储失败："+e.getMessage());
                }
            }

        }
    }
}
