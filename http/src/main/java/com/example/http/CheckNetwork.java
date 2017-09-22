package com.example.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by zengqiang on 2017/8/28.
 */

public class CheckNetwork {
    public static boolean isNetWorkConnected(Context context){
        if(context!=null){
            ConnectivityManager manager=(ConnectivityManager)context.
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info=manager.getActiveNetworkInfo();
            return info!=null&&info.isConnected();

        }
        else{
            return false;
        }
    }
}
