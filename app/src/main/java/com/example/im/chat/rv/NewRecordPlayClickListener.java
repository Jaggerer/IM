package com.example.im.chat.rv;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.example.im.client.nio.util.DownloadUtil;
import com.example.im.db.bean.MyMessage;
import com.example.im.R;
import com.example.im.utils.UserUtils;

import java.io.File;
import java.io.FileInputStream;


public class NewRecordPlayClickListener implements View.OnClickListener {

    MyMessage myMessage;
    ImageView mIvVoice;
    private AnimationDrawable anim = null;
    Context mContext;
    String currentObjectId = "";
    MediaPlayer mediaPlayer = null;
    public static boolean isPlaying = false;
    public static NewRecordPlayClickListener currentPlayListener = null;
    static MyMessage currentMsg = null;

    public NewRecordPlayClickListener(Context context, MyMessage msg, ImageView voice) {
        this.mIvVoice = voice;
        this.myMessage = msg;
        this.mContext = context.getApplicationContext();
        currentMsg = msg;
        currentPlayListener = this;
        currentObjectId = UserUtils.getCurrentUser(context);
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
                    currentMsg = myMessage;
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
        if (myMessage.getFromId().equals(currentObjectId)) {
            mIvVoice.setImageResource(R.drawable.anim_chat_voice_right);
        } else {
            mIvVoice.setImageResource(R.drawable.anim_chat_voice_left);
        }
        anim = (AnimationDrawable) mIvVoice.getDrawable();
        anim.start();
    }

    private void stopRecordAnimation() {
        if (myMessage.getFromId().equals(currentObjectId)) {
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
                    && currentMsg.hashCode() == myMessage.hashCode()) {
                currentMsg = null;
                return;
            }
        }
        if (myMessage.getFromId().equals(currentObjectId)) {// 如果是自己发送的语音消息，则播放本地地址
            //todo 获得文件地址
            String localPath = myMessage.getFileDir();
            startPlayRecord(localPath, true);
        } else {// 如果是收到的消息，则需要先下载后播放
            String localPath = getFilePath();
            startPlayRecord(localPath, true);
        }
    }

    public String getFilePath() {
        String fileName = DownloadUtil.get().getNameFromUrl(myMessage.getContent());
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "voice" + File.separator + fileName);
        return file.getPath();
    }

}