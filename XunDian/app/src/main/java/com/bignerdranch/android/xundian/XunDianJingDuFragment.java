package com.bignerdranch.android.xundian;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.model.LoginModel;
import com.bignerdranch.android.xundian.xundianguanli.XunDianXiangXiActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Administrator on 2017/9/17.
 */

public class XunDianJingDuFragment extends Fragment {

    private View mView;

    // LoginModel 登录模型
    private static LoginModel mLoginModel;
    // 登录对象
    private static Login mLogin;
    // Token
    private String mToken;
    // 请求url
    private String mUserDataUrl = Config.URL+"/app/user";
    // 开启线程
    private static Thread mThread;

    private PieChart mChart;

    // 查看详细
    private Button mButton_xiang_xi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_xun_dian_jin_du, container, false);

        // 值操作 set get值
        values();

        // 组件初始化
        ZhuJianInit();

        // 组件操作, 操作
        ZhuJianCaoZhuo();


        return mView;
    }

    /**
     * 检查网络是否完全连接 true 连接  false 没有连接
     * @return
     */
    private boolean isNetworkAvailableAndConnected(){
        ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    /**
     * 值操作 set get值
     */
    public void values() {
        // new登录模型
        mLoginModel = LoginModel.get(getActivity());
        // Token查询,赋值
        mLogin = mLoginModel.getLogin(1);
        mToken = mLogin.getToken();
    }
    /**
     * Handler
     */
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            /**
             * 请求回调
             */
            if(msg.what==1){
                // 个人信息回调
            }
        }
    };

    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mChart = (PieChart) mView.findViewById(R.id.mPieChart);

        // 查看详细
        mButton_xiang_xi = (Button)mView.findViewById(R.id.button_xiang_xi);
    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        // 设置饼状图
        showChart(mChart,200,20);
        // 查看详细
        mButton_xiang_xi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = XunDianXiangXiActivity.newIntent(getActivity(),1);
                startActivity(i);
            }
        });

    }

    /**
     * 设置饼状图数据
     * @param sum 总数
     * @param yiWanC 已完成数
     */
    private void setData(float sum,float yiWanC) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        entries.add(new PieEntry((int)yiWanC,"已完成 "+(int)yiWanC+"/条"));
        entries.add(new PieEntry((int)sum-yiWanC,"未完成 "+(int)(sum-yiWanC)+"/条"));
        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(6f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(15f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();
        // 0区域颜色
        colors.add(Color.rgb(0,153,255));
        // 1区域颜色
        colors.add(Color.rgb(200,200,200));

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(16f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    /**
     * 显示饼状图
     * @param mChart id
     * @param sum 总数
     * @param yiWanC 已完成数
     */
    private void showChart(PieChart mChart,float sum,float yiWanC) {
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        // 隐藏饼状文字
        mChart.setDrawEntryLabels(false);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterText(new SpannableString("合计\n"+(int)sum+"条"));
        mChart.setCenterTextSize(18f); // 中心字体大小

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(38f); // 中间圆半径
        mChart.setTransparentCircleRadius(45f);// 透明层圆半径

        // 中间文字显示
        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // 设置显示数据
        setData(sum,yiWanC);

        mChart.animateY(2400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        // 设置提示块
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setFormSize(20f);//比例块字体大小
        l.setTextSize(12f);
        l.setDrawInside(false);
        l.setXEntrySpace(20f);
        l.setYOffset(5f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
//        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(16f);
    }



}
