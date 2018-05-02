package com.example.im.home.find;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.im.R;
import com.example.im.chat.ChatActivity;
import com.example.im.entity.OnlineUser;
import com.example.im.entity.User;

import java.util.List;

/**
 * Created by ganchenqing on 2018/4/24.
 */

public class FindUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OnlineUser.OnLineUserData> mUserList;
    private Context mContext;

    public FindUserAdapter(Context mContext, List<OnlineUser.OnLineUserData> mUserList) {
        this.mUserList = mUserList;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_online_user, parent, false);
        return new OnLineUserHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((OnLineUserHolder) holder).mTvName.setText(mUserList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public static class OnLineUserHolder extends RecyclerView.ViewHolder {
        RelativeLayout mRlOnlineUser;
        ImageView mIvAvatar;
        TextView mTvName;

        public OnLineUserHolder(View itemView) {
            super(itemView);
            mRlOnlineUser = itemView.findViewById(R.id.rl_online_user);
            mIvAvatar = itemView.findViewById(R.id.iv_avatar);
            mTvName = itemView.findViewById(R.id.tv_name);
            mIvAvatar.setImageResource(R.mipmap.head);
            mRlOnlineUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String chatUserName = mTvName.getText().toString();
                    Intent intent = new Intent(view.getContext(), ChatActivity.class);
                    intent.putExtra("chatname", chatUserName);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

}
