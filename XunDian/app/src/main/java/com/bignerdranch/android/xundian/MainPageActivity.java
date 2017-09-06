package com.bignerdranch.android.xundian;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.List;

/**
 * Created by Administrator on 2017/9/1.
 */

public class MainPageActivity extends FragmentActivity {

    private static final String EXTRA_VIEW_ID = "com.bignerdranch.android.MainPageActivity.xundian.view_id";

    private ViewPager mViewPager;
    private List<Viewd> mViewds;
    private int viewId;

    private ViewS mViewS;

    public static Intent newIntent(Context packageContext, int viewId){
        Intent i = new Intent(packageContext,MainPageActivity.class);
        i.putExtra(EXTRA_VIEW_ID,viewId);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_view_page);

        viewId = 1;

        mViewPager = (ViewPager) findViewById(R.id.activity_main_page_view_page);
        mViewS = new ViewS();
        mViewS.init();
        mViewds = mViewS.getViewS();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Viewd viewd = mViewds.get(position);
                return MainFragment.newInstance(viewd.getViewid());
            }

            @Override
            public int getCount() {
                return mViewds.size();
            }
        });

    }

}
