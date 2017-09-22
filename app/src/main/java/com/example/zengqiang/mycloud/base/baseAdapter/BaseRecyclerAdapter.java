package com.example.zengqiang.mycloud.base.baseAdapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengqiang on 2017/8/29.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    protected List<T> data=new ArrayList<>();
    protected OnItemClickListener<T> listener;
    protected OnItemLongClickListener<T> longListener;
    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.onBaseBindViewHolder(data.get(position),position);
    }

    public void addAll(List<T> data){
        this.data.addAll(data);
    }

    public void clear(){
        Log.e("a","clear");
        data.clear();
    }

    public void add(T object){
        data.add(object);
    }

    public void setOnClickListener(OnItemClickListener<T> listener){
        this.listener=listener;
    }

    public List<T> getData(){
        return data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
