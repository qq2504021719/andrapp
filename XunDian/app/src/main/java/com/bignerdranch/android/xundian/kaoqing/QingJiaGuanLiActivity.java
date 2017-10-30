package com.bignerdranch.android.xundian.kaoqing;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bignerdranch.android.calendarmultiselect.CalendarMultiSelectActivity;
import com.bignerdranch.android.calendarmultiselect.DayColor;
import com.bignerdranch.android.calendarmultiselect.CalendarConfig;
import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.QingJia;
import com.bignerdranch.android.xundian.xundianguanli.XunDianActivity;


import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/9/29.
 */

public class QingJiaGuanLiActivity extends KaoQingCommonActivity{

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.QingJiaGuanLiActivity";
    // 返回标识
    public final static int REQUEST_PHOTO = 0;

    private AlertDialog alertDialog1;

    // 部门
    public TextView mTextview_bu_meng;
    public TextView mTextview_bu_meng_value;
    public String[] mBuMengData = new String[]{"销售部","业务部","人事部","财务部","设计部"};

    // 请假类型
    public TextView mTextview_lei_xing;
    public TextView mTextview_lei_xing_value;
    public String[] mLeiXingData = new String[]{"事假","病假","年假","其他带薪假"};
    public HashMap<String,Integer> mLeiXingBeiJingSe = new HashMap<>();


    // 请假原因
    public EditText mEditText_yuan_ying;
    // 请假原因value
    public String mEditText_yuan_ying_value;

    // 按天请假
    public LinearLayout mAn_tian_qing_jia;
    public TextView mTextview_an_tian;
    public TextView mTextview_an_tian_value;


    // 按时间段请假父
    public LinearLayout mLinear_an_shi_jian_fu_fu;
    // 按时间段请假
    public TextView mTextview_an_shi_jian;
    // 日期
    public LinearLayout mLinear_an_shi_jian;

    // 日期选择
    public TextView mTextview_an_shi_jian_ri_qi;
    public TextView mTextview_an_shi_jian_ri_qi_value;

    // 上午请假时间
    public CheckBox mShang_wu_qing_jia;
    public TextView mTextview_an_shi_jian_shang_wu;
    public TextView mTextview_an_shi_jian_shang_wu_value;

    // 下午请假时间
    public CheckBox mXia_wu_qing_jia;
    public TextView mTextview_an_shi_jian_xia_wu;
    public TextView mTextview_an_shi_jian_xia_wu_value;

    // 提交申请
    public Button mButton_ti_jiao_shen_qing;

    // 请假记录 linear
    public LinearLayout mLinear_qing_jia_data;
    // 请假数据
    private String mQingJiaData = "";

    // 请假对象
    public QingJia mQingJia = new QingJia();
    // 时间对象
    public Calendar ct;
    // 请假数据提交地址
    public String mTiJiao = Config.URL+"/app/qing_jia_add";
    // 请假数据请求地址
    public String mQingQiuDataUrl = Config.URL+"/app/qing_jia_cha_xun";



