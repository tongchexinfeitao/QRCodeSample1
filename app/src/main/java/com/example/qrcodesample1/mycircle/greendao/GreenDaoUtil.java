package com.example.qrcodesample1.mycircle.greendao;

import android.content.Context;

import com.example.qrcodesample11.database.DaoMaster;
import com.example.qrcodesample11.database.DaoSession;

public class GreenDaoUtil {
    private static DaoSession daoSession = null;

    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            daoSession = DaoMaster.newDevSession(context, "bw.db");
        }
        return daoSession;
    }

}
