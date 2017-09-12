package com.bignerdranch.android.xundian;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.bignerdranch.android.xundian.comm.AtyContainer;
import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.model.LoginModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/11.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String EXTRA_VIEW_ID = "com.bignerdranch.android.xundian.LoginActivity";

    private static final String CLIENT_ID = "2";
    private static final String CLIENT_SECRET = "S4rOJxiKfd4Ch3SuOPaq6ZBNTMg9ixuoehEMVEsg";
    private static final String MURL = "http://xd.trc-demo.com:3002/oauth/token";

    public EditText mZhang_hao_edittext;
    public EditText mMi_ma_edittext;
    public CheckBox mJi_zhu_checkbox;
    public Button mDeng_lu_button;

    // 账号
    public static String mZhangHao;
    // 密码
    private static String mMima;
    // 是否保存账号
    private static int mIsBaoCun=2;
    // this
    public static LoginActivity mContext;
    // LoginModel 登录模型
    private static LoginModel mLoginModel;
    // 登录对象
    private static Login mLogin;

    // 开启线程
    private static Thread mThread;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mContext = this;
        // 组件初始化
        ZhuJianInit();
        // 组件操作, 操作
        ZhuJianCaoZhuo();

        // 销毁其余容器
        AtyContainer.finishAllActivity();

        // 查询token
        getData();

    }

    public static Intent newIntent(Context packageContext, int viewId){
        Intent i = new Intent(packageContext,LoginActivity.class);
        i.putExtra(EXTRA_VIEW_ID,viewId);
        return i;
    }


    /**
     * Handler
     */
    public static final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            /**
             * 登录回调
             */
            if(msg.what==1){
                LoginActivity loginActivity = new LoginActivity();
                DengLuAdd((String) msg.obj);
            }

        }
    };

    /**
     * 验证是否登录,验证成功跳转
     */
    public static void getData(){
        mLoginModel = LoginModel.get(mContext);
//        mLoginModel.deleteLogin(1);
        mLogin = mLoginModel.getLogin(1);
        if(mLogin != null){
            // 存储容器
            AtyContainer.addActivity(mContext);
            Intent i = MainPageActivity.newIntent(mContext,1);
            mContext.startActivity(i);
//            Log.i("登录",mLogin.getZhangHao()+"|"+mLogin.getToken());
        }else{
//            Log.i("登录","暂无数据");
        }
    }

    /**
     * 获取当前时间戳
     * @return
     */
    public static String getTime(){
        Date currentdate = new java.util.Date();//当前时间
        String  str =String.valueOf(currentdate.getTime());
        return str;

    }

    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mZhang_hao_edittext = (EditText)findViewById(R.id.zhang_hao_edittext);
        mMi_ma_edittext = (EditText)findViewById(R.id.mi_ma_edittext);
        mJi_zhu_checkbox = (CheckBox)findViewById(R.id.ji_zhu_checkbox);
        mDeng_lu_button = (Button)findViewById(R.id.deng_lu_button);


    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        // 登录点击
        mDeng_lu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mZhangHao = mZhang_hao_edittext.getText().toString();
                mMima = mMi_ma_edittext.getText().toString();
                if(mJi_zhu_checkbox.isChecked()){
                    mIsBaoCun = 1;
                }else{
                    mIsBaoCun = 2;
                }

                dengLuyanZheng();
            }
        });
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    /**
     * 登录请求
     */
    public static void dengLuyanZheng(){
        if(!mZhangHao.isEmpty() && !mMima.isEmpty()){

            final OkHttpClient client = new OkHttpClient();
            //3, 发起新的请求,获取返回信息
            RequestBody body = new FormBody.Builder()
                    .add("username", mZhangHao)//添加键值对
                    .add("password", mMima)
                    .add("client_id", CLIENT_ID)
                    .add("client_secret", CLIENT_SECRET)
                    .add("grant_type", "password")
                    .build();
            final Request request = new Request.Builder()
                    .url(MURL)
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
        }else{
            tiShi(mContext);
        }
    }

    /**
     * 判断用户是否登录成功,成功写入数据库,并跳转
     * @param string
     */
    public static void DengLuAdd(String string){
        JSONObject jsonObject;
        try {
            // 解析json数据
            jsonObject = new JSONObject(string);
            int i = 0;
            Iterator inter =  jsonObject.keys();
            while(inter.hasNext()){
                String a = (String)inter.next();
                if(a.equals("access_token")){
                    i = 1;
                }
            }
            if(i == 1){
                // 添加入库
                Login login = new Login();
                login.setId(1);
                login.setToken(jsonObject.getString("access_token"));
                login.setTime(getTime());
                login.setZhangHao(mZhangHao);
                login.setMiMa(mMima);
                login.setIsBaoCun(mIsBaoCun);
                mLoginModel.addLogin(login);
                // 验证跳转
                getData();
            }else{
                tiShi(mContext);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 提示
     * @param context
     */
    public static void tiShi(Context context){
        Toast.makeText(context,R.string.zhang_mi_bu, Toast.LENGTH_SHORT).show();
    }

}
