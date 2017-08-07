package com.bignerdranch.android.criminalintent;

import java.util.UUID;

/**
 * Created by Administrator on 2017/8/7.
 */

public class Crime {
    private UUID mId;
    private String mTitle;

    public Crime(){
        // 独特的标识符生成
        mId = UUID.randomUUID();
    }


    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }
}
