package com.goldze.mvvmhabit.ui.main;

import android.app.Application;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.data.DemoRepository;
import com.goldze.mvvmhabit.entity.DemoEntity;
import com.goldze.mvvmhabit.entity.DeviceStatusInfoEntity;
import com.goldze.mvvmhabit.ui.base.viewmodel.ToolbarViewModel;
import com.goldze.mvvmhabit.utils.BleOption;
import com.goldze.mvvmhabit.utils.DataConvertTools;
import com.goldze.mvvmhabit.utils.RxDataTool;
import com.goldze.mvvmhabit.utils.heatbeat.HeatBleOption;
import com.inuker.bluetooth.library.Code;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleReadRssiResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.ArrayList;
import java.util.UUID;

import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by goldze on 2017/7/17.
 */

public class DeviceControlViewModel extends ToolbarViewModel<DemoRepository> implements BleNotifyResponse, BleReadRssiResponse, BleWriteResponse {
    private static final String TAG = "DeviceControlViewModel";
    //监听蓝牙接收的数据变化
    public ObservableField<DeviceStatusInfoEntity> statusInfoChageObeserver = new ObservableField();
    public ObservableField<String> testStr = new ObservableField("test");
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    private DeviceStatusInfoEntity deviceStatusInfoEntity=new DeviceStatusInfoEntity();

    @Override
    public void onNotify(UUID service, UUID character, byte[] value) {
        Log.e(TAG, "data  length is " + value.length);
        Log.e(TAG, "heat value is " + (value[3]+value[4]));
        Log.e(TAG, "onNotify UUID is " + character + ",data is " + RxDataTool.bytes2HexString(value));
        if(value.length==6){
            uc.powerSwitch.setValue(value[0]==1);
            uc.warmSwitch.setValue(value[1]==1);
            uc.highSpeedSwitch.setValue(value[2]==2);
            uc.midSpeedSwitch.setValue(value[2]==1);
            uc.lowSpeedSwitch.setValue(value[2]==0);
            uc.highVolSwitch.setValue(value[3]==25);
            uc.midVolSwitch.setValue(value[3]==20);
            uc.lowVolSwitch.setValue(value[3]==15);
            uc.muteVolSwitch.setValue(value[3]==0);
        }

    }

    @Override
    public void onResponse(int code) {
        if (code == Code.REQUEST_SUCCESS) {
            Log.e(TAG, "onWrite  sucess");
        } else {
            Log.e(TAG, "onWrite  failed");
        }
    }

