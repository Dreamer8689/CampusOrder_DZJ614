package com.dreamer.neusoft.campusorder_dzj614.javaBean;

/**
 * Created by DZJ-PC on 2017/4/10.
 */

public class UserInfo {

    /**
     * user_id : 1
     * username : lnn
     * userpass : 6512bd43d9caa6e02c990b0a82652dca
     * mobilenum :  13476543211
     * address : å¤§è¿
     * comment : 测试
     */

    private int user_id;
    private String username;
    private String userpass;
    private String mobilenum;
    private String address;
    private String comment;

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }

    public void setMobilenum(String mobilenum) {
        this.mobilenum = mobilenum;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getUserpass() {
        return userpass;
    }

    public String getMobilenum() {
        return mobilenum;
    }

    public String getAddress() {
        return address;
    }

    public String getComment() {
        return comment;
    }
}
