package com.bignerdranch.android.xundian.comm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.android.xundian.R;
/**
 * Created by Administrator on 2017/9/10.
 */

public class TitleNeiYeActivity extends AppCompatActivity{
    private TextView mTitle_nei_ye; // title
    public int mSetTitle; // 设置显示标题
    private ImageButton mImageButton; // 返回按钮
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_nei_ye);

        // 设置标题
        mTitle_nei_ye = (TextView)findViewById(R.id.title_nei_ye);
        mTitle_nei_ye.setText(mSetTitle);

    }

    /**
     * 设置title
     * @param setTitle
     */
    public void setSetTitle(int setTitle) {
        mSetTitle = setTitle;
    }

}
