package com.example.zengqiang.mycloud.http;

import com.example.http.HttpUtils;
import com.example.zengqiang.mycloud.bean.FrontPageBean;
import com.example.zengqiang.mycloud.bean.GankIoBean;
import com.example.zengqiang.mycloud.bean.GankIoDataBean;
import com.example.zengqiang.mycloud.bean.HotMoiveBean;
import com.example.zengqiang.mycloud.bean.book.BookBean;
import com.example.zengqiang.mycloud.bean.book.BookDetailBean;
import com.example.zengqiang.mycloud.bean.book.BooksBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zengqiang on 2017/8/28.
 */

public interface HttpClient {
    class Build{
        public static HttpClient getDouBanService(){
            return HttpUtils.getInstance().getDouBanServer(HttpClient.class);
        }

        public static HttpClient getTingService(){
            return HttpUtils.getInstance().getTingServer(HttpClient.class);
        }

        public static HttpClient getGankIoService(){
            return HttpUtils.getInstance().getGankIoServer(HttpClient.class);
        }
    }

    @GET("data/{type}/{pre_page}/{page}")
    Observable<GankIoDataBean> getGankIoData(@Path("type") String id, @Path("page") int page, @Path("pre_page") int pre_page);


    @GET("v2/book/search")
    Observable<BooksBean> getBook(@Query("tag") String tag, @Query("start") int start, @Query("count") int count);

    @GET("ting?from=android&version=5.8.1.0&channel=ppzs&operator=3&method=baidu.ting.plaza.index&cuid=89CF1E1A06826F9AB95A34DC0F6AAA14")
    Observable<FrontPageBean> getFrontPage();

    @GET("day/{year}/{month}/{day}")
    Observable<GankIoBean> getGankIoDay(@Path("year") String year,@Path("month") String month
    ,@Path("day") String day);

    @GET("v2/movie/in_theaters")
    public Observable<HotMoiveBean> getHotMoive();

    @GET("v2/book/{id}")
    Observable<BookDetailBean> getBookDetail(@Path("id") String id);

}
