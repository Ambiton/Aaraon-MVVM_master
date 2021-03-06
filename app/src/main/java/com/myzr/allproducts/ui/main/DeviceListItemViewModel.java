package com.myzr.allproducts.ui.main;

import androidx.databinding.ObservableField;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.myzr.allproducts.R;
import com.myzr.allproducts.app.AppApplication;
import com.myzr.allproducts.entity.BlutoothDeviceInfoEntity;
import com.myzr.allproducts.entity.DeviceStatusInfoEntity;
import com.myzr.allproducts.utils.AppTools;
import com.myzr.allproducts.utils.BleOption;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.tamsiree.rxtool.RxLogTool;

import java.util.UUID;

import me.goldze.mvvmhabit.base.AppManager;
import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.deviceinterface.OnDeviceInfoListener;
import me.goldze.mvvmhabit.utils.MaterialDialogUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

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
        //ImageView??????????????????????????????RecyclerView?????????????????????
        drawableImg = ContextCompat.getDrawable(viewModel.getApplication(), R.mipmap.massagechair);
        dialog = MaterialDialogUtils.showIndeterminateProgressDialog(viewModel.getActivity(), "??????????????????????????????..." + this.entity.get().getMacAddress(), false).build();
    }

    /**
     * ??????position?????????????????????,indexOf????????????????????????????????????Adapter??????ItemBinding.of?????????
     *
     * @return
     */
    public int getPosition() {
        return viewModel.getItemPosition(this);
    }

    //?????????????????????
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxLogTool.e(TAG,"getBondState is: "+AppApplication.getBluetoothClient(viewModel.getApplication()).getBondState(entity.get().getMacAddress()));
            //??????????????????????????????,???????????????????????????????????????????????????
            viewModel.checkDeviceInfo(AppTools.CUREENT_SERIONUM, DeviceListItemViewModel.this);
//            AppApplication.getBluetoothClient(viewModel.getContext()).connect(BluetoothUtils.getRemoteDevice(entity.get().getMacAddress()).getAddress(), new BleConnectResponse() {
//                @Override
//                public void onResponse(int code, BleGattProfile data) {
//                    dialog.dismiss();
//                    if (code == REQUEST_SUCCESS) {
//
//                        int status =  AppApplication.getBluetoothClient(viewModel.getApplication()).getConnectStatus(entity.get().getMacAddress());
//                        ToastUtils.showLong("????????????"+status);
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
//                                        //ToastUtils.showLong("????????????"+code);
//                                        Log.e("yuanjian","???????????? ");
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
//                        ToastUtils.showLong("????????????????????????");
//                    }
//                }
//            });
        }
    });
    //?????????????????????
    public BindingCommand itemLongClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //???????????????Messenger??????????????????NetWorkViewModel?????????????????????
//            Messenger.getDefault().send(NetWorkItemViewModel.this, NetWorkViewModel.TOKEN_NETWORKVIEWMODEL_DELTE_ITEM);
            //??????ItemViewModel?????????ViewModel???????????????????????????LiveData????????????
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
//            if (connectedTimes > MAX_CONNECTETIMES) {
                dialog.dismiss();
                connectedTimes = 0;
                ToastUtils.showLong(viewModel.getActivity().getString(R.string.toast_title_connect_error));
                viewModel.requestDeviceList();
//            } else {
//                RxLogTool.e(TAG, "ReconnectDevice times is " + connectedTimes);
//                BleOption.getInstance().connectDevice(entity.get().getMacAddress(), DeviceListItemViewModel.this);
//            }

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
            MaterialDialogUtils.showBasicDialog(viewModel.getActivity(), "????????????", "?????????????????????");
        }
    }

    @Override
    public void onResponse(int code) {

    }

    @Override
    public void onNotify(UUID service, UUID character, byte[] value) {
        if (!AppManager.getAppManager().currentActivity().getClass().getCanonicalName().equals(DeviceListActivity.class.getCanonicalName())) {
            RxLogTool.e(TAG, "getLocalClassName is " + AppManager.getAppManager().currentActivity().getClass().getCanonicalName() + ";cannot jump...");
            return;
        }
        RxLogTool.e(TAG, "data  length is " + value.length);
        RxLogTool.e(TAG, "getLocalClassName is " + AppManager.getAppManager().currentActivity().getClass().getCanonicalName());
        BleOption.getInstance().clearLatestWriteCommondAndFlag();
        if (value.length == 10) {
            DeviceStatusInfoEntity recDeviceStatusInfoEntity = new DeviceStatusInfoEntity(value);
            deviceListViewModel.getProductInfo(entity.get().getMacAddress(),recDeviceStatusInfoEntity);
        } else {
//            DeviceStatusInfoEntity recDeviceStatusInfoEntity = new DeviceStatusInfoEntity();
//            recDeviceStatusInfoEntity.setProductType((byte)1);
//            recDeviceStatusInfoEntity.setStyle((byte)1);
//            deviceListViewModel.getProductInfo(recDeviceStatusInfoEntity);
            deviceListViewModel.jumpToControlFragment(null);
        }
        BleOption.getInstance().uninitWriteDataEnv();
    }
//    /**
//     * ?????????xml?????????binding:currentView="@{viewModel.titleTextView}" ???????????????????????????, ???????????????????????????????????????????????????
//     **/
//    private TextView tv;
//    //?????????TextView???????????????ViewModel???
//    public BindingCommand<TextView> titleTextView = new BindingCommand(new BindingConsumer<TextView>() {
//        @Override
//        public void call(TextView tv) {
//            NetWorkItemViewModel.this.tv = tv;
//        }
//    });
}
