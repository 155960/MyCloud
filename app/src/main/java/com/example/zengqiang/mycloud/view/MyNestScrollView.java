package com.example.zengqiang.mycloud.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by zengqiang on 2017/9/16.
 */

public class MyNestScrollView extends NestedScrollView {

    private ScrollInterface scrollInterface;

    public MyNestScrollView(Context context) {
        super(context);
    }

    public MyNestScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(scrollInterface!=null){
            scrollInterface.onScroll(l,t,oldl,oldt);
        }

    }

    public MyNestScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollInterface(ScrollInterface scrollInterface){
        this.scrollInterface=scrollInterface;
    }

    public interface ScrollInterface{
        void onScroll(int l,int t,int oldL,int oldT);
    }
}
