package com.example.im.rv.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.im.Message;
import com.example.im.R;
import com.example.im.rv.OnRecyclerViewListener;
import com.example.im.utils.ImageLoaderFactory;
import com.example.im.utils.TimeUtils;

import static com.example.im.Constant.SENDING;
import static com.example.im.Constant.SEND_FAILED;

/**
 * 发送的文本类型
 */
public class SendImageHolder extends BaseViewHolder<Message> {

    private ImageView mIvAvatar;

    private ImageView mIvFailResend;

    private TextView mTvTime;

    private ImageView mIvPicture;

    private TextView mTvSendStatus;

    private ProgressBar mPbLoad;

    public SendImageHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_chat_sent_image, onRecyclerViewListener);
    }

    @Override
    public void bindData(Message o) {
        initView();
        final Message message = o;
        String avatar = getAvatarFromServer(message.getFromId());
        ImageLoaderFactory.getLoader().loadAvator(mIvAvatar, avatar, R.mipmap.head);
        String time = TimeUtils.formatTime(message.getCreateTime(), "yyyy年MM月dd日 HH:mm");
        mTvTime.setText(time);
        //
        int status = message.getSendStatus();

        if (status == SEND_FAILED) {
            mIvFailResend.setVisibility(View.VISIBLE);
            mPbLoad.setVisibility(View.GONE);
            mTvSendStatus.setVisibility(View.INVISIBLE);
        } else if (status == SENDING) {
            mPbLoad.setVisibility(View.VISIBLE);
            mIvFailResend.setVisibility(View.GONE);
            mTvSendStatus.setVisibility(View.INVISIBLE);
        } else {
            mTvSendStatus.setVisibility(View.VISIBLE);
            mTvSendStatus.setText("已发送");
            mIvFailResend.setVisibility(View.GONE);
            mPbLoad.setVisibility(View.GONE);
        }

        ImageLoaderFactory.getLoader().load(mIvPicture, message.getFileDir(), R.mipmap.ic_launcher, null);

        mIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击" + message.getFromId() + "的头像");
            }
        });
        mIvPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        //重发
        mIvFailResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                c.resendMessage(message, new MessageSendListener() {
//                    @Override
//                    public void onStart(BmobIMMessage msg) {
//                        mPbLoad.setVisibility(View.VISIBLE);
//                        mIvFailResend.setVisibility(View.GONE);
//                        mTvSendStatus.setVisibility(View.INVISIBLE);
//                    }
//
//                    @Override
//                    public void done(BmobIMMessage msg, BmobException e) {
//                        if (e == null) {
//                            mTvSendStatus.setVisibility(View.VISIBLE);
//                            mTvSendStatus.setText("已发送");
//                            mIvFailResend.setVisibility(View.GONE);
//                            mPbLoad.setVisibility(View.GONE);
//                        } else {
//                            mIvFailResend.setVisibility(View.VISIBLE);
//                            mPbLoad.setVisibility(View.GONE);
//                            mTvSendStatus.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                });
            }
        });
    }

    private void initView() {
        mIvAvatar = itemView.findViewById(R.id.iv_avatar);
        mIvPicture = itemView.findViewById(R.id.iv_picture);
        mPbLoad = itemView.findViewById(R.id.progress_load);
        mTvTime = itemView.findViewById(R.id.tv_time);
        mIvFailResend = itemView.findViewById(R.id.iv_fail_resend);
        mTvSendStatus = itemView.findViewById(R.id.tv_send_status);
    }

    public void showTime(boolean isShow) {
        mTvTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public String getAvatarFromServer(String id) {
        return null;
    }

}
