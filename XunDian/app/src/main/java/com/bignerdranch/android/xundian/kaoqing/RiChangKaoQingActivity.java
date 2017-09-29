package com.bignerdranch.android.xundian.kaoqing;

import android.content.Context;
import android.content.Intent;
import android.icu.math.BigDecimal;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.LocationBaiDu;

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
 * Created by Administrator on 2017/9/26.
 */

public class RiChangKaoQingActivity extends KaoQingCommonActivity {

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.RiChangKaoQingActivity";

    // 重新定位
    private ImageView mRi_chang_ding_wei;

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationMode mCurrentMode;
    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    boolean isFirstLoc = true; // 是否首次定位
    public boolean mIsDingWeiChengGong = false; // 定位是否成功

    // 父节点
    public LinearLayout mLinear_kao_qing_fu;
    // 日期显示
    public TextView mTextview_kao_ri_qi;

    // 公司签到经度
    public Double mGongSiLat;
    // 公司签到维度
    public Double mGongSiLng;
    // 误差范围 米
    public Double mWuChaFanWei;
    // 班次名称
    public String mBanCiName;
    // 星期一
    public String mDay1 = "0";
    // 星期二
    public String mDay2 = "0";
    // 星期三
    public String mDay3 = "0";
    // 星期四
    public String mDay4 = "0";
    // 星期五
    public String mDay5 = "0";
    // 星期六
    public String mDay6 = "0";
    // 星期日
    public String mDay7 = "0";
    // 签到次数
    public String mQianDaoNum = "0";
    // 班次签到信息
    public String mBanCiQianDaoJson = "";
    // 请求用户签到范围
    public String mQianDaoFanWeiURL = Config.URL+"/app/yong_hu_qian_dao_fan_wei";
    // 用户签到提交
    public String mQianDaoTiJiao = Config.URL+"/app/qian_dao_xie_ru";

    private LocationBaiDu mLocationBaiDu = new LocationBaiDu(); //定位信息存储

    // 签到
    public Button mButton_shang_ban_qian_dao;

    // is签到
    public int mIsQian = 0;

