package com.myzr.allproducts.ui.main;

import android.app.Application;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.myzr.allproducts.R;
import com.myzr.allproducts.data.DemoRepository;
import com.myzr.allproducts.entity.http.checkversion.CheckUpdateResponseEntity;
import com.myzr.allproducts.entity.http.register.RegisterBodyEntity;
import com.myzr.allproducts.entity.http.register.RegisterOrLoginResponseEntity;
import com.myzr.allproducts.entity.http.userinfo.RegisterUserInfoEntity;
import com.myzr.allproducts.entity.http.verifiedcode.VerifiedCodeEntity;
import com.myzr.allproducts.entity.http.verifiedcode.VerifiedCodeResponseEntity;
import com.myzr.allproducts.ui.register.UserNickNameActivity;
import com.myzr.allproducts.utils.AppTools;
import com.myzr.allproducts.utils.HttpStatus;
import com.myzr.allproducts.utils.RxRegTool;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2017/7/17.
 */

public class RegisterViewModel extends BaseViewModel<DemoRepository> implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG="RegisterViewModel";
    private static final String STR_SENDVERIFY="发送验证码";
    private static final String STR_SENDVERIFY_DURATION="s后重发";
    private static final int MAX_DURATION=60;
    private static  int timeLeft=60;
    private Timer getVerifyCodeTimer;
    //用户名的绑定
    public ObservableField<String> userName = new ObservableField<>("");
    //密码的绑定
    public ObservableField<String> password = new ObservableField<>("");
    //是否同意协议
    public boolean agreeProtect = false;
    //发送验证码按钮绑定
    public ObservableField<String> verfyCodeText = new ObservableField<>("");

    //发送dialogEvent
    public ObservableField<String> dialogEvent = new ObservableField<>("");
    //用户名清除按钮的显示隐藏绑定
    public ObservableInt clearBtnVisibility = new ObservableInt();
    //版本检测观察者
    public ObservableField<CheckUpdateResponseEntity> versionEvent = new ObservableField<>();
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    private String smsToken="";

    private final String PRIVATE_PASSWORD="myzrapp";
    private final String PRIVATE_TYPE_REGISTER="1";
    private final String PRIVATE_TYPE_LOGIN="2";
    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.cb_register:
                agreeProtect=isChecked;
                break;
            default:
                break;
        }
    }

    public RegisterViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
        //从本地取得数据绑定到View层
        userName.set("");
        password.set("");
        verfyCodeText.set(STR_SENDVERIFY);
    }

    private void startVerifyTimer(){
        stopVerifyTimer();
        getVerifyCodeTimer=new Timer();
        timeLeft=MAX_DURATION;
        getVerifyCodeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                verfyCodeText.set(timeLeft+STR_SENDVERIFY_DURATION);
                timeLeft--;

                if(timeLeft<=0){
                    verfyCodeText.set(STR_SENDVERIFY);
                    stopVerifyTimer();
                }
            }
        },0,1000);
    }

    private void stopVerifyTimer(){
        if(getVerifyCodeTimer!=null){
            getVerifyCodeTimer.cancel();
            getVerifyCodeTimer=null;
        }
    }
    //清除用户名的点击事件, 逻辑从View层转换到ViewModel层
    public BindingCommand clearUserNameOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            userName.set("");
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

    //发送验证码
    public BindingCommand verifyCodeSwitchOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(userName.get())|| !RxRegTool.isMobileSimple(userName.get())) {
                ToastUtils.showShort("请输入正确的手机号！");
                return;
            }
            if(verfyCodeText.get().equals(STR_SENDVERIFY)){
                getVersionCode();
            }
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
    //注册按钮的点击事件
    public BindingCommand registerOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            register();
        }
    });

    //用户协议的点击事件
    public BindingCommand userAgreementClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //跳转到用户协议
            startContainerActivity(AgreementFragment.class.getCanonicalName());
        }
    });

    private void register(){
        if(!agreeProtect){
            ToastUtils.showLong("请先阅读并同意服务条款！");
            return;
        }
        if (TextUtils.isEmpty(userName.get())|| !RxRegTool.isMobileSimple(userName.get())) {
            ToastUtils.showLong("请输入正确的手机号！");
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtils.showLong("请输入验证码！");
            return;
        }
        addSubscribe(model.registerUser(new RegisterBodyEntity(model.getSmsToken(),userName.get(),password.get(),"1"))
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new Consumer<RegisterOrLoginResponseEntity>() {
                    @Override
                    public void accept(RegisterOrLoginResponseEntity registerOrLoginResponseEntity) throws Exception {
                        dismissDialog();
                        if(registerOrLoginResponseEntity.getStatus()== HttpStatus.STATUS_CODE_SUCESS){
                            model.saveToken(registerOrLoginResponseEntity.getRegisterResponseDataEntity().getToken());
                            model.saveUserID(registerOrLoginResponseEntity.getRegisterResponseDataEntity().getUserId());
                            RegisterUserInfoEntity registerUserInfoEntity=new RegisterUserInfoEntity();
                            Bundle bundle=new Bundle();
                            bundle.putParcelable(AppTools.KEY_REGISTER_USERINFO,registerUserInfoEntity);
                            startActivity(UserNickNameActivity.class,bundle);
                            finish();
                        }else{
                            dialogEvent.set(registerOrLoginResponseEntity.getMessage());
                            dialogEvent.notifyChange();
                        }
                        //保存账号密码
                        model.saveUserName(userName.get());
                        model.savePassword(password.get());
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

    private void getVersionCode(){
        if (TextUtils.isEmpty(userName.get())|| !RxRegTool.isMobileSimple(userName.get())) {
            ToastUtils.showShort("请输入正确的手机号！");
            return;
        }
        if(!AppTools.isNetCanUse(getApplication(),true)){
            return;
        }

        //RaJava获取验证码
        addSubscribe(model.getVerifiedCode(new VerifiedCodeEntity(userName.get(),PRIVATE_TYPE_LOGIN))
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        startVerifyTimer();
                    }
                })
                .subscribe(new Consumer<VerifiedCodeResponseEntity>() {
                    @Override
                    public void accept(VerifiedCodeResponseEntity entity) throws Exception {
                        Log.e(TAG,"getStatus is:"+entity.getStatus()+";smstoken is "+entity.getData());
                        //保存Token
                        if(entity.getStatus()==HttpStatus.STATUS_CODE_SUCESS){
                            smsToken=entity.getData();
                            model.saveSmsToken(entity.getData());
                        }else{
                            if(!TextUtils.isEmpty(entity.getMessage())){
                                ToastUtils.showLong(entity.getMessage());
                            }
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
        stopVerifyTimer();
    }
}
