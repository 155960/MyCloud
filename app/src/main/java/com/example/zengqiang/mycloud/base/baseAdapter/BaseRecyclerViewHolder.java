package com.example.zengqiang.mycloud.base.baseAdapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zengqiang on 2017/8/29.
 */

public abstract class BaseRecyclerViewHolder<T,D extends ViewDataBinding> extends RecyclerView.ViewHolder {

    public D binding;

    public BaseRecyclerViewHolder(ViewGroup parent, int layoutId) {
        super(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),layoutId,parent,false).getRoot());
        binding=DataBindingUtil.getBinding(this.itemView);
    }

    public abstract void onBindViewHolder(T object,int position);

    public void onBaseBindViewHolder(T object,int position){
        onBindViewHolder(object,position);
        binding.executePendingBindings();
    }
}
