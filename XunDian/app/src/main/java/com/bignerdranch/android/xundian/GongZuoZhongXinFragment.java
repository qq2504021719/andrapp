package com.bignerdranch.android.xundian;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.kaoqing.KaoQingActivity;
import com.bignerdranch.android.xundian.model.LoginModel;
import com.bignerdranch.android.xundian.shujuyushenhe.ShuJuYuShenHeActivity;
import com.bignerdranch.android.xundian.xundianguanli.XunDianGuanLiActivity;
import com.bignerdranch.android.xundian.xundianjihua.JiHuaActivity;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.bignerdranch.android.xundian.LoginActivity.mHandler;

/**
 * Created by Administrator on 2017/9/7.
 */

public class GongZuoZhongXinFragment extends Fragment{


    // 工作中心按钮
    // 巡店管理
    public View mXun_dian_guan_li_LinearLayout;

    // 巡店计划
    public LinearLayout mButton_ji_hua;

    // 考勤
    public LinearLayout mGong_kao_qing_linearLayout;

    // 数据与审核
    public LinearLayout mShu_ju_yu_shen_he_linearLayout;

    // 客户拓展
    public LinearLayout mButton_ke_hu;

    private View mView;

    // 权限名称
    public String mQuanXianName = "";
    // 跳转页面
    public Intent mI;

    // Token
    public String mToken = null;

    // LoginModel 登录模型
    public static LoginModel mLoginModel = null;
    // 登录对象
    public static Login mLogin = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_gong_zuo_zhong_xin, container, false);

        // 组件初始化
        ZhuJianInit();
        // 组件操作, 操作
        ZhuJianCaoZhuo();

        // 登录数据库连接
        mLoginModel = LoginModel.get(getActivity());
        // Token查询,赋值
        mLogin = mLoginModel.getLogin(1);
        mToken = mLogin.getToken();



        return mView;
    }

    // 开启线程
    public static Thread mThread = null;
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
                    Toast.makeText(getActivity(),"无权限", Toast.LENGTH_SHORT).show();
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
        // 初始化
        mXun_dian_guan_li_LinearLayout = mView.findViewById(R.id.xun_dian_guan_li_LinearLayout);
        // 巡店计划
        mButton_ji_hua = (LinearLayout)mView.findViewById(R.id.button_ji_hua);
        // 考勤
        mGong_kao_qing_linearLayout = (LinearLayout)mView.findViewById(R.id.gong_kao_qing_linearLayout);
        // 数据与审核
        mShu_ju_yu_shen_he_linearLayout = (LinearLayout)mView.findViewById(R.id.shu_ju_yu_shen_he_llinearLayout);
        // 客户拓展
        mButton_ke_hu = (LinearLayout)mView.findViewById(R.id.button_ke_hu);
    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){

        // 巡店管理
        mXun_dian_guan_li_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 存储容器
//                AtyContainer.addActivity(getActivity());
                Intent i = XunDianGuanLiActivity.newIntent(getActivity(),1);
                startActivity(i);
            }
        });

        // 巡店计划
        mButton_ji_hua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = JiHuaActivity.newIntent(getActivity(),1);
                startActivity(i);
            }
        });

        // 考勤
        mGong_kao_qing_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = KaoQingActivity.newIntent(getActivity(),1);
                startActivity(i);
            }
        });

        // 数据与审核
        mShu_ju_yu_shen_he_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mI = ShuJuYuShenHeActivity.newIntent(getActivity(),1);
                mQuanXianName = "进入数据与审核页面";
                // 权限验证
                QunXianYanZheng();
            }
        });

        // 客户拓展
        mButton_ke_hu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = BaiFangGuanLiActivity.newIntent(getActivity(),1);
                startActivity(i);
            }
        });

    }


}
