package com.cbt.game2048.util;

import android.content.Context;
import android.media.SoundPool;

/**
 * Created by caobotao on 16/1/19.
 * 音效播放类
 */
public class Sound extends SoundPool{
    private int music;

    //通过音效资源实例化此类
    public Sound(Context context, int maxStreams, int streamType, int srcQuality, int soundRes) {
        super(maxStreams, streamType, srcQuality);
        music = this.load(context, soundRes, 1);
    }

    //播放音效
    public void play() {
        super.play(music, 1, 1, 0, 0, 1);
    }

}
