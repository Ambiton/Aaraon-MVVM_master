package com.goldze.mvvmhabit.ui.login;

import android.app.Application;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.data.DemoRepository;
import com.goldze.mvvmhabit.entity.http.login.LoginBodyEntity;
import com.goldze.mvvmhabit.entity.http.register.RegisterOrLoginResponseEntity;
import com.goldze.mvvmhabit.entity.http.verifiedcode.VerifiedCodeEntity;
import com.goldze.mvvmhabit.entity.http.verifiedcode.VerifiedCodeResponseEntity;
import com.goldze.mvvmhabit.ui.main.AgreementFragment;
import com.goldze.mvvmhabit.ui.main.DeviceListActivity;
import com.goldze.mvvmhabit.ui.main.RegisterActivity;
import com.goldze.mvvmhabit.utils.HttpStatus;
import com.goldze.mvvmhabit.utils.RxRegTool;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2017/7/17.
 */

public class LoginViewModel extends BaseViewModel<DemoRepository> implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG="LoginViewModel";
    private static final String STR_SENDVERIFY="发送验证码";
    private static final String STR_SENDVERIFY_DURATION="s后重发";
    private static final int MAX_DURATION=60;
    private static  int timeLeft=60;
    private Timer getVerifyCodeTimer;
    //发送dialogEvent
    public ObservableField<String> dialogEvent = new ObservableField<>("");
    //用户名的绑定
    public ObservableField<String> userName = new ObservableField<>("");
    //密码的绑定
    public ObservableField<String> password = new ObservableField<>("");
    //密码的焦点
    public ObservableField<Boolean> passwordFocus = new ObservableField<>(false);
    //是否同意协议
    public boolean agreeProtect = false;
    //发送验证码按钮绑定
    public ObservableField<String> verfyCodeText = new ObservableField<>("");
    //用户名清除按钮的显示隐藏绑定
    public ObservableInt clearBtnVisibility = new ObservableInt();
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    private String smsToken="";

    private final String PRIVATE_PASSWORD="myzrapp";
    private final String PRIVATE_TYPE_REGISTER="1";
    private final String PRIVATE_TYPE_LOGIN="2";

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.cb_login_useragreement:
                agreeProtect=isChecked;
                break;
            default:
                break;
        }
    }

    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();
    }

    public LoginViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
        //从本地取得数据绑定到View层
        userName.set(model.getUserName());
        password.set(model.getPassword());
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
                passwordFocus.set(true);
                passwordFocus.notifyChange();
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
    //登录按钮的点击事件
    public BindingCommand loginOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            login();
        }
    });
    //注册新用户按钮的点击事件
    public BindingCommand registerOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(RegisterActivity.class);
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

    private void getVersionCode(){
        if (TextUtils.isEmpty(userName.get())|| !RxRegTool.isMobileSimple(userName.get())) {
            ToastUtils.showShort("请输入正确的手机号！");
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
                        smsToken=entity.getData();
                        model.saveSmsToken(entity.getData());
                    }
                }));
    }
    /**
     * 网络登陆操作
     **/
    private void login() {
        if(!agreeProtect){
            ToastUtils.showShort("请先阅读并同意服务条款！");
            return;
        }
        if (TextUtils.isEmpty(userName.get())||!RxRegTool.isMobileSimple(userName.get())) {
            ToastUtils.showShort("请输入正确的手机号！");
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtils.showShort("请输入验证码！");
            return;
        }
        //RaJava登录
        addSubscribe(model.loginUser(new LoginBodyEntity(model.getSmsToken(),userName.get(),password.get(),PRIVATE_TYPE_REGISTER))
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new Consumer<RegisterOrLoginResponseEntity>() {
                    @Override
                    public void accept(RegisterOrLoginResponseEntity entity) throws Exception {
                        dismissDialog();
                        Log.e(TAG,"getStatus is:"+entity);
                        //保存Token
                      //  smsToken=entity.getData();
                        if(entity.getStatus()== HttpStatus.STATUS_CODE_SUCESS){
                            model.saveToken(entity.getRegisterResponseDataEntity().getToken());
                            model.saveUserID(entity.getRegisterResponseDataEntity().getUserId());
                            startActivity(DeviceListActivity.class);
                            finish();
                        }else{
                            dialogEvent.set(entity.getMessage());
                            dialogEvent.notifyChange();
                        }

                    }
                }));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopVerifyTimer();
    }
}
