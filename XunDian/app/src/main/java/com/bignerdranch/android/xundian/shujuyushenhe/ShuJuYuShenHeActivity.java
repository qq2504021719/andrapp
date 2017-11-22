package com.bignerdranch.android.xundian.shujuyushenhe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.Config;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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


    // 权限名称
    public String mQuanXianName = "";
    // 跳转页面
    public Intent mI;

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
     * Handler
     */
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            /**
             *  msg.obj
             */
            if(msg.what==1){
                if(msg.obj.toString().equals("无")){
                    tiShi(mContext,"无权限");
                }else{
                    startActivity(mI);
                }

            }
        }
    };

    public void QunXianYanZheng(){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("name",mQuanXianName);
        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(Config.URL+"/app/UserDataQuanXian")
                .post(body.build())
                .build();
        //新建一个线程，用于得到服务器响应的参数
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    //回调
                    response = client.newCall(request).execute();
                    //将服务器响应的参数response.body().string())发送到hanlder中，并更新ui
                    mHandler.obtainMessage(1, response.body().string()).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mThread.start();
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
                mI = XunDianChaXunActivity.newIntent(mContext,"0");
                mQuanXianName = "进入巡店查询页面";
                // 权限验证
                QunXianYanZheng();
            }
        });

        // 拜访查询
        mBai_fang_cha_xun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mI = BaiFangChaXunActivity.newIntent(mContext,1);
                mQuanXianName = "进入拜访查询页面";
                // 权限验证
                QunXianYanZheng();
            }
        });

        // 计划审核
        mJi_hua_shen_he.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mI = JiHuaShenHeActivity.newIntent(mContext,1);
                mQuanXianName = "进入Android计划审核页面";
                // 权限验证
                QunXianYanZheng();
            }
        });
        // 请假审核
        mQing_jia_shen_he.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mI = QingJiaShenHeActivity.newIntent(mContext,1);
                mQuanXianName = "进入Android请假审核页面";
                // 权限验证
                QunXianYanZheng();
            }
        });
    }

}
