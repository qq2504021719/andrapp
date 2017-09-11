package com.bignerdranch.android.xundian;

import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/1.
 */

public class ViewS {

    public List<Viewd> mViews;

    public Viewd mViewd;

    public void init(){
        mViews = new ArrayList<>();
        for(int i = 1;i<=4;i++){
            mViewd = new Viewd();
            mViewd.setViewid(i);
            if(i == 1){
                mViewd.setViewFragment(new GongZuoZhongXinFragment());
                mViewd.setViewString("工作中心");
            }else if(i == 3){
                mViewd.setViewFragment(new TongZhiZhongXinFragment());
                mViewd.setViewString("通知中心");
            }else if(i == 4){
                mViewd.setViewFragment(new WodeXinXiFragment());
                mViewd.setViewString("我的信息");
            }else{
                mViewd.setViewString("页面"+i);
                mViewd.setViewFragment(MainFragment.newInstance(i));
            }
            mViews.add(mViewd);
        }
    }

    public List<Viewd> getViewS(){
        return mViews;
    }

    /**
     * 根据id获取Viewd
     * @param id
     * @return
     */
    public Viewd getViewd(int id) {

        for(Viewd viewd:mViews){
            if(viewd.getViewid() == id){
                return viewd;
            }
        }
        return null;
    }
}
