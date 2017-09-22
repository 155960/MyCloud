package com.example.zengqiang.mycloud.utils;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.provider.ContactsContract;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zengqiang.mycloud.R;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by zengqiang on 2017/8/23.
 */

public class ImgLoadUtils {
    private ImgLoadUtils imgLoadUtils;

    public static void displayGif(String url, ImageView imageView) {

        Glide.with(imageView.getContext()).load(url)
                .asBitmap()
                .placeholder(R.drawable.img_one_bi_one)
                .error(R.drawable.img_one_bi_one)
//                .skipMemoryCache(true) //跳过内存缓存
//                .crossFade(1000)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)// 缓存图片源文件（解决加载gif内存溢出问题）
//                .into(new GlideDrawableImageViewTarget(imageView, 1));
                .into(imageView);
    }

    public static void displayEspImage(String url, ImageView imageView, int type) {
        Glide.with(imageView.getContext())
                .load(url)
                .crossFade(500)
                .placeholder(getDefaultPic(type))
                .error(getDefaultPic(type))
                .into(imageView);
    }

    public ImgLoadUtils getInstance(){
        if(imgLoadUtils==null){
            imgLoadUtils=new ImgLoadUtils();
        }
        return imgLoadUtils;
    }

    public static void displayCircle(ImageView imageView,String imageUrl){
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .crossFade(500)
                .error(R.drawable.ic_avatar_default)
                .transform(new GlideCircleTransform(imageView.getContext()))
                .into(imageView);
    }

    public static void setDisplayRandom(int num,String url,ImageView view){
        Glide.with(view.getContext())
                .load(url)
                .placeholder(getMusicDefaultPic(num))
                .error(getMusicDefaultPic(num))
                .crossFade(1500)
                .into(view);
    }

    private static int getMusicDefaultPic(int imgNumber) {
        switch (imgNumber) {
            case 1:
                return R.drawable.img_two_bi_one;
            case 2:
                return R.drawable.img_four_bi_three;
            case 3:
                return R.drawable.img_one_bi_one;
        }
        return R.drawable.img_four_bi_three;
    }

    private static int getDefaultPic(int type){
        switch(type){
            case 0:
                return R.drawable.img_default_movie;
            case 1:
                return R.drawable.img_default_meizi;
            case 2:
                return R.drawable.img_default_book;
        }
        return R.drawable.img_default_meizi;
    }

    @BindingAdapter("android:showBookImg")
    public static void showBookImg(ImageView imageView,String url){
        Glide.with(imageView.getContext())
                .load(url)
                .crossFade(500)
                .placeholder(getDefaultPic(2))
                .error(getDefaultPic(2))
                .into(imageView);
    }

    @BindingAdapter("android:showMovieImg")
    public static void showMoiveImg(ImageView imageView,String url){
        Glide.with(imageView.getContext())
                .load(url)
                .crossFade(500)
                .placeholder(getDefaultPic(0))
                .error(getDefaultPic(0))
                .into(imageView);
    }

    private static void displayGaussian(Context context, String url, ImageView imageView) {
        // "23":模糊度；"4":图片缩放4倍后再进行模糊
        Glide.with(context)
                .load(url)
                .error(R.drawable.stackblur_default)
                .placeholder(R.drawable.stackblur_default)
                .crossFade(500)
                .bitmapTransform(new BlurTransformation(context, 23, 4))
                .into(imageView);
    }

    @BindingAdapter("android:showImgBg")
    public static void showImgBg(ImageView imageView,String url){
        displayGaussian(imageView.getContext(),url,imageView);
    }
}
