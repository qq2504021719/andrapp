package com.bignerdranch.android.xundian.comm;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/10/14.
 */

public class BaiFangGuanli {

    // 门店id
    public String mMenDianId;
    // 门店
    public String mMenDian;
    // 品牌id
    public String mPingPaiId;
    // 品牌
    public String mPinPai;
    // 门店号
    public String mMenDianHao;
    // 开始时间
    public String mKaiShiTime;
    // 结束时间
    public String mJieShuShiJian;
    // 照片1
    public File mPhont1;
    // 照片上传地址
    public String mShangChuanFilePath1;
    // 照片2
    public File mPhont2;
    // 照片上传地址
    public String mShangChuanFilePath2;
    // 照片3
    public File mPhont3;
    // 照片上传地址
    public String mShangChuanFilePath3;
    // 照片4
    public File mPhont4;
    // 照片上传地址
    public String mShangChuanFilePath4;
    // 拜访内容
    public String mBaiFangNeiRong;
    // 经度
    public String mLng;
    // 纬度
    public String mLat;
    // 地址信息
    public String mAddr;
    // 地址语义化
    public String mAddr1;

    public void setShangChuanFilePath1(String shangChuanFilePath1) {
        mShangChuanFilePath1 = shangChuanFilePath1;
    }

    public void setShangChuanFilePath2(String shangChuanFilePath2) {
        mShangChuanFilePath2 = shangChuanFilePath2;
    }

    public void setShangChuanFilePath3(String shangChuanFilePath3) {
        mShangChuanFilePath3 = shangChuanFilePath3;
    }

    public void setShangChuanFilePath4(String shangChuanFilePath4) {
        mShangChuanFilePath4 = shangChuanFilePath4;
    }

    public String getShangChuanFilePath1() {
        return mShangChuanFilePath1;
    }

    public String getShangChuanFilePath2() {
        return mShangChuanFilePath2;
    }

    public String getShangChuanFilePath3() {
        return mShangChuanFilePath3;
    }

    public String getShangChuanFilePath4() {
        return mShangChuanFilePath4;
    }

    public void setAddr1(String addr1) {
        mAddr1 = addr1;
    }

    public String getAddr1() {
        return mAddr1;
    }

    public void setMenDianId(String menDianId) {
        mMenDianId = menDianId;
    }

    public void setMenDian(String menDian) {
        mMenDian = menDian;
    }

    public void setPingPaiId(String pingPaiId) {
        mPingPaiId = pingPaiId;
    }

    public void setPinPai(String pinPai) {
        mPinPai = pinPai;
    }

    public void setMenDianHao(String menDianHao) {
        mMenDianHao = menDianHao;
    }

    public void setKaiShiTime(String kaiShiTime) {
        mKaiShiTime = kaiShiTime;
    }

    public void setJieShuShiJian(String jieShuShiJian) {
        mJieShuShiJian = jieShuShiJian;
    }

    public void setPhont1(File phont1) {
        mPhont1 = phont1;
    }

    public void setPhont2(File phont2) {
        mPhont2 = phont2;
    }

    public void setPhont3(File phont3) {
        mPhont3 = phont3;
    }

    public void setPhont4(File phont4) {
        mPhont4 = phont4;
    }

    public void setBaiFangNeiRong(String baiFangNeiRong) {
        mBaiFangNeiRong = baiFangNeiRong;
    }

    public void setLng(String lng) {
        mLng = lng;
    }

    public void setLat(String lat) {
        mLat = lat;
    }

    public void setAddr(String addr) {
        mAddr = addr;
    }

    public String getMenDianId() {
        return mMenDianId;
    }

    public String getMenDian() {
        return mMenDian;
    }

    public String getPingPaiId() {
        return mPingPaiId;
    }

    public String getPinPai() {
        return mPinPai;
    }

    public String getMenDianHao() {
        return mMenDianHao;
    }

    public String getKaiShiTime() {
        return mKaiShiTime;
    }

    public String getJieShuShiJian() {
        return mJieShuShiJian;
    }

    public File getPhont1() {
        return mPhont1;
    }

    public File getPhont2() {
        return mPhont2;
    }

    public File getPhont3() {
        return mPhont3;
    }

    public File getPhont4() {
        return mPhont4;
    }

    public String getBaiFangNeiRong() {
        return mBaiFangNeiRong;
    }

    public String getLng() {
        return mLng;
    }

    public String getLat() {
        return mLat;
    }

    public String getAddr() {
        return mAddr;
    }
}
