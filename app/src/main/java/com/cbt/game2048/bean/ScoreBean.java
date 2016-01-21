package com.cbt.game2048.bean;

import java.util.Date;

/**
 * Created by caobotao on 16/1/19.
 * 游戏记录条目
 */
public class ScoreBean {
    private int score;
    private String gameDate;
    private int spendTimeSec;

    public ScoreBean() {
    }

    public ScoreBean(int score, String gameDate, int spendTimeSec) {
        this.score = score;
        this.gameDate = gameDate;
        this.spendTimeSec = spendTimeSec;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public int getSpendTimeSec() {
        return spendTimeSec;
    }

    public void setSpendTimeSec(int spendTimeSec) {
        this.spendTimeSec = spendTimeSec;
    }

    @Override
    public String toString() {
        return "ScoreBean{" +
                "score=" + score +
                ", gameDate='" + gameDate + '\'' +
                ", spendTimeSec=" + spendTimeSec +
                '}';
    }
}
