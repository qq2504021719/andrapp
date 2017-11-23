package com.bignerdranch.android.xundian.kaoqing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.calendarmultiselect.CalendarConfig;
import com.bignerdranch.android.calendarmultiselect.CalendarMultiSelectActivity;
import com.bignerdranch.android.calendarmultiselect.DayColor;
import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.Config;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/10/12.
 */

public class KaoQingJiLuActivity extends KaoQingCommonActivity {
    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.KaoQingJiLuActivity";

    // 返回标识
    public final static int REQUEST_PHOTO = 0;

    // 请假记录点击
    public TextView mText_ji_lu_qing_jia_ji_lu;
    // 请假记录数据列表
    public LinearLayout mKao_qing_ji_lu_qing_jia_data;
    // 请假数据
    private String mQingJiaData = "";
    // 请假记录 linear
    public LinearLayout mLinear_qing_jia_data;
    // 请假数据请求地址
    public String mQingQiuDataUrl = Config.URL+"/app/qing_jia_cha_xun_nian";
    // 请假数据日期
    public String mQingJiaRiQi = "";

    // 日期选择
    public TextView mText_ji_lu_ri_qi;
    // 日期选择value
    public TextView mText_ji_lu_ri_qi_value;
    // 本月应上小时数
    public TextView mText_ben_yue_ying_shang_xiao_shi;
    // 本月应上小时数value
    public TextView mText_ben_yue_ying_shang_xiao_shi_value;
    // 实际工作小时数
    public TextView mText_shi_ji_gong_zhuo_xiao_shi;
    // 实际工作小时数value
    public TextView mText_shi_ji_gong_zhuo_xiao_shi_value;

    public static Intent newIntent(Context packageContext, int intIsId){
        Intent i = new Intent(packageContext,KaoQingJiLuActivity.class);
        i.putExtra(EXTRA,intIsId);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kao_kao_qing_ji_lu);
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

        // 请假记录点击
        mText_ji_lu_qing_jia_ji_lu = (TextView)findViewById(R.id.text_ji_lu_qing_jia_ji_lu);
        // 请假记录列表
        mKao_qing_ji_lu_qing_jia_data = (LinearLayout)findViewById(R.id.kao_qing_ji_lu_qing_jia_data);
        // 请假数据
        mLinear_qing_jia_data = (LinearLayout)findViewById(R.id.linear_qing_jia_data);
        // 日期选择
        mText_ji_lu_ri_qi = (TextView)findViewById(R.id.text_ji_lu_ri_qi);
        // 日期选择value
        mText_ji_lu_ri_qi_value = (TextView)findViewById(R.id.text_ji_lu_ri_qi_value);
        // 本月应上小时数
        mText_ben_yue_ying_shang_xiao_shi = (TextView)findViewById(R.id.text_ben_yue_ying_shang_xiao_shi);
        // 本月应上小时数value
        mText_ben_yue_ying_shang_xiao_shi_value = (TextView)findViewById(R.id.text_ben_yue_ying_shang_xiao_shi_value);
        // 实际工作小时数
        mText_shi_ji_gong_zhuo_xiao_shi = (TextView)findViewById(R.id.text_shi_ji_gong_zhuo_xiao_shi);
        // 实际工作小时数value
        mText_shi_ji_gong_zhuo_xiao_shi_value = (TextView)findViewById(R.id.text_shi_ji_gong_zhuo_xiao_shi_value);

    }

    /**
     * 数据/值设置
     */
    public void values(){
        mActivityLeiXing = 1;
        // Token赋值
        setToken(mContext);
        // 请求数据
        XuanZheRiQiQingQiu();
    }

    /**
     * 组件操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText(R.string.kao_qing_ji_lu);

        // 日期选择
        mText_ji_lu_ri_qi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 清空默认选中
                CalendarConfig.mYiXuanZheData = new ArrayList<DayColor>();
                // 已过日期可选
                CalendarConfig.mYiGuoBuKeXuan = 0;
                // 编辑模式
                CalendarConfig.mMoShi = 1;
                // 背景色
                // CalendarConfig.mMoRenBeiJingSe = mLeiXingBeiJingSe.get(mQingJia.getLeiXing());
                // 按时间段请假日期选择
                CalendarConfig.mDanXuanMoShi = 1;
                // 启动
                Intent intent = new Intent(KaoQingJiLuActivity.this, CalendarMultiSelectActivity.class);
                startActivityForResult(intent,REQUEST_PHOTO);
            }
        });

        // 展开请假记录
        mText_ji_lu_qing_jia_ji_lu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float mDensity = getResources().getDisplayMetrics().density;

                int height = (int) (mDensity * mQingJiaHeight + 0.5);

                if (mKao_qing_ji_lu_qing_jia_data.getVisibility() == View.GONE) {
                    animateOpen(mKao_qing_ji_lu_qing_jia_data,height);
                    animationIvOpen();
                } else {
                    animateClose(mKao_qing_ji_lu_qing_jia_data);
                    animationIvClose();
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
            mQingJiaRiQi = JieXi(str,1);
            Log.i("巡店",mQingJiaRiQi);
            mText_ji_lu_ri_qi_value.setText(JieXi(str,1));
            // 请求数据
            XuanZheRiQiQingQiu();
        }

    }

    /**
     * 选择日期后重新请求数据
     */
    public void XuanZheRiQiQingQiu(){
        // 请求请假数据
        QingJiaDataQingQiu();
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
            if(msg.what==2){
                mQingJiaData = msg.obj.toString();
                isYEmian = 1;
                QingJiaDataShow(mQingJiaData,mLinear_qing_jia_data);
                // 应工作小时
                mText_ben_yue_ying_shang_xiao_shi_value.setText(mText_ben_yue_ying_shang_xiao_shi_value_str);
                // 实际工作小时
                mText_shi_ji_gong_zhuo_xiao_shi_value.setText(mText_shi_ji_gong_zhuo_xiao_shi_value_str);
            }

        }
    };

    /**
     * 请求请假数据
     */
    public void QingJiaDataQingQiu(){
        if(mToken != ""){
            final OkHttpClient client = new OkHttpClient();
            MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
            body.addFormDataPart("nian",mQingJiaRiQi);

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


}
