package com.example.im.voice;

import android.media.MediaRecorder;

/**
 * Created by ganchenqing on 2018/3/21.
 */

public interface RecordControlListener {
    void startRecording(String chatObjectId);

    void cancelRecording();

    int stopRecording();

    boolean isRecording();

    MediaRecorder getMediaRecorder();

    String getRecordFilePath(String chatObjectId);
}
