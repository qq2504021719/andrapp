package com.bignerdranch.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.bignerdranch.android.criminalintent.database.CrimeBaseHelper;
import com.bignerdranch.android.criminalintent.database.CrimeCursorWrapper;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2017/8/8.
 */

public class CrimeLab {

    private static CrimeLab ScrimeLab;


    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context){
        if(ScrimeLab == null){
            ScrimeLab = new CrimeLab(context);
        }
        return ScrimeLab;
    }

    private CrimeLab(Context context){
        // 创建数据库
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext)
                .getWritableDatabase();

    }

    /*
    * 返回list集合
    *
    * 从cursor中取出数据,首先要调用moveToFirst()方法移动虚拟手指指向第一个元素,
    * 读取行记录后,再调用moveToNext()方法,读取下一行记录,知道isAfterLast()告诉我们
    * 没有数据为止.
    * 最后,别忘了调用Cursor的close()方法关闭它,否则,后果很严重:轻则报错,重则导致应用崩溃
    * */
    public List<Crime> getCrimes(){
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null,null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return crimes;
    }

    /*
    * 根据id,获取记录信息
    * */
    public Crime getCrime(UUID id){
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeDbSchema.CrimeTable.Cols.UUID +" = ?",
                new String[] {id.toString()}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        }finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Cols.UUID,crime.getId().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE,crime.getTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE,crime.getDate().getTime());
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED,crime.isSolved()?1:0);
        values.put(CrimeDbSchema.CrimeTable.Cols.SUSPECT,crime.getSuspect());
        values.put(CrimeDbSchema.CrimeTable.Cols.PHONE,crime.getPhone());
        return values;
    }

    /*
    *
    * 更新记录
    * */
    public void updateCrime(Crime crime){
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeDbSchema.CrimeTable.NAME,values, CrimeDbSchema.CrimeTable.Cols.UUID+"=?",new String[] {uuidString});
    }

    /*
    *
    * 添加信息Crime
    * */
    public void addCrime(Crime c){
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME,null,values);
    }

    /**
     *
     * 查询数据
     * @param
     */
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
       Cursor cursor = mDatabase.query(
               CrimeDbSchema.CrimeTable.NAME,
               null,
               whereClause,
               whereArgs,
               null, // groupBy
               null, // habing
               null // orderBy
       );
        return new CrimeCursorWrapper(cursor);
    }

    /*
    *
    * 删除信息
    * */
    public void deleteCrime(UUID id){
        mDatabase.delete(
                CrimeDbSchema.CrimeTable.NAME,
                CrimeDbSchema.CrimeTable.Cols.UUID+"=?",
                new String[] {id.toString()}
                );
    }

    /**
     * 返回指向某个具体位置的File对象
     */
    public File getPhotoFile(Crime crime){
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // 确认外部存储是否可用,如果不可用,返回null
        if(externalFilesDir == null){
            return null;
        }
        return new File(externalFilesDir,crime.getPhotoFilename());
    }
}
