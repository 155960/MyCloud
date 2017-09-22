package com.example.zengqiang.mycloud.app;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.example.http.HttpUtils;
import com.example.zengqiang.mycloud.utils.DebugUtils;

/**
 * Created by zengqiang on 2017/8/20.
 */

public class MyApplication extends Application {
    private static MyApplication myApplication;

    public static MyApplication getInstance(){
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication=this;
        HttpUtils.getInstance().init(this, DebugUtils.DEBUG);

        initTextSize();
    }

    private void initTextSize(){
        Resources res=getResources();
        Configuration configuration=new Configuration();
        configuration.setToDefaults();
        res.updateConfiguration(configuration,res.getDisplayMetrics());
    }
}
