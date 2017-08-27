package com.bignerdranch.android.beatbox;

/**
 * Created by Administrator on 2017/8/27.
 */

public class Sound {
    // 文件存储路径
    private String mAssetPath;
    // 文件名称
    private String mName;
    // 音乐文件预加载id
    private Integer mSoundId;

    /**
     * 根据声音存储路径获得声音文件名称
     * @param assetPath
     */
    public Sound(String assetPath){
        mAssetPath = assetPath;
        String[] components = assetPath.split("/");
        String filename = components[components.length-1];
        mName = filename.replace(".wav","");
    }

    public String getAssetPath() {
        return mAssetPath;
    }

    public String getName() {
        return mName;
    }

    public void setSoundId(Integer soundId) {
        mSoundId = soundId;
    }

    public Integer getSoundId() {

        return mSoundId;
    }
}
