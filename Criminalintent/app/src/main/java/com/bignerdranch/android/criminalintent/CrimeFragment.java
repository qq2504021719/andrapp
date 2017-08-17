package com.bignerdranch.android.criminalintent;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2017/8/7.
 */

public class CrimeFragment extends Fragment{

    // argument bundle key
    private static final String ARG_CRIME_ID = "crime_id";
    // DatePickerFragment 标签
    private static final String DIALOG_DATE = "DialogDate";
    // 设置子fragment标识
    private static final int REQUEST_DATE = 0;

    private Crime mCrime;
    // 标题
    private EditText mTitleField;
    // is解决
    private CheckBox mSolvedCheckBox;
    // 时间
    private EditText mDateedittext;
    // 保存按钮,返回列表页
    private Button mcrimesaveButton;
    // 当前UUID
    private UUID crimeId;
    // 发送短信
    private Button mReportButton;




    /*
    *
    * 要附加argument bundle给fragment,需要调用Fragment.setArguments(Bundle)方法。而且，
    * 还要在fragment创建后,添加给activity前完成
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

        // 让FragmentManager知道CrimeFragment需接收选项菜单方法回调
        setHasOptionsMenu(true);

        // 获取用户点击列的id
        crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        // 根据列id获取详细信息
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    /*
    *
    *
    * 用户可能会在CrimeFragment中修改Crime实例，修改完成后,我们需要刷新CrimeLab中的Crime数据,这可以通过在CrimeFragment.java中
    * 覆盖CrimeFragment.onPause()方法完成
    *
    * */
    @Override
    public void onPause(){
        super.onPause();
        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
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

        mDateedittext = (EditText) v.findViewById(R.id.crime_data_edittext);
        // 点击显示日期对话框
        mDateedittext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                // 将CrimeFragment设置成DatePickerFragment的目标fragment,DatePickerFragment回传数据给CrimeFragment
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                // 显示日历选择器
                dialog.show(manager,DIALOG_DATE);
            }
        });
        UpdateButtonText();


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

        // 监听发送短信按钮
        mReportButton = (Button)v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // 隐试Intent启动短信
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,R.string.crime_report_subject);
                // 有多个可选择的话,始终出现列表让用户选择
                i = Intent.createChooser(i,getString(R.string.send_report));
                startActivity(i);
            }
        });

        // 监听获取联系人列表


        // 保存按钮,返回
        mcrimesaveButton = (Button)v.findViewById(R.id.crime_save);
        mcrimesaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getActivity().finish();
            }
        });

        return v;
    }

    /*
    *
    * 将布局文件中的菜单项目填充到menu中
    *
    * */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_crime,menu);
    }

    /*
    *
    * 点击菜单栏
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_crime_delete:
                CrimeLab.get(getActivity()).deleteCrime(crimeId);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    * 设置button显示时间
    * */
    private void UpdateButtonText(){
        // 设置显示时间
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
//        mDateButton.setText(sDateFormat.format(mCrime.getDate()));
        mDateedittext.setText(sDateFormat.format(mCrime.getDate()));
    }

    /*
    * 覆盖onActivityResult方法,从extra中获取数据
    * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            UpdateButtonText();
        }
    }

    /**
     * 评接消息模板
     */
    private String getCrimeReport(){
        // 是否解决
        String solvedString = null;
        if(mCrime.isSolved()){
            solvedString = getString(R.string.crime_report_solved);
        }else{
            solvedString = getString(R.string.crime_report_unsolved);
        }

        // 发送时间
        String dateFormat = "yyyy年MM月dd日";
        SimpleDateFormat sDateFormat = new SimpleDateFormat(dateFormat);
        String dateString = sDateFormat.format(mCrime.getDate());

        // 嫌疑人
        String suspect = mCrime.getSuspect();
        if (suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        }else{
            suspect = getString(R.string.crime_report_suspect,suspect);
        }

        // 标题
        String report = getString(R.string.crime_report,mCrime.getTitle(),
                dateString,suspect,solvedString);

        return report;
    }


}
