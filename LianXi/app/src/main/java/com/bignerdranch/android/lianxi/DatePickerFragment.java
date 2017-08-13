package com.bignerdranch.android.lianxi;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2017/8/13.
 */

public class DatePickerFragment extends DialogFragment {
    // 设置 newInstance键
    private static final String ARG_DATE = "addtime";
    // 设置sendResult的键
    public static final String EXTRA_DATE = "com.bignerdranch.android.criminalintent.date";

    private DatePicker mDatePicker;

    /*
    *
    * 接收外部传递的addtime
    *
    * */
    public static DatePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE,date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /*
    *
    * 创建日历,设置标题，设置显示时间
    *
    * */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        // 获取时间的年 月 日
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_addtime,null);

        // 设置日历默认选中时间
        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_dae_addtime_picker);
        mDatePicker.init(year,month,day,null);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.date_picker_title)
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 获取用户选择的年月日
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        // 获取时间戳
                        Date date = new GregorianCalendar(year,month,day).getTime();
                        sendResult(Activity.RESULT_OK,date);
                    }
                })
                .create();
    }

    /*
    *
    * 设置date到intent中,销毁Fragment时会传递到目标Fragment中
    *
    * */
    private void sendResult(int resultCode,Date date){
        // 模板fragment不能为空,创建DatePickerFragment时要设置对应的目标fragment
        if (getTargetFragment() == null){
            return;
        }
        // 创建Intent,设置键值对
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,date);
        // 调用模板onActivityResult传递值,getTargetRequestCode()设置目标fragment传递的值,可以标识是哪个子fragment返回的
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

}
