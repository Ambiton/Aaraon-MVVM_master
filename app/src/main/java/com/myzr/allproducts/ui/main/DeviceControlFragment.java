package com.myzr.allproducts.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import android.content.pm.ActivityInfo;

import androidx.databinding.Observable;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.myzr.allproducts.BR;
import com.myzr.allproducts.R;
import com.myzr.allproducts.app.AppViewModelFactory;
import com.myzr.allproducts.databinding.FragmentDevicecontrolBinding;
import com.myzr.allproducts.entity.DeviceStatusInfoEntity;
import com.myzr.allproducts.utils.AppTools;
import com.myzr.allproducts.utils.BleOption;
import com.myzr.allproducts.utils.GlideImageLoader;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.tamsiree.rxtool.RxImageTool;
import com.tamsiree.rxtool.RxLogTool;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;


import java.util.ArrayList;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.utils.MaterialDialogUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import pl.droidsonroids.gif.GifImageView;

import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

/**
 * Created by goldze on 2017/7/17.
 * 网络请求列表界面
 */

public class DeviceControlFragment extends BaseFragment<FragmentDevicecontrolBinding, DeviceControlViewModel> implements OnBannerListener {
    private static final String TAG = "DeviceControlFragment";
    private MaterialDialog.Builder builderBle;