    public static Intent newIntent(Context packageContext, int intIsId){
        Intent i = new Intent(packageContext,RiChangKaoQingActivity.class);
        i.putExtra(EXTRA,intIsId);
        return i;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_kao_ri_chang_kao_qing);
        mContext = this;
        // 组件初始化
        ZhuJianInit();
        // 组件操作
        ZhuJianCaoZhuo();
        // 数据/值设置
        values();
    }
    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mTitle_nei_ye = (TextView)findViewById(R.id.title_nei_ye);
        // 重新定位
        mRi_chang_ding_wei = (ImageView)findViewById(R.id.ri_chang_ding_wei);
        // 父节点
        mLinear_kao_qing_fu = (LinearLayout)findViewById(R.id.linear_kao_qing_fu);
        // 日期
        mTextview_kao_ri_qi = (TextView)findViewById(R.id.textview_kao_ri_qi);
        // 上班签到
        mButton_shang_ban_qian_dao = (Button)findViewById(R.id.button_shang_ban_qian_dao);
    }
    /**
     * 值操作
     */
    public void values(){
        if(isNetworkAvailableAndConnected(mContext)){
            // Token赋值
            setToken(mContext);
            // 百度地图定位
            BaiDuDingWeiDiaoYong();
            // 获取用户签到范围
            getGongSiQianDaoFanWei();
        }else{
            tiShi(mContext,"请连接网络");
        }

    }
    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText(R.string.qian_dao);
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

        // 日期
        mTextview_kao_ri_qi.setText(getDangQianTime(1));

        // 签到
        mButton_shang_ban_qian_dao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取用户签到范围
                getGongSiQianDaoFanWei();


                mIsQian = 1;
            }
        });
    }

    /**
     * 回调签到
     */
    public void qianDao(){
        Double juLi = GetShortDistance(mLocationBaiDu.getLontitude(),mLocationBaiDu.getLatitude(),mGongSiLng,mGongSiLat);
        Log.i("巡店","lat:"+mGongSiLng+"lng:"+mGongSiLat+"|"+juLi+"|"+mWuChaFanWei);
        if(juLi <= mWuChaFanWei){
//            tiShi(mContext,"上班签到成功"+juLi);
            // 提交签到信息
            qianDaoTiJiao();
        }else{
            java.text.DecimalFormat df=new java.text.DecimalFormat("#");
            tiShi(mContext,"签到超出签到范围"+df.format(juLi-mWuChaFanWei)+"米");
        }
        mIsQian = 0;
    }

    /**
     * 百度地图定位调用
     */
    public void BaiDuDingWeiDiaoYong(){
        // after andrioid m,must request Permiision on runtime
        mCurrentMode = LocationMode.NORMAL;
        mMapView = (MapView) findViewById(R.id.bmapview_kao); //找到我们的地图控件
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
     * Handler
     */
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            /**
             * 请求回调
             */
            if(msg.what==1){
                if(msg.obj.toString() != null && msg.obj.toString() != ""){

                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.getString("qian_dao_lat").equals("null") ||
                           jsonObject.getString("qian_dao_lat").equals("null") ||
                           jsonObject.getString("qian_dao_lat").equals("null")){
                            tiShi(mContext,"公司签到位置未设置,联系管理员");
                            finish();
                        }else{
                            mGongSiLat = Double.valueOf(jsonObject.getString("qian_dao_lat"));
                            mGongSiLng = Double.valueOf(jsonObject.getString("qian_dao_lng"));
                            mWuChaFanWei = Double.valueOf(jsonObject.getString("fan_wei_mi"));
                            mBanCiQianDaoJson = jsonObject.getString("banCiKaoQingData");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(mIsQian == 1){
                        qianDao();
                    }
                    // 显示用户签到信息
                    ShowQianDao();
                }
            }else if(msg.what == 2){
                tiShi(mContext,msg.obj.toString());
                if(msg.obj.toString().equals("签到成功")){
                        getGongSiQianDaoFanWei();
                }
            }
        }
    };


    /**
     * 请求公司签到范围
     */
    public void getGongSiQianDaoFanWei(){
        if(mToken != null) {
            final OkHttpClient client = new OkHttpClient();
            //3, 发起新的请求,获取返回信息
            RequestBody body = new FormBody.Builder()
                    .build();

            final Request request = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + mToken)
                    .url(mQianDaoFanWeiURL)
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
     * 根据两点的经度纬度计算出距离
     * @param lon1
     * @param lat1
     * @param lon2
     * @param lat2
     * @return
     */
    public double GetShortDistance(double lon1, double lat1, double lon2, double lat2)
    {
        double DEF_PI = 3.14159265359; // PI
        double DEF_2PI= 6.28318530712; // 2*PI
        double DEF_PI180= 0.01745329252; // PI/180.0
        double DEF_R =6370693.5; // radius of earth
        double ew1, ns1, ew2, ns2;
        double dx, dy, dew;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 经度差
        dew = ew1 - ew2;
        // 若跨东经和西经180 度，进行调整
        if (dew > DEF_PI)
        dew = DEF_2PI - dew;
        else if (dew < -DEF_PI)
        dew = DEF_2PI + dew;
        dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
        dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
        // 勾股定理求斜边长
        distance = Math.sqrt(dx * dx + dy * dy);
        return distance;
    }


    /**
     * 创建签到内容显示
     */
    public void ShowQianDao(){
        // 清空父节点
        mLinear_kao_qing_fu.removeAllViews();
        try {
            JSONArray jsonArray = new JSONArray(mBanCiQianDaoJson);
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                String name = jsonObject.getString("name").trim();
                String time = jsonObject.getString("time").trim();
                String adds = jsonObject.getString("adds").trim();
                String qianDaoTime = jsonObject.getString("qianDaoTime").trim();

                TextView titleTextView;
                if(adds.length() != 0 && qianDaoTime.length() != 0 ){
                    int ColorInt = R.color.zhuti;
                    // 上班时签到时间>大于上班时间
                    if(i%2 == 0 && Integer.valueOf(qianDaoTime.replace(":","")) > Integer.valueOf(time.replace(":",""))){
                        ColorInt = R.color.hongse;
                        // 下班时签到时间小于下班时间
                    }else if(i%2 == 1 && Integer.valueOf(qianDaoTime.replace(":","")) < Integer.valueOf(time.replace(":",""))){
                        ColorInt = R.color.hongse;
                    }

                    titleTextView = CreateTextView(name+"(已签到 "+time+")",1,ColorInt);
                }else{
                    titleTextView = CreateTextView(name+"(未签到 "+time+")",1,R.color.heise);
                }

                TextView timeTextView = CreateTextView(qianDaoTime,2,R.color.heise);

                TextView addsTextView = CreateTextView(adds,3,R.color.heise);

                // iamge
                LinearLayout imgLinearLayout = CreateLinearLayout(3);
                ImageView imageView = CreateImageView();
                imgLinearLayout.addView(imageView);

                // 内层
                LinearLayout nCreateLinearLayout = CreateLinearLayout(2);
                nCreateLinearLayout.addView(imgLinearLayout);
                nCreateLinearLayout.addView(titleTextView);
                nCreateLinearLayout.addView(timeTextView);

                // 外层
                LinearLayout wCreateLinearLayout = CreateLinearLayout(1);
                wCreateLinearLayout.addView(nCreateLinearLayout);
                wCreateLinearLayout.addView(addsTextView);

                // 添加到父节点
                mLinear_kao_qing_fu.addView(wCreateLinearLayout);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建textview
     * @param string 显示内容
     * @param leix 1 标题 2 右侧签到时间  3 签到地址
     * @param color 标题颜色
     * @return
     */
    public TextView CreateTextView(String string,int leix,int color){
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        if(leix == 1 || leix == 2){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
            layoutParam.setMargins(5,0,0,0);
        }else if(leix == 3){
            layoutParam.setMargins(11,3,0,0);
        }
        textView.setLayoutParams(layoutParam);
        textView.setText(string);

        if(leix == 1){
            textView.setTextColor(getResources().getColor(color));
            textView.setTextSize(16);
        }else if(leix == 2){
            textView.setGravity(Gravity.RIGHT);
        }else if(leix == 3){
            textView.setTextColor(getResources().getColor(R.color.huise6));
        }

        return textView;
    }

    /**
     * 创建布局
     * @param leix 1 外部垂直布局 2内部行布局
     * @return
     */
    public LinearLayout CreateLinearLayout(int leix){
        LinearLayout linearLayout = new LinearLayout(mContext);

        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        if(leix == 3){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
        }

        if(leix == 1){
            layoutParam.setMargins(0,20,0,0);
        }

        linearLayout.setLayoutParams(layoutParam);

        if(leix == 1){
            linearLayout.setOrientation(LinearLayout.VERTICAL);
        }else if(leix == 3){
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        }else if(leix == 2){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        }


        return linearLayout;
    }

    /**
     * 创建iamgeview
     * @return
     */
    public ImageView CreateImageView(){
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(20,20);
        imageView.setLayoutParams(layoutParam);
        imageView.setBackground(getResources().getDrawable(R.drawable.heise_yuan));
        return imageView;
    }

    /**
     * 签到提交
     */
    public void qianDaoTiJiao(){
        if(mToken != null) {
            final OkHttpClient client = new OkHttpClient();
            //3, 发起新的请求,获取返回信息
            MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("gps_lat",String.valueOf(mLocationBaiDu.getLatitude()));
                jsonObject.put("gps_lng",String.valueOf(mLocationBaiDu.getLontitude()));
                jsonObject.put("gps_addr",mLocationBaiDu.getAddr());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            body.addFormDataPart("is","2");
            body.addFormDataPart("data",jsonObject.toString());
            Log.i("巡店",jsonObject.toString());
            final Request request = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + mToken)
                    .url(mQianDaoTiJiao)
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
}