    public static Intent newIntent(Context packageContext, int intIsId){
        Intent i = new Intent(packageContext,QingJiaGuanLiActivity.class);
        i.putExtra(EXTRA,intIsId);
        return i;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kao_qing_jia_guan_li);
        mContext = this;
        // 组件初始化
        ZhuJianInit();
        // 数据/值设置
        values();
        // 组件操作
        ZhuJianCaoZhuo();

    }
    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mTitle_nei_ye = (TextView)findViewById(R.id.title_nei_ye);
        // 部门
        mTextview_bu_meng = (TextView)findViewById(R.id.textview_bu_meng);
        mTextview_bu_meng_value = (TextView)findViewById(R.id.textview_bu_meng_value);
        // 请假类型
        mTextview_lei_xing = (TextView)findViewById(R.id.textview_lei_xing);
        mTextview_lei_xing_value = (TextView)findViewById(R.id.textview_lei_xing_value);
        // 请假原因
        mEditText_yuan_ying = (EditText)findViewById(R.id.editText_yuan_ying);
        // 按天请假
        mAn_tian_qing_jia = (LinearLayout)findViewById(R.id.an_tian_qing_jia);
        mTextview_an_tian = (TextView)findViewById(R.id.textview_an_tian);
        mTextview_an_tian_value = (TextView)findViewById(R.id.textview_an_tian_value);

        // 按时间段请假父
        mLinear_an_shi_jian_fu_fu = (LinearLayout)findViewById(R.id.linear_an_shi_jian_fu_fu);
        // 按时间段请假
        mTextview_an_shi_jian = (TextView)findViewById(R.id.textview_an_shi_jian);
        mLinear_an_shi_jian = (LinearLayout)findViewById(R.id.linear_an_shi_jian);

        // 日期选择
        mTextview_an_shi_jian_ri_qi = (TextView)findViewById(R.id.textview_an_shi_jian_ri_qi);
        mTextview_an_shi_jian_ri_qi_value = (TextView)findViewById(R.id.textview_an_shi_jian_ri_qi_value);
        // 上午请假
        mShang_wu_qing_jia = (CheckBox)findViewById(R.id.shang_wu_qing_jia);
        mTextview_an_shi_jian_shang_wu = (TextView)findViewById(R.id.textview_an_shi_jian_shang_wu);
        mTextview_an_shi_jian_shang_wu_value = (TextView)findViewById(R.id.textview_an_shi_jian_shang_wu_value);

        // 下午请假
        mXia_wu_qing_jia = (CheckBox)findViewById(R.id.xia_wu_qing_jia);
        mTextview_an_shi_jian_xia_wu = (TextView)findViewById(R.id.textview_an_shi_jian_xia_wu);
        mTextview_an_shi_jian_xia_wu_value = (TextView)findViewById(R.id.textview_an_shi_jian_xia_wu_value);

        // 提交申请
        mButton_ti_jiao_shen_qing = (Button)findViewById(R.id.button_ti_jiao_shen_qing);

        // 请假数据
        mLinear_qing_jia_data = (LinearLayout)findViewById(R.id.linear_qing_jia_data);
    }
    /**
     * 值操作
     */
    public void values(){
        mActivityLeiXing = 0;
        // 类型背景色赋值
        mLeiXingBeiJingSe.put("事假",R.drawable.ri_qi_background_zi_se);
        mLeiXingBeiJingSe.put("病假",R.drawable.ri_qi_background_hong_se);
        mLeiXingBeiJingSe.put("年假",R.drawable.ri_qi_background_lv_se);
        mLeiXingBeiJingSe.put("其他法定婚丧假",R.drawable.ri_qi_background_huang_se);

        // Token赋值
        setToken(mContext);

        // 类型默认值
        mQingJia.setLeiXing("");
        mQingJia.setBuMeng("");
        mQingJia.setDay("");
        mQingJia.setShiJianDuan("");
        mQingJia.setShangWuJieShu("");
        mQingJia.setShangWuKaiShi("");
        mQingJia.setXiaWuKaiShi("");
        mQingJia.setXiaWuJieShu("");
        mQingJia.setYuanYing("");

        // 本月请假数据显示
        QingJiaDataXianShi();
        // 清空请假内容
        mLinear_qing_jia_data.removeAllViews();

    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText(R.string.qing_jia_guan_li);

        // 部门
        mTextview_bu_meng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                alertBuilder.setItems(mBuMengData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {
                        // 显示选择部门
                        mTextview_bu_meng_value.setText(mBuMengData[index]);
                        mQingJia.setBuMeng(mBuMengData[index]);
                        alertDialog1.dismiss();
                    }
                });
                alertDialog1 = alertBuilder.create();
                alertDialog1.show();
            }
        });

        // 类型
        mTextview_lei_xing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                alertBuilder.setItems(mLeiXingData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {
                        // 显示选择类型
                        mTextview_lei_xing_value.setText(mLeiXingData[index]);
                        mQingJia.setLeiXing(mLeiXingData[index]);
                        alertDialog1.dismiss();
                    }
                });
                alertDialog1 = alertBuilder.create();
                alertDialog1.show();
            }
        });


        // 按天请求
        mTextview_an_tian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mQingJia.getLeiXing() == ""){
                    tiShi(mContext,"请选择请假类型");
                }else{
                    mLinear_an_shi_jian_fu_fu.setVisibility(View.GONE);
                    // 清空默认选中
                    CalendarConfig.mYiXuanZheData = new ArrayList<DayColor>();
                   // 编辑模式
                    CalendarConfig.mMoShi = 1;
                    // 背景色
                    CalendarConfig.mMoRenBeiJingSe = mLeiXingBeiJingSe.get(mQingJia.getLeiXing());
                    // 启动
                    Intent intent = new Intent(QingJiaGuanLiActivity.this, CalendarMultiSelectActivity.class);
                    startActivityForResult(intent, REQUEST_PHOTO);
                }
            }
        });

        // 按时间段请假
        // 显示隐藏按时间段请假
        mTextview_an_shi_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float mDensity = getResources().getDisplayMetrics().density;

                int height = (int) (mDensity * 90 + 0.5);

                if (mLinear_an_shi_jian.getVisibility() == View.GONE) {
                    animateOpen(mLinear_an_shi_jian,height);
                    animationIvOpen();
                } else {
                    animateClose(mLinear_an_shi_jian);
                    animationIvClose();
                }
            }
        });

        // 日期选择
        mTextview_an_shi_jian_ri_qi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mQingJia.getLeiXing() == ""){
                    tiShi(mContext,"请选择请假类型");
                }else{
                    mAn_tian_qing_jia.setVisibility(View.GONE);
                    // 清空默认选中
                    CalendarConfig.mYiXuanZheData = new ArrayList<DayColor>();
                    // 编辑模式
                    CalendarConfig.mMoShi = 1;
                    // 背景色
                    CalendarConfig.mMoRenBeiJingSe = mLeiXingBeiJingSe.get(mQingJia.getLeiXing());
                    // 按时间段请假日期选择
                    CalendarConfig.mDanXuanMoShi = 1;
                    // 启动
                    Intent intent = new Intent(QingJiaGuanLiActivity.this, CalendarMultiSelectActivity.class);
                    startActivityForResult(intent,2);
                }
            }
        });

        // 上午请假
        mTextview_an_shi_jian_shang_wu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ct = Calendar.getInstance();
                new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        ct.set(Calendar.HOUR_OF_DAY,i);
                        ct.set(Calendar.MINUTE,i1);
                        String string = String.valueOf(DateFormat.format("HH:mm",ct));
                        // 上午开始时间
                        mQingJia.setShangWuKaiShi(string);

                        // 上午第二次时间
                        ct = Calendar.getInstance();
                        new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                ct.set(Calendar.HOUR_OF_DAY,i);
                                ct.set(Calendar.MINUTE,i1);
                                String string = String.valueOf(DateFormat.format("HH:mm",ct));
                                // 上午结束时间
                                mQingJia.setShangWuJieShu(string);
                                mTextview_an_shi_jian_shang_wu_value.setText(mQingJia.getShangWuKaiShi()+" - "+string);

                                // 选中
                                mShang_wu_qing_jia.setChecked(true);
                            }

                        },ct.get(Calendar.HOUR_OF_DAY),ct.get(Calendar.MINUTE),true).show();
                    }

                },ct.get(Calendar.HOUR_OF_DAY),ct.get(Calendar.MINUTE),true).show();
            }
        });
        // 上午取消请假
        mShang_wu_qing_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShang_wu_qing_jia.setChecked(false);
                mTextview_an_shi_jian_shang_wu_value.setText("");
                mQingJia.setShangWuKaiShi("");
                mQingJia.setShangWuJieShu("");
            }
        });

        // 下午请假
        mTextview_an_shi_jian_xia_wu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ct = Calendar.getInstance();
                new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        ct.set(Calendar.HOUR_OF_DAY,i);
                        ct.set(Calendar.MINUTE,i1);
                        String string = String.valueOf(DateFormat.format("HH:mm",ct));
                        mQingJia.setXiaWuKaiShi(string);

                        ct = Calendar.getInstance();
                        new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                ct.set(Calendar.HOUR_OF_DAY,i);
                                ct.set(Calendar.MINUTE,i1);
                                String string = String.valueOf(DateFormat.format("HH:mm",ct));
                                mQingJia.setXiaWuJieShu(string);
                                mTextview_an_shi_jian_xia_wu_value.setText(mQingJia.getXiaWuKaiShi()+" - "+string);

                                // 选中
                                mXia_wu_qing_jia.setChecked(true);
                            }

                        },ct.get(Calendar.HOUR_OF_DAY),ct.get(Calendar.MINUTE),true).show();

                    }

                },ct.get(Calendar.HOUR_OF_DAY),ct.get(Calendar.MINUTE),true).show();
            }
        });
        // 下午取消请假
        mXia_wu_qing_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mXia_wu_qing_jia.setChecked(false);
                mTextview_an_shi_jian_xia_wu_value.setText("");
                mQingJia.setXiaWuKaiShi("");
                mQingJia.setXiaWuJieShu("");
            }
        });

        // 提交数据
        mButton_ti_jiao_shen_qing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 请假原因
                mQingJia.setYuanYing(mEditText_yuan_ying.getText().toString());

                if(mQingJia.getBuMeng().equals("")){
                    tiShi(mContext,"部门不能为空");
                }else{
                    if(mQingJia.getLeiXing().equals("")){
                        tiShi(mContext,"请假类型不能为空");
                    }else{
                        if(mQingJia.getYuanYing().equals("")){
                            tiShi(mContext,"请假原因不能为空");
                        }else{
                            if(mQingJia.getDay().equals("") && mQingJia.getShiJianDuan().equals("")){
                                tiShi(mContext,"请选择请假时间");
                            }else{
                                Boolean isTiJiao = true;
                                // 按时间段请假验证
                                if(!mQingJia.getShiJianDuan().equals("")){
                                    if(mQingJia.getShangWuKaiShi().equals("")
                                       && mQingJia.getXiaWuKaiShi().equals("")){
                                        isTiJiao = false;
                                        tiShi(mContext,"时间段不能为空");
                                    }
                                }
                                // 按天请假和按时间段请假不能为空
                                if(!mQingJia.getDay().equals("") && !mQingJia.getShiJianDuan().equals("")){
                                    isTiJiao = false;
                                    tiShi(mContext,"按天请假和按时间段请假不能为空");
                                }
                                QingJiaoTiJiao();
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * 接收返回值
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 日期选择返回
        if(requestCode == REQUEST_PHOTO){
            String date = data.getStringExtra(CalendarMultiSelectActivity.mFanHuiBiao);
            String str = "";
            if(date.equals("[]")){
            }else{
                str = date;
            }
            mQingJia.setDay(str);
            mTextview_an_tian_value.setText(JieXi(str,1));
        }
        // 按时间段请假日期选择
        if(requestCode == 2){
            String date = data.getStringExtra(CalendarMultiSelectActivity.mFanHuiBiao);
            String str = "";
            if(date.equals("[]")){
            }else{
                str = date;
            }
            mQingJia.setShiJianDuan(str);
            mTextview_an_shi_jian_ri_qi_value.setText(JieXi(str,2));
        }
    }



    /**
     * 数据提交
     */
    public void QingJiaoTiJiao(){
        if(mToken != ""){
            final OkHttpClient client = new OkHttpClient();
            MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
            body.addFormDataPart("bu_meng",mQingJia.getBuMeng());
            body.addFormDataPart("qing_jia_lei_xing",mQingJia.getLeiXing());
            body.addFormDataPart("qing_jia_yuan_yin",mQingJia.getYuanYing());
//            Log.i("巡店",mQingJia.getBuMeng()+"|"+mQingJia.getLeiXing()+"|"+mQingJia.getYuanYing());
            if(!mQingJia.getDay().equals("")){
                body.addFormDataPart("an_tian_qing_jia",mQingJia.getDay());
                body.addFormDataPart("an_shi_jian_dun","");
                body.addFormDataPart("an_shi_jian_duan_shang_wu_kai_shi","");
                body.addFormDataPart("an_shi_jian_duan_shang_wu_jie_shu","");
                body.addFormDataPart("an_shi_jian_duan_xia_wu_kai_shi","");
                body.addFormDataPart("an_shi_jian_duan_xia_wu_jie_shu","");
            }else{
                body.addFormDataPart("an_tian_qing_jia","");
                // 请假时间段
                String an_shi_jian_dun = "";
                if (!mQingJia.getShiJianDuan().equals("")) {
                    an_shi_jian_dun = mQingJia.getShiJianDuan();
                }
                body.addFormDataPart("an_shi_jian_dun",an_shi_jian_dun);

                // 请假上午开始时间
                String an_shi_jian_duan_shang_wu_kai_shi = "";
                if (!mQingJia.getShangWuKaiShi().equals("")) {
                    an_shi_jian_duan_shang_wu_kai_shi = mQingJia.getShangWuKaiShi();
                }
                body.addFormDataPart("an_shi_jian_duan_shang_wu_kai_shi",an_shi_jian_duan_shang_wu_kai_shi);

                // 请假上午结束时间
                String an_shi_jian_duan_shang_wu_jie_shu = "";
                if (!mQingJia.getShangWuJieShu().equals("")) {
                    an_shi_jian_duan_shang_wu_jie_shu = mQingJia.getShangWuJieShu();
                }
                body.addFormDataPart("an_shi_jian_duan_shang_wu_jie_shu",an_shi_jian_duan_shang_wu_jie_shu);

                // 请假下午开始时间
                String an_shi_jian_duan_xia_wu_kai_shi = "";
                if (!mQingJia.getXiaWuKaiShi().equals("")) {
                    an_shi_jian_duan_xia_wu_kai_shi = mQingJia.getXiaWuKaiShi();
                }
                body.addFormDataPart("an_shi_jian_duan_xia_wu_kai_shi",an_shi_jian_duan_xia_wu_kai_shi);

                // 请假下午结束时间
                String an_shi_jian_duan_xia_wu_jie_shu = "";
                if (!mQingJia.getXiaWuJieShu().equals("")) {
                    an_shi_jian_duan_xia_wu_jie_shu = mQingJia.getXiaWuJieShu();
                }
                body.addFormDataPart("an_shi_jian_duan_xia_wu_jie_shu",an_shi_jian_duan_xia_wu_jie_shu);
//                Log.i("巡店",an_shi_jian_dun+"|"+an_shi_jian_duan_shang_wu_kai_shi+"|"+an_shi_jian_duan_shang_wu_jie_shu);
            }

            final Request request = new Request.Builder()
                    .addHeader("Authorization","Bearer "+mToken)
                    .url(mTiJiao)
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
     * Handler
     */
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            /**
             * 登录回调 msg.obj
             */
            if(msg.what==1){
                tiShi(mContext,msg.obj.toString());
                // 请求请假数据
                QingJiaDataQingQiu();
                NeiRongQingKong();
            }else if(msg.what==2){
                mQingJiaData = msg.obj.toString();
                QingJiaDataShow(mQingJiaData,mLinear_qing_jia_data);
            }

        }
    };

    /***
     * 内容清空
     */
    public void NeiRongQingKong(){
        // 清空部门
        mTextview_bu_meng_value.setText("");
        // 清空类型
        mTextview_lei_xing_value.setText("");
        // 清空原因
        mEditText_yuan_ying.setText("");
        // 清空按天请假
        mTextview_an_tian_value.setText("");
        mTextview_an_shi_jian_ri_qi_value.setText("");

        mShang_wu_qing_jia.setChecked(false);
        mTextview_an_shi_jian_shang_wu_value.setText("");

        mXia_wu_qing_jia.setChecked(false);
        mTextview_an_shi_jian_xia_wu_value.setText("");
    }

    /**
     * 请求请假数据
     */
    public void QingJiaDataQingQiu(){
        if(mToken != ""){
            final OkHttpClient client = new OkHttpClient();
            MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
            body.addFormDataPart("xx","xx");

            final Request request = new Request.Builder()
                    .addHeader("Authorization","Bearer "+mToken)
                    .url(mQingQiuDataUrl)
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

    /**
     * 请假数据显示
     */
    public void QingJiaDataXianShi(){
        // 请求数据
        QingJiaDataQingQiu();
    }

}
