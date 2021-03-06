package com.example.im.chat.rv.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.im.db.bean.MyMessage;
import com.example.im.R;
import com.example.im.chat.rv.OnRecyclerViewListener;
import com.example.im.utils.imageloaderutil.ImageLoaderFactory;
import com.example.im.utils.TimeUtils;


/**
 * 接收到的文本类型
 */
public class ReceiveTextHolder extends BaseViewHolder<MyMessage> {

    private ImageView mIvAvatar;

    private TextView mTvTime;

    private TextView mTvMessage;

    private MyMessage myMessage;

    public ReceiveTextHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_chat_received_message, onRecyclerViewListener);
    }


    @Override
    public void bindData(MyMessage m) {
        initView();
        myMessage = m;
        String time = TimeUtils.formatTime(myMessage.getCreateTime(), "yyyy年MM月dd日 HH:mm");
        mTvTime.setText(time);

        //        todo 拿当前发送消息的用户头像
        String avatar = getAvatarFromServer(myMessage.getFromId());
        ImageLoaderFactory.getLoader().loadAvator(mIvAvatar, avatar, R.mipmap.head);

        String content = myMessage.getContent();
        mTvMessage.setText(content);
        mIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toast("点击" + myMessage.getFromId() + "的头像");
            }
        });
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
    }

    private String getAvatarFromServer(String id) {
        return null;
    }

    public void showTime(boolean isShow) {
        mTvTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }


    private void initView() {
        mTvMessage = itemView.findViewById(R.id.tv_message);
        mIvAvatar = itemView.findViewById(R.id.iv_avatar);
        mTvTime = itemView.findViewById(R.id.tv_time);
    }
}