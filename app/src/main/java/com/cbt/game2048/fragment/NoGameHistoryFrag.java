package com.cbt.game2048.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.cbt.game2048.R;
import com.cbt.game2048.activity.MainActivity;

/**
 * Created by caobotao on 16/1/20.
 * 当点击查看记录没有游戏记录时展示的Fragment
 */
public class NoGameHistoryFrag extends Fragment {
    private Context context ;

    public NoGameHistoryFrag(Context context) {
        this.context = context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.no_game_history,container,false);
        //开始游戏按钮
        Button startGame = (Button) view.findViewById(R.id.btn_start_game);
        //进入游戏界面
        startGame.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
        return view;

    }
}
