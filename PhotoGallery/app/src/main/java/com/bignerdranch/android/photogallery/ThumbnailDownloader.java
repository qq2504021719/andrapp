package com.bignerdranch.android.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Administrator on 2017/9/5.
 * 图片下载
 */

public class ThumbnailDownloader<T> extends HandlerThread {

    private static final String TAG = "ThumbnailDownloader";
    // 标识下载请求消息
    private static final int MESSAGE_DOWNLOAD = 0;

    private Boolean mHasQuit = false;
    // 用来存储对Handler的引用。这个Handler负责在ThumbnailDownloader后台线程上管理下载请求消息队列。
    // 这个Handler也负责从消息队列里取出并处理下载请求消息
    private Handler mRequestHandler;
    // mRequestMap是个ConcurrentHashMap,这是一种线程安全的HashMap。这里，使用一个标记下载请求的T类型对象作为key，
    // 我们可以存取和请求关联的URL下载链接。这个标记对象是PhotoHolder,下载结果该发生给哪个显示图片的UI元素在明白不过了
    private ConcurrentMap<T,String> mRequestMap = new ConcurrentHashMap<>();

    // 存放来自于主线程的Handler
    private Handler mResponseHandle;
    private ThumbnailDownloadListener<T> mThumbnailDownloadListener;

    public interface ThumbnailDownloadListener<T>{
        void onThumbnailDownloaded(T target,Bitmap thumbnail);
    }

    public void setThumbnailDownloadListener(ThumbnailDownloadListener<T> listener){
        mThumbnailDownloadListener = listener;
    }

    public ThumbnailDownloader(Handler responseHandle){
        super(TAG);
        mResponseHandle = responseHandle;
    }

    @Override
    public boolean quit(){
        mHasQuit = true;
        return super.quit();
    }

    /**
     * 实现Handler的子类中实现Handler.handleMessage
     * HandlerThread.onLooperPrepared()是在Looper首次检查消息队列之前调用的,所以该方法成了创建Handler实现的好地方
     */
    @Override
    protected void onLooperPrepared(){
        mRequestHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                // 检查消息类型,再获取obj值(T类型下载请求),然后将其传递给handleRqeuest()方法处理
                if(msg.what == MESSAGE_DOWNLOAD){
                    T target = (T) msg.obj;
                    Log.i(TAG,"有一个下载的URL请求: "+mRequestMap.get(target));
                    handleReqeuest(target);
                }
            }
        };

    }

    /**
     * 清除队列中的所有请求
     */
    public void clearQueue(){
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
    }

    /**
     * 根据target获取mRequestMap里面url并下载创建位图
     * @param target
     */
    private void handleReqeuest(final T target){
        try{
            final String url = mRequestMap.get(target);
            if(url == null){
                return;
            }
            byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
            // 将返回的字节数组转换为位图
            final Bitmap bitmap = BitmapFactory
                    .decodeByteArray(bitmapBytes,0,bitmapBytes.length);
            Log.i(TAG,"位图创建");

            // Message设有回调方法时,它从消息队列取出后,是不会发给target Handler的。相反,
            // 存储在回调方法中的Runnable的run()方法会直接执行
            // 因为mResponseHandle与主线程的Looper相关联,所以UI更新代码会在主线程中完成。

            mResponseHandle.post(new Runnable() {
                @Override
                public void run() {
                    // 它再次检查requestMap,这很有必要,因为RecyclerView会循环使用其视图。在ThumbnailDownloader下载完成Bitmap之后,
                    // RecyclerView可能循环使用了PhotoHolder并相应请求一个不同的URL。该检查可保证每个PhotoHolder都能获取到正确的图片,
                    // 即使中间发生了其他请求也无妨
                    // 接下来,选中mHasQuit。如果ThumbnailDownloader已经退出,运行任何回调方法可能都不太安全。
                    if (mRequestMap.get(target) != url || mHasQuit){
                        return;
                    }
                    // 从requestMap中删除配对的PhotoHolder-URL,然后将位图设置到目标PhotoHolder上。
                    mRequestMap.remove(target);
                    Log.i(TAG,"图片设置");
                    mThumbnailDownloadListener.onThumbnailDownloaded(target,bitmap);
                }
            });

        }catch (IOException ioe){
            Log.i(TAG,"图像下载错误",ioe);
        }
    }

    /**
     * 存根方法
     * @param target
     * @param url
     */
    public void queueThumbnail(T target,String url){
        Log.i(TAG,"有一个URL: "+url);

        if(url == null){
            mRequestMap.remove(target);
        }else{
            mRequestMap.put(target,url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD,target)
                    .sendToTarget();
        }
    }

}
