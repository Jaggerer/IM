package com.example.im.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.im.R;
import com.example.im.chat.ChatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ganchenqing on 2018/3/29.
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Contact> mList = new ArrayList<>();
    private Context mContext;

    public HomeAdapter(Context context, List<Contact> mList) {
        this.mContext = context;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_home, parent, false);
        return new HomeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((HomeViewHolder) holder).mTv.setText(mList.get(position).getUserID());
    }

    @Override
    public int getItemCount() {
        return mList.size();
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

}
