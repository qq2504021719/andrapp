package com.bignerdranch.android.xundian.xundianjihua;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.Config;
import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.comm.WeiboDialogUtils;
import com.bignerdranch.android.xundian.comm.XunDianJiHua;
import com.bignerdranch.android.xundian.model.LoginModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/20.
 */

public class BenZhouFragment extends Fragment{

    private View mView;

    private Context mContext;

    private TextView mTextview_zhou_title;

    // 周工作数据String
    private String mZhouJsonData = "[{\"id\":4,\"zhou\":\"2017-10-17\",\"ri_qi\":\"2017-10-17\",\"kai_shi_time\":\"09:50\",\"jie_shu_time\":\"11:10\",\"mendian_pin_pai\":\"\\u79d1\\u96f6\\u56fd\\u9645\\u98df\\u54c1\",\"mendian_id\":6483,\"mendian_name\":\"\\u79d1\\u96f6\\u4e9a\\u6d32\\u5e97\",\"mendian_hao\":\"65868\",\"uid\":13,\"gongsi_name\":\"\\u79d1\\u96f6\\u98df\\u54c1\\u6709\\u9650\\u516c\\u53f8\",\"gongsi_id\":34,\"gongsi_week_start\":\"2017-03-14\",\"updated_at\":\"2017-10-18 12:23:27\",\"created_at\":\"2017-10-18 12:23:27\",\"deleted_at\":null,\"zhuang_tai\":\"\\u6b63\\u5e38\",\"bo_hui_yi_jian\":null,\"shen_he_id\":1,\"isWC\":1},{\"id\":5,\"zhou\":\"2017-10-17\",\"ri_qi\":\"2017-10-18\",\"kai_shi_time\":\"12:00\",\"jie_shu_time\":\"06:00\",\"mendian_pin_pai\":\"\\u79d1\\u96f6\\u56fd\\u9645\\u98df\\u54c1\",\"mendian_id\":6483,\"mendian_name\":\"\\u79d1\\u96f6\\u4e9a\\u6d32\\u5e97\",\"mendian_hao\":\"65868\",\"uid\":13,\"gongsi_name\":\"\\u79d1\\u96f6\\u98df\\u54c1\\u6709\\u9650\\u516c\\u53f8\",\"gongsi_id\":34,\"gongsi_week_start\":\"2017-03-14\",\"updated_at\":\"2017-10-18 12:23:27\",\"created_at\":\"2017-10-18 12:23:27\",\"deleted_at\":null,\"zhuang_tai\":\"\\u6b63\\u5e38\",\"bo_hui_yi_jian\":null,\"shen_he_id\":1,\"isWC\":0},{\"id\":6,\"zhou\":\"2017-10-17\",\"ri_qi\":\"2017-10-19\",\"kai_shi_time\":\"09:00\",\"jie_shu_time\":\"11:00\",\"mendian_pin_pai\":\"\\u79d1\\u96f6\\u56fd\\u9645\\u98df\\u54c1\",\"mendian_id\":6483,\"mendian_name\":\"\\u79d1\\u96f6\\u4e9a\\u6d32\\u5e97\",\"mendian_hao\":\"65868\",\"uid\":13,\"gongsi_name\":\"\\u79d1\\u96f6\\u98df\\u54c1\\u6709\\u9650\\u516c\\u53f8\",\"gongsi_id\":34,\"gongsi_week_start\":\"2017-03-14\",\"updated_at\":\"2017-10-18 12:24:26\",\"created_at\":\"2017-10-18 12:24:26\",\"deleted_at\":null,\"zhuang_tai\":\"\\u6b63\\u5e38\",\"bo_hui_yi_jian\":null,\"shen_he_id\":1,\"isWC\":0},{\"id\":7,\"zhou\":\"2017-10-17\",\"ri_qi\":\"2017-10-20\",\"kai_shi_time\":\"06:00\",\"jie_shu_time\":\"08:00\",\"mendian_pin_pai\":\"\\u79d1\\u96f6\",\"mendian_id\":6474,\"mendian_name\":\"\\u79d1\\u96f6\\u98df\\u54c1\\u771f\\u5317\\u5e97\",\"mendian_hao\":\"666666\",\"uid\":13,\"gongsi_name\":\"\\u79d1\\u96f6\\u98df\\u54c1\\u6709\\u9650\\u516c\\u53f8\",\"gongsi_id\":34,\"gongsi_week_start\":\"2017-03-14\",\"updated_at\":\"2017-10-18 13:31:49\",\"created_at\":\"2017-10-18 13:31:49\",\"deleted_at\":null,\"zhuang_tai\":\"\\u6b63\\u5e38\",\"bo_hui_yi_jian\":null,\"shen_he_id\":1,\"isWC\":1},{\"id\":8,\"zhou\":\"2017-10-17\",\"ri_qi\":\"2017-10-21\",\"kai_shi_time\":\"10:00\",\"jie_shu_time\":\"14:10\",\"mendian_pin_pai\":\"\\u79d1\\u96f6\\u56fd\\u9645\\u98df\\u54c1\",\"mendian_id\":6483,\"mendian_name\":\"\\u79d1\\u96f6\\u4e9a\\u6d32\\u5e97\",\"mendian_hao\":\"65868\",\"uid\":13,\"gongsi_name\":\"\\u79d1\\u96f6\\u98df\\u54c1\\u6709\\u9650\\u516c\\u53f8\",\"gongsi_id\":34,\"gongsi_week_start\":\"2017-03-14\",\"updated_at\":\"2017-10-18 13:31:49\",\"created_at\":\"2017-10-18 13:31:49\",\"deleted_at\":null,\"zhuang_tai\":\"\\u6b63\\u5e38\",\"bo_hui_yi_jian\":null,\"shen_he_id\":1,\"isWC\":1},{\"id\":9,\"zhou\":\"2017-10-17\",\"ri_qi\":\"2017-10-20\",\"kai_shi_time\":\"11:00\",\"jie_shu_time\":\"01:00\",\"mendian_pin_pai\":\"\\u79d1\\u96f6\\u56fd\\u9645\\u98df\\u54c1\",\"mendian_id\":6483,\"mendian_name\":\"\\u79d1\\u96f6\\u4e9a\\u6d32\\u5e97\",\"mendian_hao\":\"65868\",\"uid\":13,\"gongsi_name\":\"\\u79d1\\u96f6\\u98df\\u54c1\\u6709\\u9650\\u516c\\u53f8\",\"gongsi_id\":34,\"gongsi_week_start\":\"2017-03-14\",\"updated_at\":\"2017-10-18 13:31:49\",\"created_at\":\"2017-10-18 13:31:49\",\"deleted_at\":null,\"zhuang_tai\":\"\\u6b63\\u5e38\",\"bo_hui_yi_jian\":null,\"shen_he_id\":1,\"isWC\":0}]";

