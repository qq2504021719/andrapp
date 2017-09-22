package com.bignerdranch.android.xundian.comm;

/**
 * Created by Administrator on 2017/9/22.
 */

public class ChaoShi {

    // 门店id
    private int mId;
    // 是否超时
    private int mIsChaoShi;
    // 超时时间
    private int mChaoShi;
    // 剩余时时间
    private int mWeiChaoShi;
    // 总时间
    private int mZhongShi;

    public void setZhongShi(int zhongShi) {
        mZhongShi = zhongShi;
    }

    public int getZhongShi() {
        return mZhongShi;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setIsChaoShi(int isChaoShi) {
        mIsChaoShi = isChaoShi;
    }

    public void setChaoShi(int chaoShi) {
        mChaoShi = chaoShi;
    }

    public void setWeiChaoShi(int weiChaoShi) {
        mWeiChaoShi = weiChaoShi;
    }

    public int getId() {
        return mId;
    }

    public int getIsChaoShi() {
        return mIsChaoShi;
    }

    public int getChaoShi() {
        return mChaoShi;
    }

    public int getWeiChaoShi() {
        return mWeiChaoShi;
    }
}
