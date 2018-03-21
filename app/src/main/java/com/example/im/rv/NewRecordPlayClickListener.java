package com.example.im.rv;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.view.View;
import android.widget.ImageView;

import com.example.im.Message;
import com.example.im.R;

import java.io.File;
import java.io.FileInputStream;


public class NewRecordPlayClickListener implements View.OnClickListener {

    Message message;
    ImageView mIvVoice;
    private AnimationDrawable anim = null;
    Context mContext;
    String currentObjectId = "";
    MediaPlayer mediaPlayer = null;
    public static boolean isPlaying = false;
    public static NewRecordPlayClickListener currentPlayListener = null;
    static Message currentMsg = null;

    public NewRecordPlayClickListener(Context context, Message msg, ImageView voice) {
        this.mIvVoice = voice;
        this.message = msg;
        this.mContext = context.getApplicationContext();
        currentMsg = msg;
        currentPlayListener = this;
        currentObjectId = msg.getToId();
    }

    @SuppressWarnings("resource")
    public void startPlayRecord(String filePath, boolean isUseSpeaker) {
        if (!(new File(filePath).exists())) {
            return;
        }
        AudioManager audioManager = (AudioManager) mContext
                .getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();
        if (isUseSpeaker) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        } else {
            audioManager.setSpeakerphoneOn(false);
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }

        try {
            mediaPlayer.reset();
            // 单独使用此方法会报错播放错误:setDataSourceFD failed.: status=0x80000000
            // mediaPlayer.setDataSource(filePath);
            // 因此采用此方式会避免这种错误
            FileInputStream fis = new FileInputStream(new File(filePath));
            mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer arg0) {
                    isPlaying = true;
                    currentMsg = message;
                    arg0.start();
                    startRecordAnimation();
                }
            });
            mediaPlayer
                    .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopPlayRecord();
                        }

                    });
            currentPlayListener = this;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPlayRecord() {
        stopRecordAnimation();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isPlaying = false;
    }

    private void startRecordAnimation() {
        if (message.getFromId().equals(currentObjectId)) {
            mIvVoice.setImageResource(R.drawable.anim_chat_voice_right);
        } else {
            mIvVoice.setImageResource(R.drawable.anim_chat_voice_left);
        }
        anim = (AnimationDrawable) mIvVoice.getDrawable();
        anim.start();
    }

    private void stopRecordAnimation() {
        if (message.getFromId().equals(currentObjectId)) {
            mIvVoice.setImageResource(R.mipmap.voice_left3);
        } else {
            mIvVoice.setImageResource(R.mipmap.voice_right3);
        }
        if (anim != null) {
            anim.stop();
        }
    }

    @Override
    public void onClick(View arg0) {
        if (isPlaying) {
            currentPlayListener.stopPlayRecord();
            if (currentMsg != null
                    && currentMsg.hashCode() == message.hashCode()) {
                currentMsg = null;
                return;
            }
        }
        if (message.getFromId().equals(currentObjectId)) {// 如果是自己发送的语音消息，则播放本地地址
            //todo 获得文件地址
            String localPath = message.getContent().split("&")[0];
            startPlayRecord(localPath, true);
        } else {// 如果是收到的消息，则需要先下载后播放
            String localPath = downloadVoice(message.getFileDir());
            startPlayRecord(localPath, true);
        }
    }

    // todo 从服务端下载音频并存储到本地
    private String downloadVoice(String fileDir) {
        return null;
    }

}