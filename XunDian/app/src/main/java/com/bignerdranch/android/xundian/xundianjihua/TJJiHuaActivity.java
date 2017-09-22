package com.bignerdranch.android.xundian.xundianjihua;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.NeiYeCommActivity;
import com.bignerdranch.android.xundian.comm.XunDianJiHua;
import com.bignerdranch.android.xundian.model.XunDianJiHuaModel;
import com.bignerdranch.android.xundian.xundianguanli.XunDianActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/9/19.
 */

public class TJJiHuaActivity extends NeiYeCommActivity implements NeiYeCommActivity.Callbacks{
    private static final String EXTRA = "com.bignerdranch.android.xundian.xundianguanli.TJJiHuaActivity";

    private AlertDialog alertDialog1;

    // 选择周
    private TextView mTextview_zhou;
    // 选择周显示
    private TextView mTextview_zhou_value;
    // 周数据
    public String[] mZhouData;

    // 选择日期
    private TextView mTextview_ri_qi;
    // 日期显示
    private TextView mTextview_ri_qi_value;
    // 日期数据
    public String[] mRiQiData;

    // 选择时间
    private TextView mTextview_shi_jian;
    // 显示时间
    private TextView mTextview_shi_jian_value;
    // 时间对象
    public Calendar ct;
    // 选择结束时间
    private TextView mTextview_js_shi_jian;
    // 显示结束时间
    private TextView mTextview_js_shi_jian_value;

    // 选择品牌
    private TextView mTextview_pin_pai;
    // 显示品牌
    private TextView mTextview_pin_pai_value;

    // 选择门店名称
    private TextView mTextview_ming_cheng;
    // 显示门店名称
    private TextView mTextview_ming_cheng_value;

    // 显示门店店号
    private TextView mTextview_dian_hao_value;

    // 工作录入
    private Button mButton_gong_zuo;

    // 删除工作记录
    private Button mButton_shan_chu_gong;

    // 提交工作表
    private Button mButton_ti_jiao_gzb;

    // LoginModel 登录模型
    public static XunDianJiHuaModel mXunDianJiHuaModel;

    // 周一节点
    public LinearLayout mLinear_zhou_yi;
    // 周二节点
    public LinearLayout mLinear_zhou_er;
    // 周三节点
    public LinearLayout mLinear_zhou_san;
    // 周四节点
    public LinearLayout mLinear_zhou_si;
    // 周五节点
    public LinearLayout mLinear_zhou_wu;
    // 周六节点
    public LinearLayout mLinear_zhou_liu;
    // 周日节点
    public LinearLayout mLinear_zhou_ri;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    // 巡店计划集合
    public List<XunDianJiHua> mXunDianJiHuas = new ArrayList<>();

    // 巡店计划对象
    public XunDianJiHua mXunDianJiHua = new XunDianJiHua();

    // 当前下标(数据库id)
    public int mId = mXunDianJiHuas.size();

    // 每天序号 巡店计划
    public int ZhouYi = 1;
    public int ZhouEr = 1;
    public int ZhouSan = 1;
    public int ZhouSi = 1;
    public int ZhouWu = 1;
    public int ZhouLiu = 1;
    public int ZhouQi = 1;

    // 修改下标
    public int mXiuGaiKey = 1000;

    public static Intent newIntent(Context packageContext, int intIsId){
        Intent i = new Intent(packageContext,TJJiHuaActivity.class);
        i.putExtra(EXTRA,intIsId);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tian_jia_ji_hua);

        mContext = this;
        // 组件初始化
        ZhuJianInit();

