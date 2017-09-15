package com.bignerdranch.android.xundian.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.bignerdranch.android.xundian.database.DbSchema.LoginTable;
import com.bignerdranch.android.xundian.database.DbSchema.XunDianTable;
/**
 * Created by Administrator on 2017/9/11.
 */

public class BaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "xunDian.db";

    public BaseHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * 创建登录表
         */
        db.execSQL("create table "+ LoginTable.NAME+"("+
                "_id integer primary key autoincrement, "+
                LoginTable.Cols.ID+", "+
                LoginTable.Cols.TOKEN+", "+
                LoginTable.Cols.TIME+", "+
                LoginTable.Cols.ZHANGHAO+", "+
                LoginTable.Cols.MIMA+", "+
                LoginTable.Cols.ISBAOCUN+");"
        );

        /**
         * 创建巡店表
         */
        db.execSQL("create table "+ XunDianTable.NAME+"("+
                "_id integer primary key autoincrement, "+
                XunDianTable.Cols.ID+", "+
                XunDianTable.Cols.XIABIAO+", "+
                XunDianTable.Cols.VALUES+", "+
                XunDianTable.Cols.PHONE+");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
