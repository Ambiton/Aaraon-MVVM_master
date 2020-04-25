package com.goldze.mvvmhabit.ui.main;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.data.DemoRepository;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateBodyEntity;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateResponseEntity;
import com.goldze.mvvmhabit.ui.login.LoginActivity;
import com.goldze.mvvmhabit.utils.AppTools;
import com.goldze.mvvmhabit.utils.HttpStatus;
import com.goldze.mvvmhabit.utils.HttpsUtils;
import com.tamsiree.rxtool.RxLogTool;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.http.NetworkUtil;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2017/7/17.
 */

public class LoadingViewModel extends BaseViewModel <DemoRepository>{
    private static final String TAG="LoadingViewModel";
    //版本检测观察者
    public ObservableField<CheckUpdateResponseEntity> versionEvent = new ObservableField<>();
    public ObservableBoolean apkNewst = new ObservableBoolean(false);
    public ObservableBoolean bannerNewst = new ObservableBoolean(false);
    public ObservableBoolean loadNewst = new ObservableBoolean(false);
    public LoadingViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
    }

    public boolean isNeedLogin(Context context){
        return TextUtils.isEmpty(model.getToken())&& NetworkUtil.isNetworkAvailable(context) ;
    }
    public boolean isNeedCheckUpdate(Context context){
        return !TextUtils.isEmpty(model.getToken())&& NetworkUtil.isNetworkAvailable(context) ;
    }

    public void setBannerPlayModel(String playMode){
        model.saveBannerPlayMode(playMode);
    }
    public String getBannerVersion(){
        return model.getBannerVersion();
    }
    public String getLoadingVersion(){
        return model.getLoadingVersion();
    }
    public void setBannerVersion(String version){
        model.saveBannerVersion(version);
    }
    public void setLoadingVersion(String version){
        model.saveLoadingVersion(version);
    }

    public void setBannerNewst(boolean isNew){
        bannerNewst.set(isNew);
        bannerNewst.notifyChange();
    }
    public void setLoadNewst(boolean isNew){
        loadNewst.set(isNew);
        loadNewst.notifyChange();
    }
    public void setApkNewst(boolean isNew){
        apkNewst.set(isNew);
        apkNewst.notifyChange();
    }

    public boolean isAllNewst(){
        RxLogTool.e(TAG,"isAllNewst is "+(loadNewst.get()&&bannerNewst.get()));
        RxLogTool.e(TAG,"bannerNewst is "+(bannerNewst.get()));
        RxLogTool.e(TAG,"loadNewst is "+(loadNewst.get()));
        return loadNewst.get()&&bannerNewst.get();
    }
    public void checkVersion(){
        //RaJava检测更新
        addSubscribe(model.checkUpdate(AppTools.APPKEY,"sig-result",model.getToken(), HttpsUtils.getCurrentMills(),new CheckUpdateBodyEntity("01.00","01.00","01.00"))
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
//                        showDialog();
                    }
                })
                .subscribe(new Consumer<CheckUpdateResponseEntity>() {
                    @Override
                    public void accept(CheckUpdateResponseEntity entity) throws Exception {
//                        dismissDialog();
                        Log.e(TAG,"getStatus is:;Array is "+entity.getStatus());

                        if(entity.getStatus()== HttpStatus.STATUS_CODE_SUCESS){
                            //保存账号密码
                            versionEvent.set(entity);
                            versionEvent.notifyChange();
                        }else if(entity.getStatus()== HttpStatus.STATUS_CODE_TOKEN_OVERDUE){
                            ToastUtils.showLong(R.string.tip_errtoken);
                            startActivity(LoginActivity.class);
                            finish();
                        }else{
                            ToastUtils.showLong(R.string.tip_errservice);
                            startActivity(DeviceListActivity.class);
                            finish();
                        }

                    }
                }));
    }
}
