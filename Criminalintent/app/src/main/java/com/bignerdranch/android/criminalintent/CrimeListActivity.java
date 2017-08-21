package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2017/8/9.
 */

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks,CrimeFragment.Callbacks{

    /*
    *
    * 返回由activity托管的fragment实例
    * */
    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }

    @LayoutRes
    protected int getLayoutResId(){
        return R.layout.activity_masterdetail;
    }

    /**
     * 实现接口方法 根据不同的布局界面分别处理
     * @param crime
     */
    public void onCrimeSelected(Crime crime){
        if(findViewById(R.id.detail_fragment_container) == null){
            Intent intent = CrimePagerActivity.newIntent(this,crime.getId());
            startActivity(intent);
        }else{
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container,newDetail)
                    .commit();
        }
    }

    /**
     * 重新加载crime列表
     * @param crime
     */
    public void onCrimeUpdated(Crime crime){
        CrimeListFragment listFragment = (CrimeListFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }

}
