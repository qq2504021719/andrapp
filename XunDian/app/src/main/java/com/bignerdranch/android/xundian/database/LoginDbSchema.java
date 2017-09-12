package com.bignerdranch.android.xundian.database;

import java.util.Date;

/**
 * Created by Administrator on 2017/9/11.
 */

public class LoginDbSchema {
    public static final class LoginTable{
        public static final String NAME = "login";

        public static final class Cols{
            public static final String ID = "id";
            public static final String TOKEN = "token";
            public static final String TIME = "time";
            public static final String ZHANGHAO = "zhanghao";
            public static final String MIMA ="mima";
            public static final String ISBAOCUN = "isbaocun";
        }
    }



}
