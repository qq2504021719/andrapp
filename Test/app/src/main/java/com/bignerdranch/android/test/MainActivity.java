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

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);//获取AlarmManager实例

        int anHour =  4 * 1000 ;  // 6秒

        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;

        Intent intent2 = new Intent(context, AlarmReceiver.class);

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent2, 0);

        manager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),anHour, pi);//开启提醒
    }
}
