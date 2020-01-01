package com.goldze.mvvmhabit.data.source;

import com.goldze.mvvmhabit.entity.DemoEntity;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateBodyEntity;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateResponseEntity;
import com.goldze.mvvmhabit.entity.http.login.LoginBodyEntity;
import com.goldze.mvvmhabit.entity.http.register.RegisterBodyEntity;
import com.goldze.mvvmhabit.entity.http.register.RegisterResponseDataEntity;
import com.goldze.mvvmhabit.entity.http.register.RegisterResponseEntity;
import com.goldze.mvvmhabit.entity.http.verifiedcode.VerifiedCodeEntity;
import com.goldze.mvvmhabit.entity.http.verifiedcode.VerifiedCodeResponseEntity;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;

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
    Observable<CheckUpdateResponseEntity> checkUpdate(String appid, String sign,String token,CheckUpdateBodyEntity entity);

    /**
     * 登录
     *
     * @param entity
     * @return
     */
    Observable<Object> loginUser(LoginBodyEntity entity);

    /**
     * 注册
     *
     * @param entity
     * @return
     */
    Observable<RegisterResponseEntity> registerUser(RegisterBodyEntity entity);

    //模拟上拉加载
    Observable<DemoEntity> loadMore();

    Observable<BaseResponse<DemoEntity>> demoGet();

    Observable<BaseResponse<DemoEntity>> demoPost(String catalog);


}
