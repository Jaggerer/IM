package com.example.im.rv.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.im.Message;
import com.example.im.R;
import com.example.im.rv.NewRecordPlayClickListener;
import com.example.im.rv.OnRecyclerViewListener;
import com.example.im.utils.ImageLoaderFactory;
import com.example.im.utils.TimeUtils;

/**
 * 接收到的文本类型
 */
public class ReceiveVoiceHolder extends BaseViewHolder<Message> {

    private ImageView iv_avatar;
    private TextView mTvTime;
    private TextView mTvLength;
    private ImageView mIvVoice;

    private ProgressBar mPbLoad;

    private String currentUid = "";
    private String currentObjectId;

    private Message message;

    public ReceiveVoiceHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_chat_received_voice, onRecyclerViewListener);
        //todo 从服务器拿当前的id
//        currentUid = getCurrentObjectId();
    }

    @Override
    public void bindData(Message o) {
        message = o;
        //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'

        String avatar = getAvatarFromServer(message.getFromId());
        ImageLoaderFactory.getLoader().loadAvator(mIvVoice, avatar, R.mipmap.head);

        String time = TimeUtils.formatTime(message.getCreateTime(), "yyyy年MM月dd日 HH:mm");
        mTvTime.setText(time);

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击" + message.getFromId() + "的头像");
            }
        });
        // 声音下载存在本地格式为fromID+creatTime.amr
        boolean isExists = isVoiceExists();

        //todo 下载
//        if (!isExists) {//若指定格式的录音文件不存在，则需要下载，因为其文件比较小，故放在此下载
//
//
//            BmobDownloadManager downloadTask = new BmobDownloadManager(getContext(), msg, new FileDownloadListener() {
//                @Override
//                public void onStart() {
//                    mPbLoad.setVisibility(View.VISIBLE);
//                    mTvLength.setVisibility(View.GONE);
//                    mIvVoice.setVisibility(View.INVISIBLE);//只有下载完成才显示播放的按钮
//                }
//                @Override
//                public void done(BmobException e) {
//                    if (e == null) {
//                        mPbLoad.setVisibility(View.GONE);
//                        mTvLength.setVisibility(View.VISIBLE);
//                        mTvLength.setText(message.getDuration() + "\''");
//                        mIvVoice.setVisibility(View.VISIBLE);
//                    } else {
//                        mPbLoad.setVisibility(View.GONE);
//                        mTvLength.setVisibility(View.GONE);
//                        mIvVoice.setVisibility(View.INVISIBLE);
//                    }
//                }
//            });
//            downloadTask.execute(message.getContent());
//        } else {
//            mTvLength.setVisibility(View.VISIBLE);
//            mTvLength.setText(message.getDuration() + "\''");
//        }

        mIvVoice.setOnClickListener(new NewRecordPlayClickListener(getContext(), message, mIvVoice));

        mIvVoice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemLongClick(getAdapterPosition());
                }
                return true;
            }
        });

    }

    public void showTime(boolean isShow) {
        mTvTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public String getCurrentObjectId() {
        return currentObjectId;
    }

    public String getAvatarFromServer(String id) {
        return null;
    }

    public boolean isVoiceExists() {
        return false;
    }
}