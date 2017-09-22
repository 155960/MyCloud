package com.example.zengqiang.mycloud.utils;

import android.util.Log;

/**
 * Created by zengqiang on 2017/8/20.
 */

public class DebugUtils {
    public static final String TAG="zengqiang";
    public static final boolean DEBUG=true;

    public static  void debug(String tag,String error){
        if(DEBUG){
            Log.d(tag,error);
        }
    }

    public static void error(String tag,String error){
        if(DEBUG){
            Log.e(tag,error);
        }
    }


    public static void error(String error){
        if(DEBUG){
            Log.e(TAG,error);
        }
    }
}
