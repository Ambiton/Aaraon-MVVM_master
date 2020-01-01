package com.goldze.mvvmhabit.ui.main;

import android.Manifest;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.app.AppApplication;
import com.goldze.mvvmhabit.data.DemoRepository;
import com.goldze.mvvmhabit.entity.DemoEntity;
import com.goldze.mvvmhabit.entity.DeviceInfoEntity;
import com.goldze.mvvmhabit.ui.base.viewmodel.ToolbarViewModel;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by goldze on 2017/7/17.
 */

public class DeviceListViewModel extends ToolbarViewModel<DemoRepository> {
    public SingleLiveEvent<DeviceListItemViewModel> deleteItemLiveData = new SingleLiveEvent<>();
    //绑定列表的绑定
    public ObservableField<String> bindListStr = new ObservableField<>(getApplication().getString(R.string.devicelist_title_bindlist));
    //可用设备列表的绑定
    public ObservableField<String> canUseListStr = new ObservableField<>(getApplication().getString(R.string.devicelist_title_canuselist));
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

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
        setRightIcon(R.mipmap.applauncher);
        setRightIconVisible(View.VISIBLE);
       // setRightTextVisible(View.GONE);
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

    /**
     * 停止扫描
     */
    public void cancelScan(){
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
        final HashSet<String>canuseList=new HashSet<>();
        final HashSet<String>bindeduseList=new HashSet<>();
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
                DeviceInfoEntity entity=new DeviceInfoEntity(R.mipmap.applauncher,device.rssi,device.getName(),device.getAddress());
                DeviceListItemViewModel itemViewModel = new DeviceListItemViewModel(DeviceListViewModel.this, entity);
                BluetoothLog.e("device  is "+device.getName());
                if(AppApplication.getBluetoothClient(getApplication()).getBondState(device.getAddress())==BluetoothDevice.BOND_BONDED){
                    if(bindeduseList.add(device.getAddress())){
                        observableBindedList.add(itemViewModel);
                        BluetoothLog.e("bindList is "+device.getName()+" MacAdress is "+device.getAddress());
                    }
                }else {
                    //
                    if(canuseList.add(device.getAddress())){
                        observableCanUseList.add(itemViewModel);
                        BluetoothLog.e("CanUseList is "+device.getName()+" MacAdress is "+device.getAddress());
                    }

                }



            }

            @Override
            public void onSearchStopped() {
                //请求刷新完成收回
                uc.finishRefreshing.call();
                bindListStr.set(getApplication().getString(R.string.devicelist_title_bindlist)+"("+observableBindedList.size()+")");
                canUseListStr.set(getApplication().getString(R.string.devicelist_title_canuselist)+"("+observableCanUseList.size()+")");
            }

            @Override
            public void onSearchCanceled() {
                //请求刷新完成收回
                uc.finishRefreshing.call();
                bindListStr.set(getApplication().getString(R.string.devicelist_title_bindlist)+"("+observableBindedList.size()+")");
                canUseListStr.set(getApplication().getString(R.string.devicelist_title_canuselist)+"("+observableCanUseList.size()+")");
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
