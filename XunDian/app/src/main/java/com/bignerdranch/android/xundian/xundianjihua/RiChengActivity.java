package com.bignerdranch.android.xundian.xundianjihua;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.NeiYeCommActivity;
import com.bignerdranch.android.xundian.comm.TongZhi;

/**
 * Created by Administrator on 2017/9/20.
 */

public class RiChengActivity extends NeiYeCommActivity{

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.RiChengActivity";

    // 顶部导航条
    private LinearLayout mBen_zhou_linearLayout;
    private LinearLayout mBo_hui_linearLayout;
    private TextView mBen_zhou_textview;
    private TextView mBo_hui_textview;
    private int mIsYeMian = 2;

    // Fragment
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Fragment f1,f2;

    public static Intent newIntent(Context packageContext, int intIsId){
        Intent i = new Intent(packageContext,RiChengActivity.class);
        i.putExtra(EXTRA,intIsId);
        return i;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ri_cheng);
        mContext = this;
        // 组件初始化
        ZhuJianInit();
        // 组件操作
        ZhuJianCaoZhuo();
        // 数据/值设置
        values();
    }
    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mTitle_nei_ye = (TextView)findViewById(R.id.title_nei_ye);
        // 顶部导航条
        mBen_zhou_linearLayout = (LinearLayout)findViewById(R.id.ben_zhou_linearLayout);
        mBo_hui_linearLayout = (LinearLayout)findViewById(R.id.bo_hui_linearLayout);
        mBen_zhou_textview = (TextView)findViewById(R.id.ben_zhou_textview);
        mBo_hui_textview = (TextView)findViewById(R.id.bo_hui_textview);
        // Fragment
    }
    /**
     * 值操作
     */
    public void values(){
        // Token赋值
        setToken(mContext);
    }
    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText(R.string.wo_de_ri_cheng);
        // 更改导航栏颜色
        updateButtonBackground();

        // 本周点击事件
        mBen_zhou_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsYeMian == 2){
                    mIsYeMian = 1;
                    updateButtonBackground();
                    updateUI();
                }
            }
        });

        // 驳回点击
        mBo_hui_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsYeMian == 1){
                    mIsYeMian = 2;
                    updateButtonBackground();
                    updateUI();
                }
            }
        });
    }

    /**
     * 更改导航栏颜色
     */
    public void updateButtonBackground(){
        if(mIsYeMian == 1){
            mBen_zhou_textview.setBackgroundColor(mContext.getResources().getColor(R.color.zhuti));
            mBo_hui_textview.setBackgroundColor(mContext.getResources().getColor(R.color.huise));
            mBen_zhou_linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.zhuti));
            mBo_hui_linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.huise));
        }else if(mIsYeMian == 2){
            mBen_zhou_textview.setBackgroundColor(mContext.getResources().getColor(R.color.huise));
            mBo_hui_textview.setBackgroundColor(mContext.getResources().getColor(R.color.zhuti));
            mBen_zhou_linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.huise));
            mBo_hui_linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.zhuti));
        }
    }

    /**
     * 更新UI信息
     */
    public void updateUI(){
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        if(mIsYeMian == 1){
            hideFragment(transaction);
            f1 = new BenZhouFragment();
            transaction.replace(R.id.fragment_container, f1);
            transaction.commit();
        }else if(mIsYeMian == 2){
            hideFragment(transaction);
            f2 = new BoHuiFragment();
            transaction.replace(R.id.fragment_container, f2);
            transaction.commit();
        }
    }

    /*
    * 去除（隐藏）所有的Fragment
    * */
    private void hideFragment(FragmentTransaction transaction) {
        if (f1 != null) {
            //transaction.hide(f1);隐藏方法也可以实现同样的效果，不过我一般使用去除
            transaction.remove(f1);
        }
        if (f2 != null) {
            //transaction.hide(f2);
            transaction.remove(f2);
        }


    }

}
