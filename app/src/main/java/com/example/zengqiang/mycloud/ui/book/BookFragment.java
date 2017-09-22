package com.example.zengqiang.mycloud.ui.book;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.example.zengqiang.mycloud.R;
import com.example.zengqiang.mycloud.base.BaseFragment;
import com.example.zengqiang.mycloud.databinding.BookFragmentBinding;
import com.example.zengqiang.mycloud.view.MyFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengqiang on 2017/8/22.
 */

public class BookFragment extends BaseFragment<BookFragmentBinding> {

    private MyFragmentAdapter adapter;
    private List<Fragment> fragments;
    private List<String> titles;

    @Override
    public void onActivityCreated(Bundle save) {
        super.onActivityCreated(save);
        showLoading();
        initFragments();
        adapter=new MyFragmentAdapter(getChildFragmentManager(),fragments,titles);
        mBinding.bookPager.setAdapter(adapter);
        mBinding.bookPager.setOffscreenPageLimit(2);
        mBinding.bookTab.setTabMode(TabLayout.MODE_FIXED);
        mBinding.bookTab.setupWithViewPager(mBinding.bookPager);
        showContentView();
    }

    @Override
    public int setContent() {
        return R.layout.book_fragment;
    }

    private void initFragments(){
        fragments=new ArrayList<>(3);
        titles=new ArrayList<>(3);
        fragments.add(BookCustomFragment.newInstance("文学"));
        fragments.add(BookCustomFragment.newInstance("文化"));
        fragments.add(BookCustomFragment.newInstance("生活"));
        titles.add("文学");
        titles.add("文化");
        titles.add("生活");
    }
}
