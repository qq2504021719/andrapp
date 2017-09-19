package com.bignerdranch.android.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/9/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AlarmReceiver","接收到广播");
        Toast.makeText(context, "收到定时广播", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(context, RSSPullService.class);
        context.startService(i);
    }

}
