package com.bignerdranch.android.xundian.xundianguanli;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.xundian.LoginActivity;
import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.ChaoShi;
import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.comm.NeiYeCommActivity;
import com.bignerdranch.android.xundian.comm.PictureUtils;
import com.bignerdranch.android.xundian.comm.WeiboDialogUtils;
import com.bignerdranch.android.xundian.comm.XunDianCanShu;
import com.bignerdranch.android.xundian.model.ChaoShiModel;
import com.bignerdranch.android.xundian.model.LoginModel;
import com.bignerdranch.android.xundian.model.XunDianModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.id.list;
import static android.os.Build.TIME;

/**
 * Created by Administrator on 2017/9/12.
 */

public class XunDianActivity extends NeiYeCommActivity {

    private static final String TAG = "XunDianActivity";

    // 请求代码常量 相机拍照
    private static final int REQUEST_PHOTO = 3;

    // 巡店定位信息,店铺id
    public JSONObject mXunDian;

    public String mXunDianJSONData;

    public XunDianCanShu mXunDianCanShu;

    // 巡店参数list
    public JSONArray mXunDianJson;

    // 巡店数据对象存储
    public HashMap<Integer,XunDianCanShu> mXunDianCanShus;

    // 存储显示序号
    public HashMap<Integer,Integer> mShowXuHao = new HashMap<>();


    // LinearLayout 公共样式
    public static LinearLayout.LayoutParams mLayoutParam;

    // 弹出选择器
    private AlertDialog alertDialog1;

    // LinearLayout
    public LinearLayout mXun_dian_bounce_linearlayout;
    // 保存
    private Button mXun_dian_bc_button;


    // 图片点击id
    public static int mTuPianDianJi;

    // this
    // mXunDianModel 登录模型
    private static XunDianModel mXunDianModel;
    private Login mLogin;

    // 门店id
    private int mMenDianID;

    // 参数请求url
    private String mCanShuURL = Config.URL+"/app/menDian/";

    // 参数提交url
    private String mCanShuTJURL = Config.URL+"/app/xun_dian_ti_jiao";

    // 图片提交url
    private String mTuPanTJURL = Config.URL+"/app/xun_dian_ti_jiao/tuPian";

    // 参数必填数量
    private static int mCanShuNum = 0;
    // 图片数量
    private static int mCanShuNums = 0;
    // 图片已提交数量
    private static int mCanShuYiTiJiao = 0;

    public ChaoShiModel mChaoShiModel;
    // 是否超时 1 没有 0 超时
    private int mIsChaoShi = 1;
    // 倒计时显示
    private TextView mTextview_dao_ji_shi;
    // 倒计时时间
    private int mDaoJiShi = 0;
    // 倒计时实时时间
    private int mDaoJiShi1 = 0;
    // 超时时间
    private int mDaoJiShi2 = 0;
    // 定时任务
    private int mTIME = 1000;
    Handler handler = new Handler();

    // 是否提交
    private int mIsTiJiao = 0;

    // 最大下标
    public int mZuiDaXiaBiao = 0;

    // 当前时间
    public String mDangQianTime;

    // 点击保存后提示语
    public String mDianJiBaoCun = "您已点击保存,无法修改,请再次点击保存";

    // 超出范围多少米
    String ChaoFanWei = "0.1";

    /**
     * 设置最大下标
     * @param num
     */
    public void setZuiDaXiaBiao(int num){
        if(num > mZuiDaXiaBiao){
            mZuiDaXiaBiao = num;
        }
    }


    public static Intent newIntent(Context packageContext, String string){
        Intent i = new Intent(packageContext,XunDianActivity.class);
        i.putExtra(TAG,string);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_xun_dian);

        mContext = this;


        // 连接数据库
        mXunDianModel = XunDianModel.get(mContext);
        mChaoShiModel = ChaoShiModel.get(mContext);

        // 组件初始化
        ZhuJianInit();

        // 值接收处理
        values();



        // 组件操作
        ZhuJianCaoZhuo();

        // 请求数据
        canShuQingQiu();

