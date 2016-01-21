package com.cbt.game2048.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.ListView;

import com.cbt.game2048.R;
import com.cbt.game2048.adapter.GameHistoryAdapter;
import com.cbt.game2048.bean.ScoreBean;
import com.cbt.game2048.fragment.NoGameHistoryFrag;
import com.cbt.game2048.util.DBUtil;

import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Created by caobotao on 16/1/19.
 * 游戏记录界面
 */
public class GameHistoryActivity extends FragmentActivity {
    private ListView lvGameHistory;//展示游戏记录的ListView
    private List<ScoreBean> list;//放置游戏记录条目的List集合
    private DBUtil dbUtil = new DBUtil(this);//数据库工具类
    private GameHistoryAdapter adapter;//ListView的适配器

    private NoGameHistoryFrag noGameHistoryFrag;//当没有游戏记录时展示此Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);
        initView();//初始化控件
        initData();//初始化数据

    }

    private void initData() {
        //获取所有游戏记录的集合
        list = dbUtil.getAllScoreHistory();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        //当没有记录时展示对应的Fragment
        if (list.size() == 0) {
            if (noGameHistoryFrag == null) {
                noGameHistoryFrag = new NoGameHistoryFrag(this);
                transaction.add(R.id.rl_nogame, noGameHistoryFrag);
            } else {
                transaction.show(noGameHistoryFrag);
            }
        } else {//否则在ListView中展示相应的游戏记录
            if (noGameHistoryFrag != null) {
                transaction.hide(noGameHistoryFrag);
            }
            adapter = new GameHistoryAdapter(list, this);
            lvGameHistory.setAdapter(adapter);
        }
        transaction.commit();
    }

    private void initView() {
        lvGameHistory = (ListView) findViewById(R.id.lv_gameHistory);
    }
}
