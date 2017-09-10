package com.bignerdranch.android.xundian.comm;

/**
 * Created by Administrator on 2017/9/10.
 * 通知类
 */

public class TongZhi {

    private int mId; // id
    private String mTitle;// 标题
    private String mTime;// 时间
    private String mContent;// 内容
    private Boolean mIsChaKan; // 是否查看

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public Boolean getChaKan() {
        return mIsChaKan;
    }

    public void setChaKan(Boolean chaKan) {
        mIsChaKan = chaKan;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getTime() {
        return mTime;
    }

    public String getContent() {
        return mContent;
    }
}
