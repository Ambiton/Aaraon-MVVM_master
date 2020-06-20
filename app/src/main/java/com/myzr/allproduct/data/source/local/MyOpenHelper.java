package com.myzr.allproduct.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.myzr.allproduct.entity.db.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * @author Areo
 * @description:
 * @date : 2020/4/6 22:28
 */
public class MyOpenHelper extends DaoMaster.OpenHelper {
    public MyOpenHelper(Context context, String name) {
        super(context, name);
    }

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {

//        //把需要管理的数据库表DAO作为最后一个参数传入到方法中
//        UserActionDataH.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
//
//            @Override
//            public void onCreateAllTables(Database db, boolean ifNotExists) {
//                DaoMaster.createAllTables(db, ifNotExists);
//            }
//
//            @Override
//            public void onDropAllTables(Database db, boolean ifExists) {
//                DaoMaster.dropAllTables(db, ifExists);
//            }
//        },  MovieCollectDao.class);
    }

}
