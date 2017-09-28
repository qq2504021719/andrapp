package com.bignerdranch.android.xundian.xundianguanli;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.bignerdranch.android.xundian.comm.NeiYeCommActivity;
import com.bignerdranch.android.xundian.comm.XunDianCanShu;
import com.bignerdranch.android.xundian.model.XunDianModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/7.
 */

public class XunDianGuanLiActivity extends NeiYeCommActivity implements SearchView.OnQueryTextListener,NeiYeCommActivity.Callbacks{

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.XunDianGuanLiActivity";

    private Button mXun_dian_addr_button;

    private Button mXuan_zhe_men_dian_ping_pai_button; //选择门店品牌
    private Button mXuan_zhe_men_dian_button; //选择门店
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

    /**
     * 值接收处理
     */
    public void values(){
        // Token赋值
        setToken(mContext);
        // 品牌请求
        pinPaiSearch();
        // 请求店铺
        menDianSearch();

    }

    /**
     * 数据请求成功回调
     * @param string 数据
     * @param is 1 品牌 2门店
     */
    public void shuJuHuiDiao(String string,int is){
        if(is == 1){
            mMengDianPingpaiJsonData = string;
            setData(mMengDianPingpaiJsonData,1);
        }else if(is == 2){
            mMengDianJsonData = string;
            setData(mMengDianJsonData,2);
        }

    }


    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mXuan_zhe_men_dian_ping_pai_button = (Button)findViewById(R.id.xuan_zhe_men_dian_ping_pai_button);
        mXuan_zhe_men_dian_button = (Button)findViewById(R.id.xuan_zhe_men_dian_button);
        mMen_dian_ming_cheng_searchview = (EditText) findViewById(R.id.men_dian_ming_cheng);
        mKai_shi_xun_dian_button = (Button)findViewById(R.id.kai_shi_xun_dian_button);
        mTitle_nei_ye = (TextView)findViewById(R.id.title_nei_ye);
        mXun_dian_addr_button = (Button)findViewById(R.id.xun_dian_addr_button);
    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        // 设置标题
        mTitle_nei_ye.setText(R.string.gong_zuo_zhong_xin_xun_dian_guan_li);

        // 搜索内容存储
        mMen_dian_ming_cheng_searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSearchString = String.valueOf(editable).trim();
                // 清空显示
                mXuan_zhe_men_dian_button.setText("选择门店");
                // 清空门店搜索
                mLocationBaiDu.setMenDianId(0);
                // 搜索门店
                menDianSearch();
            }
        });


        // 品牌选择
        mXuan_zhe_men_dian_ping_pai_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i("巡店",mMengDianPingPaiData.toString());
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                alertBuilder.setItems(mMengDianPingPaiData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {

                        mXuan_zhe_men_dian_ping_pai_button.setText(mMengDianPingPaiData[index]);

                        int id =ChanKanId(mMengDianPingpaiJsonData,mMengDianPingPaiData[index]);
                        // 更新用户选择门店品牌
                        mMen_Dian_ping_pai = mMengDianPingPaiData[index];
                        // 清空显示
                        mXuan_zhe_men_dian_button.setText("选择门店");
                        // 清空门店搜索
                        mLocationBaiDu.setMenDianId(0);
                        // 从新搜索内容
                        menDianSearch();

                        mLocationBaiDu.setMenDianPingPaiId(Integer.valueOf(id));
                        alertDialog1.dismiss();
                    }
                });
                alertDialog1 = alertBuilder.create();
                alertDialog1.show();
            }
        });

        // 门店选择
        mXuan_zhe_men_dian_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);

                alertBuilder.setItems(mMengDianData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {

                        mMen_Dian = mMengDianData[index];
                        mXuan_zhe_men_dian_button.setText(mMengDianData[index]);
                        // 存储选择门店id
                        mLocationBaiDu.setMenDianId(ChanKanId(mMengDianJsonData,mMengDianData[index]));
                        // 存储用户选择门店
                        mLocationBaiDu.setMenDianMingCheng(mMengDianData[index]);
                        alertDialog1.dismiss();

                    }
                });
                alertDialog1 = alertBuilder.create();
                alertDialog1.show();
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
                            String string = mGson.toJson(mLocationBaiDu);
                            // 存储容器
                            Intent i = XunDianActivity.newIntent(XunDianGuanLiActivity.this,string);
                            startActivity(i);
                        }else{
                            tiShi(mContext,strings[1]+"未提交,请先提交");
                        }
                    }
                }else{
                    tiShi(mContext,"网络连接失败");
                }
            }
        });
    }

    /**
     * 查询数据库是否有巡店数据
     * @param Jsonstring
     * @return [0=>true/false] 有/无数据 [1=>"棋盘点"] 店铺名称
     */
    public String[] DianIDgetData(String Jsonstring){
        String[] strings = new String[2];
        strings[0] = "true";
        // 查询数据库所有的记录
        List<XunDianCanShu> xunDianCanShuList = mXunDianModel.getXunDianID();

        if(xunDianCanShuList.size() > 0){
            try {
                JSONArray jsonArray = new JSONArray(Jsonstring);
                if(jsonArray.length() > 0){
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                        if(xunDianCanShuList.get(0).getMenDianId() == Integer.valueOf(jsonObject.getString("id"))){
                            strings[1] = jsonObject.getString("name");
                        }else{
                            strings[0] = "false";
                        }
                    }
                }
            }catch (JSONException e){
            }

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
        mCallbacks = (Callbacks)mContext;
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
        mCallbacks = null;
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