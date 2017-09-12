package com.bignerdranch.android.xundian.comm;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/9/12.
 */

public class NeiYeCommActivity extends AppCompatActivity {

    public TextView mTitle_nei_ye; // 设置显示标题

    // json转换
    public Gson mGson = new Gson();

    /**
     * 点击返回
     * @param v
     */
    public void DianJiFanHui(View v){
        finish();
    }
}
