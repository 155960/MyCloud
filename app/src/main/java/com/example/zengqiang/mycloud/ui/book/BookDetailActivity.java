package com.example.zengqiang.mycloud.ui.book;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.zengqiang.mycloud.R;
import com.example.zengqiang.mycloud.base.BaseHeaderActivity;
import com.example.zengqiang.mycloud.bean.book.BookBean;
import com.example.zengqiang.mycloud.bean.book.BookDetailBean;
import com.example.zengqiang.mycloud.databinding.ActivityBookDetailBinding;
import com.example.zengqiang.mycloud.databinding.BookHeaderDetailBinding;
import com.example.zengqiang.mycloud.http.HttpClient;
import com.example.zengqiang.mycloud.utils.CommonUtils;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BookDetailActivity extends BaseHeaderActivity<BookHeaderDetailBinding,ActivityBookDetailBinding> {

    private static final String EXTRA_PARAM="bookBean";
    private BookBean bookBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent()!=null){
            bookBean=(BookBean) getIntent().getSerializableExtra(EXTRA_PARAM);
        }
        setContentView(R.layout.activity_book_detail);


        setSubtext("作者：" + bookBean.getAuthor());
        setTitle(bookBean.getTitle());
        bindingHeader.setBooksBean(bookBean);
        loadBookDetail();
    }

    public void loadBookDetail(){
        Subscription subscription= HttpClient.Build.getDouBanService().getBookDetail(bookBean.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookDetailBean>() {
                    @Override
                    public void onCompleted() {
                        showContent();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError();
                    }

                    @Override
                    public void onNext(BookDetailBean bookDetailBean) {
                        bindingContent.setBean(bookDetailBean);

                    }
                });
    }

    @Override
    protected String setHeaderImageUrl() {
        if(bookBean==null){
            return "";
        }
        return bookBean.getImages().getMedium();
    }

    @Override
    protected ImageView setHeaderImageView() {
        return bindingHeader.imgItemBg;
    }

    @Override
    public int setHeaderLayout() {
        return R.layout.book_header_detail;
    }

    @Override
    protected void onRefresh(){
        loadBookDetail();
    }

    public static void startActivity(Activity activity, BookBean bookBean, ImageView imageView){
        Intent intent=new Intent(activity,BookDetailActivity.class);
        intent.putExtra(EXTRA_PARAM,bookBean);
       ActivityOptionsCompat compat= ActivityOptionsCompat.makeSceneTransitionAnimation
                (activity,imageView, CommonUtils.getString(R.string.transition_book_img));
        ActivityCompat.startActivity(activity,intent,compat.toBundle());
    }
}
