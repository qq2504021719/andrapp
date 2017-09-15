package com.bignerdranch.android.xundian;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bignerdranch.android.xundian.comm.AtyContainer;
import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.model.LoginModel;
import com.gigamole.library.ShadowLayout;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Administrator on 2017/9/11.
 */

public class WodeXinXiFragment extends Fragment {

    // 当前Fragment的View
    private View mView;
    private Button mTui_chu_button;


    private ShadowLayout mSl;
    private ShadowLayout mSl_button;

    // LoginModel 登录模型
    private static LoginModel mLoginModel;
    // 登录对象
    private static Login mLogin;

    // Token
    private String mToken;

    //


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.wo_de_xin_xi, container, false);

        // 值操作 set get值
        values();

        // 组件初始化
        ZhuJianInit();
        // 组件操作, 操作
        ZhuJianCaoZhuo();




        return mView;
    }


    /**
     * 值操作 set get值
     */
    public void values() {
        // new登录模型
        mLoginModel = LoginModel.get(getActivity());
        // Token查询,赋值
        mLogin = mLoginModel.getLogin(1);
        mToken = mLogin.getToken();
    }
        /**
         * Handler
         */
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            /**
             * 请求回调
             */
            if(msg.what==1){
                // 参数请求回调
            }
        }
    };

//    public void getRenXin(){
//        if(mToken != null){
//            final OkHttpClient client = new OkHttpClient();
//            //3, 发起新的请求,获取返回信息
//            RequestBody body = new FormBody.Builder()
//                    .build();
//            final Request request = new Request.Builder()
//                    .addHeader("Authorization","Bearer "+mToken)
//                    .url(mCanShuURL+mMenDianID)
//                    .post(body)
//                    .build();
//            //新建一个线程，用于得到服务器响应的参数
//            mThread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Response response = null;
//                    try {
//                        //回调
//                        response = client.newCall(request).execute();
//                        //将服务器响应的参数response.body().string())发送到hanlder中，并更新ui
//                        mHandler.obtainMessage(1, response.body().string()).sendToTarget();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            mThread.start();
//        }
//    }

    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mSl = (ShadowLayout)mView.findViewById(R.id.sl);
        mSl_button = (ShadowLayout)mView.findViewById(R.id.sl_button);
        mTui_chu_button = (Button)mView.findViewById(R.id.tui_chu_button);

    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        yinYingSheZhi(mSl);
        yinYingSheZhi(mSl_button);

        // 退出登录
        mTui_chu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 弹窗
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                // 获取布局文件
                LayoutInflater inflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewD = inflater.inflate(R.layout.wo_de_xin_xi_tui_chu_dialog, null);

                // 初始化组件
                Button tui_chu = viewD.findViewById(R.id.tui_chu_dialog_button);
                Button qie_huan = viewD.findViewById(R.id.qie_huan_dialog_button);

                // 设置View
                alertBuilder.setView(viewD);
                // 显示
                alertBuilder.create();
                alertBuilder.show();

                // 退出应用
                tui_chu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isShanChu();
                        ActivityManager manager = (ActivityManager)getActivity().getSystemService(ACTIVITY_SERVICE);
                        manager.killBackgroundProcesses(getActivity().getPackageName());
                    }
                });
                // 切换账号
                qie_huan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 删除账号信息
                        mLoginModel.deleteLogin(1);
                        // 存储容器
                        AtyContainer.addActivity(getActivity());
                        // 跳转登录
                        Intent i = LoginActivity.newIntent(getActivity(),1);
                        startActivity(i);
                    }
                });
            }
        });
    }

    /**
     * 验证是否删除账号信息
     */
    public void isShanChu(){
        mLogin = mLoginModel.getLogin(1);
//        Log.i("登录",""+mLogin.getIsBaoCun());
        if(mLogin.getIsBaoCun() == 2){
            mLoginModel.deleteLogin(1);
        }
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
