package com.nanchen.retrofitrxdemoo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 获取json数据基类
 *
 * @author nanchen
 * @fileName RetrofitRxDemoo
 * @packageName com.nanchen.retrofitrxdemoo
 * @date 2016/12/09  17:05
 */

public class BaseResponse<T> implements Serializable{
    @SerializedName("code")
    public int code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public T data;
}
