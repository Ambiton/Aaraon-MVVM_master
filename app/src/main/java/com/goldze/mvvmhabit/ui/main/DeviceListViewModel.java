package com.goldze.mvvmhabit.ui.main;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.app.AppApplication;
import com.goldze.mvvmhabit.data.DemoRepository;
import com.goldze.mvvmhabit.entity.BlutoothDeviceInfoEntity;
import com.goldze.mvvmhabit.entity.DeviceStatusInfoEntity;
import com.goldze.mvvmhabit.entity.db.ProductInfoData;
import com.goldze.mvvmhabit.entity.db.UserActionData;
import com.goldze.mvvmhabit.entity.http.ResponseNetDeviceInfoEntity;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateBodyEntity;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateResponseEntity;
import com.goldze.mvvmhabit.entity.http.productinfo.ProductInfoResponseDataEntity;
import com.goldze.mvvmhabit.entity.http.productinfo.ProductInfoResponseEntity;
import com.goldze.mvvmhabit.entity.http.useraction.SubmitActionDataResponseEntity;
import com.goldze.mvvmhabit.ui.base.viewmodel.ToolbarViewModel;
import com.goldze.mvvmhabit.utils.AppTools;
import com.goldze.mvvmhabit.utils.BleOption;
import com.goldze.mvvmhabit.utils.HttpStatus;
import com.goldze.mvvmhabit.utils.HttpsUtils;
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
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by goldze on 2017/7/17.
 */

public class DeviceListViewModel extends ToolbarViewModel<DemoRepository> {
    private static final String TAG="DeviceListViewModel";
    public SingleLiveEvent<DeviceListItemViewModel> deleteItemLiveData = new SingleLiveEvent<>();
    //绑定列表的绑定
    public ObservableField<String> bindListStr = new ObservableField<>(getApplication().getString(R.string.devicelist_title_bindlist));
    //可用设备列表的绑定
    public ObservableField<String> canUseListStr = new ObservableField<>(getApplication().getString(R.string.devicelist_title_canuselist));
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();
    //版本检测观察者
    public ObservableField<CheckUpdateResponseEntity> versionEvent = new ObservableField<>();
    private Context contex;


    public void setContext(Context context) {
        this.contex = context;
    }

    public Context getContext() {
        return this.contex;
    }

    public class UIChangeObservable {
        //下拉刷新完成
        public SingleLiveEvent finishRefreshing = new SingleLiveEvent<>();
        //上拉加载完成
        public SingleLiveEvent finishLoadmore = new SingleLiveEvent<>();
    }

    public DeviceListViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
    }

    /**
     * 初始化Toolbar
     */
    public void initToolbar() {
        //初始化标题栏
        //setRightIcon(R.mipmap.roundlogo);
        //setRightIconVisible(View.VISIBLE);
        //setRightTextVisible(View.GONE);
        setTitleText(getApplication().getString(R.string.devicelist_title_devicelist));
    }

    @Override
    public void rightTextOnClick() {
        ToastUtils.showShort("更多");
    }

    public ObservableList<DeviceListItemViewModel> observableBindedList = new ObservableArrayList<>();
    //给RecyclerView添加ObservableList
    public ObservableList<DeviceListItemViewModel> observableCanUseList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<DeviceListItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_devicelist);
    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            requestDeviceList();
        }
    });
    //上拉加载
    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
