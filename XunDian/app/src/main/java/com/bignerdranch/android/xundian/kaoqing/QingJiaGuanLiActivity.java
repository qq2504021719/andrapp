package com.bignerdranch.android.xundian.kaoqing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.bignerdranch.android.xundian.R;

/**
 * Created by Administrator on 2017/9/29.
 */

public class QingJiaGuanLiActivity extends KaoQingCommonActivity{

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.QingJiaGuanLiActivity";
    public static Intent newIntent(Context packageContext, int intIsId){
        Intent i = new Intent(packageContext,QingJiaGuanLiActivity.class);
        i.putExtra(EXTRA,intIsId);
        return i;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kao_qing_jia_guan_li);
        mContext = this;
        // 组件初始化
        ZhuJianInit();
        // 组件操作
        ZhuJianCaoZhuo();
        // 数据/值设置
        values();
    }
    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mTitle_nei_ye = (TextView)findViewById(R.id.title_nei_ye);
    }
    /**
     * 值操作
     */
    public void values(){
        // Token赋值
        setToken(mContext);
    }
    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText(R.string.qing_jia_guan_li);
    }

}
