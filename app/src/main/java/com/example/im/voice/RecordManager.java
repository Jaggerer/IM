package com.example.im.voice;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by ganchenqing on 2018/3/21.
 */

public class RecordManager {

    private static final int RECORDING_BITRATE = 12200;
    private MediaRecorder mMediaRecorder;
    public static int MAX_RECORD_TIME = 60;
    public static int MIN_RECORD_TIME = 1;
    private File file;
    private long startTime;
    private String recordName;
    private String recordPath;
    Context context;
    private static volatile RecordManager INSTANCE;
    private static Object INSTANCE_LOCK = new Object();
    OnRecordChangeListener mChangListener;
    private AtomicBoolean mIsRecording = new AtomicBoolean(false);
    private ExecutorService mThreadPool;
    private static final int UPDATE_VOICE_CHANGE = 10;
    String VOICE_DIR = Environment.getExternalStorageDirectory().
            getAbsolutePath() + File.separator + "IMChat" + File.separator + "voice";


    final Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            if (msg.what == 10) {
                int volume = msg.arg1;
                int time = msg.arg2;
                if (RecordManager.this.mChangListener != null) {
                    RecordManager.this.mChangListener.onVolumeChanged(volume);
                    if (time % 10 == 0) {
                        RecordManager.this.mChangListener.onTimeChanged(time / 10, RecordManager.this.recordPath);
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    });


    public static RecordManager getInstance(Context context) {
        if (INSTANCE == null) {
            Object obj = INSTANCE_LOCK;
            synchronized (INSTANCE_LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new RecordManager();
                }

                INSTANCE.init(context);
            }
        }

        return INSTANCE;
    }

    public void init(Context c) {
        this.context = c;
        this.mThreadPool = Executors.newCachedThreadPool();
    }

    public void clear() {
        this.context = null;
        if (this.mChangListener != null) {
            this.mChangListener = null;
        }

    }

    public void setOnRecordChangeListener(OnRecordChangeListener listener) {
        this.mChangListener = listener;
    }

    public void startRecording(String userObjectId) {
        if (this.mMediaRecorder == null) {
            this.mMediaRecorder = new MediaRecorder();
            this.mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            this.mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            this.mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            this.mMediaRecorder.setAudioChannels(1);
            this.mMediaRecorder.setAudioEncodingBitRate(RECORDING_BITRATE);
            this.mMediaRecorder.setOnErrorListener(new RecordManager.RecorderErrorListener());
        } else {
            this.mMediaRecorder.stop();
            this.mMediaRecorder.reset();
        }

        this.recordName = this.getRecordFileName();
        this.recordPath = this.getRecordFilePath(userObjectId);
        this.file = new File(this.recordPath);
        this.mMediaRecorder.setOutputFile(this.file.getAbsolutePath());

        try {
            this.mMediaRecorder.prepare();
            this.mMediaRecorder.start();
            this.mIsRecording.set(true);
            this.startTime = (new Date()).getTime();
            this.mThreadPool.execute(new RecordManager.RecordingChangeUpdater());
        } catch (IllegalStateException e) {
            Log.d("voice", "IllegalStateException thrown while trying to record a greeting");
            this.mIsRecording.set(false);
            this.mMediaRecorder.release();
            this.mMediaRecorder = null;
        } catch (IOException e) {
            Log.d("voice", "IOException thrown while trying to record a greeting");
            this.mIsRecording.set(false);
            this.mMediaRecorder.release();
            this.mMediaRecorder = null;
        }

    }

    public void cancelRecording() {
        if (this.mMediaRecorder != null) {
            this.mMediaRecorder.setOnErrorListener((MediaRecorder.OnErrorListener) null);
            this.mMediaRecorder.stop();
            this.mMediaRecorder.release();
            this.mMediaRecorder = null;
            if (this.file != null && this.file.exists() && !this.file.isDirectory()) {
                this.file.delete();
            }

            this.mIsRecording.set(false);
        }
    }

    public int stopRecording() {
        if (this.mMediaRecorder != null) {
            this.mIsRecording.set(false);
            this.mMediaRecorder.setOnErrorListener((MediaRecorder.OnErrorListener) null);
            this.mMediaRecorder.stop();
            this.mMediaRecorder.release();
            this.mMediaRecorder = null;
            int i = (int) ((new Date()).getTime() - this.startTime) / 1000;
            return i;
        } else {
            return 0;
        }
    }

    public boolean isRecording() {
        return this.mIsRecording.get();
    }

    public MediaRecorder getMediaRecorder() {
        return this.mMediaRecorder;
    }

    public String getRecordFilePath(String sendUserId) {
        File dir = new File(VOICE_DIR + File.separator + sendUserId);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File audioFile = new File(dir.getAbsolutePath() + File.separator + this.recordName);

        try {
            if (!audioFile.exists()) {
                audioFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return audioFile.getAbsolutePath();
    }

    protected String getRecordFileName() {
        return System.currentTimeMillis() + ".amr";
    }

    public class RecorderErrorListener implements MediaRecorder.OnErrorListener {
        public RecorderErrorListener() {
        }

        public void onError(MediaRecorder mp, int what, int extra) {
            String whatDescription;
            switch (what) {
                case 1:
                    whatDescription = "MEDIA_RECORDER_ERROR_UNKNOWN";
                    break;
                default:
                    whatDescription = Integer.toString(what);
            }


            try {
                if (mp != null) {
                    mp.reset();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
    }

    private final class RecordingChangeUpdater implements Runnable {
        private RecordingChangeUpdater() {
        }

        public void run() {
            for (int currentRecordCounter = 0; RecordManager.this.mIsRecording.get(); ++currentRecordCounter) {
                int volume = RecordManager.this.mMediaRecorder.getMaxAmplitude();
                int value = 5 * volume / 32768;
                if (value > 5) {
                    value = 5;
                }

                Message msg = new Message();
                msg.arg1 = value;
                msg.arg2 = currentRecordCounter;
                msg.what = 10;
                RecordManager.this.handler.sendMessage(msg);

                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

        }
    }
}
