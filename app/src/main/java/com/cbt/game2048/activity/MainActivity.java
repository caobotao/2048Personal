package com.cbt.game2048.activity;

import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.cbt.game2048.R;
import com.cbt.game2048.fragment.NavigateFrag;
import com.cbt.game2048.util.DBUtil;
import com.cbt.game2048.view.GameView;
import com.cbt.game2048.view.GameView.OnGameChangeListener;

import java.util.Date;

/**
 *  游戏主界面
 */
public class MainActivity extends Activity {
    private TextView tvScore;
    private TextView tvBest;
    private LinearLayout gameLayout;
    private GameView gameView;
    private Button replay;
    private Button scanHistory;
    private DBUtil dbUtil = new DBUtil(this);//数据库工具类

    private NavigateFrag navigateFrag;//展示导航栏的Fragment

    public static final String IS_QUIT = "isQuit";//退出游戏的标签
    public static final String IS_START_NEW_GAME = "startNewGame";//开始新游戏的标签

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();//初始化控件
        initEvent();//初始化事件
        addGameBoard(gameView, gameLayout);//添加游戏控件
        checkBestScore();//设置最高分
    }

    private void initEvent() {
        //点击重新开始按钮时则重新开始游戏
        replay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.startGame();
            }
        });
        //点击查看记录按钮时跳转到游戏记录页面
        scanHistory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scanHistory();
            }
        });
        //设置游戏状态改变时的监听
        gameView.setOnGameChangeListener(new OnGameChangeListener() {
            //游戏结束时的回调
            @Override
            public void onFinishGame(Date gameDate, long startGameTime, long finishGameTime, int score) {
                Log.e("Tag", "Game Finish");
                Log.e("Tag", gameDate.toString());
                //展示提示窗口
                new AlertDialog.Builder(MainActivity.this).setTitle("游戏结束").setMessage("您得了" + score + "分!").
                        setNegativeButton("重新开始", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                gameView.startGame();
                            }
                        }).setPositiveButton("查看记录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanHistory();
                    }
                }).show();
                //将游戏记录插入数据库
                dbUtil.insertData(score, finishGameTime - startGameTime, gameDate);
                //检查设置最高分
                checkBestScore();
            }
            //分数改变时的回调
            @Override
            public void onScoreChange(int score) {
                tvScore.setText(score + "");
            }
        });
    }

    //检查设置最高分
    private void checkBestScore() {
        int bestScore = dbUtil.getBestScore();
        tvBest.setText(bestScore + "");
    }

    //跳转到游戏记录页面
    private void scanHistory() {
        Intent intent = new Intent(MainActivity.this, GameHistoryActivity.class);
        startActivity(intent);
    }

    private void initView() {
        tvScore = (TextView) findViewById(R.id.tvSocre);
        tvBest = (TextView) findViewById(R.id.tvBest);
        gameView = new GameView(this);
        gameLayout = (LinearLayout) findViewById(R.id.game_layout);

        navigateFrag = (NavigateFrag) getFragmentManager().findFragmentById(R.id.frag_navigate);

        replay = (Button) findViewById(R.id.replay);
        scanHistory = (Button) findViewById(R.id.scanHistory);
    }

    //添加游戏控件,并设置此控件宽高与屏幕宽度一致
    private void addGameBoard(GameView gameView, LinearLayout gameLayout) {
        WindowManager wm = (WindowManager) (this.getSystemService(Context.WINDOW_SERVICE));
        int width = wm.getDefaultDisplay().getWidth();
        LayoutParams layoutParams = new LayoutParams(width, width);
        layoutParams.gravity = Gravity.CENTER;
        gameLayout.addView(gameView, layoutParams);
    }

    //由于此Activity的lunchMode设置为了singleTask,当调转至本Activity检查是否需要退出游戏或者开启新游戏
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean isQuit = intent.getBooleanExtra(IS_QUIT, false);
        boolean isStartNewGame = intent.getBooleanExtra(IS_START_NEW_GAME, false);
        if (isStartNewGame) {
            gameView.startGame();
        }
        if (isQuit) {
            finish();
        }
    }

    /**
     * 当Activity被关闭时，如果PopupWindow仍在显示，此时就会抛出Window Leaked异常，原因是
     * PopupWindow附属于Activity的WindowManager，而Activity被关闭了，窗体也不再存在。所以
     * 应该覆写onStop方法如下，确保在Activity退出前先关闭PopupWindow。
     */
    @Override
    protected void onStop() {
        navigateFrag.dismissPopupWindow();
        super.onStop();
    }
}
