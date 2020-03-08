package com.goldze.mvvmhabit.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.goldze.mvvmhabit.data.source.HttpDataSource;
import com.goldze.mvvmhabit.data.source.LocalDataSource;
import com.goldze.mvvmhabit.entity.DemoEntity;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateBodyEntity;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateResponseEntity;
import com.goldze.mvvmhabit.entity.http.login.LoginBodyEntity;
import com.goldze.mvvmhabit.entity.http.register.RegisterBodyEntity;
import com.goldze.mvvmhabit.entity.http.register.RegisterOrLoginResponseEntity;
import com.goldze.mvvmhabit.entity.http.verifiedcode.VerifiedCodeEntity;
import com.goldze.mvvmhabit.entity.http.verifiedcode.VerifiedCodeResponseEntity;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseModel;
import me.goldze.mvvmhabit.http.BaseResponse;

/**
 * MVVM的Model层，统一模块的数据仓库，包含网络数据和本地数据（一个应用可以有多个Repositor）
 * Created by goldze on 2019/3/26.
 */
public class DemoRepository extends BaseModel implements HttpDataSource, LocalDataSource {
    private volatile static DemoRepository INSTANCE = null;
    private final HttpDataSource mHttpDataSource;

    private final LocalDataSource mLocalDataSource;

    private DemoRepository(@NonNull HttpDataSource httpDataSource,
                           @NonNull LocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static DemoRepository getInstance(HttpDataSource httpDataSource,
                                             LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (DemoRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DemoRepository(httpDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public Observable<VerifiedCodeResponseEntity> getVerifiedCode(VerifiedCodeEntity entity) {
        return mHttpDataSource.getVerifiedCode(entity);
    }


    @Override
    public Observable<Object> login() {
        return mHttpDataSource.login();
    }

    @Override
    public Observable<CheckUpdateResponseEntity> checkUpdate(String appid, String sign,String token,String callId,CheckUpdateBodyEntity entity) {
        Map<String, String> map=new HashMap<>();
        map.put("appKey",appid);
        map.put("mysig",sign);
        map.put("token",token);
        map.put("callId",callId);
        return mHttpDataSource.checkUpdate(appid, sign, token,callId, entity);
    }

    @Override
    public Observable<RegisterOrLoginResponseEntity> loginUser(LoginBodyEntity entity) {
        return mHttpDataSource.loginUser(entity);
    }

    @Override
    public Observable<RegisterOrLoginResponseEntity> registerUser(RegisterBodyEntity entity) {
        return mHttpDataSource.registerUser(entity);
    }

    @Override
    public Observable<DemoEntity> loadMore() {
        return mHttpDataSource.loadMore();
    }

    @Override
    public Observable<BaseResponse<DemoEntity>> demoGet() {
        return mHttpDataSource.demoGet();
    }

    @Override
    public Observable<BaseResponse<DemoEntity>> demoPost(String catalog) {
        return mHttpDataSource.demoPost(catalog);
    }


    @Override
    public void saveToken(String token) {
        mLocalDataSource.saveToken(token);
    }
    @Override
    public void saveUserID(int token) {
        mLocalDataSource.saveUserID(token);
    }

    @Override
    public void saveUserName(String userName) {
        mLocalDataSource.saveUserName(userName);
    }

    @Override
    public void savePassword(String password) {
        mLocalDataSource.savePassword(password);
    }

    @Override
    public String getUserName() {
        return mLocalDataSource.getUserName();
    }

    @Override
    public void saveSmsToken(String smsToken) {
        mLocalDataSource.saveSmsToken(smsToken);
    }

    @Override
    public String getSmsToken() {
        return mLocalDataSource.getSmsToken();
    }

    @Override
    public int getUserID() {
        return mLocalDataSource.getUserID();
    }

    @Override
    public String getPassword() {
        return mLocalDataSource.getPassword();
    }

    @Override
    public String getToken() {
        return mLocalDataSource.getToken();
    }
}
