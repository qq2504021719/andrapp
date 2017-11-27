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
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/6.
 */

public class BaiFangChaXunActivity  extends KaoQingCommonActivity implements KaoQingCommonActivity.Callbacks{

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.BaiFangChaXunActivity";

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


    // 提交人
    private EditText mTi_jiao_ren_value;

    // 点击查询
    TextView mXun_dian_cha_xun_cha_xun;

    // 巡店查询数据显示
    private LinearLayout mXun_dian_cha_xun_nei_rong;

    // 巡店数据
    private String mXunDianChaXunData = "";

    // 拜访数据查询URL
    private String mXunDianShuJuChaXunURL = Config.URL+"/app/BaiFangShuJuChaXun";

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
    // 审核状态
    private String ShenHeZhuang = "";

    // 内容审核
    // 拜访id
    public String bfid = "";
    // 拜访反馈
    public String BFFankui = "";
    // 用户审核反馈
    public String Fankui = "";
    // alert View
    private View mbfViewD;
    // title
    public TextView mTitle_TextView;
    // editText
    private EditText mEdittext_Fan_Kui_Shu_Ru;
    // Button
    public Button mButton_TextView;

    public Dialog bfdialog = null;

    // image弹窗显示
    public Dialog mImagedialog;

    // 弹出审核标识
    public int mTanChuangBiaoShi = 0;
    public String mstringJson = "";

    // 图片左右滑动
    public ArrayList<Phone> mPhoneZuoYouHuaDong = new ArrayList<>();

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    final int RIGHT = 0;
    final int LEFT = 1;
    private GestureDetector gestureDetector;

    public View mAlertImageViewD;


