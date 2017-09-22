package com.example.zengqiang.mycloud.ui.book;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.zengqiang.mycloud.MainActivity;
import com.example.zengqiang.mycloud.R;
import com.example.zengqiang.mycloud.adapter.BookAdapter;
import com.example.zengqiang.mycloud.app.MyApplication;
import com.example.zengqiang.mycloud.base.BaseFragment;
import com.example.zengqiang.mycloud.bean.book.BookBean;
import com.example.zengqiang.mycloud.bean.book.BooksBean;
import com.example.zengqiang.mycloud.databinding.BookCustomFragmentBinding;
import com.example.zengqiang.mycloud.http.HttpClient;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zengqiang on 2017/9/14.
 */

public class BookCustomFragment extends BaseFragment<BookCustomFragmentBinding> {

    private static String TYPE="综合";
    private final int COUNT=18;
    private int mStart=0;
    private String type;
    private MainActivity mainActivity;
    private BookAdapter mAdapter;
    private boolean isInPrepare=false;
    private GridLayoutManager layoutManager;
    private boolean isFirst=true;

    public static BookCustomFragment newInstance(String type){
        BookCustomFragment fragment=new BookCustomFragment();
        Bundle args=new Bundle();
        args.putString(TYPE,type);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity=(MainActivity)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            type=getArguments().getString(TYPE);
        }

    }

    @Override
    public void onActivityCreated(Bundle save) {
        super.onActivityCreated(save);
        mBinding.bookCustomRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mStart=0;
                loadCustomData();
            }
        });
        layoutManager=new GridLayoutManager(getActivity(),3);
        mBinding.bookCustomRecy.setLayoutManager(layoutManager);
        scrollRecy();
        isInPrepare=true;
        loadData();

    }

    public void scrollRecy(){
        mBinding.bookCustomRecy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    lastItem=layoutManager.findLastVisibleItemPosition();
                    if(mAdapter==null){
                        return;
                    }

                    if(lastItem+1==mAdapter.getItemCount()&&mAdapter.getState()!=BookAdapter.LOAD_MORE){
                        mAdapter.setState(BookAdapter.LOAD_MORE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mStart+=COUNT;
                                loadCustomData();
                            }
                        },500);
                    }


                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastItem=layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    public void loadData(){
        if(!mIsVisivle||!isInPrepare||!isFirst){
            return;
        }
        mBinding.bookCustomRefresh.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadCustomData();
            }
        },500);

    }

    public void loadCustomData(){
        Subscription subscription= HttpClient.Build.getDouBanService().getBook(type,mStart,COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BooksBean>() {
                    @Override
                    public void onCompleted() {
                        showContentView();
                        if(mBinding.bookCustomRefresh.isRefreshing()){
                            mBinding.bookCustomRefresh.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showContentView();
                        if(mBinding.bookCustomRefresh.isRefreshing()) {
                            mBinding.bookCustomRefresh.setRefreshing(false);
                        }
                        if(mStart==0)
                            showError();
                    }

                    @Override
                    public void onNext(BooksBean bookBean) {
                        if(mStart==0){
                            if(bookBean!=null&&bookBean.getBooks()!=null&&
                                    bookBean.getBooks().size()>0){
                                if(mAdapter==null){
                                    mAdapter=new BookAdapter(bookBean.getBooks(),getActivity());
                                    mAdapter.notifyDataSetChanged();
                                }
                                mBinding.bookCustomRecy.setAdapter(mAdapter);

                            }
                            isFirst=false;
                        }
                        else{
                                mAdapter.addAll(bookBean.getBooks());
                                mAdapter.notifyDataSetChanged();

                        }
                        if(mAdapter!=null){
                            mAdapter.setState(BookAdapter.LOAD_PULL_TO);
                        }
                    }
                });
    }

    @Override
    public void onRefresh(){
        mBinding.bookCustomRefresh.setRefreshing(true);
        loadData();
    }

    @Override
    public int setContent() {
        return R.layout.book_custom_fragment;
    }
}
