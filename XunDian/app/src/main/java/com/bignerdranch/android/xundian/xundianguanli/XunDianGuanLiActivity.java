package com.bignerdranch.android.xundian.xundianguanli;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.TongZhiZhongXinFragment;
import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.LocationBaiDu;
import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.comm.NeiYeCommActivity;
import com.bignerdranch.android.xundian.comm.XunDianCanShu;
import com.bignerdranch.android.xundian.kaoqing.KaoQingCommonActivity;
import com.bignerdranch.android.xundian.kaoqing.RiChangKaoQingActivity;
import com.bignerdranch.android.xundian.model.ChaoShiModel;
import com.bignerdranch.android.xundian.model.LoginModel;
import com.bignerdranch.android.xundian.model.XunDianModel;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/7.
 */

public class XunDianGuanLiActivity extends KaoQingCommonActivity implements SearchView.OnQueryTextListener,KaoQingCommonActivity.Callbacks{

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.XunDianGuanLiActivity";

    // 重新定位
    private ImageView mRi_chang_ding_wei;

    private Button mXun_dian_addr_button;

    private Button mXuan_zhe_men_dian_ping_pai_button; //选择门店品牌
    // 品牌alert View
    private View mViewPPD;
    // 品牌alert
    private LinearLayout mBf_search_men_dian_pp;
    public Dialog dialogpp = null;

    // 门店类型
    private Button mCha_xun_men_dian_lei_xing;
    private String mMenDianLeiXingUrl = Config.URL+"/app/ChaXunMenDianLeiXing";
    private String mMenDianLeiXingJsonData = "";
    private String mleiXingSearchName = "";
    // 门店类型alert view
    private View mMenDianView;
    // 门店类型alert
    private LinearLayout mBf_search_men_dian_lei_xing;
    // 门店类型 dialog
    public Dialog dialogMenDianLeiXing = null;

    private Button mXuan_zhe_men_dian_button; //选择门店
    // 公司alert View
    private View mViewD;
    // 公司alert
    private LinearLayout mBf_search_men_dian;
    public Dialog dialog = null;

    private Button mKai_shi_xun_dian_button; // 开始巡店
    private String mMen_Dian;// 门店


    private EditText mMen_dian_ming_cheng_searchview; // 门店搜索框

    private LocationBaiDu mLocationBaiDu = new LocationBaiDu(); //定位信息存储

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationMode mCurrentMode;
    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    boolean isFirstLoc = true; // 是否首次定位
    public boolean mIsDingWeiChengGong = false; // 定位是否成功

    private AlertDialog alertDialog1;

    // mXunDianModel 登录模型
    private static XunDianModel mXunDianModel;
    // 巡店数据
    public XunDianCanShu mXunDianCanShu;

    // LoginModel 登录模型
    private static LoginModel mLoginModel;
    private Login mLogin;

    // 用户开始巡店弹窗
    public String[] tishiS;

    // 巡店超时表
    public ChaoShiModel mChaoShiModel;

    /**
     * 封装创建Intent对象,并传入参数
     * @param packageContext
     * @param intIsId 1 来自工作中心启动
     * @return
     */
    public static Intent newIntent(Context packageContext,int intIsId){
        Intent i = new Intent(packageContext,XunDianGuanLiActivity.class);
        i.putExtra(EXTRA,intIsId);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_xun_dian_guan_li);

        mContext = this;

        // 连接数据库
        mXunDianModel = XunDianModel.get(mContext);

        // 组件初始化
        ZhuJianInit();

        // 组件操作
        ZhuJianCaoZhuo();
        // 数据/值设置
        values();
        // 设置选择数据
        setData(mMengDianPingpaiJsonData,1);
//        setData(mMengDianJsonData,2);

