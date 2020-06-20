package com.myzr.allproducts.ui.main;

import android.app.Application;

import com.myzr.allproducts.BR;
import com.myzr.allproducts.R;
import com.myzr.allproducts.app.AppApplication;
import com.myzr.allproducts.data.DemoRepository;
import com.myzr.allproducts.entity.DeviceStatusInfoEntity;
import com.myzr.allproducts.entity.db.UserActionData;
import com.myzr.allproducts.ui.base.viewmodel.ToolbarViewModel;
import com.myzr.allproducts.utils.AppTools;
import com.myzr.allproducts.utils.BleOption;
import com.myzr.allproducts.utils.RxDataTool;
import com.google.gson.Gson;
import com.inuker.bluetooth.library.Code;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleReadRssiResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.tamsiree.rxtool.RxLogTool;
import com.tamsiree.rxtool.RxTimeTool;
import com.tamsiree.rxtool.RxTool;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by goldze on 2017/7/17.
 */

public class AllDeviceControlViewModel extends ToolbarViewModel<DemoRepository> implements BleNotifyResponse, BleReadRssiResponse, BleWriteResponse {
    private static final String TAG = "AllDeviceControlViewModel";
    private static final long WAIT_FOR_USEROPTION = 1 * 1000;
    //监听蓝牙接收的数据变化
    public ObservableField<DeviceStatusInfoEntity> statusInfoChageObeserver = new ObservableField();
    public ObservableField<DeviceStatusInfoEntity> statusRoationModeUserChageObeserver = new ObservableField();
    public ObservableField<Boolean> writeDataResult = new ObservableField();
    public ObservableField<Boolean> isTrunOff = new ObservableField(false);
    public ObservableField<String> testStr = new ObservableField("test");
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();
    private AtomicBoolean isGifNeedChange=new AtomicBoolean(false);//GIF 资源是否需要切换

    private DeviceStatusInfoEntity deviceStatusInfoEntity = new DeviceStatusInfoEntity();

    private UserActionData userActionData=null;

    private long latestUserOptionTime;

    @Override
    public void onNotify(UUID service, UUID character, byte[] value) {
        if(isTrunOff.get()){
            RxLogTool.e(TAG, "isTrunOff,,,, " );
            return;
        }
        RxLogTool.e(TAG, "data  length is " + value.length);
        RxLogTool.e(TAG, "onNotify UUID is " + character + ",data is " + RxDataTool.bytes2HexString(value));
        if (value.length == 6 || value.length == 10) {
            //dismissDialog();
            DeviceStatusInfoEntity recDeviceStatusInfoEntity = new DeviceStatusInfoEntity(value);
            Gson gson = new Gson();
            String jsonDevStatus = gson.toJson(recDeviceStatusInfoEntity);
            if(BleOption.getInstance().isCurrentSetReturn(recDeviceStatusInfoEntity)){
                BleOption.getInstance().clearLatestWriteCommondAndFlag();
                if(userActionData!=null){
                    userActionData.setDevStatus(jsonDevStatus);
                    model.saveUserActionDataToDB(userActionData);
                    userActionData=null;
                }
            }
//            if( deviceStatusInfoEntity.getDeviceRoationMode() == recDeviceStatusInfoEntity.getDeviceRoationMode()){
//                updateDeviceRoationDirect(recDeviceStatusInfoEntity,deviceStatusInfoEntity.getDeviceRoationDirect());
//            }
            RxLogTool.e(TAG, "onNotify roationDirect is " + recDeviceStatusInfoEntity.getDeviceRoationDirect());
            setIsGifNeedChange(recDeviceStatusInfoEntity);
            if(!isGetInfoNeedShow()){
                RxLogTool.e(TAG, "isGetInfoNeedShow donotneed show " );
                return;
            }

            if (deviceStatusInfoEntity.getIsHeatingOpen() != recDeviceStatusInfoEntity.getIsHeatingOpen() ||
                    deviceStatusInfoEntity.getIsDeviceOpen() != recDeviceStatusInfoEntity.getIsDeviceOpen() ||
                    deviceStatusInfoEntity.getDeviceRoationMode() != recDeviceStatusInfoEntity.getDeviceRoationMode() ||
                    deviceStatusInfoEntity.getDeviceSpeed() != recDeviceStatusInfoEntity.getDeviceSpeed() ||
                    deviceStatusInfoEntity.getDeviceVoice() != recDeviceStatusInfoEntity.getDeviceVoice() ||
                    deviceStatusInfoEntity.getDeviceVoiceSwitch() != recDeviceStatusInfoEntity.getDeviceVoiceSwitch()||
                    deviceStatusInfoEntity.getDeviceRoationDirect() != recDeviceStatusInfoEntity.getDeviceRoationDirect()) {
                deviceStatusInfoEntity = recDeviceStatusInfoEntity;
                statusInfoChageObeserver.set(deviceStatusInfoEntity);
                statusInfoChageObeserver.notifyChange();
            }

        }

    }

