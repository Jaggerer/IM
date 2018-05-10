package com.example.im.chat.rv.holder;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.im.client.nio.util.DownloadUtil;
import com.example.im.db.bean.MyMessage;
import com.example.im.R;
import com.example.im.chat.rv.NewRecordPlayClickListener;
import com.example.im.chat.rv.OnRecyclerViewListener;
import com.example.im.utils.imageloaderutil.ImageLoaderFactory;
import com.example.im.utils.TimeUtils;
import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * 接收到的文本类型
 */
public class ReceiveVoiceHolder extends BaseViewHolder<MyMessage> {

    private ImageView mIvAvatar;

    private ImageView mIvFailresend;

    private TextView mTvtime;

    private TextView mTvVoiceLength;
    private ImageView mIvVoice;

    private TextView mTvSendtatus;

    private ProgressBar mPbProgressLoad;

    private MyMessage myMessage;

    private static final String downloadPath = "voice";

    public ReceiveVoiceHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_chat_received_voice, onRecyclerViewListener);
    }

    @Override
    public void bindData(MyMessage o) {
        initView();
        myMessage = o;

        String avatar = getAvatarFromServer(myMessage.getFromId());
        ImageLoaderFactory.getLoader().loadAvator(mIvAvatar, avatar, R.mipmap.head);

        String time = TimeUtils.formatTime(myMessage.getCreateTime(), "yyyy年MM月dd日 HH:mm");
        mTvtime.setText(time);

        boolean isExists = isVoiceExists();


        if (!isExists) {//若指定格式的录音文件不存在，则需要下载，因为其文件比较小，故放在此下载
            DownloadUtil.get().download(myMessage.getContent(), downloadPath, new DownloadUtil.OnDownloadListener() {
                @Override
                public void onDownloadSuccess() {
                    Logger.d("success");
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPbProgressLoad.setVisibility(View.GONE);
                            mTvVoiceLength.setVisibility(View.VISIBLE);
                            mTvVoiceLength.setText(myMessage.getRecorderLength() + "\''");
                            mIvVoice.setVisibility(View.VISIBLE);
                            logMessageInfo();
                        }
                    });
                }

                @Override
                public void onDownloading(int progress) {
                    ((Activity) context).runOnUiThread(() -> {
                        mPbProgressLoad.setVisibility(View.VISIBLE);
                        mTvVoiceLength.setVisibility(View.GONE);
                        mIvVoice.setVisibility(View.INVISIBLE);//只有下载完成才显示播放的按钮
                        Logger.d(progress);
                    });

                }

                @Override
                public void onDownloadFailed() {
                    ((Activity) context).runOnUiThread(() -> {
                        Logger.d("wrong");
                        mPbProgressLoad.setVisibility(View.GONE);
                        mTvVoiceLength.setVisibility(View.GONE);
                        mIvVoice.setVisibility(View.INVISIBLE);
                    });
                }
            });
        } else {
            mTvVoiceLength.setVisibility(View.VISIBLE);
            mTvVoiceLength.setText(myMessage.getRecorderLength() + "\''");
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

    }

    private void logMessageInfo() {
        Logger.d(myMessage.getFileDir());
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

    public boolean isVoiceExists() {
        String fileName = DownloadUtil.get().getNameFromUrl(myMessage.getContent());
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + downloadPath + File.separator + fileName);
        return file.exists();
    }


}