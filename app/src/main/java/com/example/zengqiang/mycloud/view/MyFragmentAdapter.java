package com.example.zengqiang.mycloud.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by zengqiang on 2017/8/22.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {

    private List<?> mFragmentList;
    private List<String> mTitleList;

    public MyFragmentAdapter(FragmentManager fm,List<?> mFragmentList){
        super(fm);
        this.mFragmentList=mFragmentList;
    }

    public MyFragmentAdapter(FragmentManager fm,List<?> frgmentList,List<String> titleList){
        super(fm);
        this.mFragmentList=frgmentList;
        this.mTitleList=titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public void destroyItem(ViewGroup viewGroup,int position,Object o){
        super.destroyItem(viewGroup,position,o);
    }

    @Override
    public CharSequence getPageTitle(int position){
        if(mTitleList!=null){
            return mTitleList.get(position);
        }
        else{
            return "";
        }
    }
}
