package com.example.zengqiang.mycloud;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zengqiang.mycloud.app.ConstantsImageUrl;
import com.example.zengqiang.mycloud.databinding.ActivityMainBinding;
import com.example.zengqiang.mycloud.http.rx.RxBus;
import com.example.zengqiang.mycloud.http.rx.RxBusMessage;
import com.example.zengqiang.mycloud.http.rx.RxConstants;
import com.example.zengqiang.mycloud.ui.book.BookFragment;
import com.example.zengqiang.mycloud.ui.gank.GankFragment;
import com.example.zengqiang.mycloud.ui.one.OneFragment;
import com.example.zengqiang.mycloud.utils.CommonUtils;
import com.example.zengqiang.mycloud.utils.ImgLoadUtils;
import com.example.zengqiang.mycloud.view.MyFragmentAdapter;
import com.example.zengqiang.mycloud.view.statusbar.StatusBarUtils;

import java.util.ArrayList;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{
    private ActivityMainBinding activityMainBinding;

    private FrameLayout llTitleMenu;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private FloatingActionButton fab;

    private ImageView llTitleGank;
    private ImageView llTitleOne;
    private ImageView llTitleDou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        initId();
        initRxBus();
        initStatusBar();
        initFragment();
        initDrawerLayout();
        initListener();
    }

    public void initListener(){
        viewPager.addOnPageChangeListener(this);
        llTitleOne.setOnClickListener(this);
        llTitleDou.setOnClickListener(this);
        llTitleGank.setOnClickListener(this);
        llTitleMenu.setOnClickListener(this);
    }

    public void initDrawerLayout(){
        ImgLoadUtils.displayCircle((ImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_img
        ),ConstantsImageUrl.IC_AVATAR);
        activityMainBinding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.menu_main:
                        activityMainBinding.drawerLayout.closeDrawers();
                        break;
                    case R.id.menu_download:
                        activityMainBinding.drawerLayout.closeDrawers();
                        break;
                    case R.id.menu_back:
                        activityMainBinding.drawerLayout.closeDrawers();
                        break;
                    case R.id.menu_about:
                        activityMainBinding.drawerLayout.closeDrawers();
                        break;
                    case R.id.menu_git:
                        activityMainBinding.drawerLayout.closeDrawers();
                        break;
                    case R.id.menu_exit:
                        activityMainBinding.drawerLayout.closeDrawers();
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    private void initFragment(){
        ArrayList<Fragment> list=new ArrayList<>();
        list.add(new GankFragment());
        list.add(new OneFragment());
        list.add(new BookFragment());
        MyFragmentAdapter fragmentAdapter=new MyFragmentAdapter(getSupportFragmentManager(),list);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(2);
        activityMainBinding.include.titlebarDisco.setSelected(true);
        viewPager.setCurrentItem(0);

    }

    private void initStatusBar(){
        StatusBarUtils.setColorNoTranslucentForLayout(this,drawerLayout, CommonUtils.getColor(R.color.colorTheme));
    }

    private void initId(){
        llTitleMenu=activityMainBinding.include.llTitleMenu;
        llTitleDou=activityMainBinding.include.titlebarDou;
        llTitleGank=activityMainBinding.include.titlebarDisco;
        llTitleOne=activityMainBinding.include.titlebarMusic;

        toolbar=activityMainBinding.include.toolbar;
        fab=activityMainBinding.include.fab;
        viewPager=activityMainBinding.include.ivContent;

        drawerLayout=activityMainBinding.drawerLayout;
        navigationView=activityMainBinding.navView;
    }

    public void initRxBus(){
        RxBus.getInstance().toObservable(RxConstants.JUMP_TO_ONE, RxBusMessage.class)
                .subscribe(new Action1<RxBusMessage>() {
                    @Override
                    public void call(RxBusMessage rxBusMessage) {
                        activityMainBinding.include.ivContent.setCurrentItem(1);
                    }
                });
    }

    @Override
    public void onClick(View view) {
      //  Toast.makeText(MainActivity.this,"www",Toast.LENGTH_SHORT).show();
        switch(view.getId()){
            case R.id.ll_title_menu:
               // Toast.makeText(MainActivity.this,"www",Toast.LENGTH_SHORT).show();
                drawerLayout.openDrawer(Gravity.START);
                //activityMainBinding.drawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.titlebar_disco:
                if(activityMainBinding.include.ivContent.getCurrentItem()!=0){
                    activityMainBinding.include.ivContent.setCurrentItem(0);
                    activityMainBinding.include.titlebarDisco.setSelected(true);
                    llTitleDou.setSelected(false);
                    llTitleOne.setSelected(false);
                }

                break;
            case R.id.titlebar_music:
                if(viewPager.getCurrentItem()!=1){
                    viewPager.setCurrentItem(1);
                    llTitleGank.setSelected(false);
                    llTitleOne.setSelected(true);
                    llTitleDou.setSelected(false);
                }
                break;
            case R.id.titlebar_dou:
                if(viewPager.getCurrentItem()!=2){
                    viewPager.setCurrentItem(2);
                    llTitleGank.setSelected(false);
                    llTitleDou.setSelected(true);
                    llTitleOne.setSelected(false);
                }
                break;
            case R.id.nav_img:
                Toast.makeText(MainActivity.this,"头像",Toast.LENGTH_SHORT).show();
              //  drawerLayout.openDrawer(Gravity.START);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //Toast.makeText(MainActivity.this,"sss",Toast.LENGTH_SHORT).show();
        switch(position){
            case 0:
               // if(activityMainBinding.include.ivContent.getCurrentItem()!=0){
                    activityMainBinding.include.ivContent.setCurrentItem(0);
                    activityMainBinding.include.titlebarDisco.setSelected(true);
                    llTitleDou.setSelected(false);
                    llTitleOne.setSelected(false);
              //  }

                break;
            case 1:
               // if(viewPager.getCurrentItem()!=1){
                    viewPager.setCurrentItem(1);
                    llTitleGank.setSelected(false);
                    llTitleOne.setSelected(true);
                    llTitleDou.setSelected(false);
              //  }
                break;
            case 2:
              //  if(viewPager.getCurrentItem()!=2){
                    viewPager.setCurrentItem(2);
                    llTitleGank.setSelected(false);
                    llTitleDou.setSelected(true);
                    llTitleOne.setSelected(false);
              //  }
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(Gravity.START)){
            drawerLayout.closeDrawers();
        }
        else {
            super.onBackPressed();
        }
    }
}
