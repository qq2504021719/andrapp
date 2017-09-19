package com.bignerdranch.android.xundian.xundianguanli;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.NeiYeCommActivity;
import com.bignerdranch.android.xundian.comm.TongZhi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public class XunDianXiangXiActivity extends NeiYeCommActivity {

    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.XunDianXiangXiActivity";

    private XiangXiAdapter mAdapter;

    // 通知
    private LinearLayout mXiang_xi_linearLayout;
    private TextView mXiang_xi_textview;
    // 通知未读数量

    // 公告
    private LinearLayout mMen_dian_linearLayout;
    private TextView mMen_dian_textview;


    // 通知公告数据List
    public List<TongZhi> mTongZhis;
    // is页面 1 通知 2 公告
    private int mIsYeMian = 1;

    public String mShowString = "{\"leixing\":[\"DD-CVS-B \\u672a\\u5b8c\\u62101 \\u5df2\\u5b8c\\u62102\",\"DD-CVS-B \\u672a\\u5b8c\\u62102 \\u5df2\\u5b8c\\u62103\"],\"mendian\":[\"DD-CVS-B \\u672a\\u5b8c\\u621011 \\u5df2\\u5b8c\\u621022\",\"DD-CVS-B \\u672a\\u5b8c\\u621022 \\u5df2\\u5b8c\\u621033\"]}";


    private RecyclerView mTongZhiRecyclerView;


    public static Intent newIntent(Context packageContext, int intIsId){
        Intent i = new Intent(packageContext,XunDianXiangXiActivity.class);
        i.putExtra(EXTRA,intIsId);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_xun_dian_xiang_xi);

        mContext = this;
        // 组件初始化
        ZhuJianInit();

        // 组件操作
        ZhuJianCaoZhuo();
        // 数据/值设置
        values();
    }

    /**
     * 更新数据,更改背景色
     */
    public void updateButtonBackground(){
        if(mIsYeMian == 1){
            mXiang_xi_textview.setBackgroundColor(mContext.getResources().getColor(R.color.zhuti));
            mMen_dian_textview.setBackgroundColor(mContext.getResources().getColor(R.color.huise));
            mXiang_xi_linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.zhuti));
            mMen_dian_linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.huise));
        }else if(mIsYeMian == 2){
            mXiang_xi_textview.setBackgroundColor(mContext.getResources().getColor(R.color.huise));
            mMen_dian_textview.setBackgroundColor(mContext.getResources().getColor(R.color.zhuti));
            mXiang_xi_linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.huise));
            mMen_dian_linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.zhuti));
        }
    }

    /**
     * ui更新
     */
    public void updateUI(){
        getTongZhis();
        // 获取数据
        mAdapter = new XiangXiAdapter(mTongZhis);
        mTongZhiRecyclerView.setAdapter(mAdapter);
        // 通知公告背景色
        updateButtonBackground();
    }

    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mTitle_nei_ye = (TextView)findViewById(R.id.title_nei_ye);


        mXiang_xi_linearLayout = (LinearLayout)findViewById(R.id.lei_xin_linearLayout);
        mMen_dian_linearLayout = (LinearLayout)findViewById(R.id.men_dian_linearLayout);

        mXiang_xi_textview = (TextView)findViewById(R.id.lei_xin_textview);
        mMen_dian_textview = (TextView)findViewById(R.id.men_dian_textview);

        // 创建 RecyclerView
        mTongZhiRecyclerView = (RecyclerView)findViewById(R.id.xiang_xi_recycler_view);

    }

    /**
     * 值操作
     */
    public void values(){
        // Token赋值
        setToken(mContext);
        // 属性UI数据
        updateUI();

    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText(R.string.footer_bar_2);


        mXiang_xi_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsYeMian == 2){
                    mIsYeMian = 1;
                    updateButtonBackground();
                    updateUI();
                }
            }
        });

        mMen_dian_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsYeMian == 1){
                    mIsYeMian = 2;
                    updateButtonBackground();
                    updateUI();
                }
            }
        });

        mTongZhiRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

    }

    private class XiangXiHolder extends RecyclerView.ViewHolder{

        private TextView mXiang_xi_title_textview;

        public View mItemView;

        private TongZhi mTongZhi;

        /**
         * 绑定每行数据
         * @param tongZhi
         */
        public void bindTongZhi(TongZhi tongZhi){
            mTongZhi = tongZhi;
            mXiang_xi_title_textview.setText(mTongZhi.getTitle());
        }

        public XiangXiHolder(View itemView){
            super(itemView);
            mItemView = itemView;
            // 绑定点击事件
            mXiang_xi_title_textview = (TextView)mItemView.findViewById(R.id.Xiang_xi_title_textview);
        }
    }

    private class XiangXiAdapter extends RecyclerView.Adapter<XiangXiHolder>{
        private List<TongZhi> mTongZhis;
        public XiangXiAdapter(List<TongZhi> tongZhis){
            mTongZhis = tongZhis;
        }

        /**
         * 设置布局文件
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public XiangXiHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view = layoutInflater.inflate(R.layout.item_xun_dian_xiang_xi,parent,false);
            return new XiangXiHolder(view);
        }

        @Override
        public void onBindViewHolder(XiangXiHolder holder, int position){
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
     * 获取Tongzhis数据 mIsYeMian
     */
    public void getTongZhis(){
        JSONArray jsonArray = new JSONArray();
        mTongZhis = new ArrayList<TongZhi>();
        try {
            JSONObject jsonObject = new JSONObject(mShowString);
            if(mIsYeMian == 1){
                jsonArray = new JSONArray(jsonObject.getString("leixing").toString());
            }else if(mIsYeMian == 2){
                jsonArray = new JSONArray(jsonObject.getString("mendian").toString());
            }
            for(int i = 0;i<jsonArray.length();i++){
                TongZhi tongZhi = new TongZhi();
                tongZhi.setId(i);
                tongZhi.setTitle(jsonArray.getString(i));
                mTongZhis.add(tongZhi);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        for(int i = 0;i<30;i++){
//            TongZhi tongZhi = new TongZhi();
//            if(i%2 == 0){
//                tongZhi.setChaKan(false);
//            }else{
//                tongZhi.setChaKan(true);
//            }
//            tongZhi.setId(i);
//            tongZhi.setTitle(i+str+"DD-DS-DY-B 未完成8 已完成0");
//            mTongZhis.add(tongZhi);
//        }

    }

}
