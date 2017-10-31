package com.bignerdranch.android.xundian.shujuyushenhe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.WeiboDialogUtils;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/10/19.
 */

public class QingJiaShenHeActivity extends ShuJuYuShenHeCommActivity implements ShuJuYuShenHeCommActivity.Callbacks{

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.QingJiaShenHeActivity";

    // 数据未审核
    private LinearLayout mLinear_wei_shen_he;

    // 数据已审核
    private LinearLayout mLinear_yi_shen_he;

    // 开始时间
    private String mKaiSiTime;
    // 结束时间
    private String mJieshuTime;

    // 未审核数据请求URL
    private String mWeiShenHeURL = Config.URL+"/app/qing_jia_wei_shen_he_data";
    // 请求已审核数据URl
    private String mYiShenHeURL = Config.URL+"/app/qing_jia_yi_shen_he_data";

    // 审核数据提交
    private String mShenHeURL = Config.URL+"/app/qing_jia_shen_he";

    // 提交状态
    public String mTiJiaoZhuangTai = "";

    public static Intent newIntent(Context packageContext, int intIsId){
        Intent i = new Intent(packageContext,QingJiaShenHeActivity.class);
        i.putExtra(EXTRA,intIsId);
        return i;
    }




    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shu_qing_jia_shen_he);
        mContext = this;
        // 组件初始化
        ZhuJianInit();
        // 组件操作
        ZhuJianCaoZhuo();
        // 数据/值设置
        values();
    }

    @Override
    public void onAttachedToWindow(){
        mCallbacksc = (ShuJuYuShenHeCommActivity.Callbacks)mContext;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 销毁回调
        mCallbacksc = null;
        // 弹窗销毁
        if(dialog != null){
            dialog.dismiss();

        }
    }

    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mTitle_nei_ye = (TextView)findViewById(R.id.title_nei_ye);

        // 数据未审核
        mLinear_wei_shen_he = (LinearLayout)findViewById(R.id.linear_wei_shen_he);

        // 数据已审核
        mLinear_yi_shen_he = (LinearLayout)findViewById(R.id.linear_yi_shen_he);
    }
    /**
     * 值操作
     */
    public void values(){
        LoadingStringEdit("加载中...");
        // Token赋值
        setToken(mContext);
        // 请求未审核数据
        qingQiuWeiShenHeShuJu();
        // 请求已审核数据
        qingQiuYiShenHeShuJu();

        mIsShenHe = 1;
    }
    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText(R.string.qing_jia_shen_he);
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
                // 显示未审核数据
                if(!msg.obj.toString().equals("")){
                    mActivityLeiXing = 0;
                    QingJiaDataShow(msg.obj.toString(),mLinear_wei_shen_he);
                }
            }else if(msg.what==2){
                if(msg.obj.toString().equals("审核成功")){
                    // 请求未审核数据
                    qingQiuWeiShenHeShuJu();
                    // 请求已审核数据
                    qingQiuYiShenHeShuJu();
                }
                tiShi(mContext,msg.obj.toString());
            }else if(msg.what==3){
                // 显示未审核数据
                if(!msg.obj.toString().equals("")){
                    mActivityLeiXing = 1;
                    QingJiaDataShow(msg.obj.toString(),mLinear_yi_shen_he);
                }
                // 关闭loading
                WeiboDialogUtils.closeDialog(mWeiboDialog);
            }
        }
    };

    /**
     * 请求未审核数据
     */
    public void qingQiuWeiShenHeShuJu(){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("xxx","xxx");
        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mWeiShenHeURL)
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
     * 请求已审核数据
     */
    public void qingQiuYiShenHeShuJu(){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("xxx","xxx");
        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mYiShenHeURL)
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
                    mHandler.obtainMessage(3, response.body().string()).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mThread.start();
    }

    @Override
    public void shuJuHuiDiao(String string, int is) {

    }

    @Override
    public void dingWeiData() {

    }

    /**
     * 用户同意不同意回调
     * @param string 同意/不同意
     * @param id 请假id
     */
    @Override
    public void weiShenHe(String string, int id,String BString) {
        if(string.trim().equals("同意") || string.trim().equals("不同意")){
            mTiJiaoZhuangTai = string.trim();
            ShenHeTiJiao(string,id,BString);
        }else{
            tiShi(mContext,"异常,请稍后再试");
        }
    }

    public void ShenHeTiJiao(String string, int id,String BString){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);

        String BuTongString = "";
        if(!BString.equals("")){
            BuTongString = BString;
        }

        body.addFormDataPart("zhuang_tai",string);
        body.addFormDataPart("id",id+"");
        body.addFormDataPart("bu_tong_yi_yuan_yin",BuTongString);

        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mShenHeURL)
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
                    mHandler.obtainMessage(2, response.body().string()).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mThread.start();
    }
}
