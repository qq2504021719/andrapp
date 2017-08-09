package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

public class CrimeActivity extends SingleFragmentActivity {


    /*
    *
    * 返回由activity托管的fragment实例
    * */
    @Override
    protected Fragment createFragment(){
        return new CrimeFragment();
    }

}