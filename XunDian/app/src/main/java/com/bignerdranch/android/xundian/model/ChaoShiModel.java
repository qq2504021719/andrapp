package com.bignerdranch.android.xundian.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bignerdranch.android.xundian.comm.ChaoShi;
import com.bignerdranch.android.xundian.comm.XunDianCanShu;
import com.bignerdranch.android.xundian.database.BaseHelper;
import com.bignerdranch.android.xundian.database.DbCursorWrapper;
import com.bignerdranch.android.xundian.database.DbSchema;
import com.bignerdranch.android.xundian.database.DbSchema.XunDianTable;

/**
 * Created by Administrator on 2017/9/14.
 */

public class ChaoShiModel {
    public Context mContext;
    public SQLiteDatabase mDatabase;
    public static ChaoShiModel mChaoShi;

    public static ChaoShiModel get(Context context){
        if(mChaoShi == null){
            mChaoShi = new ChaoShiModel(context);
        }
        return mChaoShi;
    }

    private ChaoShiModel(Context context){
        // 创建数据库
        mContext = context.getApplicationContext();
        mDatabase= new BaseHelper(mContext).getWritableDatabase();
    }



    /**
     *
     * 查询数据
     * @param
     */
    private DbCursorWrapper queryXunDian(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                DbSchema.ChaoShiTable.NAME,
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
     * @param id 店铺id
     * @return
     */
    public ChaoShi getChaoShi(String id){
        DbCursorWrapper cursor = queryXunDian(
                XunDianTable.Cols.ID +" = ?",
                new String[] {id}
        );
        try{
            if(cursor.getCount() == 0){
                return null;
            }
            if(cursor.moveToFirst()){
                return cursor.getChaoShi();
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
    public void deleteCaoShi(String id){
        mDatabase.delete(
                DbSchema.ChaoShiTable.NAME,
                DbSchema.ChaoShiTable.Cols.ID+"=?",
                new String[] {id}
        );
    }

    /**
     * 添加记录
     * @param chaoShi
     */
    public void addChaoShi(ChaoShi chaoShi){
        ContentValues values = getContentValuesXun(chaoShi);
        mDatabase.insert(DbSchema.ChaoShiTable.NAME,null,values);
    }

    /**
     * 构建ContentValues对象
     * @param chaoShi
     * @return
     */
    public static ContentValues getContentValuesXun(ChaoShi chaoShi){
        ContentValues values = new ContentValues();
        values.put(DbSchema.ChaoShiTable.Cols.ID,String.valueOf(chaoShi.getId()));
        values.put(DbSchema.ChaoShiTable.Cols.ISCHAOSHI,String.valueOf(chaoShi.getIsChaoShi()));
        values.put(DbSchema.ChaoShiTable.Cols.CHAOSHI,String.valueOf(chaoShi.getChaoShi()));
        values.put(DbSchema.ChaoShiTable.Cols.WEICHAOSHI,String.valueOf(chaoShi.getWeiChaoShi()));
        values.put(DbSchema.ChaoShiTable.Cols.ZHONGSHI,String.valueOf(chaoShi.getZhongShi()));
        return values;
    }

    /**
     * 更新记录
     * @param chaoShi
     */
    public void updateChaoShi(ChaoShi chaoShi){
        String id = String.valueOf(chaoShi.getId());
        ContentValues values = getContentValuesXun(chaoShi);
        mDatabase.update(DbSchema.ChaoShiTable.NAME,values,
                DbSchema.ChaoShiTable.Cols.ID+"=?",
                new String[] {id});
    }


    /**
     * 将值写入数据库,不存在写入,存在修改
     * @param chaoShi
     */
    public void addIsUpdate(ChaoShi chaoShi){
        ChaoShi chaoShi1 = getChaoShi(
                String.valueOf(chaoShi.getId())
        );
        if(chaoShi1 != null){
            updateChaoShi(chaoShi);
        }else{
            addChaoShi(chaoShi);
        }
    }
}
