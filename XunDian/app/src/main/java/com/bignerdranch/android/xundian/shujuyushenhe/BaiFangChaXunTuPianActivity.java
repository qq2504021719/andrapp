package com.bignerdranch.android.xundian.shujuyushenhe;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.WeiboDialogUtils;
import com.bignerdranch.android.xundian.kaoqing.KaoQingCommonActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 * Created by Administrator on 2017/11/20.
 */

public class BaiFangChaXunTuPianActivity extends KaoQingCommonActivity {

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.BaiFangChaXunTuPianActivity";

    // 拜访图片审核数据
    public String mBaiFangShenHeData = "";

    // 提交数据
    // 用户选择的那张图
    public int isT = 1;
    // 用户选择合格不合格
    public String isHeGE = "";
    // 反馈内容
    public String FanKui = "";
    // 是否删除
    public String isShanChu = "";
    // 操作模式
    public String ZhaoZhuoMS = "抽查";

    // id
    public String id = "";

    // 图片1
    public String mPhone1 = "";
    public String mIsHeGe1 = "";
    public String mbuHui1 = "";
    // 图片2
    public String mPhone2 = "";
    public String mIsHeGe2 = "";
    public String mbuHui2 = "";
    // 图片3
    public String mPhone3 = "";
    public String mIsHeGe3 = "";
    public String mbuHui3 = "";
    // 图片4
    public String mPhone4 = "";
    public String mIsHeGe4 = "";
    public String mbuHui4 = "";

    // 图片1组件
    public ImageView mPhone1_image;
    // 图片1linear
    public LinearLayout mPhone1_shenhe_linear_bj;
    // 审核linear
    public LinearLayout mPhone1_xian_shi_mo_shi_1;
    // 抽查
    public TextView mPhone1_shen_he_chou_cha;
    // 反馈
    public TextView mPhone1_shen_he_bian_ji;
    // 删除
    public TextView mPhone1_shen_he_shan_chu;
    // 合格linear
    public LinearLayout mPhone1_xian_shi_mo_shi_2;
    // 合格
    public ImageView mPhone1_cha_xun_zhao_pian_xian_shi;
    // 不合格
    public ImageView mPhone1_cha_xun_can_shu_xian_shi;
    // 合格不合格保存
    public TextView mPhone1_chou_cha_bao_cun;
    // 反馈linear
    public LinearLayout mPhone1_xian_shi_mo_shi_3;
    // 反馈编辑
    public EditText mPhone1_bian_ji_can_shu_xiu_gai;
    // 反馈保存
    public TextView mPhone1_shen_he_ti_jiao;
    // 图片1状态 1正常 2抽查 3反馈
    public int phone1_zhuang_tai = 1;

    // 图片2组件
    public ImageView mPhone2_image;
    // 图片2linear
    public LinearLayout mPhone2_shenhe_linear_bj;
    // 审核linear
    public LinearLayout mPhone2_xian_shi_mo_shi_1;
    // 抽查
    public TextView mPhone2_shen_he_chou_cha;
    // 反馈
    public TextView mPhone2_shen_he_bian_ji;
    // 删除
    public TextView mPhone2_shen_he_shan_chu;
    // 合格linear
    public LinearLayout mPhone2_xian_shi_mo_shi_2;
    // 合格
    public ImageView mPhone2_cha_xun_zhao_pian_xian_shi;
    // 不合格
    public ImageView mPhone2_cha_xun_can_shu_xian_shi;
    // 合格不合格保存
    public TextView mPhone2_chou_cha_bao_cun;
    // 反馈linear
    public LinearLayout mPhone2_xian_shi_mo_shi_3;
    // 反馈编辑
    public EditText mPhone2_bian_ji_can_shu_xiu_gai;
    // 反馈保存
    public TextView mPhone2_shen_he_ti_jiao;
    // 图片1状态 1正常 2抽查 3反馈
    public int phone2_zhuang_tai = 1;

