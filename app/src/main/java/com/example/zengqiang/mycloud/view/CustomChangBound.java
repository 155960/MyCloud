package com.example.zengqiang.mycloud.view;

import android.animation.Animator;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.ChangeBounds;
import android.transition.TransitionValues;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

/**
 * Created by zengqiang on 2017/9/7.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class CustomChangBound extends ChangeBounds {
    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues start,TransitionValues
                                   end){
        Animator changeBounds=super.createAnimator(sceneRoot,start,end);
        if(start==null||end==null||changeBounds==null){
            return null;
        }
        changeBounds.setDuration(500);
        changeBounds.setInterpolator(AnimationUtils.loadInterpolator(sceneRoot.getContext(),
                android.R.interpolator.fast_out_slow_in));
        return changeBounds;
    }
}
