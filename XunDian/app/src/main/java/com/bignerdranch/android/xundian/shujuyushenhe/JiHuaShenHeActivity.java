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

    // 提交状态
    public String mTiJiaoZhuangTai = "";


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
        LoadingStringEdit("加载中...");
        // Token赋值
        setToken(mContext);
        // 请求未审核数据
        qingQiuWeiShenHeShuJu();
        // 请求已审核数据
//        qingQiuYiShenHeShuJu();
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
                    shiTuCreate();
                }
            }else if(msg.what==2){
                if(msg.obj.toString().equals("审核成功")){

                }
                tiShi(mContext,msg.obj.toString());
            }else if(msg.what==3){
                // 显示未审核数据
                if(!msg.obj.toString().equals("")){

                }
                // 关闭loading
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
        body.addFormDataPart("xxx","xxx");
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
        body.addFormDataPart("xxx","xxx");
        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mYiShenHeURL)
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


    public void ShenHeTiJiao(String string, int id,String BString){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);

        String BuTongString = "";
        if(!BString.equals("")){
            BuTongString = BString;
        }

        body.addFormDataPart("zhuang_tai",string);
        body.addFormDataPart("id",id+"");
        body.addFormDataPart("bu_tong_yi_yuan_yin",BuTongString);

        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mShenHeURL)
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
     * 创建视图 人
     */
    public void shiTuCreate(){
        mLinear_ji_hua_wei_shen_he.removeAllViews();
        try {
            JSONArray jsonArray = new JSONArray(mWeiSheHeData);

            if(jsonArray.length() > 0){
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                    LinearLayout linearLayout1 = LinearCreate(1);


                    LinearLayout linearLayout2 = LinearCreate(2);

                    LinearLayout linearLayout3 = LinearCreate(3);
                    // 创建显示隐藏
                    LinearCreateShowHide(linearLayout2,linearLayout3,32*Integer.valueOf(jsonObject.getString("num")));
                    // 创建视图
                    TextView textView1 = CreateTextView(1,jsonObject.getString("uname"));
                    // 创建图片
                    ImageView imageView3 = CreateImageView(2,new String[4]);

                    linearLayout2.addView(textView1);
                    linearLayout2.addView(imageView3);

                    // 创建周
                    CreateZhouData(linearLayout3,jsonObject);

                    linearLayout1.addView(linearLayout2);
                    linearLayout1.addView(linearLayout3);
                    mLinear_ji_hua_wei_shen_he.addView(linearLayout1);
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
     */
    public void CreateZhouData(LinearLayout linearLayout,JSONObject jsonObject){
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
                    // 创建图片
                    ImageView imageView = CreateImageView(1,mXunZheData);
                    // 创建周
                    TextView textView = CreateTextView(5,jsonObject1.getString("zhou")+" 周");

                    linearLayout5.addView(imageView);
                    linearLayout5.addView(textView);
                    linearLayout4.addView(linearLayout5);
                    linearLayout.addView(linearLayout4);

                    // 创建天数据
                    createDay(linearLayout,jsonObject1);
                }

                // 创建 同意 不同意
                LinearLayout linearLayout41 = LinearCreate(4);
                LinearLayout linearLayout8 = LinearCreate(8);
                TextView textView3 = CreateTextView(3,"同意");
                TextView textView4 = CreateTextView(4,"不同意");
                linearLayout8.addView(textView3);
                linearLayout8.addView(textView4);
                linearLayout41.addView(linearLayout8);
                linearLayout.addView(linearLayout41);
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
    public void createDay(LinearLayout linearLayout,JSONObject jsonObject){
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
                    // 创建图片
                    ImageView imageView = CreateImageView(1,mXunZheData);
                    // 创建周
                    TextView textView = CreateTextView(2,""+jsonObject1.getString("zhouDay"));

                    linearLayout5.addView(imageView);
                    linearLayout5.addView(textView);
                    linearLayout4.addView(linearLayout5);

                    // 创建天数据
                    createDayMiXi(linearLayout4,jsonObject1);
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
     */
    public void createDayMiXi(LinearLayout linearLayout,JSONObject jsonObject){
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
                    // 创建图片
                    ImageView imageView = CreateImageView(1,mXunZheData);
                    // 创建周
                    TextView textView = CreateTextView(2,
                            ""+jsonObject1.getString("kai_shi_time")
                            +"-"+jsonObject1.getString("jie_shu_time")
                            +" "+jsonObject1.getString("mendian_pin_pai")
                            +""+jsonObject1.getString("mendian_id")
                            +" "+jsonObject1.getString("mendian_name"));

                    linearLayout5.addView(imageView);
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
        }else if(is == 4){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParam.setMargins(10,0,0,0);
        }else if(is == 3){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
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
                    Log.i("巡店","同意");
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
                    Log.i("巡店","不同意");
                }
            });
        }else if(is == 5){
            textView.setPadding(5,5,5,5);
            textView.setTextSize(14);
            textView.setTextColor(getResources().getColor(R.color.heise));
        }

        textView.setText(string);

        return textView;
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
        }

        imageView.setLayoutParams(layoutParam);

        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ka_qing_ku_lian));

        if(is == 1){
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mLingShiData = string;
                    Log.i("巡店","选择框"+string[0]+"|"+string[1]+"|"+string[2]+"|"+string[3]);
                }
            });
        }else if(is == 2){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.bottom_hui_se));
        }

        return imageView;
    }


}
