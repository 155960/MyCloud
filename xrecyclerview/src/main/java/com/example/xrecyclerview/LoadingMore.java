package com.example.xrecyclerview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zengqiang on 2017/8/24.
 */

public class LoadingMore extends LinearLayout {

    public static final int STATE_LOADING=0;
    public static final int STATE_COMPLETE=1;
    public static final int STATE_NORMAL=2;
    private TextView mText;
    private AnimationDrawable mAnimation;
    private ImageView progress;


    public LoadingMore(Context context) {
        super(context);
        init(context);
    }

    public LoadingMore(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.yun_footer,this);
        mText=(TextView)findViewById(R.id.text);
        progress=(ImageView)findViewById(R.id.img_progress);
        mAnimation=(AnimationDrawable)progress.getDrawable();
        if(!mAnimation.isRunning()){
            mAnimation.start();
        }
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setState(int state){
        switch(state){
            case STATE_LOADING:
                if(!mAnimation.isRunning()){
                    mAnimation.start();
                }
                progress.setVisibility(VISIBLE);
                this.setVisibility(VISIBLE);
                mText.setText("努力加载中...");
                break;
            case STATE_COMPLETE:
                if(mAnimation.isRunning()){
                    mAnimation.stop();
                }
                this.setVisibility(GONE);
                mText.setText("努力加载中...");
                break;
            case STATE_NORMAL:
                if(mAnimation.isRunning()){
                    mAnimation.stop();
                }
                progress.setVisibility(GONE);
                this.setVisibility(VISIBLE);
                mText.setText("没有更多内容了");
                break;
        }
    }

    public void reset(){
        this.setVisibility(GONE);
    }
}
