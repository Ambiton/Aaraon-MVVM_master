package com.myzr.allproducts.data.source.http;

import com.myzr.allproducts.data.source.HttpDataSource;
import com.myzr.allproducts.data.source.http.service.DemoApiService;
import com.myzr.allproducts.entity.DemoEntity;
import com.myzr.allproducts.entity.db.UserActionData;
import com.myzr.allproducts.entity.http.ResponseNetDeviceInfoEntity;
import com.myzr.allproducts.entity.http.checkversion.CheckUpdateBodyEntity;
import com.myzr.allproducts.entity.http.checkversion.CheckUpdateResponseEntity;
import com.myzr.allproducts.entity.http.login.LoginBodyEntity;
import com.myzr.allproducts.entity.http.productinfo.ProductInfoResponseEntity;
import com.myzr.allproducts.entity.http.register.RegisterBodyEntity;
import com.myzr.allproducts.entity.http.register.RegisterOrLoginResponseEntity;
import com.myzr.allproducts.entity.http.useraction.SubmitActionDataResponseEntity;
import com.myzr.allproducts.entity.http.userinfo.RegisterUserInfoEntity;
import com.myzr.allproducts.entity.http.userinfo.RegisterUserInfoResponseEntity;
import com.myzr.allproducts.entity.http.verifiedcode.VerifiedCodeEntity;
import com.myzr.allproducts.entity.http.verifiedcode.VerifiedCodeResponseEntity;
import com.myzr.allproducts.utils.AppTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import me.goldze.mvvmhabit.http.BaseResponse;

/**
 * Created by goldze on 2019/3/26.
 */
public class HttpDataSourceImpl implements HttpDataSource {
    private DemoApiService apiService;
    private volatile static HttpDataSourceImpl INSTANCE = null;

    public static HttpDataSourceImpl getInstance(DemoApiService apiService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private HttpDataSourceImpl(DemoApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Observable<VerifiedCodeResponseEntity> getVerifiedCode(VerifiedCodeEntity body) {
        Map<String, String> map=new HashMap<>();
        map.put("appKey", AppTools.APPKEY);
        map.put("mysig","sig-result");
        map.put("callId","1276418994");
        return apiService.getVerifiedCode(map,body);
    }

    @Override
    public Observable<RegisterOrLoginResponseEntity> registerUser(RegisterBodyEntity entity) {
        return apiService.registerUser(entity);
    }

    @Override
    public Observable<Object> login() {
        return Observable.just(new Object()).delay(3, TimeUnit.SECONDS); //??????3???
    }

    @Override
    public Observable<CheckUpdateResponseEntity> checkUpdate(String appid, String sign,String token,String callId,CheckUpdateBodyEntity entity ) {
        Map<String, String> map=new HashMap<>();
        map.put("appKey",appid);
        map.put("mysig",sign);
        map.put("token",token);
        map.put("callId",callId);
        return apiService.checkUpdate(map,entity);
    }

    @Override
    public Observable<ResponseNetDeviceInfoEntity> getDeviceInfo(String serioNum, String appid, String sign, String token, String callId) {
        Map<String, String> map=new HashMap<>();
        map.put("appKey",appid);
        map.put("mysig",sign);
        map.put("token",token);
        map.put("callId",callId);
        return apiService.getDeviceInfo(serioNum,map);
    }

    @Override
    public Observable<ProductInfoResponseEntity> getProductInfo(String batchCode, String appid, String sign,String token, String callId) {
        Map<String, String> map=new HashMap<>();
        map.put("appKey",appid);
        map.put("mysig",sign);
        map.put("callId",callId);
        return apiService.getProductInfo(token,batchCode,map);
    }

    @Override
    public Observable<RegisterUserInfoResponseEntity> registerUserInfo(String userId, String appid, String sign, String token, String callId, RegisterUserInfoEntity entity) {
        Map<String, String> map=new HashMap<>();
        map.put("appKey",appid);
        map.put("mysig",sign);
        map.put("token",token);
        map.put("callId",callId);
        return apiService.registerUserInfo(userId,map,entity);
    }

    @Override
    public Observable<SubmitActionDataResponseEntity> submitUserActionData(String appid, String sign, String token, String callId, List<UserActionData> entitys) {
        Map<String, String> map=new HashMap<>();
        map.put("appKey",appid);
        map.put("mysig",sign);
        map.put("token",token);
        map.put("callId",callId);
        return apiService.submitUserActionData(map,entitys);
    }


    @Override
    public Observable<RegisterOrLoginResponseEntity> loginUser(LoginBodyEntity entity) {
        return apiService.login(entity);
    }


    @Override
    public Observable<DemoEntity> loadMore() {
        return Observable.create(new ObservableOnSubscribe<DemoEntity>() {
            @Override
            public void subscribe(ObservableEmitter<DemoEntity> observableEmitter) throws Exception {
                DemoEntity entity = new DemoEntity();
                List<DemoEntity.ItemsEntity> itemsEntities = new ArrayList<>();
                //????????????????????????
                for (int i = 0; i < 10; i++) {
                    DemoEntity.ItemsEntity item = new DemoEntity.ItemsEntity();
                    item.setId(-1);
                    item.setName("????????????");
                    itemsEntities.add(item);
                }
                entity.setItems(itemsEntities);
                observableEmitter.onNext(entity);
            }
        }).delay(3, TimeUnit.SECONDS); //??????3???
    }

    @Override
    public Observable<BaseResponse<DemoEntity>> demoGet() {
        return apiService.demoGet();
    }

    @Override
    public Observable<BaseResponse<DemoEntity>> demoPost(String catalog) {
        return apiService.demoPost(catalog);
    }
}
