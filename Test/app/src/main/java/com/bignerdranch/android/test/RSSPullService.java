package com.bignerdranch.android.test;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

/**
 * Created by Administrator on 2017/9/17.
 */

public class RSSPullService extends IntentService {
    private static final String TAG = "RSSPullService";

    public RSSPullService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        Log.i("服务","发送通知");
        // 给用户发送通知
        Resources resources = getResources();
        Intent i = MainActivity.newIntent(this);
        PendingIntent pi = PendingIntent.getActivity(this,0,i,0);
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(resources.getString(R.string.new_pictures_title)) // 设置ticker text
                .setSmallIcon(android.R.drawable.ic_menu_report_image) // 设置小图标
                .setContentTitle(resources.getString(R.string.new_pictures_title)) //设置标题
                .setContentText(resources.getString(R.string.new_pictures_text)) // 设置文字
                .setContentIntent(pi) //指定用户点击消息所触发的动作行为
                .setAutoCancel(true) // 设置为true表示用户点击通知后,该消息会重消息抽屉中删除
                .build();

        // 从当前context中取出一个notificationManagerCompat实例
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        // 调用方法贴出消息,传入的整数参数是通知消息的标识符,在整个应用中该值应该是唯一的。如果使用同一个ID发送两条消息。
        // 则第二条消息会替换掉第一条消息。在实际开发中,这也是进度条或其他动态视觉效果的实现方式。
        mNotificationManager.notify(0,notification);
    }



}
