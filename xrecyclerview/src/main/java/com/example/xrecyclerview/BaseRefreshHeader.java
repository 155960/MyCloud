package com.example.xrecyclerview;

/**
 * Created by zengqiang on 2017/8/24.
 */

interface BaseRefreshHeader {
    int STATE_NORMAL=0;
    int STATE_RELEASE_TO_REFRESH=1;
    int STATE_REFRESHING=2;
    int STATE_DONE=3;

    void onMove(float delta);
    boolean releaseAction();
    void refreshComplete();
    int getVisibleHeight();

}
