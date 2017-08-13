package com.bignerdranch.android.lianxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2017/8/13.
 */

public class ShangPingFragment extends Fragment{

    // argument bundle key
    private static final String ARG_CRIME_ID = "shanging_id";
    //DatePickerFragment 标识
    private static final String DIALOG_DATE = "DialogDate";
    // 设置子fragment标识
    private static final int REQUEST_DATE = 0;

    private ShangPing mShangPing;

    // 标题
    private EditText mTitleField;
    // 添加时间
    private Button mAddTimeButton;
    // 是否喜欢
    private CheckBox mIsXiHuanCheckBox;

    /*
    *
    * 要不加argument bundle给fragment,需要调用Fragment.setArguments(bundle)方法,而且,
    * 还要在fragment创建后,添加给activity前完成
    *
    * 托管activity需要fragment实例时,会调用newInstance()方法,而非自己调用其构造方法。
    *
    * */
    public static ShangPingFragment newInstance(UUID ShangPingId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,ShangPingId);

        ShangPingFragment fragment = new ShangPingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // 获取用户点击列的id
        UUID ShangPingId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        // 根据列id获取详细信息
        mShangPing = ShangPingS.get(getActivity()).getShangping(ShangPingId);
    }

    /*
    *
    *创建视图 等同于Activity.onCreate()的方法处理
    *
    * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        // 创建fragment_shangping视图
        View v = inflater.inflate(R.layout.fragment_shangping,container,false);

        // 设置EditText监听方法
        mTitleField = (EditText)v.findViewById(R.id.shangping_title);
        mTitleField.setText(mShangPing.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 这个空间故意留空
            }
            /*
            *
            * 调用charSequence(代表用户输入)的toString()方法,改方法最后返回用来设置ShangPing标题的字符串
            * */
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mShangPing.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // 设置按钮显示时间,点击选择时间
        mAddTimeButton = (Button) v.findViewById(R.id.shangping_addtime);
        UpdateButtonText();
        // 点击按钮显示日期对话框
        mAddTimeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager manager = getFragmentManager();
                // 创建日历实例,将时间传递进去
                DatePickerFragment dialog = DatePickerFragment.newInstance(mShangPing.getAddTime());
                // 将ShangPingFragment设置成DatePickerFragment的目标fragment，DatePickerFragment sendResult的数据会传递个ShangpingFragment
                dialog.setTargetFragment(ShangPingFragment.this,REQUEST_DATE);
                // 显示日历
                dialog.show(manager,DIALOG_DATE);
            }
        });

        // 设置IsXiHuan的值,监听并更新
        mIsXiHuanCheckBox = (CheckBox)v.findViewById(R.id.shangping_isxihuan);
        mIsXiHuanCheckBox.setChecked(mShangPing.isXiHuan());
        mIsXiHuanCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mShangPing.setXiHuan(b);
            }
        });

        return v;
    }

    /*
    * 设置button显示时间
    * */
    private void UpdateButtonText(){
        // 设置显示时间
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        mAddTimeButton.setText(sDateFormat.format(mShangPing.getAddTime()));
    }

    /*
    *
    * 覆盖onActivityResult方法,从extra中获取数据
    * requestCode 设置目标子fragment做的表示
    * sendResult 创建返回的时候设置的 一般取 Activity.RESULT_OK
    * Intent 传递的值
    *
    * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_DATE){
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mShangPing.setAddTime(date);
            UpdateButtonText();
        }
    }

}
