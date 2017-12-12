package com.bignerdranch.android.xundian.kaoqing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.bignerdranch.android.xundian.LoginActivity;
import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.BaiDuActivity;
import com.bignerdranch.android.xundian.comm.CommActivity;
import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.LocationBaiDu;
import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.comm.NeiYeCommActivity;
import com.bignerdranch.android.xundian.comm.PictureUtils;
import com.bignerdranch.android.xundian.comm.WeiboDialogUtils;
import com.bignerdranch.android.xundian.model.LoginModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/26.
 */

public class KaoQingCommonActivity extends CommActivity {

    // 0 请假管理 1考勤记录
    public int isYEmian = 0;

    public TextView mTitle_nei_ye = null; // 设置显示标题
    public Context mContext = null;

    // 门店品牌数据
    public String mMengDianPingpaiJsonData = "";
    public String[] mMengDianPingPaiData;
    public String mPinPaiSearch = "";


    // 门店数据
    public String mMengDianJsonData = "";
    public String[] mMengDianData =new String[0];

    public String mSearchString = ""; // 用户输入
    public String mMen_Dian_ping_pai = ""; // 门店品牌

    // 门店搜索URl
    public String mMenDianSearchURL = Config.URL+"/app/menDian";
    // 品牌搜索URl
    public String mPinPaiSearchURL = Config.URL+"/app/get_ping_pai";

    // Token
    public String mToken = null;

    // LoginModel 登录模型
    public static LoginModel mLoginModel = null;
    // 登录对象
    public static Login mLogin = null;

    // 开启线程
    public static Thread mThread = null;

    // dialog,加载
    public Dialog mWeiboDialog;

    // 考勤记录-请假记录列表高度
    public int mQingJiaHeight = 0;
    // activity 类型
    public int mActivityLeiXing = 0;

    // 门店类型
    public String mSearchMenDianLeiXing = "";

    // 百度地图定位
    public LocationBaiDu mLocationBaiDu = new LocationBaiDu(); //定位信息存储

    public MapView mMapView;
    public BaiduMap mBaiduMap;
    public MyLocationConfiguration.LocationMode mCurrentMode;
    public LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    public boolean isFirstLoc = true; // 是否首次定位
    public boolean mIsDingWeiChengGong = false; // 定位是否成功
    // 定位显示TextView组件
    public TextView mDingWeiTextView;
    // 百度地图定位

    // 回调函数存储变量
    public Callbacks mCallbacksc;

    // 图片提交url
    public String mTuPanTJURL = Config.URL+"/app/xun_dian_ti_jiao/tuPian";

    // 门店搜索模式
    public String moshi = "3";

    // 本月应工作小时
    public String mText_ben_yue_ying_shang_xiao_shi_value_str = "0";

    // 本月时间工作小时
    public String mText_shi_ji_gong_zhuo_xiao_shi_value_str = "0";

    // 权限名称
    public String mQuanXianName = "";
    // 跳转页面
    public Intent mI;

    /**
     * 实现回调接口
     */
    public interface Callbacks{
        void shuJuHuiDiao(String string,int is);
        void dingWeiData();
    }

    /**
     * 默认连接数据库
     * @param context
     */
    public void setToken(Context context){
        // 登录数据库连接
        mLoginModel = LoginModel.get(context);
        // Token查询,赋值
        mLogin = mLoginModel.getLogin(1);
        mToken = mLogin.getToken();
    }

    /**
     * 点击返回
     *
     * @param v
     */
    public void DianJiFanHui(View v) {
        finish();
    }

    /**
     * 获取当前时间
     * @return 当前时间y-m-d h-i-s
     */
    public String getDangQianTime(int is){
        SimpleDateFormat simpleDateFormats = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        if(is == 1){
             simpleDateFormats = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        }else if(is == 2){
             simpleDateFormats =new SimpleDateFormat("yyyy-MM-dd hh:ii:ss", Locale.CHINA);
        }
        Calendar calendar=Calendar.getInstance(Locale.CHINA);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        //当前时间，貌似多余，其实是为了所有可能的系统一致
        calendar.setTimeInMillis(System.currentTimeMillis());
        return simpleDateFormats.format(calendar.getTime());
    }

