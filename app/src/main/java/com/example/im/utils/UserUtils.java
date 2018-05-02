package com.example.im.utils;

import android.content.Context;

/**
 * Created by ganchenqing on 2018/5/1.
 */

public class UserUtils {

    public static String getCurrentUser(Context context) {
        SPUtil spUtil = new SPUtil(context);
        return spUtil.getString("username", "");
    }
}
