package com.bignerdranch.android.lianxi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by Administrator on 2017/8/12.
 */

public abstract class SingleFragmentActivity extends FragmentActivity {

    protected abstract Fragment createFragmnet();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        // 获得FragmentManager,创建容器
        FragmentManager fm = getSupportFragmentManager();

        // 获取一个fragmnet
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        // bfragment为空则创建
        if(fragment == null){
            fragment = createFragmnet();
            // 创建一个新的fragment并交由FragmentManager管理
            fm.beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit();
        }
    }

}
