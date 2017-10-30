package com.bignerdranch.android.xundian.shujuyushenhe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.comm.WeiboDialogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/10/20.
 */

public class JiHuaShenHeActivity extends ShuJuYuShenHeCommActivity {

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.JiHuaShenHeActivity";

    // 数据未审核
    private LinearLayout mLinear_ji_hua_wei_shen_he;
    public String mWeiSheHeData = "";


    // 数据已审核
    private LinearLayout mLinear_ji_hua_yi_shen_he;
    public String mYiSheHeData = "";

    // 1 未审核数据显示 2 已审核数据显示
    public int mBiaoShi = 1;

    // 开始时间
    private String mKaiSiTime;
    // 结束时间
    private String mJieshuTime;

    // 未审核数据请求URL
    private String mWeiShenHeURL = Config.URL+"/app/ji_hua_wei_shen_he_data";
    // 请求已审核数据URl
    private String mYiShenHeURL = Config.URL+"/app/qing_jia_yi_shen_he_data";

    // 审核数据提交
    private String mShenHeURL = Config.URL+"/app/qing_jia_shen_he";

    // 计划删除地址
    private String mJiHuaShanChuDiZhi = Config.URL+"/app/shan_chu_xun_dian_ji_hua";

    // 提交状态
    public String mTiJiaoZhuangTai = "";

    // 用户选择项
    public int mXuanZheXiang = 0;

    // 用户是否同意
    public String mIsTong = "同意";
    // 审核提交地址
    public String mShenHeTiJaiURL = Config.URL+"/app/ji_hua_shen_he";


    // 零时数据
    public String[] mLingShiData = new String[4];

