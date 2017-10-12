package com.bignerdranch.android.xundian.kaoqing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.xundian.LoginActivity;
import com.bignerdranch.android.xundian.R;
import com.bignerdranch.android.xundian.comm.CommActivity;
import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.comm.WeiboDialogUtils;
import com.bignerdranch.android.xundian.model.LoginModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrator on 2017/9/26.
 */

public class KaoQingCommonActivity extends CommActivity {
    public TextView mTitle_nei_ye = null; // 设置显示标题
    public Context mContext = null;

    // Token
    public String mToken = null;

    // LoginModel 登录模型
    public static LoginModel mLoginModel = null;
    // 登录对象
    public static Login mLogin = null;

    // 开启线程
    public static Thread mThread = null;

    // dialog,加载
    public Dialog mWeiboDialog;

    /**
     * 默认连接数据库
     * @param context
     */
    public void setToken(Context context){
        // 登录数据库连接
        mLoginModel = LoginModel.get(context);
        // Token查询,赋值
        mLogin = mLoginModel.getLogin(1);
        mToken = mLogin.getToken();
    }

    /**
     * 点击返回
     *
     * @param v
     */
    public void DianJiFanHui(View v) {
        finish();
    }

    /**
     * 获取当前时间
     * @return 当前时间y-m-d h-i-s
     */
    public String getDangQianTime(int is){
        SimpleDateFormat simpleDateFormats = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        if(is == 1){
             simpleDateFormats = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        }else if(is == 2){
             simpleDateFormats =new SimpleDateFormat("yyyy-MM-dd hh:ii:ss", Locale.CHINA);
        }
        Calendar calendar=Calendar.getInstance(Locale.CHINA);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        //当前时间，貌似多余，其实是为了所有可能的系统一致
        calendar.setTimeInMillis(System.currentTimeMillis());
        return simpleDateFormats.format(calendar.getTime());
    }

