package com.bignerdranch.android.xundian.xundianjihua;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.NeiYeCommActivity;
import com.bignerdranch.android.xundian.comm.XunDianJiHua;
import com.bignerdranch.android.xundian.kaoqing.KaoQingCommonActivity;
import com.bignerdranch.android.xundian.model.XunDianJiHuaModel;
import com.bignerdranch.android.xundian.xundianguanli.XunDianActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/19.
 */

public class TJJiHuaActivity extends KaoQingCommonActivity implements KaoQingCommonActivity.Callbacks{
    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.TJJiHuaActivity";

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
    // 日期数据-周
    public String[] mRiQiDataZhou;
    // 日期数据 日期为key-周几为值
    public String[] mRiQiKeyZhouJi;

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
    // 品牌alert View
    private View mViewPPD;
    // 品牌alert
    private LinearLayout mBf_search_men_dian_pp;
    public Dialog dialogpp = null;
    // 显示品牌
    private TextView mTextview_pin_pai_value;

    // 选择门店名称
    private TextView mTextview_ming_cheng;
    // 公司alert View
    private View mViewD;
    // 公司alert
    private LinearLayout mBf_search_men_dian;
    public Dialog dialog = null;
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

    // 巡店日期请求
    public String mRiQiQingQiu = Config.URL+"/app/xun_dian_zhou_ri_qi";

    // 巡店日期String
    public String mXunDianRiQiData = "";

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

    // 巡店数据提交地址
    public String mTiJiaoDiZhi = Config.URL+"/app/xun_dian_ji_hua_ti_jiao";

    public static Intent newIntent(Context packageContext, int intIsId){
        Intent i = new Intent(packageContext,TJJiHuaActivity.class);
        i.putExtra(EXTRA,intIsId);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tian_jia_ji_hua);

        mContext = this;
        // 组件初始化
        ZhuJianInit();

        // 组件操作
        ZhuJianCaoZhuo();
        // 数据/值设置
        values();

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
                mXunDianRiQiData = msg.obj.toString();
                // 日期请求后处理才在
                RiQiQingQiuHouCZ();
            }else if(msg.what == 2){
                tiShi(mContext,msg.obj.toString());
                // 删除数据
                mXunDianJiHuaModel.deleteXunDianJiHua(String.valueOf(mLogin.getUid()));
                // 对象数据清空
                mXunDianJiHuas = new ArrayList<>();
                // 刷新视图
                initAddDelete();
            }
        }
    };

    /**
     * 日期请求后处理才在
     */
    public void RiQiQingQiuHouCZ(){
        setZhouData();
        // 显示数据库计划
        setShowJH();
    }

    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        // title设置
        mTitle_nei_ye = (TextView)findViewById(R.id.title_nei_ye);
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
        // Token赋值
        setToken(mContext);
        // 巡店计划model
        mXunDianJiHuaModel = XunDianJiHuaModel.get(mContext);
        // 设置周数据,日期数据
