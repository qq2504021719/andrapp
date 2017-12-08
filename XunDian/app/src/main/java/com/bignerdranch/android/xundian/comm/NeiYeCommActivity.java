package com.bignerdranch.android.xundian.comm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Observable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.xundian.LoginActivity;
import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.TongZhiZhongXinFragment;
import com.bignerdranch.android.xundian.model.LoginModel;
import com.bignerdranch.android.xundian.xundianguanli.XunDianGuanLiActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/12.
 */

public class NeiYeCommActivity extends AppCompatActivity{

    public TextView mTitle_nei_ye = null; // 设置显示标题

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    public Context mContext = null;

    // Token
    public String mToken = null;

    // LoginModel 登录模型
    public static LoginModel mLoginModel = null;
    // 登录对象
    public static Login mLogin = null;

    // 开启线程
    public static Thread mThread = null;

    // json转换
    public Gson mGson = new Gson();


    // 门店品牌数据
    public String mMengDianPingpaiJsonData = "";
    public String[] mMengDianPingPaiData;
    public HashMap<String,String> mMengDianHashMapData;


    // 门店数据
    public String mMengDianJsonData = "";
    public String[] mMengDianData =new String[0];

    public String mSearchString = ""; // 用户输入
    public String mMen_Dian_ping_pai = ""; // 门店品牌

    // 门店搜索URl
    public String mMenDianSearchURL = Config.URL+"/app/menDian";
    // 品牌搜索URl
    public String mPinPaiSearchURL = Config.URL+"/app/get_ping_pai";

    // dialog,加载
    public Dialog mWeiboDialog;

    /**
     * 默认连接数据库
     * @param context
     */
    public void setToken(Context context){
        // 登录数据库连接
        mLoginModel = LoginModel.get(context);
        // Token查询,赋值
        mLogin = mLoginModel.getLogin(1);
        mToken = mLogin.getToken();
    }

    // 回调函数存储变量
    public Callbacks mCallbacks;


    /**
     * 实现回调接口
     */
    public interface Callbacks{
        void shuJuHuiDiao(String string,int is);
    }


    /**
     * 获取当前时间
     * @return 当前时间y-m-d h-i-s
     */
    public String getDangQianTime(){
        SimpleDateFormat simpleDateFormats =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Calendar calendar=Calendar.getInstance(Locale.CHINA);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        //当前时间，貌似多余，其实是为了所有可能的系统一致
        calendar.setTimeInMillis(System.currentTimeMillis());
        return simpleDateFormats.format(calendar.getTime());
    }

