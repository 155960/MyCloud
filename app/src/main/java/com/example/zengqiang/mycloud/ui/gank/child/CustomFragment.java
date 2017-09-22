package com.example.zengqiang.mycloud.ui.gank.child;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.example.http.HttpUtils;
import com.example.xrecyclerview.XRecyclerView;
import com.example.zengqiang.mycloud.R;
import com.example.zengqiang.mycloud.adapter.AndroidAdapter;
import com.example.zengqiang.mycloud.app.Constants;
import com.example.zengqiang.mycloud.base.BaseFragment;
import com.example.zengqiang.mycloud.bean.GankIoDataBean;
import com.example.zengqiang.mycloud.databinding.CustomFragmentBinding;
import com.example.zengqiang.mycloud.http.RequestImg;
import com.example.zengqiang.mycloud.http.cache.ACache;
import com.example.zengqiang.mycloud.model.GankOtherModel;
import com.example.zengqiang.mycloud.utils.SPUtils;

import rx.Subscription;

/**
 * Created by zengqiang on 2017/8/22.
 */

public class CustomFragment extends BaseFragment<CustomFragmentBinding> {

    private String TYPE="all";
    private String TAG="CustomFragment";

    private int page=1;

    private GankOtherModel model;
    private AndroidAdapter adapter;
    private GankIoDataBean gankIoDataBean;
    private ACache cache;
    private View headerView;

    private boolean mIsPrepared=false;
    private boolean isFirst=true;

    @Override
    public void onActivityCreated(Bundle save) {
        super.onActivityCreated(save);
        Log.e("qqq","进入");
        cache=ACache.get(getActivity());
        model=new GankOtherModel();
        adapter=new AndroidAdapter();
        mBinding.customFragmentRecycler.setAdapter(adapter);
        mBinding.customFragmentRecycler.clearHeader();
        mBinding.customFragmentRecycler.setPullRefreshEnabled(false);
        mBinding.customFragmentRecycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                page++;
                loadCustomData();
            }
        });
        mIsPrepared=true;
    }

    @Override
    public void loadData(){
        if(!mIsPrepared||!mIsVisivle||!isFirst){
            return ;
        }

        if(gankIoDataBean!=null&&gankIoDataBean.getResults()!=null
                &&gankIoDataBean.getResults().size()>0){
            showContentView();
            gankIoDataBean=(GankIoDataBean) cache.getAsObject(Constants.GANK_CUSTOM);

            setAdapter(gankIoDataBean);
        }
        else{
            loadCustomData();
        }
    }

    private void setAdapter(GankIoDataBean bean){
        showContentView();
        Log.e("qqq","91");
        if(headerView==null){
            headerView=View.inflate(getContext(),R.layout.custom_header,null);
            mBinding.customFragmentRecycler.addHeaderView(headerView);
        }
        initHeader(headerView);
        boolean isAll=SPUtils.getString("gank_cala","全部").equals("全部");
        adapter.clear();
        adapter.setAll(isAll);
        Log.e("qqq",bean.getResults().toString());
        adapter.addAll(bean.getResults());
        mBinding.customFragmentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.customFragmentRecycler.setAdapter(adapter);
        Log.e("qqq",adapter.getItemCount()+"");
        adapter.notifyDataSetChanged();
        isFirst=false;
    }

    private void loadCustomData(){
        model.setData(TYPE, page, HttpUtils.per_page_more);
        model.getGankIoData(new RequestImg() {
            @Override
            public void loadSuccess(Object object) {
                showContentView();
                GankIoDataBean gankIoDataBean = (GankIoDataBean) object;
                if (page == 1) {
                    if (gankIoDataBean != null && gankIoDataBean.getResults() != null && gankIoDataBean.getResults().size() > 0) {
                        setAdapter(gankIoDataBean);

                        cache.remove(Constants.GANK_CUSTOM);
                        // 缓存50分钟
                        cache.put(Constants.GANK_CUSTOM, gankIoDataBean, 30000);
                    }
                } else {
                    if (gankIoDataBean != null && gankIoDataBean.getResults() != null && gankIoDataBean.getResults().size() > 0) {
                        mBinding.customFragmentRecycler.refreshComplete();
                        adapter.addAll(gankIoDataBean.getResults());
                        adapter.notifyDataSetChanged();
                    } else {
                        mBinding.customFragmentRecycler.noMoreLoading();
                    }
                }
            }

            @Override
            public void loadFailed() {
                Log.e("xys","加载失败");
                showContentView();
                mBinding.customFragmentRecycler.refreshComplete();
                if (adapter.getItemCount() == 0) {
                    showError();
                }
                if (page > 1) {
                    page--;
                }
            }

            @Override
            public void addSubscription(Subscription subscription) {
                CustomFragment.this.addSubscription(subscription);
            }
        });
    }

    private void initHeader(View header){
        final TextView textView=header.findViewById(R.id.custom_type);
        String text= SPUtils.getString("gank_cala","全部");
        textView.setText(text);
        View view=header.findViewById(R.id.custom_choose);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new BottomSheet.Builder(getActivity(),R.style.BottomSheet_style).title("选择分类")
                       .sheet(R.menu.custom_sheet).listener(new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       switch (i) {
                           case R.id.gank_all:
                               if (isOtherType("全部")) {
                                   showLoading();
                                   loadCustomData();
                               }
                               break;
                           case R.id.gank_ios:
                               if (isOtherType("IOS")) {
                                   showLoading();
                                   loadCustomData();
                               }
                               break;
                           case R.id.gank_qian:
                               if (isOtherType("前端")) {
                                   changeContent(textView, "前端");
                               }
                               break;
                           case R.id.gank_app:
                               if (isOtherType("App")) {
                                   changeContent(textView, "App");
                               }
                               break;
                           case R.id.gank_movie:
                               if (isOtherType("休息视频")) {
                                   changeContent(textView, "休息视频");
                               }
                               break;
                           case R.id.gank_resouce:
                               if (isOtherType("拓展资源")) {
                                   changeContent(textView, "拓展资源");
                               }
                               break;
                       }
                   }
               }).show();

            }
        });
    }

    private boolean isOtherType(String selectType) {
        String clickText = SPUtils.getString("gank_cala", "全部");
        if (clickText.equals(selectType)) {
            Toast.makeText(getContext(),"当前已经是" + selectType + "分类",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            // 重置XRecyclerView状态，解决 如出现刷新到底无内容再切换其他类别后，无法上拉加载的情况
            mBinding.customFragmentRecycler.reset();
            return true;
        }
    }

    private void changeContent(TextView textView, String content) {
        textView.setText(content);
        TYPE = content;
        page = 1;
        adapter.clear();
        SPUtils.putString("gank_cala", content);
        showLoading();
        loadCustomData();
    }


    @Override
    public int setContent() {
        return R.layout.custom_fragment;
    }

    @Override
    public void onRefresh(){
        loadCustomData();
    }
}
