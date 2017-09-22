package com.example.zengqiang.mycloud.ui.gank.child;

import android.animation.Animator;
import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zengqiang.mycloud.R;
import com.example.zengqiang.mycloud.adapter.EverydayAdapter;
import com.example.zengqiang.mycloud.app.Constants;
import com.example.zengqiang.mycloud.app.MyApplication;
import com.example.zengqiang.mycloud.base.BaseFragment;
import com.example.zengqiang.mycloud.bean.AndroidBean;
import com.example.zengqiang.mycloud.bean.FrontPageBean;
import com.example.zengqiang.mycloud.databinding.FragmentEverydayBinding;
import com.example.zengqiang.mycloud.databinding.HeaderItemEverydayBinding;
import com.example.zengqiang.mycloud.databinding.ItemEverydayFooterBinding;
import com.example.zengqiang.mycloud.http.RequestImg;
import com.example.zengqiang.mycloud.http.cache.ACache;
import com.example.zengqiang.mycloud.http.rx.RxBus;
import com.example.zengqiang.mycloud.http.rx.RxBusMessage;
import com.example.zengqiang.mycloud.http.rx.RxConstants;
import com.example.zengqiang.mycloud.model.EverydayModel;
import com.example.zengqiang.mycloud.utils.GridImageLoader;
import com.example.zengqiang.mycloud.utils.PerfectListener;
import com.example.zengqiang.mycloud.utils.SPUtils;
import com.example.zengqiang.mycloud.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;


import rx.Subscription;

/**
 * Created by zengqiang on 2017/8/22.
 */

public class EverydayFragment extends BaseFragment<FragmentEverydayBinding> {

    private static final String TAG="EverydayFragment";

    private ACache mCache;
    private ArrayList<List<AndroidBean>> mLists;
    private ArrayList<String> mBannerImages;

    private EverydayModel mEverydayModel;
    private RotateAnimation animation;

    private HeaderItemEverydayBinding headerItemEverydayBinding;
    private ItemEverydayFooterBinding footerBinding;
    private EverydayAdapter mEverydayAdapter;
    private View mHeaderView;
    private View mFooterView;

    private boolean mInPressed=false;
    private boolean isFirst=true;
    private boolean isOldDayRequset=false;

    String year=getDate().get(0);
    String month=getDate().get(1);
    String day=getDate().get(2);
    @Override
    public int setContent() {
        return R.layout.fragment_everyday;
    }

