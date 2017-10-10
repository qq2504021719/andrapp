package com.bignerdranch.android.xundian.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.comm.XunDianCanShu;
import com.bignerdranch.android.xundian.database.BaseHelper;
import com.bignerdranch.android.xundian.database.DbCursorWrapper;
import com.bignerdranch.android.xundian.database.DbSchema;
import com.bignerdranch.android.xundian.database.DbSchema.LoginTable;

/**
 * Created by Administrator on 2017/9/11.
 */

public class LoginModel {

    public Context mContext;
    public SQLiteDatabase mDatabase;
    public static LoginModel SloginModel;

    public static LoginModel get(Context context){
        if(SloginModel == null){
            SloginModel = new LoginModel(context);
        }
        return SloginModel;
    }

    private LoginModel(Context context){
        // 创建数据库
        mContext = context.getApplicationContext();
        mDatabase= new BaseHelper(mContext).getWritableDatabase();
    }



    /**
     *
     * 查询数据
     * @param
     */
    private DbCursorWrapper queryLogin(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                LoginTable.NAME,
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
     * 根据id获取信息
     * @param id
     * @return
     */
    public Login getLogin(int id){
        DbCursorWrapper cursor = queryLogin(
                LoginTable.Cols.ID +" = ?",
                new String[] {String.valueOf(id)}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getLogin();
        }finally {
            cursor.close();
        }
    }

    /**
     * 根据id删除记录
     * @param id
     */
    public void deleteLogin(int id){
        mDatabase.delete(
                LoginTable.NAME,
                LoginTable.Cols.ID+"=?",
                new String[] {String.valueOf(id)}
        );
    }

    /**
     * 更新记录
     * @param login
     */
    public void updateLogin(Login login){
        ContentValues values = getContentValues(login);
        mDatabase.update(DbSchema.LoginTable.NAME,values,
                DbSchema.LoginTable.Cols.ID+"=?",
                new String[] {"1"});
    }

    /**
     * 添加Login
     * @param l
     */
    public void addLogin(Login l){
        ContentValues values = getContentValues(l);
        mDatabase.insert(LoginTable.NAME,null,values);
    }

    /**
     * 构建ContentValues对象
     * @param login
     * @return
     */
    public static ContentValues getContentValues(Login login){
        ContentValues values = new ContentValues();
        values.put(LoginTable.Cols.ID,String.valueOf(login.getId()));
        values.put(LoginTable.Cols.TOKEN,login.getToken());
        values.put(LoginTable.Cols.TIME,login.getTime());
        values.put(LoginTable.Cols.ZHANGHAO,login.getZhangHao());
        values.put(LoginTable.Cols.MIMA,login.getMiMa());
        values.put(LoginTable.Cols.ISBAOCUN,login.getIsBaoCun());
        values.put(LoginTable.Cols.USERID,login.getUid());
        return values;
    }


}
