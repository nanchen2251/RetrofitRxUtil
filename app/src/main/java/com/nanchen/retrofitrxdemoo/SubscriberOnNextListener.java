package com.nanchen.retrofitrxdemoo;

/**
 * @author nanchen
 * @fileName RetrofitRxDemoo
 * @packageName com.nanchen.retrofitrxdemoo
 * @date 2016/12/12  14:48
 */

public interface SubscriberOnNextListener<T> {
    void onNext(T t);
}
