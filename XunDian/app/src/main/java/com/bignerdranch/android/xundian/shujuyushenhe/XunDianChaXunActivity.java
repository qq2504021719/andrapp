package com.bignerdranch.android.xundian.shujuyushenhe;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.WeiboDialogUtils;
import com.bignerdranch.android.xundian.kaoqing.KaoQingCommonActivity;
import com.bignerdranch.android.xundian.xundianguanli.XunDianActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/10/31.
 */

public class XunDianChaXunActivity extends KaoQingCommonActivity implements KaoQingCommonActivity.Callbacks{

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.XunDianChaXunController";

    private AlertDialog alertDialog1;

    // 日期
    TextView mXun_dian_text_cha_xun_ri_qi;
    TextView mXun_dian_text_cha_xun_ri_qi_value;
    // 结束时间

    // 门店名称
    private TextView mText_bf_gong_si_ming_cheng;
    private TextView mText_bf_gong_si_ming_cheng_value;
    // 公司alert View
    private View mViewD;
    // 公司alert
    private LinearLayout mBf_search_men_dian;
    public Dialog dialog = null;

    // 门店品牌
    private TextView mText_bf_gong_si_pin_pai;
    private TextView mText_bf_gong_si_pin_pai_value;
    // 公司alert View
    private View mViewPPD;
    // 公司alert
    private LinearLayout mBf_search_men_dian_pp;
    public Dialog dialogpp = null;

    // 门店编号
    private TextView mText_bf_gong_si_bian_hao;
    private TextView mText_bf_gong_si_bian_hao_value;

    // 门店类型
    private TextView mCha_xun_men_dian_lei_xing;
    private TextView mCha_xun_men_dian_lei_xing_value;
    private String mMenDianLeiXingUrl = Config.URL+"/app/ChaXunMenDianLeiXing";
    private String mMenDianLeiXingJsonData = "";
    private String mleiXingSearchName = "";
    // 门店类型alert view
    private View mMenDianView;
    // 门店类型alert
    private LinearLayout mBf_search_men_dian_lei_xing;
    // 门店类型 dialog
    public Dialog dialogMenDianLeiXing = null;

    // 巡店项目名称
    private EditText mXun_dian_xiang_mu_ming_chen_search;
    // 照片显示
    private ImageView mCha_xun_zhao_pian_xian_shi;
    // 参数显示
    private ImageView mCha_xun_can_shu_xian_shi;
    private int mIsChanXianShi = 1;

    // 提交人
    private EditText mTi_jiao_ren_value;

    // 点击查询
    TextView mXun_dian_cha_xun_cha_xun;

    // 巡店查询数据显示
    private LinearLayout mXun_dian_cha_xun_nei_rong;

    // 巡店数据
    private String mXunDianChaXunData = "";

    // 巡店数据查询URL
    private String mXunDianShuJuChaXunURL = Config.URL+"/app/XunDianShuJuData";

    // 公司名称
    private String likebt = "";
    // 提交人
    private String tiJiaoRen = "";
    // 开始时间
    private String kstime = "";
    // 结束时间
    private String jstime = "";
    // 品牌
    private String pinPai = "";
    // 门店类型
    private String canShuLeiXing = "";
    // 项目名称
    private String XiangMuMingCheng = "";

    // image弹窗显示
    public Dialog mImagedialog;

