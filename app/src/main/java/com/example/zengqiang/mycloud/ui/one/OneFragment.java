package com.example.zengqiang.mycloud.ui.one;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zengqiang.mycloud.MainActivity;
import com.example.zengqiang.mycloud.R;
import com.example.zengqiang.mycloud.adapter.OneAdapter;
import com.example.zengqiang.mycloud.app.Constants;
import com.example.zengqiang.mycloud.base.BaseFragment;
import com.example.zengqiang.mycloud.bean.HotMoiveBean;
import com.example.zengqiang.mycloud.databinding.FragmentOneBinding;
import com.example.zengqiang.mycloud.http.HttpClient;
import com.example.zengqiang.mycloud.http.cache.ACache;
import com.example.zengqiang.mycloud.utils.ImgLoadUtils;
import com.example.zengqiang.mycloud.utils.PerfectListener;
import com.example.zengqiang.mycloud.utils.SPUtils;
import com.example.zengqiang.mycloud.utils.TimeUtil;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zengqiang on 2017/8/22.
 */

public class OneFragment extends BaseFragment<FragmentOneBinding>{

    private boolean isPrepareed=false;
    private boolean isFirst=true;
    private boolean mIsLoading=false;

    private ACache mCache;
    private MainActivity mainActivity;

    private HotMoiveBean mHotMoive;
    private View mHeaderView=null;
    private OneAdapter mAdapter;

    @Override
    public int setContent() {
        return R.layout.fragment_one;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity=(MainActivity)context;
    }

    @Override
    public void onActivityCreated(Bundle save) {
        super.onActivityCreated(save);
        mAdapter=new OneAdapter(getActivity());
        mCache=ACache.get(getActivity());
        mHotMoive=(HotMoiveBean) mCache.getAsObject(Constants.ONE_HOT_MOVIE);
        isPrepareed=true;
    }

    @Override
    public void loadData(){
        if(!isPrepareed||!mIsVisivle){
            return ;
        }
        String data=SPUtils.getString("one_data","2016-11-26");
        if(!data.equals(TimeUtil.getData())&&!mIsLoading){
            showLoading();
            postDelayLoad();
        }
        else{
            if(mIsLoading){
                return;
            }
            if(!isFirst){
                return;
            }
            showLoading();
            if(mHotMoive==null&&!mIsLoading){
                postDelayLoad();
            }
            else{
                mBinding.oneList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            setAdapter(mHotMoive);
                            showContentView();
                        }
                    }
                }, 150);
            }
        }
    }

    @Override
    public void onRefresh(){
        postDelayLoad();
    }

    private void postDelayLoad(){
        synchronized (this){
            if(!mIsLoading){
                mIsLoading=true;
                mBinding.oneList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadHotMoive();
                    }
                },150);
            }
        }
    }

    private void loadHotMoive(){
        Subscription subscription= HttpClient.Build.getDouBanService().getHotMoive()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotMoiveBean>() {
                    @Override
                    public void onCompleted() {
                        showContentView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showContentView();
                        if(mAdapter==null||mAdapter.getItemCount()==0){
                            showError();
                        }
                    }

                    @Override
                    public void onNext(HotMoiveBean hotMoiveBean) {
                        if(hotMoiveBean!=null){
                            mCache.remove(Constants.ONE_HOT_MOVIE);
                            mCache.put(Constants.ONE_HOT_MOVIE,hotMoiveBean,43200);
                            setAdapter(hotMoiveBean);
                            SPUtils.putString("one_data", TimeUtil.getData());
                            mIsLoading=false;
                        }
                    }
                });
        addSubscription(subscription);
    }

    private void setAdapter(HotMoiveBean bean){
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.oneList.setLayoutManager(layoutManager);
        mBinding.oneList.setPullRefreshEnabled(false);
        mBinding.oneList.clearHeader();
        mBinding.oneList.setLoadingMoreEnabled(false);
        if(mHeaderView==null){
            mHeaderView= LayoutInflater.from(getActivity()).inflate(
                    R.layout.one_header,null,false);
            View viewTop=mHeaderView.findViewById(R.id.ll_movie_top);
            ImageView imageView=(ImageView)mHeaderView.findViewById(R.id.iv_img);
            ImgLoadUtils.setDisplayRandom(3, Constants.ONE_URL_01,imageView);
            viewTop.setOnClickListener(new PerfectListener() {
                @Override
                public void onDoubleClick(View v) {
                    Toast.makeText(getContext(),"点击",Toast.LENGTH_SHORT).show();
                }
            });
            mBinding.oneList.addHeaderView(mHeaderView);
            mAdapter.clear();
            mAdapter.addAll(bean.getSubjects());
            mBinding.oneList.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            isFirst=false;
        }

    }
}
