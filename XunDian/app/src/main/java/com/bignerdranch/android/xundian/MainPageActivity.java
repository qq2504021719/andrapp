package com.bignerdranch.android.xundian;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.bignerdranch.android.xundian.comm.AtyContainer;
import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.comm.NeiYeCommActivity;
import com.bignerdranch.android.xundian.comm.WeiboDialogUtils;
import com.bignerdranch.android.xundian.model.LoginModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/1.
 */

public class MainPageActivity extends AppCompatActivity implements TongZhiZhongXinFragment.Callbacks{

    private static final String EXTRA_VIEW_ID = "com.bignerdranch.android.MainPageActivity.xundian.view_id";

    private int GPS_REQUEST_CODE = 10;

    private ViewPager mViewPager;
    private List<Viewd> mViewds;
    private int mViewId;

    // Fragmen集合
    private ViewS mViewS;

    // 底部按钮
    private RadioButton mRb1;
    private RadioButton mRb2;
    private RadioButton mRb3;
    private RadioButton mRb4;
    // 头部标题
    private TextView mTextViewTitle;
    private RadioGroup mRgroup;

    private FragmentManager mManager;

    // 通知公告红点提示
    public View mBar_yuan_view;

    private String permissionInfo;
    private final int SDK_PERMISSION_REQUEST = 127;

    // 开启线程
    public Thread mThread;
    // Token
    public String mToken;

    // LoginModel 登录模型
    public LoginModel mLoginModel;
    // 登录对象
    public Login mLogin;

    public String MURL = Config.URL+"/app/get_tong_zhi_gong_gao";

    private static final String CLIENT_ID = "2";
    private static final String CLIENT_SECRET = "S4rOJxiKfd4Ch3SuOPaq6ZBNTMg9ixuoehEMVEsg";
    // access_token url
    private static final String mAccessMURL = Config.URL+"/oauth/token";

    public static Intent newIntent(Context packageContext, int viewId){
        Intent i = new Intent(packageContext,MainPageActivity.class);
        i.putExtra(EXTRA_VIEW_ID,viewId);
        return i;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main_page_view_page);


        // 销毁其余容器
        AtyContainer.finishAllActivity();

        // 组件初始化
        ZhuJianInit();
        // 组件操作, 操作
        ZhuJianCaoZhuo();

        getPersimmions();
        // 值操作
        values();


    }

    /**
     * 值操作 set get值
     */
    public void values() {
        // new登录模型
        mLoginModel = LoginModel.get(this);
        // Token查询,赋值
        mLogin = mLoginModel.getLogin(1);
        mToken = mLogin.getToken();
        // 获取公告通知,是否显示红点
        getTongZhiGongGao();
        // 重新请求登录信息
        qingQiuDengLuXingXi();

        openGPSSettings();
    }

    /**
     * 检测GPS是否打开
     *
     * @return
     */
    private boolean checkGPSIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    /**
     * 跳转GPS设置
     */
    private void openGPSSettings() {
        if (checkGPSIsOpen()) {
//            initLocation(); //自己写的定位方法
        } else {
            //没有打开则弹出对话框
            new AlertDialog.Builder(this)
                    .setTitle(R.string.notifyTitle)
                    .setMessage(R.string.gpsNotifyMsg)
                    // 拒绝, 退出应用
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })

                    .setPositiveButton(R.string.setting,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //跳转GPS设置界面
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivityForResult(intent, GPS_REQUEST_CODE);
                                }
                            })

                    .setCancelable(false)
                    .show();

        }
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
			/*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            // 拍照权限
            if (addPermission(permissions, Manifest.permission.CAMERA)) {
                permissionInfo += "Manifest.permission.CAMERA Deny \n";
            }


            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }
    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }
    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("定位","定位");
    }

    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        /*
         * 初始化按钮
         */
        mRb1 = (RadioButton)findViewById(R.id.rb_1);
        mRb2 = (RadioButton)findViewById(R.id.rb_2);
        mRb3 = (RadioButton)findViewById(R.id.rb_3);
        mRb4 = (RadioButton)findViewById(R.id.rb_4);
        mTextViewTitle = (TextView)findViewById(R.id.title);
        mRgroup = (RadioGroup)findViewById(R.id.rg);

        mBar_yuan_view = (View)findViewById(R.id.bar_yuan_view);
    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        // 设置红点
