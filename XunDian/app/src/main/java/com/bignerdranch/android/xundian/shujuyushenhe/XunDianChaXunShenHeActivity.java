package com.bignerdranch.android.xundian.shujuyushenhe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.kaoqing.KaoQingCommonActivity;

/**
 * Created by Administrator on 2017/11/16.
 */

public class XunDianChaXunShenHeActivity extends KaoQingCommonActivity {

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.XunDianChaXunShenHeActivity";

    private String mMenDianDataJson = "";

    public static Intent newIntent(Context packageContext,String string){
        Intent i = new Intent(packageContext,XunDianChaXunShenHeActivity.class);
        i.putExtra(EXTRA,string);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shu_xun_dian_cha_xun_shen_he);
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
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText(R.string.xun_dian_shen_he);
    }

    /**
     * 值操作
     */
    public void values() {
        // Token赋值
        setToken(mContext);
        // 门店信息接收
        mMenDianDataJson = getIntent().getStringExtra(EXTRA);
        Log.i("巡店",mMenDianDataJson);
    }

}