    /**
     * 刚切换为自动模式状态下可能存在转动方向为停止的情况，这种情况改成切换前的转动方向，没有切换前的记录则默认正转动
     * @param entity
     */
    private void updateDeviceRoationDirect(DeviceStatusInfoEntity entity,byte preDirectionDirect){
        if((entity.getDeviceRoationMode()==DeviceStatusInfoEntity.FLAG_ROATION_AUTO||
                entity.getDeviceRoationMode()==DeviceStatusInfoEntity.FLAG_ROATION_POSISTION_AND_REV)&&
                (entity.getDeviceRoationDirect()==DeviceStatusInfoEntity.FLAG_ROATION_DIRECT_STOP0||
                        entity.getDeviceRoationDirect()==DeviceStatusInfoEntity.FLAG_ROATION_DIRECT_STOP3)){
            entity.setDeviceRoationDirect(preDirectionDirect==DeviceStatusInfoEntity.FLAG_ROATION_DIRECT_REV?
                    DeviceStatusInfoEntity.FLAG_ROATION_DIRECT_REV:DeviceStatusInfoEntity.FLAG_ROATION_DIRECT_POSISION);
        }
    }

    private void setUserActionData(String actFlag,String actVal){
        userActionData=new UserActionData();
        userActionData.setActFlag(actFlag);
        userActionData.setActVal(actVal);
        userActionData.setActTime(RxTimeTool.getCurTimeString());
        userActionData.setUnitId(model.getUnitID());
        userActionData.setStatus("0");
        userActionData.setSeriNo(AppTools.CUREENT_SERIONUM);
    }
    public boolean isGifNeedChange(){
        return isGifNeedChange.get();
    }

    private void setIsGifNeedChange(boolean isTure){
        isGifNeedChange.compareAndSet(!isTure, isTure);
    }

    private void setLatestUserOptionTime(){
        latestUserOptionTime=System.currentTimeMillis();
    }

    private boolean isGetInfoNeedShow() {
        long currentTime = System.currentTimeMillis();
        return currentTime - latestUserOptionTime >= WAIT_FOR_USEROPTION;
    }


    private void setIsGifNeedChange(DeviceStatusInfoEntity recDeviceStatusInfoEntity) {
        if (recDeviceStatusInfoEntity == null) {
            isGifNeedChange.compareAndSet(true, false);
            return;
        }
        if (deviceStatusInfoEntity.getIsHeatingOpen() != recDeviceStatusInfoEntity.getIsHeatingOpen() ||
                deviceStatusInfoEntity.getIsDeviceOpen() != recDeviceStatusInfoEntity.getIsDeviceOpen() ||
                deviceStatusInfoEntity.getDeviceRoationMode() != recDeviceStatusInfoEntity.getDeviceRoationMode() ||
                deviceStatusInfoEntity.getDeviceSpeed() != recDeviceStatusInfoEntity.getDeviceSpeed() ||
                deviceStatusInfoEntity.getDeviceRoationDirect() != recDeviceStatusInfoEntity.getDeviceRoationDirect()) {
            isGifNeedChange.compareAndSet(false, true);
        } else {
            isGifNeedChange.compareAndSet(true, false);
        }

    }

