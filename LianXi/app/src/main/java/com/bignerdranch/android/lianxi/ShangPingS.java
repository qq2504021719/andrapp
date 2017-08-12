package com.bignerdranch.android.lianxi;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2017/8/12.
 */

public class ShangPingS {

    private static ShangPingS Sshangpings;

    private List<ShangPing> mshangpings;


    /*
    *
    * 单例,单例是特殊的java类,在创建实例时,一个单例类仅允许创建一个实例。
    * 应用能在内存里存多久,单例就能存多久。因此将对象列表保存在单例里面的话,就能随时获取到shangping数据，
    * 不管activity和fragment的生命周期怎么变化。使用单例还有注意一点:android从内存里移除应用时，单例对象就不复存在了,
    *
    * */
    public static ShangPingS get(Context context){
        if(Sshangpings == null){
            Sshangpings = new ShangPingS(context);
        }
        return Sshangpings;
    }

    /*
    *
    * 生成数组
    *
    *  */
    public ShangPingS(Context context){
        mshangpings = new ArrayList<>();
        for(int i=0;i<100;i++){
            ShangPing shangping = new ShangPing();
            shangping.setTitle("商品 #"+i);
            shangping.setXiHuan(i%2==0);
            mshangpings.add(shangping);
        }
    }

    /*
    *
    * 获取商品集合
    *
    * */
    public List<ShangPing> getShangpingS(){
        return mshangpings;
    }

    /*
    *
    * 根据商品id获取对应数据
    *
    * */
    public ShangPing getShangping(UUID id){
        for(ShangPing shangping:mshangpings){
            if(shangping.getId().equals(id)){
                return shangping;
            }
        }
        return null;
    }


}
