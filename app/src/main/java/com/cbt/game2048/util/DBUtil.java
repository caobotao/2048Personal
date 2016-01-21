package com.cbt.game2048.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cbt.game2048.bean.ScoreBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by caobotao on 16/1/19.
 */
public class DBUtil {
    private DBHelper dbHelper ;

    public DBUtil(Context context) {
        dbHelper = DBHelper.getDbHelper(context);
    }

    //向表中插入数据
    public void insertData(int score, long spendTime, Date gameDate) {
        //获取SQLiteDatabase
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //计算出游戏花费秒数
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = format.format(gameDate);
        int second = (int) (spendTime / 1000);

        //插入数据
        db.execSQL("insert into " + DBHelper.TAB_GAMEHISTORY + "("
                +DBHelper.COL_SCORE + "," + DBHelper.COL_Date + "," + DBHelper.COL_SPEND_TIME +
                ")values(" + score + ",'" + date + "'," + second + ")");
        db.close();
    }

    //获取最高分
    public int getBestScore() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();;
        Cursor cursor = db.rawQuery("select max("+DBHelper.COL_SCORE+") from " + DBHelper.TAB_GAMEHISTORY, null);
        int bestScore = 0;
        while (cursor.moveToNext()) {
            int score = cursor.getInt(0);
            if (score > bestScore) {
                bestScore = score;
            }
        }
        return bestScore;
    }

    //获取全部游戏记录的集合
    public List<ScoreBean> getAllScoreHistory() {
        List<ScoreBean> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + DBHelper.TAB_GAMEHISTORY + " order by " + DBHelper.COL_Date + " desc", null);
        while (cursor.moveToNext()) {
            int score = cursor.getInt(cursor.getColumnIndex(DBHelper.COL_SCORE));
            String gameDate = cursor.getString(cursor.getColumnIndex(DBHelper.COL_Date));
            int spendTimeSec = cursor.getInt(cursor.getColumnIndex(DBHelper.COL_SPEND_TIME));
            ScoreBean bean = new ScoreBean(score, gameDate, spendTimeSec);
            list.add(bean);
        }
        db.close();
        return list;
    }
}
