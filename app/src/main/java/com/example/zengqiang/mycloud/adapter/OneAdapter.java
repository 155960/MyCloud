package com.example.zengqiang.mycloud.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.example.zengqiang.mycloud.R;
import com.example.zengqiang.mycloud.app.MyApplication;
import com.example.zengqiang.mycloud.base.baseAdapter.BaseRecyclerAdapter;
import com.example.zengqiang.mycloud.base.baseAdapter.BaseRecyclerViewHolder;
import com.example.zengqiang.mycloud.bean.SubjectsBean;
import com.example.zengqiang.mycloud.databinding.OneItemBinding;
import com.example.zengqiang.mycloud.utils.CommonUtils;
import com.example.zengqiang.mycloud.utils.PerfectListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * Created by zengqiang on 2017/9/4.
 */

public class OneAdapter extends BaseRecyclerAdapter<SubjectsBean> {

    private Activity activity;

    public OneAdapter(Activity activity){
        this.activity=activity;
    }

    private class ViewHolder extends BaseRecyclerViewHolder<SubjectsBean,OneItemBinding>{

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
        }

        @Override
        public void onBindViewHolder(SubjectsBean object, int position) {
            if(object!=null){
                binding.setSubjectsBean(object);
                binding.viewColor.setBackgroundColor(CommonUtils.randomColor());
                ViewHelper.setScaleX(itemView,0.8f);
                ViewHelper.setScaleY(itemView,0.8f);
                ViewPropertyAnimator.animate(itemView).scaleX(1.0f).setDuration(350).setInterpolator(
                        new OvershootInterpolator()).start();
                ViewPropertyAnimator.animate(itemView).scaleY(1.0f).setDuration(350).setInterpolator(
                        new OvershootInterpolator()).start();
                binding.oneLine.setOnClickListener(new PerfectListener() {
                    @Override
                    public void onDoubleClick(View v) {
                        Toast.makeText(MyApplication.getInstance(),"点击",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.one_item);
    }
}
