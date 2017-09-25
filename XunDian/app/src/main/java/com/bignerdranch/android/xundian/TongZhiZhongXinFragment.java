package com.bignerdranch.android.xundian;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.ExtendsFragment;
import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.comm.TongZhi;
import com.bignerdranch.android.xundian.comm.WeiboDialogUtils;
import com.bignerdranch.android.xundian.model.LoginModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Administrator on 2017/9/10.
 */

public class TongZhiZhongXinFragment extends Fragment {

    private static final String VIEW_PUT = "com.bignerdranch.android.xundian.TongZhiZhongXinFragment";

    // 当前Fragment的View
    private View mView;
    // 通知
    private View mTong_zhi_linearLayout;
    private TextView mTong_zhi_textview;
    private View mTong_zhi_yuan_view;
    // 通知未读数量
    private int mTongZhiNum = 0;

    // 公告
    private View mGong_gao_linearLayout;
    private TextView mGong_gao_textview;
    private View mGong_gao_yuan_view;
    // 公告未读数量
    private int mGongGaoNum = 0;


    // 通知数据List
    public List<TongZhi> mTongZhis;
    // 公告数据List
    public List<TongZhi> mGongGaos;
    // is页面 1 通知 2 公告
    private int mIsYeMian = 1;


    private RecyclerView mTongZhiRecyclerView;
    private TongZhiAdapter mAdapter;

    // 回调函数存储变量
    private Callbacks mCallbacks;

    // 通知公告查询
    public String MURL = Config.URL+"/app/get_tong_zhi_gong_gao";
    // 通知公告已读提交
    public String mTongGaoTiJiao = Config.URL+"/app/post_tong_zhi_gong_gao";


    // LoginModel 登录模型
    public static LoginModel mLoginModel;
    // 登录对象
    public static Login mLogin;

    // Token
    public String mToken;

    // 开启线程
    public Thread mThread;

    // dialog,加载
    public Dialog mWeiboDialog;

    /**
     * 实现回调接口
     */
    public interface Callbacks{
        void IsHong(int num);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_tong_zhi_zhong_xin, container, false);

        // 组件初始化
        ZhuJianInit();

        LoadingStringEdit("加载中...");
        // 值操作
        values();
        // 数据请求
        getTongZhiGongGao();

        // 组件操作, 操作
        ZhuJianCaoZhuo();




