package com.bignerdranch.android.xundian.xundianjihua;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.NeiYeCommActivity;
import com.bignerdranch.android.xundian.comm.TongZhi;
import com.bignerdranch.android.xundian.comm.WeiboDialogUtils;
import com.bignerdranch.android.xundian.comm.XunDianJiHua;
import com.bignerdranch.android.xundian.model.XunDianJiHuaModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/20.
 */

public class RiChengActivity extends NeiYeCommActivity implements NeiYeCommActivity.Callbacks{

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.RiChengActivity";

    // 顶部导航条
    private LinearLayout mBen_zhou_linearLayout;
    private LinearLayout mBo_hui_linearLayout;
    private TextView mBen_zhou_textview;
    private TextView mBo_hui_textview;
    private int mIsYeMian = 1;

    // 驳回父节点
    private LinearLayout mLinear_bo_hui;
    // 本周父节点
    private LinearLayout mLinear_ben_zou;

    // Fragment
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Fragment f1;

    // 周工作数据String
    private String mZhouJsonData = "[{\"zhou\":\"2017-09-25\",\"riRi\":\"2017-09-25\",\"KSShijian\":\"09:10\",\"JSShiJian\":\"09:30\",\"pingPai\":\"\\u4f0d\\u7f18\",\"menDianHao\":\"10057\",\"menMingCheng\":\"\\u5929\\u5c71\\u5e97\",\"isWC\":1},{\"zhou\":\"2017-09-25\",\"riRi\":\"2017-09-25\",\"KSShijian\":\"11:10\",\"JSShiJian\":\"11:40\",\"pingPai\":\"\\u53ef\\u8fea\",\"menDianHao\":\"10057\",\"menMingCheng\":\"\\u4e0a\\u6d77\\u5b9c\\u5ddd\\u5e97\",\"isWC\":1},{\"zhou\":\"2017-09-25\",\"riRi\":\"2017-09-26\",\"KSShijian\":\"10:10\",\"JSShiJian\":\"10:30\",\"pingPai\":\"\\u4f0d\\u7f18\",\"menDianHao\":\"10057\",\"menMingCheng\":\"\\u5408\\u80a5\\u5e97\",\"isWC\":1},{\"zhou\":\"2017-09-25\",\"riRi\":\"2017-09-27\",\"KSShijian\":\"13:10\",\"JSShiJian\":\"14:30\",\"pingPai\":\"\\u597d\\u5fb7\",\"menDianHao\":\"10057\",\"menMingCheng\":\"\\u8398\\u5e84\\u5e97\",\"isWC\":0}]";

    private AlertDialog alertDialog1;

    // 选择周
    private TextView mTextview_zhou;
    // 选择周显示
    private TextView mTextview_zhou_value;
    // 周数据
    public String[] mZhouData;

    // 选择日期
    private TextView mTextview_ri_qi;
    // 日期显示
    private TextView mTextview_ri_qi_value;
    // 日期数据
    public String[] mRiQiData;

    // 选择时间
    private TextView mTextview_shi_jian;
    // 显示时间
    private TextView mTextview_shi_jian_value;
    // 时间对象
    public Calendar ct;
    // 选择结束时间
    private TextView mTextview_js_shi_jian;
    // 显示结束时间
    private TextView mTextview_js_shi_jian_value;

    // 选择品牌
    private TextView mTextview_pin_pai;
    // 显示品牌
    private TextView mTextview_pin_pai_value;

    // 选择门店名称
    private TextView mTextview_ming_cheng;
    // 显示门店名称
    private TextView mTextview_ming_cheng_value;

    // 显示门店店号
    private TextView mTextview_dian_hao_value;

    // 工作录入
    private Button mButton_gong_zuo;

    // 删除工作记录
    private Button mButton_shan_chu_gong;

    // 提交工作表
    private Button mButton_ti_jiao_gzb;

    // LoginModel 登录模型
    public static XunDianJiHuaModel mXunDianJiHuaModel;

