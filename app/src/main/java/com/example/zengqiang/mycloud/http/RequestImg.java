package com.example.zengqiang.mycloud.http;

import rx.Subscription;

/**
 * Created by zengqiang on 2017/8/28.
 */

public interface RequestImg {
    void loadSuccess(Object object);

    void loadFailed();

    void addSubscription(Subscription subscription);
}
