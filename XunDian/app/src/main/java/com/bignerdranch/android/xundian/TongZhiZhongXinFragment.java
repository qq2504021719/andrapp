package com.bignerdranch.android.xundian;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bignerdranch.android.xundian.comm.TongZhi;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by Administrator on 2017/9/10.
 */

public class TongZhiZhongXinFragment extends Fragment{

    // 当前Fragment的View
    private View mView;
    // 通知
    private View mTong_zhi_linearLayout;
    private int mTongZhiNum = 5;
    private Button mTong_zhi_button;
    // 公告
    private View mGong_gao_linearLayout;
    private int mGongGaoNum = 7;
    private Button mGong_gao_button;

    // 通知公告数据List
    public List<TongZhi> mTongZhis;
    // is页面 1 通知 2 公告
    private int mIsYeMian = 1;


    private RecyclerView mTongZhiRecyclerView;
    private TongZhiAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tong_zhi_zhong_xin, container, false);

        // 组件初始化
        ZhuJianInit();
        // 组件操作, 操作
        ZhuJianCaoZhuo();

        // 创建 RecyclerView
        mTongZhiRecyclerView = (RecyclerView) mView.findViewById(R.id.tong_zhi_zhong_xin_item);
        mTongZhiRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return mView;
    }


    /**
     * 更新数据,更改背景色
     */
    public void updateButtonBackground(){
        if(mIsYeMian == 1){
            mTong_zhi_button.setBackgroundColor(getActivity().getResources().getColor(R.color.zhuti));
            mGong_gao_button.setBackgroundColor(getActivity().getResources().getColor(R.color.huise));
        }else if(mIsYeMian == 2){
            mTong_zhi_button.setBackgroundColor(getActivity().getResources().getColor(R.color.huise));
            mGong_gao_button.setBackgroundColor(getActivity().getResources().getColor(R.color.zhuti));
        }
        updateUI();
    }

    /**
     * ui更新
     */
    public void updateUI(){
        if(mIsYeMian == 1){
            getTongZhis("--通知");
        }else{
            getTongZhis("--公告");

        }
        // 获取数据
        mAdapter = new TongZhiAdapter(mTongZhis);
        mTongZhiRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mTong_zhi_linearLayout = (View) mView.findViewById(R.id.tong_zhi_linearLayout);
        mGong_gao_linearLayout = (View)mView.findViewById(R.id.gong_gao_linearLayout);
        mTong_zhi_button = (Button)mView.findViewById(R.id.tong_zhi_button);
        mGong_gao_button = (Button)mView.findViewById(R.id.gong_gao_button);
    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        // 设置红点
        updateTongGongNum();

        mTong_zhi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsYeMian == 2){
                    mIsYeMian = 1;
                    updateButtonBackground();
                }
            }
        });

        mGong_gao_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsYeMian == 1){
                    mIsYeMian = 2;
                    updateButtonBackground();
                }
            }
        });
    }

    /**
     * 设置红点数字
     * @param button 设置按钮
     * @param num   数字
     */
    private Badge badgeViewTong;
    private Badge badgeViewGong;
    public void SetHongDian(View button, int num,int isYeMian) {
        if (isYeMian == 1) {
            badgeViewTong = new QBadgeView(getActivity()).bindTarget(button).setBadgeNumber(num);
        } else if (isYeMian == 2) {
            badgeViewGong = new QBadgeView(getActivity()).bindTarget(button).setBadgeNumber(num);
        }
    }
    /**
     * 因为bageView隐藏有有BUG,就手动更换显示位置
     */
    public void shanChuHongDian(){
        if(mIsYeMian == 1 && badgeViewTong != null){
            badgeViewTong.hide(true);
        }else if(mIsYeMian == 2 && badgeViewGong != null){
            badgeViewGong.hide(true);
        }
    }

    /**
     * 更新通知和公告显示的num数量
     */
    public void updateTongGongNum(){
        SetHongDian(mTong_zhi_linearLayout,mTongZhiNum,1);
        SetHongDian(mGong_gao_linearLayout,mGongGaoNum,2);
    }

    private class TongZhiHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTong_zhi_title_textview;
        private TextView mTong_zhi_shi_jian_textview;
        private View mTong_zhi_ischakan_view;

        public View mItemView;

        private TongZhi mTongZhi;

        /**
         * 绑定每行数据
         * @param tongZhi
         */
        public void bindTongZhi(TongZhi tongZhi){
            mTongZhi = tongZhi;
            mTong_zhi_title_textview.setText(mTongZhi.getTitle());
            mTong_zhi_shi_jian_textview.setText(mTongZhi.getTime());
            // 是否显示未读红标
            if(mTongZhi.getChaKan()){
                mTong_zhi_ischakan_view.setVisibility(View.GONE);
            }else{
                mTong_zhi_ischakan_view.setVisibility(View.VISIBLE);
            }
        }

        public TongZhiHolder(View itemView){
            super(itemView);
            mItemView = itemView;
            // 绑定点击事件
            mItemView.setOnClickListener(this);
            mTong_zhi_title_textview = (TextView)itemView.findViewById(R.id.tong_zhi_title_textview);
            mTong_zhi_shi_jian_textview = (TextView)itemView.findViewById(R.id.tong_zhi_shi_jian_textview);
            mTong_zhi_ischakan_view = (View)itemView.findViewById(R.id.tong_zhi_ischakan_view);
        }

        /**
         * 点击事件
         * @param v
         */
        public void onClick(View v){
            // 更新通知和公告显示的数字
            if(!mTongZhi.getChaKan()){
                if(mIsYeMian == 1){
                    if(mTongZhiNum > 0){
                        mTongZhiNum = mTongZhiNum-1;
                    }else{
                        shanChuHongDian();
                    }
                }else if(mIsYeMian == 2){
                    if(mGongGaoNum > 0){
                        mGongGaoNum = mGongGaoNum-1;
                    }else{
                        shanChuHongDian();
                    }
                }
                updateTongGongNum();
            }

            mTongZhi.setChaKan(true);
            // 刷新数据
            mAdapter.notifyDataSetChanged();
            // 弹窗
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
            // 获取布局文件
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewD = inflater.inflate(R.layout.tong_zhi_zhong_xin_dialog, null);

//            final Dialog dialog = new Dialog(getActivity());
            // 初始化组件
            TextView title_dialog = viewD.findViewById(R.id.title_dialog);
            TextView content_dialog = viewD.findViewById(R.id.content_dialog);
            Button fan_hui_dialog = viewD.findViewById(R.id.fan_hui_dialog);

            // 设置对应TextView可滚动
            content_dialog.setMovementMethod(new ScrollingMovementMethod());
            // 设置显示内容
            title_dialog.setText(mTongZhi.getTitle());
            content_dialog.setText(mTongZhi.getContent());

            // 设置View
            alertBuilder.setView(viewD);

            // 显示
            alertBuilder.create();
            final Dialog dialog = alertBuilder.show();

            // 点击销毁Dialog
            fan_hui_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        }
    }

    private class TongZhiAdapter extends RecyclerView.Adapter<TongZhiHolder>{
        private List<TongZhi> mTongZhis;
        public TongZhiAdapter(List<TongZhi> tongZhis){
            mTongZhis = tongZhis;
        }

        /**
         * 设置布局文件
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public TongZhiHolder onCreateViewHolder(ViewGroup parent,int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.tong_zhi_zhong_xin_item,parent,false);
            return new TongZhiHolder(view);
        }

        @Override
        public void onBindViewHolder(TongZhiHolder holder,int position){
            TongZhi tongZhi = mTongZhis.get(position);
            holder.bindTongZhi(tongZhi);
        }

        @Override
        public int getItemCount(){
            return mTongZhis.size();
        }

        /**
         * 刷新数据时使用
         * @param tongZhis
         */
        public void setTongZhis(List<TongZhi> tongZhis){
            mTongZhis = tongZhis;
        }

    }


    /**
     * 获取Tongzhis数据
     */
    public void getTongZhis(String str){
        mTongZhis = new ArrayList<TongZhi>();
        for(int i = 0;i<30;i++){
            TongZhi tongZhi = new TongZhi();
            if(i%2 == 0){
                tongZhi.setChaKan(false);
            }else{
                tongZhi.setChaKan(true);
            }
            tongZhi.setId(i);
            tongZhi.setTitle(i+str+"巡店达人管理系统手机操作端苹果IOS版");
            tongZhi.setTime("2017-06-14 14:42:07");
            tongZhi.setContent("内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i
                    +"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i+"内容"+i);
            mTongZhis.add(tongZhi);
        }

    }
}
