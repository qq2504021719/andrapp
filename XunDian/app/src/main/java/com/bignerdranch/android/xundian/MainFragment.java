package com.bignerdranch.android.xundian;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/9/6.
 */

public class MainFragment extends Fragment {

    private static final String EXTRA_VIEW_ID = "com.bignerdranch.android.xundian.view_id";
    private TextView mTextViewView1;
    private ViewS mViewS;

    private int viewId;


    public static MainFragment newInstance(int viewId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_VIEW_ID,viewId);

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        viewId = (int)getArguments().getSerializable(EXTRA_VIEW_ID);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.view_1,container,false);

        mTextViewView1 = (TextView)view.findViewById(R.id.text_view_view_1);
        mViewS = new ViewS();
        mViewS.init();
        Viewd viewd = mViewS.getViewd(viewId);
        String viewdString = viewd.getViewString();
        mTextViewView1.setText(viewdString);

        return view;
    }

}
