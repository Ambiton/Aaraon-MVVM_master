package com.goldze.mvvmhabit.ui.main;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.app.AppApplication;
import com.goldze.mvvmhabit.entity.DeviceInfoEntity;
import com.goldze.mvvmhabit.utils.HexUtil;
import com.goldze.mvvmhabit.utils.RxDataTool;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleReadRssiResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.inuker.bluetooth.library.utils.ByteUtils;
import com.lcodecore.tkrefreshlayout.utils.LogUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.MaterialDialogUtils;
import me.goldze.mvvmhabit.utils.StringUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

/**
 * Created by goldze on 2017/7/17.
 */

public class DeviceListItemViewModel extends ItemViewModel<DeviceListViewModel> {
    public ObservableField<DeviceInfoEntity> entity = new ObservableField<>();
    public Drawable drawableImg;
    MaterialDialog  dialog;
    private final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private final String CHAR_UUID = "64e400001-b5a3-f393-e0a9-e50e24dcca9e";
    private static final UUID UUID_SERVICE_CHANNEL
            = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");

    private static final UUID UUID_CHARACTERISTIC_CHANNEL_WRITE
            = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");
    private static final UUID UUID_CHARACTERISTIC_CHANNEL
            = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");


    public DeviceListItemViewModel(@NonNull DeviceListViewModel viewModel, DeviceInfoEntity entity) {
        super(viewModel);
        this.entity.set(entity);
        //ImageView的占位图片，可以解决RecyclerView中图片错误问题
        drawableImg = ContextCompat.getDrawable(viewModel.getApplication(), R.mipmap.ic_launcher);
        dialog=MaterialDialogUtils.showIndeterminateProgressDialog(viewModel.getContext(),"正在连接设备，请稍后..."+this.entity.get().getMacAddress(),false).build();
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
            dialog.show();
            AppApplication.getBluetoothClient(viewModel.getApplication()).stopSearch();
            AppApplication.getBluetoothClient(viewModel.getContext()).connect(BluetoothUtils.getRemoteDevice(entity.get().getMacAddress()).getAddress(), new BleConnectResponse() {
                @Override
                public void onResponse(int code, BleGattProfile data) {
                    dialog.dismiss();
                    if (code == REQUEST_SUCCESS) {

                        int status =  AppApplication.getBluetoothClient(viewModel.getApplication()).getConnectStatus(entity.get().getMacAddress());
                        ToastUtils.showLong("连接成功"+status);
                        List<BleGattService> services = data.getServices();

                        BleGattService service=services.get(services.size()-1);
                        List<BleGattCharacter> characters = service.getCharacters();
                        byte[]sends= "ATSTART".getBytes();
                        for(byte dataB:sends){
                            Log.e("yuanjian",dataB+" data value");
                        }
                        Log.e("yuanjian",service.getUUID()+" =service.getUUID()"+UUID_SERVICE_CHANNEL);
                        Log.e("yuanjian",characters.get(characters.size()-1).getUuid()+" =characters.get(characters.size()-1).getUuid()"+UUID_CHARACTERISTIC_CHANNEL);
                        if(services.size()>0){
                            AppApplication.getBluetoothClient(viewModel.getContext()).write(entity.get().getMacAddress(),UUID_SERVICE_CHANNEL, UUID_CHARACTERISTIC_CHANNEL_WRITE, HexUtil.decodeHex("41545354415254".toCharArray()), new BleWriteResponse() {
                                @Override
                                public void onResponse(int code) {
                                    Log.e("yuanjian","code is  "+code);
                                    if(code == REQUEST_SUCCESS){
                                        //ToastUtils.showLong("通信成功"+code);
                                        Log.e("yuanjian","通信成功 ");
                                    }
                                }
                            });
                            AppApplication.getBluetoothClient(viewModel.getContext()).readRssi(entity.get().getMacAddress(), new BleReadRssiResponse() {
                                @Override
                                public void onResponse(int code, Integer rssi) {
                                    if (code == REQUEST_SUCCESS) {
                                        Log.e("yuanjian","rssi is "+rssi);
                                    }
                                }
                            });

                            AppApplication.getBluetoothClient(viewModel.getContext()).notify(entity.get().getMacAddress(), UUID_SERVICE_CHANNEL, UUID_CHARACTERISTIC_CHANNEL, new BleNotifyResponse() {
                                @Override
                                public void onNotify(UUID service, UUID character, byte[] value) {
                                    Log.e("yuanjian","onNotify UUID is "+character +RxDataTool.bytes2HexString(value));
                                }

                                @Override
                                public void onResponse(int code) {
                                    if (code == REQUEST_SUCCESS) {
                                        Log.e("yuanjian","onNotify is sucess");
                                    }
                                }
                            });
                            AppApplication.getBluetoothClient(viewModel.getContext()).read(entity.get().getMacAddress(), UUID_SERVICE_CHANNEL, UUID_CHARACTERISTIC_CHANNEL_WRITE, new BleReadResponse() {
                                @Override
                                public void onResponse(int code, byte[] data) {
                                    Log.e("yuanjian","code is "+code);
                                    if (code == REQUEST_SUCCESS) {
                                        Log.e("yuanjian","recive is "+RxDataTool.bytes2HexString(data));
                                    }
                                }
                            });
                        }

                        //viewModel.startContainerActivity(DeviceControlFragment.class.getCanonicalName());
                    }else{
                        ToastUtils.showLong("连接失败，请重试");
                    }
                }
            });
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
