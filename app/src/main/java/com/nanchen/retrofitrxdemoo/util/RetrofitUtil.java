package com.nanchen.retrofitrxdemoo.util;

import com.nanchen.retrofitrxdemoo.ApiService;
import com.nanchen.retrofitrxdemoo.consts.Consts;
import com.nanchen.retrofitrxdemoo.model.BaseResponse;
import com.nanchen.retrofitrxdemoo.model.UserModel;
import com.nanchen.retrofitrxdemoo.model.tngou.Cook;
import com.nanchen.retrofitrxdemoo.model.tngou.TngouResponse;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author nanchen
 * @fileName RetrofitRxDemoo
 * @packageName com.nanchen.retrofitrxdemoo.util
 * @date 2016/12/12  10:38
 */

public class RetrofitUtil {

    public static final int DEFAULT_TIMEOUT = 5;

    private Retrofit mRetrofit;
    private ApiService mApiService;

    private static RetrofitUtil mInstance;

    /**
     * 私有构造方法
     */
    private RetrofitUtil(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(Consts.APP_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mApiService = mRetrofit.create(ApiService.class);
    }

    public static RetrofitUtil getInstance(){
        if (mInstance == null){
            synchronized (RetrofitUtil.class){
                mInstance = new RetrofitUtil();
            }
        }
        return mInstance;
    }

    /**
     * 用于获取用户信息
     * @param subscriber
     */
    public void getUsers(Subscriber<BaseResponse<List<UserModel>>> subscriber){
        mApiService.getUsersByRx()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getUsersByMore(Subscriber<BaseResponse<? extends Object>> subscriber){
        mApiService.getUsersByRx()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     *
     * @param page  请求页数，默认page=1
     * @param rows  每页返回的条数，默认rows = 20
     * @param subscriber
     */
    public void getCookList(int page, int rows, Subscriber<TngouResponse<List<Cook>>> subscriber){
//        mApiService.getCookList(page,rows)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);

        toSubscribe(mApiService.getCookList(page,rows),subscriber);
    }

    private <T> void toSubscribe(Observable<T> observable,Subscriber<T> subscriber){
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