    @Override
    public void onResponse(int code, Integer data) {
        if (code == Code.REQUEST_SUCCESS) {
            Log.e(TAG, "onResponse rssi sucess,rssi is " + data);
        } else {
            Log.e(TAG, "onResponse rssi is failed");
        }
    }


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
        statusInfoChageObeserver.set(deviceStatusInfoEntity);
    }

    /**
     * 初始化Toolbar
     */
    public void initToolbar() {
        //初始化标题栏
        setRightIcon(R.mipmap.applauncher);
        setRightIconVisible(View.GONE);
        setRightTextVisible(View.GONE);
        setTitleText(getApplication().getString(R.string.devicelist_title_mydevice));
        uc.powerSwitch.setValue(false);
        uc.warmSwitch.setValue(false);
        BleOption.getInstance().getDeviceInfo(DeviceControlViewModel.this);
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
            //uc.powerSwitch.setValue(aBoolean);

            if (aBoolean) {
                statusInfoChageObeserver.get().setIsDeviceOpen(DeviceStatusInfoEntity.FLAG_TRUE);
                BleOption.getInstance().turnOnDevice(DeviceControlViewModel.this);
            } else {
                statusInfoChageObeserver.get().setIsDeviceOpen(DeviceStatusInfoEntity.FLAG_FALSE);
                BleOption.getInstance().turnOffDevice(DeviceControlViewModel.this);
            }

        }
    });
    //加热开关
    public BindingCommand onWarmSwitchChangeCommand = new BindingCommand<>(new BindingConsumer<Boolean>() {
        @Override
        public void call(Boolean aBoolean) {
            uc.warmSwitch.setValue(aBoolean);
            if (aBoolean) {
                statusInfoChageObeserver.get().setIsHeatingOpen(DeviceStatusInfoEntity.FLAG_TRUE);
                BleOption.getInstance().turnOnHeating(DeviceControlViewModel.this);
            } else {
                statusInfoChageObeserver.get().setIsHeatingOpen(DeviceStatusInfoEntity.FLAG_FALSE);
                BleOption.getInstance().turnOffHeating(DeviceControlViewModel.this);
            }
        }
    });
    //高速开关
    public BindingCommand onHighSpeeSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.highSpeedSwitch.call();
            deviceStatusInfoEntity.setIsSpeedHigh(DeviceStatusInfoEntity.FLAG_TRUE);
            deviceStatusInfoEntity.setIsSpeedMid(DeviceStatusInfoEntity.FLAG_FALSE);
            deviceStatusInfoEntity.setIsSpeedLow(DeviceStatusInfoEntity.FLAG_FALSE);
            testStr.set("high");
            statusInfoChageObeserver.set(deviceStatusInfoEntity);
            Log.e(TAG, "getIsSpeedHigh is " +  statusInfoChageObeserver.get().getIsSpeedHigh());
            BleOption.getInstance().setMaxSpeed(DeviceControlViewModel.this);
        }
    });
    //中速开关
    public BindingCommand onMidSpeeSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.midSpeedSwitch.call();
            deviceStatusInfoEntity.setIsSpeedHigh(DeviceStatusInfoEntity.FLAG_FALSE);
            deviceStatusInfoEntity.setIsSpeedMid(DeviceStatusInfoEntity.FLAG_TRUE);
            deviceStatusInfoEntity.setIsSpeedLow(DeviceStatusInfoEntity.FLAG_FALSE);
            statusInfoChageObeserver.set(deviceStatusInfoEntity);
            BleOption.getInstance().setMidSpeed(DeviceControlViewModel.this);
        }
    });
    //低速开关
    public BindingCommand onLowSpeeSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.lowSpeedSwitch.call();
            statusInfoChageObeserver.get().setIsSpeedHigh(DeviceStatusInfoEntity.FLAG_FALSE);
            statusInfoChageObeserver.get().setIsSpeedMid(DeviceStatusInfoEntity.FLAG_FALSE);
            statusInfoChageObeserver.get().setIsSpeedLow(DeviceStatusInfoEntity.FLAG_TRUE);
            BleOption.getInstance().setMinSpeed(DeviceControlViewModel.this);
        }
    });
    //静音开关
    public BindingCommand onMuteVolSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.muteVolSwitch.call();
            statusInfoChageObeserver.get().setIsVoiceMute(DeviceStatusInfoEntity.FLAG_TRUE);
            BleOption.getInstance().reduceVoice(DeviceControlViewModel.this);
            BleOption.getInstance().setSilentVoice(DeviceControlViewModel.this);
        }
    });
    //低音开关
    public BindingCommand onLowVolSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.lowVolSwitch.call();
            statusInfoChageObeserver.get().setIsVoiceHigh(DeviceStatusInfoEntity.FLAG_FALSE);
            statusInfoChageObeserver.get().setIsVoiceMid(DeviceStatusInfoEntity.FLAG_FALSE);
            statusInfoChageObeserver.get().setIsVoiceLow(DeviceStatusInfoEntity.FLAG_TRUE);
            BleOption.getInstance().setMinVoice(DeviceControlViewModel.this);
        }
    });
    //中音开关
    public BindingCommand onMidVolSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.midVolSwitch.call();
            statusInfoChageObeserver.get().setIsVoiceHigh(DeviceStatusInfoEntity.FLAG_FALSE);
            statusInfoChageObeserver.get().setIsVoiceMid(DeviceStatusInfoEntity.FLAG_TRUE);
            statusInfoChageObeserver.get().setIsVoiceLow(DeviceStatusInfoEntity.FLAG_FALSE);
            BleOption.getInstance().setMidVoice(DeviceControlViewModel.this);
        }
    });
    //高音开关
    public BindingCommand onHighVolSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.highVolSwitch.call();
            statusInfoChageObeserver.get().setIsVoiceHigh(DeviceStatusInfoEntity.FLAG_TRUE);
            statusInfoChageObeserver.get().setIsVoiceMid(DeviceStatusInfoEntity.FLAG_FALSE);
            statusInfoChageObeserver.get().setIsVoiceLow(DeviceStatusInfoEntity.FLAG_FALSE);
            BleOption.getInstance().setMaxVoice(DeviceControlViewModel.this);
        }
    });
    //自动转
    public BindingCommand onRoationAutoCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            statusInfoChageObeserver.get().setIsRoationAuto(DeviceStatusInfoEntity.FLAG_TRUE);
            statusInfoChageObeserver.get().setIsRoationPositive(DeviceStatusInfoEntity.FLAG_FALSE);
            statusInfoChageObeserver.get().setIsRoationReversal(DeviceStatusInfoEntity.FLAG_FALSE);
            BleOption.getInstance().roationAuto(DeviceControlViewModel.this);
        }
    });
    //正转
    public BindingCommand onRoationPositiveCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            statusInfoChageObeserver.get().setIsRoationAuto(DeviceStatusInfoEntity.FLAG_FALSE);
            statusInfoChageObeserver.get().setIsRoationPositive(DeviceStatusInfoEntity.FLAG_TRUE);
            statusInfoChageObeserver.get().setIsRoationReversal(DeviceStatusInfoEntity.FLAG_FALSE);
            BleOption.getInstance().roationPositive(DeviceControlViewModel.this);
        }
    });
    //反转
    public BindingCommand onRoationReversalCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            statusInfoChageObeserver.get().setIsRoationAuto(DeviceStatusInfoEntity.FLAG_FALSE);
            statusInfoChageObeserver.get().setIsRoationPositive(DeviceStatusInfoEntity.FLAG_FALSE);
            statusInfoChageObeserver.get().setIsRoationReversal(DeviceStatusInfoEntity.FLAG_TRUE);
            BleOption.getInstance().roationReversal(DeviceControlViewModel.this);
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

    public void initBleEvent() {
        BleOption.getInstance().getNotifyData(this);
        BleOption.getInstance().getRssiData(this);
        BleOption.getInstance().getReadData(new BleReadResponse() {
            @Override
            public void onResponse(int code, byte[] data) {
                if (code == Code.REQUEST_SUCCESS) {
                    Log.e(TAG, "onRead is sucess,data is " + RxDataTool.bytes2HexString(data));
                }
            }
        });
    }

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
