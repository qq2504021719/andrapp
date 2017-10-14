package com.bignerdranch.android.xundian.kaoqing;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import com.bignerdranch.android.xundian.xundianguanli.XunDianActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;


/**
 * Created by Administrator on 2017/10/13.
 */

public class BaiFangGuanLiActivity extends KaoQingCommonActivity implements KaoQingCommonActivity.Callbacks{
    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.KaoQingJiLuActivity";

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
    }


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

    }

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
        dialog.dismiss();
        // 图片弹出销毁
        mDialogPhone.dismiss();
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
        }
    }

    /**
     * 显示对应图片
     */
    private void updatePhotoView(){
        Log.i("巡店",REQUEST_PHOTO+"");
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
                    break;
                case 2:
                    mImageview_bf_phone2.setImageBitmap(bitmap);
                    break;
                case 3:
                    mImageview_bf_phone3.setImageBitmap(bitmap);
                    break;
                case 4:
                    mImageview_bf_phone4.setImageBitmap(bitmap);
                    break;
            }
        }
    }
}