    public static Intent newIntent(Context packageContext, int intIsId){
        Intent i = new Intent(packageContext,BaiFangChaXunActivity.class);
        i.putExtra(EXTRA,intIsId);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shu_bai_fang_cha_xun);
        mContext = this;
        // 组件初始化
        ZhuJianInit();
        // 数据/值设置
        values();
        // 组件操作
        ZhuJianCaoZhuo();

    }



    @Override
    public void onResume(){
        // 请求拜访数据
        XunDianShuJuChaXun();
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
        moshi = "3";

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

        // 内容审核
        // 获取布局文件
        mbfViewD = inflater.inflate(R.layout.alert_shu_ji_hu_shen_he_bu_tong_yi, null);
        // 内容审核布局文件
        mTitle_TextView = mbfViewD.findViewById(R.id.bu_hui_title);
        mTitle_TextView.setText("驳回原因");
        // 内容审核输入框
        mEdittext_Fan_Kui_Shu_Ru = mbfViewD.findViewById(R.id.edittext_qing_jia_bu_tong_yi_ti_jiao);
        mEdittext_Fan_Kui_Shu_Ru.setHint("请输入驳回原因");
        // Button
        mButton_TextView = mbfViewD.findViewById(R.id.button_qing_jia_bu_tong_yi_ti_jiao);
        mButton_TextView.setText("驳回提交");


        // 弹窗显示图片
        // 获取布局文件
        LayoutInflater inflateImage = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAlertImageViewD = inflateImage.inflate(R.layout.alert_image, null);


        setGestureListener();
    }

    public float mPosX;
    public float mPosY;
    public float mCurPosX;
    public float mCurPosY;

    /**
     * 设置上下滑动作监听器
     * @author jczmdeveloper
     */
    private void setGestureListener(){
        mAlertImageViewD.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mPosX = event.getX();
                        mPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurPosX = event.getX();
                        mCurPosY = event.getY();

                        break;
                    case MotionEvent.ACTION_UP:
                        if (mCurPosY - mPosY > 0
                                && (Math.abs(mCurPosY - mPosY) > 25)) {
                            //向下滑動
                            tiShi(mContext,"向下");

                        } else if (mCurPosY - mPosY < 0
                                && (Math.abs(mCurPosY - mPosY) > 25)) {
                            //向上滑动
                            tiShi(mContext,"向上");
                        }

                        break;
                }
                return true;
            }

        });
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
                if(msg.obj.toString().equals("审核成功")){
                    // 请求拜访数据0
                    XunDianShuJuChaXun();
                }
                tiShi(mContext,msg.obj.toString());
                bfdialog.hide();
            }else if(msg.what == 3){
                if(msg.obj.toString().equals("无")){
                    tiShi(mContext,"无权限");
                }else{
                    if(mTanChuangBiaoShi == 0){
                        if(!mI.equals("")){
                            startActivity(mI);
                        }
                    }else if(mTanChuangBiaoShi == 1){
                        TanChuangShenHeDialog(mstringJson);
                    }

                }

            }
        }
    };

    public void QunXianYanZheng(){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("name",mQuanXianName);
        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(Config.URL+"/app/UserDataQuanXian")
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
                    mHandler.obtainMessage(3, response.body().string()).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mThread.start();
    }

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
        body.addFormDataPart("ShenHeZhuang",ShenHeZhuang);

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
                    LinearLayout linearLayout3 = CreateLinear(2);
                    TextView textView31 = CreateTextViewXun(1,jsonObject.getString("shijian"),"");
                    TextView textView32 = CreateTextViewXun(1,jsonObject.getString("uname"),"");
                    TextView textView33 = CreateTextViewXun(1,jsonObject.getString("mname"),"");
                    linearLayout3.addView(textView31);
                    linearLayout3.addView(textView32);
                    linearLayout3.addView(textView33);
                    linearLayout1.addView(linearLayout3);

                    // 图标linear
                    LinearLayout linearLayout8t = CreateLinear(8);
                    // 第二个表格
                    LinearLayout linearLayout33 = CreateLinear(3);
                    LinearLayout linearLayout7 = CreateLinear(7);
                    // 获取没有删除的图片

                    // 图片张数
                    TextView textView331 = new TextView(mContext);

                    String[] Phone1 = getBuWeiKongDeTuPian(jsonObject);
                    ImageView imageView332 = CreateImageViewXunDian(1,0,Config.URL+"/vendor/courier/img/TR.ico");
                    if(Phone1.length > 0){
                        imageView332 = CreateImageViewXunDian(1,0,Config.URL+"/"+Phone1[0]);
                        if(!Phone1[0].equals("")){
                            Picasso.with(mContext).load(Config.URL+"/"+Phone1[0]).into(imageView332);
                        }


                        textView331 = CreateTextViewXun(1,Phone1[2]+"张图片","");
                    }
                    // tu_xiugai_hon
                    // 图片审核
                    ImageView tuImageView = new ImageView(mContext);
                    if(jsonObject.getString("tu_xiugai_hon").equals("1")){
                        tuImageView = CreateImageViewXunDian(2,R.drawable.hong_gan,"");
                        linearLayout8t.addView(tuImageView);
                    }

                    TextView textView333 = CreateTextViewXun(2,"审核",jsonArray.get(i).toString());
                    // 图片张数
                    linearLayout7.addView(textView331);
                    // 图片是否审核图标
                    linearLayout7.addView(linearLayout8t);
                    // 审核按钮
                    linearLayout7.addView(textView333);
                    linearLayout33.addView(imageView332);
                    linearLayout33.addView(linearLayout7);
                    linearLayout1.addView(linearLayout33);

                    mXun_dian_cha_xun_nei_rong.addView(linearLayout1);

                    // 内容表头
                    LinearLayout NlinearLayout1 = CreateLinear(1);
                    LinearLayout NlinearLayout5 = CreateLinear(5);
                    TextView NtextView31 = CreateTextViewXun(3,"内容","");
                    NlinearLayout5.addView(NtextView31);
                    NlinearLayout1.addView(NlinearLayout5);
                    mXun_dian_cha_xun_nei_rong.addView(NlinearLayout1);


                    // 图标linear
                    LinearLayout linearLayout8n = CreateLinear(8);
                    // 内容表格
                    LinearLayout NGlinearLayout1 = CreateLinear(1);
                    LinearLayout NGlinearLayout2 = CreateLinear(2);
                    LinearLayout NGlinearLayout6 = CreateLinear(6);
                    TextView NGtextView31 = CreateTextViewXun(1,jsonObject.getString("bai_fang_nei_rong"),"");
                    // 内容审核
                    ImageView neiImageView = new ImageView(mContext);

                    if(jsonObject.getString("nei_xiugai_huang").equals("1")){
                        neiImageView = CreateImageViewXunDian(2,R.drawable.huang_gan,"");
                        linearLayout8n.addView(neiImageView);
                    }

                    TextView NGtextView333 = CreateTextViewXun(4,"审核",jsonArray.get(i).toString());

                    NGlinearLayout6.addView(linearLayout8n);
                    NGlinearLayout6.addView(NGtextView333);

                    NGlinearLayout2.addView(NGtextView31);
                    NGlinearLayout2.addView(NGlinearLayout6);
                    NGlinearLayout1.addView(NGlinearLayout2);
                    mXun_dian_cha_xun_nei_rong.addView(NGlinearLayout1);

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 关闭loading
        WeiboDialogUtils.closeDialog(mWeiboDialog);
    }

    /**
     * 获取json对象中不为空的图片
     * 创建图片滑动list mPhoneZuoYouHuaDong
     * @param jsonObject
     */
    public String[] getBuWeiKongDeTuPian(JSONObject jsonObject){

        try {
            JSONArray jsonArray = new JSONArray(jsonObject.getString("BaiFangPhoneFan"));

            if(jsonArray.length() > 0){
                JSONObject jsonObject1 = new JSONObject(jsonArray.get(0).toString());
                String string[] = new String[3];
                String path = jsonObject1.getString("path");
                String phone_is_hg = jsonObject1.getString("phone_is_hg");
                string[0] = path;
                string[1] = phone_is_hg;
                string[2] = jsonArray.length()+"";

                Phone phone = new Phone();
                phone.setTitle(jsonObject.getString("mname"));
                phone.setPath(path);
                // 第二个表格
                LinearLayout linearLayout7 = CreateLinear(7);
                // 获取没有删除的图片

                // 图片张数
                TextView textView331 = new TextView(mContext);
                ImageView imageView332 = CreateImageViewXunDian(1,0,Config.URL+"/vendor/courier/img/TR.ico");
                imageView332 = CreateImageViewXunDian(1,0,Config.URL+"/"+path);
                if(!path.equals("")){
                    Picasso.with(mContext).load(Config.URL+"/"+path).into(imageView332);
                }
                // 图片审核
                ImageView tuImageView = new ImageView(mContext);
                if(phone_is_hg.equals("1")){
                    tuImageView = CreateImageViewXunDian(2,R.drawable.hong_gan,"");
                }
                // 审核按钮
                linearLayout7.addView(imageView332);
                linearLayout7.addView(tuImageView);
                // 存储
                phone.setLinearLayout(linearLayout7);

                mPhoneZuoYouHuaDong.add(phone);

                return string;
            }else{
                return new String[0];
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText(R.string.bai_fang_cha_xun);

        // 日期默认显示今天日期
        String riQiString  = getDangQianTime(1);
        mXun_dian_text_cha_xun_ri_qi_value.setText(riQiString+"~"+riQiString);

        // 日期弹出选择
        mXun_dian_text_cha_xun_ri_qi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(BaiFangChaXunActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        String string = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                        // 开始时间
                        kstime = string;
                        Calendar c = Calendar.getInstance();
                        new DatePickerDialog(BaiFangChaXunActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                    MenDianSearchChuli(bf_men_dian_ming_cheng,2);

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
                // 请求巡店数据
                XunDianShuJuChaXun();

            }
        });

        // 审核内容获取
        mEdittext_Fan_Kui_Shu_Ru.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Fankui = String.valueOf(editable);
            }
        });
        // 审核内容提交
        mButton_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 审核提交
                BaiFangTupianShenHe();
