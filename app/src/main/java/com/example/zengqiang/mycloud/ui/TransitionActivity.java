package com.example.zengqiang.mycloud.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.zengqiang.mycloud.MainActivity;
import com.example.zengqiang.mycloud.R;
import com.example.zengqiang.mycloud.app.ConstantsImageUrl;
import com.example.zengqiang.mycloud.databinding.ActivityTransitionBinding;
import com.example.zengqiang.mycloud.utils.CommonUtils;

import java.util.Random;

public class TransitionActivity extends AppCompatActivity {
    private ActivityTransitionBinding transitionBinding;
    private boolean animationEnd;
    private boolean isIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transitionBinding= DataBindingUtil.setContentView(this,R.layout.activity_transition);
        int i=new Random().nextInt(ConstantsImageUrl.TRANSITION_URLS.length);
        transitionBinding.ivPic.setImageDrawable(CommonUtils.getDrawable(R.drawable.img_transition_default));
        Glide.with(this)
                .load(ConstantsImageUrl.TRANSITION_URLS[i])
                .placeholder(R.drawable.img_transition_default)
                .error(R.drawable.img_transition_default)
                .into(transitionBinding.img);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                transitionBinding.ivPic.setVisibility(View.GONE);
            }
        },1500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toMainActivity();
            }
        },3500);
        transitionBinding.jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionBinding.jump.setClickable(false);
                toMainActivity();
            }
        });
    }

    public void toMainActivity(){
        if(isIn){
            return;
        }
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.screen_in,R.anim.screen_out);
        finish();
        isIn=true;

    }
}
