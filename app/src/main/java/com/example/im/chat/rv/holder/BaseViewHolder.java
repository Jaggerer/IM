package com.example.im.chat.rv.holder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.im.chat.rv.OnRecyclerViewListener;

/**
 * Created by ganchenqing on 2018/3/19.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public OnRecyclerViewListener onRecyclerViewListener;
    protected Context context;

    public BaseViewHolder(Context context, ViewGroup root, int layoutRes, OnRecyclerViewListener listener) {
        super(LayoutInflater.from(context).inflate(layoutRes, root, false));
        this.context = context;
        this.onRecyclerViewListener = listener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public abstract void bindData(T t);

    @Override
    public void onClick(View view) {
        if (onRecyclerViewListener != null) {
            onRecyclerViewListener.onItemClick(getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (onRecyclerViewListener != null) {
            onRecyclerViewListener.onItemClick(getAdapterPosition());
        }
        return true;
    }

    public Context getContext() {
        return itemView.getContext();
    }

    private Toast toast;
    public void toast(final Object obj) {
        try {
            ((Activity)context).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (toast == null)
                        toast = Toast.makeText(context,"", Toast.LENGTH_SHORT);
                    toast.setText(obj.toString());
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
