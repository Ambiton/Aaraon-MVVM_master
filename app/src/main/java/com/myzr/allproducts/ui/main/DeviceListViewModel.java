package com.myzr.allproducts.ui.main;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.myzr.allproducts.BR;
import com.myzr.allproducts.R;
import com.myzr.allproducts.app.AppApplication;
import com.myzr.allproducts.data.DemoRepository;
import com.myzr.allproducts.entity.BlutoothDeviceInfoEntity;
import com.myzr.allproducts.entity.DeviceStatusInfoEntity;
import com.myzr.allproducts.entity.db.ProductInfoData;
import com.myzr.allproducts.entity.db.UserActionData;
import com.myzr.allproducts.entity.http.checkversion.CheckUpdateBodyEntity;
import com.myzr.allproducts.entity.http.checkversion.CheckUpdateResponseEntity;
import com.myzr.allproducts.entity.http.productinfo.ProductInfoResponseDataEntity;
import com.myzr.allproducts.entity.http.productinfo.ProductInfoResponseEntity;
import com.myzr.allproducts.entity.http.useraction.SubmitActionDataResponseEntity;
import com.myzr.allproducts.ui.base.viewmodel.ToolbarViewModel;
import com.myzr.allproducts.ui.login.LoginActivity;
import com.myzr.allproducts.utils.AppTools;
import com.myzr.allproducts.utils.BleOption;
import com.myzr.allproducts.utils.HttpStatus;
import com.myzr.allproducts.utils.HttpsUtils;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.tamsiree.rxtool.RxLogTool;

import java.util.HashSet;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.AppManager;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.deviceinterface.OnDeviceInfoListener;
import me.goldze.mvvmhabit.http.NetworkUtil;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by goldze on 2017/7/17.
 */

public class DeviceListViewModel extends ToolbarViewModel<DemoRepository> {
    private static final String TAG="DeviceListViewModel";
    public SingleLiveEvent<DeviceListItemViewModel> deleteItemLiveData = new SingleLiveEvent<>();
    //?????????????????????
    public ObservableField<String> bindListStr = new ObservableField<>(getApplication().getString(R.string.devicelist_title_bindlist));
    //???????????????????????????
    public ObservableField<String> canUseListStr = new ObservableField<>(getApplication().getString(R.string.devicelist_title_canuselist));
    //??????????????????????????????????????????
    public UIChangeObservable uc = new UIChangeObservable();
    //?????????????????????
    public ObservableField<CheckUpdateResponseEntity> versionEvent = new ObservableField<>();
    private Activity activity;


    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return this.activity;
    }

    public class UIChangeObservable {
        //??????????????????
        public SingleLiveEvent finishRefreshing = new SingleLiveEvent<>();
        //??????????????????
        public SingleLiveEvent finishLoadmore = new SingleLiveEvent<>();
    }

    public DeviceListViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
    }

    /**
     * ?????????Toolbar
     */
    public void initToolbar() {
        //??????????????????
//        setRightIcon(R.mipmap.roundlogo);
//        setRightIconVisible(View.VISIBLE);
//        setRightTextVisible(View.GONE);
        setTitleText(getApplication().getString(R.string.devicelist_title_devicelist));
    }
    public void saveBannerPlayIndex(int playIndex) {
        model.saveBannerPlayIndex(playIndex);
    }
    @Override
    public void rightTextOnClick() {
        ToastUtils.showShort("??????");
    }

    public ObservableList<DeviceListItemViewModel> observableBindedList = new ObservableArrayList<>();
    //???RecyclerView??????ObservableList
    public ObservableList<DeviceListItemViewModel> observableCanUseList = new ObservableArrayList<>();
    //???RecyclerView??????ItemBinding
    public ItemBinding<DeviceListItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_devicelist);
    //????????????
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            requestDeviceList();
        }
    });
    //????????????
    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
