package com.bignerdranch.android.xundian.kehutuozhan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.BaiDuActivity;
import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.LocationBaiDu;
import com.bignerdranch.android.xundian.comm.NeiYeCommActivity;
import com.bignerdranch.android.xundian.comm.XunDianCanShu;
import com.bignerdranch.android.xundian.kaoqing.KaoQingCommonActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/23.
 */

public class KeHuActivity extends NeiYeCommActivity implements BaiDuActivity.Callbacks{

    public MapView mMapView;

    // 门店品牌
    private EditText mEditview_ke_pin_pai;
    private String mKe_pin_pai = "";
    // 门店名称
    private EditText mEditview_ke_men_dian;
    private String mKe_men_dian = "";
    // 门店号
    private EditText mEditview_ke_dian_hao;
    private String mKe_Dian_hao = "";
    // 门店地址
    private EditText mEditview_ke_di_zhi;
    private String mKe_di_zhi = "";

    // 定位地址
    private TextView mText_tian_jiao_ding_wei_value;

    // 联系人
    private EditText mEditview_ke_lian_xi_ren;
    private String mKe_lian_xi_ren = "";
    // 联系电话
    private EditText mEditview_ke_zuo_ji;
    private String mKe_zuo_ji = "";
    // 手机号
    private EditText mEditview_ke_shou_ji;
    private String mKe_shou_ji = "";
    // 邮箱
    private EditText mEditview_ke_email;
    private String mKe_email = "";
    // 联系人职务
    private EditText mEditview_ke_zhi_wu;
    private String mKe_zhi_wu = "";
    // 其他备注
    private EditText mEditview_qi_ta_bei_zhu;
    private String mBei_zhu = "";

    // 经度
    private String mLng = "";
    // 纬度
    private String mlat = "";
    // 地址
    private String mDing_wei_addr = "";
    // 语义化地址
    private String mDing_wei_addr1 = "";

    // 提交
    private Button mButton_ke_ti_jiao;

    // 百度地图定位
    private BaiDuActivity mBaiDuActivity = null;

    // 提交数据库地址
    private String mUrl = Config.URL+"/app/ke_hu_tuo_zhan";

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.KeHuActivity";