    /**
     * 掉此方法输入所要转换的时间输入例如（"2014-06-14 16-09-00"）返回时间戳
     *
     * @param time
     * @return
     */
    public String dataTime(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 点击返回
     *
     * @param v
     */
    public void DianJiFanHui(View v) {
        finish();
    }

    /**
     * 隐藏键盘
     * 这种方法实现起来比较麻烦，解决思路与iOS中的事件分发机制是类似，对于处理隐藏事件比较清晰，通过层层事件分发，然后判断是否在需要屏蔽的区域。
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 隐藏键盘
     *
     * @param v
     * @param event
     * @return
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * {"例子","例子1"}
     * 将 jsonstring转为String[] 数组
     *
     * @param string
     * @return
     */
    public String[] ChuLiJson(String string) {
        try {
            JSONArray jsonArray = new JSONArray(string);

            String[] strings = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                strings[i] = String.valueOf(jsonArray.get(i));
            }
            return strings;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    /**
     * 提示
     *
     * @param context
     */
    public static void tiShi(Context context, String string) {
        Toast mToast = null;
        if (mToast == null) {
            mToast = Toast.makeText(context, "",
                    Toast.LENGTH_LONG);
            LinearLayout layout = (LinearLayout) mToast.getView();
            TextView tv = (TextView) layout.getChildAt(0);
            tv.setTextSize(20);
        }
        mToast.setGravity(Gravity.BOTTOM, 0, 10);
        mToast.setText(string);
        mToast.show();
    }


    /**
     * 检查网络是否完全连接 true 连接  false 没有连接
     * @return
     */
    public boolean isNetworkAvailableAndConnected(Context context){
        ConnectivityManager cm =(ConnectivityManager)context.getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    /**
     * 返回指向某个具体位置的File对象
     */
    public File getPhotoFile(String string){
        File externalFilesDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File externalFilesDir = this.getExternalCacheDir(Environment.DIRECTORY_PICTURES);

//        File externalFilesDir = this.getExternalFilesDir("android.support.v4.content.FileProvider");
        // 确认外部存储是否可用,如果不可用,返回null,
        if(externalFilesDir == null){
            return null;
        }
        return new File(externalFilesDir,string);
    }

    /**
     * 文件获取获取方法
     */
    public String getPhotoFilename(){
        return "IMG_"+ LoginActivity.getTime()+".jpg";
    }

    /**
     *
     * 图片压缩方法
     * path 图片存储路径
     * destWidth 压缩成多宽
     * destHeight 压缩成多高
     * @return 返回图片路径
     */
    public String imgYaSuo(String path,int destWidth,int destHeight){
        // 压缩图片
        Bitmap bitmap = PictureUtils.getScaledBitmap(path,destWidth,destHeight);
        File f = getPhotoFile(getPhotoFilename());
        try {
            // 存储到本地
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f.getPath();
    }




    /**
     * 将string转为JSON对象,匹配string1，返回对应的id
     * @param Jsonstring
     * @param string
     * @return
     */
    public int ChanKanId(String Jsonstring,String string){
        try {
            JSONArray jsonArray = new JSONArray(Jsonstring);
            if(jsonArray.length() > 0){
                for(int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                    if(jsonObject.getString("name").equals(string)){
                        return Integer.valueOf(jsonObject.getString("id"));
                    }
                }
            }
            return 0;
        }catch (JSONException e){

        }
        return 0;
    }

    /**
     * 将string转为JSON对象,匹配string1，返回对应的id
     * @param Jsonstring
     * @param string
     * @return
     */
    public String[] ChanKanIds(String Jsonstring,String string){
        String[] strings = new String[7];
        strings[0] = "0";
        strings[2] = "0";
        try {
            JSONArray jsonArray = new JSONArray(Jsonstring);
            if(jsonArray.length() > 0){
                for(int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                    String bjString = jsonObject.getString("men_dian_hao")+"-"+jsonObject.getString("name");
                    if(bjString.equals(string)){
                        strings[0] = jsonObject.getString("id");
                        strings[1] = jsonObject.getString("name");
                        strings[2] = jsonObject.getString("men_dian_hao");
                        strings[3] = jsonObject.getString("men_dian_ping_pai");

                        if(jsonObject.getString("fan_wei").trim().length() == 0 || jsonObject.getString("fan_wei").equals("null")){
                            strings[4] = "0";
                        }else{
                            strings[4] = jsonObject.getString("fan_wei");
                        }

                        if(jsonObject.getString("md_lat").trim().length() == 0 || jsonObject.getString("md_lat").equals("null")){
                            strings[5] = "0.0";
                        }else{
                            strings[5] = jsonObject.getString("md_lat");
                        }

                        if(jsonObject.getString("md_lng").trim().length() == 0 || jsonObject.getString("md_lng").equals("null")){
                            strings[6] = "0.0";
                        }else{
                            strings[6] = jsonObject.getString("md_lng");
                        }
//                        return Integer.valueOf(jsonObject.getString("id"));
                    }
                }
            }
            return strings;
        }catch (JSONException e){

        }
        return strings;
    }

    /**
     * 将string转为JSON对象,匹配string1，返回对应的id
     * @param Jsonstring
     * @param string
     * @param key 查询的key
     * @return
     */
    public String ChanKanKey(String Jsonstring,String string,String key){
        try {
            JSONArray jsonArray = new JSONArray(Jsonstring);
            if(jsonArray.length() > 0){
                for(int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                    if(jsonObject.getString("name").equals(string)){
                        return jsonObject.getString(key);
                    }
                }
            }
            return "";
        }catch (JSONException e){

        }
        return "";
    }



    /**
     * 将 string的值设置到对应strings中
     * @param string
     * @param is 1 品牌 2门店
     */
    public void setData(String string,int is){
        try {
            JSONArray jsonArray = new JSONArray(string);
            if(jsonArray.length() > 0){
                if(is == 1){
                    mMengDianPingPaiData = new String[jsonArray.length()];
                }else if(is == 2){
                    mMengDianData = new String[jsonArray.length()];
                }

                for(int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                    if(is == 1){
                        mMengDianPingPaiData[i] = jsonObject.getString("name");
//                        mMengDianHashMapData
                    }else if(is == 2){
                        mMengDianData[i] = jsonObject.getString("men_dian_hao")+"-"+jsonObject.getString("name");
                    }
                }
            }else{
                if(is == 1){
                    mMengDianPingPaiData = new String[0];
                }else if(is == 2){
                    mMengDianData= new String[0];
                }
            }
        }catch (JSONException e){

        }
    }


    /**
     * Handler
     */
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            // 关闭loading
            WeiboDialogUtils.closeDialog(mWeiboDialog);
            /**
             * 请求回调
             */
            if(msg.what==1){
                // 门店参数请求回调
                String string = msg.obj.toString();
                mCallbacks.shuJuHuiDiao(string,2);
            }else if(msg.what==2){
                // 品牌参数请求回调
                String string = msg.obj.toString();
                mMengDianPingpaiJsonData = string;
                mCallbacks.shuJuHuiDiao(string,1);
            }
        }
    };

    /**
     * 门店搜索
     */
    public void menDianSearch(){
        if(mToken != null){
            final OkHttpClient client = new OkHttpClient();

            String str = "";
            String str1 = "";
            if(!mSearchString.isEmpty()) str = mSearchString;

            if(!mMen_Dian_ping_pai.isEmpty()) str1 = mMen_Dian_ping_pai;
            //3, 发起新的请求,获取返回信息
            RequestBody body = new FormBody.Builder()
                    .add("name",str)
                    .add("pin_pai",str1)
                    .build();
            final Request request = new Request.Builder()
                    .addHeader("Authorization","Bearer "+mToken)
                    .url(mMenDianSearchURL)
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
     * 品牌搜索
     */
    public void pinPaiSearch(){
        if(mToken != null){
            final OkHttpClient client = new OkHttpClient();
            //3, 发起新的请求,获取返回信息
            RequestBody body = new FormBody.Builder().build();
            final Request request = new Request.Builder()
                    .addHeader("Authorization","Bearer "+mToken)
                    .url(mPinPaiSearchURL)
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
                        mHandler.obtainMessage(2, response.body().string()).sendToTarget();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            mThread.start();
        }
    }

    /**
     * 根据str截取字符串string返回字符串
     * @param str
     * @param string
     * @return
     */
    public String strSplit(String str,String string){
        String s = new String(string);
        String a[] = s.split(str);

        String returnStr = "";

        for (int i = 0;i<a.length;i++){
            returnStr += a[i];
        }

        return returnStr;
    }

    /**
     * 根据str截取字符串string数组
     * @param str
     * @param string
     * @return
     */
    public String[] strSplitArray(String str,String string){
        String s = new String(string);
        String a[] = s.split(str);

        return a;
    }

    /**
     * loading 浮层
     *
     * @param logingString 提示文字
     */
    public void LoadingStringEdit(String logingString){
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(mContext,logingString);
    }

    /**
     * 根据两点的经度纬度计算出距离
     * @param lon1
     * @param lat1
     * @param lon2
     * @param lat2
     * @return
     */
    public double GetShortDistance(double lon1, double lat1, double lon2, double lat2)
    {
        double DEF_PI = 3.14159265359; // PI
        double DEF_2PI= 6.28318530712; // 2*PI
        double DEF_PI180= 0.01745329252; // PI/180.0
        double DEF_R =6370693.5; // radius of earth
        double ew1, ns1, ew2, ns2;
        double dx, dy, dew;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 经度差
        dew = ew1 - ew2;
        // 若跨东经和西经180 度，进行调整
        if (dew > DEF_PI)
            dew = DEF_2PI - dew;
        else if (dew < -DEF_PI)
            dew = DEF_2PI + dew;
        dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
        dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
        // 勾股定理求斜边长
        distance = Math.sqrt(dx * dx + dy * dy);
        return distance;
    }

    /**
     * @author jerry.chen 2017-10-12
     * @param dateStr
     * @return 获取当前是星期几
     */
    public String getCurrentWeekOfMonth(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            String[] day_of_week = {"周日","周一","周二","周三","周四","周五","周六"};
            c.setTime(format.parse(dateStr));
            int dayForWeek = 0;

            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            return day_of_week[dayForWeek];

        } catch (ParseException e) {

            e.printStackTrace();
        }
        return "";
    }

}
