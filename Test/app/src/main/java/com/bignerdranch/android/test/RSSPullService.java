package com.bignerdranch.android.test;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
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
        Log.i("服务","服务服务服务服务服务服务服务服务服务服务服务服务服务服务服务服务服务服务服务服务服务服务服务服务服务服务服务");
    }


}
