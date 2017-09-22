package com.example.zengqiang.mycloud.utils;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.example.zengqiang.mycloud.app.MyApplication;

import java.util.Random;

/**
 * Created by zengqiang on 2017/8/21.
 */

public class CommonUtils {

    public static int randomColor(){
        Random random=new Random();
        int red=random.nextInt(150)+50;
        int green=random.nextInt(150)+50;
        int blue=random.nextInt(150)+50;
        return Color.rgb(red,green,blue);
    }

    public static String getString(int id){
        return getResources().getString(id);
    }

    public static int getColor(int resid) {
        return getResources().getColor(resid);
    }

    public static Resources getResources(){
        return MyApplication.getInstance().getResources();
    }

    public static float getDimens(int id){
        return getResources().getDimension(id);
    }

    public static Drawable getDrawable(int reid){
       //getResources().getDrawable(reid,getResources().newTheme());
        return getResources().getDrawable(reid);
    }
}
