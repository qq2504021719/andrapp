package com.bignerdranch.android.xundian.xundianguanli;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
import com.bignerdranch.android.xundian.comm.LocationBaiDu;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Administrator on 2017/9/7.
 */

public class XunDianGuanLiActivity extends AppCompatActivity{

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.XunDianGuanLiActivity";

    private TextView mTitle_nei_ye; // title

    private Button mXuan_zhe_men_dian_ping_pai_button; //选择门店品牌
    private Button mXuan_zhe_men_dian_button; //选择门店
    private String mMen_Dian_ping_pai; // 门店品牌
    private String mMen_Dian;// 门店

    private SearchView mMen_dian_ming_cheng_searchview; // 门店搜索框

    // 门店品牌数据
    public String[] mMengDianPingPaiData = {"伍缘","可的便利","喜士多便利","好德便利","快客便利","罗森便利"};
    // 门店数据
    public String[] mMengDianData = {"快客便利*836*天北店(直营)","快客便利*845*福良店(直营)","快客便利*952*丰新(直营)","快客便利*980*金沙店(直营)"};

    private LocationBaiDu mLocationBaiDu = new LocationBaiDu(); //定位信息存储

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationMode mCurrentMode;
    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    boolean isFirstLoc = true; // 是否首次定位
    public boolean mIsDingWeiChengGong = false; // 定位是否成功

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

        // 组件初始化
        ZhuJianInit();

        // 组件操作
        ZhuJianCaoZhuo();

        // 百度地图定位调用
        BaiDuDingWeiDiaoYong();
    }



    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mTitle_nei_ye = (TextView)findViewById(R.id.title_nei_ye);
        mXuan_zhe_men_dian_ping_pai_button = (Button)findViewById(R.id.xuan_zhe_men_dian_ping_pai_button);
        mXuan_zhe_men_dian_button = (Button)findViewById(R.id.xuan_zhe_men_dian_button);
        mMen_dian_ming_cheng_searchview = (SearchView)findViewById(R.id.men_dian_ming_cheng_searchview);

    }



    /**
     * 组件操作
     */
    public void ZhuJianCaoZhuo(){
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
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private AlertDialog alertDialog1;

    /**
     * 选择门店品牌
     * @param view
     */
    public void showListAlertDialogp(View view){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setItems(mMengDianPingPaiData, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int index) {
                // 清除搜索框焦点
                mMen_dian_ming_cheng_searchview.clearFocus();
                mMen_Dian_ping_pai = mMengDianPingPaiData[index];
                mXuan_zhe_men_dian_ping_pai_button.setText(mMengDianPingPaiData[index]);
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
        alertBuilder.setItems(mMengDianData, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int index) {
                // 清除搜索框焦点
                mMen_dian_ming_cheng_searchview.clearFocus();
                mMen_Dian = mMengDianData[index];
                mXuan_zhe_men_dian_button.setText(mMengDianData[index]);
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


}