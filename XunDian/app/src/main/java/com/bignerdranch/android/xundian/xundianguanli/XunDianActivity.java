package com.bignerdranch.android.xundian.xundianguanli;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.comm.NeiYeCommActivity;
import com.bignerdranch.android.xundian.comm.PictureUtils;
import com.bignerdranch.android.xundian.comm.XunDianCanShu;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */

public class XunDianActivity extends NeiYeCommActivity {

    private static final String TAG = "XunDianActivity";

    // 请求代码常量 相机拍照
    private static final int REQUEST_PHOTO = 3;

    // 巡店定位信息,店铺id
    public String mXunDian;

    public String mXunDianJSONData = "[{\"id\":1,\"name\":\"\\u996e\\u6599\\u6392\\u9762\\u62cd\\u7167\",\"tian_xie_fang_shi\":\"\\u6587\\u672c\",\"is_pai_zhao\":\"\\u662f\",\"xuan_ze_qi\":\"\",\"is_bi_tian\":1},{\"id\":2,\"name\":\"\\u996e\\u6599\\u6570\\u91cf\",\"tian_xie_fang_shi\":\"\\u6570\\u5b57\",\"is_pai_zhao\":\"\\u5426\",\"xuan_ze_qi\":\"\",\"is_bi_tian\":1},{\"id\":3,\"name\":\"\\u65e0\\u6cd5\\u8ba2\\u8d27\\u5546\\u54c1\",\"tian_xie_fang_shi\":\"\\u9009\\u62e9\\u5668\",\"is_pai_zhao\":\"\\u5426\",\"xuan_ze_qi\":[\"\\u6e20\\u9053\\u65e0\\u8d27\",\"\\u5176\\u4ed6\\u539f\\u56e0\"],\"is_bi_tian\":1},{\"id\":4,\"name\":\"\\u996e\\u6599\\u51b0\\u67dc\\u62cd\\u7167\",\"tian_xie_fang_shi\":\"\\u6587\\u672c\",\"is_pai_zhao\":\"\\u662f\",\"xuan_ze_qi\":\"\",\"is_bi_tian\":1},{\"id\":5,\"name\":\"\\u9175\\u6bcd\\u9762\\u5305\\u5e93\\u5b58\",\"tian_xie_fang_shi\":\"\\u6570\\u5b57\",\"is_pai_zhao\":\"\\u662f\",\"xuan_ze_qi\":\"\",\"is_bi_tian\":0},{\"id\":6,\"name\":\"\\u9999\\u98d8\\u98d8\\u5355\\u676f\\u5e93\\u5b58\",\"tian_xie_fang_shi\":\"\\u6570\\u5b57\",\"is_pai_zhao\":\"\\u5426\",\"xuan_ze_qi\":\"\",\"is_bi_tian\":1},{\"id\":7,\"name\":\"\\u767e\\u4e8b\\u7f3a\\u8d27\\u65e5\\u671f\",\"tian_xie_fang_shi\":\"\\u65e5\\u671f\",\"is_pai_zhao\":\"\\u5426\",\"xuan_ze_qi\":\"\",\"is_bi_tian\":1}]";

    public XunDianCanShu mXunDianCanShu;

    // 巡店参数list
    public JSONArray mXunDianJson;

    // 巡店数据对象存储
    public List<XunDianCanShu> mXunDianCanShus;

    // LinearLayout 公共样式
    public static LinearLayout.LayoutParams mLayoutParam;

    private TextView mRi_qi_textview;
    private TextView mSelect_textview;
    // 图片点击
    private ImageView mXun_dian_pai_imageview;
    // 图片显示
    private ImageView mXun_dian_show_imageview;
    // 图片存储位置
    private File mPhotoFile;
    // 弹出选择器
    private AlertDialog alertDialog1;

    // LinearLayout
    public LinearLayout mXun_dian_bounce_linearlayout;
    // 保存
    private Button mXun_dian_bc_button;
    // 当前下标
    public final int mDnagqian = 0;

    public static Intent newIntent(Context packageContext, String string){
        Intent i = new Intent(packageContext,XunDianActivity.class);
        i.putExtra(TAG,string);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.xun_dian);

        mXunDian = getIntent().getStringExtra(TAG);

