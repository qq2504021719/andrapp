package com.bignerdranch.android.xundian;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;


import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.bignerdranch.android.xundian.comm.BaiFangGuanli;
import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.PictureUtils;
import com.bignerdranch.android.xundian.comm.WeiboDialogUtils;
import com.bignerdranch.android.xundian.kaoqing.KaoQingCommonActivity;
import com.bignerdranch.android.xundian.kehutuozhan.KeHuActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Administrator on 2017/10/13.
 */

public class BaiFangGuanLiActivity extends KaoQingCommonActivity implements KaoQingCommonActivity.Callbacks{
    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.KaoQingJiLuActivity";

    private TextView mText_bf_tian_jia;

    // 公司名称
    private TextView mText_bf_gong_si_ming_cheng;
    private TextView mText_bf_gong_si_ming_cheng_value;
    // 公司alert View
    private View mViewD;
    // 公司alert
    private LinearLayout mBf_search_men_dian;
    public Dialog dialog = null;


    // 公司品牌
    private TextView mBai_fang_guan_li_pin_pai;
    private TextView mText_bf_gong_si_pin_pai_value;
    // 品牌alert View
    private View mViewPPD;
    // 品牌alert
    private LinearLayout mBf_search_men_dian_pp;
    public Dialog dialogpp = null;

    // 公司编号
    private TextView mText_bf_gong_si_bian_hao_value;

    // 开始时间
    private TextView mtext_bf_kai_shi_time;
    private TextView mtext_bf_kai_shi_time_value;

    // 结束时间
    private TextView mtext_bf_jie_shu_time;
    private TextView mtext_bf_jie_shu_time_value;
    // 图片点击标识
    public int REQUEST_PHOTO = 1;
    // 拜访内容
    private EditText mEditText_bf_nei_rong;

    // 定位信息显示
    private TextView mText_bf_ding_wei_value;
    // 重新定位
    private ImageView mBf_ri_chang_ding_wei;

    // 提交信息
    private Button mButton_bf_ti_jiao_gzb;

    // 拍照选择
    public Dialog mDialogPhone = null;
    // 拍照view
    public View mViewPaiZhao;
    // alert 拍照
    public TextView mPaiZhao;
    // alert 相册选择
    public TextView mXiangCe;


    // 时间对象
    public Calendar ct;
    // 拜访对象
    private BaiFangGuanli mBaiFangGuanli = new BaiFangGuanli();

    // 图片ImageViewList
    public HashMap<Integer,ImageView> mPhoneImageViewList = new HashMap<Integer, ImageView>();
    // 图片存储对象
    public HashMap<Integer,File> mPhoneList = new HashMap<Integer, File>();
    // 图片存储提交地址
    public JSONObject mPhonePathJson = new JSONObject();

    // 数据提交
    // 拜访数据提交url
    private String mBfUrl = Config.URL+"/app/bai_fang_add";
    Handler handler = new Handler();
    // 图片提交返回数量
    private int mTuPianNum = 1;
    // 图片总数
    private int mTuPianCount = 4;

    // 公司设置图片数量
    public int mGongSetPhoneNum = 10;

    // 照片父Linear
    public LinearLayout mXian_chang_zhao_pian;

    // 当前点击ImageView
    public ImageView mDangQianOnclickImageView;

    // 当前拍照数量
    public int mPaiZhaoNum = 0;


    public static Intent newIntent(Context packageContext, int intIsId){
        Intent i = new Intent(packageContext,BaiFangGuanLiActivity.class);
        i.putExtra(EXTRA,intIsId);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_kao_bai_fang_guan_li);
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
        // 地图控件
        mMapView = (MapView) findViewById(R.id.bmapView_bf);

        // 客户添加
        mText_bf_tian_jia = (TextView)findViewById(R.id.text_bf_tian_jia);

        // 公司名称
        mText_bf_gong_si_ming_cheng = (TextView)findViewById(R.id.text_bf_gong_si_ming_cheng);
        mText_bf_gong_si_ming_cheng_value = (TextView)findViewById(R.id.text_bf_gong_si_ming_cheng_value);

