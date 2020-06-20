package com.myzr.allproduct.data.source;

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

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseResponse;
import retrofit2.http.Body;

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

    Observable<ProductInfoResponseEntity> getProductInfo(String batchCode, String appid, String sign,String token, String callId) ;
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
