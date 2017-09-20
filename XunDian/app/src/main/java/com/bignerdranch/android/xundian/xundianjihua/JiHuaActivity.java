package com.bignerdranch.android.xundian.xundianjihua;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.NeiYeCommActivity;
import com.bignerdranch.android.xundian.xundianguanli.XunDianXiangXiActivity;

/**
 * Created by Administrator on 2017/9/19.
 */

public class JiHuaActivity extends NeiYeCommActivity {
    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.JiHuaActivity";

    // 计划安排
    private LinearLayout mJi_hua_an_pai;
    // 我的日程
    private LinearLayout mWo_de_ri_cheng;

    public static Intent newIntent(Context packageContext, int intIsId){
        Intent i = new Intent(packageContext,JiHuaActivity.class);
        i.putExtra(EXTRA,intIsId);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_xun_dian_ji_hua);

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
        mJi_hua_an_pai = (LinearLayout)findViewById(R.id.ji_hua_an_pai);
        mWo_de_ri_cheng = (LinearLayout)findViewById(R.id.wo_de_ri_cheng);
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
        mTitle_nei_ye.setText(R.string.xun_dian_ji_hua);

        // 计划安排
        mJi_hua_an_pai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = TJJiHuaActivity.newIntent(mContext,1);
                startActivity(i);
            }
        });

        // 我的日程
        mWo_de_ri_cheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = RiChengActivity.newIntent(mContext,1);
                startActivity(i);
            }
        });
    }

}
