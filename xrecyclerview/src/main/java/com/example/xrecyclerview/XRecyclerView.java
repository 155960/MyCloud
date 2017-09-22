package com.example.xrecyclerview;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zengqiang on 2017/8/24.
 */

public class XRecyclerView extends RecyclerView {
    private LoadingListener mLoadingListener;
    private WrapAdapter mWrapAdater;

    private SparseArray<View> mHeadersView=new SparseArray<>();
    private SparseArray<View> mFootersView=new SparseArray<>();

    private boolean pullRefreshEnabled=true;
    private boolean loadingMoreEnabled=true;

    private YunRefreshHeader mRefreshHeader;

    private boolean isLoadingData;
    private int preTotal;
    private boolean isNoMore;

    private float mLastY=-1;

    private static final float DRAG_RATE = 1.75f;

    private boolean isOther=false;

    public XRecyclerView(Context context) {
        this(context,null);
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        if(pullRefreshEnabled){
            YunRefreshHeader refreshHeader=new YunRefreshHeader(context);
            mHeadersView.put(0,refreshHeader);
            mRefreshHeader=refreshHeader;
        }
        LoadingMore loadingMore=new LoadingMore(context);
        addFooterView(loadingMore,false);
        mFootersView.get(0).setVisibility(GONE);

    }

    public void addFooterView(final View view,boolean isOther){
        mFootersView.clear();
        mFootersView.put(0,view);
        this.isOther=isOther;
    }


    public void clearHeader(){
        mHeadersView.clear();
        final float scale=getContext().getResources().getDisplayMetrics().density;
        int height=(int)(1*scale+0.5f);
        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
        View v=new View(getContext());
        v.setLayoutParams(layoutParams);
        mHeadersView.put(0,v);
    }

    public void addHeaderView(View v){
        if(pullRefreshEnabled&&!(mHeadersView.get(0) instanceof YunRefreshHeader)){
            YunRefreshHeader refreshHeader=new YunRefreshHeader(getContext());
            mHeadersView.put(0,refreshHeader);
            mRefreshHeader=refreshHeader;
        }
        mHeadersView.put(mHeadersView.size(),v);
    }

    public void loadMoreComplete(){
        isLoadingData = false;
        View footer=mFootersView.get(0);
        if(preTotal<=getLayoutManager().getItemCount()){
            if(footer instanceof LoadingMore){
                ((LoadingMore)footer).setState(LoadingMore.STATE_COMPLETE);
            }
            else{
                footer.setVisibility(GONE);
            }
        }
        else{
            if(footer instanceof LoadingMore){
                ((LoadingMore)footer).setState(LoadingMore.STATE_NORMAL);
            }
            else{
                footer.setVisibility(GONE);
            }
            isNoMore=true;
        }
        preTotal=getLayoutManager().getItemCount();
    }

    public void noMoreLoading(){
        isLoadingData=false;
        isNoMore=true;
        final View view=mFootersView.get(0);
        if(view instanceof LoadingMore){
            ((LoadingMore)view).setState(LoadingMore.STATE_NORMAL);
        }
        else{
            view.setVisibility(GONE);
        }
        if(isOther){
            view.setVisibility(VISIBLE);
        }
    }

    public void refreshComplete(){
        if(isLoadingData){
            loadMoreComplete();
        }
        else{
           mRefreshHeader.refreshComplete();
        }
    }

    @Override
    public void setAdapter(Adapter adapter){
        mWrapAdater=new WrapAdapter(mHeadersView,mFootersView,adapter);
      //  Log.e("xys","second");
        super.setAdapter(mWrapAdater);
        adapter.registerAdapterDataObserver(mAdapterObserver);
        //Log.e("xys","firth");
    }

    private RecyclerView.AdapterDataObserver mAdapterObserver=new AdapterDataObserver() {
        @Override
        public void onChanged() {
            mWrapAdater.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdater.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdater.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdater.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdater.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdater.notifyItemMoved(fromPosition, toPosition);
        }
    };

