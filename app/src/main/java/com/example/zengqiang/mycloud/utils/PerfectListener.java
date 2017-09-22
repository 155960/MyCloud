package com.example.zengqiang.mycloud.utils;

import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

/**
 * Created by zengqiang on 2017/8/22.
 */

public abstract class PerfectListener implements View.OnClickListener {

    public abstract void onDoubleClick(View v);
    long lastime=0;
    int id=-1;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        long current= Calendar.getInstance().getTimeInMillis();
        if(id!=view.getId()){
            lastime=current;
            id=view.getId();
            onDoubleClick(view);
            return;
        }
        if(current-lastime>1000){
            lastime=current;
            onDoubleClick(view);
        }
    }
}
