package com.bignerdranch.android.criminalintent;

import android.content.Context;

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
        for(int i = 0;i<100;i++){
            Crime crime = new Crime();
            crime.setTitle("Crime #"+i);
            crime.setSolved(i % 2 == 0);
            mCrimes.add(crime);
        }
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
}
