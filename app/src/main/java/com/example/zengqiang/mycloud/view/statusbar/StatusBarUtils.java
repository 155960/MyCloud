package com.example.zengqiang.mycloud.view.statusbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.view.WindowCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by zengqiang on 2017/8/21.
 */

public class StatusBarUtils {

    public static void setColorNoTranslucentForLayout(Activity activity, DrawerLayout drawerLayout, @ColorInt int color){
        setColorNoTranslucentForLayout(activity,drawerLayout,color,0);
    }

    public static void setColorNoTranslucentForLayout(Activity activity,DrawerLayout drawerLayout,int color,float alpha){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
           /* View decorView=activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);*/
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        else{
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        ViewGroup content=(ViewGroup) drawerLayout.getChildAt(0);
        if(content.getChildCount()>0&&content.getChildAt(0) instanceof StatusBarView){
            content.getChildAt(0).setBackgroundColor(caculateStatusColor(color,alpha));
        }
        else{
            StatusBarView statusBarView=createStatusBarView(activity,color);
            content.addView(statusBarView,0);
        }
        ViewGroup viewGroup=(ViewGroup)drawerLayout.getChildAt(1);
        viewGroup.setFitsSystemWindows(false);
        content.setFitsSystemWindows(false);
        drawerLayout.setFitsSystemWindows(false);
    }

    public static int getStatusBarHeight(Context context){
        int rsid=context.getResources().getIdentifier("status_bar_height","dimen","android");
        return context.getResources().getDimensionPixelSize(rsid);
    }

    private static StatusBarView createStatusBarView(Activity activity, @ColorInt int color, int alpha) {
        // 绘制一个和状态栏一样高的矩形
        StatusBarView statusBarView = new StatusBarView(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(caculateStatusColor(color, alpha));
        return statusBarView;
    }

    private static StatusBarView createStatusBarView(Activity activity,int color){
        StatusBarView statusBarView=new StatusBarView((activity));
        LinearLayout.LayoutParams l=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight(activity));
        statusBarView.setLayoutParams(l);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }

    private static int caculateStatusColor(int color,float alpha){
        float a=1-alpha/255f;
        int red=color>>16&0xff;
        int green=color>>8&0xff;
        int blue=color&0xff;
        red=(int)(red*a+0.5);
        green=(int)(green*a+0.5);
        blue=(int)(blue*a+0.5);
        return 0xff<<24|red<<16|green<<8|blue;

    }

    public static void setFullScreen(Activity activity){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window=activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

    }

    public static void setTranslucentImageHeader(Activity activity,int aphla,View need){
        setFullScreen(activity);
        ViewGroup deco=(ViewGroup) activity.getWindow().getDecorView();
        int count=deco.getChildCount();
        if(count>0&&deco.getChildAt(count-1) instanceof StatusBarView){
            deco.getChildAt(count-1).setBackgroundColor(Color.argb(aphla,0,0,0));
        }
        else{
            StatusBarView view=createStatusBarView(activity,aphla);
            deco.addView(view);
        }
        if (need != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) need.getLayoutParams();
            layoutParams.setMargins(0, getStatusBarHeight(activity), 0, 0);
        }
    }


    private static StatusBarView createTranslucentStatusBarView(Activity activity, int alpha) {
        // 绘制一个和状态栏一样高的矩形
        StatusBarView statusBarView = new StatusBarView(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        return statusBarView;
    }
}
