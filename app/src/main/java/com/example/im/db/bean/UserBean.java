package com.example.im.db.bean;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by ganchenqing on 2018/4/30.
 */

public class UserBean extends RealmObject implements Cloneable {
    public String currentUserName;
    public RealmList<String> recentUserName;

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public RealmList<String> getRecentUserName() {
        return recentUserName;
    }

    public void setRecentUserName(RealmList<String> recentUserName) {
        this.recentUserName = recentUserName;
    }
}
