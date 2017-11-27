package com.bignerdranch.android.xundian.shujuyushenhe;

import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/11/27.
 */

public class Phone {

    // 标题
    public String mTitle;

    // 图片地址
    public String mPath;

    // 底部view
    public LinearLayout mLinearLayout;

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        mLinearLayout = linearLayout;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPath() {
        return mPath;
    }

    public LinearLayout getLinearLayout() {
        return mLinearLayout;
    }
}