        // 组件操作
        ZhuJianCaoZhuo();
        // 数据/值设置
        values();

    }

    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        // title设置
        mTitle_nei_ye = (TextView)findViewById(R.id.title_nei_ye);
        // 周一
        mLinear_zhou_yi = (LinearLayout)findViewById(R.id.linear_zhou_yi);
        // 周二
        mLinear_zhou_er = (LinearLayout)findViewById(R.id.linear_zhou_er);
        // 周三
        mLinear_zhou_san = (LinearLayout)findViewById(R.id.linear_zhou_san);
        // 周四
        mLinear_zhou_si = (LinearLayout)findViewById(R.id.linear_zhou_si);
        // 周五
        mLinear_zhou_wu = (LinearLayout)findViewById(R.id.linear_zhou_wu);
        // 周六
        mLinear_zhou_liu = (LinearLayout)findViewById(R.id.linear_zhou_liu);
        // 周日
        mLinear_zhou_ri = (LinearLayout)findViewById(R.id.linear_zhou_ri);
        // 周
        mTextview_zhou = (TextView)findViewById(R.id.textview_zhou);
        // 显示周
        mTextview_zhou_value = (TextView)findViewById(R.id.textview_zhou_value);
        // 日期
        mTextview_ri_qi = (TextView)findViewById(R.id.textview_ri_qi);
        // 显示日期
        mTextview_ri_qi_value = (TextView)findViewById(R.id.textview_ri_qi_value);
        // 时间
        mTextview_shi_jian = (TextView)findViewById(R.id.textview_shi_jian);
        // 显示时间
        mTextview_shi_jian_value = (TextView)findViewById(R.id.textview_shi_jian_value);
        // 结束时间
        mTextview_js_shi_jian = (TextView)findViewById(R.id.textview_js_shi_jian);
        // 显示结束时间
        mTextview_js_shi_jian_value = (TextView)findViewById(R.id.textview_js_shi_jian_value);
        // 品牌
        mTextview_pin_pai = (TextView)findViewById(R.id.textview_pin_pai);
        // 显示品牌
        mTextview_pin_pai_value = (TextView)findViewById(R.id.textview_pin_pai_value);
        // 门店名称
        mTextview_ming_cheng = (TextView)findViewById(R.id.textview_ming_cheng);
        // 显示门店名称
        mTextview_ming_cheng_value = (TextView)findViewById(R.id.textview_ming_cheng_value);
        // 显示门店店号
        mTextview_dian_hao_value = (TextView)findViewById(R.id.textview_dian_hao_value);
        // 工作录入
        mButton_gong_zuo = (Button)findViewById(R.id.button_gong_zuo);
        // 删除记录
        mButton_shan_chu_gong = (Button)findViewById(R.id.button_shan_chu_gong);
        // 提交工作表
        mButton_ti_jiao_gzb = (Button)findViewById(R.id.button_ti_jiao_gzb);
    }

    /**
     * 值操作
     */
    public void values(){
        // 开启loading
        LoadingStringEdit("数据加载中");
        // Token赋值
        setToken(mContext);
        // 巡店计划model
        mXunDianJiHuaModel = XunDianJiHuaModel.get(mContext);
        // 设置周数据,日期数据
        setZhouData();
        // 品牌请求
        pinPaiSearch();
        // 请求店铺
        menDianSearch();
        // 数据库查询巡店计划
        DatabaseXunDian();
        // 显示数据库计划
        setShowJH();
    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        mTitle_nei_ye.setText(R.string.tj_xun_dian_ji_hua);

        // 显示周选择项
        mTextview_zhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                alertBuilder.setItems(mZhouData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {
                        // 显示选择周
                        mTextview_zhou_value.setText(mZhouData[index]);
                        // 更新用户选择周
                        mXunDianJiHua.setZhou(mZhouData[index]);

                        alertDialog1.dismiss();
                    }
                });
                alertDialog1 = alertBuilder.create();
                alertDialog1.show();
            }
        });

        // 选择日期 mRiQiData
        mTextview_ri_qi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                alertBuilder.setItems(mRiQiData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {
                        // 显示选择日期
                        mTextview_ri_qi_value.setText(mRiQiData[index]);
                        // 更新用户选择日期
                        mXunDianJiHua.setRiQi(mRiQiData[index]);

                        alertDialog1.dismiss();
                    }
                });
                alertDialog1 = alertBuilder.create();
                alertDialog1.show();
            }
        });

        // 选择时间
        mTextview_shi_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ct = Calendar.getInstance();
                new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        ct.set(Calendar.HOUR_OF_DAY,i);
                        ct.set(Calendar.MINUTE,i1);
                        String string = String.valueOf(DateFormat.format("HH:mm",ct));
                        mTextview_shi_jian_value.setText(string);
                        // 存入对象
                        mXunDianJiHua.setShiJian(string);
                    }

                },ct.get(Calendar.HOUR_OF_DAY),ct.get(Calendar.MINUTE),true).show();
            }
        });

        // 选择结束时间
        mTextview_js_shi_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ct = Calendar.getInstance();
                new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        ct.set(Calendar.HOUR_OF_DAY,i);
                        ct.set(Calendar.MINUTE,i1);
                        String string = String.valueOf(DateFormat.format("HH:mm",ct));
                        mTextview_js_shi_jian_value.setText(string);
                        // 存入对象
                        mXunDianJiHua.setJSShiJian(string);
                    }

                },ct.get(Calendar.HOUR_OF_DAY),ct.get(Calendar.MINUTE),true).show();
            }
        });

        // 品牌选择
        mTextview_pin_pai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                alertBuilder.setItems(mMengDianPingPaiData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {

                        mTextview_pin_pai_value.setText(mMengDianPingPaiData[index]);
                        // 门店品牌
                        int id =ChanKanId(mMengDianPingpaiJsonData,mMengDianPingPaiData[index]);

                        mMen_Dian_ping_pai = mMengDianPingPaiData[index];
                        // 请求店铺
                        menDianSearch();

                        // 存入id
                        mXunDianJiHua.setPingPaiId(id);
                        // 存入名称
                        mXunDianJiHua.setPingPaiStr(mMengDianPingPaiData[index]);

                        alertDialog1.dismiss();
                    }
                });
                alertDialog1 = alertBuilder.create();
                alertDialog1.show();
            }
        });

        // 门店选择
        mTextview_ming_cheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);

                alertBuilder.setItems(mMengDianData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {

                        mTextview_ming_cheng_value.setText(mMengDianData[index]);

                        int id = ChanKanId(mMengDianJsonData,mMengDianData[index]);
                        // 店号
                        String dianHao = ChanKanKey(mMengDianJsonData,mMengDianData[index],"men_dian_hao");
                        // 显示店号
                        mTextview_dian_hao_value.setText(dianHao);
                        // 存入店号
                        mXunDianJiHua.setMenDianHao(dianHao);

                        // 存储选择门店id
                        mXunDianJiHua.setMenDianId(id);
                        // 存储用户选择门店
                        mXunDianJiHua.setMenDianStr(mMengDianData[index]);

                        alertDialog1.dismiss();

                    }
                });
                alertDialog1 = alertBuilder.create();
                alertDialog1.show();
            }
        });

        // 工作录入
        mButton_gong_zuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 存入值
                setDatabase();
            }
        });

        // 删除记录
        mButton_shan_chu_gong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mXiuGaiKey != 1000){
                    // 数据库删除
                    mXunDianJiHuaModel.deleteXunDianJiHua(String.valueOf(mXunDianJiHuas.get(mXiuGaiKey).getId()));
                    // list删除
                    mXunDianJiHuas.remove(mXiuGaiKey);
                    // 更新操作
                    initAddDelete();
                }
            }
        });

        // 提交工作表
        mButton_ti_jiao_gzb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    /**
     * 设置周数据,得到下周周一日期,日期数据
     */
    public void setZhouData(){
        mZhouData = new String[1];
        mRiQiData = new String[7];

//        mZhouData[0] = "2017-09-18周";
        SimpleDateFormat simpleDateFormats =new SimpleDateFormat("y-M-d", Locale.CHINA);
        Calendar calendar=Calendar.getInstance(Locale.CHINA);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        //当前时间，貌似多余，其实是为了所有可能的系统一致
        calendar.setTimeInMillis(System.currentTimeMillis());
        // 获取当前周的周日
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        // 周日加一天
        calendar.add(Calendar.DATE,1);
        // 添加周数据
        mZhouData[0] = simpleDateFormats.format(calendar.getTime())+"周";
        mRiQiData[0] = simpleDateFormats.format(calendar.getTime());
        // 添加日期数据
        for(int i = 1;i<7;i++){
            calendar.add(Calendar.DATE,1);
            mRiQiData[i] = simpleDateFormats.format(calendar.getTime());
        }

    }

    /**
     * 验证数据及存入数据库
     */
    public void setDatabase(){
        if(mXunDianJiHua.getZhou() == null || mXunDianJiHua.getZhou().equals("")){
            tiShi(mContext,"请选择周");
        }else if(mXunDianJiHua.getRiQi() == null || mXunDianJiHua.getRiQi().equals("")){
            tiShi(mContext,"请选择日期");
        }else if(mXunDianJiHua.getShiJian() == null || mXunDianJiHua.getShiJian().equals("")){
            tiShi(mContext,"请选择开始日期");
        }else if(mXunDianJiHua.getJSShiJian() == null || mXunDianJiHua.getJSShiJian().equals("")){
            tiShi(mContext,"请选择结束日期");
        }else if(mXunDianJiHua.getPingPaiStr() == null || mXunDianJiHua.getPingPaiStr().equals("")){
            tiShi(mContext,"请选择门店品牌");
        }else if(mXunDianJiHua.getMenDianStr() == null || mXunDianJiHua.getMenDianStr().equals("")){
            tiShi(mContext,"请选择门店名称");
        }else{
            if(mXiuGaiKey == 1000){
                mXunDianJiHua.setId(mId);
            }
            // 添加数据
            mXunDianJiHuaModel.addIsUpdate(mXunDianJiHua);
            // 添加到List
            if(mXiuGaiKey != 1000){
                mXunDianJiHuas.set(mXiuGaiKey,mXunDianJiHua);
            }else{
                mXunDianJiHuas.add(mXunDianJiHua);
            }

            mId = mXunDianJiHuas.size();

            // 更新下标
            initAddDelete();
        }
    }

    /**
     * 添加删除更新操作
     */
    public void initAddDelete(){
        mXiuGaiKey = 1000;
        // 初始化对象
        mXunDianJiHua = new XunDianJiHua();
        // 清空输入内容
        qingKongNeiRong();
        // 刷新显示
        setShowJH();
    }

    /**
     * 查询数据库数据
     */
    public void DatabaseXunDian(){
        // 查询数据
        mXunDianJiHuas = mXunDianJiHuaModel.getXunDianJiHuas();
        //
        mId = mXunDianJiHuas.size();
    }

    /**
     * 显示计划
     *
     */
    public void setShowJH(){
        // 初始化显示组件
        initShowView();
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
            // 生成TextView
            for(int i = 0;i<xunDianJiHuaList.size();i++){
                if(xunDianJiHuaList.get(i) != null){
                    CreateViewRiQi(xunDianJiHuaList.get(i),i);
                }
            }
        }

    }

    /**
     * 初始化显示组件
     */
    public void initShowView(){
        // 初始化显示父组件
        mLinear_zhou_yi.removeAllViews();
        mLinear_zhou_yi.setVisibility(View.GONE);
        mLinear_zhou_er.removeAllViews();
        mLinear_zhou_er.setVisibility(View.GONE);
        mLinear_zhou_san.removeAllViews();
        mLinear_zhou_san.setVisibility(View.GONE);
        mLinear_zhou_si.removeAllViews();
        mLinear_zhou_si.setVisibility(View.GONE);
        mLinear_zhou_wu.removeAllViews();
        mLinear_zhou_wu.setVisibility(View.GONE);
        mLinear_zhou_liu.removeAllViews();
        mLinear_zhou_liu.setVisibility(View.GONE);
        mLinear_zhou_ri.removeAllViews();
        mLinear_zhou_ri.setVisibility(View.GONE);
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
     * 组合viewStr
     * @param xunDianJiHua
     * @param i 当前下标
     */
    public void CreateViewRiQi(XunDianJiHua xunDianJiHua,int i){
        String[] RiQi = strSplitArray("-",xunDianJiHua.getRiQi());
        String stringRiQi = RiQi[1]+"-"+RiQi[2];
        // 详细
        String GongZhuo = xunDianJiHua.getShiJian()+"-"+xunDianJiHua.getJSShiJian()+" "
                +xunDianJiHua.getPingPaiStr()+" "+xunDianJiHua.getMenDianHao()+" "+xunDianJiHua.getMenDianStr();

        if(mRiQiData[0].equals(xunDianJiHua.getRiQi())){
            // 周一
            String strs = stringRiQi+" 周一";
            if(ZhouYi == 1){
                mLinear_zhou_yi.setVisibility(View.VISIBLE);
                mLinear_zhou_yi.addView(CreateTextView(strs,1000));
            }
            addTextView(mLinear_zhou_yi,ZhouYi,GongZhuo,i);
            ZhouYi++;

        }else if(mRiQiData[1].equals(xunDianJiHua.getRiQi())){
           // 周二
            String strs = stringRiQi+" 周二";
            if(ZhouEr == 1){
                mLinear_zhou_er.setVisibility(View.VISIBLE);
                mLinear_zhou_er.addView(CreateTextView(strs,1000));
            }
            addTextView(mLinear_zhou_er,ZhouEr,GongZhuo,i);
            ZhouEr++;
        }else if(mRiQiData[2].equals(xunDianJiHua.getRiQi())){
            // 周三
            String strs = stringRiQi+" 周三";
            if(ZhouSan == 1){
                mLinear_zhou_san.setVisibility(View.VISIBLE);
                mLinear_zhou_san.addView(CreateTextView(strs,1000));
            }
            addTextView(mLinear_zhou_san,ZhouSan,GongZhuo,i);
            ZhouSan++;
        }else if(mRiQiData[3].equals(xunDianJiHua.getRiQi())){
            // 周四
            String strs = stringRiQi+" 周四";
            if(ZhouSi == 1){
                mLinear_zhou_si.setVisibility(View.VISIBLE);
                mLinear_zhou_si.addView(CreateTextView(strs,1000));
            }
            addTextView(mLinear_zhou_si,ZhouSi,GongZhuo,i);
            ZhouSi++;
        }else if(mRiQiData[4].equals(xunDianJiHua.getRiQi())){
            // 周五
            String strs = stringRiQi+" 周五";
            if(ZhouWu == 1){
                mLinear_zhou_wu.setVisibility(View.VISIBLE);
                mLinear_zhou_wu.addView(CreateTextView(strs,1000));
            }
            addTextView(mLinear_zhou_wu,ZhouWu,GongZhuo,i);
            ZhouWu++;
        }else if(mRiQiData[5].equals(xunDianJiHua.getRiQi())){
            // 周六
            String strs = stringRiQi+" 周六";
            if(ZhouLiu == 1){
                mLinear_zhou_liu.setVisibility(View.VISIBLE);
                mLinear_zhou_liu.addView(CreateTextView(strs,1000));
            }
            addTextView(mLinear_zhou_liu,ZhouLiu,GongZhuo,i);
            ZhouLiu++;
        }else if(mRiQiData[6].equals(xunDianJiHua.getRiQi())){
            // 周日
            String strs = stringRiQi+" 周日";
            if(ZhouQi == 1){
                mLinear_zhou_ri.setVisibility(View.VISIBLE);
                mLinear_zhou_ri.addView(CreateTextView(strs,1000));
            }
            addTextView(mLinear_zhou_ri,ZhouQi,GongZhuo,i);
            ZhouQi++;
        }
    }

    /**
     * 添加TextView
     * @param linearLayout 显示节点
     * @param BianHao 编号
     * @param str1 工作详情
     * @param i 1000为不添加点击事件
     *
     */
    public void addTextView(LinearLayout linearLayout,int BianHao,String str1,int i){
        // 工作内容
        String strs1 = BianHao+". "+str1;
        linearLayout.addView(CreateTextView(strs1,i));
    }

    /**
     * 创建TextView
     * @param string
     * @param i 当前下标
     * @return TextView组件
     */
    public TextView CreateTextView(String string,int i){
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParam.setMargins(0,0,0,5);
        textView.setLayoutParams(layoutParam);
        textView.setText(string);
        // 显示内容
        final int xiaobiao = i;
        if(xiaobiao != 1000){
            textView.setOnClickListener(new View.OnClickListener() {
                final int xiaoBiao = xiaobiao;
                @Override
                public void onClick(View view) {
                    mXiuGaiKey = xiaoBiao;
                    qingKongNeiRong();
                }
            });
        }

        return textView;
    }

    /**
     * 清空输入内容/设置内容
     */
    public void qingKongNeiRong(){
        // 更改修改文字
        mButton_gong_zuo.setText(R.string.gong_zuo_lu_ru);
        // 隐藏删除
        mButton_shan_chu_gong.setVisibility(View.GONE);

        mTextview_zhou_value.setText("");
        mTextview_ri_qi_value.setText("");
        mTextview_shi_jian_value.setText("");
        mTextview_js_shi_jian_value.setText("");
        mTextview_pin_pai_value.setText("");
        mTextview_ming_cheng_value.setText("");
        mTextview_dian_hao_value.setText("");

        if(mXiuGaiKey != 1000){
            // 更改显示文字
            mButton_gong_zuo.setText(R.string.xiu_gai_ji_hua);
            // 显示删除
            mButton_shan_chu_gong.setVisibility(View.VISIBLE);

            mXunDianJiHua = mXunDianJiHuas.get(mXiuGaiKey);
            mTextview_zhou_value.setText(mXunDianJiHua.getZhou());
            mTextview_ri_qi_value.setText(mXunDianJiHua.getRiQi());
            mTextview_shi_jian_value.setText(mXunDianJiHua.getShiJian());
            mTextview_js_shi_jian_value.setText(mXunDianJiHua.getJSShiJian());
            mTextview_pin_pai_value.setText(mXunDianJiHua.getPingPaiStr());
            mTextview_ming_cheng_value.setText(mXunDianJiHua.getMenDianStr());
            mTextview_dian_hao_value.setText(mXunDianJiHua.getMenDianHao());
        }
    }

    /**
     * 数据请求成功回调
     * @param string 数据
     * @param is 1 品牌 2门店
     */
    public void shuJuHuiDiao(String string,int is){
        if(is == 1){
            mMengDianPingpaiJsonData = string;
            setData(mMengDianPingpaiJsonData,1);
        }else if(is == 2){
            mMengDianJsonData = string;
            setData(mMengDianJsonData,2);
        }

    }

    @Override
    public void onAttachedToWindow(){
        mCallbacks = (Callbacks)mContext;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 销毁回调
        mCallbacks = null;
    }
}
