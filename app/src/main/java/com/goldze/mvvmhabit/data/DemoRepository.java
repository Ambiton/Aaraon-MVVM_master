package com.goldze.mvvmhabit.data;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.goldze.mvvmhabit.app.AppApplication;
import com.goldze.mvvmhabit.data.source.HttpDataSource;
import com.goldze.mvvmhabit.data.source.LocalDataSource;
import com.goldze.mvvmhabit.entity.DemoEntity;
import com.goldze.mvvmhabit.entity.UserDao;
import com.goldze.mvvmhabit.entity.db.ProductInfoData;
import com.goldze.mvvmhabit.entity.db.UserActionData;
import com.goldze.mvvmhabit.entity.db.UserActionDataDao;
import com.goldze.mvvmhabit.entity.http.ResponseNetDeviceInfoEntity;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateBodyEntity;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateResponseEntity;
import com.goldze.mvvmhabit.entity.http.login.LoginBodyEntity;
import com.goldze.mvvmhabit.entity.http.productinfo.ProductInfoResponseEntity;
import com.goldze.mvvmhabit.entity.http.register.RegisterBodyEntity;
import com.goldze.mvvmhabit.entity.http.register.RegisterOrLoginResponseEntity;
import com.goldze.mvvmhabit.entity.http.useraction.SubmitActionDataResponseEntity;
import com.goldze.mvvmhabit.entity.http.userinfo.RegisterUserInfoEntity;
import com.goldze.mvvmhabit.entity.http.userinfo.RegisterUserInfoResponseEntity;
import com.goldze.mvvmhabit.entity.http.verifiedcode.VerifiedCodeEntity;
import com.goldze.mvvmhabit.entity.http.verifiedcode.VerifiedCodeResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


    public List<UserActionData> getLimitUserActionData(int limitCount){
        int allCount= (int) AppApplication.getInstance().getDaoSession().getUserActionDataDao().count();
        if(allCount==0){
            return null;
        }
        if(allCount>limitCount){
            allCount=limitCount;
        }
        return AppApplication.getInstance().getDaoSession().getUserActionDataDao().queryBuilder().orderAsc(UserActionDataDao.Properties.RemoteId).limit(allCount).list();
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
    public Observable<ResponseNetDeviceInfoEntity> getDeviceInfo(String serioNum, String appid, String sign, String token, String callId) {
        return mHttpDataSource.getDeviceInfo(serioNum,appid, sign, token,callId);
    }

    @Override
    public Observable<ProductInfoResponseEntity> getProductInfo(String batchCode, String appid, String sign, String callId) {
        return mHttpDataSource.getProductInfo(batchCode, appid, sign, callId);
    }

    @Override
    public Observable<RegisterUserInfoResponseEntity> registerUserInfo(String userId, String appid, String sign, String token, String callId, RegisterUserInfoEntity entity) {
        return mHttpDataSource.registerUserInfo(userId,appid, sign, token,callId,entity);
    }

    @Override
    public Observable<SubmitActionDataResponseEntity> submitUserActionData(String appid, String sign, String token, String callId, List<UserActionData> entitys) {
        return mHttpDataSource.submitUserActionData(appid, sign, token, callId, entitys);
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
    public void saveUnitId(int unitId) {
        mLocalDataSource.saveUnitId(unitId);
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
    public void saveBannerVersion(String version) {
        mLocalDataSource.saveBannerVersion(version);
    }

    @Override
    public void saveLoadingVersion(String version) {
        mLocalDataSource.saveLoadingVersion(version);
    }

    @Override
    public void saveBannerPlayMode(String bannerPlayMode) {
        mLocalDataSource.saveBannerPlayMode(bannerPlayMode);
    }

    @Override
    public void saveBannerPlayIndex(int bannerPlayIndex) {
        mLocalDataSource.saveBannerPlayIndex(bannerPlayIndex);
    }

    @Override
    public int getBannerPlayIndex() {
        return mLocalDataSource.getBannerPlayIndex();
    }

    @Override
    public String getBannerPlayMode() {
        return mLocalDataSource.getBannerPlayMode();
    }

    @Override
    public String getLoadingVersion() {
        return mLocalDataSource.getLoadingVersion();
    }

    @Override
    public String getBannerVersion() {
        return mLocalDataSource.getBannerVersion();
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
    public int getUnitID() {
        return mLocalDataSource.getUnitID();
    }

    @Override
    public void saveUserActionDataToDB(UserActionData userActionData) {
        mLocalDataSource.saveUserActionDataToDB(userActionData);

    }

    @Override
    public void saveProductInfoDataToDB(ProductInfoData productInfoData) {
        mLocalDataSource.saveProductInfoDataToDB(productInfoData);
    }

    @Override
    public ProductInfoData getProductInfoData(String productId) {
        return mLocalDataSource.getProductInfoData(productId);
    }

    public synchronized void deleteUserActionDataToDB(List<UserActionData> userActionDatas) {
        AppApplication.getInstance().getDaoSession().getUserActionDataDao().deleteInTx(userActionDatas);
    }
    @Override
    public void deleteUserActionDataToDB(UserActionData... userActionDatas) {
        mLocalDataSource.deleteUserActionDataToDB(userActionDatas);
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
