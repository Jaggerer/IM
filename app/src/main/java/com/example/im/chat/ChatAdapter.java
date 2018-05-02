package com.example.im.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.im.Constant;
import com.example.im.chat.rv.holder.BaseViewHolder;
import com.example.im.chat.rv.OnRecyclerViewListener;
import com.example.im.chat.rv.holder.ReceiveImageHolder;
import com.example.im.chat.rv.holder.ReceiveTextHolder;
import com.example.im.chat.rv.holder.ReceiveVoiceHolder;
import com.example.im.chat.rv.holder.SendImageHolder;
import com.example.im.chat.rv.holder.SendTextHolder;
import com.example.im.chat.rv.holder.SendVoiceHolder;
import com.example.im.db.bean.MyMessage;
import com.example.im.utils.UserUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.im.Constant.TYPE_RECEIVER_IMAGE;
import static com.example.im.Constant.TYPE_RECEIVER_TXT;
import static com.example.im.Constant.TYPE_RECEIVER_VOICE;
import static com.example.im.Constant.TYPE_SEND_IMAGE;
import static com.example.im.Constant.TYPE_SEND_TXT;
import static com.example.im.Constant.TYPE_SEND_VOICE;

/**
 * Created by ganchenqing on 2018/3/19.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MyMessage> mList = new ArrayList<>();
    private Context mContext;
    private String currentUid;

    /**
     * 显示时间间隔:10分钟
     */
    private final long TIME_INTERVAL = 10 * 60 * 1000;


    public ChatAdapter(Context context, List<MyMessage> list) {
        this.mList = list;
        this.mContext = context;
        //todo 从服务器获得当前用户的uid
        currentUid = getCurrentUid();
    }

    public int findPosition(MyMessage myMessage) {
        int index = this.getCount();
        int position = -1;
        while (index-- > 0) {
            if (myMessage.equals(this.getItem(index))) {
                position = index;
                break;
            }
        }
        return position;
    }

    public MyMessage getItem(int position) {
        return this.mList == null ? null : (position >= this.mList.size() ? null : this.mList.get(position));
    }

    public int getCount() {
        return this.mList == null ? 0 : this.mList.size();
    }

    public void remove(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    public MyMessage getFirstMessage() {
        if (null != mList && mList.size() > 0) {
            return mList.get(0);
        } else {
            return null;
        }
    }

    public void addMessages(List<MyMessage> myMessages) {
        mList.addAll(0, myMessages);
        notifyDataSetChanged();
    }

    public void addMessage(MyMessage myMessage) {
        mList.addAll(Arrays.asList(myMessage));
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SEND_TXT) {
            return new SendTextHolder(parent.getContext(), parent, onRecyclerViewListener);
        } else if (viewType == TYPE_SEND_IMAGE) {
            return new SendImageHolder(parent.getContext(), parent, onRecyclerViewListener);
        } else if (viewType == TYPE_SEND_VOICE) {
            return new SendVoiceHolder(parent.getContext(), parent, onRecyclerViewListener);
        } else if (viewType == TYPE_RECEIVER_TXT) {
            return new ReceiveTextHolder(parent.getContext(), parent, onRecyclerViewListener);
        } else if (viewType == TYPE_RECEIVER_IMAGE) {
            return new ReceiveImageHolder(parent.getContext(), parent, onRecyclerViewListener);
        } else if (viewType == TYPE_RECEIVER_VOICE) {
            return new ReceiveVoiceHolder(parent.getContext(), parent, onRecyclerViewListener);
        } else {
            Logger.e("viewType is null");
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder) holder).bindData(mList.get(position));
        if (holder instanceof ReceiveTextHolder) {
            ((ReceiveTextHolder) holder).showTime(shouldShowTime(position));
        } else if (holder instanceof ReceiveImageHolder) {
            ((ReceiveImageHolder) holder).showTime(shouldShowTime(position));
        } else if (holder instanceof ReceiveVoiceHolder) {
            ((ReceiveVoiceHolder) holder).showTime(shouldShowTime(position));
        } else if (holder instanceof SendTextHolder) {
            ((SendTextHolder) holder).showTime(shouldShowTime(position));
        } else if (holder instanceof SendImageHolder) {
            ((SendImageHolder) holder).showTime(shouldShowTime(position));
        } else if (holder instanceof SendVoiceHolder) {
            ((SendVoiceHolder) holder).showTime(shouldShowTime(position));
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MyMessage myMessage = mList.get(position);
        Logger.d("myMessage.getMessageType() --> " + myMessage.getMessageType());
        if (myMessage.getMessageType() == MyMessage.TYPE_PIC) {
            return myMessage.getFromId().equals(currentUid) ? TYPE_SEND_IMAGE : TYPE_RECEIVER_IMAGE;
        } else if (myMessage.getMessageType() == MyMessage.TYPE_VOICE) {
            return myMessage.getFromId().equals(currentUid) ? TYPE_SEND_VOICE : TYPE_RECEIVER_VOICE;
        } else if (myMessage.getMessageType() == MyMessage.TYPE_STRING) {
            return myMessage.getFromId().equals(currentUid) ? TYPE_SEND_TXT : TYPE_RECEIVER_TXT;
        } else {
            return -1;
        }
    }

    public String getCurrentUid() {
        return UserUtils.getCurrentUser(mContext);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private boolean shouldShowTime(int position) {
        if (position == 0) {
            return true;
        }
        long lastTime = mList.get(position - 1).getCreateTime();
        long curTime = mList.get(position).getCreateTime();
        return curTime - lastTime > TIME_INTERVAL;
    }
}