    // 图片3组件
    public ImageView mPhone3_image;
    // 图片3linear
    public LinearLayout mPhone3_shenhe_linear_bj;
    // 审核linear
    public LinearLayout mPhone3_xian_shi_mo_shi_1;
    // 抽查
    public TextView mPhone3_shen_he_chou_cha;
    // 反馈
    public TextView mPhone3_shen_he_bian_ji;
    // 删除
    public TextView mPhone3_shen_he_shan_chu;
    // 合格linear
    public LinearLayout mPhone3_xian_shi_mo_shi_2;
    // 合格
    public ImageView mPhone3_cha_xun_zhao_pian_xian_shi;
    // 不合格
    public ImageView mPhone3_cha_xun_can_shu_xian_shi;
    // 合格不合格保存
    public TextView mPhone3_chou_cha_bao_cun;
    // 反馈linear
    public LinearLayout mPhone3_xian_shi_mo_shi_3;
    // 反馈编辑
    public EditText mPhone3_bian_ji_can_shu_xiu_gai;
    // 反馈保存
    public TextView mPhone3_shen_he_ti_jiao;
    // 图片1状态 1正常 2抽查 3反馈
    public int phone3_zhuang_tai = 1;

    // 图片4组件
    public ImageView mPhone4_image;
    // 图片4linear
    public LinearLayout mPhone4_shenhe_linear_bj;
    // 审核linear
    public LinearLayout mPhone4_xian_shi_mo_shi_1;
    // 抽查
    public TextView mPhone4_shen_he_chou_cha;
    // 反馈
    public TextView mPhone4_shen_he_bian_ji;
    // 删除
    public TextView mPhone4_shen_he_shan_chu;
    // 合格linear
    public LinearLayout mPhone4_xian_shi_mo_shi_2;
    // 合格
    public ImageView mPhone4_cha_xun_zhao_pian_xian_shi;
    // 不合格
    public ImageView mPhone4_cha_xun_can_shu_xian_shi;
    // 合格不合格保存
    public TextView mPhone4_chou_cha_bao_cun;
    // 反馈linear
    public LinearLayout mPhone4_xian_shi_mo_shi_3;
    // 反馈编辑
    public EditText mPhone4_bian_ji_can_shu_xiu_gai;
    // 反馈保存
    public TextView mPhone4_shen_he_ti_jiao;
    // 图片1状态 1正常 2抽查 3反馈
    public int phone4_zhuang_tai = 1;

    public static Intent newIntent(Context packageContext,String string){
        Intent i = new Intent(packageContext,BaiFangChaXunTuPianActivity.class);
        i.putExtra(EXTRA,string);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shu_bai_fang_cha_xun_tu_pian);
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
        // 图片1组件
        mPhone1_image = (ImageView)findViewById(R.id.phone1_image);
        // 图片linear
        mPhone1_shenhe_linear_bj = (LinearLayout)findViewById(R.id.phone1_shenhe_linear_bj);
        // 审核linear
        mPhone1_xian_shi_mo_shi_1 = (LinearLayout)findViewById(R.id.phone1_xian_shi_mo_shi_1);
        // 抽查
        mPhone1_shen_he_chou_cha = (TextView)findViewById(R.id.phone1_shen_he_chou_cha);
        // 反馈
        mPhone1_shen_he_bian_ji = (TextView)findViewById(R.id.phone1_shen_he_bian_ji);
        // 删除
        mPhone1_shen_he_shan_chu = (TextView)findViewById(R.id.phone1_shen_he_shan_chu);
        // 合格linear
        mPhone1_xian_shi_mo_shi_2 = (LinearLayout)findViewById(R.id.phone1_xian_shi_mo_shi_2);
        // 合格
        mPhone1_cha_xun_zhao_pian_xian_shi = (ImageView)findViewById(R.id.phone1_cha_xun_zhao_pian_xian_shi);
        // 不合格
        mPhone1_cha_xun_can_shu_xian_shi = (ImageView)findViewById(R.id.phone1_cha_xun_can_shu_xian_shi);
        // 合格不合格保存
        mPhone1_chou_cha_bao_cun = (TextView)findViewById(R.id.phone1_chou_cha_bao_cun);
        // 反馈linear
        mPhone1_xian_shi_mo_shi_3 = (LinearLayout)findViewById(R.id.phone1_xian_shi_mo_shi_3);
        // 反馈编辑框
        mPhone1_bian_ji_can_shu_xiu_gai = (EditText)findViewById(R.id.phone1_bian_ji_can_shu_xiu_gai);
        // 反馈保存
        mPhone1_shen_he_ti_jiao = (TextView)findViewById(R.id.phone1_shen_he_ti_jiao);


