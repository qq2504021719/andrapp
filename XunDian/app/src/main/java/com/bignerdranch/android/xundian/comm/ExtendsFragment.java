package com.bignerdranch.android.xundian.comm;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.bignerdranch.android.xundian.model.LoginModel;

/**
 * Created by Administrator on 2017/9/23.
 */

public class ExtendsFragment extends Fragment {

    // LoginModel 登录模型
    public static LoginModel mLoginModel;
    // 登录对象
    public static Login mLogin;

    // Token
    public String mToken;

    // 开启线程
    public Thread mThread;

    public void initFragment(Context context){
        // new登录模型
        mLoginModel = LoginModel.get(getActivity());
        // Token查询,赋值
        mLogin = mLoginModel.getLogin(1);
        mToken = mLogin.getToken();
    }
}
