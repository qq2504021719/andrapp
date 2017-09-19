package com.bignerdranch.android.xundian.xundianjihua;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.NeiYeCommActivity;
import com.bignerdranch.android.xundian.comm.XunDianJiHua;
import com.bignerdranch.android.xundian.xundianguanli.XunDianActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19.
 */

public class TJJiHuaActivity extends NeiYeCommActivity implements NeiYeCommActivity.Callbacks{
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

        Log.i("巡店",mXunDianJiHuas.size()+"");
    }

    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mTitle_nei_ye = (TextView)findViewById(R.id.title_nei_ye);
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
    }

    /**
     * 值操作
     */
    public void values(){
        // Token赋值
        setToken(mContext);
        // 设置周数据
        setZhouData();
        // 设置门店/品牌
        setData(mMengDianPingpaiJsonData,1);
        // 请求店铺
        menDianSearch();
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

        // 选择日期
        mTextview_ri_qi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        String string = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                        mTextview_ri_qi_value.setText(string);
                        // 存入对象
                        mXunDianJiHua.setRiQi(string);

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
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
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                alertBuilder.setItems(mMengDianPingPaiData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {

                        mTextview_pin_pai_value.setText(mMengDianPingPaiData[index]);
                        // 门店品牌
                        int id =ChanKanId(mMengDianPingpaiJsonData,mMengDianPingPaiData[index]);

                        // 请求店铺
                        menDianSearch();

                        // 存入id
                        mXunDianJiHua.setPingPaiId(id);
                        // 存入名称
                        mXunDianJiHua.setPingPaiStr(mMengDianPingPaiData[index]);

                        alertDialog1.dismiss();
                    }
                });
                alertDialog1 = alertBuilder.create();
                alertDialog1.show();
            }
        });

        // 门店选择
        mTextview_ming_cheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);

                alertBuilder.setItems(mMengDianData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {

                        mTextview_ming_cheng_value.setText(mMengDianData[index]);

                        int id = ChanKanId(mMengDianJsonData,mMengDianData[index]);
                        // 店号
                        String dianHao = ChanKanKey(mMengDianJsonData,mMengDianData[index],"men_dian_hao");
                        // 显示店号
                        mTextview_dian_hao_value.setText(dianHao);
                        // 存入店号
                        mXunDianJiHua.setMenDianHao(dianHao);

                        // 存储选择门店id
                        mXunDianJiHua.setMenDianId(id);
                        // 存储用户选择门店
                        mXunDianJiHua.setMenDianStr(mMengDianData[index]);

                        alertDialog1.dismiss();

                    }
                });
                alertDialog1 = alertBuilder.create();
                alertDialog1.show();
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
    }

    /**
     * 设置周数据
     */
    public void setZhouData(){
        mZhouData = new String[2];
        mZhouData[0] = "2017-09-18周";
        mZhouData[1] = "2017-09-25周";
    }

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

        }






    }

    /**
     * 数据请求成功回调
     * @param string 数据
     * @param is 1 品牌 2门店
     */
    public void shuJuHuiDiao(String string,int is){
        mMengDianJsonData = string;
        if(is == 1){

        }else if(is == 2){
            setData(mMengDianJsonData,2);
        }

    }

    @Override
    public void onAttachedToWindow(){
        mCallbacks = (Callbacks)mContext;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 销毁回调
        mCallbacks = null;
    }
}