    @Override
    public void onActivityCreated(Bundle save) {
        super.onActivityCreated(save);
        showContentView();
        mBinding.lineLoading.setVisibility(View.VISIBLE);
        animation=new RotateAnimation(0,360f,Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(3000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(10);
        mBinding.imgLoading.setAnimation(animation);
        animation.startNow();

        mCache=ACache.get(getContext());
        mEverydayModel=new EverydayModel();

        mBannerImages=(ArrayList<String>)mCache.getAsObject(Constants.BANNER_PIC);
        headerItemEverydayBinding= DataBindingUtil.inflate(LayoutInflater.from(getContext())
        ,R.layout.header_item_everyday,null,false);

        initLocalSetting();
        initRecyclerView();

       mInPressed=true;
        loadData();

    }

    @Override
    protected void loadData(){
        Log.e("xys","loadData()");
        if(headerItemEverydayBinding!=null&&
                headerItemEverydayBinding.banner!=null){
            headerItemEverydayBinding.banner.startAutoPlay();
            headerItemEverydayBinding.banner.setDelayTime(4000);
        }
        if(!mIsVisivle||!mInPressed){
            Log.e("xys","return");
            return;
        }
        String date=SPUtils.getString("everyday_data","2016-11-16");
        if(!date.equals(TimeUtil.getData())){
            if(TimeUtil.isRightTime()){
                isOldDayRequset=false;
                mEverydayModel.setData(getDate().get(0),getDate().get(1),getDate().get(2));
                showRotaLoading(true);
                loadBannerImage();
                showContentData();
            }
            else{
                ArrayList<String> lastTime = TimeUtil.getLastTime(getDate().get(0), getDate().get(1), getDate().get(2));
                mEverydayModel.setData(lastTime.get(0), lastTime.get(1), lastTime.get(2));
                year = lastTime.get(0);
                month = lastTime.get(1);
                day = lastTime.get(2);
                Toast.makeText(getContext(),"小于"+month+" "+getDate().get(1),Toast.LENGTH_SHORT).show();
                isOldDayRequset = true;// 是昨天
                getCacheData();
            }
        }
        else{
            isOldDayRequset=false;
            getCacheData();
        }
    }

    private void showContentData(){
        Log.e("xys","showContentdata()");
        mEverydayModel.showRecyclerViewData(new RequestImg() {
            @Override
            public void loadSuccess(Object object) {
                if(mLists!=null){
                    mLists.clear();
                }
                mLists=(ArrayList<List<AndroidBean>>)object;
                Log.e("xys","adapter"+object.toString());
                if(mLists.size()>0&&mLists.get(0).size()>0){
                    setAdapter(mLists);
                }
                else{
                    requestForData();
                }
            }

            @Override
            public void loadFailed() {
                Log.e("xys","fail");
                Toast.makeText(MyApplication.getInstance(),"adapter false",Toast.LENGTH_SHORT).show();
                if(mLists!=null&&mLists.size()>0){
                    return;
                }
                showError();
            }

            @Override
            public void addSubscription(Subscription subscription) {
                EverydayFragment.this.addSubscription(subscription);
            }
        });
    }

    private void requestForData(){
      mLists=(ArrayList<List<AndroidBean>>)( mCache.getAsObject(Constants.EVERYDAY_CONTENT));
        if(mLists!=null&&mLists.size()>0){
            setAdapter(mLists);
        }
        else{
            ArrayList<String> list=TimeUtil.getLastTime(year,month,day);
            mEverydayModel.setData(list.get(0),list.get(1),list.get(2));
            year=list.get(0);
            month=list.get(1);
            day=list.get(2);
            showContentData();
        }
    }

    private void getCacheData(){
        Log.e("xys","getCache()");
        if(!isFirst){
            return ;
        }
        if(mBannerImages!=null&&mBannerImages.size()>0){
            headerItemEverydayBinding.banner.setImages(mBannerImages)
                    .setImageLoader(new GridImageLoader()).start();
        }
        else{
            loadBannerImage();
        }

        mLists = (ArrayList<List<AndroidBean>>) mCache.getAsObject(Constants.EVERYDAY_CONTENT);
        if (mLists != null && mLists.size() > 0) {
            Log.e("xys","setAdapter()");
            setAdapter(mLists);
        } else {
            Log.e("xys","else()");
            showRotaLoading(true);
            showContentData();
        }
    }

    private void setAdapter(ArrayList<List<AndroidBean>> list){
        Log.e("xys",list.toString());
        showRotaLoading(false);
        Toast.makeText(getContext(),(mBinding.xrvEveryday.getVisibility()==View.VISIBLE)+"",Toast.LENGTH_SHORT).show();
        if(mEverydayAdapter==null){
            mEverydayAdapter = new EverydayAdapter();
        }
        mEverydayAdapter.clear();
        mEverydayAdapter.addAll(list);

        mCache.remove(Constants.EVERYDAY_CONTENT);
       // mCache.put(Constants.EVERYDAY_CONTENT,list,259200);
        if(isOldDayRequset){
            ArrayList<String> list1=TimeUtil.getLastTime(getDate().get(0),getDate().get(1),getDate().get(2));
            SPUtils.putString("everyday_data",list1.get(0)+"-"+list1.get(1)+"-"
            +list1.get(0));
        }
        else{
            SPUtils.putString("everyday_data",TimeUtil.getData());
        }
        isFirst=false;
        mBinding.xrvEveryday.setAdapter(mEverydayAdapter);

        mEverydayAdapter.notifyDataSetChanged();
    }

    private void showRotaLoading(boolean isLoading){
        if(isLoading){
            mBinding.lineLoading.setVisibility(View.VISIBLE);
            mBinding.xrvEveryday.setVisibility(View.GONE);
            animation.startNow();
        }
        else{
            mBinding.lineLoading.setVisibility(View.GONE);
            mBinding.xrvEveryday.setVisibility(View.VISIBLE);
            animation.cancel();
        }
    }

    private void initLocalSetting(){
        mEverydayModel.setData(getDate().get(0),getDate().get(1),getDate().get(2));
        headerItemEverydayBinding.include.day.setText(getDate().get(2).indexOf("0")==0
        ?getDate().get(2).replace("0",""):getDate().get(2));
        headerItemEverydayBinding.include.fm.setOnClickListener(new PerfectListener() {
            @Override
            public void onDoubleClick(View v) {
                Toast.makeText(getContext(),"dianji",Toast.LENGTH_SHORT).show();
            }
        });
        headerItemEverydayBinding.include.movie.setOnClickListener(new PerfectListener() {
            @Override
            public void onDoubleClick(View v) {
                RxBus.getInstance().post(RxConstants.JUMP_TO_ONE,new RxBusMessage());
            }
        });
    }

   private void initRecyclerView(){
        mBinding.xrvEveryday.setPullRefreshEnabled(false);
       mBinding.xrvEveryday.setLoadingMoreEnabled(false);
       if(mHeaderView==null){
           mHeaderView=headerItemEverydayBinding.getRoot();
            mBinding.xrvEveryday.addHeaderView(mHeaderView);
       }
       if(mFooterView==null){
           footerBinding=DataBindingUtil.inflate(LayoutInflater.from(getContext())
           ,R.layout.item_everyday_footer,null,false);
           mFooterView=footerBinding.getRoot();
           mBinding.xrvEveryday.addFooterView(mFooterView,true);
           mBinding.xrvEveryday.noMoreLoading();
       }
       mBinding.xrvEveryday.setLayoutManager(new LinearLayoutManager(getContext()));
       mBinding.xrvEveryday.setItemAnimator(new DefaultItemAnimator());
   }

   private void loadBannerImage(){
        mEverydayModel.showBanncePage(new RequestImg() {
            @Override
            public void loadSuccess(Object object) {
                if(mBannerImages==null){
                    mBannerImages=new ArrayList<String>();
                }else{
                    mBannerImages.clear();
                }
                FrontPageBean bean=(FrontPageBean)object;
                if(bean!=null&&bean.getResult()!=null&&bean.getResult().getFocus()!=null
                        &&bean.getResult().getFocus().getResult()!=null){
                    final List<FrontPageBean.ResultBeanXXXXXXXXXXXXXX.FocusBean.ResultBeanX>
                            result=bean.getResult().getFocus().getResult();
                    if(result!=null&&result.size()>0){
                        for(int i=0;i<result.size();i++){
                            mBannerImages.add(result.get(i).getRandpic());
                        }
                        headerItemEverydayBinding.banner.setImages(mBannerImages)
                                .setImageLoader(new GridImageLoader()).start();
                    }
                }
                mCache.remove(Constants.BANNER_PIC);
               mCache.put(Constants.BANNER_PIC,mBannerImages,30000);
            }

            @Override
            public void loadFailed() {

            }

            @Override
            public void addSubscription(Subscription subscription) {
                EverydayFragment.this.addSubscription(subscription);
            }
        });
   }

    private ArrayList<String> getDate(){
        String date= TimeUtil.getData();
        String[] str=date.split("-");
        ArrayList<String> list=new ArrayList<>();
        list.add(str[0]);
        list.add(str[1]);
        list.add(str[2]);
        return list;
    }

    @Override
    public void onResume(){
        super.onResume();
        mBinding.xrvEveryday.setFocusable(false);
        Glide.with(getActivity()).resumeRequests();
    }

    @Override
    public void onPause(){
        super.onPause();
        Glide.with(getActivity()).pauseRequests();
    }

    @Override
    protected void onInVisible(){
        if(headerItemEverydayBinding!=null&&
                headerItemEverydayBinding.banner!=null){
            headerItemEverydayBinding.banner.startAutoPlay();
        }
    }

    @Override
    protected void onRefresh(){
        showContentData();
        showRotaLoading(true);
        loadData();
    }
}
