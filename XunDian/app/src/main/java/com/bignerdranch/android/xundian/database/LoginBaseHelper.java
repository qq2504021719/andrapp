package com.bignerdranch.android.xundian.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.bignerdranch.android.xundian.database.LoginDbSchema.LoginTable;
/**
 * Created by Administrator on 2017/9/11.
 */

public class LoginBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "xunDian.db";

    public LoginBaseHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ LoginTable.NAME+"("+
                "_id integer primary key autoincrement, "+
                LoginTable.Cols.ID+", "+
                LoginTable.Cols.TOKEN+", "+
                LoginTable.Cols.TIME+")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