        // 公司品牌
        mBai_fang_guan_li_pin_pai = (TextView)findViewById(R.id.bai_fang_guan_li_pin_pai);
        mText_bf_gong_si_pin_pai_value = (TextView)findViewById(R.id.text_bf_gong_si_pin_pai_value);

        // 公司编号
        mText_bf_gong_si_bian_hao_value = (TextView)findViewById(R.id.text_bf_gong_si_bian_hao_value);

        // 开始时间
        mtext_bf_kai_shi_time = (TextView)findViewById(R.id.text_bf_kai_shi_time);
        mtext_bf_kai_shi_time_value = (TextView)findViewById(R.id.text_bf_kai_shi_time_value);

        // 结束时间
        mtext_bf_jie_shu_time = (TextView)findViewById(R.id.text_bf_jie_shu_time);
        mtext_bf_jie_shu_time_value = (TextView)findViewById(R.id.text_bf_jie_shu_time_value);

        // 照片父几点
        mXian_chang_zhao_pian = (LinearLayout)findViewById(R.id.xian_chang_zhao_pian);


        // 拜访内容
        mEditText_bf_nei_rong = (EditText)findViewById(R.id.editText_bf_nei_rong);

        // 定位信息显示
        mText_bf_ding_wei_value = (TextView)findViewById(R.id.text_bf_ding_wei_value);
        // 重新定位
        mBf_ri_chang_ding_wei = (ImageView)findViewById(R.id.bf_ri_chang_ding_wei);