//            if (observableCanUseList.size() > 50) {
//                ToastUtils.showLong("???dei??????????????????~??????????????????~");
//                uc.finishLoadmore.call();
//                return;
//            }
            //requestDeviceList();
        }
    });
    public void saveProductInfoToDB(ProductInfoResponseDataEntity productInfoResponseDataEntity){
        if (productInfoResponseDataEntity == null) {
            return;
        }
        model.saveProductInfoDataToDB(new ProductInfoData(productInfoResponseDataEntity));
    }
    public void jumpToControlFragment(String batchCode){
        if (!AppManager.getAppManager().currentActivity().getClass().getCanonicalName().equals(DeviceListActivity.class.getCanonicalName())) {
            RxLogTool.e(TAG, "getLocalClassName is " + AppManager.getAppManager().currentActivity().getClass().getCanonicalName() + ";cannot jump...");
            return;
        }
//        if (!NetworkUtil.isNetworkAvailable(activity)) {
        if(AppTools.isStyleResDrawableTotalComplete(activity, batchCode)){
            Bundle bundle=new Bundle();
            bundle.putString(AllDeviceControlFragment.KEY_BATCHCODE,batchCode);
            startContainerActivity(AllDeviceControlFragment.class.getCanonicalName(),bundle);
            dismissDialog();
        }else{
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showLong("*****??????????????????????????????????????????????????????????????????????????????");
//                        MaterialDialogUtils.showBasicDialogNoCancel(activity,"??????????????????????????????????????????????????????????????????????????????");
                }
            });

        }
    }
    public void jumpToControlFragment(String batchCode,final int test){
        if (!AppManager.getAppManager().currentActivity().getClass().getCanonicalName().equals(DeviceListActivity.class.getCanonicalName())) {
            RxLogTool.e(TAG, "getLocalClassName is " + AppManager.getAppManager().currentActivity().getClass().getCanonicalName() + ";cannot jump...");
            return;
        }
//        if (!NetworkUtil.isNetworkAvailable(activity)) {
            if(AppTools.isStyleResDrawableTotalComplete(activity, batchCode)){
                Bundle bundle=new Bundle();
                bundle.putString(AllDeviceControlFragment.KEY_BATCHCODE,batchCode);
                startContainerActivity(AllDeviceControlFragment.class.getCanonicalName(),bundle);
                dismissDialog();
            }else{
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showLong(test+"*****??????????????????????????????????????????????????????????????????????????????");
//                        MaterialDialogUtils.showBasicDialogNoCancel(activity,"??????????????????????????????????????????????????????????????????????????????");
                    }
                });

            }
    }
    public void setBannerPlayModel(String playMode){
        model.saveBannerPlayMode(playMode);
    }
    public String getBannerVersion(){
        return model.getBannerVersion();
    }

    public String getLoadingVersion(){
        return model.getLoadingVersion();
    }
    public void setBannerVersion(String version){
        model.saveBannerVersion(version);
    }
    public void setLoadingVersion(String version){
        model.saveLoadingVersion(version);
    }
    public void getProductInfo(final String mac,final DeviceStatusInfoEntity deviceStatusInfoEntity){
        if (deviceStatusInfoEntity==null) {
            jumpToControlFragment(null);
            return;
        }
        if (!NetworkUtil.isNetworkAvailable(activity)) {
            if(AppTools.isStyleResDrawableTotalComplete(activity, deviceStatusInfoEntity.getBatchCode())){
                jumpToControlFragment(deviceStatusInfoEntity.getBatchCode(),6);
            }else{
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showLong("??????????????????????????????????????????????????????????????????????????????");
//                        MaterialDialogUtils.showBasicDialogNoCancel(activity,"??????????????????????????????????????????????????????????????????????????????");
                    }
                });

            }
            return;
        }
        //deviceStatusInfoEntity.getBatchCode()
        addSubscribe(model.getProductInfo(deviceStatusInfoEntity.getBatchCode(),AppTools.APPKEY,"sig-result",model.getToken(), HttpsUtils.getCurrentMills())
                .compose(RxUtils.schedulersTransformer()) //????????????
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog("?????????????????????????????????...");
                    }
                })
                .subscribe(new Consumer<ProductInfoResponseEntity>() {
                    @Override
                    public void accept(ProductInfoResponseEntity entity) throws Exception {
                        dismissDialog();
                        boolean isOptionSuccess = entity.getStatus() == HttpStatus.STATUS_CODE_SUCESS;
                        RxLogTool.e(TAG, "ProductInfoResponseEntity getStatus is: " + entity.getStatus());
                        if(!isOptionSuccess){
                            final String msg=entity.getMessage();
                            if(AppTools.isTokenErr(entity.getStatus())){
                                AppTools.dearWithErrorReturn(getActivity(),entity.getStatus(),entity.getMessage());
                            }else{
                                RxLogTool.e(TAG,"statusCode is other condition...");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showLong("????????????,"+msg);
                                    }
                                });
                            }

                            return;
                        }
                        if(entity.getData().getProdId() != null ){
                            model.saveProductnameByMac(mac, entity.getData().getProdName());
                            model.saveProductname(deviceStatusInfoEntity.getBatchCode(),entity.getData().getProdName());
                            if (getProductDBInfo(entity.getData().getProdId()) == null ||(!AppTools.isStyleResDrawableTotalComplete(activity, deviceStatusInfoEntity.getBatchCode())||
                                    AppTools.isVersionNeedUpdate(getProductDBInfo(entity.getData().getProdId()).getStyleResNewestVerno(), entity.getData().getStyleResNewestVerno()))) {
                                AppTools.downProductInfoImageFiles(activity, entity, DeviceListViewModel.this, deviceStatusInfoEntity.getBatchCode());
                            } else {
                                RxLogTool.e(TAG, "ProductInfoStyleRes is newest");
                                jumpToControlFragment(deviceStatusInfoEntity.getBatchCode(),7);
                            }
                        }else{
                            ToastUtils.showLong("????????????,"+entity.getMessage());
                            return;
                        }



                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //???????????????
                        dismissDialog();
                        RxLogTool.e(TAG, "ProductInfoStyleRes throwable :",throwable);
                        jumpToControlFragment(deviceStatusInfoEntity.getBatchCode(),8);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //???????????????
                        dismissDialog();
                    }
                }));
    }

    public ProductInfoData getProductDBInfo(String productId){
        return model.getProductInfoData(productId);
    }

    public void checkVersion(){
        //RaJava????????????
        addSubscribe(model.checkUpdate(AppTools.APPKEY,"sig-result",model.getToken(), HttpsUtils.getCurrentMills(),new CheckUpdateBodyEntity(AppTools.getCurrentAppVersion(),getLoadingVersion(),getBannerVersion()))
                .compose(RxUtils.schedulersTransformer()) //????????????
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
//                        showDialog();
                    }
                })
                .subscribe(new Consumer<CheckUpdateResponseEntity>() {
                    @Override
                    public void accept(CheckUpdateResponseEntity entity) throws Exception {
                        dismissDialog();
                        RxLogTool.e(TAG,"getStatus is:;Array is "+entity);
                        if(entity.getStatus()== HttpStatus.STATUS_CODE_SUCESS){
                            //??????????????????
                            versionEvent.set(entity);
                            versionEvent.notifyChange();
                        } else if(AppTools.isTokenErr(entity.getStatus())){
                           AppTools.dearWithErrorReturn(getActivity(),entity.getStatus(),entity.getMessage());
                        }else{
                            RxLogTool.e(TAG,"statusCode is other condition...");
                        }


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //???????????????
                        dismissDialog();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //???????????????
                        dismissDialog();
                    }
                }));
    }

    public void submitUserDeviceOption(){
        final List<UserActionData> submitDatas=model.getLimitUserActionData(10);
        if(model.getLimitUserActionData(10)==null|| !NetworkUtil.isNetworkAvailable(AppApplication.getInstance())){
            RxLogTool.e(TAG,"cannot submit, isNetworkAvailable is "+NetworkUtil.isNetworkAvailable(AppApplication.getInstance()));
            return;
        }
        Log.e(TAG,"submitDatas size  is:; "+submitDatas.size());
        addSubscribe(model.submitUserActionData(AppTools.APPKEY,AppTools.APPSIGN,model.getToken(), HttpsUtils.getCurrentMills(),submitDatas)
                .compose(RxUtils.schedulersTransformer()) //????????????
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .subscribe(new Consumer<SubmitActionDataResponseEntity>() {
                    @Override
                    public void accept(SubmitActionDataResponseEntity entity) throws Exception {
                        Log.e(TAG,"getStatus is:; "+entity.getStatus());
                        boolean isOptionSuccess=entity.getStatus()== HttpStatus.STATUS_CODE_SUCESS;
                        if(isOptionSuccess){
                            model.deleteUserActionDataToDB(submitDatas);
                            if(model.getLimitUserActionData(10)==null){
                                RxLogTool.e(TAG,"All action Data already submit ,no data to submit now...");
                            }else{
                                submitUserDeviceOption();
                                RxLogTool.e(TAG,"go on  submit action Data...");
                            }
                        }else if(AppTools.isTokenErr(entity.getStatus())){
                            AppTools.dearWithErrorReturn(getActivity(),entity.getStatus(),entity.getMessage());
                        }else{
                            RxLogTool.e(TAG,"other submitUserDeviceOption condition ...");
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //???????????????
                        dismissDialog();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //???????????????
                        dismissDialog();
                    }
                }));
    }

    public void checkDeviceInfo(String serioNum, final OnDeviceInfoListener onDeviceInfoListener){
        onDeviceInfoListener.onDeviceCanUseResult(true);
//        addSubscribe(model.getDeviceInfo(serioNum,AppTools.APPKEY,"sig-result",model.getToken(), HttpsUtils.getCurrentMills())
//                .compose(RxUtils.schedulersTransformer()) //????????????
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        showDialog();
//                    }
//                })
//                .subscribe(new Consumer<ResponseNetDeviceInfoEntity>() {
//                    @Override
//                    public void accept(ResponseNetDeviceInfoEntity entity) throws Exception {
//                        dismissDialog();
//                        RxLogTool.e(TAG,"getStatus is:; "+entity.getStatus());
//                        boolean isCanUse=entity.getStatus()== HttpStatus.STATUS_CODE_SUCESS;
//                        model.saveUnitId(entity.getData().getUnitId());
//                        onDeviceInfoListener.onDeviceCanUseResult(isCanUse);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        //???????????????
//                        dismissDialog();
//                        RxLogTool.e(TAG,"getStatus error:; ",throwable);
//                        onDeviceInfoListener.onDeviceCanUseResult(true);
//                    }
//                }, new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        //???????????????
//                        dismissDialog();
//                    }
//                }));
    }
    /**
     * ????????????
     */
    public void cancelScan() {
        AppApplication.getBluetoothClient(getApplication()).stopSearch();
    }


    /**
     * ??????????????????????????????ViewModel?????????Model????????????Okhttp+Retrofit+RxJava????????????
     */
    public void requestDeviceList() {
        // Get the local Bluetooth adapter
        AppApplication.getBluetoothClient(getApplication()).openBluetooth();
        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        //????????????addSubscribe()??????Disposable????????????View????????????
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)   // ??????BLE??????3????????????3s
                .searchBluetoothClassicDevice(5000) // ??????????????????5s
                .searchBluetoothLeDevice(2000)      // ??????BLE??????2s
                .build();
        final HashSet<String> canuseList = new HashSet<>();
        final HashSet<String> bindeduseList = new HashSet<>();
        AppApplication.getBluetoothClient(getApplication()).search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {
                observableCanUseList.clear();
                observableBindedList.clear();
                bindListStr.set(getApplication().getString(R.string.devicelist_title_bindlist_scaning));
                canUseListStr.set(getApplication().getString(R.string.devicelist_title_canuselist_scaning));
            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                BlutoothDeviceInfoEntity entity = new BlutoothDeviceInfoEntity(R.mipmap.applauncher, device.rssi, device.getName(), device.getAddress());
                if(!TextUtils.isEmpty(device.getAddress())&&!TextUtils.isEmpty(model.getProductnameByMac(device.getAddress()))){
                    entity.setDeviceName(model.getProductnameByMac(device.getAddress()));
                }
                BluetoothLog.e("device  is " + device.getName()+";device.getAddress() is "+device.getAddress());
                if (AppApplication.getBluetoothClient(getApplication()).getBondState(device.getAddress()) == BluetoothDevice.BOND_BONDED) {
                    if (BleOption.getInstance().isDeviceBluetooth(device.getName()) && bindeduseList.add(device.getAddress())) {
                        DeviceListItemViewModel itemViewModel = new DeviceListItemViewModel(DeviceListViewModel.this, entity);
                        observableBindedList.add(itemViewModel);
                        BluetoothLog.e("bindList is " + device.getName() + " MacAdress is " + device.getAddress());
                    }
                } else {
                    //
                    if (BleOption.getInstance().isDeviceBluetooth(device.getName()) && canuseList.add(device.getAddress())) {
                        DeviceListItemViewModel itemViewModel = new DeviceListItemViewModel(DeviceListViewModel.this, entity);
                        observableCanUseList.add(itemViewModel);
                        BluetoothLog.e("CanUseList is " + device.getName() + " MacAdress is " + device.getAddress());
                    }
                }


            }

            @Override
            public void onSearchStopped() {
                //????????????????????????
                uc.finishRefreshing.call();
                bindListStr.set(getApplication().getString(R.string.devicelist_title_bindlist) + "(" + observableBindedList.size() + ")");
                canUseListStr.set(getApplication().getString(R.string.devicelist_title_canuselist) + "(" + observableCanUseList.size() + ")");
            }

            @Override
            public void onSearchCanceled() {
                //????????????????????????
                uc.finishRefreshing.call();
                bindListStr.set(getApplication().getString(R.string.devicelist_title_bindlist) + "(" + observableBindedList.size() + ")");
                canUseListStr.set(getApplication().getString(R.string.devicelist_title_canuselist) + "(" + observableCanUseList.size() + ")");
            }
        });
    }

    /**
     * ????????????
     *
     * @param netWorkItemViewModel
     */
    public void deleteItem(DeviceListItemViewModel netWorkItemViewModel) {
        //?????????????????? observableCanUseList ????????????????????????????????????
        observableCanUseList.remove(netWorkItemViewModel);
    }

    /**
     * ??????????????????
     *
     * @param deviceListItemViewModel
     * @return
     */
    public int getItemPosition(DeviceListItemViewModel deviceListItemViewModel) {
        return observableCanUseList.indexOf(deviceListItemViewModel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
