package com.bignerdranch.android.xundian;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/9/1.
 */

public class ViewActivity extends AppCompatActivity{

    private static final String EXTRA_VIEW_ID = "com.bignerdranch.android.xundian.view_id";
    private TextView mTextViewView1;
    private ViewS mViewS;

    public static Intent newIntent(Context packageContext,int viewId){
        Intent i = new Intent(packageContext,ViewActivity.class);
        i.putExtra(EXTRA_VIEW_ID,viewId);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_1);


        int viewId = getIntent().getIntExtra(EXTRA_VIEW_ID,1);

        mTextViewView1 = (TextView)findViewById(R.id.text_view_view_1);
        mViewS = new ViewS();
        mViewS.init();
        Viewd viewd = mViewS.getViewd(viewId);
        String viewdString = viewd.getViewString();
        mTextViewView1.setText(viewdString);
    }
}
