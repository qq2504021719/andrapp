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
import android.view.ViewGroup;
import android.view.WindowManager;
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

    // 拜访图片审核父linear
    public LinearLayout mBai_fang_shen_he_linear;

    // 图片JsonArray对象
    public JSONArray mBaiFangPhoneFan;

    // 需要审查的linear
    public LinearLayout mShanChuLinear;

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
        // 父linear
        mBai_fang_shen_he_linear = (LinearLayout)findViewById(R.id.bai_fang_shen_he_linear);
    }

    /**
     * 组件操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText(R.string.xun_dian_shen_he);
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
        // 数据显示
        ShiTuXianShi();
    }

    /**
     * 图片数据解析
     */
    public void tuPianDataJieXi(){
        try {
            JSONObject jsonObject = new JSONObject(mBaiFangShenHeData);
            id = jsonObject.getString("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 视图显示 mBai_fang_shen_he_linear
     */
    public void ShiTuXianShi(){
        try {
            JSONObject jsonObject = new JSONObject(mBaiFangShenHeData);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("BaiFangPhoneFan"));
            mBaiFangPhoneFan = jsonArray;
            if(jsonArray.length() > 0){
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
                    // 图片父
                    LinearLayout linearLayout = CreateLinearBFSh(1);
                    mBai_fang_shen_he_linear.addView(linearLayout);

                    // 图片
                    ImageView imageView = CreateImageViewBFSH(1,R.mipmap.ic_launcher);
                    Picasso.with(mContext).load(Config.URL+"/"+jsonObject1.getString("path")).into(imageView);
                    linearLayout.addView(imageView);

                    // 抽查 反馈 删除 按钮
                    LinearLayout linearLayout2 = CreateLinearBFSh(2);
                    linearLayout.addView(linearLayout2);
                    // 抽查linear
                    LinearLayout linearLayout3 = CreateLinearBFSh(3);
                    linearLayout.addView(linearLayout3);
                    // 抽查
                    TextView textView = CreateTextViewBFSH(1,"抽查",jsonObject1,jsonObject1.getString("id"),linearLayout3,linearLayout2,"",linearLayout);
                    linearLayout2.addView(textView);
                    // 抽查 合格 不合格
                    ImageView imageViewB = CreateImageViewBFSH(2,R.drawable.ka_qing_ku_lian);
                    ImageView imageViewH = CreateImageViewBFSH(2,R.drawable.ka_qing_ku_lian);
                    // 不合格选中
                    if(jsonObject1.getString("phone_is_hg").equals("1")){
                        imageViewB = CreateImageViewBFSH(2,R.drawable.ka_qing_xiao_lian);
                    }else{
                        // 合格选中
                        imageViewH = CreateImageViewBFSH(2,R.drawable.ka_qing_xiao_lian);
                    }
                    // 不合格
                    LinearLayout linearLayout4 = CreateLinearBFShHG(4,i,"不合格",imageViewH,imageViewB);
                    TextView textView1 = CreateTextViewBFSH(2,"不合格",jsonObject1,jsonObject1.getString("id"),linearLayout3,linearLayout2,"不合格",linearLayout);
                    linearLayout4.addView(imageViewB);
                    linearLayout4.addView(textView1);
                    linearLayout3.addView(linearLayout4);

                    // 合格
                    LinearLayout linearLayout41 = CreateLinearBFShHG(4,i,"合格",imageViewH,imageViewB);
                    TextView textView11 = CreateTextViewBFSH(2,"合格",jsonObject1,jsonObject1.getString("id"),linearLayout3,linearLayout2,"合格",linearLayout);
                    linearLayout41.addView(imageViewH);
                    linearLayout41.addView(textView11);
                    linearLayout3.addView(linearLayout41);

                    // 抽查保存
                    LinearLayout linearLayout5 = CreateLinearBFSh(5);
                    TextView textView2 = CreateTextViewBFSH(1,"保存",jsonObject1,jsonObject1.getString("id"),linearLayout3,linearLayout2,"抽查",linearLayout);
                    linearLayout5.addView(textView2);
                    linearLayout3.addView(linearLayout5);

                    // 反馈linear
                    LinearLayout linearLayout6 = CreateLinearBFSh(6);
                    EditText editText1 = CreateEditTextBFSH();
                    TextView textView3 = CreateTextViewBFSH(1,"保存",jsonObject1,jsonObject1.getString("id"),linearLayout6,linearLayout2,"反馈",linearLayout);
                    LinearLayout linearLayout7 = CreateLinearBFSh(7);
                    LinearLayout linearLayout8 = CreateLinearBFSh(8);
                    linearLayout7.addView(editText1);
                    linearLayout8.addView(textView3);
                    linearLayout6.addView(linearLayout7);
                    linearLayout6.addView(linearLayout8);
                    linearLayout.addView(linearLayout6);
                    // 反馈
                    TextView textViewF = CreateTextViewBFSH(1,"反馈",jsonObject1,jsonObject1.getString("id"),linearLayout6,linearLayout2,"反馈",linearLayout);
                    linearLayout2.addView(textViewF);

                    // 删除
                    TextView textViewS = CreateTextViewBFSH(1,"删除",jsonObject1,jsonObject1.getString("id"),linearLayout3,linearLayout2,"删除",linearLayout);
                    linearLayout2.addView(textViewS);


                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
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
//                    finish();
//                    TuPianXianShiMoShi();
                }
                tiShi(mContext,msg.obj.toString());
            }else if(msg.what == 2){
                if(msg.obj.toString().equals("无")){
                    tiShi(mContext,"无权限");
                }else{
                    // 拜访查询图片审核数据提交
                    TuBianShenHeYanZhengTiJiao();
                }

            }
        }
    };

    /**
     * 请求验证
     */
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
                    mHandler.obtainMessage(2, response.body().string()).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mThread.start();
    }

    /**
     * 拜访查询图片审核验证数据提交
     */
    public void TuBianShenHeYanZhengTiJiao(){
        if(ZhaoZhuoMS.equals("删除")){
            // 隐藏布局
            if(!mShanChuLinear.equals("")){
                mShanChuLinear.setVisibility(View.GONE);
            }

            shenHeShuJuTiJiao();
        }else if(ZhaoZhuoMS.equals("抽查")){
            if(!isHeGE.equals("")){
                if(isHeGE.equals("合格")){
                    isHeGE = "";
                }
                if(isHeGE.equals("不合格")){
                    isHeGE = "1";
                }
                shenHeShuJuTiJiao();
            }

        }else if(ZhaoZhuoMS.equals("反馈")){
            if(!FanKui.equals("")){
                shenHeShuJuTiJiao();
            }
        }
    }

    /**
     * 拜访查询图片审核数据提交
     */
    public void shenHeShuJuTiJiao(){
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

    /**
     * 创建TextView
     * @param is 创建类型
     * @param string 显示内容
     * @param stringJson 审核json字符串
     * @param ids 审核id
     * @param linearLayout 显示linear
     * @param HiddenLinear 隐藏linear
     * @param tag 标签 (抽查/反馈/删除)
     */
    public TextView CreateTextViewBFSH(int is,final String string,final JSONObject stringJson,final String ids,final LinearLayout linearLayout,final LinearLayout HiddenLinear,final String tag,final LinearLayout linearLayoutF){

        TextView textView = new TextView(mContext);

        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        if(is == 1){
            layoutParam = new LinearLayout.LayoutParams(130,60);
            layoutParam.setMargins(0,0,10,0);
        }else if(is == 2){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
        }

        textView.setLayoutParams(layoutParam);

        if(is == 1){

            textView.setBackground(getResources().getDrawable(R.drawable.button));
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getResources().getColor(R.color.colorAccent));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    id = ids;

                    // 抽查
                    if(string.equals("抽查")){
                        isHeGE = "";
                    }

                    // 反馈
                    if(string.equals("反馈")){
                        FanKui = "";
                    }

                    if(string.equals("抽查") || string.equals("反馈")){
                        linearLayout.setVisibility(View.VISIBLE);
                        HiddenLinear.setVisibility(View.GONE);
                    }
                    if(string.equals("保存")){
                        linearLayout.setVisibility(View.GONE);
                        HiddenLinear.setVisibility(View.VISIBLE);
                    }

                    // 抽查保存
                    if(string.equals("保存") && tag.equals("抽查")){
                        ZhaoZhuoMS = "抽查";
                        mQuanXianName = "拜访查询-审核-图片抽查";
                        QunXianYanZheng();
                    }
                    // 反馈保存
                    if(string.equals("保存") && tag.equals("反馈")){
                        ZhaoZhuoMS = "反馈";
                        mQuanXianName = "拜访查询-审核-图片反馈";
                        QunXianYanZheng();
                    }

                    // 删除保存
                    if(string.equals("删除")){
                        mQuanXianName = "拜访查询-审核-图片删除";
                        ZhaoZhuoMS = "删除";
                        isShanChu = "是";
                        mShanChuLinear = linearLayoutF;
                        QunXianYanZheng();
                    }
                }
            });
        }else if(is == 2){
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getResources().getColor(R.color.heise));
        }

        textView.setText(string);
        return textView;
    }

    /**
     * 创建linearLayout
     * @param is 创建类型
     */
    public LinearLayout CreateLinearBFSh(int is){
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        if(is == 3){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,150);
        }else if(is == 2){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,150);
        }else if(is == 4){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,1);
        }else if(is == 5){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,1);
        }else if(is == 6){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,150);
        }else if(is == 7){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
        }else if(is == 8){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
        }

        linearLayout.setLayoutParams(layoutParam);

        linearLayout.setOrientation(LinearLayout.VERTICAL);

        if(is == 1){
            linearLayout.setPadding(10,10,10,10);
            linearLayout.setBackground(getResources().getDrawable(R.drawable.bottom_border));
        }else if(is == 2){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.RIGHT);
            linearLayout.setPadding(0,10,0,0);
        }else if(is == 3){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setVisibility(View.GONE);
        }else if(is == 4){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);
        }else if(is == 5){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.RIGHT);
        }else if(is == 6){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setVisibility(View.GONE);
            linearLayout.setPadding(0,10,0,0);
        }else if(is == 8){
            linearLayout.setGravity(Gravity.RIGHT);
        }

        return linearLayout;
    }

    /**
     * 合格不合格linear创建
     *
     */
    public LinearLayout CreateLinearBFShHG(int is,final int key,final String isHe,final ImageView imageViewH,final ImageView imageViewB){
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        if(is == 4){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1);
        }

        linearLayout.setLayoutParams(layoutParam);

        linearLayout.setOrientation(LinearLayout.VERTICAL);

        if(is == 4){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isHeGE = "";
                    if(isHe.equals("合格")){
                        isHeGE = "合格";
                        imageViewH.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
                        imageViewB.setBackground(getResources().getDrawable(R.drawable.ka_qing_ku_lian));
                    }else if(isHe.equals("不合格")){
                        isHeGE = "不合格";
                        Log.i("巡店",isHe+"点击");
                        imageViewH.setBackground(getResources().getDrawable(R.drawable.ka_qing_ku_lian));
                        imageViewB.setBackground(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
                    }
                }
            });
        }

        return linearLayout;
    }


    /**
     * 创建ImageView
     * @param is 创建类型
     * @return
     */
    public ImageView CreateImageViewBFSH(int is,int idDr){
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        if(is == 2){
            layoutParam = new LinearLayout.LayoutParams(60,60);
        }

        imageView.setLayoutParams(layoutParam);

        if(is == 2){
            imageView.setBackground(getResources().getDrawable(idDr));
        }

        return imageView;
    }

    /**
     * 创建EditText
     * @return
     */
    public EditText CreateEditTextBFSH(){
        EditText editText = new EditText(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(600,LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParam.setMargins(10,0,0,0);
        editText.setLayoutParams(layoutParam);

        editText.setHint("反馈内容");

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                FanKui = String.valueOf(editable);
            }
        });

        editText.setTextColor(getResources().getColor(R.color.huise6));
        return editText;
    }
}
