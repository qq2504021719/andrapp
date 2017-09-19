package com.bignerdranch.android.sunset;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/9/19.
 */

public class SunsetFragment extends Fragment {

    public static SunsetFragment newInstance(){
        return new SunsetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragmnet_sunset,container,false);
        return v;
    }

}
