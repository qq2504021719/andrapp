package com.bignerdranch.android.xundian.comm;

/**
 * Created by Administrator on 2017/9/15.
 */

public class Config {
    // 服务器地址
    public static String URL = "http://xd.trc-demo.com:3002";

    // 巡店参数图片压缩宽
    public static int XunCanImgWidth = 340;

    // 巡店参数图片压缩高
    public static int XunCanImgHeight = 520;

    // 图片比例
    public static double TuPianBiLi = 340/520;

    // 用户id
    public static String ChaoJiYongHu = "0";

    // 巡店免验证机器码 "64d8be952063a587",
    public static String[] XunDianMianyanZheng = new String[]{"64d8be952063a587","dccdc7ef501d4026"};

    // 巡店查询最大天数
    public static Integer XunDianChaXunZuiDaTianShu = 31;

}
