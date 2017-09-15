package com.bignerdranch.android.xundian.xundianguanli;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.bignerdranch.android.xundian.comm.LocationBaiDu;
import com.bignerdranch.android.xundian.comm.NeiYeCommActivity;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/9/7.
 */

public class XunDianGuanLiActivity extends NeiYeCommActivity {

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.XunDianGuanLiActivity";
    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;

    private Context mContext;

    private Button mXuan_zhe_men_dian_ping_pai_button; //选择门店品牌
    private Button mXuan_zhe_men_dian_button; //选择门店
    private Button mKai_shi_xun_dian_button; // 开始巡店
    private String mMen_Dian_ping_pai; // 门店品牌
    private String mMen_Dian;// 门店

    private SearchView mMen_dian_ming_cheng_searchview; // 门店搜索框

    // 门店品牌数据
    private String mMengDianPingpaiJsonData = "{\"1\":\"\\u4f0d\\u7f18\",\"2\":\"\\u53ef\\u7684\\u4fbf\\u5229\",\"3\":\"\\u559c\\u58eb\\u591a\\u4fbf\\u5229\",." +
            "\"4\":\"\\u597d\\u5fb7\\u4fbf\\u5229\",\"5\":\"\\u5feb\\u5ba2\\u4fbf\\u5229\",\"6\":\"\\u7f57\\u68ee\\u4fbf\\u5229\"}";
    public List<String> mMengDianPingPaiData = new ArrayList<String>();

    // 门店数据
    public String mMengDianJsonData = "{\"1\":\"\\u5feb\\u5ba2\\u4fbf\\u5229*836*\\u5929\\u5317\\u5e97(\\u76f4\\u8425)\",\"2\":\"\\u5feb\\u5ba2\\" +
            "u4fbf\\u5229*845*\\u798f\\u826f\\u5e97(\\u76f4\\u8425)\",\"3\":\"\\u5feb\\u5ba2\\u4fbf\\u5229*952*\\u4e30\\u65b0(\\u76f4\\u8425)\",\"4\":\"\\" +
            "u5feb\\u5ba2\\u4fbf\\u5229*980*\\u91d1\\u6c99\\u5e97(\\u76f4\\u8425)\"}";
    public List<String> mMengDianData = new ArrayList<String>();

    private LocationBaiDu mLocationBaiDu = new LocationBaiDu(); //定位信息存储

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationMode mCurrentMode;
    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    boolean isFirstLoc = true; // 是否首次定位
    public boolean mIsDingWeiChengGong = false; // 定位是否成功

    private AlertDialog alertDialog1;


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
        setContentView(R.layout.xun_dian_guan_li);
        // 设置选择数据
        setData(mMengDianPingpaiJsonData,mMengDianPingPaiData);
        setData(mMengDianJsonData,mMengDianData);
        mContext = this;
        // 组件初始化
        ZhuJianInit();

        // 组件操作
        ZhuJianCaoZhuo();

