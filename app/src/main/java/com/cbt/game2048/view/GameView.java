package com.cbt.game2048.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.media.AudioManager;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import com.cbt.game2048.activity.MainActivity;
import com.cbt.game2048.R;
import com.cbt.game2048.util.SharedPreferencesUtil;
import com.cbt.game2048.util.Sound;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by caobotao on 16/1/18.
 * 游戏控件
 */
public class GameView extends GridLayout {
    private Card[][] cardsMap = new Card[4][4];//十六个Card类
    private List<Point> emptyPoints = new ArrayList<>();
    private int cardWidth;
    private Sound sound;
    private int score;
    //手势滑动方向
    private static final int SWIPE_TO_LEFT  = 1;
    private static final int SWIPE_TO_RIGHT = 2;
    private static final int SWIPE_TO_UP    = 3;
    private static final int SWIPE_TO_DOWN  = 4;

    private SharedPreferencesUtil sharedPreferencesUtil;
    private OnGameChangeListener listener;

    private Date gameDate;
    private long startGameTime;
    private long finishGameTime;

    //向外部提供一个设置游戏状态监听的方法
    public void setOnGameChangeListener(OnGameChangeListener listener){
        this.listener = listener;
    }

    //回调接口
    public interface OnGameChangeListener{
        public void onFinishGame(Date gameDate,long startGameTime,long finishGameTime,int score);

        public void onScoreChange(int score);
    }

