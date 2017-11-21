package com.bignerdranch.android.xundian.shujuyushenhe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.kaoqing.KaoQingCommonActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/16.
 */

public class XunDianChaXunShenHeActivity extends KaoQingCommonActivity {

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.XunDianChaXunShenHeActivity";

    private String mMenDianDataJson = "";

    // 返回传参
    public String like = "";

    // 参数值
    // 参数id
    private String id = "";
    // 参数合格不合格=审查  shen_cha
    private String shen_cha = "";
    // 参数title
    private String canshu_name = "";
    // 参数值
    private String value = "";

    // 提交模式 1 删除 2 抽查 3修改参数值
    private String TiJiaoMoShi = "1";

    // 巡店查询审核提交
    private String XunDianShenHeTiJiaoUrl = Config.URL+"/app/CanShuShenHe";

    // 合格
    private ImageView mCha_xun_zhao_pian_xian_shi;
    // 不合格
    private ImageView mCha_xun_can_shu_xian_shi;

    // 显示模式 1默认 2 抽查 3 编辑
    private int mShwoMoShi = 1;

    // 模式标题
    private TextView mMo_shi_title;
    // 参数名称
    private TextView mShen_he_title;
    // 参数值
    private TextView mShen_he_content;
    // 参数编辑框
    private EditText mBian_ji_can_shu_xiu_gai;
    // 显示模式 1
    private LinearLayout mXian_shi_mo_shi_1;
    // 抽查按钮
    private TextView mShen_he_chou_cha;
    // 编辑按钮
    private TextView mShen_he_bian_ji;
    // 删除按钮
    private TextView mShen_he_shan_chu;
    // 显示模式 2 抽查
    private LinearLayout mXian_shi_mo_shi_2;
    // 抽查模式保存
    private TextView mChou_cha_bao_cun;
    // 显示模式 3 编辑
    private LinearLayout mXian_shi_mo_shi_3;
    // 编辑模式保存
    private TextView mShen_he_ti_jiao;

    public static Intent newIntent(Context packageContext,String string){
        Intent i = new Intent(packageContext,XunDianChaXunShenHeActivity.class);
        i.putExtra(EXTRA,string);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shu_xun_dian_cha_xun_shen_he);
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
        //合格
        mCha_xun_zhao_pian_xian_shi = (ImageView)findViewById(R.id.cha_xun_zhao_pian_xian_shi);
        // 不合格
        mCha_xun_can_shu_xian_shi = (ImageView)findViewById(R.id.cha_xun_can_shu_xian_shi);

        // 模式标题
        mMo_shi_title = (TextView)findViewById(R.id.mo_shi_title);
        // 参数名称
        mShen_he_title = (TextView)findViewById(R.id.shen_he_title);
        // 参数值
        mShen_he_content = (TextView)findViewById(R.id.shen_he_content);
        // 参数编辑框
        mBian_ji_can_shu_xiu_gai = (EditText)findViewById(R.id.bian_ji_can_shu_xiu_gai);
        // 显示模式 1
        mXian_shi_mo_shi_1 = (LinearLayout)findViewById(R.id.xian_shi_mo_shi_1);
        // 抽查按钮
        mShen_he_chou_cha = (TextView)findViewById(R.id.shen_he_chou_cha);
        // 编辑按钮
        mShen_he_bian_ji = (TextView)findViewById(R.id.shen_he_bian_ji);
        // 删除按钮
        mShen_he_shan_chu = (TextView)findViewById(R.id.shen_he_shan_chu);
        // 显示模式 2 抽查
        mXian_shi_mo_shi_2 = (LinearLayout)findViewById(R.id.xian_shi_mo_shi_2);
        // 抽查模式保存
        mChou_cha_bao_cun = (TextView)findViewById(R.id.chou_cha_bao_cun);
        // 显示模式 3 编辑
        mXian_shi_mo_shi_3 = (LinearLayout)findViewById(R.id.xian_shi_mo_shi_3);
        // 编辑模式保存
        mShen_he_ti_jiao = (TextView)findViewById(R.id.shen_he_ti_jiao);
    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText(R.string.xun_dian_shen_he);
        mShen_he_title.setText(canshu_name);
        mShen_he_content.setText(value);
        mBian_ji_can_shu_xiu_gai.setText(value);