        return mView;
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

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mCallbacks = null;
    }


    /**
     * 更新数据,更改背景色
     */
    public void updateButtonBackground(){
        if(mIsYeMian == 1){
            mTong_zhi_textview.setBackgroundColor(getActivity().getResources().getColor(R.color.zhuti));
            mGong_gao_textview.setBackgroundColor(getActivity().getResources().getColor(R.color.huise));
            mTong_zhi_linearLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.zhuti));
            mGong_gao_linearLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.huise));
        }else if(mIsYeMian == 2){
            mTong_zhi_textview.setBackgroundColor(getActivity().getResources().getColor(R.color.huise));
            mGong_gao_textview.setBackgroundColor(getActivity().getResources().getColor(R.color.zhuti));
            mTong_zhi_linearLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.huise));
            mGong_gao_linearLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.zhuti));
        }
    }

    /**
     * ui更新
     */
    public void updateUI(){
        if(mIsYeMian == 1){
            mAdapter = new TongZhiAdapter(mTongZhis);
            mTongZhiRecyclerView.setAdapter(mAdapter);
        }else if(mIsYeMian == 2){
            mAdapter = new TongZhiAdapter(mGongGaos);
            mTongZhiRecyclerView.setAdapter(mAdapter);
        }
        // 获取数据


        // 通知公告背景色
        updateButtonBackground();
        // 隐藏通知/公告红点
        shanChuHongDian();
    }

    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mTong_zhi_linearLayout = (View) mView.findViewById(R.id.tong_zhi_linearLayout);
        mGong_gao_linearLayout = (View)mView.findViewById(R.id.gong_gao_linearLayout);

        mTong_zhi_textview = (TextView)mView.findViewById(R.id.tong_zhi_textview);
        mGong_gao_textview = (TextView)mView.findViewById(R.id.gong_gao_textview);

        mTong_zhi_yuan_view = (View)mView.findViewById(R.id.tong_zhi_yuan_view);
        mGong_gao_yuan_view = (View)mView.findViewById(R.id.gong_gao_yuan_view);

        // 创建 RecyclerView
        mTongZhiRecyclerView = (RecyclerView) mView.findViewById(R.id.tong_zhi_zhong_xin_item);

    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){

        mTong_zhi_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsYeMian == 2){
                    mIsYeMian = 1;
                    updateButtonBackground();
                    updateUI();
                }
            }
        });

        mGong_gao_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsYeMian == 1){
                    mIsYeMian = 2;
                    updateButtonBackground();
                    updateUI();
                }
            }
        });

        mTongZhiRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    /**
     * 红点默认显示,当没有通知或公告时隐藏红点
     */
    public void shanChuHongDian(){
        if(mGongGaoNum == 0){
            mGong_gao_yuan_view.setVisibility(View.INVISIBLE);
        }else{
            mGong_gao_yuan_view.setVisibility(View.VISIBLE);
        }
        if(mTongZhiNum == 0){
            mTong_zhi_yuan_view.setVisibility(View.INVISIBLE);
        }else{
            mTong_zhi_yuan_view.setVisibility(View.VISIBLE);
        }
        Log.i("巡店","通知公告-公告"+mGongGaoNum+"|通知"+mTongZhiNum);
        if(mGongGaoNum == 0 && mTongZhiNum == 0){
            mCallbacks.IsHong(1);
        }else{
            mCallbacks.IsHong(2);
        }
        // 关闭loading
        WeiboDialogUtils.closeDialog(mWeiboDialog);
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
                mTong_zhi_ischakan_view.setVisibility(View.INVISIBLE);
            }else{
                mTong_zhi_ischakan_view.setVisibility(View.VISIBLE);
            }
        }

        public TongZhiHolder(View itemView){
            super(itemView);
            mItemView = itemView;
            // 绑定点击事件
            mItemView.setOnClickListener(this);
            mTong_zhi_title_textview = (TextView)mItemView.findViewById(R.id.tong_zhi_title_textview);
            mTong_zhi_shi_jian_textview = (TextView)mItemView.findViewById(R.id.tong_zhi_shi_jian_textview);
            mTong_zhi_ischakan_view = (View)mItemView.findViewById(R.id.tong_zhi_ischakan_view);


        }

        /**
         * 点击事件
         * @param v
         */
        public void onClick(View v){
            // 提交已读
            postTongZhiGongGao(String.valueOf(mTongZhi.getId()));
            mTongZhi.setChaKan(true);
            // 刷新数据
            mAdapter.notifyDataSetChanged();
            // 弹窗
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
            // 获取布局文件
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewD = inflater.inflate(R.layout.tong_zhi_zhong_xin_dialog, null);

            // 初始化组件
            TextView title_dialog = viewD.findViewById(R.id.title_dialog);
            final WebView content_dialog = viewD.findViewById(R.id.content_dialog);
            Button fan_hui_dialog = viewD.findViewById(R.id.fan_hui_dialog);

            // 设置对应TextView可滚动
//            content_dialog.setMovementMethod(new ScrollingMovementMethod());
            // 设置显示内容
            title_dialog.setText(mTongZhi.getTitle());
            content_dialog.loadUrl(mTongZhi.getContent());
            WebSettings webSettings = content_dialog.getSettings();
            webSettings.setUseWideViewPort(true);
            webSettings.setJavaScriptEnabled(true);

            // 可能的话使所有列的宽度不超过屏幕宽度
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSettings.setLoadWithOverviewMode(true);

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
                    content_dialog.destroy();
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
        mGongGaos = new ArrayList<TongZhi>();
        try {
            JSONArray jsonArray = new JSONArray(str);
            if(jsonArray.length() > 0){
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                    // 已读信息
                    JSONArray jsonArray1 = new JSONArray(jsonObject.getString("yi_du_user"));
                    TongZhi tongZhi = new TongZhi();

                    // 公告
                    if(jsonObject.getString("lei_xing").equals("0") && jsonArray1.length() == 0){
                        mGongGaoNum +=1;
                    }

                    // 通知
                    if(jsonObject.getString("lei_xing").equals("1") && jsonArray1.length() == 0){
                        mTongZhiNum +=1;
                    }

                    if(jsonArray1.length() == 0){
                        tongZhi.setChaKan(false);
                    }else{
                        tongZhi.setChaKan(true);
                    }
                    tongZhi.setId(Integer.valueOf(jsonObject.getString("id")));
                    tongZhi.setTitle(jsonObject.getString("biao_ti"));
                    tongZhi.setTime(jsonObject.getString("created_at"));
                    tongZhi.setContent(jsonObject.getString("url"));

                    if(jsonObject.getString("lei_xing").equals("0")){
                        mGongGaos.add(tongZhi);
                    }else{
                        mTongZhis.add(tongZhi);
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 更新UI
        updateUI();

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
                // 通知公告数据请求
                getTongZhis(msg.obj.toString());
            }else if(msg.what == 2){
                // 提交通知公告
                // 更新数量,是否显示红点
                if(mIsYeMian == 1){
                    if(mTongZhiNum > 0){
                        mTongZhiNum = mTongZhiNum-1;
                    }
                }else if(mIsYeMian == 2){
                    if(mGongGaoNum > 0){
                        mGongGaoNum = mGongGaoNum-1;
                    }
                }
                shanChuHongDian();
            }
        }
    };

    /**
     * 查询通知公告
     */
    public void getTongZhiGongGao(){
        if(!mToken.isEmpty()){
            final OkHttpClient client = new OkHttpClient();

            MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
            body.addFormDataPart("is","2");

            final Request request = new Request.Builder()
                    .addHeader("Authorization","Bearer "+mToken)
                    .url(MURL)
                    .post(body.build())
                    .build();


            //新建一个线程，用于得到服务器响应的参数
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Response response = null;
                    try {
                        //回调
                        response = client.newCall(request).execute();
                        //将服务器响应的参数response.body().string())发送到hanlder中，并更新ui
                        mHandler.obtainMessage(1, response.body().string()).sendToTarget();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            mThread.start();
        }
    }

    /**
     * 提交已读通知公告
     * @param id
     */
    public void postTongZhiGongGao(String id){
        if(!mToken.isEmpty()){
            final OkHttpClient client = new OkHttpClient();

            MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
            body.addFormDataPart("id",id);

            final Request request = new Request.Builder()
                    .addHeader("Authorization","Bearer "+mToken)
                    .url(mTongGaoTiJiao)
                    .post(body.build())
                    .build();


            //新建一个线程，用于得到服务器响应的参数
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Response response = null;
                    try {
                        //回调
                        response = client.newCall(request).execute();
                        //将服务器响应的参数response.body().string())发送到hanlder中，并更新ui
                        mHandler.obtainMessage(2, response.body().string()).sendToTarget();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            mThread.start();
        }
    }

    /**
     * loading 浮层
     *
     * @param logingString 提示文字
     */
    public void LoadingStringEdit(String logingString){
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getContext(),logingString);
    }

}
