package com.example.zengqiang.mycloud.model;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.http.HttpUtils;
import com.example.zengqiang.mycloud.app.Constants;
import com.example.zengqiang.mycloud.app.ConstantsImageUrl;
import com.example.zengqiang.mycloud.app.MyApplication;
import com.example.zengqiang.mycloud.bean.AndroidBean;
import com.example.zengqiang.mycloud.bean.FrontPageBean;
import com.example.zengqiang.mycloud.bean.GankIoBean;
import com.example.zengqiang.mycloud.http.HttpClient;
import com.example.zengqiang.mycloud.http.RequestImg;
import com.example.zengqiang.mycloud.utils.SPUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zengqiang on 2017/8/28.
 */

public class EverydayModel {
    private String year="2016";
    private String month="11";
    private String day="24";

    private static final String HOME_ONE="home_one";
    private static final String HOME_TWO="home_two";
    private static final String HOME_SIX="home_six";

    public void setData(String year,String month,String day){
        this.year=year;
        this.month=month;
        this.day=day;
    }

    public EverydayModel(){

    }

    public EverydayModel(String year,String month,String day){
        this.month=month;
        this.year=year;
        this.day=day;
    }


    public void showBanncePage(final RequestImg listener){
        Toast.makeText(MyApplication.getInstance(),"xxx",Toast.LENGTH_SHORT).show();
        Subscription subscription= HttpClient.Build.getTingService().getFrontPage()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())

