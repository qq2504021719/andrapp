package com.bignerdranch.android.xundian;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.bignerdranch.android.xundian.comm.AtyContainer;
import com.bignerdranch.android.xundian.model.LoginModel;

import java.util.List;

/**
 * Created by Administrator on 2017/9/1.
 */

public class MainPageActivity extends AppCompatActivity implements TongZhiZhongXinFragment.Callbacks{

    private static final String EXTRA_VIEW_ID = "com.bignerdranch.android.MainPageActivity.xundian.view_id";

    private ViewPager mViewPager;
    private List<Viewd> mViewds;
    private int mViewId;

    // Fragmen集合
    private ViewS mViewS;

    // 底部按钮
    private RadioButton mRb1;
    private RadioButton mRb2;
    private RadioButton mRb3;
    private RadioButton mRb4;
    // 头部标题
    private TextView mTextViewTitle;
    private RadioGroup mRgroup;

    private FragmentManager mManager;

    // 通知公告红点提示
    public View mBar_yuan_view;






    public static Intent newIntent(Context packageContext, int viewId){
        Intent i = new Intent(packageContext,MainPageActivity.class);
        i.putExtra(EXTRA_VIEW_ID,viewId);
        return i;
    }
    // LoginModel 登录模型
    private static LoginModel mLoginModel;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_page_view_page);

        // 销毁其余容器
        AtyContainer.finishAllActivity();

        // 组件初始化
        ZhuJianInit();
        // 组件操作, 操作
        ZhuJianCaoZhuo();

//        mLoginModel = LoginModel.get(this);
//        mLoginModel.chaxbiao();
    }

    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        /*
         * 初始化按钮
         */
        mRb1 = (RadioButton)findViewById(R.id.rb_1);
        mRb2 = (RadioButton)findViewById(R.id.rb_2);
        mRb3 = (RadioButton)findViewById(R.id.rb_3);
        mRb4 = (RadioButton)findViewById(R.id.rb_4);
        mTextViewTitle = (TextView)findViewById(R.id.title);
        mRgroup = (RadioGroup)findViewById(R.id.rg);

        mBar_yuan_view = (View)findViewById(R.id.bar_yuan_view);
    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        // 设置红点
        TongZhiZhongXinFragment tongZhiZhongXinFragment = new TongZhiZhongXinFragment();
        tongZhiZhongXinFragment.shanChuHongDian();

        /*
         * 为四个按钮添加监听
         */
        mRb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(1);
            }
        });
        mRb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(2);
            }
        });
        mRb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(3);
            }
        });
        mRb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(4);
            }
        });

        /*
         * 默认选中第一个
         */
        mViewId = getIntent().getIntExtra(EXTRA_VIEW_ID,1);
        mRgroup.check(R.id.rb_1);

        mViewPager = (ViewPager) findViewById(R.id.activity_main_page_view_page);
        mViewS = new ViewS();
        mViewS.init();
        mViewds = mViewS.getViewS();

        mManager = getSupportFragmentManager();
        showFragment(mViewId);

        // 通知公告红点浮动,不占据位置
        mBar_yuan_view.bringToFront();

    }

    public void showFragment(int viewId){
        mViewId = viewId;
        /**
         * Fragment生成
         */
        mViewPager.setAdapter(new FragmentStatePagerAdapter(mManager) {
            @Override
            public Fragment getItem(int position) {
                Viewd viewd = mViewds.get(position);
                return viewd.getViewFragment();
            }


            @Override
            public int getCount() {
                return mViewds.size();
            }
        });

        /**
         * ViewPage监听
         */
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                showRgroup(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /**
         * 显示对应页面
         */
        for(int i = 0;i<mViewds.size();i++){
            if(mViewds.get(i).getViewid() == mViewId){
                mViewPager.setCurrentItem(i);
                showRgroup(i);
                break;
            }
        }
    }


    /**
     * 选择对应Rgroup,设置title
     * @param id
     */
    public void showRgroup(int id){
        int titles = 0;
        switch (id){
            case 0:
                mRgroup.check(R.id.rb_1);
                titles = R.string.footer_bar_1;
                break;
            case 1:
                mRgroup.check(R.id.rb_2);
                titles = R.string.footer_bar_2;
                break;
            case 2:
                mRgroup.check(R.id.rb_3);
                titles = R.string.footer_bar_3;
                break;
            case 3:
                mRgroup.check(R.id.rb_4);
                titles = R.string.footer_bar_4;
                break;
            default:
                break;
        }
        mTextViewTitle.setText(titles);
    }

    public void IsHong(int num){
        // 通知公告回调
        if(num == 1){
            // 隐藏通知公告红点
            mBar_yuan_view.setVisibility(View.INVISIBLE);
        }
    }
}
