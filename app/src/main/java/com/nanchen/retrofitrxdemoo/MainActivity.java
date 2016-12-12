package com.nanchen.retrofitrxdemoo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nanchen.retrofitrxdemoo.consts.Consts;
import com.nanchen.retrofitrxdemoo.model.BaseResponse;
import com.nanchen.retrofitrxdemoo.model.UserModel;
import com.nanchen.retrofitrxdemoo.model.tngou.Cook;
import com.nanchen.retrofitrxdemoo.model.tngou.TngouResponse;
import com.nanchen.retrofitrxdemoo.util.RetrofitUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);
        mListener = new SubscriberOnNextListener<TngouResponse<List<Cook>>>() {
            @Override
            public void onNext(TngouResponse<List<Cook>> listTngouResponse) {
                mTextView.setText(listTngouResponse.tngou.toString());
                showToast(listTngouResponse.tngou.toString());
            }
        };
    }


    /**
     * 普通的retrofit获取数据方法
     *
     * @param view
     */
    public void btnClick(View view) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.APP_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService.getUsers().enqueue(new Callback<BaseResponse<List<UserModel>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<UserModel>>> call, Response<BaseResponse<List<UserModel>>> response) {
                showToast("成功:" + response.body().data.toString());
                Log.e(TAG, "成功:" + response.body().data.toString());
                Log.e(TAG, "retCode:" + response.code() + ",msg：" + response.message());
                mTextView.setText("成功:" + response.body().data.toString());
            }

            @Override
            public void onFailure(Call<BaseResponse<List<UserModel>>> call, Throwable t) {
                showToast("失败:" + t.getMessage());
                Log.e(TAG, "失败:" + t.getMessage());
                mTextView.setText("失败:" + t.getMessage());
            }
        });
    }


    private Toast mToast;

    public void showToast(String desc) {
        if (mToast == null) {
            mToast = Toast.makeText(this.getApplicationContext(), desc, Toast.LENGTH_LONG);
        } else {
            mToast.setText(desc);
        }
        mToast.show();
    }

    /**
     * 使用rx+retrofit获取数据
     * <p>
     * 【subscribeOn和observeOn区别】
     * 1、subscribeOn用于切换之前的线程
     * 2、observeOn用于切换之后的线程
     * 3、observeOn之后，不可再调用subscribeOn切换线程
     * <p>
     * ————————  下面是来自扔物线的额外总结 ————————————
     * 1、下面提到的“操作”包括产生事件、用操作符操作事件以及最终的通过 subscriber 消费事件
     * 2、只有第一subscribeOn() 起作用（所以多个 subscribeOn() 毛意义）
     * 3、这个 subscribeOn() 控制从流程开始的第一个操作，直到遇到第一个 observeOn()
     * 4、observeOn() 可以使用多次，每个 observeOn() 将导致一次线程切换()，这次切换开始于这次 observeOn() 的下一个操作
     * 5、不论是 subscribeOn() 还是 observeOn()，每次线程切换如果不受到下一个 observeOn() 的干预，线程将不再改变，不会自动切换到其他线程
     *
     * @param view
     */
    public void btnClick1(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.APP_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Observable<BaseResponse<List<UserModel>>> observable = apiService.getUsersByRx();
        observable
                .subscribeOn(Schedulers.io())  // 网络请求切换在io线程中调用
                .unsubscribeOn(Schedulers.io())// 取消网络请求放在io线程
                .observeOn(AndroidSchedulers.mainThread())// 观察后放在主线程调用
                .subscribe(new Subscriber<BaseResponse<List<UserModel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast("rx失败:" + e.getMessage());
                        Log.e(TAG, "rx失败:" + e.getMessage());
                        mTextView.setText("rx失败:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse<List<UserModel>> listBaseResponse) {
                        showToast("rx成功:" + listBaseResponse.data.toString());
                        mTextView.setText("rx成功:" + listBaseResponse.data.toString());
                        Log.e(TAG, "rx成功:" + listBaseResponse.data.toString());
                    }
                });
    }

    public void btnClick2(View view) {
        RetrofitUtil.getInstance().getUsers(new Subscriber<BaseResponse<List<UserModel>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showToast("rx失败:" + e.getMessage());
                Log.e(TAG, "rx失败:" + e.getMessage());
                mTextView.setText("rx失败:" + e.getMessage());
            }

            @Override
            public void onNext(BaseResponse<List<UserModel>> listBaseResponse) {
                showToast("rx成功:" + listBaseResponse.data.toString());
                mTextView.setText("rx成功:" + listBaseResponse.data.toString());
                Log.e(TAG, "rx成功:" + listBaseResponse.data.toString());
            }
        });
    }

    public void btnClick3(View view) {
        RetrofitUtil.getInstance().getUsersByMore(new Subscriber<BaseResponse<?>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showToast("rx失败:" + e.getMessage());
                Log.e(TAG, "rx失败:" + e.getMessage());
                mTextView.setText("rx失败:" + e.getMessage());
            }

            @Override
            public void onNext(BaseResponse<?> baseResponse) {
                showToast("rx成功:" + baseResponse.data.toString());
                mTextView.setText("rx成功:" + baseResponse.data.toString());
                Log.e(TAG, "rx成功:" + baseResponse.data.toString());
            }
        });
    }

    private SubscriberOnNextListener mListener;

    public void btnClick4(View view) {
//        RetrofitUtil.getInstance().getCookList(2, 5, new Subscriber<TngouResponse<List<Cook>>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                showToast("rx失败:" + e.getMessage());
//                Log.e(TAG, "rx失败:" + e.getMessage());
//                mTextView.setText("rx失败:" + e.getMessage());
//            }
//
//            @Override
//            public void onNext(TngouResponse<List<Cook>> listTngouResponse) {
//                showToast("rx成功:" + listTngouResponse.tngou.toString());
//                mTextView.setText("rx成功:" + listTngouResponse.tngou.toString());
//                Log.e(TAG, "rx成功:" + listTngouResponse.tngou.toString());
//            }
//
//        });

        RetrofitUtil.getInstance().getCookList(2,5,new ProgressSubscriber<TngouResponse<List<Cook>>>(mListener,this));
    }
}