    public boolean isFastOption() {
        boolean result = RxTool.isFastClick(1000)&& BleOption.getInstance().getResendTimeCount()!=0;
        if (result) {
            ToastUtils.showShort("请不要过快频繁的操作～");
            RxLogTool.e(TAG,"isFastOption..."+BleOption.getInstance().getResendTimeCount());
        }
        return result;
    }

    @Override
    public void onResponse(int code) {
        if (code == Code.REQUEST_SUCCESS) {
            RxLogTool.e(TAG, "onWrite  sucess");
            writeDataResult.set(true);
        } else if (code == BleOption.REQUEST_TIMEOUT) {
            writeDataResult.set(false);
            BleOption.getInstance().getDeviceInfo(this);
            writeDataResult.notifyChange();
        } else {
            RxLogTool.e(TAG, "onWrite  failed");
        }

    }

    @Override
    public void onResponse(int code, Integer data) {
        if (code == Code.REQUEST_SUCCESS) {
            RxLogTool.e(TAG, "onResponse rssi sucess,rssi is " + data);
        } else {
            RxLogTool.e(TAG, "onResponse rssi is failed");
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
    }

    public AllDeviceControlViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
        statusInfoChageObeserver.set(deviceStatusInfoEntity);
        statusInfoChageObeserver.notifyChange();
    }


    public boolean isDeviceCanChangeSpeed() {
        boolean result = (deviceStatusInfoEntity.getDeviceRoationMode() != DeviceStatusInfoEntity.FLAG_ROATION_AUTO);
        if (!result) {
            ToastUtils.showLong("自动模式下无法进行手动调速，如要调速请切换到其他模式");
        }
        return result;
    }

    public String getProductName(String batchCode){
        return model.getProductname(batchCode);
    }
    /**
     * 初始化Toolbar
     */
    public void initToolbar(String batchCode) {
        //初始化标题栏

        uc.powerSwitch.setValue(false);
        uc.warmSwitch.setValue(false);
        //BleOption.getInstance().startTimerGetDeviceInfo(DeviceControlViewModel.this);
    }

    public int getBannerPlayIndex() {
        return model.getBannerPlayIndex();
    }

    public String getBannerPlayMode() {
        return model.getBannerPlayMode();
    }

    public void saveBannerPlayIndex(int playIndex) {
        model.saveBannerPlayIndex(playIndex);
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
//            ToastUtils.showShort("下拉刷新");
            requestDeviceInfo();

        }
    });

    public void setPowerSwitchStatus(Boolean aBoolean) {
        showWaitingDialog();
        deviceStatusInfoEntity.setIsDeviceOpen(aBoolean ? DeviceStatusInfoEntity.FLAG_TRUE : DeviceStatusInfoEntity.FLAG_FALSE);
        RxLogTool.e("setPowerSwitchStatus is "+aBoolean);
        if (aBoolean) {
            isTrunOff.set(false);
            BleOption.getInstance().turnOnDevice(AllDeviceControlViewModel.this);
        } else {
            isTrunOff.set(true);
            deviceStatusInfoEntity.setDeviceRoationMode(DeviceStatusInfoEntity.FLAG_ROATION_AUTO);
            deviceStatusInfoEntity.setIsHeatingOpen(DeviceStatusInfoEntity.FLAG_FALSE);
            deviceStatusInfoEntity.setDeviceSpeed(DeviceStatusInfoEntity.FLAG_SPEED_MIN);
            BleOption.getInstance().turnOffDevice(AllDeviceControlViewModel.this);
        }
        setUserActionData(AppTools.UserActionFlagAndValue.FLAG_SWITCH_DEVICE,String.valueOf(deviceStatusInfoEntity.getIsDeviceOpen()));
        setIsGifNeedChange(true);
        setLatestUserOptionTime();
        statusInfoChageObeserver.set(deviceStatusInfoEntity);
        statusInfoChageObeserver.notifyChange();
    }

    public void showWaitingDialog() {
        //showDialog("操作进行中，请稍后...");
//        BleOption.getInstance().startTimerGetDeviceInfo(this);
    }

    public void dissmissDialog() {
//        dismissDialog();
    }

    public void setWarmSwitchStatus(Boolean aBoolean) {
        deviceStatusInfoEntity.setIsHeatingOpen(aBoolean ? DeviceStatusInfoEntity.FLAG_TRUE : DeviceStatusInfoEntity.FLAG_FALSE);
        if (aBoolean) {
            BleOption.getInstance().turnOnHeating(AllDeviceControlViewModel.this);
        } else {
            BleOption.getInstance().turnOffHeating(AllDeviceControlViewModel.this);
        }
        setUserActionData(AppTools.UserActionFlagAndValue.FLAG_SWITCH_WARM,String.valueOf(deviceStatusInfoEntity.getIsHeatingOpen()));
        setIsGifNeedChange(true);
        setLatestUserOptionTime();
        statusInfoChageObeserver.set(deviceStatusInfoEntity);
        statusInfoChageObeserver.notifyChange();
        showWaitingDialog();
    }

    //电源开关
    public BindingCommand onPowSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
