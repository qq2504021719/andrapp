package com.bignerdranch.android.photogallery;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/31.
 */

public class PhotoGalleryFragment extends Fragment {

    private RecyclerView mPhotoRecyclerView;

    private static final String TAG = "PhotoGalleryFragment";

    private List<GalleryItem> mItems = new ArrayList<>();
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;

    public static PhotoGalleryFragment newInstance(){
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        // execute() 方法会启动AsyncTask,继而触发后台线程并调用 doInbackground(...)
        new FetchItemsTask().execute();

        // Handler默认与当前线程的Looper相关联。这个Handler是在onCreate()方法中创建的,因此它会与主线程的Looper相关联。
        Handler responseHandler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloadListener(
                new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>(){
                    @Override
                    public void onThumbnailDownloaded(PhotoHolder photoHolder, Bitmap bitmap){
                        Drawable drawable = new BitmapDrawable(getResources(),bitmap);
                        Log.i(TAG,"图片设置");
//                        photoHolder.bindDrawable(drawable);
                    }
                }
        );


        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
        Log.i(TAG,"后台线程开始");

        // 启动后台服务
//        Intent i = PollService.newIntent(getActivity());
//        getActivity().startService(i);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedinstanceState){
        View v = inflater.inflate(R.layout.fragment_photo_gallery,container,false);

        mPhotoRecyclerView = (RecyclerView)v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        // 网格布局,每行3列
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        setupAdapter();

        NaoZhang(getActivity());

        return v;
    }

    private static final int POLL_INTERVAL = 1000*61;

    public static void NaoZhang(Context context){
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getBroadcast(context,0,i,0);

        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                0,
                POLL_INTERVAL, pi);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mThumbnailDownloader.quit();
        Log.i(TAG,"后台线程销毁");
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        // 清除队列中的所有请求
        mThumbnailDownloader.clearQueue();
    }

    private void setupAdapter(){
        if (isAdded()){
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder{

        private ImageView mItemImageView;

        public PhotoHolder(View itemView){
            super(itemView);
            mItemImageView = (ImageView) itemView.findViewById(R.id.fragment_photo_gallery_image_view);
        }

        public void bindDrawable(String url){
            Picasso.with(getActivity()).load(url).into(mItemImageView);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder>{
        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> galleryItems){
            mGalleryItems = galleryItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup,int viewType){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.gallery_item,viewGroup,false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder photoHolder,int position){
            GalleryItem galleryItem = mGalleryItems.get(position);
            Drawable placeholder = getResources().getDrawable(R.drawable.bill_up_close);
            photoHolder.bindDrawable(galleryItem.getUrl());
            // 调用线程的 queueThumbnail方法,传入PhotoHolder和GalleryItem的URL
//            mThumbnailDownloader.queueThumbnail(photoHolder,galleryItem.getUrl());
        }

        @Override
        public int getItemCount(){
            return mGalleryItems.size();
        }
    }

    /**
     * 使用 AsyncTask 在后台线程上运行网络请求
     */
    private class FetchItemsTask extends AsyncTask<Void,Void,List<GalleryItem>>{

        @Override
        protected List<GalleryItem> doInBackground(Void... params){
            return new FlickrFetchr().fetchItems();
        }

        /**
         * onPostExecute 方法在 doInBackground 方法执行完毕后才会运行。更为重要的是，它是在主线程而非后台线程上运行的。因此，在该方法中更新UI比较安全
         * @param items
         */
        @Override
        protected void onPostExecute(List<GalleryItem> items){
            mItems = items;
            Log.i(TAG,String.valueOf(mItems.size()));
            setupAdapter();
        }

    }

}