    @Override
    public void initParam() {
        super.initParam();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BleOption.getInstance().unregisterConnectStatusListener(mBleConnectStatusListener);
    }

    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.fragment_devicecontrol;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public DeviceControlViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用DeviceListViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return ViewModelProviders.of(this, factory).get(DeviceControlViewModel.class);
    }

    @Override
    public void initData() {
        //给RecyclerView添加Adpter，请使用自定义的Adapter继承BindingRecyclerViewAdapter，重写onBindBinding方法，里面有你要的Item对应的binding对象。

        builderBle = MaterialDialogUtils.showBasicDialogNoCancel(this.getActivity(), "温馨提示", getResources().getString(R.string.dialog_title_connect_failed));
        builderBle.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                DeviceControlFragment.this.getActivity().finish();
            }
        });
        // Adapter属于View层的东西, 不建议定义到ViewModel中绑定，以免内存泄漏
        BleOption.getInstance().registerConnectListener(mBleConnectStatusListener);
        //请求设备信息数据
        viewModel.initBleEvent();
        viewModel.initToolbar();
        startBanner();
        //binding.ivDevicecontrolGif.setBackgroundResource("asset:inner_high_hot");
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.requestDeviceInfo();
    }

    private ArrayList<Object> getBannerPlayList(ArrayList<Object> allDatas) {
        ArrayList<Object> list_path = new ArrayList<>();
        if (AppTools.isAutoPlayMode(viewModel.getBannerPlayMode())) {
            list_path = allDatas;
        } else {
            if (viewModel.getBannerPlayIndex() > allDatas.size() - 1) {
                viewModel.saveBannerPlayIndex(0);
            }
            list_path.add(allDatas.get(viewModel.getBannerPlayIndex()));
        }
        return list_path;
    }

    private void startBanner() {
        Banner banner = binding.bannerControl;
        //放标题的集合
        ArrayList<String> list_title = new ArrayList<>();
        ArrayList<Object> list_path = getBannerPlayList(AppTools.getBannerUnZipFilesOld(this.getActivity()));

//        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
//        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
//        list_path.add(Uri.fromFile());
//        list_title.add("天天向上");
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        if (list_path.size() > 1) {
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        } else {
            banner.setBannerStyle(BannerConfig.CENTER);
        }

        banner.setImages(list_path);

        for (int i = 0; i < list_path.size(); i++) {
            list_title.add("");
        }
        banner.setBannerTitles(list_title);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合

        //List<String> image = new ArrayList<>();
        //banner.setImages(DataBean.getTestData2());

        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        List<String> titles = new ArrayList<>();
//        titles.add("图1");
//        titles.add("图2");
//        banner.setBannerTitles(null);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setOnBannerListener(this);
        banner.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.bannerControl.startAutoPlay();
        setDrawableTop(binding.tvRotationModePositive,R.drawable.inner_mode_selector);
        setDrawableTop(binding.tvRotationModeReversal,R.drawable.outer_mode_selector);
        setDrawableTop(binding.tvRotationModeAuto,R.drawable.auto_mode_selector);
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.bannerControl.stopAutoPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void setRoationModeGif(DeviceStatusInfoEntity deviceStatusInfoEntity) {
        boolean isRotationModePositive = deviceStatusInfoEntity.getDeviceRoationMode() == DeviceStatusInfoEntity.FLAG_ROATION_POSISTION;
        boolean isRotationModeReversal = deviceStatusInfoEntity.getDeviceRoationMode() == DeviceStatusInfoEntity.FLAG_ROATION_REV;
        boolean isRotationModeAuto = deviceStatusInfoEntity.getDeviceRoationMode() == DeviceStatusInfoEntity.FLAG_ROATION_AUTO ||
                deviceStatusInfoEntity.getDeviceRoationMode() == DeviceStatusInfoEntity.FLAG_ROATION_POSISTION_AND_REV;
        binding.tvRotationModePositive.setChecked(isRotationModePositive);
        binding.tvRotationModeReversal.setChecked(isRotationModeReversal);
        binding.tvRotationModeAuto.setChecked(isRotationModeAuto);

    }

    private void setRoationModeGifVisiable(final GifImageView view, boolean isChecked) {
        boolean isVisble = view.getVisibility() == View.VISIBLE;
        if (isVisble == isChecked) {
            //如果View当前状态和 check状态一致则不需要下面的处理
            return;
        }
        if (isChecked) {
            view.setImageResource(R.drawable.roation_mode);
            view.setVisibility(View.VISIBLE);
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setImageResource(R.drawable.oval_highlight_translate);
                    view.setVisibility(View.GONE);
                }
            }, 750);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 根据当前的设备信息决定使用何种动态图
     *
     * @param deviceStatusInfoEntity
     * @return
     */
    private void setGifRes(DeviceStatusInfoEntity deviceStatusInfoEntity) {
        if (deviceStatusInfoEntity.getDeviceRoationDirect() != DeviceStatusInfoEntity.FLAG_ROATION_DIRECT_POSISION &&
                deviceStatusInfoEntity.getDeviceRoationDirect() != DeviceStatusInfoEntity.FLAG_ROATION_DIRECT_REV) {
            binding.ivDevicecontrolGif.setImageResource(R.drawable.pause);
        } else {
            int res = R.drawable.pause;
            int directtion = deviceStatusInfoEntity.getDeviceRoationDirect();
            int speed = deviceStatusInfoEntity.getDeviceSpeed();
            if (directtion == DeviceStatusInfoEntity.FLAG_ROATION_DIRECT_POSISION) {
                if (speed == DeviceStatusInfoEntity.FLAG_SPEED_MAX) {
                    res = R.drawable.roation_pos_high_normal;
                } else if (speed == DeviceStatusInfoEntity.FLAG_SPEED_MID) {
                    res = R.drawable.roation_pos_mid_normal;
                } else {
                    res = R.drawable.roation_pos_low_normal;
                }
            } else {
                if (speed == DeviceStatusInfoEntity.FLAG_SPEED_MAX) {
                    res = R.drawable.roation_rev_high_normal;
                } else if (speed == DeviceStatusInfoEntity.FLAG_SPEED_MID) {
                    res = R.drawable.roation_rev_mid_normal;
                } else {
                    res = R.drawable.roation_rev_low_normal;
                }
            }
            binding.ivDevicecontrolGif.setImageResource(res);
        }
    }

    private void setAllOptionEnable(boolean isEnable) {
        binding.switchOpen.setChecked(isEnable);
        binding.switchWarm.setEnabled(isEnable);
        binding.tvRotationHigh.setEnabled(isEnable);
        binding.tvRotationMid.setEnabled(isEnable);
        binding.tvRotationLow.setEnabled(isEnable);
        binding.tvVolumeHigh.setEnabled(isEnable);
        binding.tvVolumeMiddle.setEnabled(isEnable);
        binding.tvVolumeLow.setEnabled(isEnable);
        binding.tvVolumeSilent.setEnabled(isEnable);
        binding.tvRotationModePositive.setEnabled(isEnable);
        binding.tvRotationModeReversal.setEnabled(isEnable);
        binding.tvRotationModeAuto.setEnabled(isEnable);
        if (!isEnable) {
            binding.switchWarm.setChecked(false);
        }
    }

    private final BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {

        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == STATUS_CONNECTED) {
                builderBle.build().dismiss();
            } else if (status == STATUS_DISCONNECTED) {
                builderBle.show();
            }
        }
    };

    private void controlFastOption(final View view) {
        view.setClickable(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setClickable(true);
            }
        }, 500);
    }

    private void setDrawableTop(TextView textView, int resId) {
        Drawable drawable = getResources().getDrawable(resId); //获取图片
        if(resId==R.drawable.inner_mode_selector){
            drawable.setBounds(0, 0, RxImageTool.dip2px(56.6f), RxImageTool.dip2px(36.6f));  //设置图片参数
        }else if((resId==R.drawable.outer_mode_selector)){
            drawable.setBounds(0, 0, RxImageTool.dip2px(56), RxImageTool.dip2px(36.6f));  //设置图片参数
        }else{
            drawable.setBounds(0, 0, RxImageTool.dip2px(36), RxImageTool.dip2px(36.6f));  //设置图片参数
        }
        textView .setCompoundDrawables(null,drawable,null,null);
    }

    @Override
    public void initViewObservable() {
        binding.switchOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                controlFastOption(binding.switchOpen);
                DeviceStatusInfoEntity deviceStatusInfoEntity = viewModel.statusInfoChageObeserver.get();
                Boolean aBoolean = (isChecked == (deviceStatusInfoEntity.getIsDeviceOpen() == DeviceStatusInfoEntity.FLAG_TRUE));
                if (!aBoolean) {
                    viewModel.setPowerSwitchStatus(isChecked);
                }

            }
        });
        binding.switchWarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                RxLogTool.e("onCheckedChanged", "switchWarm. isClickable is " + binding.switchWarm.isClickable());
                controlFastOption(binding.switchWarm);
                DeviceStatusInfoEntity deviceStatusInfoEntity = viewModel.statusInfoChageObeserver.get();
                Boolean aBoolean = (isChecked == (deviceStatusInfoEntity.getIsHeatingOpen() == DeviceStatusInfoEntity.FLAG_TRUE));
                if (!aBoolean) {
                    viewModel.setWarmSwitchStatus(isChecked);
                }

            }
        });

        viewModel.writeDataResult.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!viewModel.writeDataResult.get()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showLong("操作失败，请重试!");
                        }
                    });
                }
            }
        });

        viewModel.statusInfoChageObeserver.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Log.e(TAG, "onPropertyChanged " + viewModel.statusInfoChageObeserver.get());
                DeviceStatusInfoEntity deviceStatusInfoEntity = viewModel.statusInfoChageObeserver.get();
                if (deviceStatusInfoEntity == null) {
                    binding.ivDevicecontrolAllBg.setImageResource(R.mipmap.pillow);
                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
                    binding.ivDevicecontrolBg.setVisibility(View.VISIBLE);
                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
                    return;
                }
                if (deviceStatusInfoEntity.getIsDeviceOpen() == DeviceStatusInfoEntity.FLAG_FALSE) {
                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
                    binding.ivDevicecontrolAllBg.setImageResource(R.mipmap.pillow);
                    binding.ivDevicecontrolBg.setVisibility(View.VISIBLE);
                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);

                    deviceStatusInfoEntity.setIsHeatingOpen(DeviceStatusInfoEntity.FLAG_FALSE);
                    viewModel.statusInfoChageObeserver.set(deviceStatusInfoEntity);
                    binding.tvRotationModePositive.setChecked(false);
                    binding.tvRotationModeReversal.setChecked(false);
                    binding.tvRotationModeAuto.setChecked(false);
                    setAllOptionEnable(false);
                } else {
                    setAllOptionEnable(true);
                    binding.ivDevicecontrolGif.setVisibility(View.VISIBLE);
                    binding.ivDevicecontrolBg.setVisibility(View.INVISIBLE);
                    binding.ivDevicecontrolAllBg.setImageResource(deviceStatusInfoEntity.getIsHeatingOpen() == DeviceStatusInfoEntity.FLAG_TRUE ? R.mipmap.warmpillow : R.mipmap.pillow);
                    binding.switchOpen.setChecked(deviceStatusInfoEntity.getIsDeviceOpen() == DeviceStatusInfoEntity.FLAG_TRUE);
                    binding.switchWarm.setChecked(deviceStatusInfoEntity.getIsHeatingOpen() == DeviceStatusInfoEntity.FLAG_TRUE);
                    binding.tvRotationHigh.setChecked(deviceStatusInfoEntity.getDeviceSpeed() == DeviceStatusInfoEntity.FLAG_SPEED_MAX);
                    binding.tvRotationMid.setChecked(deviceStatusInfoEntity.getDeviceSpeed() == DeviceStatusInfoEntity.FLAG_SPEED_MID);
                    binding.tvRotationLow.setChecked(deviceStatusInfoEntity.getDeviceSpeed() == DeviceStatusInfoEntity.FLAG_SPEED_MIN);
                    binding.tvVolumeHigh.setChecked(deviceStatusInfoEntity.getDeviceVoice() == DeviceStatusInfoEntity.FLAG_VOICE_MAX);
                    binding.tvVolumeMiddle.setChecked(deviceStatusInfoEntity.getDeviceVoice() == DeviceStatusInfoEntity.FLAG_VOICE_MID);
                    binding.tvVolumeLow.setChecked(deviceStatusInfoEntity.getDeviceVoice() == DeviceStatusInfoEntity.FLAG_VOICE_MIN);
                    binding.tvVolumeSilent.setChecked(deviceStatusInfoEntity.getDeviceVoice() == DeviceStatusInfoEntity.FLAG_VOICE_MUTE);
                    setRoationModeGif(deviceStatusInfoEntity);
                    if (viewModel.isGifNeedChange()) {
                        setGifRes(deviceStatusInfoEntity);
                    }
                }

            }
        });
        viewModel.statusRoationModeUserChageObeserver.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.statusRoationModeUserChageObeserver.get().getIsDeviceOpen() == DeviceStatusInfoEntity.FLAG_FALSE) {
                    return;
                }
                boolean isRotationModePositive = viewModel.statusRoationModeUserChageObeserver.get().getDeviceRoationMode() == DeviceStatusInfoEntity.FLAG_ROATION_POSISTION;
                boolean isRotationModeReversal = viewModel.statusRoationModeUserChageObeserver.get().getDeviceRoationMode() == DeviceStatusInfoEntity.FLAG_ROATION_REV;
                boolean isRotationModeAuto = viewModel.statusRoationModeUserChageObeserver.get().getDeviceRoationMode() == DeviceStatusInfoEntity.FLAG_ROATION_AUTO ||
                        viewModel.statusRoationModeUserChageObeserver.get().getDeviceRoationMode() == DeviceStatusInfoEntity.FLAG_ROATION_POSISTION_AND_REV;
                setRoationModeGifVisiable(binding.innerModeGif, isRotationModePositive);
                setRoationModeGifVisiable(binding.outerModeGif, isRotationModeReversal);
                setRoationModeGifVisiable(binding.autoModeGif, isRotationModeAuto);
            }
        });
    }

    @Override
    public void OnBannerClick(int position) {
//        ToastUtils.showLong("点击了 "+position);
    }
}
