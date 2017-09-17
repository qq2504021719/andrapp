package com.bignerdranch.android.xundian;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
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
import android.widget.TextView;

import com.bignerdranch.android.xundian.comm.AtyContainer;
import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.model.LoginModel;
import com.bignerdranch.android.xundian.ui.CircleImageView;
import com.gigamole.library.ShadowLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Administrator on 2017/9/11.
 */

public class WodeXinXiFragment extends Fragment {

    // 当前Fragment的View
    private View mView;
    private Button mTui_chu_button;


    private ShadowLayout mSl;
    private ShadowLayout mSl_button;

    private CircleImageView mWo_img;
    private TextView mWo_xing_ming;
    private TextView mWo_gong_si;
    private TextView mWo_gong_hao;
    private TextView mWo_zhi_wu;
    private TextView mWo_shou_ji;
    private TextView mWo_tong_zhi;
    private TextView mWo_qing_jia;
    private TextView mWo_guan_yu;


    // LoginModel 登录模型
    private static LoginModel mLoginModel;
    // 登录对象
    private static Login mLogin;

    // Token
    private String mToken;

    // 请求url
    private String mUserDataUrl = Config.URL+"/app/user";
    // 开启线程
    private static Thread mThread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.wo_de_xin_xi, container, false);

        // 值操作 set get值
        values();

        // 组件初始化
        ZhuJianInit();
        // 组件操作, 操作
        ZhuJianCaoZhuo();

        // 查看网络是否连接
        if(!isNetworkAvailableAndConnected()){
            String string = null;
            setUserView(string);
        }else{
            // 个人信息获取
            getRenXin();
        }



        return mView;
    }

    /**
     * 检查网络是否完全连接 true 连接  false 没有连接
     * @return
     */
    private boolean isNetworkAvailableAndConnected(){
        ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
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
                // 个人信息回调
                setUserView(msg.obj.toString());
            }
        }
    };

    public void getRenXin(){
        if(mToken != null){
            final OkHttpClient client = new OkHttpClient();
            //3, 发起新的请求,获取返回信息
            RequestBody body = new FormBody.Builder()
                    .build();
            final Request request = new Request.Builder()
                    .addHeader("Authorization","Bearer "+mToken)
                    .url(mUserDataUrl)
                    .post(body)
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
    }

    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mSl = (ShadowLayout)mView.findViewById(R.id.sl);
        mSl_button = (ShadowLayout)mView.findViewById(R.id.sl_button);
        mTui_chu_button = (Button)mView.findViewById(R.id.tui_chu_button);

        mWo_img = (CircleImageView)mView.findViewById(R.id.wo_img);

        mWo_xing_ming = (TextView)mView.findViewById(R.id.wo_xing_ming);
        mWo_gong_si = (TextView)mView.findViewById(R.id.wo_gong_si);
        mWo_gong_hao = (TextView)mView.findViewById(R.id.wo_gong_hao);
        mWo_zhi_wu = (TextView)mView.findViewById(R.id.wo_zhi_wu);
        mWo_shou_ji = (TextView)mView.findViewById(R.id.wo_shou_ji);
        mWo_tong_zhi = (TextView)mView.findViewById(R.id.wo_tong_zhi);
        mWo_qing_jia = (TextView)mView.findViewById(R.id.wo_qing_jia);
        mWo_guan_yu = (TextView)mView.findViewById(R.id.wo_guan_yu);

    }

    /**
     * 设置用户信息
     * @param string 用户信息json
     */
    public void setUserView(String string){
        String name = "姓名 : 小郑";
        if(string == null){
            // 头像
            mWo_img.setImageResource(R.drawable.timg);

        }else{
            try {
                JSONObject jsonObject = new JSONObject(string);
                Log.i("巡店:",string);
                // 姓名
                if(jsonObject.getString("name") != null){
                    name = "姓名 : "+jsonObject.getString("name");
                }
                // 头像
                if(jsonObject.getString("touXiang") != null) {
                    //图片资源
                    String url = Config.URL+"/"+jsonObject.getString("touXiang");
                    Picasso.with(getActivity()).load(url).into(mWo_img);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 姓名
        mWo_xing_ming.setText(name);


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
