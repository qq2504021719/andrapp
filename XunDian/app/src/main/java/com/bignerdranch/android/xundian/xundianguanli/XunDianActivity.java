package com.bignerdranch.android.xundian.xundianguanli;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.comm.NeiYeCommActivity;
import com.bignerdranch.android.xundian.comm.XunDianCanShu;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */

public class XunDianActivity extends NeiYeCommActivity {

    private static final String TAG = "XunDianActivity";

    // 巡店定位信息,店铺id
    public String mXunDian;

    public String mXunDianJSONData = "[{\"name\":\"\\u996e\\u6599\\u6392\\u9762\\u62cd\\u7167\",\"tian_xie_fang_shi\":\"\\u6587\\u672c\",\"is_pai_zhao\":\"\\" +
            "u662f\",\"is_bi_tian\":1},{\"name\":\"\\u996e\\u6599\\u6570\\u91cf\",\"tian_xie_fang_shi\":\"\\u6570\\u5b57\",\"is_pai_zhao\":\"\\u5426\"," +
            "\"is_bi_tian\":1},{\"name\":\"\\u65e0\\u6cd5\\u8ba2\\u8d27\\u5546\\u54c1\",\"tian_xie_fang_shi\":\"\\u9009\\u62e9\\u5668\",\"is_pai_zhao\":" +
            "\"\\u5426\",\"xuan_ze_qi\":[\"\\u6e20\\u9053\\u65e0\\u8d27\",\"\\u5176\\u4ed6\\u539f\\u56e0\"],\"is_bi_tian\":1},{\"name\":\"\\u996e\\u6599" +
            "\\u51b0\\u67dc\\u62cd\\u7167\",\"tian_xie_fang_shi\":\"\\u6587\\u672c\",\"is_pai_zhao\":\"\\u662f\",\"is_bi_tian\":1},{\"name\":\"\\u9175\\" +
            "u6bcd\\u9762\\u5305\\u5e93\\u5b58\",\"tian_xie_fang_shi\":\"\\u6570\\u5b57\",\"is_pai_zhao\":\"\\u662f\",\"is_bi_tian\":0},{\"name\":\"\\u9999" +
            "\\u98d8\\u98d8\\u5355\\u676f\\u5e93\\u5b58\",\"tian_xie_fang_shi\":\"\\u6570\\u5b57\",\"is_pai_zhao\":\"\\u5426\",\"is_bi_tian\":1}]";

    // 巡店参数list
    public List<XunDianCanShu> mXunDianCanShus = new ArrayList<>();

    public static Intent newIntent(Context packageContext, String string){
        Intent i = new Intent(packageContext,XunDianActivity.class);
        i.putExtra(TAG,string);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.xun_dian);

        mXunDian = getIntent().getStringExtra(TAG);

        setData(mXunDianJSONData);

        // 组件初始化
        ZhuJianInit();

        // 组件操作
        ZhuJianCaoZhuo();

        Log.i("巡店",mXunDian);

    }

    /**
     * 将 jsonString转换为对象并存储到list中
     * @param jsonString
     */
    public void setData(String jsonString){
        try {
            Log.i("巡店","转换"+jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            Log.i("巡店","转换"+jsonObject.keys().toString());

            Iterator inter =  jsonObject.keys();

            while(inter.hasNext()){
                String a = (String)inter.next();
                Log.i("巡店",a);
                Log.i("巡店",jsonObject.getString(a));
            }
        }catch (JSONException e){

        }
    }

    /**
     * 组件初始化
     */
    public void ZhuJianInit(){
        mTitle_nei_ye = (TextView)findViewById(R.id.title_nei_ye);
    }


    /**
     * 组件操作, 操作
     */
    public void ZhuJianCaoZhuo(){
        // 设置标题
        mTitle_nei_ye.setText(R.string.gong_zuo_zhong_xin_xun_dian_guan_li);

    }

}