    // 周一节点
    public LinearLayout mLinear_zhou_yi;
    // 周二节点
    public LinearLayout mLinear_zhou_er;
    // 周三节点
    public LinearLayout mLinear_zhou_san;
    // 周四节点
    public LinearLayout mLinear_zhou_si;
    // 周五节点
    public LinearLayout mLinear_zhou_wu;
    // 周六节点
    public LinearLayout mLinear_zhou_liu;
    // 周日节点
    public LinearLayout mLinear_zhou_ri;

    // 本周工作请求地址
    public String mBenZhouQingQiuURL = Config.URL+"/app/ben_zhou_xun_dian_ji_hua";

    // 删除巡店计划
    public String mShanChuXunDianURL = Config.URL+"/app/shan_chu_xun_dian_ji_hua";

    // 修改数据提交
    public String mXiuGaiJiHuaURL = Config.URL+"/app/xiu_gai_xun_dian_ji_hua";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    // 巡店计划集合
    public List<XunDianJiHua> mXunDianJiHuas = new ArrayList<>();

    // 巡店计划对象
    public XunDianJiHua mXunDianJiHua = new XunDianJiHua();

    // 当前下标(数据库id)
    public int mId = mXunDianJiHuas.size();

    // 每天序号 巡店计划
    public int ZhouYi = 1;
    public int ZhouEr = 1;
    public int ZhouSan = 1;
    public int ZhouSi = 1;
    public int ZhouWu = 1;
    public int ZhouLiu = 1;
    public int ZhouQi = 1;

    // 修改下标
    public int mXiuGaiKey = 1000;