        LoadingStringEdit("数据加载中...");
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
                // 参数请求回调
                String string = msg.obj.toString();
                if(string != null){
                    try {
                        JSONObject jsonObject = new JSONObject(string);
                        mXunDianJSONData = jsonObject.getString("can_shu");
                        if(!mXunDianJSONData.isEmpty()){
                            // 添加组件
                            addView(mXunDianJSONData);
                            // 倒计时显示
                            if(mDaoJiShi == 0 && mDaoJiShi1 == 0){
                                if(!jsonObject.getString("tian_xie_shi").isEmpty()){
                                    mDaoJiShi = Integer.valueOf(jsonObject.getString("tian_xie_shi"))*60;
                                    mDaoJiShi1 = mDaoJiShi;
                                    Log.i("巡店","倒计时请求 mDaoJiShi:"+mDaoJiShi+"| mDaoJiShi1:"+mDaoJiShi1+"|mDaoJiShi2:"+mDaoJiShi2);


                                }
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }else if(msg.what == 2){
                // 参数上传回调
                if(msg.obj.toString().equals("上传成功")){
//                    tiShi(mContext,msg.obj.toString());
                    // 删除数据
                    try {
                        deleteXunDianData(mXunDian.getString("mMenDianId"));
                        tiShi(mContext,"上传成功");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if(msg.what == 3){
                String string = msg.obj.toString();
                // 图片上传回调
                if(msg.obj != null && string != null){
                    try {
                        JSONObject jsonObject = new JSONObject(string);
                    if(jsonObject.getString("id") != null && jsonObject.getString("path") != null){
                        // 参数提交回调
                        mCanShuYiTiJiao++;
                        if(mXunDianCanShus.get(Integer.valueOf(jsonObject.getString("id"))) != null){
                            mXunDianCanShus.get(Integer.valueOf(jsonObject.getString("id"))).setServerPhotoPath(jsonObject.getString("path"));
//                            tiShi(mContext,"图片上传 : "+mCanShuYiTiJiao+"/"+mCanShuNums);
                        }
                        // 提交数据
                        Log.i("巡店",mCanShuNums+"提交参数"+mCanShuYiTiJiao);
                        if(mCanShuNums == mCanShuYiTiJiao){
                            Log.i("巡店","提交参数");
                            TiJiao(mXunDianCanShus);
                        }

                    }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    };


    /**
     * 定时任务调用
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                handler.postDelayed(this, mTIME);
                setDaoJiShi();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 倒计时调用
     */
    public void diaoYongDaoJiShi(){
        // 本地服务器存储倒计时计算
//        benDitimeDaoJiShi();
        handler.postDelayed(runnable, mTIME); //每隔1s执行
    }

    /**
     * 设置倒计时显示
     *
     */
    public void setDaoJiShi(){
        String XunKaiShiTime = mXunDianCanShu.getXunKaiShiTime();

        String date1 = dataTime(getDangQianTime());
        String date2 = dataTime(XunKaiShiTime);

        // 计算退出时间
        int time = Integer.valueOf(date1)-Integer.valueOf(date2);

        if(mXunDianCanShus != null  &&  mXunDianCanShus.size() > 0){
            String str = "";
            if(mDaoJiShi > time){
                int mIsChaoShiData = mDaoJiShi-time;
                mDaoJiShi1 = mIsChaoShiData;
                str = ""+mDaoJiShi1+"秒";
            }else{
                mIsChaoShi = 0;
                int mIsChaoShiData = time-mDaoJiShi;
                mDaoJiShi2 = mIsChaoShiData;
                str = "已超时"+mDaoJiShi2+"秒";
            }
            mTextview_dao_ji_shi.setText(str);
        }

    }

    /**
     * 本地存储巡店开始时间倒计时计算,并赋值
     */
    public void benDitimeDaoJiShi(){
        if(mXunDianCanShu.getXunKaiShiTime() != null  &&  mXunDianCanShu.getXunKaiShiTime()!=""){
            String XunKaiShiTime = mXunDianCanShu.getXunKaiShiTime();

            String date1 = dataTime(getDangQianTime());
            String date2 = dataTime(XunKaiShiTime);

            // 计算时间退出时间
            int time = Integer.valueOf(date1)-Integer.valueOf(date2);


            // 赋值,剩余时间大于没有计算时间,剩余时间-没有计算时间并且大于0 超时时间为0
            if(mDaoJiShi1 > time && (mDaoJiShi1-time) > 0 && mDaoJiShi2 == 0){
                mDaoJiShi1 = mDaoJiShi-time;
            }else if(time > 1){
                mDaoJiShi2 = time;
            }
            Log.i("巡店","mDaoJiShi:"+mDaoJiShi+"|mDaoJiShi2:"+mDaoJiShi2+"|time:"+time);
        }
    }

    /**
     * 值接收处理
     */
    public void values(){
        mLoginModel = LoginModel.get(mContext);
        // 查询登录信息
        mLogin = mLoginModel.getLogin(1);
        // 当前时间
        mDangQianTime = getDangQianTime();
        // Token赋值
        setToken(mContext);

        String XunDian = getIntent().getStringExtra(TAG);
        try {
            mXunDian = new JSONObject(XunDian);
            mMenDianID = Integer.valueOf(mXunDian.getString("mMenDianId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 查询超时,并设置
        ChaoShi chaoShi = mChaoShiModel.getChaoShi(String.valueOf(mMenDianID));
        if(chaoShi != null){
            // 是否超时 1 没有 0 超时
            mIsChaoShi = chaoShi.getIsChaoShi();
            // 倒计时时间
            mDaoJiShi = chaoShi.getZhongShi();
            // 倒计时实时时间
            mDaoJiShi1 = chaoShi.getWeiChaoShi();
            // 超时时间
            mDaoJiShi2 = chaoShi.getChaoShi();
            // 启动倒计时
//            handler.postDelayed(runnable, mTIME); //每隔1s执行
        }

    }

    /**
     * 判断提交
     */
    public void panDuanTiJiao(){
        java.text.DecimalFormat df=new java.text.DecimalFormat("#");

        String ChaoStr = "";

        try {
            Double lat = Double.valueOf(mXunDian.getString("mLatitude"));
            Double lng = Double.valueOf(mXunDian.getString("mLontitude"));
            int mFanWei = Integer.valueOf(mXunDian.getString("mFanWei"));
            Double mMenDianLat = Double.valueOf(mXunDian.getString("mMenDianLat"));
            Double mMenDianLng = Double.valueOf(mXunDian.getString("mMenDianLng"));
            if(mMenDianLat <= 0.0 || mMenDianLng <= 0.0 || mFanWei < 0){
                ChaoStr = "提交超出提交范围,是否提交?";
            }else{
                Double juLi = GetShortDistance(lng,lat,mMenDianLng,mMenDianLat);
                if(juLi <= mFanWei){
                    ChaoStr = "";
                }else{
                    ChaoFanWei = String.valueOf(df.format(juLi-mFanWei));
                    ChaoStr = "提交超出提交范围"+ChaoFanWei+"米,是否提交?";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 参数提交
//        canShuTiJiao();
        if(ChaoStr.equals("")){
            canShuTiJiao();
        }else{
            new AlertDialog.Builder(XunDianActivity.this).setTitle("系统提示")//设置对话框标题
                    .setMessage(ChaoStr)//设置显示的内容
                    .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            // 提交
                            canShuTiJiao();
                        }
                    }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮
                @Override
                public void onClick(DialogInterface dialog, int which) {//响应事件
                    // 关闭loading
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                }
            }).show();//在按键响应事件中显示此对话框
        }
    }

    /**
     * 参数数据请求
     */
    public void canShuQingQiu(){
        if(mToken != null && mMenDianID > 0){
            final OkHttpClient client = new OkHttpClient();
            //3, 发起新的请求,获取返回信息
            RequestBody body = new FormBody.Builder()
                    .build();
            final Request request = new Request.Builder()
                    .addHeader("Authorization","Bearer "+mToken)
                    .url(mCanShuURL+mMenDianID)
                    .post(body)
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

    /**
     * 参数提交循环
     */
    public void canShuTiJiao(){
        if(isNetworkAvailableAndConnected(mContext)){
            if(mXunDianCanShus.size() > 0){
                mCanShuYiTiJiao = 0;
                mCanShuNums = 0;
//            tiShi(mContext,"上传中,请稍等");
                /**
                 * 图片的数量
                 */
                for(Integer key:mXunDianCanShus.keySet()){
                    if(mXunDianCanShus.get(key) != null){
                        if(mXunDianCanShus.get(key).getPhotoFile() != null || mXunDianCanShus.get(key).getPhontPath() != null){
                            mCanShuNums++;
                        }
                    }
                }
                /**
                 * 图片上传
                 */
                if(mCanShuNums > 0){
                    Log.i("巡店",""+mCanShuNums);
                    for(Integer key:mXunDianCanShus.keySet()){
                        if(mXunDianCanShus.get(key) != null){
                            PhoneTiJiao(mXunDianCanShus.get(key));
                        }
                    }
                }else{
                    TiJiao(mXunDianCanShus);
                }
            }
        }else{
            tiShi(mContext,"网络无法连接");
            finish();
        }

    }

    /**
     * 图片提交
     */
    public void PhoneTiJiao(XunDianCanShu xunDianCanShu){
        final OkHttpClient client = new OkHttpClient();
        //3, 发起新的请求,获取返回信息
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        // 图片不为空
        if(xunDianCanShu.getPhotoFile() != null || xunDianCanShu.getPhontPath() != null){
            File file;
            if(xunDianCanShu.getPhotoFile() != null){
                file = xunDianCanShu.getPhotoFile();
            }else{
                file = new File(xunDianCanShu.getPhontPath());
            }

            // MediaType.parse() 里面是上传的文件类型。 MediaType.parse("image/*")
            body.addFormDataPart(
                    "photo",
                    getPhotoFilename(),
                    RequestBody.create(MediaType.parse("image/jpeg"),file)
            );

        }

        // 参数id
        body.addFormDataPart("id",String.valueOf(xunDianCanShu.getId()));

        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mTuPanTJURL)
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
     * 参数提交 mXunDianCanShus
     */
    public void TiJiao(HashMap<Integer,XunDianCanShu> xunDianCanShuHashMap){
        JSONObject jsonObjects = new JSONObject();
        for(Integer key:mXunDianCanShus.keySet()){
            if(mXunDianCanShus.get(key) != null){
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id",""+mXunDianCanShus.get(key).getId());
                    jsonObject.put("mendianid",""+mXunDianCanShus.get(key).getMenDianId());

                    if(mXunDianCanShus.get(key).getValue() == null){
                        jsonObject.put("value","");
                    }else{
                        jsonObject.put("value",""+mXunDianCanShus.get(key).getValue());

                    }

                    if(mXunDianCanShus.get(key).getServerPhotoPath() == null){
                        jsonObject.put("path","");
                    }else{
                        jsonObject.put("path",""+mXunDianCanShus.get(key).getServerPhotoPath());

                    }

                    jsonObject.put("gps_lat",mXunDian.getString("mLatitude"));
                    jsonObject.put("gps_lng",mXunDian.getString("mLontitude"));
                    jsonObject.put("gps_addr",mXunDian.getString("mAddr"));
                    jsonObject.put("gps_addr_yuyihua",mXunDian.getString("mLocationDescribe"));
                    // 是否超时
                    jsonObject.put("is_chao_shi",String.valueOf(mIsChaoShi));
                    // 超时时间
                    jsonObject.put("chao_shi_shi_jian",String.valueOf(mDaoJiShi2));
                    // 未超时所用时间
                    jsonObject.put("yong_shi_jian",String.valueOf(mDaoJiShi-mDaoJiShi1));
                    // 开始时间
                    jsonObject.put("xun_kai_si_time",mXunDianCanShus.get(key).getXunKaiShiTime());
                    // 完成时间
                    jsonObject.put("xun_jie_shu_time",mXunDianCanShus.get(key).getXunJieShuTime());
                    // 提交超出多少范围米
                    jsonObject.put("chao_fan_wei",ChaoFanWei);
                    Log.i("巡店",jsonObject.toString());
                    jsonObjects.put(""+key,jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
//        Log.i(" ",jsonObjects.toString());
        final OkHttpClient client = new OkHttpClient();
        //3, 发起新的请求,获取返回信息
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonObjects.toString());
        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mCanShuTJURL)
                .post(requestBody)
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
     * 删除本地数据库对应信息,关闭loading
     */
    public void deleteXunDianData(String strid){
        // 图片数量
        mCanShuNums = 0;
        // 图片已提交数量
        mCanShuYiTiJiao = 0;
        // 关闭loading
        WeiboDialogUtils.closeDialog(mWeiboDialog);
        // 删除本地数据库数据
        mXunDianModel.deleteXunDian(strid,String.valueOf(mLogin.getUid()));
        // 设置已提交
        mIsTiJiao = 1;
        // 删除本地超时数据
        mChaoShiModel.deleteCaoShi(String.valueOf(mMenDianID));
        // 关闭当前activity
        finish();
    }

    /**
     * 根据巡店参数 jsonString 创建View
     * @param jsonString
     */
    public void addView(String jsonString){

        final String jsonStrings = jsonString;

        mXun_dian_bounce_linearlayout = (LinearLayout)findViewById(R.id.xun_dian_bounce_linearlayout);
        mLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParam.setMargins(0,15,0,0);

        mXunDianCanShus = new HashMap<Integer, XunDianCanShu>();

        try {
            mXunDianJson = new JSONArray(jsonStrings);
            mCanShuNum = 0;
            for(int i = 0;i<mXunDianJson.length();i++){
                mCanShuNum++;
                if(Integer.valueOf(mXunDianJson.getJSONObject(i).getString("is_bi_tian")) == 1
                        && mXunDianJson.getJSONObject(i).getString("is_pai_zhao").equals("是")){
                    // 不为空数量
                    mCanShuNum++;
                }



                mXunDianCanShu = new XunDianCanShu();
                // 用户id
                mXunDianCanShu.setUserId(String.valueOf(mLogin.getUid()));
                // 参数id
                int id = Integer.valueOf(mXunDianJson.getJSONObject(i).getString("id"));
                // 店铺id
                mXunDianCanShu.setMenDianId(Integer.valueOf(mXunDian.getString("mMenDianId")));
                // 品牌id
                mXunDianCanShu.setMenDianPingPaiId(Integer.valueOf(mXunDian.getString("mMenDianPingPaiId")));
                // 店铺名称
                mXunDianCanShu.setMenDianMingCheng(mXunDian.getString("mMenDianMingCheng"));
                // 下标
                mXunDianCanShu.setXiaBiao(id);
                // 参数id
                mXunDianCanShu.setId(id);
                // 名称
                mXunDianCanShu.setName(mXunDianJson.getJSONObject(i).getString("name"));
                // 输入类型
                mXunDianCanShu.setTian_xie_fang_shi(mXunDianJson.getJSONObject(i).getString("tian_xie_fang_shi"));
                // 是否拍照
                mXunDianCanShu.setIs_pai_zhao(mXunDianJson.getJSONObject(i).getString("is_pai_zhao"));
                // 选择项目
                mXunDianCanShu.setXuan_ze_qi(mXunDianJson.getJSONObject(i).getString("xuan_ze_qis"));
                // 是否必须拍照
                mXunDianCanShu.setIs_bi_tian(Integer.valueOf(mXunDianJson.getJSONObject(i).getString("is_bi_tian")));
                // 编号+name
                mXunDianCanShu.setBian_hao_name(mXunDian.getString("mBianHao")+"-"+mXunDianCanShu.getMenDianMingCheng());

                // 查询数据库是否有值
                ChaKanFuZhi();

                // 存储显示序号
                mShowXuHao.put(id,i+1);

                // 创建标题
                CreateBiaoTi(mXunDianCanShu.getName(),mXunDianCanShu.getIs_bi_tian(),i);

                // 创建对应view
                CreateView(mXunDianCanShu.getTian_xie_fang_shi(),mXunDianCanShu.getIs_pai_zhao(),id);

                // 添加到List对象
                mXunDianCanShus.put(id,mXunDianCanShu);

                // 设置最大id
                setZuiDaXiaBiao(id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 关闭loading
        WeiboDialogUtils.closeDialog(mWeiboDialog);
        // 倒计时调用
        diaoYongDaoJiShi();
    }

    /**
     * 创建标题
     * @param title 标题
     * @param isXin 1 是显示*号 0 不显示*号
     * @param xuHao 序号
     *
     */
    public void CreateBiaoTi(String title,int isXin,int xuHao){
        LinearLayout ll = new LinearLayout(this);
        // 设置宽高
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,80,0,0);
        ll.setLayoutParams(layoutParams);
        // 设置方向
        ll.setOrientation(LinearLayout.HORIZONTAL);
        // 设置居中
        ll.setGravity(Gravity.CENTER_VERTICAL);
        // 设置排版
        ll.setWeightSum(1);
        // 添加到父布局
        mXun_dian_bounce_linearlayout.addView(ll);

        // 创建标题TextView
        TextView titleTv = new TextView(this);
        LinearLayout.LayoutParams layoutParamtv = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        titleTv.setLayoutParams(layoutParamtv);
        // 设置字体大小
        titleTv.setTextSize(18);
        titleTv.setText((xuHao+1)+" : "+title);
        // 添加到父布局
        ll.addView(titleTv);

        // 创建*
        if(isXin == 1){
            TextView titleXin = new TextView(this);
            LinearLayout.LayoutParams layoutParamXin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
            // 设置左边距
            layoutParamXin.setMargins(0,0,0,5);
            titleXin.setLayoutParams(layoutParamXin);
            // 设置居中
            titleXin.setGravity(Gravity.CENTER_VERTICAL);
            // 设置颜色
            titleXin.setTextColor(getResources().getColor(R.color.hongse));
            // 设置显示
            titleXin.setText(R.string.xin);
            // 添加到父布局
            ll.addView(titleXin);
        }
    }

    /**
     * 创建对应View
     * @param leixin 创建对应view 数字EditView 文本 EditView 选择器 日期
     * @param isZhaoPian 是否创建照相 是 否
     * @param XiaBiao 当前下标
     */
    public void CreateView(String leixin,String isZhaoPian,int XiaBiao){
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(mLayoutParam);
        ll.setOrientation(LinearLayout.VERTICAL);
        // 添加父布局
        mXun_dian_bounce_linearlayout.addView(ll);

        if(leixin.equals("数字")){
            CreateShuZi(ll,XiaBiao);
        }
        if(leixin.equals("文本")){
            CreateWenBen(ll,XiaBiao);
        }
        if(leixin.equals("选择器")){
            CreateXunZeQi(ll,XiaBiao);
        }
        if(leixin.equals("日期")){
            CreateRiQi(ll,XiaBiao);
        }
        // 创建照片
        if(isZhaoPian.equals("是")){
            CreateZhaoPian(ll,XiaBiao);
        }
    }

    /**
     * 创建数字
     * @param ll
     * @param XiaBiao 当前下标
     */
    public void CreateShuZi(LinearLayout ll,int XiaBiao){
        EditText editText = new EditText(this);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(layoutParam);
        // 内边距
        editText.setPadding(0,0,0,5);
        // 字体大小
        editText.setTextSize(16);
        // 背景
        editText.setBackgroundResource(R.drawable.login_border);
        // 光标
        try {
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editText, R.drawable.edit_cursor_color);
        } catch (Exception ignored) {
        }
        // 设置默认提示
        editText.setHint(R.string.shu_zi);
        // 设置限制输入
        editText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        // 设置editText类型
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        // 设置数据库值
        if(mXunDianCanShu.getValue() != null){
            editText.setText(mXunDianCanShu.getValue());
        }
        // 禁止输入
        if(mXunDianCanShu.getXunJieShuTime() != null && mXunDianCanShu.getXunJieShuTime() != "" ){
            editText.setFocusable(false);
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tiShi(mContext,mDianJiBaoCun);
                }
            });
        }
        // 存储
        mXunDianCanShu.setEditText(editText);
        // 唯一id
        final int editTextId = XiaBiao;
        // 输入值存储
        editText.addTextChangedListener(new TextWatcher() {
            final int id = editTextId;
            /**
             * 内容改变之前调用
             * @param charSequence
             * @param i
             * @param i1
             * @param i2
             */
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            /**
             * 内容改变，可以去告诉服务器
             * @param charSequence
             * @param i
             * @param i1
             * @param i2
             */
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            /**
             * 内容改变之后调用
             * @param editable
             */
            @Override
            public void afterTextChanged(Editable editable) {
                // 设置值
                mXunDianCanShus.get(id).setValue(String.valueOf(editable));
                // 写入数据库
                mXunDianModel.addIsUpdate(mXunDianCanShus.get(id));
            }
        });

        // 添加父布局
        ll.addView(editText);

    }

    /**
     * 创建文本
     * @param ll
     * @param XiaBiao 当前下标
     */
    public void CreateWenBen(LinearLayout ll,int XiaBiao){
        EditText editText = new EditText(this);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(layoutParam);
        // 内边距
        editText.setPadding(0,0,0,5);
        // 字体大小
        editText.setTextSize(16);
        // 背景
        editText.setBackgroundResource(R.drawable.login_border);
        // 光标
        try {
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editText, R.drawable.edit_cursor_color);
        } catch (Exception ignored) {
        }
        // 设置默认提示
        editText.setHint(R.string.wen_ben);
        // 存储
        mXunDianCanShu.setEditText(editText);
        // 设置数据库值
        if(mXunDianCanShu.getValue() != null){
            editText.setText(mXunDianCanShu.getValue());
        }
        // 唯一id
        final int editTextId = XiaBiao;
        // 禁止输入
        if(mXunDianCanShu.getXunJieShuTime() != null && mXunDianCanShu.getXunJieShuTime() != "" ){
            editText.setFocusable(false);
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tiShi(mContext,mDianJiBaoCun);
                }
            });
        }
        // 输入值存储
        editText.addTextChangedListener(new TextWatcher() {
            final int id = editTextId;
            /**
             * 这个方法被调用，说明在s字符串中，从start位置开始的count个字符即将被长度为after的新文本所取代。在这个方法里面改变s，会报错。
             * @param charSequence
             * @param i
             * @param i1
             * @param i2
             */
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            /**
             * 这个方法被调用，说明在s字符串中，从start位置开始的count个字符刚刚取代了长度为before的旧文本。在这个方法里面改变s，会报错。
             * @param charSequence
             * @param i
             * @param i1
             * @param i2
             */
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            /**
             * 这个方法被调用，那么说明s字符串的某个地方已经被改变。
             * @param editable
             */
            @Override
            public void afterTextChanged(Editable editable) {
                // 设置值
                mXunDianCanShus.get(id).setValue(String.valueOf(editable));
                // 写入数据库
                mXunDianModel.addIsUpdate(mXunDianCanShus.get(id));
            }
        });

        // 添加父布局
        ll.addView(editText);
    }

    /**
     * 创建选择器
     * @param ll
     * @param XiaBiao 当前下标
     */
    public void CreateXunZeQi(LinearLayout ll,int XiaBiao){
        final TextView textView = new TextView(this);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParam);
        // 内边距
        textView.setPadding(0,0,0,5);
        // 字体大小
        textView.setTextSize(16);
        // 背景
        textView.setBackgroundResource(R.drawable.login_border);
        // 设置默认提示
        textView.setHint(R.string.wen_ben);
        // 设置颜色
        textView.setTextColor(getResources().getColor(R.color.heise));
        // 设置默认显示
        textView.setHint(R.string.xun_ze_qi);
        // 存储
        mXunDianCanShu.setTextView(textView);
        // 设置数据库值
        if(mXunDianCanShu.getValue() != null){
            textView.setText(mXunDianCanShu.getValue());
        }
        // 唯一id
        final int editTextId = XiaBiao;

        String[] stringZhuan = new String[]{"无"};
        // 选择项数据转换
        if(mXunDianCanShu.getXuan_ze_qi() != null){
            stringZhuan = ChuLiJson(mXunDianCanShu.getXuan_ze_qi());
        }
//        Log.i("巡店",mXunDianCanShu.getXuan_ze_qi());
        final String[] strings = stringZhuan;
        // 点击保存后无法编辑
        if(mXunDianCanShu.getXunJieShuTime() != null && mXunDianCanShu.getXunJieShuTime() != "" ){
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tiShi(mContext,mDianJiBaoCun);
                }
            });
        }else{
            textView.setOnClickListener(new View.OnClickListener() {
                final int id = editTextId;
                @Override
                public void onClick(View view) {
                    if(strings.length > 0){
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(XunDianActivity.this);
                        alertBuilder.setItems(strings, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int index) {
                                // 更新显示
                                textView.setText(strings[index]);
                                // 存储值
                                if(strings[index] != null){
                                    mXunDianCanShus.get(id).setValue(strings[index]);
                                    // 写入数据库
                                    mXunDianModel.addIsUpdate(mXunDianCanShus.get(id));
                                }
                                alertDialog1.dismiss();
                            }
                        });
                        alertDialog1 = alertBuilder.create();
                        alertDialog1.show();
                    }

                }
            });
        }

        // 添加到父组件
        ll.addView(textView);
    }

    /**
     * 创建日期选择器
     * @param ll
     * @param XiaBiao 当前下标
     */
    public void CreateRiQi(LinearLayout ll,int XiaBiao){
        final TextView textView = new TextView(this);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParam);
        // 内边距
        textView.setPadding(0,0,0,5);
        // 字体大小
        textView.setTextSize(16);
        // 背景
        textView.setBackgroundResource(R.drawable.login_border);
        // 设置默认提示
        textView.setHint(R.string.wen_ben);
        // 设置颜色
        textView.setTextColor(getResources().getColor(R.color.heise));
        // 存储
        mXunDianCanShu.setTextView(textView);
        // 设置默认显示
        textView.setHint(R.string.ri_qi);
        // 设置数据库值
        if(mXunDianCanShu.getValue() != null){
            textView.setText(mXunDianCanShu.getValue());
        }
        // 唯一id
        final int editTextId = XiaBiao;
        // 禁止输入
        if(mXunDianCanShu.getXunJieShuTime() != null && mXunDianCanShu.getXunJieShuTime() != "" ){
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tiShi(mContext,mDianJiBaoCun);
                }
            });
        }else{
            // 点击事件
            textView.setOnClickListener(new View.OnClickListener() {
                final int id = editTextId;
                @Override
                public void onClick(View view) {
                    Calendar c = Calendar.getInstance();
                    new DatePickerDialog(XunDianActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // TODO Auto-generated method stub
                            String string = year+"年"+(monthOfYear+1)+"月"+dayOfMonth;
                            textView.setText(string);
                            // 数据存储
                            if(string != null){
                                mXunDianCanShus.get(id).setValue(string);
                                // 写入数据库
                                mXunDianModel.addIsUpdate(mXunDianCanShus.get(id));
                            }
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        }

        // 添加到父组件
        ll.addView(textView);
    }

    /**
     * 创建拍照
     * @param ll
     * @param XiaBiao
     */
    public void CreateZhaoPian(LinearLayout ll,int XiaBiao){
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout linearLayout1 = new LinearLayout(this);
        final FrameLayout frameLayout = new FrameLayout(this);
        final ImageView imageViewShow = new ImageView(this);
        final ImageView imageViewDian = new ImageView(this);
        final LinearLayout.LayoutParams layoutParamls = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParaml = new LinearLayout.LayoutParams(250,300);
        layoutParamls.setMargins(0,20,0,0);
        linearLayout.setLayoutParams(layoutParamls);

        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        // 外边距
        // 添加父组件
        ll.addView(linearLayout);


        linearLayout1.setLayoutParams(layoutParaml);
        // 设置背景

        linearLayout1.setGravity(Gravity.CENTER);
        // 添加父组件
        linearLayout.addView(linearLayout1);


        frameLayout.setLayoutParams(layoutParaml);
        // 添加父组件
        linearLayout1.addView(frameLayout);

        // 图片显示组件
        LinearLayout.LayoutParams layoutParamI = new LinearLayout.LayoutParams(250,300);
        imageViewShow.setLayoutParams(layoutParamI);
        imageViewShow.setBackgroundResource(R.drawable.xun_dian_img_border);
        imageViewShow.setPadding(0,10,0,10);
        // 添加父组件
        frameLayout.addView(imageViewShow);
        // 显示数据库图片
        if(mXunDianCanShu.getPhontPath() != null){
            Bitmap bitmap = PictureUtils.getScaledBitmap(mXunDianCanShu.getPhontPath(),this);
            imageViewShow.setImageBitmap(bitmap);
        }else{
            // 图片加号显示
            imageViewDian.setLayoutParams(layoutParaml);
            imageViewDian.setImageResource(R.drawable.xun_dian_img_add);
            // 添加父组件
            frameLayout.addView(imageViewDian);
        }

        // 唯一id
        final int editTextId = XiaBiao;

        PackageManager packageManager = this.getPackageManager();
        // 相机
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 获取文件存储地址
        final File mPhotoFile = getPhotoFile(getPhotoFilename());

        // 文件存储地址不能为空,相机类应用不能为空
        final boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        imageViewShow.setEnabled(canTakePhoto);
        if(canTakePhoto){
//            Uri uri = Uri.fromFile(mPhotoFile);
            Uri uri = FileProvider.getUriForFile(mContext,"com.bignerdranch.android.xundian.provider",mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        }
        // 点击保存后无法编辑
        if(mXunDianCanShu.getXunJieShuTime() != null && mXunDianCanShu.getXunJieShuTime() != "" ){
            imageViewShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tiShi(mContext,mDianJiBaoCun);
                }
            });
        }else{
            imageViewShow.setOnClickListener(new View.OnClickListener() {
                final int id = editTextId;
                @Override
                public void onClick(View view) {
                    // 未输入值提示输入值
                    if(mXunDianCanShus.get(editTextId) != null &&
                            (mXunDianCanShus.get(editTextId).getValue() == null ||  mXunDianCanShus.get(editTextId).getValue() == "")){
                        tiShi(mContext,mShowXuHao.get(mXunDianCanShus.get(editTextId).getId())+" : "+mXunDianCanShus.get(editTextId).getName()+"请选择或输入值");
                    }else{
                        startActivityForResult(captureImage,REQUEST_PHOTO);
                        if(canTakePhoto){
                            // 点击id
                            mTuPianDianJi = id;
                            // 存储加号View
                            mXunDianCanShus.get(id).setMImageViewj(imageViewDian);
                            // 存储显示图片View
                            mXunDianCanShus.get(id).setImageView(imageViewShow);
                            // 存储文件对象
                            mXunDianCanShus.get(id).setPhotoFile(mPhotoFile);
                            // 存储图片路径
                            mXunDianCanShus.get(id).setPhontPath(mPhotoFile.getPath());
                        }
                    }
                }
            });
        }

    }

    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        // 布局
        mTitle_nei_ye = (TextView)findViewById(R.id.title_nei_ye);
        // 保存
        mXun_dian_bc_button = (Button)findViewById(R.id.xun_dian_bc_button);
        // 倒计时显示
        mTextview_dao_ji_shi = (TextView)findViewById(R.id.textview_dao_ji_shi);
    }


    /**
     * 组件操作, 操作 mCanShuNum
     */
    public void ZhuJianCaoZhuo(){
        // 设置标题
        mTitle_nei_ye.setText(R.string.gong_zuo_zhong_xin_xun_dian_guan_li);
        // 保存
        mXun_dian_bc_button.setOnClickListener(new View.OnClickListener() {
            int inWenTi = 0;
            int mIsTijiao = 1;
            @Override
            public void onClick(View view) {
                inWenTi = 0;
                if(mXunDianCanShus != null  &&  mXunDianCanShus.size() > 0){
                    for(int i = 0;i<=mZuiDaXiaBiao;i++){
                        if(mXunDianCanShus.get(i) != null) {
                            int c = mShowXuHao.get(mXunDianCanShus.get(i).getId());
                            // 验证值
                            if (mXunDianCanShus.get(i).getValue() != null) {
                                String s = mXunDianCanShus.get(i).getValue().trim();
                                if (s == null || "".equals(s)) {
                                    tiShi(mContext, c + " : " + mXunDianCanShus.get(i).getName() + "内容不能为空");
                                    break;
                                } else {
                                    // 验证图片 isZhaoPian
                                    if (mXunDianCanShus.get(i).getIs_pai_zhao().equals("是")
                                            && mXunDianCanShus.get(i).getIs_bi_tian() == 1
                                            && mXunDianCanShus.get(i).getPhontPath() == null
                                            && mXunDianCanShus.get(i).getPhotoFile() == null
                                            && mXunDianCanShus.get(i).getServerPhotoPath() == null) {
                                        tiShi(mContext, c + " : " + mXunDianCanShus.get(i).getName() + "图片不能为空");
                                        break;
                                    } else {
                                        inWenTi++; // 输入框累加

                                        if(mXunDianCanShus.get(i).getIs_pai_zhao().equals("是") && mXunDianCanShus.get(i).getIs_bi_tian() == 1){
                                            inWenTi++;
                                        }
                                    }
                                }
                            } else {
//                                Log.i("巡店", mXunDianCanShus.get(i).getName());
                                tiShi(mContext, c + " : " + mXunDianCanShus.get(i).getName() + "内容不能为空");
                                break;
                            }
                        }
                    }

                    Log.i("巡店",mCanShuNum+"|"+inWenTi);

                    // 提交 18817610991  34805
                    if(mIsTijiao == 1){
                        if(mCanShuNum == inWenTi){
                            LoadingStringEdit("提交中...");
                            mIsTijiao = 0;
                            // 保存完成时间
                            baoCunWanChengTime();

                            // 延迟地址提交
                            panDuanTiJiao();
                        }
                    }else{
                        tiShi(mContext,"已提交...");
                    }
                }else{
                    tiShi(mContext,"为空不能提交");
                }

            }
        });
    }

    /**
     * 保存完成时间
     */
    public void baoCunWanChengTime(){
        // 数据库查询出来如果为空将巡店开始时间,存入数据库
        if(mXunDianCanShu.getXunJieShuTime() == null || mXunDianCanShu.getXunJieShuTime() == "" ){
            for(Integer key:mXunDianCanShus.keySet()){
                if(mXunDianCanShus.get(key) != null){
                    XunDianCanShu xunCan = mXunDianCanShus.get(key);
                    xunCan.setXunJieShuTime(getDangQianTime());
                    mXunDianModel.addIsUpdate(xunCan);
                }
            }
        }
    }


    /**
     * 启动其他Activity返回方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != this.RESULT_OK){
            return;
        }else if(requestCode == REQUEST_PHOTO){
            // 显示图片
            updatePhotoView();
        }
    }

    /**
     * 显示对应图片
     */
    private void updatePhotoView(){
        File PhotoFile = mXunDianCanShus.get(mTuPianDianJi).getPhotoFile();
        ImageView view =mXunDianCanShus.get(mTuPianDianJi).getImageView();
        ImageView imageViewDian = mXunDianCanShus.get(mTuPianDianJi).getMImageViewj();

        if(PhotoFile != null && view != null){
            if(PhotoFile == null || !PhotoFile.exists()){
                view.setImageDrawable(null);
            }else{
                // 隐藏加号
                imageViewDian.setVisibility(View.GONE);
                // 压缩图片
                String pathPhoto = imgYaSuo(PhotoFile.getPath(),Config.XunCanImgWidth,Config.XunCanImgHeight);
                File files = new File(pathPhoto);

                // 显示图片
                Bitmap bitmap = PictureUtils.getScaledBitmap(files.getPath(),this);
                view.setImageBitmap(bitmap);

                // 存入文件对象
                mXunDianCanShus.get(mTuPianDianJi).setPhotoFile(files);
                // 存入文件路径
                mXunDianCanShus.get(mTuPianDianJi).setPhontPath(files.getPath());
                // 写入数据库
                mXunDianModel.addIsUpdate(mXunDianCanShus.get(mTuPianDianJi));
            }
        }
    }


    /**
     * 查询对应巡店是否有值,有值则赋值
     */
    public void ChaKanFuZhi(){

        XunDianCanShu xunDianCanShu = mXunDianModel.getXunDian(
                String.valueOf(mXunDianCanShu.getMenDianId()),
                String.valueOf(mXunDianCanShu.getXiaBiao()),
                mXunDianCanShu.getUserId()
        );
        if(xunDianCanShu != null){
            // 用户输入值
            mXunDianCanShu.setValue(xunDianCanShu.getValue());
            // 图片路径
            mXunDianCanShu.setPhontPath(xunDianCanShu.getPhontPath());
            // 巡店开始时间
            mXunDianCanShu.setXunKaiShiTime(xunDianCanShu.getXunKaiShiTime());
            // 巡店结束时间
            mXunDianCanShu.setXunJieShuTime(xunDianCanShu.getXunJieShuTime());
        }
        // 数据库查询出来如果为空将巡店开始时间,存入数据库
        if(mXunDianCanShu.getXunKaiShiTime() == null || mXunDianCanShu.getXunKaiShiTime() == "" ){
            mXunDianCanShu.setXunKaiShiTime(mDangQianTime);
            mXunDianModel.addIsUpdate(mXunDianCanShu);
        }
//        Log.i("巡店","开始时间:"+mXunDianCanShu.getXunKaiShiTime()+",完成时间:"+mXunDianCanShu.getXunJieShuTime());
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        // 保存超时数据
        chaoShiBC();
    }

    /**
     * 用户如果没有提交,则保持超时信息
     */
    public void chaoShiBC(){
        if(mXunDianCanShus != null  &&  mIsTiJiao == 0 && mXunDianCanShus.size() > 0){
            ChaoShi chaoShi = new ChaoShi();
            chaoShi.setId(mMenDianID);
            chaoShi.setIsChaoShi(mIsChaoShi);
            chaoShi.setChaoShi(mDaoJiShi2);
            chaoShi.setWeiChaoShi(mDaoJiShi1);
            chaoShi.setZhongShi(mDaoJiShi);
            mChaoShiModel.addIsUpdate(chaoShi);
        }
    }


}
