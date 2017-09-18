package com.bignerdranch.android.test;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public static Intent newIntent(Context context){
        return new Intent(context,MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent i = new Intent(this, RSSPullService.class);
//        i.setData(Uri.parse("巡店"));
//        this.startService(i);

        DingShi(this);
    }

    public void DingShi(Context context){

         alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);//获取AlarmManager实例

        int anHour =  4 * 1000 ;  // 6秒

        Intent intent2 = new Intent(context, AlarmReceiver.class);

        alarmIntent = PendingIntent.getBroadcast(context, 0, intent2, 0);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),anHour, alarmIntent);//开启提醒
    }
}