    public static Intent newIntent(Context packageContext, int intIsId){
        Intent i = new Intent(packageContext,RiChengActivity.class);
        i.putExtra(EXTRA,intIsId);
        return i;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ri_cheng);
        mContext = this;
        // 组件初始化
        ZhuJianInit();
        // 数据/值设置
        values();
        // 组件操作
        ZhuJianCaoZhuo();
    }


    /**
     * 更改导航栏颜色
     */
    public void updateButtonBackground(){
        if(mIsYeMian == 1){
            mBen_zhou_textview.setBackgroundColor(mContext.getResources().getColor(R.color.zhuti));
            mBo_hui_textview.setBackgroundColor(mContext.getResources().getColor(R.color.huise));
            mBen_zhou_linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.zhuti));
            mBo_hui_linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.huise));
        }else if(mIsYeMian == 2){
            mBen_zhou_textview.setBackgroundColor(mContext.getResources().getColor(R.color.huise));
            mBo_hui_textview.setBackgroundColor(mContext.getResources().getColor(R.color.zhuti));
            mBen_zhou_linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.huise));
            mBo_hui_linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.zhuti));
        }
    }

    /**
     * 更新UI信息
     */
    public void updateUI(){
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        if(mIsYeMian == 1){
            // 隐藏驳回
            mLinear_bo_hui.setVisibility(View.GONE);
            // 显示本周
            mLinear_ben_zou.setVisibility(View.VISIBLE);
            // 实例 Fragment
            f1 = new BenZhouFragment();
            // 添加到layout的Fragment
            transaction.replace(R.id.fragment_container, f1);
            // 提交
            transaction.commit();
        }else if(mIsYeMian == 2){
            // 隐藏本周
            mLinear_ben_zou.setVisibility(View.GONE);
            // 显示驳回
            mLinear_bo_hui.setVisibility(View.VISIBLE);
            // 删除本周Fragment
            transaction.remove(f1);
        }
    }

    /**
     * 数据请求后处理
     * @param string
     */
    public void JiHuaQingQiuFanHui(String string){

        if(!string.equals("")){
            try {

                JSONObject jsonObject = new JSONObject(string);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("riqi").toString());
                if(jsonArray.length() > 0){
                    // 周赋值
                    mZhouData = new String[1];
                    mZhouData[0] = jsonArray.get(0).toString()+"周";

                    // 日期赋值
                    mRiQiData = new String[7];
                    mRiQiData[0] = jsonArray.get(0).toString();
                    mRiQiData[1] = jsonArray.get(1).toString();
                    mRiQiData[2] = jsonArray.get(2).toString();
                    mRiQiData[3] = jsonArray.get(3).toString();
                    mRiQiData[4] = jsonArray.get(4).toString();
                    mRiQiData[5] = jsonArray.get(5).toString();
                    mRiQiData[6] = jsonArray.get(6).toString();

                    // 数据赋值
                    mZhouJsonData = jsonObject.getString("data").toString();

                    // 值处理
                    setXunDianJiHuas();
                    // 显示计划
                    setShowJH();
                    // 组件操作
                    ZhuJianCaoZhuo();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

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
                if(msg.obj.toString().equals("暂无数据")){
                    tiShi(mContext,"暂无数据");
                }else{
                    JiHuaQingQiuFanHui(msg.obj.toString());
                }
            }else if(msg.what == 2){
                tiShi(mContext,msg.obj.toString());
            }else if(msg.what == 3){
                tiShi(mContext,msg.obj.toString());
                // 删除数据
                mXunDianJiHuas = new ArrayList<>();
                // 刷新视图
                setShowJH();
            }
        }
    };

    /**
     * 请求本周数据
     */
    public void qingQiuBenZhouJiHua(){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("is","2");
        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mBenZhouQingQiuURL)
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
     * 处理请求数据
     */
    public void setXunDianJiHuas() {
        try {
            JSONArray jsonArray = new JSONArray(mZhouJsonData);
            if(jsonArray.length() > 0){
                for(int i =0;i<jsonArray.length();i++){
                    XunDianJiHua xunDianJiHua = new XunDianJiHua();
                    if(jsonArray.get(i) != null){
                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                        xunDianJiHua.setId(Integer.valueOf(jsonObject.getString("id")));
                        xunDianJiHua.setZhou(jsonObject.getString("zhou").trim());
                        xunDianJiHua.setRiQi(jsonObject.getString("ri_qi").trim());
                        xunDianJiHua.setShiJian(jsonObject.getString("kai_shi_time").trim());
                        xunDianJiHua.setJSShiJian(jsonObject.getString("jie_shu_time").trim());
                        xunDianJiHua.setPingPaiStr(jsonObject.getString("mendian_pin_pai").trim());
                        xunDianJiHua.setMenDianHao(jsonObject.getString("mendian_hao").trim());
                        xunDianJiHua.setMenDianStr(jsonObject.getString("mendian_name").trim());
                        xunDianJiHua.setMenDianId(Integer.valueOf(jsonObject.getString("mendian_id")));
                        xunDianJiHua.setZhouStr(jsonObject.getString("zhouStr").trim());
                    }
                    mXunDianJiHuas.add(xunDianJiHua);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 修改数据提交
     */
    public void gongZuoBiaoTiJiao(){
        JSONObject jsonObjects = new JSONObject();
        for(int i = 0;i<mXunDianJiHuas.size();i++){
            if(mXunDianJiHuas.get(i) != null){
                JSONObject jsonObject = new JSONObject();
                try {
                    XunDianJiHua xunDianJiHua = mXunDianJiHuas.get(i);

                    jsonObject.put("id",xunDianJiHua.getId());
                    jsonObject.put("zhou",xunDianJiHua.getZhou());
                    jsonObject.put("ri_qi",xunDianJiHua.getRiQi());
                    jsonObject.put("kai_shi_time",xunDianJiHua.getShiJian());
                    jsonObject.put("jie_shu_time",xunDianJiHua.getJSShiJian());
                    jsonObject.put("mendian_id",xunDianJiHua.getMenDianId());

                    jsonObjects.put(""+i,jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
//        Log.i(" ",jsonObjects.toString());
        final OkHttpClient client = new OkHttpClient();
        //3, 发起新的请求,获取返回信息
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonObjects.toString());
        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mXiuGaiJiHuaURL)
                .post(requestBody)
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
                    mHandler.obtainMessage(3, response.body().string()).sendToTarget();
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
        mTitle_nei_ye = (TextView)findViewById(R.id.title_nei_ye);
        // 顶部导航条
        mBen_zhou_linearLayout = (LinearLayout)findViewById(R.id.ben_zhou_linearLayout);
        mBo_hui_linearLayout = (LinearLayout)findViewById(R.id.bo_hui_linearLayout);
        mBen_zhou_textview = (TextView)findViewById(R.id.ben_zhou_textview);
        mBo_hui_textview = (TextView)findViewById(R.id.bo_hui_textview);

        mLinear_bo_hui = (LinearLayout)findViewById(R.id.linear_bo_hui);
        mLinear_ben_zou = (LinearLayout)findViewById(R.id.linear_ben_zou);
        // 周一
        mLinear_zhou_yi = (LinearLayout)findViewById(R.id.linear_zhou_yi);
        // 周二
        mLinear_zhou_er = (LinearLayout)findViewById(R.id.linear_zhou_er);
        // 周三
        mLinear_zhou_san = (LinearLayout)findViewById(R.id.linear_zhou_san);
        // 周四
        mLinear_zhou_si = (LinearLayout)findViewById(R.id.linear_zhou_si);
        // 周五
        mLinear_zhou_wu = (LinearLayout)findViewById(R.id.linear_zhou_wu);
        // 周六
        mLinear_zhou_liu = (LinearLayout)findViewById(R.id.linear_zhou_liu);
        // 周日
        mLinear_zhou_ri = (LinearLayout)findViewById(R.id.linear_zhou_ri);
        // 周
        mTextview_zhou = (TextView)findViewById(R.id.textview_zhou);
        // 显示周
        mTextview_zhou_value = (TextView)findViewById(R.id.textview_zhou_value);
        // 日期
        mTextview_ri_qi = (TextView)findViewById(R.id.textview_ri_qi);
        // 显示日期
        mTextview_ri_qi_value = (TextView)findViewById(R.id.textview_ri_qi_value);
        // 时间
        mTextview_shi_jian = (TextView)findViewById(R.id.textview_shi_jian);
        // 显示时间
        mTextview_shi_jian_value = (TextView)findViewById(R.id.textview_shi_jian_value);
        // 结束时间
        mTextview_js_shi_jian = (TextView)findViewById(R.id.textview_js_shi_jian);
        // 显示结束时间
        mTextview_js_shi_jian_value = (TextView)findViewById(R.id.textview_js_shi_jian_value);
        // 品牌
        mTextview_pin_pai = (TextView)findViewById(R.id.textview_pin_pai);
        // 显示品牌
        mTextview_pin_pai_value = (TextView)findViewById(R.id.textview_pin_pai_value);
        // 门店名称
        mTextview_ming_cheng = (TextView)findViewById(R.id.textview_ming_cheng);
        // 显示门店名称
        mTextview_ming_cheng_value = (TextView)findViewById(R.id.textview_ming_cheng_value);
        // 显示门店店号
        mTextview_dian_hao_value = (TextView)findViewById(R.id.textview_dian_hao_value);
        // 工作录入
        mButton_gong_zuo = (Button)findViewById(R.id.button_gong_zuo);
        // 删除记录
        mButton_shan_chu_gong = (Button)findViewById(R.id.button_shan_chu_gong);
        // 提交工作表
        mButton_ti_jiao_gzb = (Button)findViewById(R.id.button_ti_jiao_gzb);
    }

    /**
     * 值操作
     */
    public void values(){
        // 开启loading
        LoadingStringEdit("数据加载中");

        // 给回调对象赋值
        mCallbacks = (Callbacks)mContext;

        // Token赋值
        setToken(mContext);
        // 巡店计划model
        mXunDianJiHuaModel = XunDianJiHuaModel.get(mContext);
        // 数据请求
        qingQiuBenZhouJiHua();
        // 请求驳回数据处理
        setXunDianJiHuas();
        // 设置周数据,日期数据
//        setZhouData();
        // 品牌请求
        pinPaiSearch();
        // 请求店铺
        menDianSearch();
        // 显示数据库计划
        setShowJH();
    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText(R.string.wo_de_ri_cheng);
        // 更改导航栏颜色
        updateButtonBackground();
        // 更新UI
        updateUI();
        // 本周点击事件
        mBen_zhou_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsYeMian == 2){
                    mIsYeMian = 1;
                    updateButtonBackground();
                    updateUI();
                }
            }
        });

        // 驳回点击
        mBo_hui_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsYeMian == 1){
                    mIsYeMian = 2;
                    updateButtonBackground();
                    updateUI();
                }
            }
        });

        // 显示周选择项
        mTextview_zhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                alertBuilder.setItems(mZhouData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {
                        // 显示选择周
                        mTextview_zhou_value.setText(mZhouData[index]);
                        // 更新用户选择周
                        mXunDianJiHua.setZhou(mZhouData[index]);

                        alertDialog1.dismiss();
                    }
                });
                alertDialog1 = alertBuilder.create();
                alertDialog1.show();
            }
        });

        // 选择日期 mRiQiData
        mTextview_ri_qi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                alertBuilder.setItems(mRiQiData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {
                        // 显示选择日期
                        mTextview_ri_qi_value.setText(mRiQiData[index]);
                        // 更新用户选择日期
                        mXunDianJiHua.setRiQi(mRiQiData[index]);

                        alertDialog1.dismiss();
                    }
                });
                alertDialog1 = alertBuilder.create();
                alertDialog1.show();
            }
        });

        // 选择时间
        mTextview_shi_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ct = Calendar.getInstance();
                new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        ct.set(Calendar.HOUR_OF_DAY,i);
                        ct.set(Calendar.MINUTE,i1);
                        String string = String.valueOf(DateFormat.format("HH:mm",ct));
                        mTextview_shi_jian_value.setText(string);
                        // 存入对象
                        mXunDianJiHua.setShiJian(string);
                    }

                },ct.get(Calendar.HOUR_OF_DAY),ct.get(Calendar.MINUTE),true).show();
            }
        });

        // 选择结束时间
        mTextview_js_shi_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ct = Calendar.getInstance();
                new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        ct.set(Calendar.HOUR_OF_DAY,i);
                        ct.set(Calendar.MINUTE,i1);
                        String string = String.valueOf(DateFormat.format("HH:mm",ct));
                        mTextview_js_shi_jian_value.setText(string);
                        // 存入对象
                        mXunDianJiHua.setJSShiJian(string);
                    }

                },ct.get(Calendar.HOUR_OF_DAY),ct.get(Calendar.MINUTE),true).show();
            }
        });

        // 品牌选择
