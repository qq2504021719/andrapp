package com.bignerdranch.android.xundian.comm;

import android.support.annotation.NonNull;

/**
 * Created by Administrator on 2017/9/19.
 */

public class XunDianJiHua implements Comparable<XunDianJiHua>{

    // id
    public int mId;

    // 驳回修改后的记录 1 0
    public int mBoHuiXG = 0;

    // UID
    public int mUid;

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

    // 根据开始时间排序
    public int mOrderBy;

    // 是否完成 0 1
    public int mIsWC;

    // 日期 周
    public String mZhouStr;

    // 驳回原因
    public String mBoHuiYuanYi;

    // 状态
    public String mZhuangTai;

    public void setZhuangTai(String zhuangTai) {
        mZhuangTai = zhuangTai;
    }

    public String getZhuangTai() {
        return mZhuangTai;
    }

    public void setBoHuiXG(int boHuiXG) {
        mBoHuiXG = boHuiXG;
    }

    public int getBoHuiXG() {
        return mBoHuiXG;
    }

    public void setBoHuiYuanYi(String boHuiYuanYi) {
        mBoHuiYuanYi = boHuiYuanYi;
    }

    public String getBoHuiYuanYi() {
        return mBoHuiYuanYi;
    }

    public void setZhouStr(String zhouStr) {
        mZhouStr = zhouStr;
    }

    public String getZhouStr() {
        return mZhouStr;
    }

    public void setUid(int uid) {
        mUid = uid;
    }

    public int getUid() {
        return mUid;
    }

    public void setIsWC(int isWC) {
        mIsWC = isWC;
    }

    public int getIsWC() {
        return mIsWC;
    }

    public void setOrderBy(int orderBy) {
        mOrderBy = orderBy;
    }

    public int getOrderBy() {
        return mOrderBy;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
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


    @Override
    public int compareTo(@NonNull XunDianJiHua xunDianJiHua) {
        return this.getOrderBy()-xunDianJiHua.getOrderBy();
    }
}
