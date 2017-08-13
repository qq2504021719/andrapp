package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2017/8/8.
 */

public class CrimeLab {

    private static CrimeLab ScrimeLab;

    private List<Crime> mCrimes;

    public static CrimeLab get(Context context){
        if(ScrimeLab == null){
            ScrimeLab = new CrimeLab(context);
        }
        return ScrimeLab;
    }

    private CrimeLab(Context context){
        mCrimes = new ArrayList<>();
    }

    /*
    * 返回list集合
    * */
    public List<Crime> getCrimes(){
        return mCrimes;
    }

    /*
    * 根据id,获取记录信息
    * */
    public Crime getCrime(UUID id){
        for(Crime crime:mCrimes){
            if(crime.getId().equals(id)){
                return crime;
            }
        }
        return null;
    }

    /*
    *
    * 添加信息Crime
    * */
    public void addCrime(Crime c){
        mCrimes.add(c);
    }

    /*
    *
    * 删除信息
    * */
    public void deleteCrime(UUID id){
        for(int i =0;i<mCrimes.size();i++){
            if(mCrimes.get(i).getId().equals(id)){
                mCrimes.remove(i);
            }
        }
    }
}
