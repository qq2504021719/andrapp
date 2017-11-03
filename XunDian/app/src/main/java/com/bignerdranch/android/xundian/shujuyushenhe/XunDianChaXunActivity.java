package com.bignerdranch.android.xundian.shujuyushenhe;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.kaoqing.KaoQingCommonActivity;
import com.bignerdranch.android.xundian.xundianguanli.XunDianActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/10/31.
 */

public class XunDianChaXunActivity extends KaoQingCommonActivity implements KaoQingCommonActivity.Callbacks{

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.XunDianChaXunController";

    // 日期
    TextView mXun_dian_text_cha_xun_ri_qi;
    TextView mXun_dian_text_cha_xun_ri_qi_value;
    // 开始时间
    String kstime;
    String jstime;
    // 结束时间

    // 门店名称
    private TextView mText_bf_gong_si_ming_cheng;
    private TextView mText_bf_gong_si_ming_cheng_value;
    // 公司alert View
    private View mViewD;
    // 公司alert
    private LinearLayout mBf_search_men_dian;
    public Dialog dialog = null;
    String likebt;

    // 门店品牌
    private TextView mText_bf_gong_si_pin_pai_value;
    String pinPai;

    // 门店编号
    private TextView mText_bf_gong_si_bian_hao_value;

    // 巡店项目名称
    EditText mXun_dian_xiang_mu_ming_chen_search;
    String XiangMuMingCheng;

    public static Intent newIntent(Context packageContext, int intIsId){
        Intent i = new Intent(packageContext,XunDianChaXunActivity.class);
        i.putExtra(EXTRA,intIsId);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shu_xun_dian_cha_xun);
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

        // 日期
        mXun_dian_text_cha_xun_ri_qi = (TextView)findViewById(R.id.xun_dian_text_cha_xun_ri_qi);
        mXun_dian_text_cha_xun_ri_qi_value = (TextView)findViewById(R.id.xun_dian_text_cha_xun_ri_qi_value);

        // 公司名称
        mText_bf_gong_si_ming_cheng = (TextView)findViewById(R.id.text_bf_gong_si_ming_cheng);
        mText_bf_gong_si_ming_cheng_value = (TextView)findViewById(R.id.text_bf_gong_si_ming_cheng_value);

        // 公司品牌
        mText_bf_gong_si_pin_pai_value = (TextView)findViewById(R.id.text_bf_gong_si_pin_pai_value);

        // 公司编号
        mText_bf_gong_si_bian_hao_value = (TextView)findViewById(R.id.text_bf_gong_si_bian_hao_value);

        // 巡店项目名称

    }
    /**
     * 值操作
     */
    public void values(){
        // Token赋值
        setToken(mContext);



        // 公司弹出View
        // 获取布局文件
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewD = inflater.inflate(R.layout.alert_kao_bai_fang_guan_li_search, null);
        // 公司 alert 弹出
        mBf_search_men_dian = mViewD.findViewById(R.id.bf_search_men_dian);

        // 搜索门店
        menDianSearch();
    }
    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText(R.string.xun_dian_cha_xun);

        // 日期弹出选择
        mXun_dian_text_cha_xun_ri_qi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(XunDianChaXunActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        String string = year+"年"+(monthOfYear+1)+"月"+dayOfMonth;
                        // 开始时间
                        kstime = string;
                        Calendar c = Calendar.getInstance();
                        new DatePickerDialog(XunDianChaXunActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // TODO Auto-generated method stub
                                String string = year+"年"+(monthOfYear+1)+"月"+dayOfMonth;
                                // 开始时间
                                jstime = string;
                                mXun_dian_text_cha_xun_ri_qi_value.setText(kstime+"~"+jstime);

                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // 门店选择
        mText_bf_gong_si_ming_cheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog == null){
                    // 弹窗
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                    // 初始化组件
                    // 搜索
                    EditText bf_men_dian_ming_cheng = mViewD.findViewById(R.id.bf_men_dian_ming_cheng);

                    // 搜索门店处理
                    MenDianSearchChuli(bf_men_dian_ming_cheng);

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
    }

    /**
     * 搜索门店
     * @param editText
     */
    public void MenDianSearchChuli(EditText editText){
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
                mSearchString = String.valueOf(editable).trim();
                // 搜索门店
                menDianSearch();
            }
        });
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

        }else if(is == 2){
            mMengDianJsonData = string;
            setData(string,2);
            ShowMenDian(string,2);
        }

    }

    @Override
    public void dingWeiData() {

    }

    /**
     * 显示查询出来的门店
     * @param string
     * @param is 1 品牌 2门店
     */
    public void ShowMenDian(String string,int is){
        mBf_search_men_dian.removeAllViews();
        try {
            JSONArray jsonArray = new JSONArray(string);
            if(jsonArray.length() > 0){
                for(int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = new JSONObject();
                    TextView textView = new TextView(mContext);
                    if(is == 2){
                        String stringJson = jsonArray.get(i).toString();
                        JSONObject jsonObject1 = new JSONObject(stringJson);
                        textView = CreateTextvBf(1,stringJson,jsonObject1.getString("name")+" "+jsonObject1.getString("men_dian_hao")+" "+jsonObject1.getString("men_dian_ping_pai"));
                        mBf_search_men_dian.addView(textView);
                    }else if(is == 2){

                    }
                }
            }
        }catch (JSONException e){

        }
    }

    /**
     *

     */

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
                // 隐藏弹出
                dialog.hide();
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
            JSONObject jsonObject = new JSONObject(string);
            String idText = jsonObject.getString("id");
            String nameText = jsonObject.getString("name");
            String men_dian_ping_paiText = jsonObject.getString("men_dian_ping_pai");
            String men_dian_haoText = jsonObject.getString("men_dian_hao");
            likebt = nameText;
            pinPai = men_dian_ping_paiText;
            mText_bf_gong_si_ming_cheng_value.setText(nameText);
            // 编号
            mText_bf_gong_si_bian_hao_value.setText(men_dian_haoText);
            // 品牌
            mText_bf_gong_si_pin_pai_value.setText(men_dian_ping_paiText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        // 弹窗销毁
        if(dialog != null){
            dialog.dismiss();

        }
    }

}