    /**
     * 提示
     *
     * @param context
     */
    public static void tiShi(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    /**
     * 检查网络是否完全连接 true 连接  false 没有连接
     * @return
     */
    public boolean isNetworkAvailableAndConnected(Context context){
        ConnectivityManager cm =(ConnectivityManager)context.getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    /**
     * loading 浮层
     *
     * @param logingString 提示文字
     */
    public void LoadingStringEdit(String logingString){
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(mContext,logingString);
    }

    /**
     * 日期选择解析
     * @param str
     * @param c 1 多选 2 单选
     * @return
     */
    public String JieXi(String str,int c){
        String string = "";
        try {
            JSONArray jsonArray = new JSONArray(str);

            if (jsonArray.length()>0){
                if(c == 2){
                    string = String.valueOf(jsonArray.get(0));
                }else if(c == 1) {
                    for (int i = 0;i<jsonArray.length();i++){
                        if(string.equals("")){
                            string = String.valueOf(jsonArray.get(i));
                        }else{
                            string += ","+String.valueOf(jsonArray.get(i));
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return string;
    }

    /**
     * 请假记录展示
     * @param dataStr 请假记录数据
     * @param linearLayout 显示父布局
     */
    public void QingJiaDataShow(String dataStr, LinearLayout linearLayout){
        // 清空布局
        linearLayout.removeAllViews();
        // 请假数据不为空
        if(!dataStr.equals("")){
            try {
                JSONArray jsonArray = new JSONArray(dataStr);
                if(jsonArray.length() > 0){
                    for (int i = 0;i<jsonArray.length();i++){

                        LinearLayout linearLayout1 = CreateLinear(1);
                        // 第一行
                        CreateDiYiHang(linearLayout1,jsonArray.get(i).toString(),i);
                        // 添加到父布局
                        linearLayout.addView(linearLayout1);
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 创建View
     * @param linearLayout
     * @param string 数据
     * @param i 编号
     * @return
     */
    public void CreateDiYiHang(LinearLayout linearLayout,String string,int i){
        try {
            JSONObject jsonObject = new JSONObject(string);

            JSONObject jsonObject_users_id_s = new JSONObject();
            // 获取用户信息
            if(!jsonObject.getString("users_id_s").equals("null")){
                jsonObject_users_id_s = new JSONObject(jsonObject.getString("users_id_s"));
            }


            LinearLayout linearLayout2 = CreateLinear(2);

            LinearLayout linearLayout3 = CreateLinear(3);
            LinearLayout linearLayout4 = CreateLinear(4);


            LinearLayout linearLayout5 = CreateLinear(5);
            LinearLayout linearLayout6 = CreateLinear(6);

            // 评接名称,请假详细内容
            if(jsonObject_users_id_s.length() > 0){
                TextView textView1 = CreateTextView(1,(i+1)+" "+jsonObject_users_id_s.getString("name"));

                String string_ri_qi = "";
                // 评接显示内容
                if(!jsonObject.getString("an_tian_qing_jia").equals("null")){
                    // 得到按钮天请假字符串
                    string_ri_qi = JieXi(jsonObject.getString("an_tian_qing_jia"),1);
                }else{
                    // 得到按时间段请假字符串
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("an_shi_jian_dun"));
                    string_ri_qi += jsonArray.get(0);

                    if(!jsonObject.getString("an_shi_jian_duan_shang_wu_kai_shi").equals("null")
                            && !jsonObject.getString("an_shi_jian_duan_shang_wu_jie_shu").equals("null")){
                        string_ri_qi += " 上午 "+jsonObject.getString("an_shi_jian_duan_shang_wu_kai_shi")+"-"+
                                jsonObject.getString("an_shi_jian_duan_shang_wu_jie_shu");
                    }

                    if(!jsonObject.getString("an_shi_jian_duan_xia_wu_kai_shi").equals("null")
                            && !jsonObject.getString("an_shi_jian_duan_xia_wu_jie_shu").equals("null")){
                        string_ri_qi += " 下午 "+jsonObject.getString("an_shi_jian_duan_xia_wu_kai_shi")+"-"+
                                jsonObject.getString("an_shi_jian_duan_xia_wu_jie_shu");
                    }
                }

                string_ri_qi += " "+jsonObject.getString("qing_jia_lei_xing")+" "+jsonObject.getString("qing_jia_yuan_yin");
                TextView textView2 = CreateTextView(2,string_ri_qi);
                linearLayout3.addView(textView1);
                linearLayout3.addView(textView2);
            }

            // 评接审核状态
            TextView textView3 = CreateTextView(3,jsonObject.getString("qing_jia_zhuang_tai"));
            linearLayout4.addView(textView3);
            // 不同意设置图标 CreateImageView
            if(jsonObject.getString("qing_jia_zhuang_tai").equals("不同意")){
                linearLayout4.addView(CreateImageView(1,jsonObject.getString("bu_tong_yi_yuan_yin"),R.drawable.kao_qing_xing));
            }



            // 添加到父节点
            linearLayout2.addView(linearLayout3);
            linearLayout2.addView(linearLayout4);

            // 第一行添加到父节点
            linearLayout.addView(linearLayout2);


            // 创建审核view
            CreateDierhang(linearLayout6,string);
            linearLayout5.addView(linearLayout6);

            // 第二行添加到父节点
            linearLayout.addView(linearLayout5);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建审核信息
     * @param linearLayout
     * @param string
     */
    public void CreateDierhang(LinearLayout linearLayout,String string){
            try {
                JSONObject jsonObject = new JSONObject(string);

                JSONObject jsonObject_users_id_s = new JSONObject();
                JSONObject jsonObject_users_id_1_s = new JSONObject();
                JSONObject jsonObject_users_id_2_s = new JSONObject();
                JSONObject jsonObject_users_id_3_s = new JSONObject();

                // 获取用户信息
                if(!jsonObject.getString("users_id_s").equals("null")){
                    jsonObject_users_id_s = new JSONObject(jsonObject.getString("users_id_s"));
                }
                if(!jsonObject.getString("users_id_1_s").equals("null")){
                    jsonObject_users_id_1_s = new JSONObject(jsonObject.getString("users_id_1_s"));
                }
                if(!jsonObject.getString("users_id_2_s").equals("null")){
                    jsonObject_users_id_2_s = new JSONObject(jsonObject.getString("users_id_2_s"));
                }
                if(!jsonObject.getString("users_id_3_s").equals("null")){
                    jsonObject_users_id_3_s = new JSONObject(jsonObject.getString("users_id_3_s"));
                }



                LinearLayout linearLayout7_1 = CreateLinear(7);
                LinearLayout linearLayout7_2 = CreateLinear(7);
                LinearLayout linearLayout7_3 = CreateLinear(7);

                // 图片
                ImageView imageView2_1 = CreateImageView(2,"",R.drawable.kao_qing_yong_hu);
                ImageView imageView2_2 = CreateImageView(2,"",R.drawable.kao_qing_yong_hu);
                ImageView imageView2_3 = CreateImageView(2,"",R.drawable.kao_qing_yong_hu);

                // 文字信息
                TextView textView4_1 = CreateTextView(4,"");
                TextView textView4_2 = CreateTextView(4,"");
                TextView textView4_3 = CreateTextView(4,"");

                // 状态图标
                ImageView imageView3_1 = CreateImageView(3,"",R.drawable.ka_qing_ku_lian);
                ImageView imageView3_2 = CreateImageView(3,"",R.drawable.ka_qing_ku_lian);
                ImageView imageView3_3 = CreateImageView(3,"",R.drawable.ka_qing_ku_lian);

                if(jsonObject_users_id_1_s.length() > 0){
                    textView4_1 = CreateTextView(4,jsonObject_users_id_1_s.getString("name"));
                }
                if(jsonObject_users_id_2_s.length() > 0){
                    textView4_2 = CreateTextView(4,jsonObject_users_id_2_s.getString("name"));
                }
                if(jsonObject_users_id_3_s.length() > 0){
                    textView4_3 = CreateTextView(4,jsonObject_users_id_3_s.getString("name"));
                }




                // 无审核权限
                if(jsonObject_users_id_s.getString("shenheren_jibie").equals("null")){


                    linearLayout7_3.addView(imageView2_3);
                    linearLayout7_3.addView(textView4_3);
                    isTuBiao(linearLayout7_3,imageView3_3,jsonObject.getString("zhuang_tai_3"));

                    linearLayout7_2.addView(imageView2_2);
                    linearLayout7_2.addView(textView4_2);
                    isTuBiao(linearLayout7_2,imageView3_2,jsonObject.getString("zhuang_tai_2"));

                    linearLayout7_1.addView(imageView2_1);
                    linearLayout7_1.addView(textView4_1);
                    isTuBiao(linearLayout7_1,imageView3_1,jsonObject.getString("zhuang_tai_1"));
                }

                // 3级权限
                if(jsonObject_users_id_s.getString("shenheren_jibie").equals("3")){

                    linearLayout7_2.addView(imageView2_2);
                    linearLayout7_2.addView(textView4_2);
                    isTuBiao(linearLayout7_2,imageView3_2,jsonObject.getString("zhuang_tai_2"));

                    linearLayout7_1.addView(imageView2_1);
                    linearLayout7_1.addView(textView4_1);
                    isTuBiao(linearLayout7_1,imageView3_1,jsonObject.getString("zhuang_tai_1"));
                }

                // 2级权限
                if(jsonObject_users_id_s.getString("shenheren_jibie").equals("2")){
                    linearLayout7_1.addView(imageView2_1);
                    linearLayout7_1.addView(textView4_1);

                    isTuBiao(linearLayout7_1,imageView3_1,jsonObject.getString("zhuang_tai_1"));
                }


                linearLayout.addView(linearLayout7_1);
                linearLayout.addView(linearLayout7_2);
                linearLayout.addView(linearLayout7_3);

            } catch (JSONException e) {
                e.printStackTrace();
            }

    }

    public void isTuBiao(LinearLayout linearLayout,ImageView imageView,String string){
        if(!string.equals("null")){
            if(string.equals("待审核")){
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ka_qing_ku_lian));
            }
            if(string.equals("不同意")){
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ka_qing_dai_shen_he));
            }
            if(string.equals("同意")){
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ka_qing_xiao_lian));
            }
            linearLayout.addView(imageView);
        }

    }

    /**
     * 创建linearLayout
     * @param is 创建类型
     */
    public LinearLayout CreateLinear(int is){
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        if(is == 1){
            layoutParam.setMargins(0,15,0,0);
        }else if(is == 2){
            layoutParam.setMargins(0,5,0,0);
        }
        else if(is == 3){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
        }else if( is == 4){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,4);
        }else if(is == 5){
            layoutParam.setMargins(0,5,0,0);
        }else if(is == 7){
            layoutParam.setMargins(0,0,10,0);
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
        }


        linearLayout.setLayoutParams(layoutParam);

        linearLayout.setOrientation(LinearLayout.VERTICAL);

        if(is == 2 || is == 4 || is == 7 || is == 6){
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        }else if(is == 4){
            linearLayout.setGravity(Gravity.RIGHT);
        }else if(is == 5){
            linearLayout.setPadding(15,0,0,0);
        }else if(is == 7){
            linearLayout.setGravity(Gravity.BOTTOM);

        }

        return linearLayout;
    }

    /**
     * 创建TextView
     * @param is 创建类型
     * @param string 显示内容
     */
    public TextView CreateTextView(int is,String string){
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        if(is == 2){
            layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParam.setMargins(0,5,0,0);
        }

        textView.setLayoutParams(layoutParam);

        if(is == 1){
            textView.setTextColor(getResources().getColor(R.color.heise));
            textView.setTextSize(15);
            textView.setText(string);
        }else if(is == 2){
            textView.setPadding(15,0,0,0);
            textView.setText(string);
            textView.setTextSize(12);
        }else if(is == 3){
            if(string.equals("待审核") || string.equals("不同意")){
                textView.setTextColor(getResources().getColor(R.color.hongse1));
            }
            if(string.equals("同意")){
                textView.setTextColor(getResources().getColor(R.color.zhuti));
            }
            textView.setText(string);
        }else if(is == 4){
            textView.setGravity(Gravity.BOTTOM);
            textView.setTextColor(getResources().getColor(R.color.heise));
            textView.setTextSize(12);
            textView.setText(string);
        }

        return textView;
    }

    /**
     * 创建ImageView
     * @param is 创建类型
     * @param string 提示文字
     * @param drawableSrc 图片资源
     * @return
     */
    public ImageView CreateImageView(int is, final String string, int drawableSrc){
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        if(is == 1){
            layoutParam = new LinearLayout.LayoutParams(58,58);
        }else if(is == 2){
            layoutParam = new LinearLayout.LayoutParams(88,88);
        }else if(is == 3){
            layoutParam = new LinearLayout.LayoutParams(58,58);
        }

        imageView.setLayoutParams(layoutParam);

        imageView.setImageDrawable(getResources().getDrawable(drawableSrc));

        if(is == 1){
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                    // 设置显示内容
                    alertBuilder.setMessage(string);
                    alertBuilder.create();
                    alertBuilder.show();
                }
            });
        }

        return imageView;
    }

}
