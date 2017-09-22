package com.example.zengqiang.mycloud.ui.gank;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.example.zengqiang.mycloud.R;
import com.example.zengqiang.mycloud.base.BaseFragment;
import com.example.zengqiang.mycloud.databinding.FragmentGankBinding;
import com.example.zengqiang.mycloud.http.rx.RxBus;
import com.example.zengqiang.mycloud.http.rx.RxConstants;
import com.example.zengqiang.mycloud.ui.gank.child.AndroidFragment;
import com.example.zengqiang.mycloud.ui.gank.child.CustomFragment;
import com.example.zengqiang.mycloud.ui.gank.child.EverydayFragment;
import com.example.zengqiang.mycloud.ui.gank.child.WelfareFragment;
import com.example.zengqiang.mycloud.view.MyFragmentAdapter;

import java.util.ArrayList;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by zengqiang on 2017/8/22.
 */

public class GankFragment extends BaseFragment<FragmentGankBinding>{

    private ArrayList<String> mTitleList=new ArrayList<>(4);
    private ArrayList<Fragment> mFragmentList=new ArrayList<>(4);

    @Override
    public void onActivityCreated(Bundle save){
        super.onActivityCreated(save);
        showLoading();
        initFragmentList();

        MyFragmentAdapter myFragmentAdapter=new MyFragmentAdapter(getChildFragmentManager(),mFragmentList,mTitleList);
        mBinding.vpGank.setAdapter(myFragmentAdapter);
        mBinding.vpGank.setOffscreenPageLimit(3);
        myFragmentAdapter.notifyDataSetChanged();
        mBinding.tabGank.setTabMode(TabLayout.MODE_FIXED);
        mBinding.tabGank.setupWithViewPager(mBinding.vpGank);
        showContentView();
        initRxBus();
    }

    private void initFragmentList(){
        mTitleList.add("每日推荐");
        mTitleList.add("福利");
        mTitleList.add("干货推荐");
       mTitleList.add("大安卓");
        mFragmentList.add(new EverydayFragment());
        mFragmentList.add(new WelfareFragment());
        mFragmentList.add(new CustomFragment());
        mFragmentList.add(new AndroidFragment());

    }

    @Override
    public int setContent() {
        return R.layout.fragment_gank;
    }

    private void initRxBus(){
        Subscription subscription= RxBus.getInstance().toObservable(RxConstants.JUMP_TYPE,Integer.class)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        if(integer==0){
                            mBinding.vpGank.setCurrentItem(3);
                        }
                        else if(integer==1){
                            mBinding.vpGank.setCurrentItem(1);
                        }
                        else if(integer==2){
                            mBinding.vpGank.setCurrentItem(2);
                        }
                    }
                });
    }
}
