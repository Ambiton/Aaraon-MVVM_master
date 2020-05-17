package com.goldze.mvvmhabit.ui.main;

import androidx.databinding.ObservableField;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.app.AppApplication;
import com.goldze.mvvmhabit.entity.BlutoothDeviceInfoEntity;
import com.goldze.mvvmhabit.entity.DeviceStatusInfoEntity;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateBodyEntity;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateResponseEntity;
import com.goldze.mvvmhabit.entity.http.productinfo.ProductInfoResponseDataEntity;
import com.goldze.mvvmhabit.utils.AppTools;
import com.goldze.mvvmhabit.utils.BleOption;
import com.goldze.mvvmhabit.utils.HttpsUtils;
import com.goldze.mvvmhabit.utils.RxDataTool;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.tamsiree.rxtool.RxLogTool;

import java.util.UUID;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.AppManager;
import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.deviceinterface.OnDeviceInfoListener;
import me.goldze.mvvmhabit.utils.MaterialDialogUtils;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.SERVICE_UNREADY;

/**
 * Created by goldze on 2017/7/17.
 */

public class DeviceListItemViewModel extends ItemViewModel<DeviceListViewModel> implements BleConnectResponse, OnDeviceInfoListener, BleNotifyResponse, BleWriteResponse {
    private static final String TAG = "DeviceListItemViewModel";
    private final static int MAX_CONNECTETIMES = 3;
    public ObservableField<BlutoothDeviceInfoEntity> entity = new ObservableField<>();
    public Drawable drawableImg;
    private MaterialDialog dialog;
    private DeviceListViewModel deviceListViewModel;
    private int connectedTimes = 0;

    public DeviceListItemViewModel(@NonNull DeviceListViewModel viewModel, BlutoothDeviceInfoEntity entity) {
        super(viewModel);
        this.entity.set(entity);
        deviceListViewModel = viewModel;
        //ImageView的占位图片，可以解决RecyclerView中图片错误问题
        drawableImg = ContextCompat.getDrawable(viewModel.getApplication(), R.mipmap.massagechair);
        dialog = MaterialDialogUtils.showIndeterminateProgressDialog(viewModel.getContext(), "正在连接设备，请稍后..." + this.entity.get().getMacAddress(), false).build();
    }

    /**
     * 获取position的方式有很多种,indexOf是其中一种，常见的还有在Adapter中、ItemBinding.of回调里
     *
     * @return
     */
    public int getPosition() {
        return viewModel.getItemPosition(this);
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //这里可以通过一个标识,做出判断，已达到跳入不同界面的逻辑
            viewModel.checkDeviceInfo(AppTools.CUREENT_SERIONUM, DeviceListItemViewModel.this);
//            AppApplication.getBluetoothClient(viewModel.getContext()).connect(BluetoothUtils.getRemoteDevice(entity.get().getMacAddress()).getAddress(), new BleConnectResponse() {
//                @Override
//                public void onResponse(int code, BleGattProfile data) {
//                    dialog.dismiss();
//                    if (code == REQUEST_SUCCESS) {
//
//                        int status =  AppApplication.getBluetoothClient(viewModel.getApplication()).getConnectStatus(entity.get().getMacAddress());
//                        ToastUtils.showLong("连接成功"+status);
//                        List<BleGattService> services = data.getServices();
//
//                        BleGattService service=services.get(services.size()-1);
//                        List<BleGattCharacter> characters = service.getCharacters();
//                        byte[]sends= "ATSTART".getBytes();
//                        for(byte dataB:sends){
//                            Log.e("yuanjian",dataB+" data value");
//                        }
//                        Log.e("yuanjian",service.getUUID()+" =service.getUUID()"+UUID_SERVICE_CHANNEL);
//                        Log.e("yuanjian",characters.get(characters.size()-1).getUuid()+" =characters.get(characters.size()-1).getUuid()"+UUID_CHARACTERISTIC_CHANNEL);
//                        if(services.size()>0){
//                            AppApplication.getBluetoothClient(viewModel.getContext()).write(entity.get().getMacAddress(),UUID_SERVICE_CHANNEL, UUID_CHARACTERISTIC_CHANNEL_WRITE, HexUtil.decodeHex("41545354415254".toCharArray()), new BleWriteResponse() {
//                                @Override
//                                public void onResponse(int code) {
//                                    Log.e("yuanjian","code is  "+code);
//                                    if(code == REQUEST_SUCCESS){
//                                        //ToastUtils.showLong("通信成功"+code);
//                                        Log.e("yuanjian","通信成功 ");
//                                    }
//                                }
//                            });
//                            AppApplication.getBluetoothClient(viewModel.getContext()).readRssi(entity.get().getMacAddress(), new BleReadRssiResponse() {
//                                @Override
//                                public void onResponse(int code, Integer rssi) {
//                                    if (code == REQUEST_SUCCESS) {
//                                        Log.e("yuanjian","rssi is "+rssi);
//                                    }
//                                }
//                            });
//
//                            AppApplication.getBluetoothClient(viewModel.getContext()).notify(entity.get().getMacAddress(), UUID_SERVICE_CHANNEL, UUID_CHARACTERISTIC_CHANNEL, new BleNotifyResponse() {
//                                @Override
//                                public void onNotify(UUID service, UUID character, byte[] value) {
//                                    Log.e("yuanjian","onNotify UUID is "+character +RxDataTool.bytes2HexString(value));
//                                }
//
//                                @Override
//                                public void onResponse(int code) {
//                                    if (code == REQUEST_SUCCESS) {
//                                        Log.e("yuanjian","onNotify is sucess");
//                                    }
//                                }
//                            });
//                            AppApplication.getBluetoothClient(viewModel.getContext()).read(entity.get().getMacAddress(), UUID_SERVICE_CHANNEL, UUID_CHARACTERISTIC_CHANNEL_WRITE, new BleReadResponse() {
//                                @Override
//                                public void onResponse(int code, byte[] data) {
//                                    Log.e("yuanjian","code is "+code);
//                                    if (code == REQUEST_SUCCESS) {
//                                        Log.e("yuanjian","recive is "+RxDataTool.bytes2HexString(data));
//                                    }
//                                }
//                            });
//                        }
//
//                        //viewModel.startContainerActivity(DeviceControlFragment.class.getCanonicalName());
//                    }else{
//                        ToastUtils.showLong("连接失败，请重试");
//                    }
//                }
//            });
        }
    });
    //条目的长按事件
    public BindingCommand itemLongClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //以前是使用Messenger发送事件，在NetWorkViewModel中完成删除逻辑
