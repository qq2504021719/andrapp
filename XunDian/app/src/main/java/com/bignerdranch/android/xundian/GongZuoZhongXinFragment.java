package com.bignerdranch.android.xundian;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.xundian.xundianguanli.XunDianGuanLiActivity;

/**
 * Created by Administrator on 2017/9/7.
 */

public class GongZuoZhongXinFragment extends Fragment{


    // 工作中心按钮
    // 巡店管理
    public View mXun_dian_guan_li_LinearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gong_zuo_zhong_xin, container, false);
        // 初始化
        mXun_dian_guan_li_LinearLayout = view.findViewById(R.id.xun_dian_guan_li_LinearLayout);
        // 工作中心
        mXun_dian_guan_li_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = XunDianGuanLiActivity.newIntent(getActivity(),1);
                startActivity(i);
            }
        });

        return view;
    }


}
