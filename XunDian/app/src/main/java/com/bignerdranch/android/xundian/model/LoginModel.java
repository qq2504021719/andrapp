package com.bignerdranch.android.xundian.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.database.LoginBaseHelper;
import com.bignerdranch.android.xundian.database.LoginCursorWrapper;
import com.bignerdranch.android.xundian.database.LoginDbSchema.LoginTable;

/**
 * Created by Administrator on 2017/9/11.
 */

public class LoginModel {

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private static LoginModel SloginModel;

    public static LoginModel get(Context context){
        if(SloginModel == null){
            SloginModel = new LoginModel(context);
        }
        return SloginModel;
    }

    private LoginModel(Context context){
        // 创建数据库
        mContext = context.getApplicationContext();
        mDatabase= new LoginBaseHelper(mContext).getWritableDatabase();
    }

    /**
     *
     * 查询数据
     * @param
     */
    private LoginCursorWrapper queryLogin(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                LoginTable.NAME,
                null,
                whereClause,
                whereArgs,
                null, // groupBy
                null, // habing
                null // orderBy
        );
        return new LoginCursorWrapper(cursor);
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public Login getLogin(int id){
        LoginCursorWrapper cursor = queryLogin(
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
        return values;
    }


}
