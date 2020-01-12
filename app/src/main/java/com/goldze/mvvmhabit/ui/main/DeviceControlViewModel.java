package com.goldze.mvvmhabit.ui.main;

import android.app.Application;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.data.DemoRepository;
import com.goldze.mvvmhabit.entity.DemoEntity;
import com.goldze.mvvmhabit.ui.base.viewmodel.ToolbarViewModel;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.http.BaseResponse;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by goldze on 2017/7/17.
 */

public class DeviceControlViewModel extends ToolbarViewModel<DemoRepository> {
    //监听蓝牙接收的数据变化
    public ObservableField<DemoEntity> dataChageObeserver = new ObservableField();
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {

        //下拉刷新完成
        public SingleLiveEvent finishRefreshing = new SingleLiveEvent<>();
        //上拉加载完成
        public SingleLiveEvent finishLoadmore = new SingleLiveEvent<>();
        //电源开关
        public SingleLiveEvent<Boolean> powerSwitch = new SingleLiveEvent<>();
        //加热开关
        public SingleLiveEvent<Boolean> warmSwitch = new SingleLiveEvent<>();
        //高速
        public SingleLiveEvent highSpeedSwitch = new SingleLiveEvent<>();
        //中速
        public SingleLiveEvent midSpeedSwitch = new SingleLiveEvent<>();
        //低速
        public SingleLiveEvent lowSpeedSwitch = new SingleLiveEvent<>();
        //高音
        public SingleLiveEvent highVolSwitch = new SingleLiveEvent<>();
        //中音
        public SingleLiveEvent midVolSwitch = new SingleLiveEvent<>();
        //低音
        public SingleLiveEvent lowVolSwitch = new SingleLiveEvent<>();
        //静音
        public SingleLiveEvent muteVolSwitch = new SingleLiveEvent<>();
    }

    public DeviceControlViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
    }
    /**
     * 初始化Toolbar
     */
    public void initToolbar() {
        //初始化标题栏
        setRightIcon(R.mipmap.applauncher);
        setRightIconVisible(View.GONE);
        setRightTextVisible(View.GONE);
        setTitleText(getApplication().getString(R.string.devicelist_title_devicelist));
        uc.powerSwitch.setValue(false);
        uc.warmSwitch.setValue(false);
    }

    @Override
    public void rightTextOnClick() {
        ToastUtils.showShort("更多");
    }
    public ObservableList<DeviceListItemViewModel> observableBindedList = new ObservableArrayList<>();
    //给RecyclerView添加ObservableList
    public ObservableList<DeviceListItemViewModel> observableList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<DeviceListItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_devicelist);
    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtils.showShort("下拉刷新");
            requestDeviceInfo();

        }
    });
    //电源开关
    public BindingCommand<Boolean> onPowSwitchChangeCommand = new BindingCommand<>(new BindingConsumer<Boolean>() {
        @Override
        public void call(Boolean aBoolean) {
            uc.powerSwitch.setValue(aBoolean);
        }
    });
    //加热开关
    public BindingCommand onWarmSwitchChangeCommand = new BindingCommand<>(new BindingConsumer<Boolean>() {
        @Override
        public void call(Boolean aBoolean) {
            uc.warmSwitch.setValue(aBoolean);
        }
    });
    //高速开关
    public BindingCommand onHighSpeeSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.highSpeedSwitch.call();
        }
    });
    //中速开关
    public BindingCommand onMidSpeeSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.midSpeedSwitch.call();
        }
    });
    //低速开关
    public BindingCommand onLowSpeeSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.lowSpeedSwitch.call();
        }
    });
    //静音开关
    public BindingCommand onMuteVolSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.muteVolSwitch.call();
        }
    });
    //低音开关
    public BindingCommand onLowVolSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.lowVolSwitch.call();
        }
    });
    //中音开关
    public BindingCommand onMidVolSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.midVolSwitch.call();
        }
    });
    //高音开关
    public BindingCommand onHighVolSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.highVolSwitch.call();
        }
    });
    //上拉加载
    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (observableList.size() > 50) {
                ToastUtils.showLong("兄dei，你太无聊啦~崩是不可能的~");
                return;
            }
        }
    });

    /**
     * 设备信息请求方法，在ViewModel中调用Model层，通过Okhttp+Retrofit+RxJava发起请求
     */
    public void requestDeviceInfo() {
        //可以调用addSubscribe()添加Disposable，请求与View周期同步
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