        // 合格
        mCha_xun_zhao_pian_xian_shi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shen_cha = "";
                ZhaoCanXianShi();
            }
        });
        // 不合格
        mCha_xun_can_shu_xian_shi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shen_cha = "审查";
                ZhaoCanXianShi();
            }
        });

        // 抽查
        mShen_he_chou_cha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMo_shi_title.setText("抽查");
                mShwoMoShi = 2;
                XianShiMoShi();
            }
        });
        // 编辑
        mShen_he_bian_ji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMo_shi_title.setText("编辑");
                mShwoMoShi = 3;
                XianShiMoShi();
            }
        });

        // 参数编辑编辑
        mBian_ji_can_shu_xiu_gai.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value = String.valueOf(editable).trim();
            }
        });

        // 删除
        mShen_he_shan_chu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TiJiaoMoShi = "1";
                shenHeDataTiJiao();
            }
        });

        // 抽查保存
        mChou_cha_bao_cun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TiJiaoMoShi = "2";
                shenHeDataTiJiao();
            }
        });
        // 编辑保存
        mShen_he_ti_jiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TiJiaoMoShi = "3";
                shenHeDataTiJiao();
            }
        });
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
//                finish();
                Intent i = XunDianChaXunActivity.newIntent(XunDianChaXunShenHeActivity.this,like);
                startActivity(i);
                finish();
                tiShi(mContext,msg.obj.toString());
            }
        }
    };

    // 审核数据提交
    public void shenHeDataTiJiao(){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("id",id);
        body.addFormDataPart("shen_cha",shen_cha);
        body.addFormDataPart("value",value);
        body.addFormDataPart("TiJiaoMoShi",TiJiaoMoShi);

        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(XunDianShenHeTiJiaoUrl)
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
     * 值操作
     */
    public void values() {
        // Token赋值
        setToken(mContext);
        // 门店信息接收
        mMenDianDataJson = getIntent().getStringExtra(EXTRA);

        // 参数解析
        canShuJieXi();
        //合格,不合格
        ZhaoCanXianShi();
    }

    public void canShuJieXi(){
        try {
            JSONObject jsonObject = new JSONObject(mMenDianDataJson);
            like = jsonObject.getString("mendian_name");
            id = jsonObject.getString("id");
            shen_cha = jsonObject.getString("shen_cha");
            if(shen_cha.equals("null")){
                shen_cha = "";
            }
            canshu_name = jsonObject.getString("canshu_name");
            value = jsonObject.getString("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 合格,不合格
     */
    public void ZhaoCanXianShi(){
        if(shen_cha.equals("")){
            mCha_xun_can_shu_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_ku_lian));
            mCha_xun_zhao_pian_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
        }else if (shen_cha.equals("审查")){
            mCha_xun_can_shu_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
            mCha_xun_zhao_pian_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_ku_lian));
        }
    }

    public void XianShiMoShi(){
        if(mShwoMoShi == 1){
            // 参数值
            mShen_he_content.setVisibility(View.VISIBLE);
            // 参数编辑框
            mBian_ji_can_shu_xiu_gai.setVisibility(View.GONE);
            // 默认模式
            mXian_shi_mo_shi_1.setVisibility(View.VISIBLE);
            // 抽查模式
            mXian_shi_mo_shi_2.setVisibility(View.GONE);
            // 编辑模式
            mXian_shi_mo_shi_3.setVisibility(View.GONE);
        }else if(mShwoMoShi == 2){
            mXian_shi_mo_shi_2.setVisibility(View.VISIBLE);
            mXian_shi_mo_shi_1.setVisibility(View.GONE);
            mXian_shi_mo_shi_3.setVisibility(View.GONE);
        }else if(mShwoMoShi == 3){
            // 参数值
            mShen_he_content.setVisibility(View.GONE);
            // 参数编辑框
            mBian_ji_can_shu_xiu_gai.setVisibility(View.VISIBLE);
            mXian_shi_mo_shi_2.setVisibility(View.GONE);
            mXian_shi_mo_shi_1.setVisibility(View.GONE);
            mXian_shi_mo_shi_3.setVisibility(View.VISIBLE);
        }
    }

}