//                bfdialog.hide();
            }
        });
    }

    /**
     * 搜索门店
     * @param editText
     * @param is 1 门店搜索 2 品牌
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
                }else if(is == 2){
                    mPinPaiSearch = String.valueOf(editable).trim();
                    // 搜索品牌
                    pingPaiSouShuo();
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
        }else if(is == 5){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        }else if(is == 6){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        }else if(is == 7){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        }else if(is == 8){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
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
        }else if(is == 5){
            linearLayout.setBackground(getResources().getDrawable(R.drawable.border_bottom_left_right_huise));
        }else if(is == 6){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setPadding(0,0,10,0);
            linearLayout.setGravity(Gravity.RIGHT);
        }else if(is == 7){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setPadding(0,0,10,0);
            linearLayout.setGravity(Gravity.RIGHT);
        }else if(is == 8){
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
        }else if(is == 3){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        }else if(is == 4){
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
                    mTanChuangBiaoShi = 0;
                    mI = BaiFangChaXunTuPianActivity.newIntent(BaiFangChaXunActivity.this,stringJson);
                    mQuanXianName = "拜访查询-拜访查询图片审核";
                    // 权限验证
                    QunXianYanZheng();
                }
            });
        }else if(is == 3){
            textView.setGravity(Gravity.LEFT);
            textView.setPadding(10,10,10,10);
        }else if(is == 4){
            textView.setBackground(getResources().getDrawable(R.drawable.button));
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getResources().getColor(R.color.colorAccent));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTanChuangBiaoShi = 1;
                    mQuanXianName = "拜访查询-拜访查询内容审核";
                    // 权限验证
                    QunXianYanZheng();
                    mstringJson = stringJson;

                }
            });
        }
        textView.setText(string);
        return textView;
    }

    /**
     * 弹窗审核
     * @param stringJson
     */
    public void TanChuangShenHeDialog(String stringJson){
        try {
            JSONObject jsonObject = new JSONObject(stringJson);
            // id
            bfid = jsonObject.getString("id");
            // 驳回内容
            BFFankui = jsonObject.getString("nei_rong_fan_kui");
            BFFankui = BFFankui=="null"?"":BFFankui;
            // 设置默认显示
            if(!BFFankui.equals("")){
                mEdittext_Fan_Kui_Shu_Ru.setText(BFFankui);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(bfdialog == null){
            // 弹窗
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
            // 设置View
            alertBuilder.setView(mbfViewD);
            // 显示
            alertBuilder.create();

            bfdialog = alertBuilder.show();
        }else{
            bfdialog.show();
        }
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
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,200);
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




                    ImageViewAlert(mAlertImageViewD,mPhoneZuoYouHuaDong.get(0));

                    // 设置View
                    alertBuilder.setView(mAlertImageViewD);

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


    /**
     * ImageView弹窗显示
     */
    public void ImageViewAlert(View view,Phone phone){
        // 标题
        TextView textView = view.findViewById(R.id.alert_image_title);
        textView.setText(phone.getTitle());
        // 初始化组件
        ImageView image = view.findViewById(R.id.alert_iamge_image);
        // linear
        LinearLayout linearLayout = view.findViewById(R.id.alert_image_linear);
        linearLayout.addView(phone.getLinearLayout());


        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        int height = wm.getDefaultDisplay().getHeight();

        int width = (int)Config.TuPianBiLi*height;

        Picasso.with(mContext).load(phone.getPath()).resize(width,height).into(image);
    }

    // 内容审核提交
    public void BaiFangTupianShenHe(){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("isT","1");
        body.addFormDataPart("isHeGE","");
        body.addFormDataPart("FanKui",Fankui);
        body.addFormDataPart("id",bfid);
        body.addFormDataPart("isShanChu","");
        body.addFormDataPart("ZhaoZhuoMS","内容审核");

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
                    mHandler.obtainMessage(2, response.body().string()).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mThread.start();
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
        // 弹窗销毁
        if(bfdialog != null){
            bfdialog.dismiss();
        }
        // imageDialog销毁
        if(mImagedialog != null){
            mImagedialog.dismiss();
        }
    }
}