//        TongZhiZhongXinFragment tongZhiZhongXinFragment = new TongZhiZhongXinFragment();
//        tongZhiZhongXinFragment.shanChuHongDian();

        /*
         * 为四个按钮添加监听
         */
        mRb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(1);
            }
        });
        mRb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(2);
            }
        });
        mRb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(3);
            }
        });
        mRb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(4);
            }
        });

        /*
         * 默认选中第一个
         */
        mViewId = getIntent().getIntExtra(EXTRA_VIEW_ID,1);
        mRgroup.check(R.id.rb_1);

        mViewPager = (ViewPager) findViewById(R.id.activity_main_page_view_page);
        mViewS = new ViewS();
        mViewS.init();
        mViewds = mViewS.getViewS();

        mManager = getSupportFragmentManager();
        showFragment(mViewId);

        // 通知公告红点浮动,不占据位置
        mBar_yuan_view.bringToFront();

    }

    public void showFragment(int viewId){
        mViewId = viewId;
        /**
         * Fragment生成
         */
        mViewPager.setAdapter(new FragmentStatePagerAdapter(mManager) {
            @Override
            public Fragment getItem(int position) {
                Viewd viewd = mViewds.get(position);
                return viewd.getViewFragment();
            }


            @Override
            public int getCount() {
                return mViewds.size();
            }
        });

        /**
         * ViewPage监听
         */
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                showRgroup(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /**
         * 显示对应页面
         */
        for(int i = 0;i<mViewds.size();i++){
            if(mViewds.get(i).getViewid() == mViewId){
                mViewPager.setCurrentItem(i);
                showRgroup(i);
                break;
            }
        }
    }


    /**
     * 选择对应Rgroup,设置title
     * @param id
     */
    public void showRgroup(int id){
        int titles = 0;
        switch (id){
            case 0:
                TuBiaoQieHuan(0);
                mRgroup.check(R.id.rb_1);
                Drawable top1 = getResources().getDrawable(R.drawable.zhuye_blue);
                top1.setBounds(0, 0, top1.getMinimumWidth(), top1.getMinimumHeight());
                mRb1.setCompoundDrawables(null,top1,null , null);
                titles = R.string.footer_bar_1;
                break;
            case 1:
                TuBiaoQieHuan(1);
                mRgroup.check(R.id.rb_2);
                Drawable top2 = getResources().getDrawable(R.drawable.jindu_blue);
                top2.setBounds(0, 0, top2.getMinimumWidth(), top2.getMinimumHeight());
                mRb2.setCompoundDrawables(null,top2,null , null);
                titles = R.string.footer_bar_2;
                break;
            case 2:
                TuBiaoQieHuan(2);
                mRgroup.check(R.id.rb_3);
                Drawable top3 = getResources().getDrawable(R.drawable.tongzhi_blue);
                top3.setBounds(0, 0, top3.getMinimumWidth(), top3.getMinimumHeight());
                mRb3.setCompoundDrawables(null,top3,null , null);
                titles = R.string.footer_bar_3;
                break;
            case 3:
                TuBiaoQieHuan(3);
                mRgroup.check(R.id.rb_4);
                Drawable top4 = getResources().getDrawable(R.drawable.gr_center_blue);
                top4.setBounds(0, 0, top4.getMinimumWidth(), top4.getMinimumHeight());
                mRb4.setCompoundDrawables(null,top4,null , null);
                titles = R.string.footer_bar_4;
                break;
            default:
                break;
        }
        mTextViewTitle.setText(titles);
    }

    /**
     * 根据id设置底部图片
     * @param id
     */
    public void TuBiaoQieHuan(int id){
        switch (id){
            case 0:
                Drawable top1 = getResources().getDrawable(R.drawable.zhuye_blue);
                top1.setBounds(0, 0, top1.getMinimumWidth(), top1.getMinimumHeight());
                mRb1.setCompoundDrawables(null,top1,null , null);
                Drawable top12 = getResources().getDrawable(R.drawable.jindu);
                top12.setBounds(0, 0, top12.getMinimumWidth(), top12.getMinimumHeight());
                mRb2.setCompoundDrawables(null,top12,null , null);
                Drawable top13 = getResources().getDrawable(R.drawable.tongzhi);
                top13.setBounds(0, 0, top13.getMinimumWidth(), top13.getMinimumHeight());
                mRb3.setCompoundDrawables(null,top13,null , null);
                Drawable top14 = getResources().getDrawable(R.drawable.grcenter);
                top14.setBounds(0, 0, top14.getMinimumWidth(), top14.getMinimumHeight());
                mRb4.setCompoundDrawables(null,top14,null , null);
                break;
            case 1:
                Drawable top21 = getResources().getDrawable(R.drawable.zhuye);
                top21.setBounds(0, 0, top21.getMinimumWidth(), top21.getMinimumHeight());
                mRb1.setCompoundDrawables(null,top21,null , null);
                Drawable top22 = getResources().getDrawable(R.drawable.jindu_blue);
                top22.setBounds(0, 0, top22.getMinimumWidth(), top22.getMinimumHeight());
                mRb2.setCompoundDrawables(null,top22,null , null);
                Drawable top33 = getResources().getDrawable(R.drawable.tongzhi);
                top33.setBounds(0, 0, top33.getMinimumWidth(), top33.getMinimumHeight());
                mRb3.setCompoundDrawables(null,top33,null , null);
                Drawable top44 = getResources().getDrawable(R.drawable.grcenter);
                top44.setBounds(0, 0, top44.getMinimumWidth(), top44.getMinimumHeight());
                mRb4.setCompoundDrawables(null,top44,null , null);
                break;
            case 2:
                Drawable top31 = getResources().getDrawable(R.drawable.zhuye);
                top31.setBounds(0, 0, top31.getMinimumWidth(), top31.getMinimumHeight());
                mRb1.setCompoundDrawables(null,top31,null , null);
                Drawable top32 = getResources().getDrawable(R.drawable.jindu);
                top32.setBounds(0, 0, top32.getMinimumWidth(), top32.getMinimumHeight());
                mRb2.setCompoundDrawables(null,top32,null , null);
                Drawable top333 = getResources().getDrawable(R.drawable.tongzhi_blue);
                top333.setBounds(0, 0, top333.getMinimumWidth(), top333.getMinimumHeight());
                mRb3.setCompoundDrawables(null,top333,null , null);
                Drawable top34 = getResources().getDrawable(R.drawable.grcenter);
                top34.setBounds(0, 0, top34.getMinimumWidth(), top34.getMinimumHeight());
                mRb4.setCompoundDrawables(null,top34,null , null);
                break;
            case 3:
                Drawable top41 = getResources().getDrawable(R.drawable.zhuye);
                top41.setBounds(0, 0, top41.getMinimumWidth(), top41.getMinimumHeight());
                mRb1.setCompoundDrawables(null,top41,null , null);
                Drawable top42 = getResources().getDrawable(R.drawable.jindu);
                top42.setBounds(0, 0, top42.getMinimumWidth(), top42.getMinimumHeight());
                mRb2.setCompoundDrawables(null,top42,null , null);
                Drawable top43 = getResources().getDrawable(R.drawable.tongzhi);
                top43.setBounds(0, 0, top43.getMinimumWidth(), top43.getMinimumHeight());
                mRb3.setCompoundDrawables(null,top43,null , null);
                Drawable top444 = getResources().getDrawable(R.drawable.gr_center_blue);
                top444.setBounds(0, 0, top444.getMinimumWidth(), top444.getMinimumHeight());
                mRb4.setCompoundDrawables(null,top444,null , null);
                break;
            default:
                break;
        }
    }

    public void IsHong(int num){
        // 通知公告回调
        if(num == 1){
            // 隐藏通知公告红点
            mBar_yuan_view.setVisibility(View.INVISIBLE);
        }else if(num == 2){
            // 显示通知公告红点
            mBar_yuan_view.setVisibility(View.VISIBLE);
        }
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
                if(msg.obj.toString().equals("0")){
                    IsHong(2);
                }else if(msg.obj.toString().equals("1")){
                    IsHong(1);
                }
            }else if(msg.what==2){
                try {
                    // 解析json数据
//                    Log.i("巡店",msg.obj.toString());
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    int i = 0;
                    Iterator inter =  jsonObject.keys();
                    while(inter.hasNext()){
                        String a = (String)inter.next();
                        if(a.equals("access_token")){
                            i = 1;
                        }
                    }
                    if(i == 1){
                        // 修改
                        Log.i("巡店","修改");
                        mLogin.setToken(jsonObject.getString("access_token"));
                        mLoginModel.updateLogin(mLogin);
                    }else{
                        Log.i("巡店","跳转");
                        Intent intent = new Intent(MainPageActivity.this,LoginActivity.class);
                        startActivity(intent);
                        // 销毁当前Activity
                        MainPageActivity.this.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 获取用户未读通知公告
     */
    public void getTongZhiGongGao(){
        if(!mToken.isEmpty()){
            final OkHttpClient client = new OkHttpClient();

            MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
            body.addFormDataPart("is","1");

            final Request request = new Request.Builder()
                    .addHeader("Authorization","Bearer "+mToken)
                    .url(MURL)
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
    }

    /**
     * 重新请求 access_token
     */
    public void qingQiuDengLuXingXi(){
        if(!mLogin.getZhangHao().isEmpty() && !mLogin.getMiMa().isEmpty()){
            final OkHttpClient client = new OkHttpClient();
            //3, 发起新的请求,获取返回信息
            RequestBody body = new FormBody.Builder()
                    .add("username", mLogin.getZhangHao())//添加键值对
                    .add("password", mLogin.getMiMa())
                    .add("client_id", CLIENT_ID)
                    .add("client_secret", CLIENT_SECRET)
                    .add("grant_type", "password")
                    .build();
            final Request request = new Request.Builder()
                    .url(mAccessMURL)
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
                        mHandler.obtainMessage(2, response.body().string()).sendToTarget();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            mThread.start();
        }
    }
}