    public static Intent newIntent(Context packageContext, int intIsId){
        Intent i = new Intent(packageContext,KeHuActivity.class);
        i.putExtra(EXTRA,intIsId);
        return i;
    }
    @Override

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ke_hu_tuo_zhan);
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

        // 地图控件
        mMapView = (MapView) findViewById(R.id.bmapView_ti_jiao);

        // 门店品牌
        mEditview_ke_pin_pai = (EditText) findViewById(R.id.editview_ke_pin_pai);
        // 门店名称
        mEditview_ke_men_dian = (EditText)findViewById(R.id.editview_ke_men_dian);
        // 门店号
        mEditview_ke_dian_hao = (EditText)findViewById(R.id.editview_ke_dian_hao);
        // 门店地址
        mEditview_ke_di_zhi = (EditText)findViewById(R.id.editview_ke_di_zhi);
        // 定位地址
        mText_tian_jiao_ding_wei_value = (TextView)findViewById(R.id.text_tian_jiao_ding_wei_value);
        // 联系人
        mEditview_ke_lian_xi_ren = (EditText)findViewById(R.id.editview_ke_lian_xi_ren);
        // 联系电话
        mEditview_ke_zuo_ji = (EditText)findViewById(R.id.editview_ke_zuo_ji);
        // 手机号
        mEditview_ke_shou_ji = (EditText)findViewById(R.id.editview_ke_shou_ji);
        // 邮箱
        mEditview_ke_email = (EditText)findViewById(R.id.editview_ke_email);
        // 联系人职务
        mEditview_ke_zhi_wu = (EditText)findViewById(R.id.editview_ke_zhi_wu);
        // 其他备注
        mEditview_qi_ta_bei_zhu = (EditText)findViewById(R.id.editview_qi_ta_bei_zhu);
        // 提交
        mButton_ke_ti_jiao = (Button)findViewById(R.id.button_ke_ti_jiao);
    }
    /**
     * 值操作
     */
    public void values(){
        // Token赋值
        setToken(mContext);

        // 百度地图
        mBaiDuActivity = new BaiDuActivity();
        mBaiDuActivity.mMapView = mMapView;
        mBaiDuActivity.mDingWeiTextView = mText_tian_jiao_ding_wei_value;
        mBaiDuActivity.BaiDuDingWeiDiaoYong(mContext);

    }

    /**
     * 定位数据回调
     */
    public void dingWeiData(LocationBaiDu locationBaiDu){
        // 经度
        mLng = String.valueOf(locationBaiDu.getLontitude());
        // 纬度
        mlat = String.valueOf(locationBaiDu.getLatitude());
        // 地址
        mDing_wei_addr = locationBaiDu.getAddr();
        // 语义化地址
        mDing_wei_addr1 = locationBaiDu.getLocationDescribe();
    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText(R.string.gong_zuo_zhong_xin_ke_hu_tuo_zhan);
        // 门店品牌
        mEditview_ke_pin_pai.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mKe_pin_pai = String.valueOf(editable);
            }
        });
        // 门店名称
        mEditview_ke_men_dian.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mKe_men_dian = String.valueOf(editable);
            }
        });
        // 门店号
        mEditview_ke_dian_hao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mKe_Dian_hao = String.valueOf(editable);
            }
        });
        // 门店地址
        mEditview_ke_di_zhi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mKe_di_zhi = String.valueOf(editable);
            }
        });
        // 联系人
        mEditview_ke_lian_xi_ren.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mKe_lian_xi_ren = String.valueOf(editable);
            }
        });
        // 联系电话
        mEditview_ke_zuo_ji.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mKe_zuo_ji = String.valueOf(editable);
            }
        });
        // 手机号
        mEditview_ke_shou_ji.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mKe_shou_ji = String.valueOf(editable);
            }
        });
        // 邮箱
        mEditview_ke_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mKe_email = String.valueOf(editable);
            }
        });
        // 联系人职务
        mEditview_ke_zhi_wu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mKe_zhi_wu = String.valueOf(editable);
            }
        });
        // 其他备注
        mEditview_qi_ta_bei_zhu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mBei_zhu = String.valueOf(editable);
            }
        });

        // 提交
        mButton_ke_ti_jiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTiJiao();
            }
        });
    }

    /**
     * 客户拓展提交
     */
    public void isTiJiao(){
        if(!mKe_men_dian.trim().isEmpty()){
            if(!mKe_pin_pai.trim().isEmpty()){
                TiJiaokehu();
            }else{
                tiShi(mContext,"品牌不能为空");
            }
        }else{
            tiShi(mContext,"门店名称不能为空");
        }
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
                String string = msg.obj.toString();
                if(string.equals("添加成功")){
                    finish();
                }
                tiShi(mContext,string);

            }
        }
    };

    /**
     * 参数提交 mXunDianCanShus
     */
    public void TiJiaokehu(){

        final OkHttpClient client = new OkHttpClient();

        String pin_pai = "";
        if(!mKe_pin_pai.trim().isEmpty()) pin_pai = mKe_pin_pai;
        String men_dian = "";
        if(!mKe_men_dian.trim().isEmpty()) men_dian = mKe_men_dian;
        String dian_hao = "";
        if(!mKe_Dian_hao.trim().isEmpty()) dian_hao = mKe_Dian_hao;
        String di_zhi = "";
        if(!mKe_di_zhi.trim().isEmpty()) di_zhi = mKe_di_zhi;
        String lian_xi_ren = "";
        if(!mKe_lian_xi_ren.trim().isEmpty()) lian_xi_ren = mKe_lian_xi_ren;
        String zuo_ji = "";
        if(!mKe_zuo_ji.trim().isEmpty()) zuo_ji = mKe_zuo_ji;
        String shou_ji = "";
        if(!mKe_shou_ji.trim().isEmpty()) shou_ji = mKe_shou_ji;
        String email = "";
        if(!mKe_email.trim().isEmpty()) email = mKe_email;
        String zhi_wu = "";
        if(!mKe_zhi_wu.trim().isEmpty()) zhi_wu = mKe_zhi_wu;

        String bei_zhu = "";
        if(!mBei_zhu.trim().isEmpty()) bei_zhu = mBei_zhu;


        //3, 发起新的请求,获取返回信息
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("pin_pai",pin_pai);
        body.addFormDataPart("men_dian",men_dian);
        body.addFormDataPart("dian_hao",dian_hao);
        body.addFormDataPart("di_zhi",di_zhi);
        body.addFormDataPart("lian_xi_ren",lian_xi_ren);
        body.addFormDataPart("zuo_ji",zuo_ji);
        body.addFormDataPart("shou_ji",shou_ji);
        body.addFormDataPart("email",email);
        body.addFormDataPart("zhi_wu",zhi_wu);
        body.addFormDataPart("bei_zhu",bei_zhu);
        body.addFormDataPart("md_lng",mLng);
        body.addFormDataPart("md_lat",mlat);
        body.addFormDataPart("ding_wei_addr",mDing_wei_addr);
        body.addFormDataPart("ding_wei_addr1",mDing_wei_addr1);


        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mUrl)
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

    @Override
    public void onAttachedToWindow(){
        mBaiDuActivity.mCallbacksc = (BaiDuActivity.Callbacks)mContext;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mBaiDuActivity != null){
            // 退出时销毁定位
            mBaiDuActivity.mLocClient.stop();
            // 关闭定位图层
            mBaiDuActivity.mBaiduMap.setMyLocationEnabled(false);
            mMapView.onDestroy();
            mMapView = null;
            // 销毁回调
            mBaiDuActivity.mCallbacksc = null;
            // 弹窗销毁
        }


    }
}
