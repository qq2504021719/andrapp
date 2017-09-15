package com.bignerdranch.android.xundian.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.comm.XunDianCanShu;
import com.bignerdranch.android.xundian.database.DbSchema.LoginTable;
import com.bignerdranch.android.xundian.database.DbSchema.XunDianTable;
/**
 * Created by Administrator on 2017/9/11.
 */

public class DbCursorWrapper extends CursorWrapper {
    public DbCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    /**
     * 获取login数据
     * @return
     */

    public Login getLogin(){

        int id = getInt(getColumnIndex(LoginTable.Cols.ID));
        String token = getString(getColumnIndex(LoginTable.Cols.TOKEN));
        String time = getString(getColumnIndex(LoginTable.Cols.TIME));
        String zhanghao = getString(getColumnIndex(LoginTable.Cols.ZHANGHAO));
        String mima = getString(getColumnIndex(LoginTable.Cols.MIMA));
        int isbaocun = getInt(getColumnIndex(LoginTable.Cols.ISBAOCUN));

        Login login = new Login();
        login.setId(id);
        login.setToken(token);
        login.setTime(time);
        login.setZhangHao(zhanghao);
        login.setMiMa(mima);
        login.setIsBaoCun(isbaocun);

        return login;
    }

    /**
     * 巡店数据查询
     * @return
     */
    public XunDianCanShu getXundian(){
        String values = getString(getColumnIndex(XunDianTable.Cols.VALUES));
        String phone = getString(getColumnIndex(XunDianTable.Cols.PHONE));
        XunDianCanShu xunDianCanShu = new XunDianCanShu();
        xunDianCanShu.setValue(values);
        xunDianCanShu.setPhontPath(phone);

        return xunDianCanShu;
    }

}
