package com.example.zengqiang.mycloud.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zengqiang.mycloud.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by zengqiang on 2017/8/29.
 */

public class GridImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .placeholder(R.drawable.img_one_bi_one)
                .error(R.drawable.img_one_bi_one)
                .crossFade(1000)
                .into(imageView);
    }
}
