package com.example.im.chat.rv.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.im.db.bean.MyMessage;
import com.example.im.R;
import com.example.im.chat.rv.OnRecyclerViewListener;
import com.example.im.utils.imageloaderutil.ImageLoaderFactory;
import com.example.im.utils.TimeUtils;

/**
 * 发送的文本类型
 */
public class SendTextHolder extends BaseViewHolder<MyMessage> implements View.OnClickListener, View.OnLongClickListener {

    private ImageView mIvAvatar;

    private ImageView mIvFailResend;

    private TextView mTvTime;

    private TextView mTvMessage;
    private TextView mTvSendStatus;

    private ProgressBar mPbLoad;

    private MyMessage myMessage;


    public SendTextHolder(Context context, ViewGroup root, OnRecyclerViewListener listener) {
        super(context, root, R.layout.item_chat_sent_message, listener);
    }

    @Override
    public void bindData(MyMessage msg) {
        initView();

        myMessage = msg;
        String time = TimeUtils.formatTime(myMessage.getCreateTime(), "yyyy年MM月dd日 HH:mm");

//        todo 拿当前发送消息的用户头像
        String avatar = getAvatarFromServer(myMessage.getToId());

        ImageLoaderFactory.getLoader().loadAvator(mIvAvatar, avatar, R.mipmap.ic_launcher);


        String content = myMessage.getContent();

        mTvMessage.setText(content);
        mTvTime.setText(time);


//       todo 发送失败、发送成功、发送中

        int status = myMessage.getSendStatus();

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
                toast("点击" + myMessage.getContent());
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
                toast("点击" + myMessage.getFromId() + "的头像");
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

    private void initView() {
        mIvAvatar = itemView.findViewById(R.id.iv_avatar);
        mTvMessage = itemView.findViewById(R.id.tv_message);
        mTvTime = itemView.findViewById(R.id.tv_time);
        mIvFailResend = itemView.findViewById(R.id.iv_fail_resend);
        mPbLoad = itemView.findViewById(R.id.progress_load);
        mTvSendStatus = itemView.findViewById(R.id.tv_send_status);
    }

    public void showTime(boolean isShow) {
        mTvTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public String getAvatarFromServer(String id) {
        return null;
    }
}
