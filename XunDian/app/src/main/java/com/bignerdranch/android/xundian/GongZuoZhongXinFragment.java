package com.bignerdranch.android.xundian;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.xundian.comm.AtyContainer;
import com.bignerdranch.android.xundian.xundianguanli.XunDianGuanLiActivity;
import com.jauker.widget.BadgeView;

/**
 * Created by Administrator on 2017/9/7.
 */

public class GongZuoZhongXinFragment extends Fragment{


    // 工作中心按钮
    // 巡店管理
    public View mXun_dian_guan_li_LinearLayout;

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.gong_zuo_zhong_xin, container, false);

        // 组件初始化
        ZhuJianInit();
        // 组件操作, 操作
        ZhuJianCaoZhuo();

        return mView;
    }

    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        // 初始化
        mXun_dian_guan_li_LinearLayout = mView.findViewById(R.id.xun_dian_guan_li_LinearLayout);
    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){

        // 巡店管理
        mXun_dian_guan_li_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 存储容器
                AtyContainer.addActivity(getActivity());
                Intent i = XunDianGuanLiActivity.newIntent(getActivity(),1);
                startActivity(i);
            }
        });

    }


}
