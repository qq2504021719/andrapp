package com.bignerdranch.android.xundian.comm;

import android.content.Context;
import android.database.Observable;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.model.LoginModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/9/12.
 */

public class NeiYeCommActivity extends AppCompatActivity {

    public TextView mTitle_nei_ye; // 设置显示标题

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    public Context mContext;

    // Token
    public String mToken;

    // LoginModel 登录模型
    public static LoginModel mLoginModel;
    // 登录对象
    public static Login mLogin;

    // 开启线程
    public static Thread mThread;

    // json转换
    public Gson mGson = new Gson();



    public void setToken(Context context){
        // 登录数据库连接
        mLoginModel = LoginModel.get(context);
        // Token查询,赋值
        mLogin = mLoginModel.getLogin(1);
        mToken = mLogin.getToken();
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


}