        // 组件初始化
        ZhuJianInit();

        // 组件操作
        ZhuJianCaoZhuo();

        // 位置信息,店铺基本信息
//        Log.i("巡店",mXunDian);

        // 添加组件
        addView(mXunDianJSONData);
    }

    public void addView(String jsonString){
        mXun_dian_bounce_linearlayout = (LinearLayout)findViewById(R.id.xun_dian_bounce_linearlayout);

        mLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParam.setMargins(0,15,0,0);

        mXunDianCanShus = new ArrayList<XunDianCanShu>();

        try {
            mXunDianJson = new JSONArray(jsonString);
            for(int i = 0;i<mXunDianJson.length();i++){


                mXunDianCanShu = new XunDianCanShu();
                // id
                mXunDianCanShu.setId(Integer.valueOf(mXunDianJson.getJSONObject(i).getString("id")));
                // 名称
                mXunDianCanShu.setName(mXunDianJson.getJSONObject(i).getString("name"));
                // 输入类型
                mXunDianCanShu.setTian_xie_fang_shi(mXunDianJson.getJSONObject(i).getString("tian_xie_fang_shi"));
                // 是否拍照
                mXunDianCanShu.setIs_pai_zhao(mXunDianJson.getJSONObject(i).getString("is_pai_zhao"));
                // 选择项目
                mXunDianCanShu.setXuan_ze_qi(mXunDianJson.getJSONObject(i).getString("xuan_ze_qi"));
                // 是否必填
                mXunDianCanShu.setIs_bi_tian(Integer.valueOf(mXunDianJson.getJSONObject(i).getString("is_bi_tian")));

                // 创建标题
                CreateBiaoTi(mXunDianCanShu.getName(),mXunDianCanShu.getIs_bi_tian());

                // 创建对应view
                CreateView(mXunDianCanShu.getTian_xie_fang_shi(),mXunDianCanShu.getIs_pai_zhao(),i);

                // 添加到List对象
                mXunDianCanShus.add(mXunDianCanShu);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建标题
     * @param title 标题
     * @param isXin 1 是显示*号 0 不显示*号
     *
     */
    public void CreateBiaoTi(String title,int isXin){
        LinearLayout ll = new LinearLayout(this);
        // 设置宽高
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,50,0,0);
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
        titleTv.setTextSize(16);
        titleTv.setText(title);
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
        // 唯一id
        final int editTextId = XiaBiao;
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
        textView.setText(R.string.xun_ze_qi);
        // 唯一id
        final int editTextId = XiaBiao;

        String[] stringZhuan = new String[]{"无"};
        // 选择项数据转换
        if(mXunDianCanShu.getXuan_ze_qi() != null){
            stringZhuan = ChuLiJson(mXunDianCanShu.getXuan_ze_qi());
        }
        final String[] strings = stringZhuan;
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
                            }
                            alertDialog1.dismiss();
                        }
                    });
                    alertDialog1 = alertBuilder.create();
                    alertDialog1.show();
                }

            }
        });
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
        // 设置默认显示
        textView.setText(R.string.ri_qi);
        // 唯一id
        final int editTextId = XiaBiao;
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
                        }
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
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
        FrameLayout frameLayout = new FrameLayout(this);
        final ImageView imageViewShow = new ImageView(this);
        final ImageView imageViewDian = new ImageView(this);
        LinearLayout.LayoutParams layoutParamls = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParaml = new LinearLayout.LayoutParams(250,300);
        layoutParamls.setMargins(0,20,0,0);
        linearLayout.setLayoutParams(layoutParamls);

        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        // 外边距
        // 添加父组件
        ll.addView(linearLayout);


        linearLayout1.setLayoutParams(layoutParaml);
        // 设置背景
        linearLayout1.setBackgroundResource(R.drawable.xun_dian_img_border);
        linearLayout1.setGravity(Gravity.CENTER);
        // 添加父组件
        linearLayout.addView(linearLayout1);


        frameLayout.setLayoutParams(layoutParaml);
        // 添加父组件
        linearLayout1.addView(frameLayout);

        // 图片显示组件
        LinearLayout.LayoutParams layoutParamI = new LinearLayout.LayoutParams(250,300);
        imageViewShow.setLayoutParams(layoutParamI);
        // 添加父组件
        frameLayout.addView(imageViewShow);

        // 图片加号显示
        imageViewDian.setLayoutParams(layoutParaml);
        imageViewDian.setImageResource(R.drawable.xun_dian_img_add);
        // 添加父组件
        frameLayout.addView(imageViewDian);

        // 唯一id
        final int editTextId = XiaBiao;

        PackageManager packageManager = this.getPackageManager();
        // 相机
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 获取文件存储地址
        mPhotoFile = getPhotoFile();

        // 文件存储地址不能为空,相机类应用不能为空
        final boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        mXun_dian_pai_imageview.setEnabled(canTakePhoto);
        if(canTakePhoto){
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        }

        imageViewShow.setOnClickListener(new View.OnClickListener() {
            final int id = editTextId;
            @Override
            public void onClick(View view) {
                startActivityForResult(captureImage,REQUEST_PHOTO);
                if(canTakePhoto){
                    mXunDianCanShus.get(id).setImageView(imageViewShow);
                    mXunDianCanShus.get(id).setPhotoFile(mPhotoFile);
                }
            }
        });
    }

    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        // 布局
        mTitle_nei_ye = (TextView)findViewById(R.id.title_nei_ye);
        // 保存
        mXun_dian_bc_button = (Button)findViewById(R.id.xun_dian_bc_button);
