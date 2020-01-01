package com.goldze.mvvmhabit.data.source.http.service;

import com.goldze.mvvmhabit.entity.DemoEntity;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateBodyEntity;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateResponseEntity;
import com.goldze.mvvmhabit.entity.http.login.LoginBodyEntity;
import com.goldze.mvvmhabit.entity.http.register.RegisterBodyEntity;
import com.goldze.mvvmhabit.entity.http.register.RegisterResponseEntity;
import com.goldze.mvvmhabit.entity.http.verifiedcode.VerifiedCodeEntity;
import com.goldze.mvvmhabit.entity.http.verifiedcode.VerifiedCodeResponseEntity;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by goldze on 2017/6/15.
 */

public interface DemoApiService {
    @GET("action/apiv2/banner?catalog=1")
    Observable<BaseResponse<DemoEntity>> demoGet();

    @FormUrlEncoded
    @POST("action/apiv2/banner")
    Observable<BaseResponse<DemoEntity>> demoPost(@Field("catalog") String catalog);

    @POST("v1/sms/token?appKey=1uMqYWpHo3MoLH&callId=1276418994&mysig=sig-result")
    Observable<VerifiedCodeResponseEntity> getVerifiedCode(@Body VerifiedCodeEntity entity);

    @POST("v1/resource_info?callId=137896774355")
    Observable<CheckUpdateResponseEntity> checkUpdate(@Query("appKey") String appid,@Query("mysig") String sign,@Query("token") String token,@Body CheckUpdateBodyEntity entity);

    @POST("v1/login?appKey=1uMqYWpHo3MoLH&callId=1276418994&mysig=sig-result")
    Observable<Object> login(@Body LoginBodyEntity entity);

    @POST("v1/register?appKey=1uMqYWpHo3MoLH&callId=1276418994&mysig=sig-result")
    Observable<RegisterResponseEntity> registerUser(@Body RegisterBodyEntity entity);
}
