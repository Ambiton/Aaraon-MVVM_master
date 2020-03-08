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
import com.goldze.mvvmhabit.entity.DeviceStatusInfoEntity;
import com.goldze.mvvmhabit.ui.base.viewmodel.ToolbarViewModel;
import com.goldze.mvvmhabit.utils.BleOption;
import com.goldze.mvvmhabit.utils.RxDataTool;
import com.inuker.bluetooth.library.Code;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleReadRssiResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
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
            for(int i=0;i<6;i++){
                Log.e(TAG, "getNotifyData,data  "+(i+1)+ "is"+ value[i]);
            }
            DeviceStatusInfoEntity recDeviceStatusInfoEntity=new DeviceStatusInfoEntity(value);
            if (deviceStatusInfoEntity.getIsHeatingOpen() != recDeviceStatusInfoEntity.getIsHeatingOpen() ||
                    deviceStatusInfoEntity.getIsDeviceOpen() != recDeviceStatusInfoEntity.getIsDeviceOpen() ||
                    deviceStatusInfoEntity.getDeviceRoation() != recDeviceStatusInfoEntity.getDeviceRoation() ||
                    deviceStatusInfoEntity.getDeviceSpeed() != recDeviceStatusInfoEntity.getDeviceSpeed() ||
                    deviceStatusInfoEntity.getDeviceVoice() != recDeviceStatusInfoEntity.getDeviceVoice() ||
                    deviceStatusInfoEntity.getDeviceVoiceSwitch() != recDeviceStatusInfoEntity.getDeviceVoiceSwitch()) {
                deviceStatusInfoEntity = recDeviceStatusInfoEntity;
                statusInfoChageObeserver.set(deviceStatusInfoEntity);
                statusInfoChageObeserver.notifyChange();
            }

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
        //statusInfoChageObeserver.notifyChange();
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
        //BleOption.getInstance().startGetDeviceInfo(DeviceControlViewModel.this);
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
    public BindingCommand onPowSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            Boolean aBoolean=!(deviceStatusInfoEntity.getIsDeviceOpen()==DeviceStatusInfoEntity.FLAG_TRUE);
            uc.powerSwitch.setValue(aBoolean);
            deviceStatusInfoEntity.setIsDeviceOpen(aBoolean?DeviceStatusInfoEntity.FLAG_TRUE:DeviceStatusInfoEntity.FLAG_FALSE);
            if (aBoolean) {
                BleOption.getInstance().turnOnDevice(DeviceControlViewModel.this);
            } else {
                BleOption.getInstance().turnOffDevice(DeviceControlViewModel.this);
            }

            statusInfoChageObeserver.set(deviceStatusInfoEntity);
            statusInfoChageObeserver.notifyChange();
        }
    });
    //加热开关
    public BindingCommand onWarmSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            Boolean aBoolean = !(deviceStatusInfoEntity.getIsHeatingOpen() == DeviceStatusInfoEntity.FLAG_TRUE);
            deviceStatusInfoEntity.setIsHeatingOpen(aBoolean ? DeviceStatusInfoEntity.FLAG_TRUE : DeviceStatusInfoEntity.FLAG_FALSE);
            if (aBoolean) {
                BleOption.getInstance().turnOnHeating(DeviceControlViewModel.this);
            } else {
                BleOption.getInstance().turnOffHeating(DeviceControlViewModel.this);
            }
            statusInfoChageObeserver.set(deviceStatusInfoEntity);
            statusInfoChageObeserver.notifyChange();
        }
    });

    //高速开关
    public BindingCommand onHighSpeeSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            deviceStatusInfoEntity.setDeviceSpeed(DeviceStatusInfoEntity.FLAG_SPEED_MAX);
            Log.e(TAG, "getIsSpeedHigh is " +  statusInfoChageObeserver.get().getDeviceSpeed());
            BleOption.getInstance().setMaxSpeed(DeviceControlViewModel.this);
            statusInfoChageObeserver.set(deviceStatusInfoEntity);
            statusInfoChageObeserver.notifyChange();
        }
    });
    //中速开关
    public BindingCommand onMidSpeeSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.midSpeedSwitch.call();
            deviceStatusInfoEntity.setDeviceSpeed(DeviceStatusInfoEntity.FLAG_SPEED_MID);
            statusInfoChageObeserver.set(deviceStatusInfoEntity);
            statusInfoChageObeserver.notifyChange();
            BleOption.getInstance().setMidSpeed(DeviceControlViewModel.this);
        }
    });
    //低速开关
    public BindingCommand onLowSpeeSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            deviceStatusInfoEntity.setDeviceSpeed(DeviceStatusInfoEntity.FLAG_SPEED_MIN);
            statusInfoChageObeserver.set(deviceStatusInfoEntity);
            statusInfoChageObeserver.notifyChange();
            BleOption.getInstance().setMinSpeed(DeviceControlViewModel.this);
        }
    });
    //静音开关
    public BindingCommand onMuteVolSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.muteVolSwitch.call();
            deviceStatusInfoEntity.setDeviceVoice(DeviceStatusInfoEntity.FLAG_VOICE_MUTE);
            statusInfoChageObeserver.set(deviceStatusInfoEntity);
            statusInfoChageObeserver.notifyChange();
            BleOption.getInstance().setSilentVoice(DeviceControlViewModel.this);
        }
    });
    //低音开关
    public BindingCommand onLowVolSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.lowVolSwitch.call();
            deviceStatusInfoEntity.setDeviceVoice(DeviceStatusInfoEntity.FLAG_VOICE_MIN);
            statusInfoChageObeserver.set(deviceStatusInfoEntity);
            statusInfoChageObeserver.notifyChange();
            BleOption.getInstance().setMinVoice(DeviceControlViewModel.this);
        }
    });
    //中音开关
    public BindingCommand onMidVolSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.midVolSwitch.call();
            deviceStatusInfoEntity.setDeviceVoice(DeviceStatusInfoEntity.FLAG_VOICE_MID);
            statusInfoChageObeserver.set(deviceStatusInfoEntity);
            statusInfoChageObeserver.notifyChange();
            BleOption.getInstance().setMidVoice(DeviceControlViewModel.this);
        }
    });
    //高音开关
    public BindingCommand onHighVolSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.highVolSwitch.call();
            deviceStatusInfoEntity.setDeviceVoice(DeviceStatusInfoEntity.FLAG_VOICE_MAX);
            statusInfoChageObeserver.set(deviceStatusInfoEntity);
            statusInfoChageObeserver.notifyChange();
            BleOption.getInstance().setMaxVoice(DeviceControlViewModel.this);
        }
    });
    //自动转
    public BindingCommand onRoationAutoCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            deviceStatusInfoEntity.setDeviceRoation(DeviceStatusInfoEntity.FLAG_ROATION_AUTO);
            statusInfoChageObeserver.set(deviceStatusInfoEntity);
            statusInfoChageObeserver.notifyChange();
            BleOption.getInstance().roationAuto(DeviceControlViewModel.this);
        }
    });
    //正转
    public BindingCommand onRoationPositiveCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            deviceStatusInfoEntity.setDeviceRoation(DeviceStatusInfoEntity.FLAG_ROATION_POSISTION);
            statusInfoChageObeserver.set(deviceStatusInfoEntity);
            statusInfoChageObeserver.notifyChange();
            BleOption.getInstance().roationPositive(DeviceControlViewModel.this);
        }
    });
    //反转
    public BindingCommand onRoationReversalCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            deviceStatusInfoEntity.setDeviceRoation(DeviceStatusInfoEntity.FLAG_ROATION_REV);
            statusInfoChageObeserver.set(deviceStatusInfoEntity);
            statusInfoChageObeserver.notifyChange();
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
        BleOption.getInstance().startGetDeviceInfo(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        BleOption.getInstance().stopGetDeviceInfo();
    }
}
