package com.goldze.mvvmhabit.data.source;

import com.goldze.mvvmhabit.entity.DemoEntity;
import com.goldze.mvvmhabit.entity.db.UserActionData;
import com.goldze.mvvmhabit.entity.http.ResponseNetDeviceInfoEntity;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateBodyEntity;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateResponseEntity;
import com.goldze.mvvmhabit.entity.http.login.LoginBodyEntity;
import com.goldze.mvvmhabit.entity.http.register.RegisterBodyEntity;
import com.goldze.mvvmhabit.entity.http.register.RegisterOrLoginResponseEntity;
import com.goldze.mvvmhabit.entity.http.useraction.SubmitActionDataResponseEntity;
import com.goldze.mvvmhabit.entity.http.userinfo.RegisterUserInfoEntity;
import com.goldze.mvvmhabit.entity.http.userinfo.RegisterUserInfoResponseEntity;
import com.goldze.mvvmhabit.entity.http.verifiedcode.VerifiedCodeEntity;
import com.goldze.mvvmhabit.entity.http.verifiedcode.VerifiedCodeResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.QueryMap;

/**
 * Created by goldze on 2019/3/26.
 */
public interface HttpDataSource {

    //获取验证码
    Observable<VerifiedCodeResponseEntity> getVerifiedCode(VerifiedCodeEntity body);


    //模拟登录
    Observable<Object> login();

    /**
     * 检测更新
     *
     * @param entity
     * @return
     */
    Observable<CheckUpdateResponseEntity> checkUpdate(String appid, String sign,String token,String callId,CheckUpdateBodyEntity entity);

    /**
     * 返回设备信息
     *
     * @return
     */
    Observable<ResponseNetDeviceInfoEntity> getDeviceInfo(String serioNum,String appid, String sign, String token, String callId);

    /**
     * 注册用户信息
     *
     * @return
     */
    Observable<RegisterUserInfoResponseEntity> registerUserInfo(String userId, String appid, String sign, String token, String callId, RegisterUserInfoEntity entity);

    /**
     * 提交用户行为信息
     *
     * @param appid
     * @param sign
     * @param token
     * @param callId
     * @param entity
     * @return
     */

    Observable<SubmitActionDataResponseEntity> submitUserActionData(String appid, String sign, String token, String callId, @Body List<UserActionData> entity);

    /**
     * 登录
     *
     * @param entity
     * @return
     */
    Observable<RegisterOrLoginResponseEntity> loginUser(LoginBodyEntity entity);

    /**
     * 注册
     *
     * @param entity
     * @return
     */
    Observable<RegisterOrLoginResponseEntity> registerUser(RegisterBodyEntity entity);

    //模拟上拉加载
    Observable<DemoEntity> loadMore();

    Observable<BaseResponse<DemoEntity>> demoGet();

    Observable<BaseResponse<DemoEntity>> demoPost(String catalog);


}
