package com.bignerdranch.android.xundian.kaoqing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.xundianjihua.TJJiHuaActivity;

/**
 * Created by Administrator on 2017/9/26.
 */

public class KaoQingActivity extends KaoQingCommonActivity{

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.KaoQingActivity";

    // 日常考勤
    private LinearLayout mRi_chang_kao_qing;
    // 考勤记录
    private LinearLayout mKao_qing_ji_lu;
    // 请假管理
    private LinearLayout mQing_jia_guan_li;
    // 拜访管理
    private LinearLayout mPai_fang_guan_li;

    public static Intent newIntent(Context packageContext, int intIsId){
        Intent i = new Intent(packageContext,KaoQingActivity.class);
        i.putExtra(EXTRA,intIsId);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kao_qing);
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
        mRi_chang_kao_qing = (LinearLayout)findViewById(R.id.ri_chang_kao_qing);
        mKao_qing_ji_lu = (LinearLayout)findViewById(R.id.kao_qing_ji_lu);
        mQing_jia_guan_li = (LinearLayout)findViewById(R.id.qing_jia_guan_li);
        mPai_fang_guan_li = (LinearLayout)findViewById(R.id.pai_fang_guan_li);
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
        mTitle_nei_ye.setText(R.string.gong_zuo_zhong_xin_kao_qing);

        // 日常考勤
        mRi_chang_kao_qing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = RiChangKaoQingActivity.newIntent(mContext,1);
                startActivity(i);
            }
        });

        // 考勤记录
        mKao_qing_ji_lu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