//        mTextview_pin_pai.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
//                alertBuilder.setItems(mMengDianPingPaiData, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int index) {
//
//                        mTextview_pin_pai_value.setText(mMengDianPingPaiData[index]);
//                        // 门店品牌
//                        int id =ChanKanId(mMengDianPingpaiJsonData,mMengDianPingPaiData[index]);
//
//                        mMen_Dian_ping_pai = mMengDianPingPaiData[index];
//                        // 请求店铺
//                        menDianSearch();
//
//                        // 存入id
//                        mXunDianJiHua.setPingPaiId(id);
//                        // 存入名称
//                        mXunDianJiHua.setPingPaiStr(mMengDianPingPaiData[index]);
//
//                        alertDialog1.dismiss();
//                    }
//                });
//                alertDialog1 = alertBuilder.create();
//                alertDialog1.show();
//            }
//        });

        // 门店选择
        mTextview_ming_cheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);

                alertBuilder.setItems(mMengDianData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {

                        String[] strings = ChanKanIds(mMengDianJsonData,mMengDianData[index]);

                        // 门店显示
                        mTextview_ming_cheng_value.setText(mMengDianData[index]);
                        // 品牌显示
                        mTextview_pin_pai_value.setText(strings[3]);
                        mXunDianJiHua.setPingPaiStr(strings[3]);
                        // 店号
                        String dianHao = strings[2];
                        // 显示店号
                        mTextview_dian_hao_value.setText(dianHao);
                        // 存入店号
                        mXunDianJiHua.setMenDianHao(dianHao);

                        // 存储选择门店id
                        mXunDianJiHua.setMenDianId(Integer.valueOf(strings[0]));
                        // 存储用户选择门店
                        mXunDianJiHua.setMenDianStr(mMengDianData[index]);


                        alertDialog1.dismiss();

                    }
                });
                alertDialog1 = alertBuilder.create();
                alertDialog1.show();
            }
        });

        // 删除记录
        mButton_shan_chu_gong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mXiuGaiKey != 1000){
                    // 数据发送服务器删除
                    shanChuJiLv(String.valueOf(mXunDianJiHuas.get(mXiuGaiKey).getId()));
                    // list删除
                    mXunDianJiHuas.remove(mXiuGaiKey);
                    // 更新操作
                    initAddDelete();

                }
            }
        });

        // 工作录入
        mButton_gong_zuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 存入值
                setDatabase();
            }
        });

        // 提交工作表
        mButton_ti_jiao_gzb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mXunDianJiHuas.size() > 0){
                    gongZuoBiaoTiJiao();
                }else{
                    tiShi(mContext,"数据为空不能提交");
                }
            }
        });
    }

    public void shanChuJiLv(String strid){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("id",strid);
        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mShanChuXunDianURL)
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

    /**
     * 验证数据及存入数据库
     */
    public void setDatabase(){
        if(mXunDianJiHua.getZhou() == null || mXunDianJiHua.getZhou().equals("")){
            tiShi(mContext,"请选择周");
        }else if(mXunDianJiHua.getRiQi() == null || mXunDianJiHua.getRiQi().equals("")){
            tiShi(mContext,"请选择日期");
        }else if(mXunDianJiHua.getShiJian() == null || mXunDianJiHua.getShiJian().equals("")){
            tiShi(mContext,"请选择开始日期");
        }else if(mXunDianJiHua.getJSShiJian() == null || mXunDianJiHua.getJSShiJian().equals("")){
            tiShi(mContext,"请选择结束日期");
        }else if(mXunDianJiHua.getPingPaiStr() == null || mXunDianJiHua.getPingPaiStr().equals("")){
            tiShi(mContext,"请选择门店品牌");
        }else if(mXunDianJiHua.getMenDianStr() == null || mXunDianJiHua.getMenDianStr().equals("")){
            tiShi(mContext,"请选择门店名称");
        }else{

            // 添加到List
            if(mXiuGaiKey != 1000){
                mXunDianJiHuas.set(mXiuGaiKey,mXunDianJiHua);
            }
            // 更新下标
            initAddDelete();
        }
    }

    /**
     * 设置周数据,得到下周周一日期,日期数据
     */
    public void setZhouData(){
        mZhouData = new String[1];
        mRiQiData = new String[7];

//        mZhouData[0] = "2017-09-18周";
        SimpleDateFormat simpleDateFormats =new SimpleDateFormat("y-MM-d", Locale.CHINA);
        Calendar calendar=Calendar.getInstance(Locale.CHINA);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        //当前时间，貌似多余，其实是为了所有可能的系统一致
        calendar.setTimeInMillis(System.currentTimeMillis());
        // 获取当前周的周日
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        // 周日加一天
        calendar.add(Calendar.DATE,1);
        // 添加周数据
        mZhouData[0] = simpleDateFormats.format(calendar.getTime())+"周";
        mRiQiData[0] = simpleDateFormats.format(calendar.getTime());
        // 添加日期数据
        for(int i = 1;i<7;i++){
            calendar.add(Calendar.DATE,1);
            mRiQiData[i] = simpleDateFormats.format(calendar.getTime());
        }

    }


    /**
     * 添加删除更新操作
     */
    public void initAddDelete(){
        mXiuGaiKey = 1000;
        // 初始化对象
        mXunDianJiHua = new XunDianJiHua();
        // 清空输入内容
        qingKongNeiRong();
        // 刷新显示
        setShowJH();
    }

    /**
     * 显示计划
     *
     */
    public void setShowJH(){
        // 初始化显示组件
        initShowView();

        Log.i("巡店",mXunDianJiHuas.size()+"");
        if(mXunDianJiHuas.size() > 0){
            List<XunDianJiHua> xunDianJiHuaList = new ArrayList<>();
            xunDianJiHuaList = mXunDianJiHuas;
            // 添加排序字段
            for(int i = 0;i<xunDianJiHuaList.size();i++){
                if(xunDianJiHuaList.get(i) != null){
                    xunDianJiHuaList.get(i).setOrderBy(Integer.valueOf(strSplit(":",xunDianJiHuaList.get(i).getShiJian())));
                }
            }
            // 排序
            Collections.sort(xunDianJiHuaList);
            // 生成TextView
            for(int i = 0;i<xunDianJiHuaList.size();i++){
                if(xunDianJiHuaList.get(i) != null){
                    CreateViewRiQi(xunDianJiHuaList.get(i),i);
                }
            }
        }

    }

    /**
     * 初始化显示组件
     */
    public void initShowView(){
        // 初始化显示父组件
        mLinear_zhou_yi.removeAllViews();
        mLinear_zhou_yi.setVisibility(View.GONE);
        mLinear_zhou_er.removeAllViews();
        mLinear_zhou_er.setVisibility(View.GONE);
        mLinear_zhou_san.removeAllViews();
        mLinear_zhou_san.setVisibility(View.GONE);
        mLinear_zhou_si.removeAllViews();
        mLinear_zhou_si.setVisibility(View.GONE);
        mLinear_zhou_wu.removeAllViews();
        mLinear_zhou_wu.setVisibility(View.GONE);
        mLinear_zhou_liu.removeAllViews();
        mLinear_zhou_liu.setVisibility(View.GONE);
        mLinear_zhou_ri.removeAllViews();
        mLinear_zhou_ri.setVisibility(View.GONE);
        // 初始化编号
        ZhouYi = 1;
        ZhouEr = 1;
        ZhouSan = 1;
        ZhouSi = 1;
        ZhouWu = 1;
        ZhouLiu = 1;
        ZhouQi = 1;
    }

    /**
     * 组合viewStr
     * @param xunDianJiHua
     * @param i 当前下标
     */
    public void CreateViewRiQi(XunDianJiHua xunDianJiHua,int i){
        String[] RiQi = strSplitArray("-",xunDianJiHua.getRiQi());
        String stringRiQi = RiQi[1]+"-"+RiQi[2];
        // 详细
        String GongZhuo = xunDianJiHua.getShiJian()+"-"+xunDianJiHua.getJSShiJian()+" "
                +xunDianJiHua.getPingPaiStr()+" "+xunDianJiHua.getMenDianHao()+" "+xunDianJiHua.getMenDianStr();

        LinearLayout ll = null;
        int BiaoShi = 0;
        String strs = null;


        if(mRiQiData[0].equals(xunDianJiHua.getRiQi())){
            // 周一
            strs = stringRiQi+" 周一";
            ll = mLinear_zhou_yi;
            BiaoShi = ZhouYi;
//            Log.i("巡店",mRiQiData[0]+"|"+xunDianJiHua.getRiQi());
            ZhouYi++;

        }else if(mRiQiData[1].equals(xunDianJiHua.getRiQi())){
            // 周二
            strs = stringRiQi+" 周二";
            ll = mLinear_zhou_er;
            BiaoShi = ZhouEr;
            ZhouEr++;
        }else if(mRiQiData[2].equals(xunDianJiHua.getRiQi())){
            // 周三
            strs = stringRiQi+" 周三";
            ll = mLinear_zhou_san;
            BiaoShi = ZhouSan;
            ZhouSan++;
        }else if(mRiQiData[3].equals(xunDianJiHua.getRiQi())){
            // 周四
            strs = stringRiQi+" 周四";
            ll = mLinear_zhou_si;
            BiaoShi = ZhouSi;
            ZhouSi++;
        }else if(mRiQiData[4].equals(xunDianJiHua.getRiQi())){
            // 周五
            strs = stringRiQi+" 周五";
            ll = mLinear_zhou_wu;
            BiaoShi = ZhouWu;
            ZhouWu++;
        }else if(mRiQiData[5].equals(xunDianJiHua.getRiQi())){
            // 周六
            strs = stringRiQi+" 周六";
            ll = mLinear_zhou_liu;
            BiaoShi = ZhouLiu;
            ZhouLiu++;
        }else if(mRiQiData[6].equals(xunDianJiHua.getRiQi())){
            // 周日
            strs = stringRiQi+" 周日";
            ll = mLinear_zhou_ri;
            BiaoShi = ZhouQi;
            ZhouQi++;
        }
        // 创建标题
        if(ll != null && BiaoShi == 1){
            ll.setVisibility(View.VISIBLE);
            ll.addView(CreateTextView(strs,1000));
        }

        // 创建记录
        if(ll != null && BiaoShi > 0){
            addTextView(ll,BiaoShi,GongZhuo,i);
        }
    }

    /**
     * 添加TextView
     * @param linearLayout 显示节点
     * @param BianHao 编号
     * @param str1 工作详情
     * @param i 1000为不添加点击事件
     *
     */
    public void addTextView(LinearLayout linearLayout,int BianHao,String str1,int i){
        // 工作内容
        String strs1 = BianHao+". "+str1;
        linearLayout.addView(CreateTextView(strs1,i));
    }

    /**
     * 创建TextView
     * @param string
     * @param i 当前下标
     * @return TextView组件
     */
    public TextView CreateTextView(String string,int i){
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParam.setMargins(0,0,0,5);
        textView.setLayoutParams(layoutParam);
        textView.setText(string);
        // 显示内容
        final int xiaobiao = i;
        if(xiaobiao != 1000){
            textView.setOnClickListener(new View.OnClickListener() {
                final int xiaoBiao = xiaobiao;
                @Override
                public void onClick(View view) {
                    mXiuGaiKey = xiaoBiao;
                    qingKongNeiRong();
                }
            });
        }

        return textView;
    }

    /**
     * 清空输入内容/设置内容
     */
    public void qingKongNeiRong(){
        // 更改修改文字
        mButton_gong_zuo.setText(R.string.gong_zuo_lu_ru);
        // 隐藏删除
        mButton_shan_chu_gong.setVisibility(View.GONE);
        // 隐藏工作修改按钮
        mButton_gong_zuo.setVisibility(View.GONE);

        mTextview_zhou_value.setText("");
        mTextview_ri_qi_value.setText("");
        mTextview_shi_jian_value.setText("");
        mTextview_js_shi_jian_value.setText("");
        mTextview_pin_pai_value.setText("");
        mTextview_ming_cheng_value.setText("");
        mTextview_dian_hao_value.setText("");

        if(mXiuGaiKey != 1000){
            // 更改显示文字
            mButton_gong_zuo.setText(R.string.xiu_gai_ji_hua);
            // 显示删除
            mButton_shan_chu_gong.setVisibility(View.VISIBLE);
            // 显示工作修改按钮
            mButton_gong_zuo.setVisibility(View.VISIBLE);

            mXunDianJiHua = mXunDianJiHuas.get(mXiuGaiKey);
            mTextview_zhou_value.setText(mXunDianJiHua.getZhou()+"周");
            mTextview_ri_qi_value.setText(mXunDianJiHua.getRiQi());
            mTextview_shi_jian_value.setText(mXunDianJiHua.getShiJian());
            mTextview_js_shi_jian_value.setText(mXunDianJiHua.getJSShiJian());
            mTextview_pin_pai_value.setText(mXunDianJiHua.getPingPaiStr());
            mTextview_ming_cheng_value.setText(mXunDianJiHua.getMenDianStr());
            mTextview_dian_hao_value.setText(mXunDianJiHua.getMenDianHao());
        }
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


//    @Override
//    public void onAttachedToWindow(){
//        mCallbacks = (Callbacks)mContext;
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 销毁回调
        mCallbacks = null;
    }

}
