package com.bignerdranch.android.xundian.kaoqing;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.BaiFangGuanli;
import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.NeiYeCommActivity;
import com.bignerdranch.android.xundian.comm.PictureUtils;
import com.bignerdranch.android.xundian.comm.XunDianCanShu;
import com.bignerdranch.android.xundian.kehutuozhan.KeHuActivity;
import com.bignerdranch.android.xundian.xundianguanli.XunDianActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

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
    private TextView mText_bf_gong_si_pin_pai_value;

    // 公司编号
    private TextView mText_bf_gong_si_bian_hao_value;

    // 开始时间
    private TextView mtext_bf_kai_shi_time;
    private TextView mtext_bf_kai_shi_time_value;

    // 结束时间
    private TextView mtext_bf_jie_shu_time;
    private TextView mtext_bf_jie_shu_time_value;

    // 图片1
    private ImageView mImageview_bf_phone1;
    public int REQUEST_PHOTO = 1;
    // 图片2
    private ImageView mImageview_bf_phone2;
    // 图片3
    private ImageView mImageview_bf_phone3;
    // 图片4
    private ImageView mImageview_bf_phone4;

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

    // 数据提交
    // 拜访数据提交url
    private String mBfUrl = Config.URL+"/app/bai_fang_add";
    Handler handler = new Handler();
    // 图片提交返回数量
    private int mTuPianNum = 0;
    // 图片总数
    private int mTuPianCount = 4;




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
        mText_bf_gong_si_pin_pai_value = (TextView)findViewById(R.id.text_bf_gong_si_pin_pai_value);

        // 公司编号
        mText_bf_gong_si_bian_hao_value = (TextView)findViewById(R.id.text_bf_gong_si_bian_hao_value);

        // 开始时间
        mtext_bf_kai_shi_time = (TextView)findViewById(R.id.text_bf_kai_shi_time);
        mtext_bf_kai_shi_time_value = (TextView)findViewById(R.id.text_bf_kai_shi_time_value);

        // 结束时间
        mtext_bf_jie_shu_time = (TextView)findViewById(R.id.text_bf_jie_shu_time);
        mtext_bf_jie_shu_time_value = (TextView)findViewById(R.id.text_bf_jie_shu_time_value);

        // 图片1
        mImageview_bf_phone1 = (ImageView)findViewById(R.id.imageview_bf_phone1);
        // 图片2
        mImageview_bf_phone2 = (ImageView)findViewById(R.id.imageview_bf_phone2);
        // 图片3
        mImageview_bf_phone3 = (ImageView)findViewById(R.id.imageview_bf_phone3);
        // 图片4
        mImageview_bf_phone4 = (ImageView)findViewById(R.id.imageview_bf_phone4);

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
    }


    /**
     * 组件操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText("拜访");

        // 客户添加
        mText_bf_tian_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = KeHuActivity.newIntent(mContext,1);
                startActivity(i);
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

        // 图片1
        mImageview_bf_phone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneXuanZhe();
                REQUEST_PHOTO = 1;
            }
        });
        // 图片2
        mImageview_bf_phone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneXuanZhe();
                REQUEST_PHOTO = 2;
            }
        });
        // 图片3
        mImageview_bf_phone3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneXuanZhe();
                REQUEST_PHOTO = 3;
            }
        });
        // 图片4
        mImageview_bf_phone4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneXuanZhe();
                REQUEST_PHOTO = 4;
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
                            if(mBaiFangGuanli.getPhont1() != null
                                    && mBaiFangGuanli.getPhont2() != null
                                    && mBaiFangGuanli.getPhont3() != null
                                    && mBaiFangGuanli.getPhont4() != null){

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
                                tiShi(mContext,"图片不能为空");
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
                            mTuPianNum++;
                            switch (mTuPianNum) {
                                case 1:
                                    mBaiFangGuanli.setShangChuanFilePath1(jsonObject.getString("path"));
                                    break;
                                case 2:
                                    mBaiFangGuanli.setShangChuanFilePath2(jsonObject.getString("path"));
                                    break;
                                case 3:
                                    mBaiFangGuanli.setShangChuanFilePath3(jsonObject.getString("path"));
                                    break;
                                case 4:
                                    mBaiFangGuanli.setShangChuanFilePath4(jsonObject.getString("path"));
                                    // 参数提交
                                    canShuTiJiao();
                                    break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if(msg.what==2){
                tiShi(mContext,msg.obj+"");
                TiJiaoNeiRongQingKong();
            }
        }
    };

    /**
     * 拜访图片提交
     */
    public void shuJiTiJiao(){

        PhoneTiJiao(mBaiFangGuanli.getPhont1());
        PhoneTiJiao(mBaiFangGuanli.getPhont2());
        PhoneTiJiao(mBaiFangGuanli.getPhont3());
        PhoneTiJiao(mBaiFangGuanli.getPhont4());
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
     * 拜访数据提交
     */
    public void canShuTiJiao(){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);

        body.addFormDataPart("men_dian_id",mBaiFangGuanli.getMenDianId());
        body.addFormDataPart("kai_shi_time",mBaiFangGuanli.getKaiShiTime());
        body.addFormDataPart("jie_shu_time",mBaiFangGuanli.getJieShuShiJian());
        body.addFormDataPart("phone1",mBaiFangGuanli.getShangChuanFilePath1());
        body.addFormDataPart("phone2",mBaiFangGuanli.getShangChuanFilePath2());
        body.addFormDataPart("phone3",mBaiFangGuanli.getShangChuanFilePath3());
        body.addFormDataPart("phone4",mBaiFangGuanli.getShangChuanFilePath4());
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

        // 图片1
        mImageview_bf_phone1.setImageDrawable(getResources().getDrawable(R.color.zhuti));
        // 图片2
        mImageview_bf_phone2.setImageDrawable(getResources().getDrawable(R.color.zhuti));
        // 图片3
        mImageview_bf_phone3.setImageDrawable(getResources().getDrawable(R.color.zhuti));
        // 图片4
        mImageview_bf_phone4.setImageDrawable(getResources().getDrawable(R.color.zhuti));

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
        switch (REQUEST_PHOTO){
            case 1:
                imageView = mImageview_bf_phone1;
                mBaiFangGuanli.setPhont1(mPhotoFile);
                break;
            case 2:
                imageView = mImageview_bf_phone2;
                mBaiFangGuanli.setPhont2(mPhotoFile);
                break;
            case 3:
                imageView = mImageview_bf_phone3;
                mBaiFangGuanli.setPhont3(mPhotoFile);
                break;
            case 4:
                imageView = mImageview_bf_phone4;
                mBaiFangGuanli.setPhont4(mPhotoFile);
                break;
        }

        PackageManager packageManager = this.getPackageManager();
        // 相机
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 文件存储地址不能为空,相机类应用不能为空
        final boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        imageView.setEnabled(canTakePhoto);
        if(canTakePhoto){
//            Uri uri = Uri.fromFile(mPhotoFile);
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

    /**
     * 定位信息回调
     */
    public void dingWeiData(){
        // 定位信息存储
        mBaiFangGuanli.setAddr(mLocationBaiDu.getAddr());
        // 存储定位信息语义化
        mBaiFangGuanli.setAddr1(mLocationBaiDu.getLocationDescribe());
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
        mBf_search_men_dian.removeAllViews();
        try {
            JSONArray jsonArray = new JSONArray(string);
            if(jsonArray.length() > 0){
                for(int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = new JSONObject();
                    TextView textView = new TextView(mContext);
                    if(is == 2){
                        textView = CreateTextvBf(jsonArray.get(i).toString(),is);
                        mBf_search_men_dian.addView(textView);
                    }else if(is == 2){

                    }
                }
            }
        }catch (JSONException e){

        }
    }

    /**
     * 创建TextView
     * @param string 内容
     * @param is 1品牌 2门店
     * @return
     */
    public TextView CreateTextvBf(final String string,final int is){
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,100);
        layoutParam.setMargins(0,10,0,0);
        textView.setLayoutParams(layoutParam);

        textView.setGravity(Gravity.CENTER_VERTICAL);

        textView.setBackground(getResources().getDrawable(R.drawable.bottom_border));

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(is == 2){
                    // 隐藏弹出
                    dialog.hide();
                    // 显示选择项
                    try {
                        JSONObject jsonObject = new JSONObject(string);
                        String idText = jsonObject.getString("id");
                        String nameText = jsonObject.getString("name");
                        String men_dian_ping_paiText = jsonObject.getString("men_dian_ping_pai");
                        String men_dian_haoText = jsonObject.getString("men_dian_hao");

                        mText_bf_gong_si_ming_cheng_value.setText(nameText);
                        // 存储
                        mBaiFangGuanli.setMenDianId(idText);
                        mBaiFangGuanli.setMenDian(nameText);
                        // 编号
                        mText_bf_gong_si_bian_hao_value.setText(men_dian_haoText);
                        mBaiFangGuanli.setMenDianHao(men_dian_haoText);
                        // 品牌
                        mText_bf_gong_si_pin_pai_value.setText(men_dian_ping_paiText);
                        mBaiFangGuanli.setPinPai(men_dian_ping_paiText);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else if(is == 1){

                }

            }
        });

        try {
            JSONObject jsonObject = new JSONObject(string);
            textView.setText(jsonObject.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return textView;

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

        switch (REQUEST_PHOTO){
            case 1:
                mImageview_bf_phone1.setImageBitmap(bitmap);
                mBaiFangGuanli.setPhont1(files);
                break;
            case 2:
                mImageview_bf_phone2.setImageBitmap(bitmap);
                mBaiFangGuanli.setPhont2(files);
                break;
            case 3:
                mImageview_bf_phone3.setImageBitmap(bitmap);
                mBaiFangGuanli.setPhont3(files);
                break;
            case 4:
                mImageview_bf_phone4.setImageBitmap(bitmap);
                mBaiFangGuanli.setPhont4(files);
                break;
        }
    }

    /**
     * 显示对应图片
     */
    private void updatePhotoView(){
        // 隐藏弹出
        File PhotoFile = null;
        switch (REQUEST_PHOTO){
            case 1:
                PhotoFile = mBaiFangGuanli.getPhont1();
                break;
            case 2:
                PhotoFile = mBaiFangGuanli.getPhont2();
                break;
            case 3:
                PhotoFile = mBaiFangGuanli.getPhont3();
                break;
            case 4:
                PhotoFile = mBaiFangGuanli.getPhont4();
                break;
        }

        if(PhotoFile != null){
            // 压缩图片
            String pathPhoto = imgYaSuo(PhotoFile.getPath(), Config.XunCanImgWidth,Config.XunCanImgHeight);
            File files = new File(pathPhoto);

            // 显示图片
            Bitmap bitmap = PictureUtils.getScaledBitmap(files.getPath(),this);

            switch (REQUEST_PHOTO){
                case 1:
                    mImageview_bf_phone1.setImageBitmap(bitmap);
                    mBaiFangGuanli.setPhont1(files);
                    break;
                case 2:
                    mImageview_bf_phone2.setImageBitmap(bitmap);
                    mBaiFangGuanli.setPhont2(files);
                    break;
                case 3:
                    mImageview_bf_phone3.setImageBitmap(bitmap);
                    mBaiFangGuanli.setPhont3(files);
                    break;
                case 4:
                    mImageview_bf_phone4.setImageBitmap(bitmap);
                    mBaiFangGuanli.setPhont4(files);
                    break;
            }
        }
    }
}
