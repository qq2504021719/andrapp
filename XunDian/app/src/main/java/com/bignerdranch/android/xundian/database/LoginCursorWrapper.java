package com.bignerdranch.android.xundian.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.database.LoginDbSchema.LoginTable;

/**
 * Created by Administrator on 2017/9/11.
 */

public class LoginCursorWrapper extends CursorWrapper {
    public LoginCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Login getLogin(){

        int id = getInt(getColumnIndex(LoginTable.Cols.ID));
        String token = getString(getColumnIndex(LoginTable.Cols.TOKEN));
        String time = getString(getColumnIndex(LoginTable.Cols.TIME));

        Login login = new Login();
        login.setId(id);
        login.setToken(token);
        login.setTime(time);

        return login;
    }

}
