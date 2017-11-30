package com.nyw.meitu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhouhu on 2017/11/27.
 */

public class SQliteLoveHelper extends SQLiteOpenHelper {
    //创建数据库，建表
    private static final String DBNAME="love.db";
    private static final int VERSION=3; //设置版本号
    private static final String TBL_DETAILNEWS="love"; //创建表名为news的表
    private static final String TBL_DETAILNEWS_COLUMN_URL="_url";
    private static final String TBL_DETAILNEWS_COLUMN_DOCID="_docid";

    public SQliteLoveHelper(Context context) {
        super(context,DBNAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuffer sb=new StringBuffer();
        sb.append("create table if not exists ");
        sb.append(TBL_DETAILNEWS+"(");
        sb.append(TBL_DETAILNEWS_COLUMN_DOCID +" varchar(100) primary key ,"); //设置主键
        sb.append(TBL_DETAILNEWS_COLUMN_URL+" varchar(100) ,");
        sb.append(")");
        sqLiteDatabase.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql2="drop table if exists "+TBL_DETAILNEWS;
        sqLiteDatabase.execSQL(sql2); //创建
        onCreate(sqLiteDatabase);
    }
}
