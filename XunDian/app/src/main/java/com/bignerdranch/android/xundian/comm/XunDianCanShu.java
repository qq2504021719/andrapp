package com.bignerdranch.android.xundian.comm;

/**
 * Created by Administrator on 2017/9/12.
 */

public class XunDianCanShu {

    // 标题
    private String mNname;
    // 填写方式
    private String mTian_xie_fang_shi;
    // 是否拍照
    private String mIs_pai_zhao;
    // 必填 1 0
    private int mIs_bi_tian;
    // 选择器项
    private String[] mXuan_ze_qi;

    public void setNname(String nname) {
        mNname = nname;
    }

    public void setTian_xie_fang_shi(String tian_xie_fang_shi) {
        mTian_xie_fang_shi = tian_xie_fang_shi;
    }

    public void setIs_pai_zhao(String is_pai_zhao) {
        mIs_pai_zhao = is_pai_zhao;
    }

    public void setIs_bi_tian(int is_bi_tian) {
        mIs_bi_tian = is_bi_tian;
    }

    public void setXuan_ze_qi(String[] xuan_ze_qi) {
        mXuan_ze_qi = xuan_ze_qi;
    }

    public String getNname() {
        return mNname;
    }

    public String getTian_xie_fang_shi() {
        return mTian_xie_fang_shi;
    }

    public String getIs_pai_zhao() {
        return mIs_pai_zhao;
    }

    public int getIs_bi_tian() {
        return mIs_bi_tian;
    }

    public String[] getXuan_ze_qi() {
        return mXuan_ze_qi;
    }
}
