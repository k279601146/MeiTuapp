package com.nyw.meitu.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nyw.meitu.db.SQliteLoveHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouhu on 2017/11/27.
 */

public class DetailLovesDao {
    private SQliteLoveHelper helper;

    public DetailLovesDao(Context context){
        helper=new SQliteLoveHelper(context); //与数据库建立连接
    }
     //插入数据
        public void insertDetailLovesDao(Love love){
            SQLiteDatabase db=helper.getWritableDatabase();
            db.execSQL("insert into love(_url,_docid)" +  "values(?,?)",new String[]{love.getUrl(),love.getId()});
                    db.close();
        }
        //删除数据
        public void del(String docid){
            //根据传入参数docid删除数据
            SQLiteDatabase db=helper.getReadableDatabase();
            db.execSQL("delete from love where _docid = ?",new Object[]{docid});
            db.close();
        }
         //查询数据
        public List<Love> findSelected(){
            SQLiteDatabase db=helper.getReadableDatabase();
            Cursor c=db.rawQuery("select * from love", null);
            //只有对数据进行查询时，才用rawQuery()，增、删、改和建表，都用execSQl()
            List<Love> list=new ArrayList<Love>();
            while(c.moveToNext()){
                Love news=new Love();
                news.setUrl(c.getString(c.getColumnIndex("_url")));
                news.setId(c.getString(c.getColumnIndex("_docid")));
                list.add(news);
            }
            c.close();
            db.close();
            return list;
        }

    public void insertDetailLovesDao(String url, String id) {
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL("insert into love(_url,_docid)" +  "values(?,?)",new String[]{url,id});
        db.close();
    }
}