    public static Intent newIntent(Context packageContext, String like){
        Intent i = new Intent(packageContext,XunDianChaXunActivity.class);
        i.putExtra(EXTRA,like);
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

    @Override
    public void onResume(){
        likebt = getIntent().getStringExtra(EXTRA);
        if(!likebt.equals("") && !likebt.equals("0")){
            XunDianShuJuChaXun();
        }
        // 请求巡店数据
//        XunDianShuJuChaXun();
        super.onResume();
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
        mText_bf_gong_si_pin_pai = (TextView)findViewById(R.id.text_bf_gong_si_pin_pai);
        mText_bf_gong_si_pin_pai_value = (TextView)findViewById(R.id.text_bf_gong_si_pin_pai_value);

        // 公司编号
        mText_bf_gong_si_bian_hao = (TextView)findViewById(R.id.text_bf_gong_si_bian_hao);
        mText_bf_gong_si_bian_hao_value = (TextView)findViewById(R.id.text_bf_gong_si_bian_hao_value);

        // 门店类型
        mCha_xun_men_dian_lei_xing = (TextView)findViewById(R.id.cha_xun_men_dian_lei_xing);
        mCha_xun_men_dian_lei_xing_value = (TextView)findViewById(R.id.cha_xun_men_dian_lei_xing_value);

        // 巡店项目名称
        mXun_dian_xiang_mu_ming_chen_search = (EditText)findViewById(R.id.xun_dian_xiang_mu_ming_chen_search);
        //照片显示
        mCha_xun_zhao_pian_xian_shi = (ImageView)findViewById(R.id.cha_xun_zhao_pian_xian_shi);
        // 参数显示
        mCha_xun_can_shu_xian_shi = (ImageView)findViewById(R.id.cha_xun_can_shu_xian_shi);

        // 提交人
        mTi_jiao_ren_value = (EditText)findViewById(R.id.ti_jiao_ren_value);

        // 点击查询
        mXun_dian_cha_xun_cha_xun = (TextView)findViewById(R.id.cha_xun_dian_ji_cha_xun);


        // 巡店查询数据显示
        mXun_dian_cha_xun_nei_rong = (LinearLayout)findViewById(R.id.xun_dian_cha_xun_nei_rong);
    }
    /**
     * 值操作
     */
    public void values(){
        // Token赋值
        setToken(mContext);

        // 门店搜索模式
        moshi = "1";

        // 公司弹出View
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
        // 品牌搜索
        pingPaiSouShuo();


        // 内容清空
        mXun_dian_cha_xun_nei_rong.removeAllViews();

        // 查询门店类型
        ChaXunMenDianLeiXing();
        LayoutInflater inflaterLeiXing = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 门店类型alert view
        mMenDianView = inflaterLeiXing.inflate(R.layout.alert_kao_bai_fang_guan_li_search, null);
        // 公司 alert 弹出
        mBf_search_men_dian_lei_xing = mMenDianView.findViewById(R.id.bf_search_men_dian);

        // 默认选中参数
        ZhaoCanXianShi();

    }

    /**
     * 照片显示,参数显示
     */
    public void ZhaoCanXianShi(){
        if(mIsChanXianShi == 1){
            mCha_xun_can_shu_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
            mCha_xun_zhao_pian_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_ku_lian));
        }else if (mIsChanXianShi == 2){
            mCha_xun_can_shu_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_ku_lian));
            mCha_xun_zhao_pian_xian_shi.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
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
                mXunDianChaXunData = msg.obj.toString();
                if(mXunDianChaXunData == ""){
                    tiShi(mContext,"没有数据");
                }
                showXianShi();
            }else if(msg.what == 2){
                mMenDianLeiXingJsonData = msg.obj.toString();
                // 显示门店类型数据
                ShowMenDian(mMenDianLeiXingJsonData,3);
            }
        }
    };

    // 巡店数据查询
    public void XunDianShuJuChaXun(){
        LoadingStringEdit("加载中");
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("likebt",likebt);
        body.addFormDataPart("tiJiaoRen",tiJiaoRen);
        body.addFormDataPart("kstime",kstime);
        body.addFormDataPart("jstime",jstime);
        body.addFormDataPart("pinPai",pinPai);
        body.addFormDataPart("canShuLeiXing",canShuLeiXing);
        body.addFormDataPart("XiangMuMingCheng",XiangMuMingCheng);

        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mXunDianShuJuChaXunURL)
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

    // 查询门店类型
    public void ChaXunMenDianLeiXing(){
//        Log.i("巡店","门店类型查询");
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("name",mleiXingSearchName);

        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mMenDianLeiXingUrl)
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
     * 显示视图 CreateLinear
     */
    public void showXianShi(){
        // 清空内容
        mXun_dian_cha_xun_nei_rong.removeAllViews();
        try {
            JSONArray jsonArray = new JSONArray(mXunDianChaXunData);
            if(jsonArray.length() > 0){
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                    LinearLayout linearLayout1 = CreateLinear(1);

                    // 第一个表格
                    LinearLayout linearLayout2 = CreateLinear(2);
                    TextView textView21 = CreateTextViewXun(1,jsonObject.getString("shijian"),"");
                    TextView textView22 = CreateTextViewXun(1,jsonObject.getString("created_user_name"),"");
                    linearLayout2.addView(textView21);
                    linearLayout2.addView(textView22);
                    linearLayout1.addView(linearLayout2);

                    // 第二个表格
                    LinearLayout linearLayout3 = CreateLinear(3);
                    TextView textView31 = CreateTextViewXun(1,jsonObject.getString("mendian_men_dian_ping_pai")+" "+jsonObject.getString("mendian_men_dian_hao"),"");
                    TextView textView32 = CreateTextViewXun(1,jsonObject.getString("mendian_name"),"");
                    TextView textView33 = CreateTextViewXun(1,jsonObject.getString("men_dian_lei_xing"),"");
                    linearLayout3.addView(textView31);
                    linearLayout3.addView(textView32);
                    linearLayout3.addView(textView33);
                    linearLayout1.addView(linearLayout3);

                    // 第三个表格
                    LinearLayout linearLayout33 = CreateLinear(3);
                    TextView textView331 = CreateTextViewXun(1,jsonObject.getString("canshu_name"),"");

                    TextView textView332 = new TextView(mContext);
                    ImageView imageView332 = new ImageView(mContext);
                    // 参数显示
                    if(mIsChanXianShi == 1){
                        textView332 = CreateTextViewXun(1,jsonObject.getString("value"),"");
                    }else if(mIsChanXianShi == 2){
                        // 照片显示
                        if(!jsonObject.getString("path").equals("")){
                            imageView332 = CreateImageViewXunDian(1,0,Config.URL+"/"+jsonObject.getString("path"));
                            Picasso.with(mContext).load(Config.URL+"/"+jsonObject.getString("path")).into(imageView332);
                        }
                    }


                    // 审核 图标外层
                    LinearLayout linearLayout6 = CreateLinear(6);

                    // 审核
                    LinearLayout linearLayout4 = CreateLinear(4);

                    // 图标linear
                    LinearLayout linearLayout5 = CreateLinear(5);

                    // 图片审核
                    ImageView tuImageView = new ImageView(mContext);
                    if(jsonObject.getString("tu_xiugai_hon").equals("1")){
                        tuImageView = CreateImageViewXunDian(2,R.drawable.hong_gan,"");
                    }
                    // 内容审核
                    ImageView neiImageView = new ImageView(mContext);
                    if(jsonObject.getString("nei_xiugai_huang").equals("1")){
                        neiImageView = CreateImageViewXunDian(2,R.drawable.huang_gan,"");
                    }


                    TextView textView333 = CreateTextViewXun(2,"审核",jsonArray.get(i).toString());
                    // 图片审核
                    if(jsonObject.getString("tu_xiugai_hon").equals("1")){
                        linearLayout5.addView(tuImageView);
                    }
                    // 内容审核
                    if(jsonObject.getString("nei_xiugai_huang").equals("1")){
                        linearLayout5.addView(neiImageView);

                    }
                    linearLayout4.addView(linearLayout5);
                    linearLayout4.addView(textView333);
                    linearLayout6.addView(linearLayout4);

                    linearLayout33.addView(textView331);

                    if(mIsChanXianShi == 1){
                        linearLayout33.addView(textView332);
                    }else if(mIsChanXianShi == 2){
                        if(!jsonObject.getString("path").equals("")){
                            linearLayout33.addView(imageView332);
                        }
                    }

                    linearLayout33.addView(linearLayout6);
                    linearLayout1.addView(linearLayout33);

                    mXun_dian_cha_xun_nei_rong.addView(linearLayout1);
//                    ImageView
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 关闭loading
        WeiboDialogUtils.closeDialog(mWeiboDialog);
    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText(R.string.xun_dian_cha_xun);

        // 日期默认显示今天日期
        String riQiString  = getDangQianTime(1);
        mXun_dian_text_cha_xun_ri_qi_value.setText(riQiString+"~"+riQiString);

        // 日期弹出选择
        mXun_dian_text_cha_xun_ri_qi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(XunDianChaXunActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        String string = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                        // 开始时间
                        kstime = string;
                        Calendar c = Calendar.getInstance();
                        new DatePickerDialog(XunDianChaXunActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // TODO Auto-generated method stub
                                String string = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                                // 开始时间
                                jstime = string;
                                mXun_dian_text_cha_xun_ri_qi_value.setText(kstime+"~"+jstime);

                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // 门店品牌选择
        mText_bf_gong_si_pin_pai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        mText_bf_gong_si_ming_cheng.setOnClickListener(new View.OnClickListener() {
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

        // 门店号选择
        mText_bf_gong_si_bian_hao.setOnClickListener(new View.OnClickListener() {
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

        // 门店类型搜索选择
        mCha_xun_men_dian_lei_xing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialogMenDianLeiXing == null){
                    // 弹窗
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                    // 初始化组件
                    // 搜索
                    EditText bf_men_dian_ming_cheng = mMenDianView.findViewById(R.id.bf_men_dian_ming_cheng);
                    bf_men_dian_ming_cheng.setHint("请输入门店类型名称");

                    // 搜索处理
                    MenDianSearchChuli(bf_men_dian_ming_cheng,2);

                    // 设置View
                    alertBuilder.setView(mMenDianView);

                    // 显示
                    alertBuilder.create();

                    dialogMenDianLeiXing = alertBuilder.show();
                }else{
                    dialogMenDianLeiXing.show();
                }
            }
        });

        // 巡店项目名称
        mXun_dian_xiang_mu_ming_chen_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                XiangMuMingCheng = String.valueOf(editable).trim();
            }
        });

        // 参数显示
        mCha_xun_can_shu_xian_shi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsChanXianShi = 1;
                ZhaoCanXianShi();
                // 显示数据刷新
                showXianShi();
            }
        });
        // 照片显示
        mCha_xun_zhao_pian_xian_shi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsChanXianShi = 2;
                ZhaoCanXianShi();
                // 显示数据刷新
                showXianShi();
            }
        });

        // 提交人
        mTi_jiao_ren_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tiJiaoRen = String.valueOf(editable).trim();
            }
        });

        // 巡店查询
        mXun_dian_cha_xun_cha_xun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!likebt.equals("")){
                    // 请求巡店数据
                    XunDianShuJuChaXun();
                }else{
                    tiShi(mContext,"请选择门店");
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
                    mleiXingSearchName = String.valueOf(editable).trim();
                    // 搜索门店类型
                    ChaXunMenDianLeiXing();
                }

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
            ShowMenDian(string,1);

        }else if(is == 2){
            mMengDianJsonData = string;
            setData(string,2);
            ShowMenDian(mMengDianJsonData,2);
        }

    }

    @Override
    public void dingWeiData() {

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
                        textView = CreateTextvBf(1,stringJson,jsonObject1.getString("name")+" "+jsonObject1.getString("men_dian_hao")+" "+jsonObject1.getString("men_dian_ping_pai"));
                        mBf_search_men_dian.addView(textView);

                    }
                }
            }else if(is == 3){
                // 门店类型搜索view清空
                mBf_search_men_dian_lei_xing.removeAllViews();
                // 显示门店类型数据
                JSONArray jsonArray1 = new JSONArray(string);
                if(jsonArray1.length() > 0){
                    JSONArray jsonArray2 = new JSONArray(jsonArray1.get(0).toString());
                    if(jsonArray2.length() > 0){
                        for (int i3 = 0;i3<jsonArray2.length();i3++){
                            TextView textView1 = new TextView(mContext);
                            textView1 = CreateTextvBf(3,jsonArray2.get(i3)+"",jsonArray2.get(i3)+"");
                            mBf_search_men_dian_lei_xing.addView(textView1);
                        }
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
                // 门店选择
                if(is == 1){
                    // 隐藏弹出
                    dialog.hide();
                }
                // 品牌
                if(is == 2){
                    dialogpp.hide();
                }
                // 门店类型选择
                if(is == 3){
                    // 隐藏弹出
                    dialogMenDianLeiXing.hide();
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
                // 品牌
                mText_bf_gong_si_pin_pai_value.setText(nameText);
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
                likebt = nameText;
                pinPai = men_dian_ping_paiText;
                // 显示
                mText_bf_gong_si_ming_cheng_value.setText(nameText);
                // 编号
                mText_bf_gong_si_bian_hao_value.setText(men_dian_haoText);
                // 品牌
                mText_bf_gong_si_pin_pai_value.setText(men_dian_ping_paiText);
            }else if(is == 3){
                // 门店类型
                canShuLeiXing = string;
//                mCha_xun_men_dian_lei_xing_value
                mCha_xun_men_dian_lei_xing_value.setText(string);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建linearLayout
     * @param is 创建类型
     */
    public LinearLayout CreateLinear(int is){
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        if(is == 2){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1);
        }else if(is == 3){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1);
        }else if(is == 4){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        }else if(is == 5){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
        }else if(is == 6){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        }

        linearLayout.setLayoutParams(layoutParam);

        linearLayout.setOrientation(LinearLayout.VERTICAL);

        if(is == 1){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        }else if(is == 2){
            linearLayout.setBackground(getResources().getDrawable(R.drawable.border_left_buttom_right_huise));
            linearLayout.setGravity(Gravity.LEFT);
            linearLayout.setPadding(10,30,0,30);
        }else if(is == 3){
            linearLayout.setBackground(getResources().getDrawable(R.drawable.border_bottom_right_huise));
            linearLayout.setGravity(Gravity.LEFT);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
            linearLayout.setPadding(10,10,10,10);
        }else if(is == 4){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.RIGHT);
        }else if(is == 5){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        }else if(is == 6){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        }

        return linearLayout;
    }

    /**
     * 创建TextView
     * @param is 创建类型
     * @param string 显示内容
     */
    public TextView CreateTextViewXun(int is,String string,final String stringJson){
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        if(is == 2){
            layoutParam = new LinearLayout.LayoutParams(100,60);
        }

        textView.setLayoutParams(layoutParam);

        if(is == 1){
            textView.setPadding(5,5,5,5);
        }else if(is == 2){
            textView.setBackground(getResources().getDrawable(R.drawable.button));
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getResources().getColor(R.color.colorAccent));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = XunDianChaXunShenHeActivity.newIntent(XunDianChaXunActivity.this,stringJson);
                    startActivity(i);
                    finish();
                }
            });
        }
        textView.setText(string);
        return textView;
    }

    /**
     * 创建ImageView
     * @param is 创建类型
     * @return
     */
    public ImageView CreateImageViewXunDian(int is,int drawable,final String file){
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        if(is == 1){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,100);
        }else if(is == 2){
            layoutParam = new LinearLayout.LayoutParams(40,40);
        }
        imageView.setLayoutParams(layoutParam);

        if(is == 1){
            imageView.setPadding(0,10,0,10);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 弹窗
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                    // 获取布局文件
                    LayoutInflater inflater = (LayoutInflater) mContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View viewD = inflater.inflate(R.layout.alert_image, null);

                    // 初始化组件
                    ImageView image = viewD.findViewById(R.id.alert_iamge);

                    WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

                    int height = wm.getDefaultDisplay().getHeight();

                    int width = (int)Config.TuPianBiLi*height;

                    Picasso.with(mContext).load(file).resize(width,height).into(image);
                    // 设置View
                    alertBuilder.setView(viewD);

                    // 显示
                    alertBuilder.create();
                    mImagedialog = alertBuilder.show();

                }
            });
        }else if(is == 2){
            imageView.setPadding(5,5,5,5);
            imageView.setBackground(getResources().getDrawable(drawable));
        }
        return imageView;
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
        // imageDialog销毁
        if(mImagedialog != null){
            mImagedialog.dismiss();
        }
        if(dialogMenDianLeiXing != null){
            dialogMenDianLeiXing.dismiss();

        }
    }
}
