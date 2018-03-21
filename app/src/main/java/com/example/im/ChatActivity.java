package com.example.im;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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

import com.scrat.app.selectorlibrary.ImageSelector;

import java.util.ArrayList;
import java.util.List;

import static com.example.im.Constant.REQUEST_CODE_SELECT_IMG;

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

    private List<Message> messageList = new ArrayList<>();


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
            }
        });
        mTvSendCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast("发送拍照图片");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
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
}