        // 图片2组件
        mPhone2_image = (ImageView)findViewById(R.id.phone2_image);
        // 图片linear
        mPhone2_shenhe_linear_bj = (LinearLayout)findViewById(R.id.phone2_shenhe_linear_bj);
        // 审核linear
        mPhone2_xian_shi_mo_shi_1 = (LinearLayout)findViewById(R.id.phone2_xian_shi_mo_shi_1);
        // 抽查
        mPhone2_shen_he_chou_cha = (TextView)findViewById(R.id.phone2_shen_he_chou_cha);
        // 反馈
        mPhone2_shen_he_bian_ji = (TextView)findViewById(R.id.phone2_shen_he_bian_ji);
        // 删除
        mPhone2_shen_he_shan_chu = (TextView)findViewById(R.id.phone2_shen_he_shan_chu);
        // 合格linear
        mPhone2_xian_shi_mo_shi_2 = (LinearLayout)findViewById(R.id.phone2_xian_shi_mo_shi_2);
        // 合格
        mPhone2_cha_xun_zhao_pian_xian_shi = (ImageView)findViewById(R.id.phone2_cha_xun_zhao_pian_xian_shi);
        // 不合格
        mPhone2_cha_xun_can_shu_xian_shi = (ImageView)findViewById(R.id.phone2_cha_xun_can_shu_xian_shi);
        // 合格不合格保存
        mPhone2_chou_cha_bao_cun = (TextView)findViewById(R.id.phone2_chou_cha_bao_cun);
        // 反馈linear
        mPhone2_xian_shi_mo_shi_3 = (LinearLayout)findViewById(R.id.phone2_xian_shi_mo_shi_3);
        // 反馈编辑框
        mPhone2_bian_ji_can_shu_xiu_gai = (EditText)findViewById(R.id.phone2_bian_ji_can_shu_xiu_gai);
        // 反馈保存
        mPhone2_shen_he_ti_jiao = (TextView)findViewById(R.id.phone2_shen_he_ti_jiao);

        // 图片3组件
        mPhone3_image = (ImageView)findViewById(R.id.phone3_image);
        // 图片linear
        mPhone3_shenhe_linear_bj = (LinearLayout)findViewById(R.id.phone3_shenhe_linear_bj);
        // 审核linear
        mPhone3_xian_shi_mo_shi_1 = (LinearLayout)findViewById(R.id.phone3_xian_shi_mo_shi_1);
        // 抽查
        mPhone3_shen_he_chou_cha = (TextView)findViewById(R.id.phone3_shen_he_chou_cha);
        // 反馈
        mPhone3_shen_he_bian_ji = (TextView)findViewById(R.id.phone3_shen_he_bian_ji);
        // 删除
        mPhone3_shen_he_shan_chu = (TextView)findViewById(R.id.phone3_shen_he_shan_chu);
        // 合格linear
        mPhone3_xian_shi_mo_shi_2 = (LinearLayout)findViewById(R.id.phone3_xian_shi_mo_shi_2);
        // 合格
        mPhone3_cha_xun_zhao_pian_xian_shi = (ImageView)findViewById(R.id.phone3_cha_xun_zhao_pian_xian_shi);
        // 不合格
        mPhone3_cha_xun_can_shu_xian_shi = (ImageView)findViewById(R.id.phone3_cha_xun_can_shu_xian_shi);
        // 合格不合格保存
        mPhone3_chou_cha_bao_cun = (TextView)findViewById(R.id.phone3_chou_cha_bao_cun);
        // 反馈linear
        mPhone3_xian_shi_mo_shi_3 = (LinearLayout)findViewById(R.id.phone3_xian_shi_mo_shi_3);
        // 反馈编辑框
        mPhone3_bian_ji_can_shu_xiu_gai = (EditText)findViewById(R.id.phone3_bian_ji_can_shu_xiu_gai);
        // 反馈保存
        mPhone3_shen_he_ti_jiao = (TextView)findViewById(R.id.phone3_shen_he_ti_jiao);