                .subscribe(new Observer<FrontPageBean>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(MyApplication.getInstance(),"complete",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("xys","fail"+e.toString());
                        Toast.makeText(MyApplication.getInstance(),"fail"+e.toString(),Toast.LENGTH_SHORT).show();
                        listener.loadFailed();
                    }

                    @Override
                    public void onNext(FrontPageBean frontPageBean) {
                        if(frontPageBean==null){
                            Toast.makeText(MyApplication.getInstance(),"jjj",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MyApplication.getInstance(),"kkk",Toast.LENGTH_SHORT).show();
                        }
                        listener.loadSuccess(frontPageBean);
                    }
                });
        listener.addSubscription(subscription);
    }

    public void showRecyclerViewData(final RequestImg listener){
        SPUtils.putString(HOME_ONE,"");
        SPUtils.putString(HOME_TWO,"");
        SPUtils.putString(HOME_SIX,"");
        Func1<GankIoBean, rx.Observable<List<List<AndroidBean>>>> func1=
                new Func1<GankIoBean, rx.Observable<List<List<AndroidBean>>>>() {
            @Override
            public rx.Observable<List<List<AndroidBean>>> call(GankIoBean gankIoBean) {
                List<List<AndroidBean>> lists=new ArrayList<>();
                GankIoBean.ResultsBean resultsBean=gankIoBean.getResults();
                if(resultsBean.getAndroid()!=null&&resultsBean.getAndroid().size()>0){
                    addUrlList(lists,resultsBean.getAndroid(),"Android");
                }
                if(resultsBean.getWelfare()!=null&&resultsBean.getWelfare().size()>0){
                    addUrlList(lists,resultsBean.getWelfare(),"福利");
                }
                if(resultsBean.getiOS()!=null&&resultsBean.getiOS().size()>0){
                    addUrlList(lists,resultsBean.getiOS(),"IOS");
                }
                if(resultsBean.getRestMovie()!=null&&resultsBean.getRestMovie().size()>0){
                    addUrlList(lists,resultsBean.getRestMovie(),"休息视频");
                }
                if(resultsBean.getResource()!=null&&resultsBean.getResource().size()>0){
                    addUrlList(lists,resultsBean.getResource(),"拓展");
                }
                if(resultsBean.getRecommend()!=null&&resultsBean.getRecommend().size()>0){
                    addUrlList(lists,resultsBean.getRecommend(),"瞎推荐");
                }
                if(resultsBean.getFront()!=null&&resultsBean.getFront().size()>0){
                    addUrlList(lists,resultsBean.getFront(),"前端");
                }
                if(resultsBean.getApp()!=null&&resultsBean.getApp().size()>0){
                    addUrlList(lists,resultsBean.getApp(),"App");
                }
                return rx.Observable.just(lists);
            }
        };
        Observer<List<List<AndroidBean>>> observer=new Observer<List<List<AndroidBean>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.loadFailed();
            }

            @Override
            public void onNext(List<List<AndroidBean>> lists) {
                listener.loadSuccess(lists);
            }
        };
        Subscription subscription=HttpClient.Build.getGankIoService().getGankIoDay(year,month,day)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(func1)
                .subscribe(observer);
        listener.addSubscription(subscription);
    }

    private void addUrlList(List<List<AndroidBean>> list,List<AndroidBean> arrayList,String type){
        AndroidBean bean=new AndroidBean();
        ArrayList<AndroidBean> androidBeen=new ArrayList<>();
        bean.setType_title(type);
        androidBeen.add(bean);
        list.add(androidBeen);
        int arraySize=arrayList.size();
        if(arraySize>0&&arraySize<4){
            list.add(addUrlList(arrayList));
        }
        else if(arraySize>=4){
            ArrayList<AndroidBean> list1=new ArrayList<>();
            ArrayList<AndroidBean> list2=new ArrayList<>();
            for(int i=0;i<arraySize;i++){
                if(i<4){
                    list1.add(getAndroidBean(arrayList,i,arraySize));
                }
                else if(i<6){
                    list2.add(getAndroidBean(arrayList,i,arraySize));
                }
            }
            list.add(list1);
            list.add(list2);
        }
    }

    private AndroidBean getAndroidBean(List<AndroidBean> arrayList, int i, int androidSize) {

        AndroidBean androidBean = new AndroidBean();
        // 标题
        androidBean.setDesc(arrayList.get(i).getDesc());
        // 类型
        androidBean.setType(arrayList.get(i).getType());
        // 跳转链接
        androidBean.setUrl(arrayList.get(i).getUrl());
        // 随机图的url
        if (i < 3) {
            androidBean.setImage_url(ConstantsImageUrl.HOME_SIX_URLS[getRandom(3)]);//三小图
        } else if (androidSize == 4) {
            androidBean.setImage_url(ConstantsImageUrl.HOME_ONE_URLS[getRandom(1)]);//一图
        } else if (androidSize == 5) {
            androidBean.setImage_url(ConstantsImageUrl.HOME_TWO_URLS[getRandom(2)]);//两图
        } else if (androidSize >= 6) {
            androidBean.setImage_url(ConstantsImageUrl.HOME_SIX_URLS[getRandom(3)]);//三小图
        }
        return androidBean;
    }

    private ArrayList<AndroidBean> addUrlList(List<AndroidBean> list){
        ArrayList<AndroidBean> temp=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            AndroidBean bean=new AndroidBean();
            bean.setDesc(list.get(i).getDesc());
            bean.setType(list.get(i).getType());
            bean.setUrl(list.get(i).getUrl());
            if(list.size()==1){
                bean.setImage_url(ConstantsImageUrl.HOME_ONE_URLS[getRandom(1)]);
            }
            else if(list.size()==2){
                bean.setImage_url(ConstantsImageUrl.HOME_TWO_URLS[getRandom(2)]);
            }
            else if(list.size()==3){
                bean.setImage_url(ConstantsImageUrl.HOME_SIX_URLS[getRandom(3)]);
            }
            temp.add(bean);
        }
        return temp;
    }

    private int getRandom(int type){
        String where=null;
        int urlLenght=0;
        if(type==1){
            where=HOME_ONE;
            urlLenght= ConstantsImageUrl.HOME_ONE_URLS.length;
        }
        else if(type==2){
            where=HOME_TWO;
            urlLenght=ConstantsImageUrl.HOME_TWO_URLS.length;
        }
        else if(type==3){
            where=HOME_SIX;
            urlLenght=ConstantsImageUrl.HOME_SIX_URLS.length;
        }
        String home= SPUtils.getString(where,"");
        if(!TextUtils.isEmpty(home)){
            String[] spilt=home.split(",");
            Random random=new Random();
            for(int j=0;j<urlLenght;j++){
                int randomInt=random.nextInt(urlLenght);
                boolean isUse=false;
                for(String str:spilt){
                    if(!TextUtils.isEmpty(str)&&str.equals(randomInt+"")){
                        isUse=true;
                        break;
                    }
                }
                if(isUse){
                    StringBuilder builder=new StringBuilder(home);
                    builder.insert(0,randomInt+",");
                    SPUtils.putString(where,builder.toString());
                    return randomInt;
                }
            }

        }
        else{
            int randomInt=new Random().nextInt(urlLenght);
            SPUtils.putString(where,randomInt+",");
            return randomInt;
        }
        return 0;
    }
}
