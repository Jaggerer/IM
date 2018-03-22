package com.example.im.voice;

/**
 * Created by ganchenqing on 2018/3/21.
 */

public interface OnRecordChangeListener {
    void onVolumeChanged(int value);

    void onTimeChanged(int recordTime, String localPath);

}
