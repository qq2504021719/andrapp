package com.bignerdranch.android.beatbox;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */

public class BeatBox {
    // 用于日志记录
    private static final String TAG = "BeatBox";
    // 用户存储声音资源文件目录名
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;

    private AssetManager mAssets;
    // Sound列表
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;

    public BeatBox(Context context){
        mAssets = context.getAssets();
        // 这个古老的构造函数是过时的但我们需要它,为了兼容API16最低级别
        // 第一个参数指定同时播放多少个音频,这里指定了个,在播放5个音频时,如果尝试再播放第6个,SoundPool会停止播放原来的音频
        // 第二个参数确定音频流类型,Android有多不同的音频流,它们都有各自独立的音量控制选项。这就是调低音乐音量,闹钟音量却不受影响的原因。打开文档,
        //查看AudioManager类的AUDIO_*常量,还可以看到其他控制选项。STREAM_MUSIC使用的是同音乐和游戏一样的音量控制。
        // 最后一个参数指定采样率转换品质。参考文档说这个参数不起作用,锁乳这里传入了0值
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC,0);
        loadSounds();
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

        for (String filename : soundNames){
            try{
                String assetPath = SOUNDS_FOLDER+"/"+filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            }catch (IOException ioe){
                Log.e(TAG,"不能加载声音"+filename,ioe);
            }

        }
    }

    /**
     * 载入音频
     * @param sound
     * @throws IOException
     */
    private void load(Sound sound) throws IOException{
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd,1);
        sound.setSoundId(soundId);
    }

    public List<Sound> getSounds() {
        return mSounds;
    }

    /**
     * 根据ID播放音乐
     * @param sound
     */
    public void play(Sound sound){
        Integer soundId = sound.getSoundId();
        if (soundId == null){
            return;
        }
        // 参数依次是:音频ID、左音量、右音量、优先级(无效)、是否循环以及播放速率。我们需要最大音量和常速播放,所以传入值1.0
        //是否循环参数传入0值，代表不循环。(如果想无限循环,可传入-1。我们觉得这会非常令人讨厌。)
        mSoundPool.play(soundId,1.0f,1.0f,1,0,1.0f);
    }

    /**
     * 释放音频
     */
    public void release(){
        mSoundPool.release();
    }
}
