package com.bignerdranch.android.xundian.database;

import java.util.Date;

/**
 * Created by Administrator on 2017/9/11.
 */

public class DbSchema {
    /**
     * 登录表
     */
    public static final class LoginTable{
        public static final String NAME = "login";

        public static final class Cols{
            // id 1
            public static final String ID = "id";
            // token
            public static final String TOKEN = "token";
            // 登录时间
            public static final String TIME = "time";
            // 账号
            public static final String ZHANGHAO = "zhanghao";
            // 密码
            public static final String MIMA ="mima";
            // 是否保存账号
            public static final String ISBAOCUN = "isbaocun";
        }
    }

    /**
     * 巡店表
     */
    public static final class XunDianTable{
        public static final String NAME = "xundian";
        public static final class Cols{
            // 店铺id
            public static final String ID = "id";
            // 下标id
            public static final String XIABIAO = "xiabiao";
            // 值
            public static final String VALUES = "valuess";
            // 图片
            public static final String PHONE = "phone";
        }
    }



}
