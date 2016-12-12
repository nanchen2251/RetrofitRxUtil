package com.nanchen.retrofitrxdemoo;

import com.nanchen.retrofitrxdemoo.model.BaseResponse;
import com.nanchen.retrofitrxdemoo.model.UserModel;
import com.nanchen.retrofitrxdemoo.model.tngou.Cook;
import com.nanchen.retrofitrxdemoo.model.tngou.TngouResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author nanchen
 * @fileName RetrofitRxDemoo
 * @packageName com.nanchen.retrofitrxdemoo
 * @date 2016/12/09  17:04
 */

public interface ApiService {

    /**
     * 使用普通的retrofit方式获取数据
     * @return
     */
    @GET("ezSQL/get_user.php")
    Call<BaseResponse<List<UserModel>>> getUsers();


    /**
     * 使用rx+retrofit的方式获取数据
     */
    @GET("ezSQL/get_user.php")
    Observable<BaseResponse<List<UserModel>>> getUsersByRx();


    @GET("api/cook/list")
    Observable<TngouResponse<List<Cook>>> getCookList(@Query("page") int page,@Query("rows")int rows);
}
