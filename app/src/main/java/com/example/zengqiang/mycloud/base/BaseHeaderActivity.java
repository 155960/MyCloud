package com.example.zengqiang.mycloud.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.zengqiang.mycloud.R;
import com.example.zengqiang.mycloud.databinding.BaseHeaderActivityBinding;
import com.example.zengqiang.mycloud.databinding.BaseHeaderTitleBinding;
import com.example.zengqiang.mycloud.utils.CommonUtils;
import com.example.zengqiang.mycloud.utils.PerfectListener;
import com.example.zengqiang.mycloud.view.MyNestScrollView;
import com.example.zengqiang.mycloud.view.statusbar.StatusBarUtils;

import jp.wasabeef.glide.transformations.BlurTransformation;

public abstract class BaseHeaderActivity<HV extends ViewDataBinding,SV extends ViewDataBinding> extends AppCompatActivity {

    protected BaseHeaderTitleBinding bindingTitle;
    protected HV bindingHeader;
    protected SV bindingContent;

    private int imageBgHeight;
    private int slideDistance;

    private View loading;
    private View refresh;

    private AnimationDrawable animationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutId){
        View ll=getLayoutInflater().inflate(R.layout.base_header_activity,null);
        bindingTitle= DataBindingUtil.inflate(getLayoutInflater(),R.layout.base_header_title,null,false);
        bindingHeader=DataBindingUtil.inflate(getLayoutInflater(),setHeaderLayout(),null,false);
        bindingContent=DataBindingUtil.inflate(getLayoutInflater(),layoutId,null,false);

        RelativeLayout titleHeader=ll.findViewById(R.id.base_title_container);
        titleHeader.addView(bindingTitle.getRoot());

        RelativeLayout header=ll.findViewById(R.id.base_header_container);
        header.addView(bindingHeader.getRoot());

        RelativeLayout content=ll.findViewById(R.id.base_container);
        content.addView(bindingContent.getRoot());
        getWindow().setContentView(ll);

        loading=getView(R.id.base_loading);
        refresh=getView(R.id.base_load_error);

        initSlideShape(setHeaderImageUrl(),setHeaderImageView());
        setToolbar();

        ImageView imageView=getView(R.id.base_yun);
        animationDrawable=(AnimationDrawable) imageView.getDrawable();
        if(!animationDrawable.isRunning()){
            animationDrawable.start();
        }
        refresh.setOnClickListener(new PerfectListener() {
            @Override
            public void onDoubleClick(View v) {
                showLoading();
                onRefresh();
            }
        });
        bindingContent.getRoot().setVisibility(View.GONE);
    }

    protected void showLoading(){
        if(loading.getVisibility()!=View.VISIBLE){
            loading.setVisibility(View.VISIBLE);
        }
        if(!animationDrawable.isRunning()){
            animationDrawable.start();
        }
        if(refresh.getVisibility()!=View.GONE){
            refresh.setVisibility(View.GONE);
        }
        if(bindingContent.getRoot().getVisibility()!=View.GONE){
            bindingContent.getRoot().setVisibility(View.GONE);
        }
    }

    protected void showError(){
        if(loading.getVisibility()!=View.GONE){
            loading.setVisibility(View.GONE);
        }
        if(animationDrawable.isRunning()){
            animationDrawable.stop();
        }
        if(refresh.getVisibility()!=View.VISIBLE){
            refresh.setVisibility(View.VISIBLE);
        }
        if(bindingContent.getRoot().getVisibility()!=View.GONE){
            bindingContent.getRoot().setVisibility(View.GONE);
        }
    }

    protected void showContent(){
        if(loading.getVisibility()!=View.GONE){
            loading.setVisibility(View.GONE);
        }
        if(animationDrawable.isRunning()){
            animationDrawable.stop();
        }
        if(refresh.getVisibility()!=View.GONE){
            refresh.setVisibility(View.GONE);
        }
        if(bindingContent.getRoot().getVisibility()!=View.VISIBLE){
            bindingContent.getRoot().setVisibility(View.VISIBLE);
        }
    }

    protected void onRefresh(){

    }

    private void setToolbar(){
        setSupportActionBar(bindingTitle.baseHeaderTitleToolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }
        bindingTitle.baseHeaderTitleToolbar.setTitleTextAppearance(this,R.style.Toolbar_Title);
        bindingTitle.baseHeaderTitleToolbar.setSubtitleTextAppearance(this,R.style.Toolbar_SubTitle);
        bindingTitle.baseHeaderTitleToolbar.inflateMenu(R.menu.base_toolbar);
        bindingTitle.baseHeaderTitleToolbar.setOverflowIcon(CommonUtils.getDrawable(R.drawable.actionbar_more));
        bindingTitle.baseHeaderTitleToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.base_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.base){
            Toast.makeText(this,"点击",Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    protected void setTitle(String text){
        bindingTitle.baseHeaderTitleToolbar.setTitle(text);
    }

    protected void setSubtext(String subtext){
        bindingTitle.baseHeaderTitleToolbar.setSubtitle(subtext);
    }

    protected void initSlideShape(String url, ImageView imageView){
        setImgHeader(url);

        int toolHeight=bindingTitle.baseHeaderTitleToolbar.getLayoutParams().height;
        int toolandstatusbarHeight=toolHeight+ StatusBarUtils.getStatusBarHeight(this);
        ViewGroup.LayoutParams layoutParams=bindingTitle.baseHeaderTitleImg.getLayoutParams();
        ViewGroup.MarginLayoutParams marginLayoutParams=(ViewGroup.MarginLayoutParams) bindingTitle.baseHeaderTitleImg.getLayoutParams();
        int marginTop=layoutParams.height-toolandstatusbarHeight;
        marginLayoutParams.setMargins(0,-marginTop,0,0);
        StatusBarUtils.setTranslucentImageHeader(this,0,bindingTitle.baseHeaderTitleToolbar);
        if(imageView!=null){
            ViewGroup.LayoutParams layoutParams1=imageView.getLayoutParams();
            imageBgHeight=layoutParams1.height;
        }

        initSlideDistance();
        initNestScrollListener();

    }

    private void initSlideDistance(){
        int height=(int)(CommonUtils.getDimens(R.dimen.base_title_height)+StatusBarUtils.getStatusBarHeight(this));
        slideDistance=imageBgHeight-height-(int)CommonUtils.getDimens(R.dimen.base_more_height);
    }

    private void initNestScrollListener(){
        ((MyNestScrollView)findViewById(R.id.base_nest_scroll)).setScrollInterface(new MyNestScrollView.ScrollInterface() {
            @Override
            public void onScroll(int l, int t, int oldL, int oldT) {
                slideChange(t);
            }
        });
    }

    private void slideChange(int y){

        float rate=(y*1.0f)/slideDistance;

        int alpha=(int)(rate*255);
        if(y<slideDistance){
            bindingTitle.baseHeaderTitleImg.setImageAlpha(alpha);
        }else{
            bindingTitle.baseHeaderTitleImg.setImageAlpha(255);
        }
    }

    private void setImgHeader(String url){

        Toast.makeText(this,"laod"+" "+url,Toast.LENGTH_SHORT).show();
        Glide.with(this)
                .load(url)
                .error(R.drawable.stackblur_default)
                .bitmapTransform(new BlurTransformation(this,23,4))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Toast.makeText(BaseHeaderActivity.this, "load fail", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        bindingTitle.baseHeaderTitleImg.setImageAlpha(0);

                        return false;
                    }
                }).into(bindingTitle.baseHeaderTitleImg);
    }

    protected <T extends View> T getView(int id){
        return (T)findViewById(id);
    }

    protected abstract String setHeaderImageUrl();

    protected abstract ImageView setHeaderImageView();

    public abstract int setHeaderLayout();
}