//            Messenger.getDefault().send(NetWorkItemViewModel.this, NetWorkViewModel.TOKEN_NETWORKVIEWMODEL_DELTE_ITEM);
            //现在ItemViewModel中存在ViewModel引用，可以直接拿到LiveData去做删除
            ToastUtils.showShort(entity.get().getDeviceName());
        }
    });

    @Override
    public void onResponse(int code, BleGattProfile data) {

        connectedTimes++;
        if (code == REQUEST_SUCCESS) {
            dialog.dismiss();
//            ToastUtils.showLong(viewModel.getContext().getString(R.string.toast_title_hasconnected));
            connectedTimes = 0;
            BleOption.getInstance().initWriteDataEnv();
            BleOption.getInstance().getNotifyData(this);
            BleOption.getInstance().getDeviceInfo(this);
        } else {
            if (connectedTimes > MAX_CONNECTETIMES) {
                dialog.dismiss();
                connectedTimes = 0;
                ToastUtils.showLong(viewModel.getContext().getString(R.string.toast_title_connect_error));
            } else {
                RxLogTool.e(TAG, "ReconnectDevice times is " + connectedTimes);
                BleOption.getInstance().connectDevice(entity.get().getMacAddress(), DeviceListItemViewModel.this);
            }

        }
    }

    @Override
    public void onDeviceCanUseResult(boolean isCanUse) {
        if (isCanUse) {
            dialog.show();
            AppApplication.getBluetoothClient(viewModel.getApplication()).stopSearch();
            connectedTimes = 0;
            BleOption.getInstance().connectDevice(entity.get().getMacAddress(), DeviceListItemViewModel.this);
        } else {
            MaterialDialogUtils.showBasicDialog(viewModel.getContext(), "连接失败", "非法的设备串号");
        }
    }

    @Override
    public void onResponse(int code) {

    }

    @Override
    public void onNotify(UUID service, UUID character, byte[] value) {
        if(!AppManager.getAppManager().currentActivity().getLocalClassName().equals("ui.main.DeviceListActivity")){
            RxLogTool.e(TAG, "getLocalClassName is " + AppManager.getAppManager().currentActivity().getLocalClassName()+";cannot jump...");
            return;
        }
        RxLogTool.e(TAG, "data  length is " + value.length);
        RxLogTool.e(TAG, "getLocalClassName is " + AppManager.getAppManager().currentActivity().getLocalClassName());
        BleOption.getInstance().clearLatestWriteCommondAndFlag();
        if (value.length == 10) {
            DeviceStatusInfoEntity recDeviceStatusInfoEntity = new DeviceStatusInfoEntity(value);
            deviceListViewModel.getProductInfo(String.valueOf(recDeviceStatusInfoEntity.getBatchCode()));
        } else {
            deviceListViewModel.jumpToControlFragment(null);
        }
        BleOption.getInstance().uninitWriteDataEnv();
    }
//    /**
//     * 可以在xml中使用binding:currentView="@{viewModel.titleTextView}" 拿到这个控件的引用, 但是强烈不推荐这样做，避免内存泄漏
//     **/
//    private TextView tv;
//    //将标题TextView控件回调到ViewModel中
//    public BindingCommand<TextView> titleTextView = new BindingCommand(new BindingConsumer<TextView>() {
//        @Override
//        public void call(TextView tv) {
//            NetWorkItemViewModel.this.tv = tv;
//        }
//    });
}
