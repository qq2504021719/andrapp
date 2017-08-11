package com.bignerdranch.android.criminalintent;


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
import java.util.UUID;

/**
 * Created by Administrator on 2017/8/7.
 */

public class CrimeFragment extends Fragment{

    // argument bundle key
    private static final String ARG_CRIME_ID = "crime_id";
    // DatePickerFragment 标签
    private static final String DIALOG_DATE = "DialogDate";

    private Crime mCrime;
    // 标题
    private EditText mTitleField;
    // 时间
    private Button mDateButton;
    // is解决
    private CheckBox mSolvedCheckBox;




    /*
    *
    * 要附加argument bundle给fragment,需要调用Fragment.setArguments(Bundle)方法。而且，
    * 还不洗在fragment创建后,添加给activity前完成
    *
    * 托管activity需要fragment实例时,会调用newInstance()方法,而非直接调用其构造方法。
    * */
    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // 获取用户点击列的id
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        // 根据列id获取详细信息
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    /*
    * 创建视图 等同于Activity.onCreate()的方法处理
    * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // 创建fragment_crime视图
        View v = inflater.inflate(R.layout.fragment_crime,container,false);

        // 设置EditText监听方法
        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 这个空间故意留空
            }

            /*
            * 调用charSequence(代表用户输入)的toString()方法,该方法最后返回用来设置Crime标题的字符串。
            * */
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton = (Button)v.findViewById(R.id.crime_data);
        // 设置显示时间
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        mDateButton.setText(sDateFormat.format(mCrime.getDate()));
        // 点击按钮显示日期对话框
        mDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.show(manager,DIALOG_DATE);
            }
        });

        // 设置监听用于更新Crime的mSolved变量值
        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        // 设置监听
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
                // 设置crime的solved的变量值
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }


}
