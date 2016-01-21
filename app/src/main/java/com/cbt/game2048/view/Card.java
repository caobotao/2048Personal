package com.cbt.game2048.view;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by caobotao on 16/1/18.
 * 游戏中每个数字的Card类
 */
public class Card extends FrameLayout {
    private int num;
    private TextView label;
    public Card(Context context) {
        super(context);
        //设置数字文本属性
        label = new TextView(getContext());
        label.setTextSize(32);
        label.setGravity(Gravity.CENTER);
        //设置数字文本的显示数字
        setNum(0);
        //设置数字文本的布局属性
        LayoutParams lp = new LayoutParams(-1, -1);
        lp.setMargins(10, 10, 0, 0);
        addView(label, lp);
    }


    public int getNum() {
        return num;
    }

    //设置数字文本的数字
    public void setNum(int num) {
        this.num = num;
        //根据num设置数字文本上的数字及背景色
        if (num <= 0) {
            label.setText("");
            label.setBackgroundColor(0x33ffffff);
        }else {
            label.setText(num + "");
            switch (num) {
                case 2:
                    label.setBackgroundColor(0xffeee4da);
                    break;
                case 4:
                    label.setBackgroundColor(0xffede0c8);
                    break;
                case 8:
                    label.setBackgroundColor(0xfff2b179);
                    break;
                case 16:
                    label.setBackgroundColor(0xfff59563);
                    break;
                case 32:
                    label.setBackgroundColor(0xfff67c5f);
                    break;
                case 64:
                    label.setBackgroundColor(0xfff65e3b);
                    break;
                case 128:
                    label.setBackgroundColor(0xffedcf72);
                    break;
                case 256:
                    label.setBackgroundColor(0xffedcc61);
                    break;
                case 512:
                    label.setBackgroundColor(0xffc3f3f2);
                    break;
                case 1024:
                    label.setBackgroundColor(0xff61badf);
                    break;
                case 2048:
                    label.setBackgroundColor(0xffd910e6);
                    break;
            }
        }
    }

    //判断与某个Card是否相同
    public boolean equals(Card card) {
        return this.getNum() == card.getNum();
    }
}
