package com.example.zengqiang.mycloud.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.zengqiang.mycloud.app.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengqiang on 2017/9/2.
 */

public class SPUtils {
    private static final String CONFIG = "config";

    private static SharedPreferences getSharedpreference(String file){
        return MyApplication.getInstance().getSharedPreferences(file, Context.MODE_PRIVATE);
    }

    public static void putString(String key,String value){
        SharedPreferences.Editor editor=SPUtils.getSharedpreference(CONFIG).edit();
        editor.putString(key,value).apply();
    }

    public static String getString(String key,String def){
        SharedPreferences preferences=SPUtils.getSharedpreference(CONFIG);
       return preferences.getString(key,def);
    }

    public static void putBoolean(String key,boolean value){
        SharedPreferences.Editor editor=SPUtils.getSharedpreference(CONFIG).edit();
        editor.putBoolean(key,value);
    }

    public static boolean getBoolean(String key,boolean def){
        SharedPreferences preferences=SPUtils.getSharedpreference(CONFIG);
        return preferences.getBoolean(key,def);
    }

    public static void putInt(String key,int a){
        SharedPreferences.Editor editor=SPUtils.getSharedpreference(CONFIG).edit();
        editor.putInt(key,a);
    }

    public static int getInt(String key,int a){
        SharedPreferences preferences=SPUtils.getSharedpreference(CONFIG);
        return preferences.getInt(key,a);
    }

    public static void putFloat(String key,float a){
        SharedPreferences.Editor editor=SPUtils.getSharedpreference(CONFIG).edit();
        editor.putFloat(key,a);
    }

    public static void putLong(String key,long a){
        SharedPreferences.Editor editor=SPUtils.getSharedpreference(CONFIG).edit();
        editor.putLong(key,a);
    }

    public static float getFloat(String key,float a){
        SharedPreferences preferences=SPUtils.getSharedpreference(CONFIG);
        return preferences.getFloat(key,a);
    }
    public static long getLong(String key,long a){
        SharedPreferences preferences=SPUtils.getSharedpreference(CONFIG);
        return preferences.getLong(key,a);
    }

    public static ArrayList<String> getList(String key){
        int a=getInt(key+"size",0);
        ArrayList<String> list=new ArrayList<>();
        for(int i=0;i<a;i++){
            list.add(getString(key+i,null));
        }
        return list;
    }

    public static void putStrList(String key, List<String> list){
        if(list==null){
            return;
        }
        removeStrList(key);
        putInt(key+"size",list.size());
        for(int i=0;i<list.size();i++){
            putString(key+i,list.get(i));
        }
    }

    public static void removeStrList(String key){
        int size=getInt(key+"size",0);
        if(size==0){
            return ;
        }
        remove(key+"size");
        for(int i=0;i<size;i++){
            remove(key+i);
        }
    }

    public static void remove(String key){
        SharedPreferences.Editor editor=SPUtils.getSharedpreference(CONFIG).edit();
        editor.remove(key).apply();
    }
}
