package com.bignerdranch.android.xundian.comm;

/**
 * Created by Administrator on 2017/10/11.
 */

public class QingJia {
    // 部门
    private String mBuMeng;
    // 请假类型
    private String mLeiXing;
    // 请假原因
    private String mYuanYing;
    // 按天请假
    private String mDay;
    // 按时间段请假
    private String mShiJianDuan;
    // 上午请假开始时间
    private String mShangWuKaiShi;
    // 上午请假结束时间
    private String mShangWuJieShu;
    // 下午请假开始时间
    private String mXiaWuKaiShi;
    // 下午请假结束时间
    private String mXiaWuJieShu;

    public void setShangWuKaiShi(String shangWuKaiShi) {
        mShangWuKaiShi = shangWuKaiShi;
    }

    public void setShangWuJieShu(String shangWuJieShu) {
        mShangWuJieShu = shangWuJieShu;
    }

    public void setXiaWuKaiShi(String xiaWuKaiShi) {
        mXiaWuKaiShi = xiaWuKaiShi;
    }

    public void setXiaWuJieShu(String xiaWuJieShu) {
        mXiaWuJieShu = xiaWuJieShu;
    }

    public String getShangWuKaiShi() {
        return mShangWuKaiShi;
    }

    public String getShangWuJieShu() {
        return mShangWuJieShu;
    }

    public String getXiaWuKaiShi() {
        return mXiaWuKaiShi;
    }

    public String getXiaWuJieShu() {
        return mXiaWuJieShu;
    }

    public void setBuMeng(String buMeng) {
        mBuMeng = buMeng;
    }

    public void setLeiXing(String leiXing) {
        mLeiXing = leiXing;
    }

    public void setYuanYing(String yuanYing) {
        mYuanYing = yuanYing;
    }

    public void setDay(String day) {
        mDay = day;
    }

    public void setShiJianDuan(String shiJianDuan) {
        mShiJianDuan = shiJianDuan;
    }

    public String getBuMeng() {
        return mBuMeng;
    }

    public String getLeiXing() {
        return mLeiXing;
    }

    public String getYuanYing() {
        return mYuanYing;
    }

    public String getDay() {
        return mDay;
    }

    public String getShiJianDuan() {
        return mShiJianDuan;
    }
}
