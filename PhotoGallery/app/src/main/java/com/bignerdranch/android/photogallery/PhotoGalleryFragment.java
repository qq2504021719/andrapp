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
import android.widget.TextView;

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

        setupAdapter();

        return v;
    }

    private void setupAdapter(){
        if (isAdded()){
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder{

        private TextView mTitleTextView;

        public PhotoHolder(View itemView){
            super(itemView);
            mTitleTextView = (TextView) itemView;
        }

        public void bindGalleryItem(GalleryItem item){
            mTitleTextView.setText(item.toString());
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder>{
        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> galleryItems){
            mGalleryItems = galleryItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup,int viewType){
            TextView textView = new TextView(getActivity());
            return new PhotoHolder(textView);
        }

        @Override
        public void onBindViewHolder(PhotoHolder photoHolder,int position){
            GalleryItem galleryItem = mGalleryItems.get(position);
            photoHolder.bindGalleryItem(galleryItem);
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
