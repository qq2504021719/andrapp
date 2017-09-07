package com.bignerdranch.android.xundian;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2017/9/1.
 */

public class Viewd {

    public Fragment mViewFragment;
    public int mViewid;

    public String mViewString;

    public String getViewString() {
        return mViewString;
    }

    public void setViewString(String viewString) {
        mViewString = viewString;
    }

    public Fragment getViewFragment() {
        return mViewFragment;
    }

    public int getViewid() {
        return mViewid;
    }

    public void setViewFragment(Fragment viewFragment) {
        mViewFragment = viewFragment;
    }

    public void setViewid(int viewid) {
        mViewid = viewid;
    }
}
