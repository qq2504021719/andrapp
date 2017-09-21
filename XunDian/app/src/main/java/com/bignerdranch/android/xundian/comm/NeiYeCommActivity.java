package com.bignerdranch.android.xundian.comm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Observable;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.List;
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
    public String mMengDianPingpaiJsonData = "[{\"id\":2,\"name\":\"\\u4f0d\\u7f18\"},{\"id\":5,\"name\":\"\\u53ef\\u7684\\u4fbf\\u5229\"},{\"id\":10,\"name\":\"\\u597d\\u5fb7\\u4fbf\\u5229\"},{\"id\":12,\"name\":\"\\u7f57\\u68ee\\u4fbf\\u5229\"}]";
    public String[] mMengDianPingPaiData;


    // 门店数据
    public String mMengDianJsonData = "";
    public String[] mMengDianData =new String[0];

    public String mSearchString = ""; // 用户输入
    public String mMen_Dian_ping_pai = "0"; // 门店品牌

    // 门店搜索URl
    public String mMenDianSearchURL = Config.URL+"/app/menDian";

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
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
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
        // 确认外部存储是否可用,如果不可用,返回null
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
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
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
                    }else if(is == 2){
                        mMengDianData[i] = jsonObject.getString("name");
                    }
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
            WeiboDialogUtils.closeDialog(mWeiboDialog);
            /**
             * 请求回调
             */
            if(msg.what==1){
                // 参数请求回调
                String string = msg.obj.toString();
                if(string != null){
                    mCallbacks.shuJuHuiDiao(string,2);
                }
            }
        }
    };

    /**
     * 门店搜索
     */
    public void menDianSearch(){
        if(mToken != null){
            final OkHttpClient client = new OkHttpClient();
            //3, 发起新的请求,获取返回信息
            RequestBody body = new FormBody.Builder()
                    .add("name",mSearchString)
                    .add("pin_pai",mMen_Dian_ping_pai)
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

}
