package com.myzr.allproduct.ui.register;

import android.app.Application;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.annotation.NonNull;

import android.view.View;

import com.myzr.allproduct.data.DemoRepository;
import com.myzr.allproduct.entity.http.userinfo.RegisterUserInfoEntity;
import com.myzr.allproduct.entity.http.userinfo.RegisterUserInfoResponseEntity;
import com.myzr.allproduct.ui.main.DeviceListActivity;
import com.myzr.allproduct.utils.AppTools;
import com.myzr.allproduct.utils.HttpStatus;
import com.myzr.allproduct.utils.HttpsUtils;
import com.tamsiree.rxtool.RxLogTool;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.AppManager;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author Areo
 * @description:
 * @date : 2019/12/14 20:25
 */
public class UserHeightViewModel extends BaseViewModel<DemoRepository> {
    private static final String TAG = "UserHeightViewModel";
    //用户名的绑定
    public ObservableField<String> userHeight = new ObservableField<>("");
    //密码的绑定
    public ObservableField<String> password = new ObservableField<>("");
    //用户名清除按钮的显示隐藏绑定
    public ObservableInt clearBtnVisibility = new ObservableInt();
    //发送dialogEvent
    public ObservableField<String> dialogEvent = new ObservableField<>("");
    //封装一个界面发生改变的观察者
    public UserHeightViewModel.UIChangeObservable uc = new UserHeightViewModel.UIChangeObservable();
    private RegisterUserInfoEntity userInfoEntity = new RegisterUserInfoEntity();

    public void setUserInfoEntity(RegisterUserInfoEntity userInfoEntity) {
        if (userInfoEntity == null) {
            return;
        }
        this.userInfoEntity = userInfoEntity;
    }

    public RegisterUserInfoEntity getUserInfoEntity() {
        return this.userInfoEntity;
    }

    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();
    }

    public UserHeightViewModel(@NonNull Application application, DemoRepository demoRepository) {
        super(application,demoRepository);
        //从本地取得数据绑定到View层
    }

    //清除用户名的点击事件, 逻辑从View层转换到ViewModel层
    public BindingCommand clearUserNameOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            userHeight.set("");
        }
    });
    //密码显示开关  (你可以尝试着狂按这个按钮,会发现它有防多次点击的功能)
    public BindingCommand passwordShowSwitchOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //让观察者的数据改变,逻辑从ViewModel层转到View层，在View层的监听则会被调用
            uc.pSwitchEvent.setValue(uc.pSwitchEvent.getValue() == null || !uc.pSwitchEvent.getValue());
        }
    });
    //用户名输入框焦点改变的回调事件
    public BindingCommand<Boolean> onFocusChangeCommand = new BindingCommand<>(new BindingConsumer<Boolean>() {
        @Override
        public void call(Boolean hasFocus) {
            if (hasFocus) {
                clearBtnVisibility.set(View.VISIBLE);
            } else {
                clearBtnVisibility.set(View.INVISIBLE);
            }
        }
    });
    //用户协议的点击事件
    public BindingCommand nextBtnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //跳转到设备列表
            submitRegisterUserInfo();

        }
    });

    private void submitRegisterUserInfo() {
        addSubscribe(model.registerUserInfo(String.valueOf(model.getUserID()), AppTools.APPKEY, AppTools.APPSIGN, model.getToken(), HttpsUtils.getCurrentMills(), userInfoEntity)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new Consumer<RegisterUserInfoResponseEntity>() {
                    @Override
                    public void accept(RegisterUserInfoResponseEntity registerUserInfoResponseEntity) throws Exception {
                        dismissDialog();
                        if (registerUserInfoResponseEntity.getStatus() == HttpStatus.STATUS_CODE_SUCESS) {
                            AppManager.getAppManager().finishAllActivity();
                            startActivity(DeviceListActivity.class);
                        } else {
                            dialogEvent.set(registerUserInfoResponseEntity.getMessage());
                            dialogEvent.notifyChange();
                            RxLogTool.e(TAG, "submit userInfo failed,status code is " + registerUserInfoResponseEntity.getStatus());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //关闭对话框
                        dismissDialog();
                        if(AppTools.isNetCanUse(getApplication(),true)){
                            if (throwable instanceof ResponseThrowable) {
                                ToastUtils.showShort(((ResponseThrowable) throwable).message);
                            }
                        }

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //关闭对话框
                        dismissDialog();
                    }
                }));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
