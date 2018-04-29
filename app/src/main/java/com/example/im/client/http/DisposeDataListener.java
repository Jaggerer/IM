package com.example.im.client.http;

/**
 * Created by ganchenqing on 2018/4/24.
 */

public interface DisposeDataListener<T> {
    /**
     * 请求成功回调事件处理
     */
    public void onSuccess(T t);

    /**
     * 请求失败回调事件处理
     */
    public void onFailure(Exception reasonObj);
}