        // 百度地图定位调用 mMengDianPingpaiJsonData
        BaiDuDingWeiDiaoYong();


    }

    // 开启线程
    public static Thread mThread = null;
    /**
     * Handler
     */
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            /**
             *  msg.obj
             */
            if (msg.what == 1) {
                mMenDianLeiXingJsonData = msg.obj.toString();
                // 显示门店类型数据
                ShowMenDian(mMenDianLeiXingJsonData, 3);
            }
        }
    };

    // 查询门店类型
    public void ChaXunMenDianLeiXing(){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("name",mleiXingSearchName);

        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mMenDianLeiXingUrl)
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
     * 值接收处理
     */
    public void values(){
        mLoginModel = LoginModel.get(mContext);
        mChaoShiModel = ChaoShiModel.get(mContext);
        // Token赋值
        setToken(mContext);

        // 门店弹出View
        // 获取布局文件
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewD = inflater.inflate(R.layout.alert_kao_bai_fang_guan_li_search, null);
        // 公司 alert 弹出
        mBf_search_men_dian = mViewD.findViewById(R.id.bf_search_men_dian);

        // 搜索门店
        menDianSearch();

        LayoutInflater inflaters = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewPPD = inflaters.inflate(R.layout.alert_kao_bai_fang_guan_li_search, null);
        // 公司 alert 弹出
        mBf_search_men_dian_pp = mViewPPD.findViewById(R.id.bf_search_men_dian);
        // 品牌搜索
        pingPaiSouShuo();

        // 查询门店类型
        ChaXunMenDianLeiXing();
        LayoutInflater inflaterLeiXing = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 门店类型alert view
        mMenDianView = inflaterLeiXing.inflate(R.layout.alert_kao_bai_fang_guan_li_search, null);
        // 公司 alert 弹出
        mBf_search_men_dian_lei_xing = mMenDianView.findViewById(R.id.bf_search_men_dian);


    }



    /**
     * 数据请求成功回调
     * @param string 数据
     * @param is 1 品牌 2门店
     */
    public void shuJuHuiDiao(String string,int is){
        if(is == 1){
            mMengDianPingpaiJsonData = string;
            setData(string,1);
            ShowMenDian(string,1);

        }else if(is == 2){
            mMengDianJsonData = string;
            setData(string,2);
            ShowMenDian(mMengDianJsonData,2);
            if(mMengDianPingpaiJsonData.equals("")){
                // 品牌搜索
                pingPaiSouShuo();
            }
        }

    }

    @Override
    public void dingWeiData() {

    }

    /**
     * 创建TextView
     * @param is  区分标识
     * @param Jstring 传递内容
     * @param string 显示文字
     * @return
     */
    public TextView CreateTextvBf(final int is,final String Jstring,String string){
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,100);
        layoutParam.setMargins(0,10,0,0);
        textView.setLayoutParams(layoutParam);

        textView.setGravity(Gravity.CENTER_VERTICAL);

        textView.setBackground(getResources().getDrawable(R.drawable.bottom_border));

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 门店选择
                if(is == 1){
                    // 隐藏弹出
                    dialog.hide();
                }
                // 品牌
                if(is == 2){
                    dialogpp.hide();
                }
                // 门店类型选择
                if(is == 3){
                    // 隐藏弹出
                    dialogMenDianLeiXing.hide();
                }

                // 回调选择项
                XuanZheLie(Jstring,is);

            }
        });
        textView.setText(string);
        return textView;

    }

    /**
     * 门店alert选择
     * @param string
     * @param is
     */
    public void XuanZheLie(String string,int is){
        try {
            // 品牌
            if(is == 2){
                JSONObject jsonObject = new JSONObject(string);
                String nameText = jsonObject.getString("name");

                // 品牌
                mXuan_zhe_men_dian_ping_pai_button.setText(nameText);
                // 门店搜索品牌
                mMen_Dian_ping_pai = nameText;

                // 门店搜索
                menDianSearch();

            }
            // 门店
            if(is == 1){
                JSONObject jsonObject = new JSONObject(string);
                String idText = jsonObject.getString("id");
                String nameText = jsonObject.getString("name");
                String men_dian_ping_paiText = jsonObject.getString("men_dian_ping_pai");
                String men_dian_haoText = jsonObject.getString("men_dian_hao");
                String FanWei = jsonObject.getString("fan_wei");
                String lat = jsonObject.getString("md_lat");
                String lng = jsonObject.getString("md_lng");

                // 门店号
                mLocationBaiDu.setBianHao(men_dian_haoText);

                // 存储选择门店id
                mLocationBaiDu.setMenDianId(Integer.valueOf(idText));
                // 存储用户选择门店
                mLocationBaiDu.setMenDianMingCheng(nameText);
                // 门店范围
                mLocationBaiDu.setFanWei(FanWei);

                // 门店lat
                if(lat.equals("null") || lat.equals("")){
                    mLocationBaiDu.setMenDianLat(0.0);
                }else{
                    mLocationBaiDu.setMenDianLat(Double.valueOf(lat));
                }

                // 门店lat
                if(lng.equals("null") || lng.equals("")){
                    mLocationBaiDu.setMenDianLng(0.0);
                }else{
                    mLocationBaiDu.setMenDianLng(Double.valueOf(lng));
                }


                mXuan_zhe_men_dian_button.setText(men_dian_ping_paiText+"-"+men_dian_haoText+"-"+nameText);
            }else if(is == 3){
                // 门店类型
                mSearchMenDianLeiXing = string;
                // 门店搜索
                menDianSearch();
                mCha_xun_men_dian_lei_xing.setText(string);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示查询出来的数据
     * @param string
     * @param is 1 品牌 2门店 3门店类型
     */
    public void ShowMenDian(String string,int is){
        try {
            if(is == 1){
                // 品牌搜索显示view清空
                mBf_search_men_dian_pp.removeAllViews();
                JSONArray jsonArray = new JSONArray(string);
                if(jsonArray.length() > 0){
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = new JSONObject();
                        TextView textView = new TextView(mContext);
                        // 显示门店数据
                        String stringJson = jsonArray.get(i).toString();
                        JSONObject jsonObject1 = new JSONObject(stringJson);
                        textView = CreateTextvBf(2,stringJson,jsonObject1.getString("name"));
                        mBf_search_men_dian_pp.addView(textView);

                    }
                }
            }
            if(is == 2){
                // 门店搜索view清空
                mBf_search_men_dian.removeAllViews();
                JSONArray jsonArray = new JSONArray(string);
                if(jsonArray.length() > 0){
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = new JSONObject();
                        TextView textView = new TextView(mContext);
                        // 显示门店数据

                        String stringJson = jsonArray.get(i).toString();
                        JSONObject jsonObject1 = new JSONObject(stringJson);
                        textView = CreateTextvBf(1,stringJson,jsonObject1.getString("men_dian_ping_pai")+"-"+jsonObject1.getString("men_dian_hao")+"- "+jsonObject1.getString("name"));
                        mBf_search_men_dian.addView(textView);

                    }
                }
            }else if(is == 3){
                // 门店类型搜索view清空
                mBf_search_men_dian_lei_xing.removeAllViews();
                // 显示门店类型数据
                JSONArray jsonArray1 = new JSONArray(string);
                if(jsonArray1.length() > 0){
                    JSONArray jsonArray2 = new JSONArray(jsonArray1.get(0).toString());
                    if(jsonArray2.length() > 0){
                        for (int i3 = 0;i3<jsonArray2.length();i3++){
                            TextView textView1 = new TextView(mContext);
                            textView1 = CreateTextvBf(3,jsonArray2.get(i3)+"",jsonArray2.get(i3)+"");
                            mBf_search_men_dian_lei_xing.addView(textView1);
                        }
                    }
                }
            }
        }catch (JSONException e){

        }
    }


    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mXuan_zhe_men_dian_ping_pai_button = (Button)findViewById(R.id.xuan_zhe_men_dian_ping_pai_button);
        mXuan_zhe_men_dian_button = (Button)findViewById(R.id.xuan_zhe_men_dian_button);
        // 门店类型
        mCha_xun_men_dian_lei_xing = (Button)findViewById(R.id.xun_dian_cha_xun_men_dian_lei_xing);

        mKai_shi_xun_dian_button = (Button)findViewById(R.id.kai_shi_xun_dian_button);
        mTitle_nei_ye = (TextView)findViewById(R.id.title_nei_ye);
        mXun_dian_addr_button = (Button)findViewById(R.id.xun_dian_addr_button);
        // 重新定位
        mRi_chang_ding_wei = (ImageView)findViewById(R.id.ri_chang_ding_wei);
    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        // 设置标题
        mTitle_nei_ye.setText(R.string.gong_zuo_zhong_xin_xun_dian_guan_li);

        // 重新定位
        mRi_chang_ding_wei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tiShi(mContext,"定位成功");

                myListener = new MyLocationListenner();
                isFirstLoc = true; // 是否首次定位
                mIsDingWeiChengGong = false; // 定位是否成功

                BaiDuDingWeiDiaoYong();
            }
        });


        // 品牌选择
        mXuan_zhe_men_dian_ping_pai_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 品牌搜索
                if(dialogpp == null){
                    // 弹窗
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                    // 初始化组件
                    // 搜索
                    EditText bf_men_dian_ming_cheng = mViewPPD.findViewById(R.id.bf_men_dian_ming_cheng);
                    bf_men_dian_ming_cheng.setHint("请输入品牌名称");

                    // 搜索处理
                    MenDianSearchChuli(bf_men_dian_ming_cheng,3);

                    // 设置View
                    alertBuilder.setView(mViewPPD);

                    // 显示
                    alertBuilder.create();

                    dialogpp = alertBuilder.show();
                }else{
                    dialogpp.show();
                }
            }
        });

        // 门店类型搜索选择
        mCha_xun_men_dian_lei_xing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialogMenDianLeiXing == null){
                    // 弹窗
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                    // 初始化组件
                    // 搜索
                    EditText bf_men_dian_ming_cheng = mMenDianView.findViewById(R.id.bf_men_dian_ming_cheng);
                    bf_men_dian_ming_cheng.setHint("请输入门店类型名称");

                    // 搜索处理
                    MenDianSearchChuli(bf_men_dian_ming_cheng,2);

                    // 设置View
                    alertBuilder.setView(mMenDianView);

                    // 显示
                    alertBuilder.create();

                    dialogMenDianLeiXing = alertBuilder.show();
                }else{
                    dialogMenDianLeiXing.show();
                }
            }
        });

        // 门店选择
        mXuan_zhe_men_dian_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog == null){
                    // 弹窗
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                    // 初始化组件
                    // 搜索
                    EditText bf_men_dian_ming_cheng = mViewD.findViewById(R.id.bf_men_dian_ming_cheng);
                    bf_men_dian_ming_cheng.setHint("请输入门店名称/门店店号/品牌");

                    // 搜索处理
                    MenDianSearchChuli(bf_men_dian_ming_cheng,1);

                    // 设置View
                    alertBuilder.setView(mViewD);

                    // 显示
                    alertBuilder.create();

                    dialog = alertBuilder.show();
                }else{
                    dialog.show();
                }
            }
        });

        // 开始巡店
        mKai_shi_xun_dian_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailableAndConnected(mContext)){
                    if(mLocationBaiDu.getMenDianId() == 0){
                        tiShi(mContext,"请选择门店");
                    }else if(mIsDingWeiChengGong == false){
                        tiShi(mContext,"定位失败");
                    }else{
                        // 验证是否有其他店铺未提交
                        String[] strings = DianIDgetData(mMengDianJsonData);
                        if(strings[0].equals("true")){
                            XunDianTiaoZhuan();
                        }else{
                            WeiTiJiaoChuLi(strings);
                        }
                    }
                }else{
                    tiShi(mContext,"网络连接失败");
                }
            }
        });
    }

    /**
     * 搜索门店
     * @param editText
     * @param is 1 门店搜索 2 门店类型搜索 3品牌
     */
    public void MenDianSearchChuli(EditText editText,final int is){
        // 搜索内容存储
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(is == 1){
                    mSearchString = String.valueOf(editable).trim();
                    // 搜索门店
                    menDianSearch();
                }else if(is == 3){
                    mPinPaiSearch = String.valueOf(editable).trim();
                    // 搜索品牌
                    pingPaiSouShuo();
                }else if(is == 2){
                    mleiXingSearchName = String.valueOf(editable).trim();
                    // 搜索门店类型
                    ChaXunMenDianLeiXing();
                }

            }
        });
    }

    /**
     * 巡店跳转
     */
    public void XunDianTiaoZhuan(){
        Gson mGson = new Gson();
        String string = mGson.toJson(mLocationBaiDu);
        // 存储容器
        Log.i("巡店",string);
        Intent i = XunDianActivity.newIntent(XunDianGuanLiActivity.this,string);
        startActivity(i);
    }

    /**
     * 巡店数据为提交
     * @param strings
     */
    public void WeiTiJiaoChuLi(final String[] strings){
        tishiS = new String[2];
        tishiS[0] = "继续巡 "+strings[1];
        tishiS[1] = "否";
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);

        alertBuilder.setItems(tishiS, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int index) {
                if(index == 0){
                    // 继续巡店
                    mLocationBaiDu.setMenDianId(Integer.valueOf(strings[2]));
                    mLocationBaiDu.setMenDianMingCheng(strings[1].substring(strings[1].indexOf("-")+1,strings[1].length()));
                    XunDianTiaoZhuan();
                }else if(index == 1){
                    // 删除巡店表数据
                    mXunDianModel.deleteXunDian(strings[2],String.valueOf(mLogin.getUid()));
                    // 删除本地超时数据
                    mChaoShiModel.deleteCaoShi(strings[2]);
                }

                alertDialog1.dismiss();
            }
        });
        alertDialog1 = alertBuilder.create();
        alertDialog1.show();
    }


    /**
     * 查询数据库是否有巡店数据
     * @param Jsonstring
     * @return [0=>true/false] 有/无数据 [1=>"棋盘点"] 店铺名称
     */
    public String[] DianIDgetData(String Jsonstring){
        String[] strings = new String[3];
        strings[0] = "true";
        // 查询数据库所有的记录
        mLogin = mLoginModel.getLogin(1);

        List<XunDianCanShu> xunDianCanShuList = mXunDianModel.getXunDianID(String.valueOf(mLogin.getUid()));

//        Log.i("巡店",xunDianCanShuList.get(0).getName());

        if(xunDianCanShuList.size() > 0){
            strings[1] = xunDianCanShuList.get(0).getBian_hao_name();
            strings[0] = "false";
            strings[2] = String.valueOf(xunDianCanShuList.get(0).getMenDianId());

            // 用户选择门店和数据存储门店相同
            if(mLocationBaiDu.getMenDianId() == xunDianCanShuList.get(0).getMenDianId()){
                strings[0] = "true";
            }
        }

        return strings;
    }

    /**
     * 百度地图定位调用
     */
    public void BaiDuDingWeiDiaoYong(){
        // after andrioid m,must request Permiision on runtime
        mCurrentMode = LocationMode.NORMAL;
        mMapView = (MapView) findViewById(R.id.bmapView); //找到我们的地图控件
        mBaiduMap = mMapView.getMap(); //获得地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL); //设置为普通模式的地图
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mLocClient = new LocationClient(this);  //定位用到的一个类
        mLocClient.registerLocationListener(myListener); //注册监听
        ///LocationClientOption类用来设置定位SDK的定位方式，
        LocationClientOption option = new LocationClientOption(); //以下是给定位设置参数
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(2000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        tiShi(mContext,"1:"+s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        tiShi(mContext,"2:"+s);
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    /**
     * 百度定位
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }

            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            mLocationBaiDu.setDingQeiTime(location.getTime());
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            mLocationBaiDu.setLatitude(location.getLatitude());
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            mLocationBaiDu.setLontitude(location.getLongitude());
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            mLocationBaiDu.setRadius(location.getRadius());
            sb.append(location.getRadius());

            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                mIsDingWeiChengGong = true;
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                mLocationBaiDu.setAddr(location.getAddrStr());
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                mLocationBaiDu.setDescribe("gps定位成功");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                mIsDingWeiChengGong = true;
                sb.append("\naddr : ");
                mLocationBaiDu.setAddr(location.getAddrStr());
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                mLocationBaiDu.setDescribe("网络定位成功");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                mIsDingWeiChengGong = true;
                sb.append("\ndescribe : ");
                mLocationBaiDu.setDescribe("离线定位成功，离线定位结果也是有效的");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            mLocationBaiDu.setLocationDescribe(location.getLocationDescribe());
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }

            if(mIsDingWeiChengGong){
                DingWeiChengGong();
            }

//            Log.e("描述：", sb.toString());
        }
    }

    /**
     * 定位成功调用方法
     */
    public void DingWeiChengGong(){
        // 设置显示地址信息
        mXun_dian_addr_button.setText("当前地址 : "+mLocationBaiDu.getAddr()+"\n详细地址 : "+mLocationBaiDu.getLocationDescribe());
        // 停止定位
        mLocClient.stop();
    }

    @Override
    public void onAttachedToWindow(){
//        mCallbacks = (KaoQingCommonActivity.Callbacks)mContext;
        mCallbacksc = (KaoQingCommonActivity.Callbacks)mContext;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        // 销毁回调
        mCallbacksc = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }



}