//      // 门店弹出View
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
        LoadingStringEdit("品牌加载中...");
        // 品牌搜索
        pingPaiSouShuo();
        // 数据库查询巡店计划
        DatabaseXunDian();
        // 查询签到日期
        ChaXunQianDaoRiQi();
        // 存储用户id
        mXunDianJiHua.setUid(mLogin.getUid());

    }



    /**
     * 查询签到日期
     */
    public void ChaXunQianDaoRiQi(){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("XXX","XXX");
        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mRiQiQingQiu)
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
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText(R.string.tj_xun_dian_ji_hua);

        // 显示周选择项
        mTextview_zhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                alertBuilder.setItems(mZhouData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {
                        Boolean is = true;
                        if(mXunDianJiHuas.size() > 0){
                            if(mXunDianJiHuas.get(0).getZhou().trim().equals(mZhouData[index])){
                                is = true;
                            }else{
                                is = false;
                                tiShi(mContext,mXunDianJiHuas.get(0).getZhou()+"未提交,请先提交");
                            }
                        }
                        if(is){
                            // 显示选择周
                            mTextview_zhou_value.setText(mZhouData[index]);
                            // 更新用户选择周
                            mXunDianJiHua.setZhou(mZhouData[index]);
                            // 更新日期
                            setRiqi(mXunDianJiHua.getZhou());
                        }

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
                alertBuilder.setItems(mRiQiDataZhou, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {
                        // 显示选择日期
                        mTextview_ri_qi_value.setText(mRiQiDataZhou[index]);
                        // 更新用户选择日期
                        mXunDianJiHua.setRiQi(mRiQiData[index]);
                        Log.i("巡店",mRiQiData[0]+"|"+mXunDianJiHua.getRiQi());
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
        mTextview_pin_pai.setOnClickListener(new View.OnClickListener() {
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

        // 门店选择
        mTextview_ming_cheng.setOnClickListener(new View.OnClickListener() {
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

        // 工作录入
        mButton_gong_zuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 存入值
                setDatabase();
            }
        });

        // 删除记录
        mButton_shan_chu_gong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mXiuGaiKey != 1000){
                    // 数据库删除
                    mXunDianJiHuaModel.deleteXunDianJiHua(String.valueOf(mXunDianJiHuas.get(mXiuGaiKey).getId()),String.valueOf(mLogin.getUid()));
                    // list删除
                    mXunDianJiHuas.remove(mXiuGaiKey);
                    // 更新操作
                    initAddDelete();
                }
            }
        });

        // 提交工作表
        mButton_ti_jiao_gzb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mXunDianJiHuas.size() > 0){
                    gongZuoBiaoTiJiao();
                }else{
                    tiShi(mContext,"请录入计划");
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
                }

            }
        });
    }

    /**
     * 工作表提交
     */
    public void gongZuoBiaoTiJiao(){
        JSONObject jsonObjects = new JSONObject();
        for(int i = 0;i<mXunDianJiHuas.size();i++){
            if(mXunDianJiHuas.get(i) != null){
                JSONObject jsonObject = new JSONObject();
                try {
                    XunDianJiHua xunDianJiHua = mXunDianJiHuas.get(i);
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

        final OkHttpClient client = new OkHttpClient();
        //3, 发起新的请求,获取返回信息
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonObjects.toString());
        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mTiJiaoDiZhi)
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
                    mHandler.obtainMessage(2, response.body().string()).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mThread.start();
    }

    /**
     * 设置周数据
     */
    public void setZhouData(){
        try {
            if(!mXunDianRiQiData.equals("")){
                JSONArray jsonArray = new JSONArray(mXunDianRiQiData);
                JSONArray JSONArray1 = new JSONArray(jsonArray.get(0).toString());
                mZhouData = new String[JSONArray1.length()];
                for(int i = 0;i<JSONArray1.length();i++){
                    mZhouData[i] = JSONArray1.get(i).toString().trim()+"周";
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新日期信息 mXunDianJiHua.setMenDianStr
     */
    public void setRiqi(String stringc){
        mRiQiData = new String[7];
        mRiQiDataZhou = new String[7];
        mRiQiKeyZhouJi = new String[7];
        try {
            if(!mXunDianRiQiData.equals("")){
                JSONArray jsonArray = new JSONArray(mXunDianRiQiData);
                // 周json对象
                JSONArray JSONArray1 = new JSONArray(jsonArray.get(0).toString());
                // 日期json对象
                JSONArray jsonArray1 = new JSONArray(jsonArray.get(1).toString());

                for(int i = 0;i<JSONArray1.length();i++){

                    if(stringc.trim().equals(JSONArray1.get(i).toString()+"周".trim())){

                        JSONArray jsonArray2 = new JSONArray(jsonArray1.get(i).toString());

                        for(int c = 0;c<jsonArray2.length();c++){
                            mRiQiData[c] = jsonArray2.get(c).toString().trim();
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 得到每天周几
        for(int i = 0;i<mRiQiData.length;i++){
            mRiQiDataZhou[i] = mRiQiData[i]+" "+getCurrentWeekOfMonth(mRiQiData[i]);
        }
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
            if(mXiuGaiKey == 1000){
                mXunDianJiHua.setId(mId);
            }
            // 添加数据
            mXunDianJiHuaModel.addIsUpdate(mXunDianJiHua);
            // 添加到List
            if(mXiuGaiKey != 1000){
                mXunDianJiHuas.set(mXiuGaiKey,mXunDianJiHua);
            }else{
                mXunDianJiHuas.add(mXunDianJiHua);
            }

            mId = mXunDianJiHuas.size();

            // 更新下标
            initAddDelete();
        }
    }

    /**
     * 添加删除更新操作
     */
    public void initAddDelete(){
        mXiuGaiKey = 1000;
        // 初始化对象
        mXunDianJiHua = new XunDianJiHua();
        // 存储用户id
        mXunDianJiHua.setUid(mLogin.getUid());
        // 清空输入内容
        qingKongNeiRong();
        // 刷新显示
        setShowJH();
    }

    /**
     * 查询数据库数据
     */
    public void DatabaseXunDian(){
        // 查询数据
        mXunDianJiHuas = mXunDianJiHuaModel.getXunDianJiHuas(String.valueOf(mLogin.getUid()));
        //
        mId = mXunDianJiHuas.size();
    }

    /**
     * 显示计划
     *
     */
    public void setShowJH(){
        // 初始化显示组件
        initShowView();
        if(mXunDianJiHuas.size() > 0){
            // 根据数据库查询设置日期
            setRiqi(mXunDianJiHuas.get(0).getZhou());

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
        // 日期
        String stringRiQi = RiQi[1]+"-"+RiQi[2];
        // 日期-周几
        String strs = stringRiQi+" "+getCurrentWeekOfMonth(xunDianJiHua.getRiQi());
        // 详细
        String GongZhuo = xunDianJiHua.getShiJian()+"-"+xunDianJiHua.getJSShiJian()+" "
                +xunDianJiHua.getPingPaiStr()+" "+xunDianJiHua.getMenDianHao()+" "+xunDianJiHua.getMenDianStr();

//        Log.i("巡店",mRiQiData.length+"|"+xunDianJiHua.getRiQi());
        if(mRiQiData[0].equals(xunDianJiHua.getRiQi())){
            // 周一

            if(ZhouYi == 1){
                mLinear_zhou_yi.setVisibility(View.VISIBLE);
                mLinear_zhou_yi.addView(CreateTextView(strs,1000));
            }
            addTextView(mLinear_zhou_yi,ZhouYi,GongZhuo,i);
            ZhouYi++;

        }else if(mRiQiData[1].equals(xunDianJiHua.getRiQi())){
           // 周二
            if(ZhouEr == 1){
                mLinear_zhou_er.setVisibility(View.VISIBLE);
                mLinear_zhou_er.addView(CreateTextView(strs,1000));
            }
            addTextView(mLinear_zhou_er,ZhouEr,GongZhuo,i);
            ZhouEr++;
        }else if(mRiQiData[2].equals(xunDianJiHua.getRiQi())){
            // 周三
            if(ZhouSan == 1){
                mLinear_zhou_san.setVisibility(View.VISIBLE);
                mLinear_zhou_san.addView(CreateTextView(strs,1000));
            }
            addTextView(mLinear_zhou_san,ZhouSan,GongZhuo,i);
            ZhouSan++;
        }else if(mRiQiData[3].equals(xunDianJiHua.getRiQi())){
            // 周四
            if(ZhouSi == 1){
                mLinear_zhou_si.setVisibility(View.VISIBLE);
                mLinear_zhou_si.addView(CreateTextView(strs,1000));
            }
            addTextView(mLinear_zhou_si,ZhouSi,GongZhuo,i);
            ZhouSi++;
        }else if(mRiQiData[4].equals(xunDianJiHua.getRiQi())){
            // 周五
            if(ZhouWu == 1){
                mLinear_zhou_wu.setVisibility(View.VISIBLE);
                mLinear_zhou_wu.addView(CreateTextView(strs,1000));
            }
            addTextView(mLinear_zhou_wu,ZhouWu,GongZhuo,i);
            ZhouWu++;
        }else if(mRiQiData[5].equals(xunDianJiHua.getRiQi())){
            // 周六
            if(ZhouLiu == 1){
                mLinear_zhou_liu.setVisibility(View.VISIBLE);
                mLinear_zhou_liu.addView(CreateTextView(strs,1000));
            }
            addTextView(mLinear_zhou_liu,ZhouLiu,GongZhuo,i);
            ZhouLiu++;
        }else if(mRiQiData[6].equals(xunDianJiHua.getRiQi())){
            // 周日
            if(ZhouQi == 1){
                mLinear_zhou_ri.setVisibility(View.VISIBLE);
                mLinear_zhou_ri.addView(CreateTextView(strs,1000));
            }
            addTextView(mLinear_zhou_ri,ZhouQi,GongZhuo,i);
            ZhouQi++;
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

            mXunDianJiHua = mXunDianJiHuas.get(mXiuGaiKey);
            mTextview_zhou_value.setText(mXunDianJiHua.getZhou());
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
            setData(string,1);
            ShowMenDian(string,1);

        }else if(is == 2){
            mMengDianJsonData = string;
            setData(string,2);
            ShowMenDian(mMengDianJsonData,2);
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
            }
        }catch (JSONException e){

        }
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
                // 存入名称
                mXunDianJiHua.setPingPaiStr(nameText);
                // 品牌
                mTextview_pin_pai_value.setText(nameText);
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


                // 品牌显示
                mTextview_pin_pai_value.setText(men_dian_ping_paiText);
                mXunDianJiHua.setPingPaiStr(men_dian_ping_paiText);
                // 显示店号
                mTextview_dian_hao_value.setText(men_dian_haoText);
                // 存入店号
                mXunDianJiHua.setMenDianHao(men_dian_haoText);

                // 存储选择门店id
                mXunDianJiHua.setMenDianId(Integer.valueOf(idText));
                // 存储用户选择门店
                mXunDianJiHua.setMenDianStr(nameText);

                mTextview_ming_cheng_value.setText(men_dian_ping_paiText+"-"+men_dian_haoText+"-"+nameText);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dingWeiData() {

    }

    @Override
    public void onAttachedToWindow(){
        mCallbacksc = (KaoQingCommonActivity.Callbacks)mContext;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 销毁回调
        mCallbacksc = null;
    }
}
