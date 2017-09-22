package com.example.http;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zengqiang on 2017/8/20.
 */

public class HttpUtils {
    private static HttpUtils httpUtils;

    private Gson gson;
    private Gson mGson;
    private Object gankHttps;
    private Object douBanHttps;
    private Object dongTingHttps;
    private IpmlTokenGetListener listener;
    private boolean debug;
    private Context context;

    private static final String API_GANK="https://gank.io/api/";
    private static final String API_DOUBAN="Https://api.douban.com/";
    private static final String API_TING="https://tingapi.ting.baidu.com/v1/restserver/";

    public final int per_page=10;
    public static final int per_page_more=20;

    public static HttpUtils getInstance(){
        if(httpUtils==null){
            synchronized(HttpUtils.class){
                if(httpUtils==null){
                    httpUtils=new HttpUtils();
                }
            }
        }
        return httpUtils;
    }

    public void init(Context context,boolean debug){
        this.context=context;
        this.debug=debug;
        HttpHead.init(context);
    }

    public <T> T getGankIoServer(Class<T> a){
        if(gankHttps==null){
            synchronized (HttpUtils.class){
                if(gankHttps==null){
                    gankHttps=getBuilder(API_GANK).build().create(a);
                }
            }
        }
        return (T)gankHttps;
    }

    public <T> T getDouBanServer(Class<T> a){
        if(douBanHttps==null){
            synchronized (HttpUtils.class){
                if(douBanHttps==null){
                    douBanHttps=getBuilder(API_DOUBAN).build().create(a);
                }
            }
        }
        return (T)douBanHttps;
    }

    public <T> T getTingServer(Class<T> a){
        if(dongTingHttps==null){
            synchronized (HttpUtils.class){
                if(dongTingHttps==null){
                    dongTingHttps=getBuilder(API_TING).build().create(a);
                }
            }
        }
        return (T)dongTingHttps;
    }

    private Retrofit.Builder getBuilder(String url){
        Retrofit.Builder builder=new Retrofit.Builder();
        builder.client(getOkClient());
        builder.baseUrl(url);
        builder.addConverterFactory(new NullOnEmptyConverterFactory());
        builder.addConverterFactory(GsonConverterFactory.create(getGson()));
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        return builder;
    }
    private Gson getGson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.setLenient();
            builder.setFieldNamingStrategy(new AnnotateNaming());
            builder.serializeNulls();
            gson = builder.create();
        }
        return gson;
    }

    private static class AnnotateNaming implements FieldNamingStrategy {
        @Override
        public String translateName(Field field) {
            ParamNames a = field.getAnnotation(ParamNames.class);
            return a != null ? a.value() : FieldNamingPolicy.IDENTITY.translateName(field);
        }
    }


    public OkHttpClient getOkClient(){
        OkHttpClient client=getUnSafeOkHttp();
        return client;
    }

    public OkHttpClient getUnSafeOkHttp(){
        try {
        final TrustManager[] managers=new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        }
        };

            SSLContext sslContext=SSLContext.getInstance("TLS");
            sslContext.init(null,managers,new SecureRandom());
            SSLSocketFactory factory=sslContext.getSocketFactory();
            OkHttpClient.Builder builder=new OkHttpClient.Builder();
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.connectTimeout(10,TimeUnit.SECONDS);
            builder.writeTimeout(20, java.util.concurrent.TimeUnit.SECONDS);
            builder.addInterceptor(new HttpHeadInterceptor());
            builder.addInterceptor(getInterceptor());
            builder.sslSocketFactory(factory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
//                    Log.d("HttpUtils", "==come");
                    return true;
                }
            });
            return builder.build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    class HttpHeadInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            builder.addHeader("Accept", "application/json;versions=1");
            if (CheckNetwork.isNetWorkConnected(context)) {
                int maxAge = 60;
                builder.addHeader("Cache-Control", "public, max-age=" + maxAge);
            } else {
                int maxStale = 60 * 60 * 24 * 28;
                builder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
            }
            // 可添加token
//            if (listener != null) {
//                builder.addHeader("token", listener.getToken());
//            }
            // 如有需要，添加请求头
//            builder.addHeader("a", HttpHead.getHeader(request.method()));
            return chain.proceed(builder.build());
        }
    }

    private HttpLoggingInterceptor getInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (debug) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // 测试
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE); // 打包
        }
        return interceptor;
    }
}
