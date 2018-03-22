package com.example.im;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.im.voice.OnRecordChangeListener;
import com.example.im.voice.RecordManager;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.PicassoAdapter;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.im.Constant.REQUEST_CAMERA;

public class ChatActivity extends AppCompatActivity {


    LinearLayout mLlChat;


    RecyclerView mRvChat;

    EditText mEtText;

    Button mBtnChatAdd;

    Button mBtnChatEmj;

    Button mBtnSpeak;

    Button mBtnChatVoice;
    Button mBtnChatKeybord;
    Button mBtnChatSend;

    TextView mTvSendPic;
    TextView mTvSendCamera;

    LinearLayout mLlMore;
    LinearLayout mLlAdd;
    LinearLayout mLlEmj;

    // 语音有关
    RelativeLayout mLlRecord;
    TextView mTvVoiceTips;
    ImageView mIvRecord;
    private Drawable[] drawable_Anims;// 话筒动画

    ChatAdapter adapter;
    protected LinearLayoutManager layoutManager;

    RecordManager mRecordManager;

    private List<Message> messageList = new ArrayList<>();

    File picFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        messageList.addAll(getMessageListFromServer());

        initAdapter();
        initBottomView();
        initListener();
    }

    private void initListener() {
        mBtnChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        mBtnChatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLlMore.getVisibility() == View.GONE) {
                    mLlMore.setVisibility(View.VISIBLE);
                    mLlAdd.setVisibility(View.VISIBLE);
                    mLlEmj.setVisibility(View.GONE);
                    hideSoftInputView();
                } else {
                    if (mLlEmj.getVisibility() == View.VISIBLE) {
                        mLlEmj.setVisibility(View.GONE);
                        mLlAdd.setVisibility(View.VISIBLE);
                    } else {
                        mLlMore.setVisibility(View.GONE);
                    }
                }
            }
        });

