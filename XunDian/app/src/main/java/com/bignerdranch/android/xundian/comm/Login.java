package com.bignerdranch.android.xundian.comm;

/**
 * Created by Administrator on 2017/9/11.
 */

public class Login {
    private int mId;
    private String mToken;
    private String mTime;


    public void setId(int id) {
        mId = id;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public int getId() {
        return mId;
    }

    public String getToken() {
        return mToken;
    }

    public String getTime() {
        return mTime;
    }
}
