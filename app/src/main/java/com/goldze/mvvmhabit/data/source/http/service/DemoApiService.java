package com.goldze.mvvmhabit.data.source.http.service;

import com.goldze.mvvmhabit.entity.DemoEntity;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateBodyEntity;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateResponseEntity;
import com.goldze.mvvmhabit.entity.http.login.LoginBodyEntity;
import com.goldze.mvvmhabit.entity.http.register.RegisterBodyEntity;
import com.goldze.mvvmhabit.entity.http.register.RegisterOrLoginResponseEntity;
import com.goldze.mvvmhabit.entity.http.verifiedcode.VerifiedCodeEntity;
import com.goldze.mvvmhabit.entity.http.verifiedcode.VerifiedCodeResponseEntity;

import java.util.Map;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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
    Observable<VerifiedCodeResponseEntity> getVerifiedCode(@QueryMap Map<String, String> map,@Body VerifiedCodeEntity entity);

    @POST("v1/resources/new")
    Observable<CheckUpdateResponseEntity> checkUpdate(@QueryMap Map<String, String> map, @Body CheckUpdateBodyEntity entity);

    @POST("v1/login?appKey=1uMqYWpHo3MoLH&callId=1276418994&mysig=sig-result")
    Observable<RegisterOrLoginResponseEntity> login(@Body LoginBodyEntity entity);

    @POST("v1/register?appKey=1uMqYWpHo3MoLH&callId=1276418994&mysig=sig-result")
    Observable<RegisterOrLoginResponseEntity> registerUser(@Body RegisterBodyEntity entity);
}