    /**
     * 提示
     *
     * @param context
     */
    public static void tiShi(Context context, String string) {
        Toast mToast = null;
        if (mToast == null) {
            mToast = Toast.makeText(context, "",
                    Toast.LENGTH_LONG);
            LinearLayout layout = (LinearLayout) mToast.getView();
            TextView tv = (TextView) layout.getChildAt(0);
            tv.setTextSize(20);
        }
        mToast.setGravity(Gravity.BOTTOM, 0, 10);
        mToast.setText(string);
        mToast.show();
    }

    /**
     * 检查网络是否完全连接 true 连接  false 没有连接
     * @return
     */
    public boolean isNetworkAvailableAndConnected(Context context){
        ConnectivityManager cm =(ConnectivityManager)context.getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    /**
     * loading 浮层
     *
     * @param logingString 提示文字
     */
    public void LoadingStringEdit(String logingString){
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(mContext,logingString);
    }

    /**
     * 日期选择解析
     * @param str
     * @param c 1 多选 2 单选
     * @return
     */
    public String JieXi(String str,int c){
        String string = "";
        try {
            JSONArray jsonArray = new JSONArray(str);

            if (jsonArray.length()>0){
                if(c == 2){
                    string = String.valueOf(jsonArray.get(0));
                }else if(c == 1) {
                    for (int i = 0;i<jsonArray.length();i++){
                        if(string.equals("")){
                            string = String.valueOf(jsonArray.get(i));
                        }else{
                            string += ","+String.valueOf(jsonArray.get(i));
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return string;
    }

    /**
     * 请假记录展示
     * @param dataStr 请假记录数据
     * @param linearLayout 显示父布局
     */
    public void QingJiaDataShow(String dataStr, LinearLayout linearLayout){
        mQingJiaHeight = 0;
        // 清空布局
        linearLayout.removeAllViews();
        // 请假数据不为空
        if(!dataStr.equals("")){

            try {
                // 请假记录
                JSONArray jsonArray = new JSONArray();
                if(mActivityLeiXing == 1){
                    JSONObject jsonObjectJi = new JSONObject(dataStr);
                    // 应工作小时
                    mText_ben_yue_ying_shang_xiao_shi_value_str = jsonObjectJi.getString("YinShangH");

                    // 时间工作小时
                    mText_shi_ji_gong_zhuo_xiao_shi_value_str = jsonObjectJi.getString("ShiJiH");

                    // 请假记录
                    jsonArray = new JSONArray(jsonObjectJi.getString("qingJia"));
                }else{
                    // 请假记录
                    jsonArray = new JSONArray(dataStr);
                }

                if(jsonArray.length() > 0){
                    if(mActivityLeiXing == 1){
                        int biaoShi = 0;
                        for (int i = 0;i<jsonArray.length();i++){
                            // time data 成
                            JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());

                            // data 拆分
                            JSONArray jsonArray1 = new JSONArray(jsonObject.getString("data"));

                            int IsNianYue = 0;
                            if(jsonArray1.length() > 0){
                                for(int c = 0;c<jsonArray1.length();c++){
                                    // 考勤记录-请假记录列表
                                    mQingJiaHeight += 140;

                                    LinearLayout linearLayout1 = CreateLinear(1);

                                    if(IsNianYue == 0){
                                        LinearLayout linearLayout1_2 = CreateLinear(8);
                                        TextView textView = CreateTextView(5,jsonObject.getString("time"));
                                        linearLayout1_2.addView(textView);
                                        linearLayout.addView(linearLayout1_2);
                                        IsNianYue = 1;
                                    }
                                    // 第一行
                                    CreateDiYiHang(linearLayout1,jsonArray1.get(c).toString(),biaoShi);
                                    // 添加到父布局
                                    linearLayout.addView(linearLayout1);

                                    biaoShi++;
                                }
                            }
                        }
                    }else{
                        for (int i = 0;i<jsonArray.length();i++){
                            // 考勤记录-请假记录列表
                            mQingJiaHeight += 120;

                            LinearLayout linearLayout1 = CreateLinear(1);
                            // 第一行
                            CreateDiYiHang(linearLayout1,jsonArray.get(i).toString(),i);
                            // 添加到父布局
                            linearLayout.addView(linearLayout1);
                        }
                    }


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 创建View
     * @param linearLayout
     * @param string 数据
     * @param i 编号
     * @return
     */
    public void CreateDiYiHang(LinearLayout linearLayout,String string,int i){
        try {
            JSONObject jsonObject = new JSONObject(string);

            JSONObject jsonObject_users_id_s = new JSONObject();
            // 获取用户信息
            if(!jsonObject.getString("users_id_s").equals("null")){
                jsonObject_users_id_s = new JSONObject(jsonObject.getString("users_id_s"));
            }


            LinearLayout linearLayout2 = CreateLinear(2);

            LinearLayout linearLayout3 = CreateLinear(3);
            LinearLayout linearLayout4 = CreateLinear(4);


            LinearLayout linearLayout5 = CreateLinear(5);
            LinearLayout linearLayout6 = CreateLinear(6);

            // 评接名称,请假详细内容
            if(jsonObject_users_id_s.length() > 0){
                TextView textView1 = CreateTextView(1,(i+1)+" "+jsonObject_users_id_s.getString("name"));

                String string_ri_qi = "";
                // 评接显示内容
                if(!jsonObject.getString("an_tian_qing_jia").equals("null")){
                    // 得到按钮天请假字符串
                    string_ri_qi = JieXi(jsonObject.getString("an_tian_qing_jia"),1);
                }else{
                    // 得到按时间段请假字符串
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("an_shi_jian_dun"));
                    string_ri_qi += jsonArray.get(0);

                    if(!jsonObject.getString("an_shi_jian_duan_shang_wu_kai_shi").equals("null")
                            && !jsonObject.getString("an_shi_jian_duan_shang_wu_jie_shu").equals("null")){
                        string_ri_qi += " 上午 "+jsonObject.getString("an_shi_jian_duan_shang_wu_kai_shi")+"-"+
                                jsonObject.getString("an_shi_jian_duan_shang_wu_jie_shu");
                    }

                    if(!jsonObject.getString("an_shi_jian_duan_xia_wu_kai_shi").equals("null")
                            && !jsonObject.getString("an_shi_jian_duan_xia_wu_jie_shu").equals("null")){
                        string_ri_qi += " 下午 "+jsonObject.getString("an_shi_jian_duan_xia_wu_kai_shi")+"-"+
                                jsonObject.getString("an_shi_jian_duan_xia_wu_jie_shu");
                    }
                }

                string_ri_qi += " "+jsonObject.getString("qing_jia_lei_xing")+" "+jsonObject.getString("qing_jia_yuan_yin");
                TextView textView2 = CreateTextView(2,string_ri_qi);
                linearLayout3.addView(textView1);
                linearLayout3.addView(textView2);
            }

            // 评接审核状态
            TextView textView3 = CreateTextView(3,jsonObject.getString("qing_jia_zhuang_tai"));
            linearLayout4.addView(textView3);
            // 不同意设置图标 CreateImageView
            if(jsonObject.getString("qing_jia_zhuang_tai").equals("不同意")){
                linearLayout4.addView(CreateImageView(1,jsonObject.getString("bu_tong_yi_yuan_yin"),R.drawable.kao_qing_xing));
            }



            // 添加到父节点
            linearLayout2.addView(linearLayout3);
            linearLayout2.addView(linearLayout4);

            // 第一行添加到父节点
            linearLayout.addView(linearLayout2);


            // 创建审核view
            CreateDierhang(linearLayout6,string);
            linearLayout5.addView(linearLayout6);

            // 第二行添加到父节点
            linearLayout.addView(linearLayout5);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建审核信息
     * @param linearLayout
     * @param string
     */
    public void CreateDierhang(LinearLayout linearLayout,String string){
            try {
                JSONObject jsonObject = new JSONObject(string);

                JSONObject jsonObject_users_id_s = new JSONObject();
                JSONObject jsonObject_users_id_1_s = new JSONObject();
                JSONObject jsonObject_users_id_2_s = new JSONObject();
                JSONObject jsonObject_users_id_3_s = new JSONObject();

                // 获取用户信息
                if(!jsonObject.getString("users_id_s").equals("null")){
                    jsonObject_users_id_s = new JSONObject(jsonObject.getString("users_id_s"));
                }
                if(!jsonObject.getString("users_id_1_s").equals("null")){
                    jsonObject_users_id_1_s = new JSONObject(jsonObject.getString("users_id_1_s"));
                }
                if(!jsonObject.getString("users_id_2_s").equals("null")){
                    jsonObject_users_id_2_s = new JSONObject(jsonObject.getString("users_id_2_s"));
                }
                if(!jsonObject.getString("users_id_3_s").equals("null")){
                    jsonObject_users_id_3_s = new JSONObject(jsonObject.getString("users_id_3_s"));
                }



                LinearLayout linearLayout7_1 = CreateLinear(7);
                LinearLayout linearLayout7_2 = CreateLinear(7);
                LinearLayout linearLayout7_3 = CreateLinear(7);

                // 图片
                ImageView imageView2_1 = CreateImageView(2,"",R.drawable.kao_qing_yong_hu);
                ImageView imageView2_2 = CreateImageView(2,"",R.drawable.kao_qing_yong_hu);
                ImageView imageView2_3 = CreateImageView(2,"",R.drawable.kao_qing_yong_hu);

                // 文字信息
                TextView textView4_1 = CreateTextView(4,"");
                TextView textView4_2 = CreateTextView(4,"");
                TextView textView4_3 = CreateTextView(4,"");

                // 状态图标
                ImageView imageView3_1 = CreateImageView(3,"",R.drawable.ka_qing_ku_lian);
                ImageView imageView3_2 = CreateImageView(3,"",R.drawable.ka_qing_ku_lian);
                ImageView imageView3_3 = CreateImageView(3,"",R.drawable.ka_qing_ku_lian);

                if(jsonObject_users_id_1_s.length() > 0){
                    textView4_1 = CreateTextView(4,jsonObject_users_id_1_s.getString("name"));
                }
                if(jsonObject_users_id_2_s.length() > 0){
                    textView4_2 = CreateTextView(4,jsonObject_users_id_2_s.getString("name"));
                }
                if(jsonObject_users_id_3_s.length() > 0){
                    textView4_3 = CreateTextView(4,jsonObject_users_id_3_s.getString("name"));
                }




                // 无审核权限
                if(jsonObject_users_id_s.getString("shenheren_jibie").equals("null") || jsonObject_users_id_s.getString("shenheren_jibie").equals("")){

                    linearLayout7_1.addView(imageView2_1);
                    linearLayout7_1.addView(textView4_1);
                    isTuBiao(linearLayout7_1,imageView3_1,jsonObject.getString("zhuang_tai_1"));

                    linearLayout7_2.addView(imageView2_2);
                    linearLayout7_2.addView(textView4_2);
                    isTuBiao(linearLayout7_2,imageView3_2,jsonObject.getString("zhuang_tai_2"));

                    linearLayout7_3.addView(imageView2_3);
                    linearLayout7_3.addView(textView4_3);
                    isTuBiao(linearLayout7_3,imageView3_3,jsonObject.getString("zhuang_tai_3"));

                    linearLayout.addView(linearLayout7_3);
                    linearLayout.addView(linearLayout7_2);
                    linearLayout.addView(linearLayout7_1);

                }

                // 3级权限
                if(jsonObject_users_id_s.getString("shenheren_jibie").equals("3")){
                    linearLayout7_1.addView(imageView2_1);
                    linearLayout7_1.addView(textView4_1);
                    isTuBiao(linearLayout7_1,imageView3_1,jsonObject.getString("zhuang_tai_1"));

                    linearLayout7_2.addView(imageView2_2);
                    linearLayout7_2.addView(textView4_2);
                    isTuBiao(linearLayout7_2,imageView3_2,jsonObject.getString("zhuang_tai_2"));

                    linearLayout.addView(linearLayout7_2);
                    linearLayout.addView(linearLayout7_1);

                }

                // 2级权限
                if(jsonObject_users_id_s.getString("shenheren_jibie").equals("2")){
                    linearLayout7_1.addView(imageView2_1);
                    linearLayout7_1.addView(textView4_1);
                    isTuBiao(linearLayout7_1,imageView3_1,jsonObject.getString("zhuang_tai_1"));
                    linearLayout.addView(linearLayout7_1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

    }

    public void isTuBiao(LinearLayout linearLayout,ImageView imageView,String string){
        if(!string.equals("null")){
            if(string.equals("待审核")){
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ka_qing_ku_lian));
            }
            if(string.equals("不同意")){
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ka_qing_dai_shen_he));
            }
            if(string.equals("同意")){
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
            }
            linearLayout.addView(imageView);
        }

    }

    /**
     * 创建linearLayout
     * @param is 创建类型
     */
    public LinearLayout CreateLinear(int is){
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        if(is == 1){
            layoutParam.setMargins(0,10,0,20);
        }else if(is == 2){
            layoutParam.setMargins(0,5,0,0);
        }
        else if(is == 3){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
        }else if( is == 4){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,3);
            layoutParam.setMargins(0,0,30,0);
        }else if(is == 5){
            layoutParam.setMargins(0,5,0,0);
        }else if(is == 7){
            layoutParam.setMargins(0,0,10,0);
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
        }else if(is == 8){
            layoutParam.setMargins(0,35,0,0);
        }


        linearLayout.setLayoutParams(layoutParam);

        linearLayout.setOrientation(LinearLayout.VERTICAL);

        if(is == 1){
            linearLayout.setPadding(0,0,0,30);
            linearLayout.setBackground(getResources().getDrawable(R.drawable.bottom_border));
        }else if(is == 2 || is == 6){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        }else if(is == 4){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.RIGHT);
        }else if(is == 5){
            linearLayout.setPadding(15,0,0,0);
        }else if(is == 7){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.BOTTOM);

        }else if(is == 8){
            linearLayout.setPadding(0,0,0,10);
        }

        return linearLayout;
    }

    /**
     * 创建TextView
     * @param is 创建类型
     * @param string 显示内容
     */
    public TextView CreateTextView(int is,String string){
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        if(is == 2){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParam.setMargins(0,5,0,0);
        }

        textView.setLayoutParams(layoutParam);

        if(is == 1){
            textView.setTextColor(getResources().getColor(R.color.heise));
            textView.setTextSize(15);
            textView.setText(string);
        }else if(is == 2){
            textView.setPadding(15,0,0,0);
            textView.setText(string);
            textView.setTextSize(12);
        }else if(is == 3){
            if(string.equals("待审核") || string.equals("不同意")){
                textView.setTextColor(getResources().getColor(R.color.hongse1));
            }
            if(string.equals("同意")){
                textView.setTextColor(getResources().getColor(R.color.zhuti));
            }
            textView.setText(string);
        }else if(is == 4){
            textView.setGravity(Gravity.BOTTOM);
            textView.setTextColor(getResources().getColor(R.color.heise));
            textView.setTextSize(14);
            textView.setText(string);
        }else if(is == 5){
            textView.setTextColor(getResources().getColor(R.color.zhuti));
            textView.setPadding(0,0,0,10);
            textView.setBackground(getResources().getDrawable(R.drawable.bottom_border));
            textView.setText(string);
        }

        return textView;
    }

    /**
     * 创建ImageView
     * @param is 创建类型
     * @param string 提示文字
     * @param drawableSrc 图片资源
     * @return
     */
    public ImageView CreateImageView(int is, final String string, int drawableSrc){
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        if(is == 1){
            layoutParam = new LinearLayout.LayoutParams(58,58);
        }else if(is == 2){
            layoutParam = new LinearLayout.LayoutParams(68,68);
        }else if(is == 3){
            layoutParam = new LinearLayout.LayoutParams(58,58);
        }

        imageView.setLayoutParams(layoutParam);

        imageView.setImageDrawable(getResources().getDrawable(drawableSrc));

        if(is == 1){
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                    // 设置显示内容
                    alertBuilder.setMessage(string);
                    alertBuilder.create();
                    alertBuilder.show();
                }
            });
        }

        return imageView;
    }

    /**
     * 百度地图定位调用
     */
    public void BaiDuDingWeiDiaoYong(Context context){
        // after andrioid m,must request Permiision on runtime
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

        mBaiduMap = mMapView.getMap(); //获得地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL); //设置为普通模式的地图
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mLocClient = new LocationClient(context);  //定位用到的一个类
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
        // 设置显示地址信息
        mDingWeiTextView.setText("当前地址 : "+mLocationBaiDu.getAddr()+"\n详细地址 : "+mLocationBaiDu.getLocationDescribe());
        // 定位成功回调
        mCallbacksc.dingWeiData();
        // 停止定位
        mLocClient.stop();
    }

    /**
     * 将 string的值设置到对应strings中
     * @param string
     * @param is 1 品牌 2门店
     */
    public void setData(String string,int is){
        try {
            JSONArray jsonArray = new JSONArray(string);
            if(jsonArray.length() > 0){
                if(is == 1){
                    mMengDianPingPaiData = new String[jsonArray.length()];
                }else if(is == 2){
                    mMengDianData = new String[jsonArray.length()];
                }

                for(int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                    if(is == 1){
                        mMengDianPingPaiData[i] = jsonObject.getString("name");
                    }else if(is == 2){
                        mMengDianData[i] = jsonObject.getString("name");
                    }
                }
            }else{
                if(is == 1){
                    mMengDianPingPaiData= new String[0];
                }else if(is == 2){
                    mMengDianData= new String[0];
                }
            }
        }catch (JSONException e){

        }
    }

    /**
     * Handler
     */
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            // 关闭loading
            WeiboDialogUtils.closeDialog(mWeiboDialog);
            /**
             * 请求回调
             */
            if(msg.what==1){
                // 门店参数请求回调
                String string = msg.obj.toString();
                mCallbacksc.shuJuHuiDiao(string,2);
            }else if(msg.what == 2){
                // 品牌参数请求回调
                String string = msg.obj.toString();
//                Log.i("巡店",""+string);
                mCallbacksc.shuJuHuiDiao(string,1);
            }
        }
    };

    /**
     * 门店搜索
     */
    public void menDianSearch(){
        if(mToken != null){
            final OkHttpClient client = new OkHttpClient();

            String str = "";
            String str1 = "";
            if(!mSearchString.isEmpty()) str = mSearchString;

            if(!mMen_Dian_ping_pai.isEmpty()) str1 = mMen_Dian_ping_pai;
            //3, 发起新的请求,获取返回信息
            RequestBody body = new FormBody.Builder()
                    .add("name",str)
                    .add("pin_pai",str1)
                    .add("moshi",moshi)
                    .add("lei_xing",mSearchMenDianLeiXing)
                    .build();
            final Request request = new Request.Builder()
                    .addHeader("Authorization","Bearer "+mToken)
                    .url(mMenDianSearchURL)
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
     * 品牌搜索
     */
    public void pingPaiSouShuo(){
        if(mToken != null){
            final OkHttpClient client = new OkHttpClient();

            String str = "";
            if(!mPinPaiSearch.isEmpty()) str = mPinPaiSearch;

            //3, 发起新的请求,获取返回信息
            RequestBody body = new FormBody.Builder()
                    .add("name",str)
                    .build();
            final Request request = new Request.Builder()
                    .addHeader("Authorization","Bearer "+mToken)
                    .url(mPinPaiSearchURL)
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

    /**
     * 返回指向某个具体位置的File对象
     */
    public File getPhotoFile(String string){
        File externalFilesDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File externalFilesDir = this.getExternalFilesDir("android.support.v4.content.FileProvider");
        // 确认外部存储是否可用,如果不可用,返回null,
        if(externalFilesDir == null){
            return null;
        }
        return new File(externalFilesDir,string);
    }

    /**
     * 文件获取获取方法
     */
    public String getPhotoFilename(){
        return "IMG_"+ LoginActivity.getTime()+".jpg";
    }

    /**
     *
     * 图片压缩方法
     * path 图片存储路径
     * destWidth 压缩成多宽
     * destHeight 压缩成多高
     * @return 返回图片路径
     */
    public String imgYaSuo(String path,int destWidth,int destHeight){
        // 压缩图片
        Bitmap bitmap = PictureUtils.getScaledBitmap(path,destWidth,destHeight);
        File f = getPhotoFile(getPhotoFilename());
        try {
            // 存储到本地
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f.getPath();
    }




}