//            if (observableCanUseList.size() > 50) {
//                ToastUtils.showLong("兄dei，你太无聊啦~崩是不可能的~");
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
        if (!AppManager.getAppManager().currentActivity().getLocalClassName().equals("ui.main.DeviceListActivity")) {
            RxLogTool.e(TAG, "getLocalClassName is " + AppManager.getAppManager().currentActivity().getLocalClassName() + ";cannot jump...");
            return;
        }
        Bundle bundle=new Bundle();
        bundle.putString(AllDeviceControlFragment.KEY_PRODUCTID,batchCode);
        startContainerActivity(AllDeviceControlFragment.class.getCanonicalName(),bundle);
    }
    public void getProductInfo(final DeviceStatusInfoEntity deviceStatusInfoEntity){
        if (deviceStatusInfoEntity==null) {
            jumpToControlFragment(null);
            return;
        }
        if (!NetworkUtil.isNetworkAvailable(contex)) {
            jumpToControlFragment(deviceStatusInfoEntity.getBatchCode());
            return;
        }
        addSubscribe(model.getProductInfo(deviceStatusInfoEntity.getBatchCode(),AppTools.APPKEY,"sig-result",model.getToken(), HttpsUtils.getCurrentMills())
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog("获取产品信息中，请稍后...");
                    }
                })
                .subscribe(new Consumer<ProductInfoResponseEntity>() {
                    @Override
                    public void accept(ProductInfoResponseEntity entity) throws Exception {
                        dismissDialog();
                        boolean isOptionSuccess = entity.getStatus() == HttpStatus.STATUS_CODE_SUCESS;
                        RxLogTool.e(TAG, "ProductInfoResponseEntity getStatus is: " + entity.getStatus());
                        if (isOptionSuccess&&getProductDBInfo(entity.getData().getProdId())!=null&&
                                AppTools.isVersionNeedUpdate(getProductDBInfo(entity.getData().getProdId()).getStyleResNewestVerno(),entity.getData().getStyleResNewestVerno())) {
                            AppTools.downProductInfoImageFiles(contex, entity, DeviceListViewModel.this,deviceStatusInfoEntity.getBatchCode());
                        }else{
                            RxLogTool.e(TAG, "ProductInfoStyleRes is newest");
                            jumpToControlFragment(deviceStatusInfoEntity.getBatchCode());
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //关闭对话框
                        dismissDialog();
                        jumpToControlFragment(deviceStatusInfoEntity.getBatchCode());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //关闭对话框
                        dismissDialog();
                    }
                }));
    }

    public ProductInfoData getProductDBInfo(String productId){
        return model.getProductInfoData(productId);
    }

    public void checkVersion(){
        //RaJava检测更新
        addSubscribe(model.checkUpdate(AppTools.APPKEY,"sig-result",model.getToken(), HttpsUtils.getCurrentMills(),new CheckUpdateBodyEntity("01.00","01.00","01.00"))
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new Consumer<CheckUpdateResponseEntity>() {
                    @Override
                    public void accept(CheckUpdateResponseEntity entity) throws Exception {
                        dismissDialog();
                        Log.e(TAG,"getStatus is:;Array is "+entity);
                        //保存账号密码
                        versionEvent.set(entity);
                        versionEvent.notifyChange();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //关闭对话框
                        dismissDialog();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //关闭对话框
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
                .compose(RxUtils.schedulersTransformer()) //线程调度
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
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //关闭对话框
                        dismissDialog();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //关闭对话框
                        dismissDialog();
                    }
                }));
    }

    public void checkDeviceInfo(String serioNum, final OnDeviceInfoListener onDeviceInfoListener){
        addSubscribe(model.getDeviceInfo(serioNum,AppTools.APPKEY,"sig-result",model.getToken(), HttpsUtils.getCurrentMills())
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new Consumer<ResponseNetDeviceInfoEntity>() {
                    @Override
                    public void accept(ResponseNetDeviceInfoEntity entity) throws Exception {
                        dismissDialog();
                        RxLogTool.e(TAG,"getStatus is:; "+entity.getStatus());
                        boolean isCanUse=entity.getStatus()== HttpStatus.STATUS_CODE_SUCESS;
                        model.saveUnitId(entity.getData().getUnitId());
                        onDeviceInfoListener.onDeviceCanUseResult(isCanUse);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //关闭对话框
                        dismissDialog();
                        RxLogTool.e(TAG,"getStatus error:; ",throwable);
                        onDeviceInfoListener.onDeviceCanUseResult(true);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //关闭对话框
                        dismissDialog();
                    }
                }));
    }
    /**
     * 停止扫描
     */
    public void cancelScan() {
        AppApplication.getBluetoothClient(getApplication()).stopSearch();
    }


    /**
     * 设备扫描请求方法，在ViewModel中调用Model层，通过Okhttp+Retrofit+RxJava发起请求
     */
    public void requestDeviceList() {
        // Get the local Bluetooth adapter
        AppApplication.getBluetoothClient(getApplication()).openBluetooth();
        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        //可以调用addSubscribe()添加Disposable，请求与View周期同步
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)   // 先扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
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
                //请求刷新完成收回
                uc.finishRefreshing.call();
                bindListStr.set(getApplication().getString(R.string.devicelist_title_bindlist) + "(" + observableBindedList.size() + ")");
                canUseListStr.set(getApplication().getString(R.string.devicelist_title_canuselist) + "(" + observableCanUseList.size() + ")");
            }

            @Override
            public void onSearchCanceled() {
                //请求刷新完成收回
                uc.finishRefreshing.call();
                bindListStr.set(getApplication().getString(R.string.devicelist_title_bindlist) + "(" + observableBindedList.size() + ")");
                canUseListStr.set(getApplication().getString(R.string.devicelist_title_canuselist) + "(" + observableCanUseList.size() + ")");
            }
        });
    }

    /**
     * 删除条目
     *
     * @param netWorkItemViewModel
     */
    public void deleteItem(DeviceListItemViewModel netWorkItemViewModel) {
        //点击确定，在 observableCanUseList 绑定中删除，界面立即刷新
        observableCanUseList.remove(netWorkItemViewModel);
    }

    /**
     * 获取条目下标
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