    @Override
    public void onScrollStateChanged(int state){
        super.onScrollStateChanged(state);

        if(state==RecyclerView.SCROLL_STATE_IDLE&&mLoadingListener!=null&&
                !isLoadingData&&loadingMoreEnabled){
            LayoutManager layoutManager=getLayoutManager();
            int lastPosition;
            if(layoutManager instanceof GridLayoutManager){
              //  Log.e("xsss","grid");
                lastPosition=((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            else if(layoutManager instanceof StaggeredGridLayoutManager){
                //Log.e("xsss","stagg");
                int[] a=new int[((StaggeredGridLayoutManager)layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(a);
                lastPosition=findMax(a);
            }
            else{
               // Log.e("xsss","sss");
                lastPosition=((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
              //  Log.e("xsss","www");
            }
            if(layoutManager.getChildCount()>0&&lastPosition>=layoutManager.getItemCount()-1
                    &&!isNoMore&&layoutManager.getItemCount()>layoutManager.getChildCount()
                    &&mRefreshHeader.getState()<YunRefreshHeader.STATE_REFRESHING){
                View view=mFootersView.get(0);
                isLoadingData=true;
                if(view instanceof LoadingMore){
                    ((LoadingMore) view).setState(LoadingMore.STATE_LOADING);
                }
                else{
                    view.setVisibility(VISIBLE);
                }
                if(isNetWorkConnected(getContext())){
                    mLoadingListener.onLoadMore();
                }
                else{
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLoadingListener.onLoadMore();
                        }
                    },1000);
                }
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        if(mLastY==-1){
            mLastY=e.getRawY();
        }
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastY=e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float delay=e.getRawY()-mLastY;
                mLastY=e.getRawY();
                if(isOnTop()&&pullRefreshEnabled){
                    mRefreshHeader.onMove(delay/DRAG_RATE);
                }
                if(mRefreshHeader.getVisibleHeight()>0&&mRefreshHeader.
                        getState()<YunRefreshHeader.STATE_REFRESHING){
                    return false;
                }
                break;
            default:
                mLastY=-1;
                if(isOnTop()&&pullRefreshEnabled){
                    if(mRefreshHeader.releaseAction()){
                        if(mLoadingListener!=null){
                            mLoadingListener.onRefresh();
                            preTotal=0;
                            isNoMore=false;
                            View v=mFootersView.get(0);
                            if(v instanceof LoadingMore)
                            if(v.getVisibility()!=GONE){
                                v.setVisibility(GONE);
                            }
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(e);
    }


    public boolean isOnTop(){
        if(mHeadersView==null||mHeadersView.size()==0){
            return false;
        }
        View view =mHeadersView.get(0);
        if(view.getParent()!=null){
            return true;
        }
        return false;
    }

    public static boolean isNetWorkConnected(Context context){
        if(context!=null){
            ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info=manager.getActiveNetworkInfo();
            if(info!=null){
                return info.isAvailable();
            }
        }
        return false;
    }

    private int findMax(int[] lastPosition){
        int max=lastPosition[0];
        for(int value:lastPosition){
            if(value>max){
                max=value;
            }
        }
        return max;
    }

    public void setLoadingListener(LoadingListener listener){
        this.mLoadingListener=listener;
    }

    public void setPullRefreshEnabled(boolean b){
        this.pullRefreshEnabled=b;
    }

    public void setLoadingMoreEnabled(boolean a){
        this.loadingMoreEnabled=a;
        if(!loadingMoreEnabled){
            if(mFootersView!=null){
                mFootersView.remove(0);
            }
        }
        else{
            if(mFootersView!=null){
                LoadingMore loadingMore=new LoadingMore(getContext());
                addFooterView(loadingMore,false);
                //loadingMore.setVisibility(GONE);
            }
        }
    }

    public interface LoadingListener{
        void onRefresh();
        void onLoadMore();
    }

    public void reset(){
        isNoMore=false;
        preTotal=0;
        View v=mFootersView.get(0);
        if(v instanceof LoadingMore){
            ((LoadingMore) v).reset();
        }
    }
}