        // 图片4组件
        mPhone4_image = (ImageView)findViewById(R.id.phone4_image);
        // 图片linear
        mPhone4_shenhe_linear_bj = (LinearLayout)findViewById(R.id.phone4_shenhe_linear_bj);
        // 审核linear
        mPhone4_xian_shi_mo_shi_1 = (LinearLayout)findViewById(R.id.phone4_xian_shi_mo_shi_1);
        // 抽查
        mPhone4_shen_he_chou_cha = (TextView)findViewById(R.id.phone4_shen_he_chou_cha);
        // 反馈
        mPhone4_shen_he_bian_ji = (TextView)findViewById(R.id.phone4_shen_he_bian_ji);
        // 删除
        mPhone4_shen_he_shan_chu = (TextView)findViewById(R.id.phone4_shen_he_shan_chu);
        // 合格linear
        mPhone4_xian_shi_mo_shi_2 = (LinearLayout)findViewById(R.id.phone4_xian_shi_mo_shi_2);
        // 合格
        mPhone4_cha_xun_zhao_pian_xian_shi = (ImageView)findViewById(R.id.phone4_cha_xun_zhao_pian_xian_shi);
        // 不合格
        mPhone4_cha_xun_can_shu_xian_shi = (ImageView)findViewById(R.id.phone4_cha_xun_can_shu_xian_shi);
        // 合格不合格保存
        mPhone4_chou_cha_bao_cun = (TextView)findViewById(R.id.phone4_chou_cha_bao_cun);
        // 反馈linear
        mPhone4_xian_shi_mo_shi_3 = (LinearLayout)findViewById(R.id.phone4_xian_shi_mo_shi_3);
        // 反馈编辑框
        mPhone4_bian_ji_can_shu_xiu_gai = (EditText)findViewById(R.id.phone4_bian_ji_can_shu_xiu_gai);
        // 反馈保存
        mPhone4_shen_he_ti_jiao = (TextView)findViewById(R.id.phone4_shen_he_ti_jiao);

    }

    /**
     * 组件操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText(R.string.xun_dian_shen_he);

        // 图片1
        // 图片展示 mPhone1_shenhe_linear_bj
        if(mPhone1_image.equals("")){
            mPhone1_shenhe_linear_bj.setVisibility(View.GONE);
        }else{
            Picasso.with(mContext).load(Config.URL+"/"+mPhone1).into(mPhone1_image);
            // 抽查
            mPhone1_shen_he_chou_cha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    phone1_zhuang_tai = 2;
                    TuPianXianShiMoShi();
                }
            });
            // 合格
            mPhone1_cha_xun_zhao_pian_xian_shi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isHeGE = "";
                    mPhone1_cha_xun_zhao_pian_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
                    mPhone1_cha_xun_can_shu_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_ku_lian));
                }
            });
            // 不合格
            mPhone1_cha_xun_can_shu_xian_shi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isHeGE = "不合格";
                    mPhone1_cha_xun_zhao_pian_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_ku_lian));
                    mPhone1_cha_xun_can_shu_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
                }
            });
            // 抽查保存
            mPhone1_chou_cha_bao_cun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isT = 1;
                    phone1_zhuang_tai = 1;
                    ZhaoZhuoMS = "抽查";
                    // 视图刷新
                    TuPianXianShiMoShi();
                    // 数据保存
                    BaiFangTupianShenHe();
                }
            });
            // 反馈
            mPhone1_shen_he_bian_ji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    phone1_zhuang_tai = 3;
                    TuPianXianShiMoShi();
                }
            });
            // 反馈内容
            mPhone1_bian_ji_can_shu_xiu_gai.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    FanKui = String.valueOf(editable).trim();
                }
            });
            // 反馈保存
            mPhone1_shen_he_ti_jiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isT = 1;
                    phone1_zhuang_tai = 1;
                    ZhaoZhuoMS = "反馈";
                    TuPianXianShiMoShi();
                    // 数据保存
                    BaiFangTupianShenHe();
                }
            });
            // 删除
            mPhone1_shen_he_shan_chu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isT = 1;
                    isShanChu = "是";
                    ZhaoZhuoMS = "删除";
                    // 数据保存
                    BaiFangTupianShenHe();
                }
            });
        }



        // 图片2展示
        if(mPhone2_image.equals("")){
            mPhone2_shenhe_linear_bj.setVisibility(View.GONE);
        }else {
            Picasso.with(mContext).load(Config.URL + "/" + mPhone2).into(mPhone2_image);
            // 抽查
            mPhone2_shen_he_chou_cha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    phone2_zhuang_tai = 2;
                    TuPianXianShiMoShi();
                }
            });
            // 合格
            mPhone2_cha_xun_zhao_pian_xian_shi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isHeGE = "";
                    mPhone2_cha_xun_zhao_pian_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
                    mPhone2_cha_xun_can_shu_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_ku_lian));
                }
            });
            // 不合格
            mPhone2_cha_xun_can_shu_xian_shi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isHeGE = "不合格";
                    mPhone2_cha_xun_zhao_pian_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_ku_lian));
                    mPhone2_cha_xun_can_shu_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
                }
            });
            // 抽查保存
            mPhone2_chou_cha_bao_cun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isT = 2;
                    phone2_zhuang_tai = 1;
                    ZhaoZhuoMS = "抽查";
                    TuPianXianShiMoShi();
                    // 数据保存
                    BaiFangTupianShenHe();
                }
            });
            // 反馈
            mPhone2_shen_he_bian_ji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    phone2_zhuang_tai = 3;
                    TuPianXianShiMoShi();
                }
            });
            // 反馈内容
            mPhone2_bian_ji_can_shu_xiu_gai.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    FanKui = String.valueOf(editable).trim();
                }
            });
            // 反馈保存
            mPhone2_shen_he_ti_jiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isT = 2;
                    phone2_zhuang_tai = 1;
                    ZhaoZhuoMS = "反馈";
                    TuPianXianShiMoShi();
                    // 数据保存
                    BaiFangTupianShenHe();
                }
            });
            // 删除
            mPhone2_shen_he_shan_chu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isT = 2;
                    isShanChu = "是";
                    ZhaoZhuoMS = "删除";
                    // 数据保存
                    BaiFangTupianShenHe();
                }
            });
        }
        // 图片3展示
        if(mPhone3_image.equals("")){
            mPhone3_shenhe_linear_bj.setVisibility(View.GONE);
        }else {
            Picasso.with(mContext).load(Config.URL + "/" + mPhone3).into(mPhone3_image);
            // 抽查
            mPhone3_shen_he_chou_cha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    phone3_zhuang_tai = 2;
                    TuPianXianShiMoShi();
                }
            });
            // 合格
            mPhone3_cha_xun_zhao_pian_xian_shi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isHeGE = "";
                    mPhone3_cha_xun_zhao_pian_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
                    mPhone3_cha_xun_can_shu_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_ku_lian));
                }
            });
            // 不合格
            mPhone3_cha_xun_can_shu_xian_shi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isHeGE = "不合格";
                    mPhone3_cha_xun_zhao_pian_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_ku_lian));
                    mPhone3_cha_xun_can_shu_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
                }
            });
            // 抽查保存
            mPhone3_chou_cha_bao_cun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isT = 3;
                    phone3_zhuang_tai = 1;
                    ZhaoZhuoMS = "抽查";
                    TuPianXianShiMoShi();
                    // 数据保存
                    BaiFangTupianShenHe();
                }
            });
            // 反馈
            mPhone3_shen_he_bian_ji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    phone3_zhuang_tai = 3;
                    TuPianXianShiMoShi();
                }
            });
            // 反馈内容
            mPhone3_bian_ji_can_shu_xiu_gai.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    FanKui = String.valueOf(editable).trim();
                }
            });
            // 反馈保存
            mPhone3_shen_he_ti_jiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isT = 3;
                    phone3_zhuang_tai = 1;
                    ZhaoZhuoMS = "反馈";
                    TuPianXianShiMoShi();
                    // 数据保存
                    BaiFangTupianShenHe();
                }
            });
            // 删除
            mPhone3_shen_he_shan_chu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isT = 3;
                    isShanChu = "是";
                    ZhaoZhuoMS = "删除";
                    // 数据保存
                    BaiFangTupianShenHe();
                }
            });
        }
        // 图片4展示
        if(mPhone4_image.equals("")){
            mPhone3_shenhe_linear_bj.setVisibility(View.GONE);
        }else {
            Picasso.with(mContext).load(Config.URL + "/" + mPhone4).into(mPhone4_image);
            // 抽查
            mPhone4_shen_he_chou_cha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    phone4_zhuang_tai = 2;
                    TuPianXianShiMoShi();
                }
            });
            // 合格
            mPhone4_cha_xun_zhao_pian_xian_shi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isHeGE = "";
                    mPhone4_cha_xun_zhao_pian_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
                    mPhone4_cha_xun_can_shu_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_ku_lian));
                }
            });
            // 不合格
            mPhone4_cha_xun_can_shu_xian_shi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isHeGE = "不合格";
                    mPhone4_cha_xun_zhao_pian_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_ku_lian));
                    mPhone4_cha_xun_can_shu_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
                }
            });
            // 抽查保存
            mPhone4_chou_cha_bao_cun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isT = 4;
                    phone4_zhuang_tai = 1;
                    ZhaoZhuoMS = "抽查";
                    TuPianXianShiMoShi();
                    // 数据保存
                    BaiFangTupianShenHe();
                }
            });
            // 反馈
            mPhone4_shen_he_bian_ji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    phone4_zhuang_tai = 3;
                    TuPianXianShiMoShi();
                }
            });
            // 反馈内容
            mPhone4_bian_ji_can_shu_xiu_gai.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    FanKui = String.valueOf(editable).trim();
                }
            });
            // 反馈保存
            mPhone4_shen_he_ti_jiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isT = 4;
                    phone4_zhuang_tai = 1;
                    ZhaoZhuoMS = "反馈";
                    TuPianXianShiMoShi();
                    // 数据保存
                    BaiFangTupianShenHe();
                }
            });
            // 删除
            mPhone4_shen_he_shan_chu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isT = 4;
                    isShanChu = "是";
                    ZhaoZhuoMS = "删除";
                    // 数据保存
                    BaiFangTupianShenHe();
                }
            });
        }

    }

    /**
     * 数据/值设置
     */
    public void values(){
        // Token赋值
        setToken(mContext);

        // 拜访图片审核数据接收
        mBaiFangShenHeData = getIntent().getStringExtra(EXTRA);

        // 图片数据解析
        tuPianDataJieXi();
    }

    /**
     * 图片数据解析
     */
    public void tuPianDataJieXi(){
        try {
            JSONObject jsonObject = new JSONObject(mBaiFangShenHeData);
            id = jsonObject.getString("id");

            mPhone1 = jsonObject.getString("phone1");
            mPhone1 = mPhone1=="null"?"":mPhone1;
            mIsHeGe1 = jsonObject.getString("phone1_is_hg");
            mIsHeGe1 = mIsHeGe1=="null"?"":mIsHeGe1;
            mbuHui1 = jsonObject.getString("phone1_fan_kui");
            mbuHui1 = mbuHui1=="null"?"":mbuHui1;

            mPhone2 = jsonObject.getString("phone2");
            mPhone2 = mPhone2=="null"?"":mPhone2;
            mIsHeGe2 = jsonObject.getString("phone2_is_hg");
            mIsHeGe2 = mIsHeGe2=="null"?"":mIsHeGe2;
            mbuHui2 = jsonObject.getString("phone2_fan_kui");
            mbuHui2 = mbuHui2=="null"?"":mbuHui2;

            mPhone3 = jsonObject.getString("phone3");
            mPhone3 = mPhone3=="null"?"":mPhone3;
            mIsHeGe3 = jsonObject.getString("phone3_is_hg");
            mIsHeGe3 = mIsHeGe3=="null"?"":mIsHeGe3;
            mbuHui3 = jsonObject.getString("phone3_fan_kui");
            mbuHui3 = mbuHui3=="null"?"":mbuHui3;

            mPhone4 = jsonObject.getString("phone4");
            mPhone4 = mPhone4=="null"?"":mPhone4;
            mIsHeGe4 = jsonObject.getString("phone4_is_hg");
            mIsHeGe4 = mIsHeGe4=="null"?"":mIsHeGe4;
            mbuHui4 = jsonObject.getString("phone4_fan_kui");
            mbuHui4 = mbuHui4=="null"?"":mbuHui4;


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片对应显示模式更改
     */
    public void TuPianXianShiMoShi(){
        if(phone1_zhuang_tai == 1){
            // 审核linear
            mPhone1_xian_shi_mo_shi_1.setVisibility(View.VISIBLE);
            // 合格linear
            mPhone1_xian_shi_mo_shi_2.setVisibility(View.GONE);
            // 反馈linear
            mPhone1_xian_shi_mo_shi_3.setVisibility(View.GONE);
            // 反馈编辑框
            mPhone1_bian_ji_can_shu_xiu_gai.setVisibility(View.GONE);
        }else if(phone1_zhuang_tai == 2){
            // 审核linear
            mPhone1_xian_shi_mo_shi_1.setVisibility(View.GONE);
            // 合格linear
            mPhone1_xian_shi_mo_shi_2.setVisibility(View.VISIBLE);
            // 勾选合格还是不合格
            if(mIsHeGe1.equals("")){
                mPhone1_cha_xun_zhao_pian_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
            }else if(mIsHeGe1.equals("不合格")){
                mPhone1_cha_xun_can_shu_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
            }
            // 反馈linear
            mPhone1_xian_shi_mo_shi_3.setVisibility(View.GONE);
            // 反馈编辑框
            mPhone1_bian_ji_can_shu_xiu_gai.setVisibility(View.GONE);

        }else if(phone1_zhuang_tai == 3){
            // 审核linear
            mPhone1_xian_shi_mo_shi_1.setVisibility(View.GONE);
            // 合格linear
            mPhone1_xian_shi_mo_shi_2.setVisibility(View.GONE);
            // 反馈linear
            mPhone1_xian_shi_mo_shi_3.setVisibility(View.VISIBLE);
            // 反馈编辑框
            mPhone1_bian_ji_can_shu_xiu_gai.setVisibility(View.VISIBLE);
            // 反馈编辑框显示内容
            if(!mbuHui1.equals("")){
                mPhone1_bian_ji_can_shu_xiu_gai.setText(mbuHui1);
            }
        }
        if(phone2_zhuang_tai == 1){
            // 审核linear
            mPhone2_xian_shi_mo_shi_1.setVisibility(View.VISIBLE);
            // 合格linear
            mPhone2_xian_shi_mo_shi_2.setVisibility(View.GONE);
            // 反馈linear
            mPhone2_xian_shi_mo_shi_3.setVisibility(View.GONE);
            // 反馈编辑框
            mPhone2_bian_ji_can_shu_xiu_gai.setVisibility(View.GONE);
        }else if(phone2_zhuang_tai == 2){
            // 审核linear
            mPhone2_xian_shi_mo_shi_1.setVisibility(View.GONE);
            // 合格linear
            mPhone2_xian_shi_mo_shi_2.setVisibility(View.VISIBLE);
            // 勾选合格还是不合格
            if(mIsHeGe2.equals("")){
                mPhone2_cha_xun_zhao_pian_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
            }else if(mIsHeGe2.equals("不合格")){
                mPhone2_cha_xun_can_shu_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
            }
            // 反馈linear
            mPhone2_xian_shi_mo_shi_3.setVisibility(View.GONE);
            // 反馈编辑框
            mPhone2_bian_ji_can_shu_xiu_gai.setVisibility(View.GONE);

        }else if(phone2_zhuang_tai == 3){
            // 审核linear
            mPhone2_xian_shi_mo_shi_1.setVisibility(View.GONE);
            // 合格linear
            mPhone2_xian_shi_mo_shi_2.setVisibility(View.GONE);
            // 反馈linear
            mPhone2_xian_shi_mo_shi_3.setVisibility(View.VISIBLE);
            // 反馈编辑框
            mPhone2_bian_ji_can_shu_xiu_gai.setVisibility(View.VISIBLE);
            // 反馈编辑框显示内容
            if(!mbuHui1.equals("")){
                mPhone2_bian_ji_can_shu_xiu_gai.setText(mbuHui1);
            }
        }
        if(phone3_zhuang_tai == 1){
            // 审核linear
            mPhone3_xian_shi_mo_shi_1.setVisibility(View.VISIBLE);
            // 合格linear
            mPhone3_xian_shi_mo_shi_2.setVisibility(View.GONE);
            // 反馈linear
            mPhone3_xian_shi_mo_shi_3.setVisibility(View.GONE);
            // 反馈编辑框
            mPhone3_bian_ji_can_shu_xiu_gai.setVisibility(View.GONE);
        }else if(phone3_zhuang_tai == 2){
            // 审核linear
            mPhone3_xian_shi_mo_shi_1.setVisibility(View.GONE);
            // 合格linear
            mPhone3_xian_shi_mo_shi_2.setVisibility(View.VISIBLE);
            // 勾选合格还是不合格
            if(mIsHeGe3.equals("")){
                mPhone3_cha_xun_zhao_pian_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
            }else if(mIsHeGe3.equals("不合格")){
                mPhone3_cha_xun_can_shu_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
            }
            // 反馈linear
            mPhone3_xian_shi_mo_shi_3.setVisibility(View.GONE);
            // 反馈编辑框
            mPhone3_bian_ji_can_shu_xiu_gai.setVisibility(View.GONE);

        }else if(phone3_zhuang_tai == 3){
            // 审核linear
            mPhone3_xian_shi_mo_shi_1.setVisibility(View.GONE);
            // 合格linear
            mPhone3_xian_shi_mo_shi_2.setVisibility(View.GONE);
            // 反馈linear
            mPhone3_xian_shi_mo_shi_3.setVisibility(View.VISIBLE);
            // 反馈编辑框
            mPhone3_bian_ji_can_shu_xiu_gai.setVisibility(View.VISIBLE);
            // 反馈编辑框显示内容
            if(!mbuHui1.equals("")){
                mPhone3_bian_ji_can_shu_xiu_gai.setText(mbuHui1);
            }
        }
        if(phone4_zhuang_tai == 1){
            // 审核linear
            mPhone4_xian_shi_mo_shi_1.setVisibility(View.VISIBLE);
            // 合格linear
            mPhone4_xian_shi_mo_shi_2.setVisibility(View.GONE);
            // 反馈linear
            mPhone4_xian_shi_mo_shi_3.setVisibility(View.GONE);
            // 反馈编辑框
            mPhone4_bian_ji_can_shu_xiu_gai.setVisibility(View.GONE);
        }else if(phone4_zhuang_tai == 2){
            // 审核linear
            mPhone4_xian_shi_mo_shi_1.setVisibility(View.GONE);
            // 合格linear
            mPhone4_xian_shi_mo_shi_2.setVisibility(View.VISIBLE);
            // 勾选合格还是不合格
            if(mIsHeGe4.equals("")){
                mPhone4_cha_xun_zhao_pian_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
            }else if(mIsHeGe4.equals("不合格")){
                mPhone4_cha_xun_can_shu_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
            }
            // 反馈linear
            mPhone4_xian_shi_mo_shi_3.setVisibility(View.GONE);
            // 反馈编辑框
            mPhone4_bian_ji_can_shu_xiu_gai.setVisibility(View.GONE);

        }else if(phone4_zhuang_tai == 3){
            // 审核linear
            mPhone4_xian_shi_mo_shi_1.setVisibility(View.GONE);
            // 合格linear
            mPhone4_xian_shi_mo_shi_2.setVisibility(View.GONE);
            // 反馈linear
            mPhone4_xian_shi_mo_shi_3.setVisibility(View.VISIBLE);
            // 反馈编辑框
            mPhone4_bian_ji_can_shu_xiu_gai.setVisibility(View.VISIBLE);
            // 反馈编辑框显示内容
            if(!mbuHui1.equals("")){
                mPhone4_bian_ji_can_shu_xiu_gai.setText(mbuHui1);
            }
        }
    }

    // 开启线程
    public static Thread mThread = null;
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
                if(msg.obj.toString().equals("审核成功")){
                    finish();
//                    TuPianXianShiMoShi();
                }
                tiShi(mContext,msg.obj.toString());
            }
        }
    };

    // 拜访查询图片审核
    public void BaiFangTupianShenHe(){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("isT",String.valueOf(isT));
        body.addFormDataPart("isHeGE",isHeGE);
        body.addFormDataPart("FanKui",FanKui);
        body.addFormDataPart("id",id);
        body.addFormDataPart("isShanChu",isShanChu);
        body.addFormDataPart("ZhaoZhuoMS",ZhaoZhuoMS);

        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(Config.URL+"/app/BaiFangShuJuChaXunTuPian")
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
