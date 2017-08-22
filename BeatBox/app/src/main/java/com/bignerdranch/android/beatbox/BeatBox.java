package com.bignerdranch.android.beatbox;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Administrator on 2017/8/22.
 */

public class BeatBox {
    // 用于日志记录
    private static final String TAG = "BeatBox";
    // 用户存储声音资源文件目录名
    private static final String SOUNDS_FOLDER = "sample_sounds";

    private AssetManager mAssets;

    public BeatBox(Context context){
        mAssets = context.getAssets();
    }

    /**
     * 给出声音文件清单
     */
    private void loadSounds(){
        String[] soundNames;
        try{
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG,"找到"+soundNames.length+"条声音记录");
        }catch (IOException ioe){
            Log.e("TAG","不能列出声音",ioe);
            return;
        }
    }

}
