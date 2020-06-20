package com.myzr.allproduct.data.source.http.service;

import com.myzr.allproduct.entity.DemoEntity;
import com.myzr.allproduct.entity.db.UserActionData;
import com.myzr.allproduct.entity.http.ResponseNetDeviceInfoEntity;
import com.myzr.allproduct.entity.http.checkversion.CheckUpdateBodyEntity;
import com.myzr.allproduct.entity.http.checkversion.CheckUpdateResponseEntity;
import com.myzr.allproduct.entity.http.login.LoginBodyEntity;
import com.myzr.allproduct.entity.http.productinfo.ProductInfoResponseEntity;
import com.myzr.allproduct.entity.http.register.RegisterBodyEntity;
import com.myzr.allproduct.entity.http.register.RegisterOrLoginResponseEntity;
import com.myzr.allproduct.entity.http.useraction.SubmitActionDataResponseEntity;
import com.myzr.allproduct.entity.http.userinfo.RegisterUserInfoEntity;
import com.myzr.allproduct.entity.http.userinfo.RegisterUserInfoResponseEntity;
import com.myzr.allproduct.entity.http.verifiedcode.VerifiedCodeEntity;
import com.myzr.allproduct.entity.http.verifiedcode.VerifiedCodeResponseEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    @GET("v1/units/{seriNo}/info")
    Observable<ResponseNetDeviceInfoEntity> getDeviceInfo(@Path("seriNo") String seriNo, @QueryMap Map<String, String> map);

    @GET("v1/units/prod/{bathCode}")
    Observable<ProductInfoResponseEntity> getProductInfo(@Header ("Authorization")String token,@Path("bathCode") String bathCode, @QueryMap Map<String, String> map);

    @POST("v1/login?appKey=1uMqYWpHo3MoLH&callId=1276418994&mysig=sig-result")
    Observable<RegisterOrLoginResponseEntity> login(@Body LoginBodyEntity entity);

    @POST("v1/register?appKey=1uMqYWpHo3MoLH&callId=1276418994&mysig=sig-result")
    Observable<RegisterOrLoginResponseEntity> registerUser(@Body RegisterBodyEntity entity);

    /**
     * 注册用户信息
     * @param entity
     * @return
     */
    @POST("v1/users/{userId}/profile")
    Observable<RegisterUserInfoResponseEntity> registerUserInfo(@Path("userId") String userId, @QueryMap Map<String, String> map,@Body RegisterUserInfoEntity entity);

    /**
     * 提交用户行为信息
     * @param entitys
     * @return
     */
    @POST("/v1/users/6/actions")
    Observable<SubmitActionDataResponseEntity> submitUserActionData(@QueryMap Map<String, String> map, @Body List<UserActionData> entitys);
}