//            Boolean aBoolean=!(deviceStatusInfoEntity.getIsDeviceOpen()==DeviceStatusInfoEntity.FLAG_TRUE);
//            uc.powerSwitch.setValue(aBoolean);
//            deviceStatusInfoEntity.setIsDeviceOpen(aBoolean?DeviceStatusInfoEntity.FLAG_TRUE:DeviceStatusInfoEntity.FLAG_FALSE);
//            if (aBoolean) {
//                BleOption.getInstance().turnOnDevice(DeviceControlViewModel.this);
//            } else {
//                BleOption.getInstance().turnOffDevice(DeviceControlViewModel.this);
//            }
//            statusInfoChageObeserver.set(deviceStatusInfoEntity);
//            statusInfoChageObeserver.notifyChange();
        }
    });
    //加热开关
    BindingCommand<Boolean> bindingAction=new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {

        }
    });
    public BindingCommand onWarmSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
//            Boolean aBoolean = !(deviceStatusInfoEntity.getIsHeatingOpen() == DeviceStatusInfoEntity.FLAG_TRUE);
//            deviceStatusInfoEntity.setIsHeatingOpen(aBoolean ? DeviceStatusInfoEntity.FLAG_TRUE : DeviceStatusInfoEntity.FLAG_FALSE);
//            if (aBoolean) {
//                BleOption.getInstance().turnOnHeating(DeviceControlViewModel.this);
//            } else {
//                BleOption.getInstance().turnOffHeating(DeviceControlViewModel.this);
//            }
//            statusInfoChageObeserver.set(deviceStatusInfoEntity);
//            statusInfoChageObeserver.notifyChange();
        }
    });

    //高速开关
    public BindingCommand onHighSpeeSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            if (!isFastOption() && isDeviceCanChangeSpeed()) {
                deviceStatusInfoEntity.setDeviceSpeed(DeviceStatusInfoEntity.FLAG_SPEED_MAX);
                RxLogTool.e(TAG, "getIsSpeedHigh is " + statusInfoChageObeserver.get().getDeviceSpeed());
                BleOption.getInstance().setMaxSpeed(AllDeviceControlViewModel.this);
                showWaitingDialog();
                setIsGifNeedChange(true);
                setLatestUserOptionTime();
                setUserActionData(AppTools.UserActionFlagAndValue.FLAG_SPEED_VALUE,String.valueOf(deviceStatusInfoEntity.getDeviceSpeed()));
                statusInfoChageObeserver.set(deviceStatusInfoEntity);
                statusInfoChageObeserver.notifyChange();
            }

        }
    });
    //中速开关
    public BindingCommand onMidSpeeSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            if (!isFastOption() && isDeviceCanChangeSpeed()) {
                uc.midSpeedSwitch.call();
                deviceStatusInfoEntity.setDeviceSpeed(DeviceStatusInfoEntity.FLAG_SPEED_MID);
                BleOption.getInstance().setMidSpeed(AllDeviceControlViewModel.this);
                showWaitingDialog();
                setIsGifNeedChange(true);
                setLatestUserOptionTime();
                setUserActionData(AppTools.UserActionFlagAndValue.FLAG_SPEED_VALUE,String.valueOf(deviceStatusInfoEntity.getDeviceSpeed()));
                statusInfoChageObeserver.set(deviceStatusInfoEntity);
                statusInfoChageObeserver.notifyChange();
            }

        }
    });
    //低速开关
    public BindingCommand onLowSpeeSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            if (!isFastOption() && isDeviceCanChangeSpeed()) {
                deviceStatusInfoEntity.setDeviceSpeed(DeviceStatusInfoEntity.FLAG_SPEED_MIN);
                BleOption.getInstance().setMinSpeed(AllDeviceControlViewModel.this);
                showWaitingDialog();
                setIsGifNeedChange(true);
                setLatestUserOptionTime();
                setUserActionData(AppTools.UserActionFlagAndValue.FLAG_SPEED_VALUE,String.valueOf(deviceStatusInfoEntity.getDeviceSpeed()));
                statusInfoChageObeserver.set(deviceStatusInfoEntity);
                statusInfoChageObeserver.notifyChange();
            }
        }
    });
    //静音开关
    public BindingCommand onMuteVolSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            if (!isFastOption()) {
                deviceStatusInfoEntity.setDeviceVoice(DeviceStatusInfoEntity.FLAG_VOICE_MUTE);
                BleOption.getInstance().setSilentVoice(AllDeviceControlViewModel.this);
                showWaitingDialog();
                setLatestUserOptionTime();
                setUserActionData(AppTools.UserActionFlagAndValue.FLAG_VOICE_VOLUME,String.valueOf(deviceStatusInfoEntity.getDeviceVoice()));
                statusInfoChageObeserver.set(deviceStatusInfoEntity);
                statusInfoChageObeserver.notifyChange();
            }
        }
    });
    //低音开关
    public BindingCommand onLowVolSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            if (!isFastOption()) {
                deviceStatusInfoEntity.setDeviceVoice(DeviceStatusInfoEntity.FLAG_VOICE_MIN);
                BleOption.getInstance().setMinVoice(AllDeviceControlViewModel.this);
                showWaitingDialog();
                setLatestUserOptionTime();
                setUserActionData(AppTools.UserActionFlagAndValue.FLAG_VOICE_VOLUME,String.valueOf(deviceStatusInfoEntity.getDeviceVoice()));
                statusInfoChageObeserver.set(deviceStatusInfoEntity);
                statusInfoChageObeserver.notifyChange();
            }

        }
    });
    //中音开关
    public BindingCommand onMidVolSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            if (!isFastOption()) {
                deviceStatusInfoEntity.setDeviceVoice(DeviceStatusInfoEntity.FLAG_VOICE_MID);
                BleOption.getInstance().setMidVoice(AllDeviceControlViewModel.this);
                showWaitingDialog();
                setLatestUserOptionTime();
                setUserActionData(AppTools.UserActionFlagAndValue.FLAG_VOICE_VOLUME,String.valueOf(deviceStatusInfoEntity.getDeviceVoice()));
                statusInfoChageObeserver.set(deviceStatusInfoEntity);
                statusInfoChageObeserver.notifyChange();
            }
        }
    });
    //高音开关
    public BindingCommand onHighVolSwitchChangeCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            if (!isFastOption()) {
                deviceStatusInfoEntity.setDeviceVoice(DeviceStatusInfoEntity.FLAG_VOICE_MAX);
                BleOption.getInstance().setMaxVoice(AllDeviceControlViewModel.this);
                showWaitingDialog();
                setLatestUserOptionTime();
                setUserActionData(AppTools.UserActionFlagAndValue.FLAG_VOICE_VOLUME,String.valueOf(deviceStatusInfoEntity.getDeviceVoice()));
                statusInfoChageObeserver.set(deviceStatusInfoEntity);
                statusInfoChageObeserver.notifyChange();
            }
        }
    });
    //自动转
    public BindingCommand onRoationAutoCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            if (!isFastOption()) {
                deviceStatusInfoEntity.setDeviceRoationMode(DeviceStatusInfoEntity.FLAG_ROATION_AUTO);
                BleOption.getInstance().roationAuto(AllDeviceControlViewModel.this);
                showWaitingDialog();
                setIsGifNeedChange(true);
                setLatestUserOptionTime();
                statusRoationModeUserChageObeserver.set(deviceStatusInfoEntity);
                statusRoationModeUserChageObeserver.notifyChange();
                setUserActionData(AppTools.UserActionFlagAndValue.FLAG_ROATION_MODE,String.valueOf(deviceStatusInfoEntity.getDeviceRoationMode()));
                statusInfoChageObeserver.set(deviceStatusInfoEntity);
                statusInfoChageObeserver.notifyChange();
            }
        }
    });
    //正转
    public BindingCommand onRoationPositiveCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            if (!isFastOption()) {
                deviceStatusInfoEntity.setDeviceRoationMode(DeviceStatusInfoEntity.FLAG_ROATION_POSISTION);
                BleOption.getInstance().roationPositive(AllDeviceControlViewModel.this);
                showWaitingDialog();
                setIsGifNeedChange(true);
                setLatestUserOptionTime();
                statusRoationModeUserChageObeserver.set(deviceStatusInfoEntity);
                statusRoationModeUserChageObeserver.notifyChange();
                setUserActionData(AppTools.UserActionFlagAndValue.FLAG_ROATION_MODE,String.valueOf(deviceStatusInfoEntity.getDeviceRoationMode()));
                statusInfoChageObeserver.set(deviceStatusInfoEntity);
                statusInfoChageObeserver.notifyChange();
            }
        }
    });
    //反转
    public BindingCommand onRoationReversalCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            if (!isFastOption()) {
                deviceStatusInfoEntity.setDeviceRoationMode(DeviceStatusInfoEntity.FLAG_ROATION_REV);
                BleOption.getInstance().roationReversal(AllDeviceControlViewModel.this);
                showWaitingDialog();
                setIsGifNeedChange(true);
                setLatestUserOptionTime();
                statusRoationModeUserChageObeserver.set(deviceStatusInfoEntity);
                statusRoationModeUserChageObeserver.notifyChange();
                setUserActionData(AppTools.UserActionFlagAndValue.FLAG_ROATION_MODE,String.valueOf(deviceStatusInfoEntity.getDeviceRoationMode()));
                statusInfoChageObeserver.set(deviceStatusInfoEntity);
                statusInfoChageObeserver.notifyChange();
            }
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
        BleOption.getInstance().initWriteDataEnv();
        BleOption.getInstance().getNotifyData(this);
        BleOption.getInstance().getRssiData(this);
        BleOption.getInstance().getReadData(new BleReadResponse() {
            @Override
            public void onResponse(int code, byte[] data) {
                if (code == Code.REQUEST_SUCCESS) {
                    RxLogTool.e(TAG, "onRead is sucess,data is " + RxDataTool.bytes2HexString(data));
                }
            }
        });
    }

    /**
     * 设备信息请求方法，在ViewModel中调用Model层，通过Okhttp+Retrofit+RxJava发起请求
     */
    public void requestDeviceInfo() {
        BleOption.getInstance().startTimerGetDeviceInfo(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        BleOption.getInstance().stopGetDeviceInfo();
        BleOption.getInstance().uninitWriteDataEnv();
        AppApplication.getBluetoothClient(this.getApplication()).disconnect(BleOption.getInstance().getMac());
    }
}