    public GameView(Context context) {
        super(context);
        initGame();
        sharedPreferencesUtil = SharedPreferencesUtil.getInstance(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGame();
        sharedPreferencesUtil = SharedPreferencesUtil.getInstance(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGame();
        sharedPreferencesUtil = SharedPreferencesUtil.getInstance(context);
    }

    //当本View宽高尺寸变化时被调用
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cardWidth = (Math.min(w, h) - 10) / 4;
        //向游戏界面中添加卡片
        addCard(cardWidth, cardWidth);
        //开始游戏
        startGame();
    }

    //添加16个卡片
    private void addCard(int cardWidth, int cardHeight) {
        Card c;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                c = new Card(getContext());
                c.setNum(0);
                addView(c, cardWidth, cardHeight);
                cardsMap[x][y] = c;
            }
        }
    }

    //开始游戏
    public void startGame() {
        score = 0;//将分数清零
        listener.onScoreChange(score);//调用分数变化的回调
        gameDate = new Date();//游戏开始时初始化游戏开始的时间
        startGameTime = System.currentTimeMillis();//游戏开始时初始化游戏开始的毫秒数
        //先将所有卡片设置为没有数字
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cardsMap[x][y].setNum(0);
            }
        }
        //随机向两个卡片上添加数字
        addRandomNum(2);
    }

    private void addRandomNum(int count) {
        //递归添加数字
        if (count > 1) {
            addRandomNum(count - 1);
        }
        //没有数字的点的集合
        emptyPoints.clear();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                //将没有数字的卡片的坐标添加到emptyPoints集合中
                if (cardsMap[x][y].getNum() <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }
        //随机获取一个没有数字卡片的坐标
        Point point = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));
        //设置此坐标对应的卡片按1:9的比例设置卡片上的数字为2或4
        cardsMap[point.x][point.y].setNum(Math.random() > 0.1 ? 2 : 4);

    }

    //初始化游戏
    public void initGame() {
        //获取音效类
        sound = new Sound(getContext(), 10, AudioManager.STREAM_SYSTEM, 5, R.raw.impact);

        //设置此游戏控件为4列
        setColumnCount(4);
        //设置此游戏控件的背景色
        setBackgroundColor(0xffbbada0);

        //设置此游戏控件的点击事件
        setOnTouchListener(new OnTouchListener() {
            float startX, startY, offsetX, offsetY;

            //判断手势滑动方向
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;
                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                swipeTo(SWIPE_TO_LEFT);
                            } else if (offsetX > 5) {
                                swipeTo(SWIPE_TO_RIGHT);
                            }
                        } else {
                            if (offsetY < -5) {
                                swipeTo(SWIPE_TO_UP);
                            } else if (offsetY > 5) {
                                swipeTo(SWIPE_TO_DOWN);
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    //检查游戏是否结束
    private void checkFinishGame() {
        boolean isFinish = true;
        ALL:
        for (int y = 0;y < 4; y++) {
            for (int x = 0; x < 4; x ++) {
                //如果游戏控件中没有无数字的卡片且任何两个相邻的卡片上的数字都不同时则游戏结束
                if (cardsMap[x][y].getNum() == 0 ||
                        (x > 0 && cardsMap[x][y].equals(cardsMap[x - 1][y]))||
                        (x < 3 && cardsMap[x][y].equals(cardsMap[x + 1][y]))||
                        (y > 0 && cardsMap[x][y].equals(cardsMap[x][y - 1]))||
                        (y < 3 && cardsMap[x][y].equals(cardsMap[x][y + 1]))){
                    isFinish = false;
                    break ALL;
                }
            }
        }
        //如果游戏结束则记录结束时的毫秒数,并进行结束游戏时的回调
        if (isFinish) {
            finishGameTime = System.currentTimeMillis();
            listener.onFinishGame(gameDate,startGameTime,finishGameTime,score);
        }
    }

    //根据sharedPreferences中的配置查看是否需要播放音效
    private void checkPlaySound() {
        String soundState = sharedPreferencesUtil.getSwitchState();
        Log.e("Tag", "soundState:" + soundState);
        if (SharedPreferencesUtil.OPENED.equals(soundState)) {
            sound.play();
        }
    }

    //根据不同的方向标签向处理不同的滑动方向
    private void swipeTo(int dir) {
        //记录本次滑动是否有合并的卡片
        boolean merge = false;
        switch (dir) {
            //向左滑动
            case SWIPE_TO_LEFT:
                for (int y = 0; y < 4; y++) {
                    for (int x = 0; x < 4; x++) {
                        for (int x1 = x + 1; x1 < 4; x1++) {
                            if (cardsMap[x1][y].getNum() > 0) {
                                if (cardsMap[x][y].getNum() <= 0) {
                                    cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                                    cardsMap[x1][y].setNum(0);
                                    merge = true;
                                    x--;
                                } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                                    cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                                    cardsMap[x1][y].setNum(0);
                                    score += cardsMap[x][y].getNum();
                                    listener.onScoreChange(score);
                                    checkPlaySound();
                                    merge = true;
                                }
                                break;
                            }
                        }
                    }
                }
                break;

            //向右滑动
            case SWIPE_TO_RIGHT:
                for (int y = 0; y < 4; y++) {
                    for (int x = 3; x >= 0; x--) {
                        for (int x1 = x - 1; x1 >= 0; x1--) {
                            if (cardsMap[x1][y].getNum() > 0) {
                                if (cardsMap[x][y].getNum() <= 0) {
                                    cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                                    cardsMap[x1][y].setNum(0);
                                    x++;
                                    merge = true;
                                } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                                    cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                                    cardsMap[x1][y].setNum(0);
                                    score += cardsMap[x][y].getNum();
                                    listener.onScoreChange(score);
                                    checkPlaySound();
                                    merge = true;
                                }
                                break;

                            }
                        }
                    }
                }
                break;

            //向上滑动
            case SWIPE_TO_UP:
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) {
                        for (int y1 = y + 1; y1 < 4; y1++) {
                            if (cardsMap[x][y1].getNum() > 0) {
                                if (cardsMap[x][y].getNum() <= 0) {
                                    cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                                    cardsMap[x][y1].setNum(0);
                                    y--;
                                    merge = true;
                                } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                                    cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                                    cardsMap[x][y1].setNum(0);
                                    score += cardsMap[x][y].getNum();
                                    listener.onScoreChange(score);
                                    checkPlaySound();
                                    merge = true;
                                }
                                break;

                            }
                        }
                    }
                }
                break;

            //向下滑动
            case SWIPE_TO_DOWN:
                for (int x = 0; x < 4; x++) {
                    for (int y = 3; y >= 0; y--) {
                        for (int y1 = y - 1; y1 >= 0; y1--) {
                            if (cardsMap[x][y1].getNum() > 0) {
                                if (cardsMap[x][y].getNum() <= 0) {
                                    cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                                    cardsMap[x][y1].setNum(0);
                                    y++;
                                    merge = true;
                                } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                                    cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                                    cardsMap[x][y1].setNum(0);
                                    score += cardsMap[x][y].getNum();
                                    listener.onScoreChange(score);
                                    merge = true;
                                    checkPlaySound();
                                }
                                break;

                            }
                        }
                    }
                }
                break;
        }

        //如果有合并的卡片则随机向一个没有数字的卡片添加数字
        if (merge) {
            addRandomNum(1);
            checkFinishGame();//添加后检查游戏是否结束
        }
    }

}
