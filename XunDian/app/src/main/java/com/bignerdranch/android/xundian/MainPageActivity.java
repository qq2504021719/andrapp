package com.bignerdranch.android.xundian;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/9/1.
 */

public class MainPageActivity extends FragmentActivity{

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

    public static Intent newIntent(Context packageContext, int viewId){
        Intent i = new Intent(packageContext,MainPageActivity.class);
        i.putExtra(EXTRA_VIEW_ID,viewId);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_page_view_page);

        /*
         * 初始化按钮
         */
        mRb1 = (RadioButton)findViewById(R.id.rb_1);
        mRb2 = (RadioButton)findViewById(R.id.rb_2);
        mRb3 = (RadioButton)findViewById(R.id.rb_3);
        mRb4 = (RadioButton)findViewById(R.id.rb_4);
        mTextViewTitle = (TextView)findViewById(R.id.title);
        mRgroup = (RadioGroup)findViewById(R.id.rg);

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

}
