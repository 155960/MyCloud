package com.example.xrecyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zengqiang on 2017/8/24.
 */

public class WrapAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>{

    private static final int TYPE_REFRESH_HEADER = -5;
    private static final int TYPE_HEADER = -4;
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = -3;

    private RecyclerView.Adapter adapter;
    private SparseArray<View> mHeaderViews;
    private SparseArray<View> mFooterViews;

    private int headerPosition=1;

    public WrapAdapter(SparseArray<View> headers,SparseArray<View> footers,RecyclerView.Adapter adapter){
        this.mHeaderViews=headers;
        this.mFooterViews=footers;
        this.adapter=adapter;

    }

    public boolean isHeader(int position){
        return position>=0&&position<mHeaderViews.size();
    }

    private boolean isFooter(int position){
        return position<getItemCount()&&position>=getItemCount()-mFooterViews.size();
    }

    private boolean isRefreshHeader(int position){
        return position==0;
    }

    @Override
    public int getItemViewType(int position){
        if(isRefreshHeader(position)){
            return TYPE_REFRESH_HEADER;
        }
        if(isHeader(position)){
            return TYPE_HEADER;
        }
        if(isFooter(position)){
            return TYPE_FOOTER;
        }
        int adjPosition = position - getHeaderCount();
        int adapterCount;

        if (adapter != null) {
            adapterCount = adapter.getItemCount();
            if (adjPosition < adapterCount) {
                Log.e("xys", "wrap 63");
                return adapter.getItemViewType(adjPosition);
            }
        }
        return TYPE_NORMAL;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager manager=recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager){
            ((GridLayoutManager) manager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return isFooter(position)||isHeader(position)?((GridLayoutManager) manager).getSpanCount():1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder){
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp=holder.itemView.getLayoutParams();
        if(lp!=null&&(isFooter(holder.getLayoutPosition())||isHeader(holder.getLayoutPosition()))
                &&lp instanceof StaggeredGridLayoutManager.LayoutParams){
            StaggeredGridLayoutManager.LayoutParams layoutParams=(StaggeredGridLayoutManager.LayoutParams)lp;
            layoutParams.setFullSpan(true);
        }
    }

    @Override
    public long getItemId(int position){
        if (adapter != null && position >= getHeaderCount()) {
            int adjPosition = position - getHeaderCount();
            int adapterCount = adapter.getItemCount();
            if (adjPosition < adapterCount) {
                return adapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer){
        if(adapter!=null){
            adapter.unregisterAdapterDataObserver(observer);
        }
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer){
        if(adapter!=null){
            adapter.registerAdapterDataObserver(observer);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_REFRESH_HEADER){
            return new ViewHolder(mHeaderViews.get(0));
        }
        else if(viewType==TYPE_HEADER){
            Log.e("xys","header position"+(headerPosition+1));
            return new ViewHolder(mHeaderViews.get(headerPosition++));

        }
        else if(viewType==TYPE_FOOTER){
            return new ViewHolder(mFooterViews.get(0));
        }
        return  adapter.onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(isHeader(position)){
            return;
        }
        if(isFooter(position)){
            return ;
        }
        adapter.onBindViewHolder(holder,position-getHeaderCount());
    }


    @Override
    public int getItemCount() {
        if(adapter!=null){
            Log.e("wrap",adapter.getItemCount()+"item count");
            return getHeaderCount()+getFooterCount()+adapter.getItemCount();
        }else{
            Log.e("wrap",getHeaderCount()+" "+getFooterCount()+"");
            return getHeaderCount()+getFooterCount();
        }

    }

    private int getHeaderCount(){
        return mHeaderViews.size();
    }

    private int getFooterCount(){
        return mFooterViews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
