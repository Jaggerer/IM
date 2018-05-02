package com.example.im.entity;

import java.util.List;

/**
 * Created by ganchenqing on 2018/5/1.
 */

public class OnlineUser {

    /**
     * data : [{"name":"asd","createTime":"Apr 29, 2018 11:39:16 PM","updateTime":"Apr 29, 2018 11:39:16 PM","userId":74}]
     * errorMesg :
     * success : true
     * status : 1
     */

    private String errorMesg;
    private boolean success;
    private int status;
    private List<OnLineUserData> data;

    public String getErrorMesg() {
        return errorMesg;
    }

    public void setErrorMesg(String errorMesg) {
        this.errorMesg = errorMesg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<OnLineUserData> getData() {
        return data;
    }

    public void setData(List<OnLineUserData> data) {
        this.data = data;
    }

    public static class OnLineUserData {
        /**
         * name : asd
         * createTime : Apr 29, 2018 11:39:16 PM
         * updateTime : Apr 29, 2018 11:39:16 PM
         * userId : 74
         */

        private String name;
        private String createTime;
        private String updateTime;
        private int userId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
