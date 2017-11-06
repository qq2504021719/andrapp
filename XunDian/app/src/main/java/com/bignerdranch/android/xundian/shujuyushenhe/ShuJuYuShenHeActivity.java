package com.bignerdranch.android.xundian.shujuyushenhe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.xundian.R;

/**
 * Created by Administrator on 2017/10/19.
 */

public class ShuJuYuShenHeActivity extends ShuJuYuShenHeCommActivity{


    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.ShuJuYuShenHeActivity";

    // 巡店查询
    private LinearLayout mXun_dian_cha_xun;
    // 拜访查询
    private LinearLayout mBai_fang_cha_xun;
    // 计划审核
    private LinearLayout mJi_hua_shen_he;
    // 请假审核
    private LinearLayout mQing_jia_shen_he;

    public static Intent newIntent(Context packageContext, int intIsId){
        Intent i = new Intent(packageContext,ShuJuYuShenHeActivity.class);
        i.putExtra(EXTRA,intIsId);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shu_ju_yu_shen_he);

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
        // 巡店查询
        mXun_dian_cha_xun = (LinearLayout)findViewById(R.id.xun_dian_cha_xun);
        // 拜访查询
        mBai_fang_cha_xun = (LinearLayout)findViewById(R.id.bai_fang_cha_xun);
        // 计划审核
        mJi_hua_shen_he = (LinearLayout)findViewById(R.id.ji_hua_shen_he);
        // 请假审核
        mQing_jia_shen_he = (LinearLayout)findViewById(R.id.qing_jia_shen_he);
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
        mTitle_nei_ye.setText(R.string.gong_zuo_zhong_xin_shu_ju_yu_shen_he);

        // 巡店查询
        mXun_dian_cha_xun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = XunDianChaXunActivity.newIntent(mContext,1);
                startActivity(i);
            }
        });

        // 拜访查询
        mBai_fang_cha_xun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = BaiFangChaXunActivity.newIntent(mContext,1);
                startActivity(i);
            }
        });

        // 计划审核
        mJi_hua_shen_he.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = JiHuaShenHeActivity.newIntent(mContext,1);
                startActivity(i);
            }
        });
        // 请假审核
        mQing_jia_shen_he.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = QingJiaShenHeActivity.newIntent(mContext,1);
                startActivity(i);
            }
        });
    }

}
