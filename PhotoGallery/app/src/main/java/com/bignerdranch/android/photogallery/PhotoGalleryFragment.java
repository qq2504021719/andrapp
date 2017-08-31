package com.bignerdranch.android.photogallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

/**
 * Created by Administrator on 2017/8/31.
 */

public class PhotoGalleryFragment extends Fragment {

    private RecyclerView mPhotoRecyclerView;

    private static final String TAG = "PhotoGalleryFragment";

    public static PhotoGalleryFragment newInstance(){
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        // execute() 方法会启动AsyncTask,继而触发后台线程并调用 doInbackground(...)
        new FetchItemsTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedinstanceState){
        View v = inflater.inflate(R.layout.fragment_photo_gallery,container,false);

        mPhotoRecyclerView = (RecyclerView)v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        // 网格布局,每行3列
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        return v;
    }

    /**
     * 使用 AsyncTask 在后台线程上运行网络请求
     */
    private class FetchItemsTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params){

            try{

                String result = new FilckrFetchr().getUrlString("https://www.bignerdranch.com");
                 Log.i(TAG,"Fetched contents of URL: "+result);

            }catch (IOException ioe){

                Log.e(TAG,"Failed to fetch URL: ",ioe);

            }

            return null;
        }


    }

}
