package com.example.im.chat.rv.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.im.entity.MyMessage;
import com.example.im.R;
import com.example.im.chat.rv.NewRecordPlayClickListener;
import com.example.im.chat.rv.OnRecyclerViewListener;
import com.example.im.utils.imageloaderutil.ImageLoaderFactory;
import com.example.im.utils.TimeUtils;

import static com.example.im.Constant.SENDING;
import static com.example.im.Constant.SEND_FAILED;

/**
 * 发送的语音类型
 */
public class SendVoiceHolder extends BaseViewHolder<MyMessage> {

    private ImageView mIvAvatar;

    private ImageView mIvFailresend;

    private TextView mTvtime;

    private TextView mTvVoiceLength;
    private ImageView mIvVoice;

    private TextView mTvSendtatus;

    private ProgressBar mPbProgressLoad;

    private MyMessage myMessage;

    public SendVoiceHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_chat_sent_voice, onRecyclerViewListener);
    }

    @Override
    public void bindData(MyMessage m) {
        initView();
        myMessage = m;

        String avatar = getAvatarFromServer(m.getFromId());
        ImageLoaderFactory.getLoader().loadAvator(mIvAvatar, avatar, R.mipmap.head);

        String time = TimeUtils.formatTime(myMessage.getCreateTime(), "yyyy年MM月dd日 HH:mm");
        mTvtime.setText(time);

//获得语音长度
        int length = myMessage.getRecorderLength();
        mTvVoiceLength.setText(String.valueOf(length));

        int status = myMessage.getSendStatus();
        if (status == SEND_FAILED) {
            mIvFailresend.setVisibility(View.VISIBLE);
            mPbProgressLoad.setVisibility(View.GONE);
            mTvSendtatus.setVisibility(View.INVISIBLE);
            mTvVoiceLength.setVisibility(View.INVISIBLE);
        } else if (status == SENDING) {
            mPbProgressLoad.setVisibility(View.VISIBLE);
            mIvFailresend.setVisibility(View.GONE);
            mTvSendtatus.setVisibility(View.INVISIBLE);
            mTvVoiceLength.setVisibility(View.INVISIBLE);
        } else {//发送成功
            mIvFailresend.setVisibility(View.GONE);
            mPbProgressLoad.setVisibility(View.GONE);
            mTvSendtatus.setVisibility(View.GONE);
            mTvVoiceLength.setVisibility(View.VISIBLE);
        }

        mIvVoice.setOnClickListener(new NewRecordPlayClickListener(getContext(), myMessage, mIvVoice));

        mIvVoice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemLongClick(getAdapterPosition());
                }
                return true;
            }
        });

        mIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击" + myMessage.getFromId() + "的头像");
            }
        });
        //todo 重发
        mIvFailresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initView() {
        mIvAvatar = itemView.findViewById(R.id.iv_avatar);
        mIvFailresend = itemView.findViewById(R.id.iv_fail_resend);
        mIvVoice = itemView.findViewById(R.id.iv_voice);
        mTvVoiceLength = itemView.findViewById(R.id.tv_voice_length);
        mTvSendtatus = itemView.findViewById(R.id.tv_send_status);
        mPbProgressLoad = itemView.findViewById(R.id.progress_load);
        mTvtime = itemView.findViewById(R.id.tv_time);
    }

    public void showTime(boolean isShow) {
        mTvtime.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public String getAvatarFromServer(String id) {
        return null;
    }

}
