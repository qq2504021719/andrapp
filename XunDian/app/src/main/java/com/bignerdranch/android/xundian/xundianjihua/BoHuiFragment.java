package com.bignerdranch.android.xundian.xundianjihua;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.NeiYeCommActivity;

/**
 * Created by Administrator on 2017/9/20.
 */

public class BoHuiFragment extends Fragment {

    private View mView;

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_bo_hui, container, false);
        mContext = getActivity();
        // 组件初始化
        ZhuJianInit();
        // 组件操作
        ZhuJianCaoZhuo();
        // 数据/值设置
        values();

        return mView;
    }
    /**
     * 组件初始化
     */
    public void ZhuJianInit(){

    }
    /**
     * 值操作
     */
    public void values(){
        // Token赋值

    }
    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){

    }



}
