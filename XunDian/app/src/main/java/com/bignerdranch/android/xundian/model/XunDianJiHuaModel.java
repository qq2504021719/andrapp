package com.bignerdranch.android.xundian.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bignerdranch.android.xundian.comm.XunDianCanShu;
import com.bignerdranch.android.xundian.comm.XunDianJiHua;
import com.bignerdranch.android.xundian.database.BaseHelper;
import com.bignerdranch.android.xundian.database.DbCursorWrapper;
import com.bignerdranch.android.xundian.database.DbSchema;
import com.bignerdranch.android.xundian.database.DbSchema.XunDianJiHuaTable;

import java.util.ArrayList;
import java.util.List;

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
                XunDianJiHuaTable.NAME,
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
     * 查询巡店表所有数据
     */
    public List<XunDianJiHua> getXunDianJiHuas(){
        List<XunDianJiHua> xunDianJiHuaList = new ArrayList<>();

        String sql = "select * from "+XunDianJiHuaTable.NAME;
        Cursor cursor = mDatabase.rawQuery(sql,null);
        while (cursor.moveToNext()) {
            XunDianJiHua xunDianJiHua =getXundianJiHuaCursor(cursor);
            xunDianJiHuaList.add(xunDianJiHua);
        }
        return xunDianJiHuaList;
    }

    /**
     * 处理查询数据
     * @param cursor
     * @return
     */
    public XunDianJiHua getXundianJiHuaCursor(Cursor cursor){
        // id
        int id = cursor.getInt(cursor.getColumnIndex(XunDianJiHuaTable.Cols.ID));
        // 周
        String zhou = cursor.getString(cursor.getColumnIndex(XunDianJiHuaTable.Cols.ZHOU));
        // 日期
        String riqi = cursor.getString(cursor.getColumnIndex(XunDianJiHuaTable.Cols.RIQI));
        // 开始时间
        String kstime = cursor.getString(cursor.getColumnIndex(XunDianJiHuaTable.Cols.KSJIAN));
        // 结束时间
        String jstime = cursor.getString(cursor.getColumnIndex(XunDianJiHuaTable.Cols.JSJIAN));
        // 品牌id
        int ppid = cursor.getInt(cursor.getColumnIndex(XunDianJiHuaTable.Cols.PPID));
        // 品牌
        String pingpai = cursor.getString(cursor.getColumnIndex(XunDianJiHuaTable.Cols.PINPAI));
        // 门店id
        int mdid = cursor.getInt(cursor.getColumnIndex(XunDianJiHuaTable.Cols.MDID));
        // 门店
        String mendian = cursor.getString(cursor.getColumnIndex(XunDianJiHuaTable.Cols.MDMINGC));
        // 门店编号
        String bianhao = cursor.getString(cursor.getColumnIndex(XunDianJiHuaTable.Cols.MDHAO));

        XunDianJiHua xunDianJiHua = new XunDianJiHua();

        xunDianJiHua.setId(id);
        xunDianJiHua.setZhou(zhou);
        xunDianJiHua.setRiQi(riqi);
        xunDianJiHua.setShiJian(kstime);
        xunDianJiHua.setJSShiJian(jstime);
        xunDianJiHua.setPingPaiId(ppid);
        xunDianJiHua.setPingPaiStr(pingpai);
        xunDianJiHua.setMenDianId(mdid);
        xunDianJiHua.setMenDianStr(mendian);
        xunDianJiHua.setMenDianHao(bianhao);

        return xunDianJiHua;
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
        mDatabase.update(XunDianJiHuaTable.NAME,values,
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