//        发送本地图片
        mTvSendPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast("发送本地图片");
                FishBun.with(ChatActivity.this).setImageAdapter(new PicassoAdapter()).setMaxCount(1).setCamera(true).startAlbum();
            }
        });
        mTvSendCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast("发送拍照图片");
                applyWritePermission();
            }
        });

    }

    public void applyWritePermission() {

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= 23) {
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check == PackageManager.PERMISSION_GRANTED) {
                //调用相机
                useCamera();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.REQUEST_CAMERA);
            }
        } else {
            useCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.REQUEST_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            useCamera();
        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }

    private void useCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        picFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/image/" + System.currentTimeMillis() + ".jpg");
        picFile.getParentFile().mkdirs();

        //改变Uri  和xml中的一致
        Uri uri = FileProvider.getUriForFile(ChatActivity.this, "com.example.im.fileprovider", picFile);
        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    List<Uri> path = imageData.getParcelableArrayListExtra(Define.INTENT_PATH);
                    sendPicture(path.get(0));
                    break;
                }
            case Constant.REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    Uri uri = FileProvider.getUriForFile(this, "com.example.im.fileprovider", picFile);
                    sendPicture(uri);
                    break;
                }
        }
    }

    private void initAdapter() {
        layoutManager = new LinearLayoutManager(this);
        mRvChat.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(this, messageList);
        mRvChat.setAdapter(adapter);

    }

    private void initView() {
        mBtnChatAdd = findViewById(R.id.btn_chat_add);
        mBtnChatEmj = findViewById(R.id.btn_chat_emo);
        mBtnChatKeybord = findViewById(R.id.btn_chat_keyboard);
        mBtnChatSend = findViewById(R.id.btn_chat_send);
        mBtnChatVoice = findViewById(R.id.btn_chat_voice);
        mBtnSpeak = findViewById(R.id.btn_speak);
        mEtText = findViewById(R.id.edit_msg);
        mIvRecord = findViewById(R.id.iv_record);
        mLlAdd = findViewById(R.id.layout_add);
        mLlChat = findViewById(R.id.ll_chat);
        mLlEmj = findViewById(R.id.layout_emo);
        mLlMore = findViewById(R.id.layout_more);
        mLlRecord = findViewById(R.id.layout_record);
        mTvVoiceTips = findViewById(R.id.tv_voice_tips);
        mRvChat = findViewById(R.id.rc_view);
        mTvSendCamera = findViewById(R.id.tv_camera);
        mTvSendPic = findViewById(R.id.tv_picture);
    }

    private void initBottomView() {
        mEtText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                    scrollToBottom();
                }
                return false;
            }
        });
        mEtText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scrollToBottom();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    mBtnChatSend.setVisibility(View.VISIBLE);
                    mBtnChatKeybord.setVisibility(View.GONE);
                    mBtnChatVoice.setVisibility(View.GONE);
                } else {
                    if (mBtnChatVoice.getVisibility() != View.VISIBLE) {
                        mBtnChatVoice.setVisibility(View.VISIBLE);
                        mBtnChatSend.setVisibility(View.GONE);
                        mBtnChatKeybord.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, 0);
    }

    public List<Message> getMessageListFromServer() {
        List<Message> serverList = new ArrayList<>();
        Message message = new Message();
        message.setContent("接受文字");
        message.setMessageType(Constant.TYPE_TEXT);
        message.setCreateTime(System.currentTimeMillis());
        message.setFromId("2");
        message.setToId("1");
        serverList.add(message);
        return serverList;
    }

    private void sendMessage() {
        String text = mEtText.getText().toString();
        if (TextUtils.isEmpty(text.trim())) {
            toast("请输入内容");
            return;
        }
        Message message = new Message();
        message.setToId("2");
        message.setFromId("1");
        message.setCreateTime(System.currentTimeMillis());
        message.setContent(text);
        message.setMessageType(Constant.TYPE_TEXT);
        adapter.addMessage(message);
        mEtText.setText("");

    }

    private void sendPicture(Uri uri) {
        final Message message = new Message();
        message.setMessageType(Constant.TYPE_PIC);
        message.setCreateTime(System.currentTimeMillis());
        message.setFromId("1");
        message.setToId("2");
        message.setFileDir(uri.toString());
        message.setSendStatus(Constant.SENDING);
        adapter.addMessage(message);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    message.setSendStatus(Constant.SEND_SUC);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }).start();

    }

    //todo 发送声音
    public void sendVoiceMessage(String localPath, int recordTime) {

    }

    private Toast toast;

    public void toast(final Object obj) {
        try {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (toast == null)
                        toast = Toast.makeText(ChatActivity.this, "", Toast.LENGTH_SHORT);
                    toast.setText(obj.toString());
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initRecordManager() {
        // 语音相关管理器
        mRecordManager = RecordManager.getInstance(this);
        // 设置音量大小监听--在这里开发者可以自己实现：当剩余10秒情况下的给用户的提示，类似微信的语音那样
        mRecordManager.setOnRecordChangeListener(new OnRecordChangeListener() {


            @Override
            public void onVolumeChanged(int value) {
                mIvRecord.setImageDrawable(drawable_Anims[value]);

            }

            @Override
            public void onTimeChanged(int recordTime, String localPath) {
                Log.d("voice", "已录音长度:" + recordTime);
                if (recordTime >= mRecordManager.MAX_RECORD_TIME) {// 1分钟结束，发送消息
                    // 需要重置按钮
                    mBtnSpeak.setPressed(false);
                    mBtnSpeak.setClickable(false);
                    // 取消录音框
                    mLlRecord.setVisibility(View.INVISIBLE);
                    // 发送语音消息
                    sendVoiceMessage(localPath, recordTime);
                    //是为了防止过了录音时间后，会多发一条语音出去的情况。
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            mBtnSpeak.setClickable(true);
                        }
                    }, 1000);
                }
            }
        });
    }

    public String getCurrentUserId() {
        return "1";
    }


    class VoiceTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!checkSdCard()) {
                        toast("发送语音需要sdcard支持！");
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        mLlRecord.setVisibility(View.VISIBLE);
                        mTvVoiceTips.setText("松开手指，取消发送");
                        // 开始录音
                        mRecordManager.startRecording(getCurrentUserId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        mTvVoiceTips.setText("松开手指，取消发送");
                        mTvVoiceTips.setTextColor(Color.RED);
                    } else {
                        mTvVoiceTips.setText("上滑手指，取消发送");
                        mTvVoiceTips.setTextColor(Color.WHITE);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    mLlRecord.setVisibility(View.INVISIBLE);
                    try {
                        if (event.getY() < 0) {
                            // 放弃录音
                            mRecordManager.cancelRecording();
                            Log.d("voice", "放弃发送语音");
                        } else {
                            int recordTime = mRecordManager.stopRecording();
                            if (recordTime > 1) {
                                // 发送语音文件
                                sendVoiceMessage(mRecordManager.getRecordFilePath(getCurrentUserId()), recordTime);
                            } else {// 录音时间过短，则提示录音过短的提示
                                mLlRecord.setVisibility(View.GONE);
                                showShortToast().show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                default:
                    return false;
            }
        }
    }

    private Toast showShortToast() {
        if (toast == null) {
            toast = new Toast(this);
        }
        View view = LayoutInflater.from(this).inflate(
                R.layout.include_chat_voice_short, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 判断sd卡是否存在
     */
    public static boolean checkSdCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }
}
