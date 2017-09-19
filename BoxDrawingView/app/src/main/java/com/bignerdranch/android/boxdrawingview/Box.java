package com.bignerdranch.android.boxdrawingview;

import android.graphics.PointF;

/**
 * Created by Administrator on 2017/9/19.
 */

public class Box {

    private PointF mOrigin;

    private PointF mCurrent;

    public Box(PointF origin){
        mOrigin = origin;
        mCurrent = origin;
    }

    public PointF getCurrent(){
        return mCurrent;
    }

    public void setCurrent(PointF current){
        mCurrent = current;
    }

    public PointF getOrigin(){
        return mOrigin;
    }
}
