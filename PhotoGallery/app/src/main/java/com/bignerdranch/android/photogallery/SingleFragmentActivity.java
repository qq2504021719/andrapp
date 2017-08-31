package com.bignerdranch.android.photogallery;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2017/8/9.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity{

    protected abstract Fragment createFragment();

    /**
     * 返回Activity需要的布局id,@LayoutRes注解,这是高数Android Studio,
     * 任何时候,改实现方法都应该返回有效的布局资源ID
     *
     * 子类重写此方法就可以了
     * @return
     */
    @LayoutRes
    protected int getLayoutResId(){
        return R.layout.activity_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        // 获取FragmentManager,创建容器
        FragmentManager fm = getSupportFragmentManager();
        // 获取一个fragment,如果存在，则直接返回
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            fragment = createFragment();
            //创建一个新二代fragment并交由FragmentManager管理
            fm.beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit();
        }
    }
}
