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
            // 用户id
            public static final String USERID = "uid";
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
            // 巡店开始时间
            public static final String XUNKAISHITIME = "xunkaishitime";
            // 巡店结束时间
            public static final String XUNJIESHITIME = "xunjieshutime";
            // 用户id
            public static final String USERID = "uid";

        }
    }

    /**
     * 超时时间存储
     */
    public static final class ChaoShiTable{
        public static final String NAME = "chaoshi";
        public static final class Cols{
            // 门店id
            public static final String ID = "id";
            // 是否超时
            public static final String ISCHAOSHI = "ischaoshi";
            // 超时时间
            public static final String CHAOSHI = "chaoshi";
            // 剩余时间
            public static final String WEICHAOSHI = "weichaoshi";
            // 总时间
            public static final String ZHONGSHI = "zhongshi";

        }
    }

    /**
     * 巡店周计划保存
     */
    public static final class XunDianJiHuaTable{
        public static final String NAME ="xundianjihua";
        public static final class Cols{
            // 计划id
            public static final String ID = "id";
            // 周
            public static final String ZHOU = "zhou";
            // 日期
            public static final String RIQI = "riqi";
            // 开始时间
            public static final String KSJIAN  = "kstime";
            // 结束时间
            public static final String JSJIAN = "jstime";
            // 门店品牌id
            public static final String PPID = "ppid";
            // 门店品牌
            public static final String PINPAI = "pinpai";
            // 门店id
            public static final String MDID = "mdid";
            // 门店名称
            public static final String MDMINGC = "mdmingc";
            // 门店号
            public static final String MDHAO = "mdhao";
        }
    }



}
