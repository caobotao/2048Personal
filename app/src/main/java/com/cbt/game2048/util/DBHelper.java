package com.cbt.game2048.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cbt.game2048.bean.ScoreBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by caobotao on 16/1/19.
 * 创建数据库的工具类
 */
public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper dbHelper = null;

    //数据库名,表名,字段名
    public static String DB_NAME = "gameDb";
    public static String TAB_GAMEHISTORY = "gameHistory";
    public static String COL_SCORE = "score";
    public static String COL_Date = "date";
    public static String COL_SPEND_TIME = "spendtime";

    private DBHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    //单例模式获取DBHelper实例
    public static DBHelper getDbHelper(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context, DB_NAME);
        }
        return dbHelper;
    }

    @Override

    //创建表
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TAB_GAMEHISTORY + " (" +
                "_id integer primary key," +
                COL_SCORE + " integer not null," +
                COL_Date + " text not null," +
                COL_SPEND_TIME + " integer not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
