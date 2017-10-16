package com.bignerdranch.android.xundian;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bignerdranch.android.xundian.comm.AtyContainer;
import com.bignerdranch.android.xundian.kaoqing.KaoQingActivity;
import com.bignerdranch.android.xundian.kehutuozhan.KeHuActivity;
import com.bignerdranch.android.xundian.xundianguanli.XunDianGuanLiActivity;
import com.bignerdranch.android.xundian.xundianjihua.JiHuaActivity;

/**
 * Created by Administrator on 2017/9/7.
 */

public class GongZuoZhongXinFragment extends Fragment{


    // 工作中心按钮
    // 巡店管理
    public View mXun_dian_guan_li_LinearLayout;

    // 巡店计划
    public LinearLayout mButton_ji_hua;

    // 考勤
    public LinearLayout mGong_kao_qing_linearLayout;

    // 客户拓展
    public LinearLayout mButton_ke_hu;

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_gong_zuo_zhong_xin, container, false);

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
        // 巡店计划
        mButton_ji_hua = (LinearLayout)mView.findViewById(R.id.button_ji_hua);
        // 考勤
        mGong_kao_qing_linearLayout = (LinearLayout)mView.findViewById(R.id.gong_kao_qing_linearLayout);
        // 客户拓展
        mButton_ke_hu = (LinearLayout)mView.findViewById(R.id.button_ke_hu);
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
//                AtyContainer.addActivity(getActivity());
                Intent i = XunDianGuanLiActivity.newIntent(getActivity(),1);
                startActivity(i);
            }
        });

        // 巡店计划
        mButton_ji_hua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = JiHuaActivity.newIntent(getActivity(),1);
                startActivity(i);
            }
        });

        // 考勤
        mGong_kao_qing_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = KaoQingActivity.newIntent(getActivity(),1);
                startActivity(i);
            }
        });

        // 客户拓展
//        mButton_ke_hu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = KeHuActivity.newIntent(getActivity(),1);
//                startActivity(i);
//            }
//        });

    }


}
