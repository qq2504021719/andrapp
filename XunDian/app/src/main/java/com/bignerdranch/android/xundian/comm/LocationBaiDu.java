package com.bignerdranch.android.xundian.comm;

/**
 * Created by Administrator on 2017/9/8.
 */

public class LocationBaiDu {

    // 定位时间
    private String mDingQeiTime;

    //
    private double mLatitude;

    //
    private double mLontitude;

    // 半径
    private float mRadius;

    // 地址
    private String mAddr;

    // 描述
    private String mDescribe;

    // 语义化结果
    private String mLocationDescribe;

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
