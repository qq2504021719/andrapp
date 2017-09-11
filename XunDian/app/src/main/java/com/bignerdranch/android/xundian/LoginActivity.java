package com.bignerdranch.android.xundian;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.model.LoginModel;

import java.util.Date;

/**
 * Created by Administrator on 2017/9/11.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String EXTRA_VIEW_ID = "com.bignerdranch.android.xundian.LoginActivity";

    private EditText mZhang_hao_edittext;
    private EditText mMi_ma_edittext;
    private CheckBox mJi_zhu_checkbox;
    private Button mDeng_lu_button;

    public static String mZhangHao;
    private static String mMima;
    private static Context mContext;

    private LoginModel mLoginModel;

    private Login mLogin;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mContext = this;
        // 组件初始化
        ZhuJianInit();
        // 组件操作, 操作
        ZhuJianCaoZhuo();

        // 查询token
        getData();

    }

    public void getData(){


        mLoginModel = LoginModel.get(this);

        Login login = new Login();
        login.setId(1);
        login.setToken("sdfsd54fw6ef8974sdf");
        login.setTime(getTime());

        mLoginModel.addLogin(login);

        mLogin = mLoginModel.getLogin(1);
        if(mLogin != null){
            Log.i("打印",""+mLogin.getToken());
        }else{
            Log.i("打印","暂无数据1");
        }
    }

    /**
     * 获取当前时间戳
     * @return
     */
    public String getTime(){
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
        mDeng_lu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mZhangHao = mZhang_hao_edittext.getText().toString();
                mMima = mMi_ma_edittext.getText().toString();
                dengLuyanZheng();
            }
        });
    }

    public static void dengLuyanZheng(){
        if(mZhangHao == null || mMima == null){

        }else{
            tiShi(mContext);
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
