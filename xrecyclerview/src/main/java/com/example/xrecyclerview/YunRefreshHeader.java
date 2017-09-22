package com.example.xrecyclerview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zengqiang on 2017/8/24.
 */

public class YunRefreshHeader extends LinearLayout implements BaseRefreshHeader {

    private Context mContext;
    private TextView mText;
    private AnimationDrawable mAnimation;
    private int mState=STATE_NORMAL;
    private int mMeasureHeight;
    private LinearLayout mContainer;


    public YunRefreshHeader(Context context) {
       this(context,null);
    }

    public YunRefreshHeader(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public YunRefreshHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        initView();
    }

    private void initView(){
        LayoutInflater.from(mContext).inflate(R.layout.yun_header,this);
        ImageView img=(ImageView)findViewById(R.id.img);
        mAnimation=(AnimationDrawable)img.getDrawable();
        if(mAnimation.isRunning()){
            mAnimation.stop();
        }
        mText=(TextView)findViewById(R.id.msg);
        measure(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasureHeight=getMeasuredHeight();
        setGravity(Gravity.CENTER_HORIZONTAL);
        mContainer=(LinearLayout)findViewById(R.id.container);
        mContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0));
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    }


    @Override
    public void onMove(float delta) {
        if(getVisibleHeight()>0||delta>0){
            setVisibleHeight((int)delta+getVisibleHeight());
            if(mState<=STATE_RELEASE_TO_REFRESH){
                if(getVisibleHeight()>mMeasureHeight){
                    setState(STATE_RELEASE_TO_REFRESH);
                }
                else{
                    setState(STATE_NORMAL);
                }
            }
        }
    }

    private void smoothScrollTo(int destHeight){
        ValueAnimator valueAnimator=ValueAnimator.ofInt(getVisibleHeight(),destHeight);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setVisibleHeight((int)valueAnimator.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    private void setState(int state){
        if(state==mState){
            return ;
        }
        switch(state){
            case STATE_NORMAL:
                if(mAnimation.isRunning()){
                    mAnimation.stop();
                }
                mText.setText("下拉刷新");
                break;
            case STATE_RELEASE_TO_REFRESH:
                if(!mAnimation.isRunning()){
                    mAnimation.start();
                }
                mText.setText("释放刷新");
                break;
            case STATE_REFRESHING:
                mText.setText("正在刷新");
                break;
            case STATE_DONE:
                mText.setText("刷新完成");
                break;
        }
        mState=state;
    }

    @Override
    public boolean releaseAction() {
        boolean isOnRefresh=false;
        int height=getVisibleHeight();
        if(height==0){
            isOnRefresh=false;
        }
        if(height>mMeasureHeight&&mState<STATE_REFRESHING){
            setState(STATE_REFRESHING);
            isOnRefresh=true;
        }

        int destHeight=0;
        if(mState==STATE_REFRESHING){
            destHeight=mMeasureHeight;

        }
        smoothScrollTo(destHeight);
        return isOnRefresh;
    }

    @Override
    public void refreshComplete() {
        setState(STATE_DONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                reset();
            }
        },500);
    }

    private void setVisibleHeight(int height){
        if(height<0){
            height=0;
        }
        LayoutParams lp=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        lp.height=height;
        mContainer.setLayoutParams(lp);
    }

    @Override
    public int getVisibleHeight() {
        return mContainer.getHeight();
    }


    private void reset(){
        smoothScrollTo(0);
        setState(STATE_NORMAL);
    }

    public int getState(){
        return mState;
    }
}
