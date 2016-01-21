package com.cbt.game2048.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cbt.game2048.R;
import com.cbt.game2048.bean.ScoreBean;

import java.util.List;

/**
 * Created by caobotao on 16/1/19.
 * 游戏记录ListView的适配器
 */
public class GameHistoryAdapter extends BaseAdapter {
    private List<ScoreBean> list ;//游戏记录条目的List集合
    private LayoutInflater inflater;//从xml中获取控件

    public GameHistoryAdapter(List<ScoreBean> list, Context context) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //合理利用ListView的缓存机制获取每一条游戏记录条目
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.game_history_item, null);
            viewHolder.tv_score = (TextView) convertView.findViewById(R.id.tv_score);
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.tv_spendTime = (TextView) convertView.findViewById(R.id.tv_spendTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ScoreBean bean = list.get(position);
        viewHolder.tv_score.setText(bean.getScore() + "分");
        viewHolder.tv_date.setText(bean.getGameDate());
        viewHolder.tv_spendTime.setText(formatSec(bean.getSpendTimeSec()));
        return convertView;
    }

    //格式化秒数
    private String formatSec(int sec) {
        StringBuilder sb = new StringBuilder();
        int minute;
        minute = sec / 60;
        if (minute > 0) {
            sb.append(minute + "分");
            sec %= 60;
        }
        sb.append(sec + "秒");
        return sb.toString();
    }


    class ViewHolder{
        TextView tv_score;
        TextView tv_spendTime;
        TextView tv_date;
    }
}
