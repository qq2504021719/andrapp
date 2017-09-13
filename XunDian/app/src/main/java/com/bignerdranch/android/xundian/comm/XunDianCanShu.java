package com.bignerdranch.android.xundian.comm;

import android.view.View;

import com.bignerdranch.android.xundian.LoginActivity;

import java.io.File;

/**
 * Created by Administrator on 2017/9/12.
 */

public class XunDianCanShu {

    // 标题
    private String mName;
    // 填写方式
    private String mTian_xie_fang_shi;
    // 是否拍照
    private String mIs_pai_zhao;
    // 必填 1 0
    private int mIs_bi_tian;
    // 选择器项 json格式
    private String mXuan_ze_qi;
    // 组件对象标识
    private int mId;
    // 值
    private String mValue;
    // 本地图片存储地址
    private File mPhotoFile;
    // 图片显示View
    private View mImageView;

    public View getImageView() {
        return mImageView;
    }

    public void setImageView(View imageView) {
        mImageView = imageView;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setPhotoFile(File photoFile) {
        mPhotoFile = photoFile;
    }

    public File getPhotoFile() {
        return mPhotoFile;
    }


    public void setValue(String value) {
        mValue = value;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getValue() {
        return mValue;
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

    public void setXuan_ze_qi(String xuan_ze_qi) {
        mXuan_ze_qi = xuan_ze_qi;
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

    public String getXuan_ze_qi() {
        return mXuan_ze_qi;
    }


}
