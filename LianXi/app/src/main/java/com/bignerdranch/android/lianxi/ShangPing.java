package com.bignerdranch.android.lianxi;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2017/8/12.
 */

public class ShangPing {
    private UUID mId;
    private String mTitle;
    private Date mAddTime;
    private boolean mIsXiHuan;

    public ShangPing(){
        // 生成唯一标识符
        mId = UUID.randomUUID();
        // 设置默认添加时间
        mAddTime = new Date();
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setAddTime(Date addTime) {
        mAddTime = addTime;
    }

    public void setXiHuan(boolean xiHuan) {
        mIsXiHuan = xiHuan;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public Date getAddTime() {
        return mAddTime;
    }

    public boolean isXiHuan() {
        return mIsXiHuan;
    }
}