    // 周工作数据list
    private List<XunDianJiHua> mXunDianJiHuas = new ArrayList<>();

    // 选择查询如期
    private TextView mText_cha_xun_ri_qi;
    // 查询日期显示
    private TextView mText_cha_xun_ri_qi_value;

    // 本周日期
    private String mBenZhou = "";
    // 日期数据
    public String[] mRiQiData;
    // 本周每天已完成的记录数
    public int[] mJiLuNum = new int[7];

    // 周一节点
    private LinearLayout mLinear_zhou_yi;
    // 周二节点
    private LinearLayout mLinear_zhou_er;
    // 周三节点
    private LinearLayout mLinear_zhou_san;
    // 周四节点
    private LinearLayout mLinear_zhou_si;
    // 周五节点
    private LinearLayout mLinear_zhou_wu;
    // 周六节点
    private LinearLayout mLinear_zhou_liu;
    // 周日节点
    private LinearLayout mLinear_zhou_ri;

    // 每天序号 巡店计划
    private int ZhouYi = 1;
    private int ZhouEr = 1;
    private int ZhouSan = 1;
    private int ZhouSi = 1;
    private int ZhouWu = 1;
    private int ZhouLiu = 1;
    private int ZhouQi = 1;

    // 周标题周一
    private LinearLayout mLinear_zhou_yi_title;
    private TextView mTextview_title_yi;
    private TextView mTextview_title_yi_baifbi;
    // 周标题周二
    private LinearLayout mLinear_zhou_er_title;
    private TextView mTextview_title_er;
    private TextView mTextview_title_er_baifbi;
    // 周标题周三
    private LinearLayout mLinear_zhou_san_title;
    private TextView mTextview_title_san;
    private TextView mTextview_title_san_baifbi;
    // 周标题周四
    private LinearLayout mLinear_zhou_si_title;
    private TextView mTextview_title_si;
    private TextView mTextview_title_si_baifbi;
    // 周标题周五
    private LinearLayout mLinear_zhou_wu_title;
    private TextView mTextview_title_wu;
    private TextView mTextview_title_wu_baifbi;
    // 周标题周六
    private LinearLayout mLinear_zhou_liu_title;
    private TextView mTextview_title_liu;
    private TextView mTextview_title_liu_baifbi;
    // 周标题周日
    private LinearLayout mLinear_zhou_ri_title;
    private TextView mTextview_title_ri;
    private TextView mTextview_title_ri_baifbi;

