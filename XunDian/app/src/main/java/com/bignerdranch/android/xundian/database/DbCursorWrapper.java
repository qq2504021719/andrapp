package com.bignerdranch.android.xundian.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.bignerdranch.android.xundian.comm.ChaoShi;
import com.bignerdranch.android.xundian.comm.Login;
import com.bignerdranch.android.xundian.comm.XunDianCanShu;
import com.bignerdranch.android.xundian.comm.XunDianJiHua;
import com.bignerdranch.android.xundian.database.DbSchema.LoginTable;
import com.bignerdranch.android.xundian.database.DbSchema.XunDianTable;
import com.bignerdranch.android.xundian.database.DbSchema.XunDianJiHuaTable;
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
        int uid = getInt(getColumnIndex(LoginTable.Cols.USERID));

        Login login = new Login();
        login.setId(id);
        login.setToken(token);
        login.setTime(time);
        login.setZhangHao(zhanghao);
        login.setMiMa(mima);
        login.setIsBaoCun(isbaocun);
        login.setUid(uid);
        return login;
    }

    /**
     * 巡店数据查询
     * @return
     */
    public XunDianCanShu getXundian(){
        String values = getString(getColumnIndex(XunDianTable.Cols.VALUES));
        String phone = getString(getColumnIndex(XunDianTable.Cols.PHONE));
        String xunKaiShiTime = getString(getColumnIndex(XunDianTable.Cols.XUNKAISHITIME));
        String xunJieShuTime = getString(getColumnIndex(XunDianTable.Cols.XUNJIESHITIME));
        String userid = getString(getColumnIndex(XunDianTable.Cols.USERID));


        XunDianCanShu xunDianCanShu = new XunDianCanShu();
        xunDianCanShu.setValue(values);
        xunDianCanShu.setPhontPath(phone);
        xunDianCanShu.setXunKaiShiTime(xunKaiShiTime);
        xunDianCanShu.setXunJieShuTime(xunJieShuTime);
        xunDianCanShu.setUserId(userid);

        return xunDianCanShu;
    }


    /**
     * 查询超时数据
     * @return
     */
    public ChaoShi getChaoShi(){
        int id = getInt(getColumnIndex(DbSchema.ChaoShiTable.Cols.ID));
        int isChaoShi = getInt(getColumnIndex(DbSchema.ChaoShiTable.Cols.ISCHAOSHI));
        int chaoSHi = getInt(getColumnIndex(DbSchema.ChaoShiTable.Cols.CHAOSHI));
        int weiChaoShi = getInt(getColumnIndex(DbSchema.ChaoShiTable.Cols.WEICHAOSHI));
        int zhongshi = getInt(getColumnIndex(DbSchema.ChaoShiTable.Cols.ZHONGSHI));


        ChaoShi chaoShi = new ChaoShi();
        chaoShi.setId(id);
        chaoShi.setIsChaoShi(isChaoShi);
        chaoShi.setChaoShi(chaoSHi);
        chaoShi.setWeiChaoShi(weiChaoShi);
        chaoShi.setZhongShi(zhongshi);

        return chaoShi;
    }

    /**
     * 查询巡店计划数据
     * @return
     */
    public XunDianJiHua getXunDianJiHua(){
        // id
        int id = getInt(getColumnIndex(XunDianJiHuaTable.Cols.ID));
        // 周
        String zhou = getString(getColumnIndex(XunDianJiHuaTable.Cols.ZHOU));
        // 日期
        String riqi = getString(getColumnIndex(XunDianJiHuaTable.Cols.RIQI));
        // 开始时间
        String kstime = getString(getColumnIndex(XunDianJiHuaTable.Cols.KSJIAN));
        // 结束时间
        String jstime = getString(getColumnIndex(XunDianJiHuaTable.Cols.JSJIAN));
        // 品牌id
        int ppid = getInt(getColumnIndex(XunDianJiHuaTable.Cols.PPID));
        // 品牌
        String pingpai = getString(getColumnIndex(XunDianJiHuaTable.Cols.PINPAI));
        // 门店id
        int mdid = getInt(getColumnIndex(XunDianJiHuaTable.Cols.MDID));
        // 门店
        String mendian = getString(getColumnIndex(XunDianJiHuaTable.Cols.MDMINGC));
        // 门店编号
        String bianhao = getString(getColumnIndex(XunDianJiHuaTable.Cols.MDHAO));

        XunDianJiHua xunDianJiHua = new XunDianJiHua();

        xunDianJiHua.setId(id);
        xunDianJiHua.setZhou(zhou);
        xunDianJiHua.setRiQi(riqi);
        xunDianJiHua.setShiJian(kstime);
        xunDianJiHua.setJSShiJian(jstime);
        xunDianJiHua.setPingPaiId(ppid);
        xunDianJiHua.setPingPaiStr(pingpai);
        xunDianJiHua.setMenDianId(mdid);
        xunDianJiHua.setMenDianStr(mendian);
        xunDianJiHua.setMenDianHao(bianhao);

        return xunDianJiHua;
    }



}
