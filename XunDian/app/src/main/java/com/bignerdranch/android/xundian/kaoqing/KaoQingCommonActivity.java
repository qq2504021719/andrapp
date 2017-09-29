package com.bignerdranch.android.xundian.kaoqing;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.xundian.LoginActivity;
import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.comm.WeiboDialogUtils;
import com.bignerdranch.android.xundian.model.LoginModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrator on 2017/9/26.
 */

public class KaoQingCommonActivity extends AppCompatActivity {
    public TextView mTitle_nei_ye = null; // 设置显示标题
    public Context mContext = null;

    // Token
    public String mToken = null;

    // LoginModel 登录模型
    public static LoginModel mLoginModel = null;
    // 登录对象
    public static Login mLogin = null;

    // 开启线程
    public static Thread mThread = null;

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

    /**
     * 点击返回
     *
     * @param v
     */
    public void DianJiFanHui(View v) {
        finish();
    }

    /**
     * 获取当前时间
     * @return 当前时间y-m-d h-i-s
     */
    public String getDangQianTime(int is){
        SimpleDateFormat simpleDateFormats = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        if(is == 1){
             simpleDateFormats = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        }else if(is == 2){
             simpleDateFormats =new SimpleDateFormat("yyyy-MM-dd hh:ii:ss", Locale.CHINA);
        }
        Calendar calendar=Calendar.getInstance(Locale.CHINA);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        //当前时间，貌似多余，其实是为了所有可能的系统一致
        calendar.setTimeInMillis(System.currentTimeMillis());
        return simpleDateFormats.format(calendar.getTime());
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
     * loading 浮层
     *
     * @param logingString 提示文字
     */
    public void LoadingStringEdit(String logingString){
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(mContext,logingString);
    }
}
