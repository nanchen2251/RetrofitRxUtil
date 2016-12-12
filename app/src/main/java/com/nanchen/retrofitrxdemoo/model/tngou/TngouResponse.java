package com.nanchen.retrofitrxdemoo.model.tngou;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 天狗Api返回格式
 *
 * {
 *  "status":true,
 *  "total":101254,
 *  "tngou":Array[20]
 *  }
 *
 * @author nanchen
 * @fileName RetrofitRxDemoo
 * @packageName com.nanchen.retrofitrxdemoo.model.tngou
 * @date 2016/12/12  14:08
 */

public class TngouResponse<T> implements Serializable {

    @SerializedName("status")
    public boolean status;

    @SerializedName("total")
    public int total;

    @SerializedName("tngou")
    public T tngou;

}
