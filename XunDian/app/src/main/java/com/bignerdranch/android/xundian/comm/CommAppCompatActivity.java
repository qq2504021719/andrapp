package com.bignerdranch.android.xundian.comm;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bignerdranch.android.xundian.model.LoginModel;

/**
 * Created by Administrator on 2017/9/11.
 */

public class CommAppCompatActivity extends AppCompatActivity {

    private LoginModel mLoginModel;

    private Login mLogin;

    public CommAppCompatActivity(){
        mLoginModel = LoginModel.get(this);
        mLogin = mLoginModel.getLogin(1);
        Log.i("打印",mLogin.getToken());
    }
}
