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
import com.bignerdranch.android.xundian.database.DbSchema.XunDianTable;
import com.bignerdranch.android.xundian.database.DbSchema.ChaoShiTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 */

public class XunDianModel {
    public Context mContext;
    public SQLiteDatabase mDatabase;
    public static XunDianModel mXunDianModel;

    public static XunDianModel get(Context context){
        if(mXunDianModel == null){
            mXunDianModel = new XunDianModel(context);
        }
        return mXunDianModel;
    }

    private XunDianModel(Context context){
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
     * 根据店铺id查询巡店数据
     * @param uid 用户id
     * @return
     */
    public List<XunDianCanShu> getXunDianID(String uid){
        List<XunDianCanShu> xunDianCanShuList = new ArrayList<>();

        String sql = "select * from "+ DbSchema.XunDianTable.NAME+" where "+ XunDianTable.Cols.USERID+" = '"+uid+"'";
        Cursor cursor = mDatabase.rawQuery(sql,null);
        while (cursor.moveToNext()) {
            XunDianCanShu xunDianCanShu =getXundianJiHuaCursor(cursor);
            xunDianCanShuList.add(xunDianCanShu);
        }
        return xunDianCanShuList;

    }

    /**
     * 处理查询数据
     * @param cursor
     * @return
     */
    public XunDianCanShu getXundianJiHuaCursor(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(XunDianTable.Cols.ID));
        String values = cursor.getString(cursor.getColumnIndex(XunDianTable.Cols.VALUES));
        String phone = cursor.getString(cursor.getColumnIndex(XunDianTable.Cols.PHONE));
        String xunKaiShiTime = cursor.getString(cursor.getColumnIndex(XunDianTable.Cols.XUNKAISHITIME));
        String xunJieShuTime = cursor.getString(cursor.getColumnIndex(XunDianTable.Cols.XUNJIESHITIME));
        String uid = cursor.getString(cursor.getColumnIndex(XunDianTable.Cols.USERID));
        String bian_name = cursor.getString(cursor.getColumnIndex(XunDianTable.Cols.NAME));
        String phone_file = cursor.getString(cursor.getColumnIndex(XunDianTable.Cols.NAME));

        XunDianCanShu xunDianCanShu = new XunDianCanShu();
        xunDianCanShu.setMenDianId(id);
        xunDianCanShu.setValue(values);
        xunDianCanShu.setPhontPath(phone);
        xunDianCanShu.setXunKaiShiTime(xunKaiShiTime);
        xunDianCanShu.setXunJieShuTime(xunJieShuTime);
        xunDianCanShu.setUserId(uid);
        xunDianCanShu.setBian_hao_name(bian_name);
        xunDianCanShu.setServerPhotoPath(phone_file);

        return xunDianCanShu;
    }

    /**
     * 查询数据
     * @param id 店铺id xiaBiao
     * @param xiaBiao
     * @param uid 用户id
     * @return
     */
    public XunDianCanShu getXunDian(String id,String xiaBiao,String uid){
        DbCursorWrapper cursor = queryXunDian(
                XunDianTable.Cols.ID +" = ? and "+XunDianTable.Cols.XIABIAO +" = ? and "+XunDianTable.Cols.USERID +" = ?",
                new String[] {id,xiaBiao,uid}
        );
        try{
            if(cursor.getCount() == 0){
                return null;
            }
            if(cursor.moveToFirst()){
                return cursor.getXundian();
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
    public void deleteXunDian(String id,String uid){
        mDatabase.delete(
                DbSchema.XunDianTable.NAME,
                DbSchema.XunDianTable.Cols.ID+"=? and "+XunDianTable.Cols.USERID +" = ?",
                new String[] {id,uid}
        );
    }

    /**
     * 添加记录
     * @param xunDianCanShu
     */
    public void addXunDian(XunDianCanShu xunDianCanShu){
        ContentValues values = getContentValuesXun(xunDianCanShu);
        mDatabase.insert(XunDianTable.NAME,null,values);
    }

    /**
     * 构建ContentValues对象
     * @param xunDianCanShu
     * @return
     */
    public static ContentValues getContentValuesXun(XunDianCanShu xunDianCanShu){
        ContentValues values = new ContentValues();
        values.put(XunDianTable.Cols.ID,String.valueOf(xunDianCanShu.getMenDianId()));
        values.put(XunDianTable.Cols.XIABIAO,String.valueOf(xunDianCanShu.getXiaBiao()));
        values.put(XunDianTable.Cols.VALUES,xunDianCanShu.getValue());
        values.put(XunDianTable.Cols.XUNKAISHITIME,xunDianCanShu.getXunKaiShiTime());
        values.put(XunDianTable.Cols.XUNJIESHITIME,xunDianCanShu.getXunJieShuTime());
        values.put(XunDianTable.Cols.USERID,xunDianCanShu.getUserId());
        values.put(XunDianTable.Cols.NAME,xunDianCanShu.getBian_hao_name());
        values.put(XunDianTable.Cols.PHONEFILE,xunDianCanShu.getServerPhotoPath());

        if(xunDianCanShu.getPhontPath() != null){
            values.put(XunDianTable.Cols.PHONE,xunDianCanShu.getPhontPath());
        }
        return values;
    }

    /**
     * 更新记录
     * @param xunDianCanShu
     */
    public void updateXunDian(XunDianCanShu xunDianCanShu){
        String id = String.valueOf(xunDianCanShu.getMenDianId());
        String xiaoBiao = String.valueOf(xunDianCanShu.getXiaBiao());
        String uid = xunDianCanShu.getUserId();

        ContentValues values = getContentValuesXun(xunDianCanShu);
        mDatabase.update(XunDianTable.NAME,values,
                XunDianTable.Cols.ID+"=? and "+XunDianTable.Cols.XIABIAO+"=?  and "+XunDianTable.Cols.USERID +" = ?",
                new String[] {id,xiaoBiao,uid});
    }


    /**
     * 将值写入数据库,不存在写入,存在修改
     * @param xunDianCanShu
     */
    public void addIsUpdate(XunDianCanShu xunDianCanShu){
        XunDianCanShu mXunDianCanShu = getXunDian(
                String.valueOf(xunDianCanShu.getMenDianId()),
                String.valueOf(xunDianCanShu.getXiaBiao()),
                String.valueOf(xunDianCanShu.getUserId())
        );
        if(mXunDianCanShu != null){
            updateXunDian(xunDianCanShu);
        }else{
            addXunDian(xunDianCanShu);
        }
    }


}