        // 提交
        mButton_bf_ti_jiao_gzb = (Button)findViewById(R.id.button_bf_ti_jiao_gzb);

    }

    /**
     * 值处理
     */
    public void values(){
        // Token赋值
        setToken(mContext);
        // 定位信息显示
        mDingWeiTextView = mText_bf_ding_wei_value;

        // 公司弹出View
        // 获取布局文件
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewD = inflater.inflate(R.layout.alert_kao_bai_fang_guan_li_search, null);
        // 公司 alert 弹出
        mBf_search_men_dian = mViewD.findViewById(R.id.bf_search_men_dian);

        // 拍照弹出
        mViewPaiZhao = inflater.inflate(R.layout.alert_kao_bai_fang_guan_li_phone,null);
        // 拍照弹出 拍照
        mPaiZhao = mViewPaiZhao.findViewById(R.id.text_bf_alert_pai_zhao);
        // 拍照弹出 相册选择
        mXiangCe = mViewPaiZhao.findViewById(R.id.text_bf_alert_xiang_ce_xuan_zhe);

        // 百度定位
        BaiDuDingWeiDiaoYong(mContext);
        // 搜索门店
        menDianSearch();
        // 请求公司信息
        GongSiXinXiQingQiu();

        LayoutInflater inflaters = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewPPD = inflaters.inflate(R.layout.alert_kao_bai_fang_guan_li_search, null);
        // 公司 alert 弹出
        mBf_search_men_dian_pp = mViewPPD.findViewById(R.id.bf_search_men_dian);
        // 品牌搜索
        pingPaiSouShuo();

    }


    /**
     * 组件操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText("拜访管理");

        // 客户添加
        mText_bf_tian_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = KeHuActivity.newIntent(mContext,1);
                startActivity(i);
            }
        });

        // 品牌
        mBai_fang_guan_li_pin_pai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 品牌搜索
                pingPaiSouShuo();
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

        // 开始时间
        mtext_bf_kai_shi_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DayRiQiXuanZhe(1);
            }
        });

        // 结束时间
        mtext_bf_jie_shu_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DayRiQiXuanZhe(2);
            }
        });

        // 拜访内容
        mEditText_bf_nei_rong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String string = String.valueOf(editable).trim();
                // 存储
                mBaiFangGuanli.setBaiFangNeiRong(string);
            }
        });

        // 重新定位
        mBf_ri_chang_ding_wei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tiShi(mContext,"定位成功");
                myListener = new MyLocationListenner();
                isFirstLoc = true; // 是否首次定位
                mIsDingWeiChengGong = false; // 定位是否成功
                // 百度定位
                BaiDuDingWeiDiaoYong(mContext);
            }
        });

        // 提交
        mButton_bf_ti_jiao_gzb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBaiFangGuanli.getMenDianId() != null && mBaiFangGuanli.getMenDian() != null){
                    if(mBaiFangGuanli.getKaiShiTime() != null){
                        if(mBaiFangGuanli.getJieShuShiJian() != null){
                            if(mBaiFangGuanli.getBaiFangNeiRong() != null){
                                if(mBaiFangGuanli.getAddr() != null){
                                    shuJiTiJiao();
                                }else{
                                    tiShi(mContext,"请重新定位");
                                }
                            }else{
                                tiShi(mContext,"拜访内容不能为空");
                            }
                        }else{
                            tiShi(mContext,"请选择结束时间");
                        }
                    }else{
                        tiShi(mContext,"请选择开始时间");
                    }
                }else{
                    tiShi(mContext,"请选择公司");
                }
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
                // 图片上传返回

                String string = msg.obj.toString();
                // 图片上传回调
                if(msg.obj != null && string != null){
                    try {
                        JSONObject jsonObject = new JSONObject(string);
                        if (jsonObject.getString("id") != null && jsonObject.getString("path") != null) {

                            // 存储返回地址
                            mPhonePathJson.put(String.valueOf(mTuPianNum),jsonObject.getString("path"));
                            if(mTuPianNum == mPaiZhaoNum){
                                // 参数提交
                                canShuTiJiao();
                            }
                            mTuPianNum++;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if(msg.what==2){
                // 关闭loading
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                tiShi(mContext,msg.obj+"");
                TiJiaoNeiRongQingKong();
            }else if(msg.what == 3){
                if(msg.obj.toString().equals("无数据")){
                }else{
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String bai_fang_phone_num = jsonObject.getString("bai_fang_phone_num");
                        if(!bai_fang_phone_num.equals("null") && !bai_fang_phone_num.equals("")){
                            if(Integer.valueOf(bai_fang_phone_num) > 10){
                                mGongSetPhoneNum = Integer.valueOf(bai_fang_phone_num);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // 照片处理显示 mGongSetPhoneNum
                ZhaoXunHuanXianShi();
            }
        }
    };

    /**
     * 请求公司信息
     */
    public void GongSiXinXiQingQiu(){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("xx","xx");
        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(Config.URL+"/app/user_gs")
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
     * 拜访图片提交
     */
    public void shuJiTiJiao(){
        LoadingStringEdit("数据提交中...");

        // 计算图片实际拍照数量
        mPaiZhaoNum = 0;
        for (int i = 1;i<=mGongSetPhoneNum;i++){
            if(mPhoneList.get(i) != null){
                mPaiZhaoNum ++;
            }
        }



        // 没有选择图片
        if(mPaiZhaoNum == 0){
            // 参数提交
            canShuTiJiao();
        }else{
            // 图片上传
            mTuPianNum = 1;
            for (int i = 1;i<=mGongSetPhoneNum;i++){
                if(mPhoneList.get(i) != null){
                    PhoneTiJiao(mPhoneList.get(i));
                }
            }
        }


    }

    /**
     * 图片提交
     */
    public void PhoneTiJiao(File cfile){
        final OkHttpClient client = new OkHttpClient();
        //3, 发起新的请求,获取返回信息
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        // 图片不为空
        File file = cfile;
        // MediaType.parse() 里面是上传的文件类型。 MediaType.parse("image/*")
        body.addFormDataPart(
                "photo",
                getPhotoFilename(),
                RequestBody.create(MediaType.parse("image/jpeg"),file)
        );

        // 参数id
        body.addFormDataPart("id","2");

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
                    mHandler.obtainMessage(1, response.body().string()).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mThread.start();
    }

    /**
     * 拜访数据提交 mPhonePathList
     */
    public void canShuTiJiao(){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);

        body.addFormDataPart("men_dian_id",mBaiFangGuanli.getMenDianId());
        body.addFormDataPart("kai_shi_time",mBaiFangGuanli.getKaiShiTime());
        body.addFormDataPart("jie_shu_time",mBaiFangGuanli.getJieShuShiJian());
        body.addFormDataPart("phone",mPhonePathJson.toString());

        body.addFormDataPart("bai_fang_nei_rong",mBaiFangGuanli.getBaiFangNeiRong());
        body.addFormDataPart("lng",mBaiFangGuanli.getLng());
        body.addFormDataPart("lat",mBaiFangGuanli.getLat());
        body.addFormDataPart("addr",mBaiFangGuanli.getAddr());
        body.addFormDataPart("addr1",mBaiFangGuanli.getAddr1());

        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mBfUrl)
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

    public void TiJiaoNeiRongQingKong(){
        mBaiFangGuanli = new BaiFangGuanli();
        // 公司名称
        mText_bf_gong_si_ming_cheng_value.setText("");

        // 公司品牌
        mText_bf_gong_si_pin_pai_value.setText("");

        // 公司编号
        mText_bf_gong_si_bian_hao_value.setText("");

        // 开始时间
        mtext_bf_kai_shi_time_value.setText("");

        // 结束时间
        mtext_bf_jie_shu_time_value.setText("");

        // 清除图片
        for (int i = 0;i<=mGongSetPhoneNum;i++){
            if(mPhoneImageViewList.get(i) != null){
                mPhoneImageViewList.get(i).setImageDrawable(getResources().getDrawable(R.color.zhuti));
            }
        }

        // 拜访内容
        mEditText_bf_nei_rong.setText("");
    }

    /**
     * 点击图片色块
     */
    public void PhoneXuanZhe(){
        if(mDialogPhone == null){
            // 弹窗
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);

            // 设置View
            alertBuilder.setView(mViewPaiZhao);

            // 图片弹出组件操作
            mViewPaiZhaoZhuJian();

            // 显示
            alertBuilder.create();

            mDialogPhone = alertBuilder.show();
        }else{
            mDialogPhone.show();
        }
    }

    /**
     * 图片弹出组件操作
     */
    public void mViewPaiZhaoZhuJian(){
        // 拍照弹出 拍照
        mPaiZhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TupianPaiZhao();
            }
        });
        // 拍照弹出 相册选择 mXiangCe
        mXiangCe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xiangCeXuanZhe();
            }
        });
    }

    /**
     * 启用拍照
     */
    public void TupianPaiZhao(){
        // 获取文件存储地址
        final File mPhotoFile = getPhotoFile(getPhotoFilename());
        ImageView imageView = new ImageView(mContext);
        // 存储生成的文件地址
        mPhoneList.put(REQUEST_PHOTO,mPhotoFile);

        imageView = mDangQianOnclickImageView;

        PackageManager packageManager = this.getPackageManager();
        // 相机
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 文件存储地址不能为空,相机类应用不能为空
        final boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        imageView.setEnabled(canTakePhoto);
        if(canTakePhoto){
            Uri uri = FileProvider.getUriForFile(mContext,"com.bignerdranch.android.xundian.provider",mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        }
        startActivityForResult(captureImage,REQUEST_PHOTO);
    }

    /**
     * 启动相册选择图片
     */
    public void xiangCeXuanZhe(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,5);
    }

    /**
     * 开始日期，结束日期 选择
     * @param is 1 开始日期 2 结束日期
     */
    public void DayRiQiXuanZhe(final int is){
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if(is == 2){
                    // 存储
                    mBaiFangGuanli.setJieShuShiJian(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                    // 显示
                    mtext_bf_jie_shu_time_value.setText((monthOfYear+1)+"-"+dayOfMonth);
                }else if (is == 1){
                    // 存储
                    mBaiFangGuanli.setKaiShiTime(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                    // 显示
                    mtext_bf_kai_shi_time_value.setText((monthOfYear+1)+"-"+dayOfMonth);
                }

                ct = Calendar.getInstance();

                new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        ct.set(Calendar.HOUR_OF_DAY,i);
                        ct.set(Calendar.MINUTE,i1);
                        String string = String.valueOf(DateFormat.format("HH:mm",ct));
                        if(is == 2){
                            // 显示
                            mtext_bf_jie_shu_time_value.setText(mtext_bf_jie_shu_time_value.getText()+" "+string);
                            // 存储
                            mBaiFangGuanli.setJieShuShiJian(mBaiFangGuanli.getJieShuShiJian()+" "+string);
                        }else if (is == 1){
                            // 显示
                            mtext_bf_kai_shi_time_value.setText(mtext_bf_kai_shi_time_value.getText()+" "+string);
                            // 存储
                            mBaiFangGuanli.setKaiShiTime(mBaiFangGuanli.getKaiShiTime()+" "+string);
                        }


                    }

                },ct.get(Calendar.HOUR_OF_DAY),ct.get(Calendar.MINUTE),true).show();

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
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
//                    mleiXingSearchName = String.valueOf(editable).trim();
                    // 搜索门店类型