    public static Intent newIntent(Context packageContext, int intIsId){
        Intent i = new Intent(packageContext,JiHuaShenHeActivity.class);
        i.putExtra(EXTRA,intIsId);
        return i;
    }




    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shu_ji_hua_shen_he);
        mContext = this;
        // 组件初始化
        ZhuJianInit();
        // 组件操作
        ZhuJianCaoZhuo();
        // 数据/值设置
        values();
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

    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mTitle_nei_ye = (TextView)findViewById(R.id.title_nei_ye);

        // 数据未审核
        mLinear_ji_hua_wei_shen_he = (LinearLayout)findViewById(R.id.linear_ji_hua_wei_shen_he);

        // 数据已审核
        mLinear_ji_hua_yi_shen_he = (LinearLayout)findViewById(R.id.linear_ji_hua_yi_shen_he);
    }
    /**
     * 值操作
     */
    public void values(){
        mLingShiData[0] = "";
        mLingShiData[1] = "";
        mLingShiData[2] = "";
        mLingShiData[3] = "";
        LoadingStringEdit("加载中...");
        // Token赋值
        setToken(mContext);
        // 请求未审核数据
        qingQiuWeiShenHeShuJu();

    }

    public int setXuanZheXiang(){
        if(!mLingShiData[0].equals("") && !mLingShiData[1].equals("") && !mLingShiData[2].equals("") && !mLingShiData[3].equals("")){
            return 3;
        }
        if(!mLingShiData[0].equals("") && !mLingShiData[1].equals("") && !mLingShiData[2].equals("")){
            return 2;
        }
        if(!mLingShiData[0].equals("") && !mLingShiData[1].equals("")){
            return 1;
        }
        return 0;
    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText(R.string.ji_hua_shen_he);
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
                // 显示未审核数据
                if(!msg.obj.toString().equals("")){
                    mWeiSheHeData = msg.obj.toString();
                    // 视图创建
                    mBiaoShi = 1;
                    shiTuCreate();
                }
                // 请求已审核数据
                qingQiuYiShenHeShuJu();
            }else if(msg.what==2){
                if(msg.obj.toString().equals("审核成功")){
                    qingQiuWeiShenHeShuJu();
                }
                tiShi(mContext,msg.obj.toString());
            }else if(msg.what==3){
                // 显示未审核数据
                if(!msg.obj.toString().equals("")){
                    mYiSheHeData  = msg.obj.toString();
                    // 视图创建
                    mBiaoShi = 2;
                    shiTuCreate();
                }
                // 关闭loading
            }else if(msg.what==4){
                if(msg.obj.toString().equals("删除成功")){
                    qingQiuWeiShenHeShuJu();
                }
                tiShi(mContext,msg.obj.toString());
            }
            WeiboDialogUtils.closeDialog(mWeiboDialog);
        }
    };

    /**
     * 请求未审核数据
     */
    public void qingQiuWeiShenHeShuJu(){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("is","1");
        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mWeiShenHeURL)
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
     * 请求已审核数据
     */
    public void qingQiuYiShenHeShuJu(){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("is","2");
        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mWeiShenHeURL)
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


    /**
     * 审核数据提交
     * @param BString 审核状态
     * @param BhString 驳回意见
     */
    public void ShenHeTiJiao(String BString,String BhString){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);

        body.addFormDataPart("zhou",mLingShiData[0]);
        body.addFormDataPart("uid",mLingShiData[1]);
        body.addFormDataPart("ri_qi",mLingShiData[2]);
        body.addFormDataPart("id",mLingShiData[3]);
        // 审核状态
        body.addFormDataPart("isTong",BString);
        // 不同意意见
        body.addFormDataPart("bo_hui_yi_jian",BhString);

        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mShenHeTiJaiURL)
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
     * 计划删除
     */
    public void jiHuaShanChu(){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);

        body.addFormDataPart("zhou",mLingShiData[0]);
        body.addFormDataPart("uid",mLingShiData[1]);
        body.addFormDataPart("ri_qi",mLingShiData[2]);
        body.addFormDataPart("id",mLingShiData[3]);

        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mJiHuaShanChuDiZhi)
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
                    mHandler.obtainMessage(4, response.body().string()).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mThread.start();
    }

    /**
     * 验证是否选择对应用户的两周里面的任意一周,选择了就返回true 否则false
     * @param jsonArray
     * @return
     */
    public Boolean yanZhengIsXuanZhe(JSONArray jsonArray){
        if(jsonArray.length() > 0){
            for (int i = 0;i<jsonArray.length();i++){
                try {
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());

                    String yongHuZhou = jsonObject.getString("zhou");
                    yongHuZhou += jsonObject.getString("uid");

                    if(mBiaoShi == 1) {
                        // 用户选择人-周
                        String XuanZhe = "";
                        if (!mLingShiData[0].equals("") && !mLingShiData[1].equals("")) {
                            XuanZhe = mLingShiData[0];
                            XuanZhe += mLingShiData[1];
                        }
                        if (yongHuZhou.equals(XuanZhe)) {
                            return true;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        return false;
    }

    /**
     * 创建视图 人
     */
    public void shiTuCreate(){
        // 已审核数据
        if(mBiaoShi == 2){
            mLinear_ji_hua_yi_shen_he.removeAllViews();
        }else if(mBiaoShi == 1){
            mLinear_ji_hua_wei_shen_he.removeAllViews();
        }
        int qunXuan = 0;
        try {
            JSONArray jsonArray = new JSONArray();
            if(mBiaoShi == 1){
                jsonArray = new JSONArray(mWeiSheHeData);
            }
            // 已审核数据
            if(mBiaoShi == 2){
                jsonArray = new JSONArray(mYiSheHeData);
            }

            if(jsonArray.length() > 0){
                for (int i = 0;i<jsonArray.length();i++){
                    // 用户基本信息
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                    LinearLayout linearLayout1 = LinearCreate(1);


                    // 用户下周信息
                    JSONArray jsonArray1 = new JSONArray(jsonObject.getString("jihua").toString());
                    JSONObject jsonObject1 = new JSONObject(jsonArray1.get(0).toString());

                    LinearLayout linearLayout2 = LinearCreate(2);
                    // 创建隐藏对应用户周计划
                    LinearLayout linearLayout3 = LinearCreate(3);

                    // 循环用户周
                    if(mBiaoShi == 1) {
                        if (yanZhengIsXuanZhe(jsonArray1)) {
                            // 创建显示对应用户周计划
                            linearLayout3 = LinearCreate(31);
                            if (mXuanZheXiang == 1) {
                                qunXuan = 1;
                            }
                        }
                    }
                    // 创建显示隐藏
                    LinearCreateShowHide(linearLayout2,linearLayout3,33*Integer.valueOf(jsonObject.getString("num")));
                    // 创建视图
                    TextView textView1 = CreateTextView(1,jsonObject.getString("uname"));

                    // 创建图片
                    ImageView imageView3 = CreateImageView(2, new String[4]);
                    linearLayout2.addView(textView1);
                    linearLayout2.addView(imageView3);

                    // 创建周
                    CreateZhouData(linearLayout3,jsonObject,qunXuan);

                    linearLayout1.addView(linearLayout2);
                    linearLayout1.addView(linearLayout3);

                    if(mBiaoShi == 2){
                        mLinear_ji_hua_yi_shen_he.addView(linearLayout1);
                    }else if(mBiaoShi == 1){
                        mLinear_ji_hua_wei_shen_he.addView(linearLayout1);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // mWeiSheHeData
    }

    /**
     * 创建周显示数据
     * @param linearLayout
     * @param jsonObject
     * @param qunXuan 1 全选
     */
    public void CreateZhouData(LinearLayout linearLayout,JSONObject jsonObject,int qunXuan){
        try {
            JSONArray jsonArray = new JSONArray(jsonObject.getString("jihua").toString());

            if(jsonArray.length() > 0){
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());

                    LinearLayout linearLayout4 = LinearCreate(4);
                    LinearLayout linearLayout5 = LinearCreate(5);

                    String[] mXunZheData = new String[4];
                    mXunZheData[0] = jsonObject1.getString("zhou");
                    mXunZheData[1] = jsonObject1.getString("uid");
                    mXunZheData[2] = "";
                    mXunZheData[3] = "";

                    ImageView imageView = new ImageView(mContext);
                    // 未审核
                    if(mBiaoShi == 1){
                        // 创建图片
                        imageView = CreateImageView(1, mXunZheData);
                        // 是否选中
                        if(qunXuan == 1 && mXuanZheXiang == 1 && mLingShiData[0].equals(jsonObject1.getString("zhou"))){
                            imageView = CreateImageView(6,mXunZheData);
                        }
                    }

                    // 创建周
                    TextView textView = CreateTextView(5,jsonObject1.getString("zhou")+" 周");


                    if(mBiaoShi == 1) {
                        linearLayout5.addView(imageView);
                    }
                    linearLayout5.addView(textView);
                    linearLayout4.addView(linearLayout5);
                    linearLayout.addView(linearLayout4);

                    // 创建天数据
                    createDay(linearLayout,jsonObject1,qunXuan);
                }

                if(mBiaoShi == 1){
                    // 创建 同意 不同意
                    LinearLayout linearLayout41 = LinearCreate(4);
                    LinearLayout linearLayout8 = LinearCreate(8);
                    TextView textView6 = CreateTextView(6,"清除选择");
                    TextView textView3 = CreateTextView(3,"同意");
                    TextView textView4 = CreateTextView(4,"不同意");
                    TextView textView7 = CreateTextView(7,"删除");

                    linearLayout8.addView(textView6);
                    linearLayout8.addView(textView3);
                    linearLayout8.addView(textView4);
                    linearLayout8.addView(textView7);

                    linearLayout41.addView(linearLayout8);
                    linearLayout.addView(linearLayout41);
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建周day
     * @param linearLayout
     * @param jsonObject
     */
    public void createDay(LinearLayout linearLayout,JSONObject jsonObject,int qunXuan){
        try {
            JSONArray jsonArray = new JSONArray(jsonObject.getString("zhouJihua").toString());

            if(jsonArray.length() > 0){
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());

                    LinearLayout linearLayout4 = LinearCreate(4);
                    LinearLayout linearLayout5 = LinearCreate(5);

                    String[] mXunZheData = new String[4];
                    mXunZheData[0] = jsonObject1.getString("zhou");
                    mXunZheData[1] = jsonObject1.getString("uid");
                    mXunZheData[2] = jsonObject1.getString("ri_qi");
                    mXunZheData[3] = "";

                    ImageView imageView = new ImageView(mContext);
                    // 创建图片
                    if(mBiaoShi == 1) {
                        imageView = CreateImageView(1,mXunZheData);
                        if (qunXuan == 1 && mXuanZheXiang == 1 && mLingShiData[0].equals(jsonObject1.getString("zhou"))) {
                            imageView = CreateImageView(6, mXunZheData);
                            qunXuan = 1;
                        } else {
                            qunXuan = 0;
                        }
                    }

                    // 用户选择
                    if(!mLingShiData[0].equals("") && !mLingShiData[1].equals("") && !mLingShiData[2].equals("") && mXuanZheXiang == 2){
                        if(mLingShiData[0].equals(mXunZheData[0]) && mLingShiData[1].equals(mXunZheData[1]) && mLingShiData[2].equals(mXunZheData[2])){
                            imageView = CreateImageView(6,mXunZheData);
                            qunXuan = 1;
                        }else{
                            qunXuan = 0;
                        }

                    }

                    // 创建周

                    TextView textView = CreateTextView(2,""+jsonObject1.getString("zhouDay"));
                    if(mBiaoShi == 2){
                        textView = CreateTextView(2," "+jsonObject1.getString("zhouDay"));
                    }
                    if(mBiaoShi == 1) {
                        linearLayout5.addView(imageView);
                    }
                    linearLayout5.addView(textView);
                    linearLayout4.addView(linearLayout5);

                    // 创建天数据
                    createDayMiXi(linearLayout4,jsonObject1,qunXuan);
                    linearLayout.addView(linearLayout4);

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建周day明细
     * @param linearLayout
     * @param jsonObject
     * @param qunXuan 1 全选
     */
    public void createDayMiXi(LinearLayout linearLayout,JSONObject jsonObject,int qunXuan){
        try {
            JSONArray jsonArray = new JSONArray(jsonObject.getString("day").toString());

            if(jsonArray.length() > 0){
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());

                    LinearLayout linearLayout5 = LinearCreate(5);

                    String[] mXunZheData = new String[4];
                    mXunZheData[0] = jsonObject1.getString("zhou");
                    mXunZheData[1] = jsonObject1.getString("uid");
                    mXunZheData[2] = jsonObject1.getString("ri_qi");
                    mXunZheData[3] = jsonObject1.getString("id");

                    ImageView imageView = new ImageView(mContext);
                    // 创建图片
                    if(mBiaoShi == 1){
                        imageView = CreateImageView(1,mXunZheData);
                        if(!mLingShiData[3].equals("") || qunXuan == 1){
                            if((mLingShiData[3].equals(jsonObject1.getString("id"))  || qunXuan == 1) && mLingShiData[0].equals(jsonObject1.getString("zhou"))){
                                imageView= CreateImageView(6,mXunZheData);
                            }
                        }
                    }


                    // 创建周
                    TextView textView = CreateTextView(2,
                            ""+jsonObject1.getString("kai_shi_time")
                            +"-"+jsonObject1.getString("jie_shu_time")
                            +" "+jsonObject1.getString("mendian_pin_pai")
                            +""+jsonObject1.getString("mendian_id")
                            +" "+jsonObject1.getString("mendian_name"));

                    if(mBiaoShi == 2){
                        textView = CreateTextView(2,
                                "  "+jsonObject1.getString("kai_shi_time")
                                        +"-"+jsonObject1.getString("jie_shu_time")
                                        +" "+jsonObject1.getString("mendian_pin_pai")
                                        +""+jsonObject1.getString("mendian_id")
                                        +" "+jsonObject1.getString("mendian_name"));
                    }

                    if(mBiaoShi == 1) {
                        linearLayout5.addView(imageView);
                    }
                    linearLayout5.addView(textView);
                    linearLayout.addView(linearLayout5);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建LinearLayout
     * @param is
     * @return
     */
    public LinearLayout LinearCreate(int is){
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        if(is == 1){
            layoutParam.setMargins(0,0,20,0);
        }else if(is == 2){
            layoutParam.setMargins(0,0,10,0);
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,120);
        }else if(is == 4){
            layoutParam.setMargins(0,0,0,5);
        }else if(is == 5){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,90);
        }

        linearLayout.setLayoutParams(layoutParam);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        if(is == 1){
            linearLayout.setBackground(getResources().getDrawable(R.drawable.bottom_border_huise));
        }else if(is == 2){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        }else if(is == 3){
            linearLayout.setBackground(getResources().getDrawable(R.color.colorAccent));
            linearLayout.setVisibility(View.GONE);
        }else if(is == 31){
            linearLayout.setBackground(getResources().getDrawable(R.color.colorAccent));
        }else if(is == 4){
            linearLayout.setBackground(getResources().getDrawable(R.drawable.bottom_border));
        }else if(is == 5){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
            linearLayout.setPadding(0,0,10,0);
        }else if(is == 8){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.RIGHT);
            linearLayout.setPadding(0,10,10,10);
        }

        return linearLayout;
    }

    /**
     * 设置点击显示隐藏事件
     * @param linearLayout 点击linear
     * @param linearLayout1 显示隐藏对应信息
     * @param height 展开高度
     * @return
     */
    public void LinearCreateShowHide(LinearLayout linearLayout,final LinearLayout linearLayout1,final int height){
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linearLayout1.getVisibility() == View.GONE) {
                    float mDensity = getResources().getDisplayMetrics().density;
                    int he = (int) (mDensity*height);
                    animateOpen(linearLayout1,he);
                    animationIvOpen();
                } else {
//                    mLingShiData = new String[4];
                    animateClose(linearLayout1);
                    animationIvClose();
                }
            }
        });
    }

    /**
     * 创建TextView
     * @param is 创建类型
     * @param string 显示内容
     */
    public TextView CreateTextView(int is,final String string){
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,90);

        if(is == 1){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
        }else if(is == 2){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        }else if(is == 4){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParam.setMargins(20,0,0,0);
        }else if(is == 3){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParam.setMargins(20,0,0,0);
        }else if(is == 6){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        }else if(is == 7){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParam.setMargins(20,0,0,0);
        }


        textView.setLayoutParams(layoutParam);

        if(is == 1){
            textView.setPadding(5,5,5,5);
            textView.setTextColor(getResources().getColor(R.color.heise));
        }else if(is == 2){
            textView.setPadding(5,5,5,5);
            textView.setTextSize(14);
        }else if(is == 3){
            textView.setPadding(10,2,10,2);
            textView.setTextColor(getResources().getColor(R.color.colorAccent));
            textView.setTextSize(14);
            textView.setBackground(getResources().getDrawable(R.color.zhuti));
            // 同意点击
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isTiJiao()){
                        // 审核提交
                        ShenHeTiJiao("同意","");
                    }

                }
            });

        }else if(is == 4){
            textView.setPadding(10,2,10,2);
            textView.setTextColor(getResources().getColor(R.color.colorAccent));
            textView.setTextSize(14);
            textView.setBackground(getResources().getDrawable(R.color.huise));
            // 不同意点击
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 获取布局文件
                    LayoutInflater inflater = (LayoutInflater) mContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View mViewD = inflater.inflate(R.layout.alert_shu_ji_hu_shen_he_bu_tong_yi, null);
                    // 弹窗
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                    // 初始化组件
                    // 输入内容
                    final EditText edittext_qing_jia_bu_tong_yi_ti_jiao = mViewD.findViewById(R.id.edittext_qing_jia_bu_tong_yi_ti_jiao);
                    // 提交按钮
                    Button button_qing_jia_bu_tong_yi_ti_jiao = mViewD.findViewById(R.id.button_qing_jia_bu_tong_yi_ti_jiao);
                    // 设置View
                    alertBuilder.setView(mViewD);

                    // 显示
                    alertBuilder.create();
                    dialog = alertBuilder.show();

                    // 提交事件
                    button_qing_jia_bu_tong_yi_ti_jiao.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isTiJiao()) {
                                String str = "";
                                str = edittext_qing_jia_bu_tong_yi_ti_jiao.getText().toString();
                                ShenHeTiJiao("不同意", str);

                                // 弹出销毁
                                dialog.dismiss();
                            }
                        }
                    });
                }
            });

        }else if(is == 5){
            textView.setPadding(5,5,5,5);
            textView.setTextSize(14);
            textView.setTextColor(getResources().getColor(R.color.heise));
        }else if(is == 6){
            textView.setPadding(10,2,10,2);
            textView.setTextColor(getResources().getColor(R.color.colorAccent));
            textView.setTextSize(14);
            textView.setBackground(getResources().getDrawable(R.color.zhuti));
            // 清除选择点击
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mLingShiData[2] = "";
                    mLingShiData[3] = "";
                    mXuanZheXiang = 0;
                    // 刷新视图
                    shiTuShuaXing();
                }
            });
        }else if(is == 7){
            textView.setPadding(10,2,10,2);
            textView.setTextColor(getResources().getColor(R.color.colorAccent));
            textView.setTextSize(14);
            textView.setBackground(getResources().getDrawable(R.color.hongse));
            // 同意点击
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isTiJiao()){
                        // 删除计划
                        jiHuaShanChu();
                    }
                }
            });

        }

        textView.setText(string);

        return textView;
    }

    /**
     * 验证是否可以提交
     * @return
     */
    public boolean isTiJiao(){
        if(!mLingShiData[0].equals("") || !mLingShiData[1].equals("") || !mLingShiData[2].equals("") || !mLingShiData[3].equals("")){
            return true;
        }
        tiShi(mContext,"请选择记录");
        return false;
    }

    /**
     * 创建ImageView
     * @param is 创建类型
     * @param string 提示文字
     * @return
     */
    public ImageView CreateImageView(int is, final String[] string){
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        if(is == 1){
            layoutParam = new LinearLayout.LayoutParams(58,58);
            layoutParam.setMargins(10,0,0,0);
        }else if(is == 5){
            layoutParam = new LinearLayout.LayoutParams(78,78);
            layoutParam.setMargins(10,0,0,0);
        }else if(is == 6){
            layoutParam = new LinearLayout.LayoutParams(58,58);
            layoutParam.setMargins(10,0,0,0);
        }

        imageView.setLayoutParams(layoutParam);

        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ka_qing_ku_lian));

        if(is == 1){
            if(mBiaoShi == 1){
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mLingShiData = string;
                        // 用户选择 1 全选周 2 全选日期  3选择天
                        mXuanZheXiang = setXuanZheXiang();
                        // 刷新视图
                        shiTuShuaXing();
                    }
                });
            }

        }else if(is == 2){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.bottom_hui_se));
        }else if(is == 6){
            if(mBiaoShi == 1) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
            }
        }

        return imageView;
    }

    /**
     * 刷新显示
     */
    public void shiTuShuaXing(){
        mBiaoShi = 1;
        shiTuCreate();
        mBiaoShi = 2;
        shiTuCreate();
    }
}
