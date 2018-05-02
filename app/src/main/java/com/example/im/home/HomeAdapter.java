package com.example.im.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.im.R;
import com.example.im.chat.ChatActivity;
import com.example.im.entity.Contact;
import com.example.im.home.find.FindUserActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ganchenqing on 2018/3/29.
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CONTENT = 1;

    private List<String> mList = new ArrayList<>();
    private Context mContext;

    public HomeAdapter(Context context, List<String> mList) {
        this.mContext = context;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CONTENT) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_home, parent, false);
            return new HomeViewHolder(v);
        } else {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_online_user, parent, false);
            return new OnLineHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position != 0) {
            ((HomeViewHolder) holder).mTv.setText(mList.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else return TYPE_CONTENT;
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {

        TextView mTv;

        public HomeViewHolder(final View itemView) {
            super(itemView);
            mTv = (TextView) itemView.findViewById(R.id.tv_recent_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), ChatActivity.class));
                }
            });
        }
    }

    public static class OnLineHolder extends RecyclerView.ViewHolder {
        RelativeLayout mRlOnlineUser;

        public OnLineHolder(View itemView) {
            super(itemView);
            mRlOnlineUser = itemView.findViewById(R.id.rl_online_user);
            mRlOnlineUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.getContext().startActivity(new Intent(view.getContext(), FindUserActivity.class));
                }
            });
        }
    }

}
