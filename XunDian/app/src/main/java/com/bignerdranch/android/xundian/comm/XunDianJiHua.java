package com.bignerdranch.android.xundian.comm;

/**
 * Created by Administrator on 2017/9/19.
 */

public class XunDianJiHua {

    // id
    public String mId;

    // 周
    public String mZhou;

    // 日期
    public String mRiQi;

    // 开始时间
    public String mShiJian;
    // 结束
    public String mJSShiJian;

    // 品牌
    public int mPingPaiId;

    // 门店id
    public int mMenDianId;

    // 品牌String
    public String mPingPaiStr;
    // 门店名称Str
    public String mMenDianStr;
    // 名称号str
    public String mMenDianHao;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setJSShiJian(String JSShiJian) {
        mJSShiJian = JSShiJian;
    }

    public String getJSShiJian() {
        return mJSShiJian;
    }

    public void setPingPaiStr(String pingPaiStr) {
        mPingPaiStr = pingPaiStr;
    }

    public void setMenDianStr(String menDianStr) {
        mMenDianStr = menDianStr;
    }

    public void setMenDianHao(String menDianHao) {
        mMenDianHao = menDianHao;
    }

    public String getPingPaiStr() {
        return mPingPaiStr;
    }

    public String getMenDianStr() {
        return mMenDianStr;
    }

    public String getMenDianHao() {
        return mMenDianHao;
    }

    public void setZhou(String zhou) {
        mZhou = zhou;
    }

    public void setRiQi(String riQi) {
        mRiQi = riQi;
    }

    public void setShiJian(String shiJian) {
        mShiJian = shiJian;
    }

    public void setPingPaiId(int pingPaiId) {
        mPingPaiId = pingPaiId;
    }

    public void setMenDianId(int menDianId) {
        mMenDianId = menDianId;
    }

    public String getZhou() {
        return mZhou;
    }

    public String getRiQi() {
        return mRiQi;
    }

    public String getShiJian() {
        return mShiJian;
    }

    public int getPingPaiId() {
        return mPingPaiId;
    }

    public int getMenDianId() {
        return mMenDianId;
    }
}