//        mRi_qi_textview = (TextView)findViewById(R.id.ri_qi_textview);
//        mSelect_textview = (TextView)findViewById(R.id.select_textview);
//        mXun_dian_pai_imageview = (ImageView)findViewById(R.id.xun_dian_pai_imageview);
//        mXun_dian_show_imageview = (ImageView)findViewById(R.id.xun_dian_show_imageview);
    }


    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        // 设置标题
        mTitle_nei_ye.setText(R.string.gong_zuo_zhong_xin_xun_dian_guan_li);
        // 保存
        mXun_dian_bc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0;i<mXunDianCanShus.size();i++){
                    if(mXunDianCanShus.get(i).getValue() != null){
                        Log.i("巡店",mXunDianCanShus.get(i).getValue());
                    }

                }
            }
        });
        // 日期选择
//        mRi_qi_textview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDatePickerDialog();
//            }
//        });
//
//        // 选择器
//        mSelect_textview.setOnClickListener(new View.OnClickListener() {
//            String[] strings = {"供应商无货","其他原因"};
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(XunDianActivity.this);
//                alertBuilder.setItems(strings, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int index) {
//                        // 清除搜索框焦点
//                        mSelect_textview.setText(strings[index]);
//                        // 更新用户选择门店品牌
//                        alertDialog1.dismiss();
//                    }
//                });
//                alertDialog1 = alertBuilder.create();
//                alertDialog1.show();
//            }
//        });
//
//        PackageManager packageManager = this.getPackageManager();
//        // 相机
//        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        // 文件存储地址不能为空,相机类应用不能为空
//        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
//        mXun_dian_pai_imageview.setEnabled(canTakePhoto);
//        if(canTakePhoto){
//            Uri uri = Uri.fromFile(mPhotoFile);
//            captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
//        }
//        mXun_dian_pai_imageview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 启动相机应用
//                startActivityForResult(captureImage,REQUEST_PHOTO);
//            }
//        });

    }

    /**
     * 返回指向某个具体位置的File对象
     */
    public File getPhotoFile(){
        File externalFilesDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // 确认外部存储是否可用,如果不可用,返回null
        if(externalFilesDir == null){
            return null;
        }
        return new File(externalFilesDir,getPhotoFilename());
    }

    /**
     * 文件获取获取方法
     */
    public String getPhotoFilename(){
        return "IMG_"+ LoginActivity.getTime()+".jpg";
    }



    /**
     * 展示日期选择对话框
     */
    public void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(XunDianActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                mRi_qi_textview.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
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
     * 刷新图片
     */
    private void updatePhotoView(){

        if(mPhotoFile == null || !mPhotoFile.exists()){
            mXun_dian_show_imageview.setImageDrawable(null);
        }else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),this);
            mXun_dian_show_imageview.setImageBitmap(bitmap);
        }
    }


}
