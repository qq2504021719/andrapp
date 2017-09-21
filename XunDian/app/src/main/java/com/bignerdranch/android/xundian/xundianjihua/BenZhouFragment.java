package com.bignerdranch.android.xundian.xundianjihua;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.XunDianJiHua;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/9/20.
 */

public class BenZhouFragment extends Fragment{

    private View mView;

    private Context mContext;

    private TextView mTextview_zhou_title;

    // 周工作数据String
    private String mZhouJsonData = "[{\"zhou\":\"2017-09-18\",\"riRi\":\"2017-09-18\",\"KSShijian\":\"09:10\",\"JSShiJian\":\"09:30\",\"pingPai\":\"\\u4f0d\\u7f18\",\"menDianHao\":\"10057\",\"menMingCheng\":\"\\u5929\\u5c71\\u5e97\",\"isWC\":1},{\"zhou\":\"2017-09-18\",\"riRi\":\"2017-09-18\",\"KSShijian\":\"11:10\",\"JSShiJian\":\"11:40\",\"pingPai\":\"\\u53ef\\u8fea\",\"menDianHao\":\"10057\",\"menMingCheng\":\"\\u4e0a\\u6d77\\u5b9c\\u5ddd\\u5e97\",\"isWC\":1},{\"zhou\":\"2017-09-18\",\"riRi\":\"2017-09-19\",\"KSShijian\":\"10:10\",\"JSShiJian\":\"10:30\",\"pingPai\":\"\\u4f0d\\u7f18\",\"menDianHao\":\"10057\",\"menMingCheng\":\"\\u5408\\u80a5\\u5e97\",\"isWC\":1},{\"zhou\":\"2017-09-18\",\"riRi\":\"2017-09-20\",\"KSShijian\":\"13:10\",\"JSShiJian\":\"14:30\",\"pingPai\":\"\\u597d\\u5fb7\",\"menDianHao\":\"10057\",\"menMingCheng\":\"\\u8398\\u5e84\\u5e97\",\"isWC\":0}]";

    // 周工作数据list
    private List<XunDianJiHua> mXunDianJiHuas = new ArrayList<>();

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
        // Token赋值

        // 获取本周周一时间
        setBenZhou();
        // 值处理
        setXunDianJiHuas();
        // 显示计划
        setShowJH();
    }

    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        mTextview_zhou_title.setText(mBenZhou+"周");
    }

    /**
     * 设置当前周周一时间,本周每天的日期
     */
    public void setBenZhou() {
        SimpleDateFormat simpleDateFormats =new SimpleDateFormat("y-MM-d", Locale.CHINA);
        Calendar calendar=Calendar.getInstance(Locale.CHINA);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        //当前时间，貌似多余，其实是为了所有可能的系统一致
        calendar.setTimeInMillis(System.currentTimeMillis());
        // 获取当前周周一的日期
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        // 添加周数据
        mBenZhou = simpleDateFormats.format(calendar.getTime());

        mRiQiData = new String[7];
        mRiQiData[0] = simpleDateFormats.format(calendar.getTime());
        // 添加日期数据
        for(int i = 1;i<7;i++){
            calendar.add(Calendar.DATE,1);
            mRiQiData[i] = simpleDateFormats.format(calendar.getTime());
        }
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
                        xunDianJiHua.setRiQi(jsonObject.getString("riRi").trim());
                        xunDianJiHua.setShiJian(jsonObject.getString("KSShijian").trim());
                        xunDianJiHua.setJSShiJian(jsonObject.getString("JSShiJian").trim());
                        xunDianJiHua.setPingPaiStr(jsonObject.getString("pingPai").trim());
                        xunDianJiHua.setMenDianHao(jsonObject.getString("menDianHao").trim());
                        xunDianJiHua.setMenDianStr(jsonObject.getString("menMingCheng").trim());
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
            // 初始化显示组件
            initShowView();

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
            if(mRiQiData[0].equals(xunDianJiHua.getRiQi())){
            if(c == 1) mJiLuNum[0]++;
        }else if(mRiQiData[1].equals(xunDianJiHua.getRiQi())){
            if(c == 1) mJiLuNum[1]++;
        }else if(mRiQiData[2].equals(xunDianJiHua.getRiQi())){
            if(c == 1) mJiLuNum[2]++;
        }else if(mRiQiData[3].equals(xunDianJiHua.getRiQi())){
            if(c == 1) mJiLuNum[3]++;
        }else if(mRiQiData[4].equals(xunDianJiHua.getRiQi())){
            if(c == 1) mJiLuNum[4]++;
        }else if(mRiQiData[5].equals(xunDianJiHua.getRiQi())){
            if(c == 1) mJiLuNum[5]++;
        }else if(mRiQiData[6].equals(xunDianJiHua.getRiQi())){
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
        LinearLayout ll = null;
        // 编号
        int bianhao = 0;

        // 周几字符串
        String strs = null;
        //周几title
        LinearLayout ll_title = null;
        TextView tv_text = null;
        TextView tv_text1 = null;
        // 每天的完成率
        String wanChengLv = "0%";

        if(mRiQiData[0].equals(xunDianJiHua.getRiQi())){
            // 周一
            strs = stringRiQi+" 周一";

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

        }else if(mRiQiData[1].equals(xunDianJiHua.getRiQi())){
            // 周二
            strs = stringRiQi+" 周二";
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
        }else if(mRiQiData[2].equals(xunDianJiHua.getRiQi())){
            // 周三
            strs = stringRiQi+" 周三";
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
        }else if(mRiQiData[3].equals(xunDianJiHua.getRiQi())){
            // 周四
            strs = stringRiQi+" 周四";
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
        }else if(mRiQiData[4].equals(xunDianJiHua.getRiQi())){
            // 周五
            strs = stringRiQi+" 周五";
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
        }else if(mRiQiData[5].equals(xunDianJiHua.getRiQi())){
            // 周六
            strs = stringRiQi+" 周六";
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
        }else if(mRiQiData[6].equals(xunDianJiHua.getRiQi())){
            // 周日
            strs = stringRiQi+" 周日";
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

}
