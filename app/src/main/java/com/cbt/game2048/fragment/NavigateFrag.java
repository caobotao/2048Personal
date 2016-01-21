package com.cbt.game2048.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cbt.game2048.R;
import com.cbt.game2048.activity.GameHistoryActivity;
import com.cbt.game2048.activity.MainActivity;
import com.cbt.game2048.util.SharedPreferencesUtil;

/**
 * Created by caobotao on 16/1/20.
 * 导航菜单栏Fragment
 */
public class NavigateFrag extends Fragment implements OnClickListener {
    private View navigateView;
    private Activity currentActivity;
    private ImageButton menu;
    private ImageButton back;

    private LinearLayout llStartGame;
    private LinearLayout llScanHistory;
    private LinearLayout llQuitGame;
    private LinearLayout ll_switch_sound;

    private SharedPreferencesUtil sharedPreferencesUtil;//进行音效配置的SharedPreferences工具类

    private TextView tv_switch_sound;

    private PopupWindow mPopupWindow;//弹出窗口的控件

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentActivity = this.getActivity();
        navigateView = inflater.inflate(R.layout.navigate, container, false);
        sharedPreferencesUtil = SharedPreferencesUtil.getInstance(currentActivity);
        initView();//初始化控件
        initEvent();//初始化事件
        return navigateView;
    }

    private void initEvent() {
        //点击菜单按钮时调出弹出菜单
        menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow(menu);
            }
        });
        //点击回退按钮回到上个页面
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentActivity.finish();
            }
        });

    }

    //展示弹出菜单,参数showUnderView为在此控件下弹出菜单
    public void showPopWindow(View showUnderView) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            return;
        }
        // 如果没有初始化则初始化
        if (mPopupWindow == null) {

            //获取弹出菜单的布局控件
            View menuView = currentActivity.getLayoutInflater().inflate(R.layout.pop_menu, null);

            //获取四个LinearLayout条目及音效的文本
            llStartGame     = (LinearLayout) menuView.findViewById(R.id.ll_start_game);
            llScanHistory   = (LinearLayout) menuView.findViewById(R.id.ll_scan_history);
            llQuitGame      = (LinearLayout) menuView.findViewById(R.id.ll_quit_game);
            ll_switch_sound = (LinearLayout) menuView.findViewById(R.id.ll_switch_sound);
            tv_switch_sound = (TextView) menuView.findViewById(R.id.tv_switch_sound);

            //设置四个LinearLayout条目的点击事件
            llStartGame.setOnClickListener(this);
            llScanHistory.setOnClickListener(this);
            llQuitGame.setOnClickListener(this);
            ll_switch_sound.setOnClickListener(this);

            //根据当前音效是否开启设置音效文本
            setSwitchSoundText(tv_switch_sound);

            //获取及设置PopupWindow
            mPopupWindow = new PopupWindow(menuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setOutsideTouchable(true);
            //必须设置PopupWindow的背景,不然点击PopupWindow外部不能dismiss掉PopupWindow
            mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.c_cadet_grey));
            //设置弹出菜单的动画效果
            mPopupWindow.setAnimationStyle(R.style.popup_window);
        }

        // 刷新内容
        mPopupWindow.update();
        // 显示
        mPopupWindow.showAsDropDown(showUnderView);
    }

    private void setSwitchSoundText(TextView tv_switch_sound) {
        //获取当前音效开启状态
        String switchState = sharedPreferencesUtil.getSwitchState();
        //如果是开启状态则设置音效文本为"关闭音效"
        if (SharedPreferencesUtil.OPENED.equals(switchState)) {
            tv_switch_sound.setText("关闭音效");

        } else {//如果是关闭状态则设置音效文本为"开启音效"
            tv_switch_sound.setText("开启音效");
        }
    }

    private void initView() {
        menu = (ImageButton) navigateView.findViewById(R.id.menu);
        back = (ImageButton) navigateView.findViewById(R.id.back);

    }

    //dismiss掉PopupWindow
    public void dismissPopupWindow() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            //处理点击开始新游戏
            case R.id.ll_start_game:
                Log.e("Tag", "ll_start_game");

                intent = new Intent(currentActivity, MainActivity.class);
                intent.putExtra(MainActivity.IS_START_NEW_GAME, true);
                currentActivity.startActivity(intent);

                dismissPopupWindow();
                break;

            //处理点击查看记录
            case R.id.ll_scan_history:
                Log.e("Tag", "ll_scan_history");

                intent = new Intent(currentActivity, GameHistoryActivity.class);
                currentActivity.startActivity(intent);

                break;

            //处理点击开关音效
            case R.id.ll_switch_sound:
                Log.e("Tag", "ll_switch_sound");

                //开关音效
                switchSound();
                //设置音效文本
                setSwitchSoundText(tv_switch_sound);
                Log.e("tag", sharedPreferencesUtil.getSwitchState());

                dismissPopupWindow();
                break;

            //处理点击退出游戏
            case R.id.ll_quit_game:
                Log.e("Tag", "ll_quit_game");

                intent = new Intent(currentActivity, MainActivity.class);
                intent.putExtra(MainActivity.IS_QUIT, true);

                currentActivity.startActivity(intent);
                break;
        }
    }

    private void switchSound() {
        String soundState = sharedPreferencesUtil.getSwitchState();
        if (SharedPreferencesUtil.OPENED.equals(soundState)) {
            sharedPreferencesUtil.switchSoundTo(SharedPreferencesUtil.CLOSED);
            Toast.makeText(currentActivity, "音效已关闭!", Toast.LENGTH_SHORT).show();
        } else {
            sharedPreferencesUtil.switchSoundTo(SharedPreferencesUtil.OPENED);
            Toast.makeText(currentActivity, "音效已开启!", Toast.LENGTH_SHORT).show();
        }
    }
}
