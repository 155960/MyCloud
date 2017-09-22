package com.example.zengqiang.mycloud.model;

import com.example.zengqiang.mycloud.bean.GankIoDataBean;
import com.example.zengqiang.mycloud.http.HttpClient;
import com.example.zengqiang.mycloud.http.RequestImg;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zengqiang on 2017/9/20.
 */

public class GankOtherModel  {
    private String id;
    private int page;
    private int per_page;

    public void setData(String id,int page,int per_page){
        this.id=id;
        this.page=page;
        this.per_page=per_page;
    }

    public void getGankIoData(final RequestImg listener){
        Subscription subscription= HttpClient.Build.getGankIoService().getGankIoData(id,page,per_page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GankIoDataBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.loadFailed();
                    }

                    @Override
                    public void onNext(GankIoDataBean gankIoDataBean) {
                        listener.loadSuccess(gankIoDataBean);
                    }
                });
        listener.addSubscription(subscription);
    }
}
