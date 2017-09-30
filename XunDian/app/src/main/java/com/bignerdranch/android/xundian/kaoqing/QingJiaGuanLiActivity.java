package com.bignerdranch.android.xundian.kaoqing;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.xundianguanli.XunDianActivity;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/9/29.
 */

public class QingJiaGuanLiActivity extends KaoQingCommonActivity{

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.QingJiaGuanLiActivity";


    private AlertDialog alertDialog1;

    // 部门
    public TextView mTextview_bu_meng;
    public TextView mTextview_bu_meng_value;
    public String[] mBuMengData = new String[]{"销售部","业务部","人事部","财务部","设计部"};

    // 请假类型
    public TextView mTextview_lei_xing;
    public TextView mTextview_lei_xing_value;
    public String[] mLeiXingData = new String[]{"事假","病假","年假","法定婚丧假","其他"};

    // 请假原因
    public EditText mEditText_yuan_ying;

    // 按天请假
    public TextView mTextview_an_tian;
    public TextView mTextview_an_tian_value;

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
        // 部门
        mTextview_bu_meng = (TextView)findViewById(R.id.textview_bu_meng);
        mTextview_bu_meng_value = (TextView)findViewById(R.id.textview_bu_meng_value);
        // 请假类型
        mTextview_lei_xing = (TextView)findViewById(R.id.textview_lei_xing);
        mTextview_lei_xing_value = (TextView)findViewById(R.id.textview_lei_xing_value);
        // 请假原因
        mEditText_yuan_ying = (EditText)findViewById(R.id.editText_yuan_ying);
        // 按天请假
        mTextview_an_tian = (TextView)findViewById(R.id.textview_an_tian);
        mTextview_an_tian_value = (TextView)findViewById(R.id.textview_an_tian_value);
    }
    /**
     * 值操作
     */
    public void values(){
        // Token赋值
        setToken(mContext);
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
                        // 显示选择周
                        mTextview_bu_meng_value.setText(mBuMengData[index]);
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
                        // 显示选择周
                        mTextview_lei_xing_value.setText(mLeiXingData[index]);
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
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        String string = year+"年"+(monthOfYear+1)+"月"+dayOfMonth;
                        mTextview_an_tian_value.setText(string);

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

}
