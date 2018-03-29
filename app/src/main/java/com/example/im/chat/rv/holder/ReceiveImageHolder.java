package com.example.im.chat.rv.holder;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 接收到的文本类型
 */
public class ReceiveImageHolder extends BaseViewHolder<Message> {

    private ImageView mIvAvatar;

    private TextView mTvTime;

    private ImageView mIvPicture;
    private ProgressBar mPbLoad;

    private Message message;

    public ReceiveImageHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_chat_received_image, onRecyclerViewListener);
    }

    @Override
    public void bindData(Message o) {
        initView();
        message = o;
        String avatar = getAvatarFromServer(message.getFromId());
        ImageLoaderFactory.getLoader().loadAvator(mIvAvatar, avatar, R.mipmap.head);
        String time = TimeUtils.formatTime(message.getCreateTime(), "yyyy年MM月dd日 HH:mm");
        mTvTime.setText(time);
        //显示图片
        ImageLoaderFactory.getLoader().load(mIvPicture, message.getFileDir(), R.mipmap.ic_launcher, new ImageLoadingListener() {
            ;

            @Override
            public void onLoadingStarted(String s, View view) {
                mPbLoad.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                mPbLoad.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                mPbLoad.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                mPbLoad.setVisibility(View.INVISIBLE);
            }
        });

        mIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击" + message.getToId() + "的头像");
            }
        });

        mIvPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击图片:" + message.getFileDir() + "");
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemClick(getAdapterPosition());
                }
            }
        });

        mIvPicture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemLongClick(getAdapterPosition());
                }
                return true;
            }
        });

    }

    private void initView() {
        mIvAvatar = itemView.findViewById(R.id.iv_avatar);
        mIvPicture = itemView.findViewById(R.id.iv_picture);
        mPbLoad = itemView.findViewById(R.id.progress_load);
        mTvTime = itemView.findViewById(R.id.tv_time);
    }

    public void showTime(boolean isShow) {
        mTvTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public String getAvatarFromServer(String id) {
        return null;
    }

}