    // Token
    public String mToken = null;

    // LoginModel 登录模型
    public static LoginModel mLoginModel = null;
    // 登录对象
    public static Login mLogin = null;

    // 开启线程
    public static Thread mThread = null;

    // 本周工作请求地址
    public String mBenZhouQingQiuURL = Config.URL+"/app/ben_zhou_xun_dian_ji_hua";

    // 用户选择查询日期
    public String mXuanZheChaXunRiQi = "";

    // dialog,加载
    public Dialog mWeiboDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_ben_zhou, container, false);
        mContext = getActivity();
        // 组件初始化
        ZhuJianInit();
        // 数据/值设置
        values();
        // 组件操作
        ZhuJianCaoZhuo();

        return mView;
    }
    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        // 选择查询日期
        mText_cha_xun_ri_qi = (TextView)mView.findViewById(R.id.text_cha_xun_ri_qi);
        // 选择查询日期值显示
        mText_cha_xun_ri_qi_value = (TextView)mView.findViewById(R.id.text_cha_xun_ri_qi_value);

        mTextview_zhou_title = (TextView)mView.findViewById(R.id.textview_zhou_title);
        // 周一
        mLinear_zhou_yi = (LinearLayout)mView.findViewById(R.id.linear_zhou_yi);
        // 周二
        mLinear_zhou_er = (LinearLayout)mView.findViewById(R.id.linear_zhou_er);
        // 周三
        mLinear_zhou_san = (LinearLayout)mView.findViewById(R.id.linear_zhou_san);
        // 周四
        mLinear_zhou_si = (LinearLayout)mView.findViewById(R.id.linear_zhou_si);
        // 周五
        mLinear_zhou_wu = (LinearLayout)mView.findViewById(R.id.linear_zhou_wu);
        // 周六
        mLinear_zhou_liu = (LinearLayout)mView.findViewById(R.id.linear_zhou_liu);
        // 周日
        mLinear_zhou_ri = (LinearLayout)mView.findViewById(R.id.linear_zhou_ri);

        // 周标题周一
        mLinear_zhou_yi_title = (LinearLayout)mView.findViewById(R.id.linear_zhou_yi_title);
        mTextview_title_yi = (TextView)mView.findViewById(R.id.textview_title_yi);
        mTextview_title_yi_baifbi = (TextView)mView.findViewById(R.id.textview_title_yi_baifbi);
        // 周标题周二
        mLinear_zhou_er_title = (LinearLayout)mView.findViewById(R.id.linear_zhou_er_title);
        mTextview_title_er = (TextView)mView.findViewById(R.id.textview_title_er);
        mTextview_title_er_baifbi = (TextView)mView.findViewById(R.id.textview_title_er_baifbi);
        // 周标题周三
        mLinear_zhou_san_title = (LinearLayout)mView.findViewById(R.id.linear_zhou_san_title);
        mTextview_title_san = (TextView)mView.findViewById(R.id.textview_title_san);
        mTextview_title_san_baifbi = (TextView)mView.findViewById(R.id.textview_title_san_baifbi);
        // 周标题周四
        mLinear_zhou_si_title = (LinearLayout)mView.findViewById(R.id.linear_zhou_si_title);
        mTextview_title_si = (TextView)mView.findViewById(R.id.textview_title_si);
        mTextview_title_si_baifbi = (TextView)mView.findViewById(R.id.textview_title_si_baifbi);
        // 周标题周五
        mLinear_zhou_wu_title = (LinearLayout)mView.findViewById(R.id.linear_zhou_wu_title);
        mTextview_title_wu = (TextView)mView.findViewById(R.id.textview_title_wu);
        mTextview_title_wu_baifbi = (TextView)mView.findViewById(R.id.textview_title_wu_baifbi);
        // 周标题周六
        mLinear_zhou_liu_title = (LinearLayout)mView.findViewById(R.id.linear_zhou_liu_title);
        mTextview_title_liu = (TextView)mView.findViewById(R.id.textview_title_liu);
        mTextview_title_liu_baifbi = (TextView)mView.findViewById(R.id.textview_title_liu_baifbi);
        // 周标题周日
        mLinear_zhou_ri_title = (LinearLayout)mView.findViewById(R.id.linear_zhou_ri_title);
        mTextview_title_ri = (TextView)mView.findViewById(R.id.textview_title_ri);
        mTextview_title_ri_baifbi = (TextView)mView.findViewById(R.id.textview_title_ri_baifbi);
    }
    /**
     * 值操作
     */
    public void values(){
        // loading
        LoadingStringEdit("加载中");
        // 登录数据库连接
        mLoginModel = LoginModel.get(getActivity());
        // Token查询,赋值
        mLogin = mLoginModel.getLogin(1);
        mToken = mLogin.getToken();

        // 本周计划请求
        qingQiuBenZhouJiHua();



    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        mTextview_zhou_title.setText(mBenZhou+"周");

        // 关闭loading
        WeiboDialogUtils.closeDialog(mWeiboDialog);

        // 选择查询日期
        mText_cha_xun_ri_qi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        String string = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                        mXuanZheChaXunRiQi = string;

                        mText_cha_xun_ri_qi_value.setText(mXuanZheChaXunRiQi);

                        // 请求数据
                        qingQiuBenZhouJiHua();
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    /**
     * 数据请求后处理
     * @param string
     */
    public void JiHuaQingQiuFanHui(String string){
        if(!string.equals("")){
            try {

                JSONObject jsonObject = new JSONObject(string);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("riqi").toString());
                if(jsonArray.length() > 0){
                    // 周赋值
                    mBenZhou = jsonArray.get(0).toString();

                    // 日期赋值
                    mRiQiData = new String[8];
                    mRiQiData[0] = jsonArray.get(0).toString();
                    mRiQiData[1] = jsonArray.get(1).toString();
                    mRiQiData[2] = jsonArray.get(2).toString();
                    mRiQiData[3] = jsonArray.get(3).toString();
                    mRiQiData[4] = jsonArray.get(4).toString();
                    mRiQiData[5] = jsonArray.get(5).toString();
                    mRiQiData[6] = jsonArray.get(6).toString();
                    mRiQiData[7] = jsonArray.get(7).toString();

                    // 数据赋值
                    mZhouJsonData = jsonObject.getString("data").toString();
                    // 值处理
                    setXunDianJiHuas();
                    // 显示计划
                    setShowJH();
                    // 组件操作
                    ZhuJianCaoZhuo();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * Handler
     */
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            /**
             *  msg.obj
             */
            if(msg.what==1){
                // 初始化显示组件
                initShowView();
                if(msg.obj.toString().equals("暂无数据")){
                    tiShi(getActivity(),"查询周无计划");
                }else{
                    JiHuaQingQiuFanHui(msg.obj.toString());
                }
            }
        }
    };

    /**
     * 请求本周数据
     */
    public void qingQiuBenZhouJiHua(){
        final OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("is","1");
        body.addFormDataPart("ri_qi",mXuanZheChaXunRiQi);
        final Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+mToken)
                .url(mBenZhouQingQiuURL)
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

    /**
     * 处理请求数据
     */
    public void setXunDianJiHuas() {
        try {
            JSONArray jsonArray = new JSONArray(mZhouJsonData);
            if(jsonArray.length() > 0){
                for(int i =0;i<jsonArray.length();i++){
                    XunDianJiHua xunDianJiHua = new XunDianJiHua();
                    if(jsonArray.get(i) != null){
                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                        xunDianJiHua.setZhou(jsonObject.getString("zhou").trim());
                        xunDianJiHua.setRiQi(jsonObject.getString("ri_qi").trim());
                        xunDianJiHua.setShiJian(jsonObject.getString("kai_shi_time").trim());
                        xunDianJiHua.setJSShiJian(jsonObject.getString("jie_shu_time").trim());
                        xunDianJiHua.setPingPaiStr(jsonObject.getString("mendian_pin_pai").trim());
                        xunDianJiHua.setMenDianHao(jsonObject.getString("mendian_hao").trim());
                        xunDianJiHua.setMenDianStr(jsonObject.getString("mendian_name").trim());
                        xunDianJiHua.setZhouStr(jsonObject.getString("zhouStr").trim());
                        xunDianJiHua.setIsWC(Integer.valueOf(jsonObject.getString("isWC").trim()));
                    }
                    mXunDianJiHuas.add(xunDianJiHua);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 显示计划
     *
     */
    public void setShowJH(){
        if(mXunDianJiHuas.size() > 0){


            List<XunDianJiHua> xunDianJiHuaList = new ArrayList<>();
            xunDianJiHuaList = mXunDianJiHuas;
            // 添加排序字段
            for(int i = 0;i<xunDianJiHuaList.size();i++){
                if(xunDianJiHuaList.get(i) != null){
                    xunDianJiHuaList.get(i).setOrderBy(Integer.valueOf(strSplit(":",xunDianJiHuaList.get(i).getShiJian())));
                }
            }
            // 排序
            Collections.sort(xunDianJiHuaList);

            // 累加每天的记录条数
            for(int i = 0;i<xunDianJiHuaList.size();i++){
                if(xunDianJiHuaList.get(i) != null){
                    WanChengnum(xunDianJiHuaList.get(i));
                }
            }

            // 生成TextView
            for(int i = 0;i<xunDianJiHuaList.size();i++){
                if(xunDianJiHuaList.get(i) != null){
                    CreateViewRiQi(xunDianJiHuaList.get(i));
                }
            }
        }

    }

    /**
     * 累加每天的记录条数
     * @param xunDianJiHua
     */
    public void WanChengnum(XunDianJiHua xunDianJiHua){
        int c = xunDianJiHua.getIsWC();
            if(mRiQiData[1].equals(xunDianJiHua.getRiQi())){
            if(c == 1) mJiLuNum[0]++;
        }else if(mRiQiData[2].equals(xunDianJiHua.getRiQi())){
            if(c == 1) mJiLuNum[1]++;
        }else if(mRiQiData[3].equals(xunDianJiHua.getRiQi())){
            if(c == 1) mJiLuNum[2]++;
        }else if(mRiQiData[4].equals(xunDianJiHua.getRiQi())){
            if(c == 1) mJiLuNum[3]++;
        }else if(mRiQiData[5].equals(xunDianJiHua.getRiQi())){
            if(c == 1) mJiLuNum[4]++;
        }else if(mRiQiData[6].equals(xunDianJiHua.getRiQi())){
            if(c == 1) mJiLuNum[5]++;
        }else if(mRiQiData[7].equals(xunDianJiHua.getRiQi())){
            if(c == 1) mJiLuNum[6]++;
        }
    }


    /**
     * 根据str截取字符串string返回字符串
     * @param str
     * @param string
     * @return
     */
    public String strSplit(String str,String string){
        String s = new String(string);
        String a[] = s.split(str);

        String returnStr = "";

        for (int i = 0;i<a.length;i++){
            returnStr += a[i];
        }

        return returnStr;
    }
    /**
     * 根据str截取字符串string数组
     * @param str
     * @param string
     * @return
     */
    public String[] strSplitArray(String str,String string){
        String s = new String(string);
        String a[] = s.split(str);

        return a;
    }

    /**
     * 组合viewStr
     * @param xunDianJiHua
     */
    public void CreateViewRiQi(XunDianJiHua xunDianJiHua){
        String[] RiQi = strSplitArray("-",xunDianJiHua.getRiQi());

        String stringRiQi = RiQi[1]+"-"+RiQi[2];
        // 记录显示父节点
        LinearLayout ll = new LinearLayout(mContext);
        ll.removeAllViews();
        // 编号
        int bianhao = 0;

        // 周几字符串
        String strs = xunDianJiHua.getZhouStr();
        //周几title
        LinearLayout ll_title = new LinearLayout(mContext);
        ll_title.removeAllViews();
        TextView tv_text = null;
        TextView tv_text1 = null;
        // 每天的完成率
        String wanChengLv = "0%";


        if(mRiQiData[1].equals(xunDianJiHua.getRiQi())){
            // 周一

            ll = mLinear_zhou_yi;

            bianhao = ZhouYi;

            ll_title = mLinear_zhou_yi_title;
            tv_text = mTextview_title_yi;
            tv_text1 = mTextview_title_yi_baifbi;

            float float1 = mJiLuNum[0];
            float float2 = ZhouYi;
            float float3 = float1/float2;
            wanChengLv = (int)(float3*100)+"%";
            ZhouYi++;

        }else if(mRiQiData[2].equals(xunDianJiHua.getRiQi())){
            // 周二

            ll = mLinear_zhou_er;
            bianhao = ZhouEr;
            ll_title = mLinear_zhou_er_title;
            tv_text = mTextview_title_er;
            tv_text1 = mTextview_title_er_baifbi;

            float float1 = mJiLuNum[1];
            float float2 = ZhouEr;
            float float3 = float1/float2;
            wanChengLv = (int)(float3*100)+"%";
            ZhouEr++;
        }else if(mRiQiData[3].equals(xunDianJiHua.getRiQi())){
            // 周三
            ll = mLinear_zhou_san;
            bianhao = ZhouSan;
            ll_title = mLinear_zhou_san_title;
            tv_text = mTextview_title_san;
            tv_text1 = mTextview_title_san_baifbi;

            float float1 = mJiLuNum[2];
            float float2 = ZhouSan;
            float float3 = float1/float2;
            wanChengLv = (int)(float3*100)+"%";
            ZhouSan++;
        }else if(mRiQiData[4].equals(xunDianJiHua.getRiQi())){
            // 周四
            ll = mLinear_zhou_si;
            bianhao = ZhouSi;
            ll_title = mLinear_zhou_si_title;
            tv_text = mTextview_title_si;
            tv_text1 = mTextview_title_si_baifbi;

            float float1 = mJiLuNum[3];
            float float2 = ZhouSi;
            float float3 = float1/float2;
            wanChengLv = (int)(float3*100)+"%";
            ZhouSi++;
        }else if(mRiQiData[5].equals(xunDianJiHua.getRiQi())){
            // 周五
            ll = mLinear_zhou_wu;
            bianhao = ZhouWu;
            ll_title = mLinear_zhou_wu_title;
            tv_text = mTextview_title_wu;
            tv_text1 = mTextview_title_wu_baifbi;

            float float1 = mJiLuNum[4];
            float float2 = ZhouWu;
            float float3 = float1/float2;
            wanChengLv = (int)(float3*100)+"%";
            ZhouWu++;
        }else if(mRiQiData[6].equals(xunDianJiHua.getRiQi())){
            // 周六
            ll = mLinear_zhou_liu;
            bianhao = ZhouLiu;
            ll_title = mLinear_zhou_liu_title;
            tv_text = mTextview_title_liu;
            tv_text1 = mTextview_title_liu_baifbi;

            float float1 = mJiLuNum[5];
            float float2 = ZhouLiu;
            float float3 = float1/float2;
            wanChengLv = (int)(float3*100)+"%";
            ZhouLiu++;
        }else if(mRiQiData[7].equals(xunDianJiHua.getRiQi())){
            // 周日
            ll = mLinear_zhou_ri;
            bianhao = ZhouQi;
            ll_title = mLinear_zhou_ri_title;
            tv_text = mTextview_title_ri;
            tv_text1 = mTextview_title_ri_baifbi;

            float float1 = mJiLuNum[6];
            float float2 = ZhouQi;
            float float3 = float1/float2;
            wanChengLv = (int)(float3*100)+"%";
            ZhouQi++;
        }
        // 创建标题
        if(ll != null && bianhao == 1){
            ll_title.setVisibility(View.VISIBLE);
            tv_text.setText(strs);
        }
        // 创建Text
        if(ll != null && bianhao > 0){
            if(bianhao == 1){
                ll.setVisibility(View.VISIBLE);
            }
            tv_text1.setText("完成率"+wanChengLv);
            addTextView(ll,bianhao,xunDianJiHua);
        }
    }

    /**
     * 添加TextView
     * @param linearLayout 显示节点
     * @param BianHao 编号
     * @param xunDianJiHua 数据
     *
     */
    public void addTextView(LinearLayout linearLayout,int BianHao,XunDianJiHua xunDianJiHua){
        // 详细
        String GongZhuo = xunDianJiHua.getShiJian()+"-"+xunDianJiHua.getJSShiJian()+" "
                +xunDianJiHua.getPingPaiStr()+" "+xunDianJiHua.getMenDianHao()+" "+xunDianJiHua.getMenDianStr();
        // 工作内容
        String strs1 = BianHao+". "+GongZhuo;

        LinearLayout ll = createLinearLayout();

        ll.addView(CreateTextView(strs1,1,0));

        if(xunDianJiHua.getIsWC() == 1){
            ll.addView(CreateTextView("已完成",0,R.color.zhuti));
        }else if(xunDianJiHua.getIsWC() == 0){
            ll.addView(CreateTextView("未完成",0,0));
        }

        linearLayout.addView(ll);
    }

    /**
     * 初始化显示组件
     */
    public void initShowView(){
        mXunDianJiHuas = new ArrayList<>();
        mJiLuNum = new int[7];
        // 初始化显示父组件
        mLinear_zhou_yi.removeAllViews();
        mLinear_zhou_yi.setVisibility(View.GONE);
        mLinear_zhou_yi_title.setVisibility(View.GONE);
        mLinear_zhou_er.removeAllViews();
        mLinear_zhou_er.setVisibility(View.GONE);
        mLinear_zhou_er_title.setVisibility(View.GONE);
        mLinear_zhou_san.removeAllViews();
        mLinear_zhou_san.setVisibility(View.GONE);
        mLinear_zhou_san_title.setVisibility(View.GONE);
        mLinear_zhou_si.removeAllViews();
        mLinear_zhou_si.setVisibility(View.GONE);
        mLinear_zhou_si_title.setVisibility(View.GONE);
        mLinear_zhou_wu.removeAllViews();
        mLinear_zhou_wu.setVisibility(View.GONE);
        mLinear_zhou_wu_title.setVisibility(View.GONE);
        mLinear_zhou_liu.removeAllViews();
        mLinear_zhou_liu.setVisibility(View.GONE);
        mLinear_zhou_liu_title.setVisibility(View.GONE);
        mLinear_zhou_ri.removeAllViews();
        mLinear_zhou_ri.setVisibility(View.GONE);
        mLinear_zhou_ri_title.setVisibility(View.GONE);
        // 初始化编号
        ZhouYi = 1;
        ZhouEr = 1;
        ZhouSan = 1;
        ZhouSi = 1;
        ZhouWu = 1;
        ZhouLiu = 1;
        ZhouQi = 1;
    }

    /**
     * 创建TextView
     * @param string
     * @param is 是否布局weight,大于1这取这个值布局
     * @param YanSe 颜色id
     * @return TextView组件
     */
    public TextView CreateTextView(String string,int is,int YanSe){
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParam;
        if(is >= 1){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,is);
        }else{
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        layoutParam.setMargins(0,0,0,5);

        textView.setLayoutParams(layoutParam);

        if(YanSe != 0){
            textView.setTextColor(getResources().getColor(YanSe));
        }

        textView.setText(string);
        return textView;
    }

    /**
     * 创建LinearLayout
     * @return
     */
    public LinearLayout createLinearLayout(){
        LinearLayout ll = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParam =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setLayoutParams(layoutParam);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        return ll;
    }

    /**
     * 提示
     *
     * @param context
     */
    public static void tiShi(Context context, String string) {
        Toast mToast = null;
        if (mToast == null) {
            mToast = Toast.makeText(context, "",
                    Toast.LENGTH_LONG);
            LinearLayout layout = (LinearLayout) mToast.getView();
            TextView tv = (TextView) layout.getChildAt(0);
            tv.setTextSize(20);
        }
        mToast.setGravity(Gravity.BOTTOM, 0, 10);
        mToast.setText(string);
        mToast.show();
    }

    /**
     * loading 浮层
     *
     * @param logingString 提示文字
     */
    public void LoadingStringEdit(String logingString){
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(),logingString);
    }
}