//                    ChaXunMenDianLeiXing();
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
            Log.i("巡店",string);
        }else if(is == 2){
            mMengDianJsonData = string;
            setData(string,2);
            ShowMenDian(string,2);
            if(mMengDianPingpaiJsonData.equals("")){
                pingPaiSouShuo();
            }
        }

    }

    /**
     * 定位信息回调
     */
    public void dingWeiData(){
        // 定位信息存储
        mBaiFangGuanli.setAddr(mLocationBaiDu.getAddr());
        // 存储定位信息语义化
        mBaiFangGuanli.setAddr1(mLocationBaiDu.getLocationDescribe());
        Log.i("巡店",mBaiFangGuanli.getAddr()+"|"+mBaiFangGuanli.getAddr1());
        // 存储经度
        mBaiFangGuanli.setLng(String.valueOf(mLocationBaiDu.getLontitude()));
        // 存储纬度
        mBaiFangGuanli.setLat(String.valueOf(mLocationBaiDu.getLatitude()));
    }

    /**
     * 显示查询出来的门店
     * @param string
     * @param is 1 品牌 2门店
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
                        textView = CreateTextvBf(1,stringJson,jsonObject1.getString("men_dian_ping_pai")+"-"+jsonObject1.getString("men_dian_hao")+"- "+jsonObject1.getString("name"));
                        mBf_search_men_dian.addView(textView);

                    }
                }
            }
        }catch (JSONException e){

        }
    }

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
//                    dialogMenDianLeiXing.hide();
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
                String FanWei = jsonObject.getString("fan_wei");

                // 门店号
//                mLocationBaiDu.setBianHao(men_dian_haoText);

                // 存储选择门店id
//                mLocationBaiDu.setMenDianId(Integer.valueOf(idText));
                // 存储用户选择门店
//                mLocationBaiDu.setMenDianMingCheng(nameText);
                // 门店范围
//                mLocationBaiDu.setFanWei(FanWei);

                // 公司编号值设置
                mText_bf_gong_si_bian_hao_value.setText(men_dian_haoText);

                mText_bf_gong_si_ming_cheng_value.setText(men_dian_ping_paiText+"-"+men_dian_haoText+"-"+nameText);
            }else if(is == 3){
//                // 门店类型
//                mSearchMenDianLeiXing = string;
//                // 门店搜索
//                menDianSearch();
//                mCha_xun_men_dian_lei_xing.setText(string);
            }

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
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        // 销毁回调
        mCallbacksc = null;
        // 弹窗销毁
        if(dialog != null){
            dialog.dismiss();

        }
        // 图片弹出销毁
        if(mDialogPhone != null){
            mDialogPhone.dismiss();
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
        mDialogPhone.hide();
        if(resultCode != this.RESULT_OK){
            return;
        }else if(requestCode == REQUEST_PHOTO){
            // 显示图片
            updatePhotoView();
            // 相册选择图片返回
        }else if(requestCode == 5){

            Uri selectedImage = data.getData();

            String[] filePathColumns = {MediaStore.Images.Media.DATA};

            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();

            int columnIndex = c.getColumnIndex(filePathColumns[0]);

            String imagePath = c.getString(columnIndex);

            xiangCeXuanZeFanhui(imagePath);

            c.close();

        }
    }

    /**
     * 相册选择返回
     * @param imaePath 图片字节
     */
    public void xiangCeXuanZeFanhui(String imaePath){
        String pathPhoto = imgYaSuo(imaePath, Config.XunCanImgWidth,Config.XunCanImgHeight);

        File files = new File(pathPhoto);

        Bitmap bitmap = PictureUtils.getScaledBitmap(files.getPath(),this);

        // 显示图片
        mDangQianOnclickImageView.setImageBitmap(bitmap);
        // 存储压缩后的图片
        mPhoneList.put(REQUEST_PHOTO,files);
    }

    /**
     * 显示对应图片
     */
    private void updatePhotoView(){
        // 隐藏弹出
        File PhotoFile = null;
        PhotoFile = mPhoneList.get(REQUEST_PHOTO);

        if(PhotoFile != null){
            // 压缩图片
            String pathPhoto = imgYaSuo(PhotoFile.getPath(), Config.XunCanImgWidth,Config.XunCanImgHeight);
            File files = new File(pathPhoto);

            // 显示图片
            Bitmap bitmap = PictureUtils.getScaledBitmap(files.getPath(),this);

            // 显示图片
            mDangQianOnclickImageView.setImageBitmap(bitmap);
            // 存储压缩后的图片
            mPhoneList.put(REQUEST_PHOTO,files);
        }
    }

    /**
     * 照片点击循环显示 mXian_chang_zhao_pian
     */
    public void ZhaoXunHuanXianShi(){
        LinearLayout linearLayout = CreateLinearBaiFang(1);
        for(int i = 0;i<mGongSetPhoneNum;i++){
            ImageView imageView = CreateImageViewBaiFang(1,i+1);

            if(i%2 == 0 && i > 0){
                mXian_chang_zhao_pian.addView(linearLayout);
                linearLayout = CreateLinearBaiFang(1);
                linearLayout.addView(imageView);
            }else{
                linearLayout.addView(imageView);
            }

            // 最后一次循环,并且最后一次不是偶数。
            if((mGongSetPhoneNum-1)%2 != 0 && (mGongSetPhoneNum-1) == i){
                mXian_chang_zhao_pian.addView(linearLayout);
            }
        }
    }

    /**
     * 创建linearLayout
     * @param is 创建类型
     */
    public LinearLayout CreateLinearBaiFang(int is){
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        if(is == 1){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        }else

        linearLayout.setLayoutParams(layoutParam);

        linearLayout.setOrientation(LinearLayout.VERTICAL);

        if(is == 1){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        }

        return linearLayout;
    }

    /**
     * 创建ImageView
     * @param is 创建类型
     * @return
     */
    public ImageView CreateImageViewBaiFang(int is,final int bs){
        final ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        if(is == 1){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,370,1);
            layoutParam.setMargins(0,0,30,30);
        }
        imageView.setLayoutParams(layoutParam);

        if(is == 1){
            imageView.setBackgroundColor(getResources().getColor(R.color.zhuti));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDangQianOnclickImageView = imageView;
                    // 启动拍照
                    PhoneXuanZhe();
                    REQUEST_PHOTO = bs;

                    mPhoneImageViewList.put(REQUEST_PHOTO,mDangQianOnclickImageView);
                }
            });
        }
        return imageView;
    }
}
