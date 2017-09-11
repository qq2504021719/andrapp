package com.bignerdranch.android.xundian;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigamole.library.ShadowLayout;

/**
 * Created by Administrator on 2017/9/11.
 */

public class WodeXinXiFragment extends Fragment {

    // 当前Fragment的View
    private View mView;


    private ShadowLayout mSl;
    private ShadowLayout mSl_button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.wo_de_xin_xi, container, false);

        // 组件初始化
        ZhuJianInit();
        // 组件操作, 操作
        ZhuJianCaoZhuo();

        return mView;
    }

    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mSl = (ShadowLayout)mView.findViewById(R.id.sl);
        mSl_button = (ShadowLayout)mView.findViewById(R.id.sl_button);

    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        yinYingSheZhi(mSl);
        yinYingSheZhi(mSl_button);
    }

    /**
     * 阴影设置
     * @param view
     */
    public void yinYingSheZhi(ShadowLayout view){
        view.setIsShadowed(true);
        view.setShadowAngle(45);//阴影角度
        view.setShadowRadius(20);//阴影半径
        view.setShadowDistance(10);//阴影距离
        view.setShadowColor(R.color.huise);//阴影颜色
    }
}
