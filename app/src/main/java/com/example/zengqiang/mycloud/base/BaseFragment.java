package com.example.zengqiang.mycloud.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.zengqiang.mycloud.R;
import com.example.zengqiang.mycloud.utils.PerfectListener;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zengqiang on 2017/8/22.
 */

public abstract class BaseFragment<SV extends ViewDataBinding> extends Fragment {
    protected  SV mBinding;
    protected  boolean mIsVisivle;

    protected LinearLayout mProcessBar;
    protected RelativeLayout mContainer;
    protected LinearLayout mRefresh;

    protected AnimationDrawable animationDrawable;
    protected CompositeSubscription subscription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle save){
        super.onCreateView(inflater,viewGroup,save);
        View ll=inflater.inflate(R.layout.fragment_base,null,false);
        mBinding= DataBindingUtil.inflate(getActivity().getLayoutInflater(),setContent(),null,false);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mBinding.getRoot().setLayoutParams(layoutParams);
        mContainer=(RelativeLayout)ll.findViewById(R.id.container);
        mContainer.addView(mBinding.getRoot());
        return ll;
    }

    public abstract int setContent();

    @Override
    public void setUserVisibleHint(boolean isHint){
        super.setUserVisibleHint(isHint);
        if(getUserVisibleHint()){
            mIsVisivle=true;
            onVisible();
        }
        else{
            mIsVisivle=false;
            onInVisible();
        }
    }

    @Override
    public void onActivityCreated(Bundle save){
        super.onActivityCreated(save);
        mProcessBar=getView(R.id.process_bar);
        ImageView img=getView(R.id.process_img);
        animationDrawable=(AnimationDrawable)img.getDrawable();
        if(!animationDrawable.isRunning()){
            animationDrawable.start();
        }
        mRefresh=getView(R.id.error_refresh);
        mRefresh.setOnClickListener(new PerfectListener() {
            @Override
            public void onDoubleClick(View v) {
                showLoading();
                onRefresh();
            }
        });
        mBinding.getRoot().setVisibility(View.GONE);
    }

    protected void showLoading(){
        if(mProcessBar.getVisibility()!=View.VISIBLE){
            mProcessBar.setVisibility(View.VISIBLE);
        }

        if(!animationDrawable.isRunning()){
            animationDrawable.start();
        }

        if(mRefresh.getVisibility()!=View.GONE){
            mRefresh.setVisibility(View.GONE);
        }

        if (mBinding.getRoot().getVisibility()!=View.GONE){
            mBinding.getRoot().setVisibility(View.GONE);
        }
    }

    protected void onRefresh(){

    }

    protected void showContentView() {
        if (mProcessBar.getVisibility() != View.GONE) {
            mProcessBar.setVisibility(View.GONE);
        }
        // 停止动画
        if (animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
        if (mRefresh.getVisibility() != View.GONE) {
            mRefresh.setVisibility(View.GONE);
        }
        if (mBinding.getRoot().getVisibility() != View.VISIBLE) {
            mBinding.getRoot().setVisibility(View.VISIBLE);
        }
    }

    protected void showError() {
        if (mProcessBar.getVisibility() != View.GONE) {
            mProcessBar.setVisibility(View.GONE);
        }
        // 停止动画
        if (animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
        if (mRefresh.getVisibility() != View.VISIBLE) {
            mRefresh.setVisibility(View.VISIBLE);
        }
        if (mBinding.getRoot().getVisibility() != View.GONE) {
            mBinding.getRoot().setVisibility(View.GONE);
        }
    }
    protected void onInVisible(){

    }

    protected void loadData(){

    }

    protected void onVisible(){
        loadData();
    }

    protected <T extends View> T getView(int id){
        return (T)getView().findViewById(id);
    }

    public void addSubscription(Subscription s){
        if(this.subscription==null){
            subscription=new CompositeSubscription();
        }
        this.subscription.add(s);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(subscription!=null&&subscription.hasSubscriptions()){
            subscription.unsubscribe();
        }
    }
}