        // 百度地图定位调用 mMengDianPingpaiJsonData
        BaiDuDingWeiDiaoYong();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPersimmions();
        }else{
            BaiDuDingWeiDiaoYong();
        }

    }

    /**
     * 将 string的值设置到对应strings中
     * @param string
     * @param strings
     */
    public void setData(String string,List<String> strings){
        try {
            JSONObject jsonObject = new JSONObject(string);
            Iterator inter =  jsonObject.keys();
            while(inter.hasNext()){
                String a = (String)inter.next();
//                int i = Integer.parseInt(a);
                strings.add(jsonObject.getString(a));
            }
        }catch (JSONException e){

        }
    }

    /**
     * 将string转为JSON对象,匹配string1，返回对应的id
     * @param string
     * @param string1
     * @return
     */
    public int ChanKanId(String string,String string1){
        try {
            JSONObject jsonObject = new JSONObject(string);
            int i = 0;
            Iterator inter =  jsonObject.keys();
            while(inter.hasNext()){
                String a = (String)inter.next();
                if(jsonObject.getString(a).equals(string1)){
                    return Integer.parseInt(a);
                }
            }
            return 0;
        }catch (JSONException e){

        }
        return 0;
    }



    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mXuan_zhe_men_dian_ping_pai_button = (Button)findViewById(R.id.xuan_zhe_men_dian_ping_pai_button);
        mXuan_zhe_men_dian_button = (Button)findViewById(R.id.xuan_zhe_men_dian_button);
        mMen_dian_ming_cheng_searchview = (SearchView)findViewById(R.id.men_dian_ming_cheng_searchview);
        mKai_shi_xun_dian_button = (Button)findViewById(R.id.kai_shi_xun_dian_button);
        mTitle_nei_ye = (TextView)findViewById(R.id.title_nei_ye);
    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        // 设置标题
        mTitle_nei_ye.setText(R.string.gong_zuo_zhong_xin_xun_dian_guan_li);

        // 搜索框边框优化
        if (mMen_dian_ming_cheng_searchview != null) {
            try {        //--拿到字节码
                Class<?> argClass = mMen_dian_ming_cheng_searchview.getClass();
                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                //--暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(mMen_dian_ming_cheng_searchview);
                //--设置背景
                mView.setBackgroundResource(R.drawable.search_view_border);
                // 设置光标颜色
                ownField.set(mMen_dian_ming_cheng_searchview, R.color.heise);

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 开始巡店
        mKai_shi_xun_dian_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLocationBaiDu.getMenDianId() == 0){
                    tiShi(mContext,"请选择门店");
                }else if(mIsDingWeiChengGong == false){
                    tiShi(mContext,"定位失败");
                }else{
                    String string = mGson.toJson(mLocationBaiDu);
                    // 存储容器
                    Intent i = XunDianActivity.newIntent(XunDianGuanLiActivity.this,string);
                    startActivity(i);
                }
            }
        });
    }




    /**
     * 选择门店品牌
     * @param view
     */
    public void showListAlertDialogp(View view){
        final String[] strings = new String[mMengDianPingPaiData.size()];
        for (int i = 0;i<mMengDianPingPaiData.size();i++){
            strings[i] = mMengDianPingPaiData.get(i);
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setItems(strings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int index) {
                // 清除搜索框焦点
                mMen_dian_ming_cheng_searchview.clearFocus();
                mMen_Dian_ping_pai = strings[index];
                mXuan_zhe_men_dian_ping_pai_button.setText(strings[index]);
                // 更新用户选择门店品牌
                mLocationBaiDu.setMenDianPingPaiId(ChanKanId(mMengDianPingpaiJsonData,strings[index]));
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = alertBuilder.create();
        alertDialog1.show();
    }

    /**
     * 选择门店
     * @param view
     */
    public void showListAlertDialog(View view){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        final String[] strings = new String[mMengDianData.size()];
        for (int i = 0;i<mMengDianData.size();i++){
            strings[i] = mMengDianData.get(i);
        }
        alertBuilder.setItems(strings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int index) {
                // 清除搜索框焦点
                mMen_dian_ming_cheng_searchview.clearFocus();
                mMen_Dian = strings[index];
                mXuan_zhe_men_dian_button.setText(strings[index]);
                // 存储选择门店id
                mLocationBaiDu.setMenDianId(ChanKanId(mMengDianJsonData,strings[index]));
                // 存储用户选择门店
                mLocationBaiDu.setMenDianMingCheng(strings[index]);
                alertDialog1.dismiss();

            }
        });
        alertDialog1 = alertBuilder.create();
        alertDialog1.show();
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
        BaiDuDingWeiDiaoYong();
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
            mLocationBaiDu.setLatitude(location.getRadius());
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
        // 停止定位
        mLocClient.stop();
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

    /**
     * 提示
     * @param context
     */
    public static void tiShi(Context context,String string){
        Toast.makeText(context,string, Toast.LENGTH_SHORT).show();
    }
}