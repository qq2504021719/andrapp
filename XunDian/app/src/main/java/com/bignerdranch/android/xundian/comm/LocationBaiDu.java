package com.bignerdranch.android.xundian.comm;

import com.bignerdranch.android.xundian.LoginActivity;
import com.bignerdranch.android.xundian.xundianguanli.XunDianGuanLiActivity;

/**
 * Created by Administrator on 2017/9/8.
 */

public class LocationBaiDu {

    // 定位时间
    private String mDingQeiTime;

    // 维度
    private double mLatitude;

    // 经度
    private double mLontitude;

    // 半径
    private float mRadius;

    // 地址
    private String mAddr;

    // 描述
    private String mDescribe;

    // 语义化结果
    private String mLocationDescribe;

    // 用户选择门店id
    private int mMenDianId;
    // 用户选择品牌id
    private int mMenDianPingPaiId;
    // 门店名称
    private String mMenDianMingCheng;
    // 门店编号
    private String mBianHao;

    public void setBianHao(String bianHao) {
        mBianHao = bianHao;
    }

    public String getBianHao() {
        return mBianHao;
    }

    public void setMenDianMingCheng(String menDianMingCheng) {
        mMenDianMingCheng = menDianMingCheng;
    }

    public String getMenDianMingCheng() {
        return mMenDianMingCheng;
    }

    public int getMenDianId() {
        return mMenDianId;
    }

    public int getMenDianPingPaiId() {
        return mMenDianPingPaiId;
    }

    public void setMenDianId(int menDianId) {
        mMenDianId = menDianId;
    }

    public void setMenDianPingPaiId(int menDianPingPaiId) {
        mMenDianPingPaiId = menDianPingPaiId;
    }

    public void setDingQeiTime(String dingQeiTime) {
        mDingQeiTime = dingQeiTime;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public void setLontitude(double lontitude) {
        mLontitude = lontitude;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }

    public void setAddr(String addr) {
        mAddr = addr;
    }

    public void setDescribe(String describe) {
        mDescribe = describe;
    }

    public void setLocationDescribe(String locationDescribe) {
        mLocationDescribe = locationDescribe;
    }

    public String getDingQeiTime() {
        return mDingQeiTime;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLontitude() {
        return mLontitude;
    }

    public float getRadius() {
        return mRadius;
    }

    public String getAddr() {
        return mAddr;
    }

    public String getDescribe() {
        return mDescribe;
    }

    public String getLocationDescribe() {
        return mLocationDescribe;
    }


}
