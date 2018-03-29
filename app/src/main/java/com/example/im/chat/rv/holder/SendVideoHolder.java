package com.example.im.chat.rv.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.im.chat.Message;
import com.example.im.R;
import com.example.im.chat.rv.OnRecyclerViewListener;
import com.example.im.utils.ImageLoaderFactory;
import com.example.im.utils.TimeUtils;


public class SendVideoHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private ImageView mIvAvatar;

    private ImageView mIvFailResend;

    private TextView mTvTime;

    private TextView mTvMessage;
    private TextView mTvSendStatus;

    private ProgressBar mPbLoad;

    Message message;

    public SendVideoHolder(Context context, ViewGroup root, OnRecyclerViewListener listener) {
        super(context, root, R.layout.item_chat_sent_message, listener);
    }

    @Override
    public void bindData(Object o) {
        initView();
        String avatar = getAvatarFromServer(message.getFromId());
        ImageLoaderFactory.getLoader().loadAvator(mIvAvatar, avatar, R.mipmap.head);

        String time = TimeUtils.formatTime(message.getCreateTime(), "yyyy年MM月dd日 HH:mm");

        String content = message.getContent();
        mTvMessage.setText("发送的视频文件：" + content);
        mTvTime.setText(time);

        int status = message.getSendStatus();

        mIvFailResend.setVisibility(View.GONE);
        mPbLoad.setVisibility(View.GONE);
//        if (status == SEND_FAILED) {
//            mIvFailResend.setVisibility(View.VISIBLE);
//            mPbLoad.setVisibility(View.GONE);
//        } else if (status == SENDING) {
//            mIvFailResend.setVisibility(View.GONE);
//            mPbLoad.setVisibility(View.VISIBLE);
//        } else {
//            mIvFailResend.setVisibility(View.GONE);
//            mPbLoad.setVisibility(View.GONE);
//        }

        mTvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击" + message.getContent());
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemClick(getAdapterPosition());
                }
            }
        });

        mTvMessage.setOnLongClickListener(new View.OnLongClickListener() {
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
                toast("点击" + message.getFromId() + "的头像");
            }
        });

        //重发
        mIvFailResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               do sth
            }
        });

    }

    public void showTime(boolean isShow) {
        mTvTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void initView() {
        mIvAvatar = itemView.findViewById(R.id.iv_avatar);
        mTvMessage = itemView.findViewById(R.id.tv_message);
        mTvTime = itemView.findViewById(R.id.tv_time);
        mIvFailResend = itemView.findViewById(R.id.iv_fail_resend);
        mPbLoad = itemView.findViewById(R.id.progress_load);
        mTvSendStatus = itemView.findViewById(R.id.tv_send_status);
    }

    public String getAvatarFromServer(String id) {
        return null;
    }

}
