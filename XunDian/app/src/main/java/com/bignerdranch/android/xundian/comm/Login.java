package com.bignerdranch.android.xundian.comm;

/**
 * Created by Administrator on 2017/9/11.
 */

public class Login {
    private int mId;
    private String mToken;
    private String mTime;
    private String mZhangHao;
    private String mMiMa;
    private int mIsBaoCun;

    public void setIsBaoCun(int isBaoCun) {
        mIsBaoCun = isBaoCun;
    }

    public int getIsBaoCun() {
        return mIsBaoCun;
    }

    public void setZhangHao(String zhangHao) {
        mZhangHao = zhangHao;
    }

    public void setMiMa(String miMa) {
        mMiMa = miMa;
    }

    public String getZhangHao() {
        return mZhangHao;
    }

    public String getMiMa() {
        return mMiMa;
    }

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
