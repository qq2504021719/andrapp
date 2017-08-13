package com.bignerdranch.android.lianxi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2017/8/12.
 */

public class ShangPingPageActivity extends FragmentActivity{

    private ViewPager mViewPager;
    private List<ShangPing> mShangPings;
    private static final String EXTRA_SHANGPING_ID = "com.bignerdranch.android.criminalintent.shangping_id";

    public static Intent newIntent(Context packageContext, UUID shangpingId){
        Intent intent = new Intent(packageContext,ShangPingPageActivity.class);
        intent.putExtra(EXTRA_SHANGPING_ID,shangpingId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangping_pager);

        // 获取Intent传递过来的shangpingId
        UUID shangingId = (UUID)getIntent().getSerializableExtra(EXTRA_SHANGPING_ID);
        mViewPager = (ViewPager)findViewById(R.id.activity_shangping_pager_view_pager);
        // 获取数据
        mShangPings = ShangPingS.get(this).getShangpingS();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                ShangPing shangping = mShangPings.get(position);
                return ShangPingFragment.newInstance(shangping.getId());
            }

            @Override
            public int getCount() {
                return mShangPings.size();
            }
        });

        // 如果Shangping实例的mId与intent extra的shangpingId相匹配,设置显示指定位置的列表项
        for (int i = 0;i<mShangPings.size();i++){
            if(mShangPings.get(i).getId().equals(shangingId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

}
