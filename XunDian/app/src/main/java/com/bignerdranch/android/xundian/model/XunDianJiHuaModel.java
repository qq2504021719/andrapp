package com.bignerdranch.android.xundian.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.xundian.comm.XunDianCanShu;
import com.bignerdranch.android.xundian.comm.XunDianJiHua;
import com.bignerdranch.android.xundian.database.BaseHelper;
import com.bignerdranch.android.xundian.database.DbCursorWrapper;
import com.bignerdranch.android.xundian.database.DbSchema;
import com.bignerdranch.android.xundian.database.DbSchema.XunDianJiHuaTable;
/**
 * Created by Administrator on 2017/9/19.
 */

public class XunDianJiHuaModel {
    public Context mContext;
    public SQLiteDatabase mDatabase;
    public static XunDianJiHuaModel mXunDianJiHuaModel;

    public static XunDianJiHuaModel get(Context context){
        if(mXunDianJiHuaModel == null){
            mXunDianJiHuaModel = new XunDianJiHuaModel(context);
        }
        return mXunDianJiHuaModel;
    }

    private XunDianJiHuaModel(Context context){
        // 创建数据库
        mContext = context.getApplicationContext();
        mDatabase= new BaseHelper(mContext).getWritableDatabase();
    }



    /**
     *
     * 查询数据
     * @param
     */
    private DbCursorWrapper queryXunDianJiHua(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                DbSchema.XunDianTable.NAME,
                null,
                whereClause,
                whereArgs,
                null, // groupBy
                null, // habing
                null // orderBy
        );
        return new DbCursorWrapper(cursor);
    }


    /**
     * 查询数据
     * @param id id
     * @return
     */
    public XunDianJiHua getXunDianJiHua(String id){
        DbCursorWrapper cursor = queryXunDianJiHua(
                XunDianJiHuaTable.Cols.ID +" = ?",
                new String[] {id}
        );
        try{
            if(cursor.getCount() == 0){
                return null;
            }
            if(cursor.moveToFirst()){
                return cursor.getXunDianJiHua();
            }

        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return null;
    }


    /**
     * 根据id删除记录
     * @param id
     */
    public void deleteXunDianJiHua(String id){
        mDatabase.delete(
                XunDianJiHuaTable.NAME,
                XunDianJiHuaTable.Cols.ID+"=?",
                new String[] {id}
        );
    }

    /**
     * 添加记录
     * @param xunDianJiHua
     */
    public void addXunDianJiHua(XunDianJiHua xunDianJiHua){
        ContentValues values = getContentValuesXun(xunDianJiHua);
        mDatabase.insert(XunDianJiHuaTable.NAME,null,values);
    }

    /**
     * 构建ContentValues对象
     * @param xunDianJiHua
     * @return
     */
    public static ContentValues getContentValuesXun(XunDianJiHua xunDianJiHua){
        ContentValues values = new ContentValues();
        values.put(XunDianJiHuaTable.Cols.ID,String.valueOf(xunDianJiHua.getId()));
        values.put(XunDianJiHuaTable.Cols.ZHOU,xunDianJiHua.getZhou());
        values.put(XunDianJiHuaTable.Cols.RIQI,xunDianJiHua.getRiQi());
        values.put(XunDianJiHuaTable.Cols.KSJIAN,xunDianJiHua.getShiJian());
        values.put(XunDianJiHuaTable.Cols.JSJIAN,xunDianJiHua.getJSShiJian());
        values.put(XunDianJiHuaTable.Cols.PPID,xunDianJiHua.getPingPaiId());
        values.put(XunDianJiHuaTable.Cols.PINPAI,xunDianJiHua.getPingPaiStr());
        values.put(XunDianJiHuaTable.Cols.MDID,xunDianJiHua.getMenDianId());
        values.put(XunDianJiHuaTable.Cols.MDMINGC,xunDianJiHua.getMenDianStr());
        values.put(XunDianJiHuaTable.Cols.MDHAO,xunDianJiHua.getMenDianHao());

        return values;
    }

    /**
     * 更新记录
     * @param xunDianJiHua
     */
    public void updateXunDianJiHua(XunDianJiHua xunDianJiHua){
        String id = String.valueOf(xunDianJiHua.getId());
        ContentValues values = getContentValuesXun(xunDianJiHua);
        mDatabase.update(DbSchema.XunDianTable.NAME,values,
                DbSchema.XunDianTable.Cols.ID+"=?",
                new String[] {id});
    }

    /**
     * 将值写入数据库,不存在写入,存在修改
     * @param xunDianJiHua
     */
    public void addIsUpdate(XunDianJiHua xunDianJiHua){
        XunDianJiHua mxunDianJiHua = getXunDianJiHua(
                String.valueOf(xunDianJiHua.getId())
        );
        if(mxunDianJiHua != null){
            updateXunDianJiHua(xunDianJiHua);
        }else{
            addXunDianJiHua(xunDianJiHua);
        }
    }
}
