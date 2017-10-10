package com.bignerdranch.android.xundian.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.bignerdranch.android.xundian.database.DbSchema.LoginTable;
import com.bignerdranch.android.xundian.database.DbSchema.XunDianTable;
import com.bignerdranch.android.xundian.database.DbSchema.XunDianJiHuaTable;
import com.bignerdranch.android.xundian.database.DbSchema.ChaoShiTable;
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
                LoginTable.Cols.USERID+", "+
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
                XunDianTable.Cols.XUNKAISHITIME+", "+
                XunDianTable.Cols.XUNJIESHITIME+", "+
                XunDianTable.Cols.USERID+", "+
                XunDianTable.Cols.PHONE+");"
        );

        /**
         * 超时时间存储
         */
        db.execSQL("create table "+ ChaoShiTable.NAME+"("+
                "_id integer primary key autoincrement, "+
                ChaoShiTable.Cols.ID+", "+
                ChaoShiTable.Cols.ISCHAOSHI+", "+
                ChaoShiTable.Cols.CHAOSHI+", "+
                ChaoShiTable.Cols.ZHONGSHI+", "+
                ChaoShiTable.Cols.WEICHAOSHI+");"
        );

        /**
         * 创建巡店计划表
         */
        db.execSQL("create table "+ XunDianJiHuaTable.NAME+"("+
                "_id integer primary key autoincrement, "+
                XunDianJiHuaTable.Cols.ID+", "+
                XunDianJiHuaTable.Cols.ZHOU+", "+
                XunDianJiHuaTable.Cols.RIQI+", "+
                XunDianJiHuaTable.Cols.KSJIAN+", "+
                XunDianJiHuaTable.Cols.JSJIAN+", "+
                XunDianJiHuaTable.Cols.PPID+", "+
                XunDianJiHuaTable.Cols.PINPAI+", "+
                XunDianJiHuaTable.Cols.MDID+", "+
                XunDianJiHuaTable.Cols.MDMINGC+", "+
                XunDianJiHuaTable.Cols.MDHAO+");"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
