package com.cbt.game2048.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by caobotao on 16/1/20.
 * 进行音效配置的SharedPreferences工具类
 */
public class SharedPreferencesUtil {
    private Context context;
    private SharedPreferences sharedPreferences;
    private static SharedPreferencesUtil util;
    private Editor editor;
    public static final String IS_OPEN_SOUND = "isOpenSound";
    public static final String CLOSED = "false";
    public static final String OPENED = "true";

    //单例模式获取此工具类实例
    private SharedPreferencesUtil(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("soundSetting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //首先设置为音效开启状态
        editor.putString(IS_OPEN_SOUND, OPENED);
        editor.commit();
    }

    //获取音效开启状态
    public String getSwitchState() {
        return sharedPreferences.getString(IS_OPEN_SOUND, "");
    }

    //将音效设置为目标状态
    public void switchSoundTo(String soundState) {
        editor.putString(IS_OPEN_SOUND, soundState);
        editor.commit();
    }

    //获取此工具类实例
    public static SharedPreferencesUtil getInstance(Context context) {
        if (util == null) {
            util = new SharedPreferencesUtil(context);
        }
        return util;
    }

}
