package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2017/8/9.
 */

public class CrimeListActivity extends SingleFragmentActivity {

    /*
    *
    * 返回由activity托管的fragment实例
    * */
    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }

}
