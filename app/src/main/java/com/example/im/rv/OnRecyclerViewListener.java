package com.example.im.rv;

/**
 * Created by ganchenqing on 2018/3/19.
 */

public interface OnRecyclerViewListener {
    void onItemClick(int position);

    boolean onItemLongClick(int position);
}
