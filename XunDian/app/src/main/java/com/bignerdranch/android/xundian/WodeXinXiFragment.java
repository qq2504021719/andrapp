package com.bignerdranch.android.xundian;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.xundian.comm.AtyContainer;
import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.FileSizeUtil;
import com.bignerdranch.android.xundian.comm.Installation;
import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.comm.WeiboDialogUtils;
import com.bignerdranch.android.xundian.model.LoginModel;
import com.bignerdranch.android.xundian.ui.CircleImageView;
import com.gigamole.library.ShadowLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;


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

    // 清除缓存
    private TextView mQing_chu_huan_cun;
    // 查看imei码
    public TextView mCha_kan_imei;
    public int imei = 0;


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

    // dialog,加载
    public static Dialog mWeiboDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_wo_de_xin_xi, container, false);

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


        LoadingStringEdit("加载中...");
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
//        Log.i("巡店",mToken);
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

        // 清除缓存
        mQing_chu_huan_cun = (TextView)mView.findViewById(R.id.qing_chu_huan_cun);
        // 查看imei
        mCha_kan_imei = (TextView)mView.findViewById(R.id.cha_kan_imei);

    }

    /**
     * 设置用户信息
     * @param string 用户信息json
     */
    public void setUserView(String string){
        String name = "姓名 : 小郑";
        String gongHao = "工号 : DD1001";
        String zhiWu = "职务 : 业务员";
        String phone = "手机: 18017217207";
        if(string == null){
            // 头像
            mWo_img.setImageResource(R.drawable.timg);

        }else{
            try {
                JSONObject jsonObject = new JSONObject(string);
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
                // 公司名称开始
                JSONArray jsongong_si = new JSONArray(jsonObject.getString("gong_si").toString());
                // 公司id
                String GsId = jsonObject.getString("logined_gongsi_id");
                String GongName = "";
                if(jsongong_si.length() > 1 && !GsId.isEmpty()){
                    for(int i = 0;i<jsongong_si.length();i++){
                        JSONObject jsonObject1 = null;
                        try {
                            jsonObject1 = new JSONObject(jsongong_si.get(i).toString());
                            if(jsonObject1.getString("id").equals(GsId)){
                                GongName = jsonObject1.getString("name");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }else if(jsongong_si.length() == 1){
                    JSONObject jsonObject1 = new JSONObject(jsongong_si.get(0).toString());
                    GongName = jsonObject1.getString("name");
                }

                if(!GsId.isEmpty()) mWo_gong_si.setText("公司 : "+GongName);
                // 公司名称结束

                // 工号
                if(jsonObject.getString("bian_hao") != null){
                    gongHao = "账号 : "+jsonObject.getString("bian_hao");
                }
                if(jsonObject.getString("roles") != null){
                    zhiWu = "工号 : "+jsonObject.getString("gong_hao");
                }

                // 手机号码
                if(jsonObject.getString("mobile_phone") != null){
                    phone = "手机 : "+jsonObject.getString("mobile_phone");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 姓名
        mWo_xing_ming.setText(name);
        // 工号
        mWo_gong_hao.setText(gongHao);
        // 职务
        mWo_zhi_wu.setText(zhiWu);
        //手机
        mWo_shou_ji.setText(phone);
        // 关闭loading
        WeiboDialogUtils.closeDialog(mWeiboDialog);
    }

    /**
     * 设置清除缓存栏目名
     */
    public void setQingChuHuanCun(){
        String path = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();

        double size = FileSizeUtil.getFileOrFilesSize(path,3);

        mQing_chu_huan_cun.setText("点击清除缓存:"+String.valueOf(size)+"MB");
    }

    /**
     * 清除 getExternalFilesDir 下的文件
     *
     *
     */
    public void deleteFilesByDirectory() {
        File externalFilesDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        for (File item : externalFilesDir.listFiles()) {
            item.delete();
        }
    }



    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        yinYingSheZhi(mSl);
        yinYingSheZhi(mSl_button);

        // 设置清除缓存栏目名
        setQingChuHuanCun();

        // 清除缓存
        mQing_chu_huan_cun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFilesByDirectory();
                LoginActivity.tiShi(getActivity(),"清除成功");
                // 设置清除缓存栏目名
                setQingChuHuanCun();
            }
        });
        // 查看imei
        mCha_kan_imei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imei == 1){
                    mCha_kan_imei.setText("点击查看唯一标识码");
                    imei = 0;
                }else if (imei == 0){
                    String imeiS = Installation.id(getActivity());
                    mCha_kan_imei.setText("唯一标识码:"+imeiS);
                    imei = 1;
                }
            }
        });

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

                // 退出应用 getPackageName
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

    /**
     * loading 浮层
     *
     * @param logingString 提示文字
     */
    public void LoadingStringEdit(String logingString){
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getContext(),logingString);
    }
}
