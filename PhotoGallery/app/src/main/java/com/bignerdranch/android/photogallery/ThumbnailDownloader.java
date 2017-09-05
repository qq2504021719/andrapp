package com.bignerdranch.android.photogallery;

import android.os.HandlerThread;
import android.util.Log;

/**
 * Created by Administrator on 2017/9/5.
 */

public class ThumbnailDownloader<T> extends HandlerThread {

    private static final String TAG = "ThumbnailDownloader";

    private Boolean mHasQuit = false;

    public ThumbnailDownloader(){
        super(TAG);
    }

    @Override
    public boolean quit(){
        mHasQuit = true;
        return super.quit();
    }

    /**
     * 存根方法
     * @param target
     * @param url
     */
    public void queueThumbnail(T target,String url){
        Log.i(TAG,"有一个URL: "+url);
    }

}
