package com.bignerdranch.android.photogallery;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2017/9/14.
 */

public class PollService extends IntentService {
    private static final String TAG = "PollService";

//    private static final int POLL_INTERVAL = 1000*3;

    public static Intent newIntent(Context context){
        return new Intent(context,PollService.class);
    }

    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//        if(!isNetworkAvailableAndConnected()){
//            return;
//        }
        Log.i(TAG,"收到一个意图"+intent);

    }

    /**
     * 检查网络是否完全连接 true 连接  false 没有连接
     * @return
     */
    private boolean isNetworkAvailableAndConnected(){
        ConnectivityManager cm =(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    /**
     * 启停定时器
     * @param context
     * @param isOn
     */
    public static void setServiceAlarm(Context context,boolean isOn){
        Intent i = PollService.newIntent(context);
        /**
         * 打包一个Content
         * 参数1 context 发送的context
         * 参数2 区分PendingIntent请求来源
         * 参数3 一个待发送的Intent对象
         * 参数4 一组用来决定如何创建PendingIntent的标志符
         */
        PendingIntent pi = PendingIntent.getService(context,0,i,0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        final int POLL_INTERVAL = 1000*60;
        /**
         * 设置或取消定时器
         */
        if(isOn){
            /**
             * 使用AlarmManager.ELAPSED_REALTIME,表示经过一段指定的时间,就启动定时器
             * 使用AlarmManager.RTC 启动基准时间就是当前时刻,一旦到了某个时刻,就启动定时器
             */
            Log.i("定时器","定时器");
            alarmManager.setRepeating(4,System.currentTimeMillis(),POLL_INTERVAL,pi);
//            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,POLL_INTERVAL, pi);//开启提醒
//            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),POLL_INTERVAL,pi);
        }else{
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }
}
