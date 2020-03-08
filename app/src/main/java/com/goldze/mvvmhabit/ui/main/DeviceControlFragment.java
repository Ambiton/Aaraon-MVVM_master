package com.goldze.mvvmhabit.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.ActivityInfo;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.app.AppApplication;
import com.goldze.mvvmhabit.app.AppViewModelFactory;
import com.goldze.mvvmhabit.databinding.FragmentDevicecontrolBinding;
import com.goldze.mvvmhabit.entity.DeviceInfoEntity;
import com.goldze.mvvmhabit.entity.DeviceStatusInfoEntity;
import com.goldze.mvvmhabit.ui.banner.DataBean;
import com.goldze.mvvmhabit.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;


import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2017/7/17.
 * 网络请求列表界面
 */

public class DeviceControlFragment extends BaseFragment<FragmentDevicecontrolBinding, DeviceControlViewModel> implements OnBannerListener {
    private static final String TAG = "DeviceControlFragment";

    @Override
    public void initParam() {
        super.initParam();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        // Adapter属于View层的东西, 不建议定义到ViewModel中绑定，以免内存泄漏
        //请求设备信息数据
        viewModel.initBleEvent();
        viewModel.requestDeviceInfo();
        viewModel.initToolbar();

        startBanner();
        //binding.ivDevicecontrolGif.setBackgroundResource("asset:inner_high_hot");
    }

    private void startBanner() {
        Banner banner=binding.bannerControl;
        //放标题的集合
        ArrayList<String> list_title = new ArrayList<>();
        ArrayList<String> list_path = new ArrayList<>();
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg");
        list_title.add("好好学习");
        list_title.add("天天向上");
        list_title.add("热爱劳动");
        list_title.add("美好愿景");
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setImages(list_path);

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

    /**
     * 根据当前的设备信息决定使用何种动态图
     *
     * @param deviceStatusInfoEntity
     * @return
     */
    private int getGifRes(DeviceStatusInfoEntity deviceStatusInfoEntity) {
        int res = R.drawable.stop;
        if (deviceStatusInfoEntity.getIsHeatingOpen() == DeviceStatusInfoEntity.FLAG_TRUE) {
            if (deviceStatusInfoEntity.getDeviceRoation() == DeviceStatusInfoEntity.FLAG_ROATION_AUTO) {
                if (deviceStatusInfoEntity.getDeviceSpeed() == DeviceStatusInfoEntity.FLAG_SPEED_MAX) {
                    res = R.drawable.auto_high_hot;
                } else if (deviceStatusInfoEntity.getDeviceSpeed() == DeviceStatusInfoEntity.FLAG_SPEED_MID) {
                    res = R.drawable.auto_mid_hot;
                } else {
                    res = R.drawable.auto_low_hot;
                }
            } else if (deviceStatusInfoEntity.getDeviceRoation() == DeviceStatusInfoEntity.FLAG_ROATION_POSISTION) {
                if (deviceStatusInfoEntity.getDeviceSpeed() == DeviceStatusInfoEntity.FLAG_SPEED_MAX) {
                    res = R.drawable.inner_high_hot;
                } else if (deviceStatusInfoEntity.getDeviceSpeed() == DeviceStatusInfoEntity.FLAG_SPEED_MID) {
                    res = R.drawable.inner_mid_hot;
                } else {
                    res = R.drawable.inner_low_hot;
                }
            } else {
                if (deviceStatusInfoEntity.getDeviceSpeed() == DeviceStatusInfoEntity.FLAG_SPEED_MAX) {
                    res = R.drawable.outer_high_hot;
                } else if (deviceStatusInfoEntity.getDeviceSpeed() == DeviceStatusInfoEntity.FLAG_SPEED_MID) {
                    res = R.drawable.outer_mid_hot;
                } else {
                    res = R.drawable.outer_low_hot;
                }
            }
        } else {
            if (deviceStatusInfoEntity.getDeviceRoation() == DeviceStatusInfoEntity.FLAG_ROATION_AUTO) {
                if (deviceStatusInfoEntity.getDeviceSpeed() == DeviceStatusInfoEntity.FLAG_SPEED_MAX) {
                    res = R.drawable.auto_high_normal;
                } else if (deviceStatusInfoEntity.getDeviceSpeed() == DeviceStatusInfoEntity.FLAG_SPEED_MID) {
                    res = R.drawable.auto_mid_normal;
                } else {
                    res = R.drawable.auto_low_normal;
                }
            } else if (deviceStatusInfoEntity.getDeviceRoation() == DeviceStatusInfoEntity.FLAG_ROATION_POSISTION) {
                if (deviceStatusInfoEntity.getDeviceSpeed() == DeviceStatusInfoEntity.FLAG_SPEED_MAX) {
                    res = R.drawable.inner_high_normal;
                } else if (deviceStatusInfoEntity.getDeviceSpeed() == DeviceStatusInfoEntity.FLAG_SPEED_MID) {
                    res = R.drawable.inner_mid_normal;
                } else {
                    res = R.drawable.inner_low_normal;
                }
            } else {
                if (deviceStatusInfoEntity.getDeviceSpeed() == DeviceStatusInfoEntity.FLAG_SPEED_MAX) {
                    res = R.drawable.outer_high_normal;
                } else if (deviceStatusInfoEntity.getDeviceSpeed() == DeviceStatusInfoEntity.FLAG_SPEED_MID) {
                    res = R.drawable.outer_mid_normal;
                } else {
                    res = R.drawable.outer_low_normal;
                }
            }
        }
        return res;
    }

    private void setAllOptionEnable(boolean isEnable){
        binding.switchWarm.setEnabled(isEnable);
        binding.tvRotationHigh.setEnabled(isEnable);
        binding.tvRotationMid.setEnabled(isEnable);
        binding.tvRotationLow.setEnabled(isEnable);
        binding.tvVolumeHigh.setEnabled(isEnable);
        binding.tvVolumeMiddle.setEnabled(isEnable);
        binding.tvVolumeLow.setEnabled(isEnable);
        binding.tvVolumeSilent.setEnabled(isEnable);
    }
    @Override
    public void initViewObservable() {
        viewModel.statusInfoChageObeserver.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Log.e(TAG, "onPropertyChanged " + viewModel.statusInfoChageObeserver.get());
                DeviceStatusInfoEntity deviceStatusInfoEntity = viewModel.statusInfoChageObeserver.get();
                if (deviceStatusInfoEntity == null) {
                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
                    binding.ivDevicecontrolBg.setVisibility(View.VISIBLE);
                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
                    return;
                }
                if (deviceStatusInfoEntity.getIsDeviceOpen() == DeviceStatusInfoEntity.FLAG_FALSE) {
                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
                    binding.ivDevicecontrolBg.setVisibility(View.VISIBLE);
                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
                    setAllOptionEnable(false);
                } else {
                    setAllOptionEnable(true);
                    binding.ivDevicecontrolGif.setVisibility(View.VISIBLE);
                    binding.ivDevicecontrolBg.setVisibility(View.INVISIBLE);
                    binding.switchOpen.setChecked(deviceStatusInfoEntity.getIsDeviceOpen() == DeviceStatusInfoEntity.FLAG_TRUE);
                    binding.switchWarm.setChecked(deviceStatusInfoEntity.getIsHeatingOpen() == DeviceStatusInfoEntity.FLAG_TRUE);
                    binding.tvRotationHigh.setChecked(deviceStatusInfoEntity.getDeviceSpeed() == DeviceStatusInfoEntity.FLAG_SPEED_MAX);
                    binding.tvRotationMid.setChecked(deviceStatusInfoEntity.getDeviceSpeed() == DeviceStatusInfoEntity.FLAG_SPEED_MID);
                    binding.tvRotationLow.setChecked(deviceStatusInfoEntity.getDeviceSpeed() == DeviceStatusInfoEntity.FLAG_SPEED_MIN);
                    binding.tvVolumeHigh.setChecked(deviceStatusInfoEntity.getDeviceVoice() == DeviceStatusInfoEntity.FLAG_VOICE_MAX);
                    binding.tvVolumeMiddle.setChecked(deviceStatusInfoEntity.getDeviceVoice() == DeviceStatusInfoEntity.FLAG_VOICE_MID);
                    binding.tvVolumeLow.setChecked(deviceStatusInfoEntity.getDeviceVoice() == DeviceStatusInfoEntity.FLAG_VOICE_MIN);
                    binding.tvVolumeSilent.setChecked(deviceStatusInfoEntity.getDeviceVoice() == DeviceStatusInfoEntity.FLAG_VOICE_MUTE);
                    binding.tvRotationModePositive.setBackgroundResource(deviceStatusInfoEntity.getDeviceRoation() == DeviceStatusInfoEntity.FLAG_ROATION_POSISTION ?
                            R.drawable.orange_round_normal_left : R.drawable.white_round_normal_left);
                    binding.tvRotationModeReversal.setBackgroundResource(deviceStatusInfoEntity.getDeviceRoation() == DeviceStatusInfoEntity.FLAG_ROATION_REV ?
                            R.drawable.orange_noround_normal : R.drawable.white_noround_normal);
                    binding.tvRotationModeAuto.setBackgroundResource(deviceStatusInfoEntity.getDeviceRoation() == DeviceStatusInfoEntity.FLAG_ROATION_AUTO?
                            R.drawable.orange_round_normal_right : R.drawable.white_round_normal_right);
                    binding.ivDevicecontrolGif.setImageResource(getGifRes(deviceStatusInfoEntity));
                }

            }
        });
//        //监听下拉刷新完成
//        viewModel.uc.finishRefreshing.observe(this, new Observer() {
//            @Override
//            public void onChanged(@Nullable Object o) {
//                //结束刷新
//                binding.twinklingRefreshLayout.finishRefreshing();
//            }
//        });
//        //监听上拉加载完成
//        viewModel.uc.finishLoadmore.observe(this, new Observer() {
//            @Override
//            public void onChanged(@Nullable Object o) {
//                //结束刷新
//               binding.twinklingRefreshLayout.finishLoadmore();
//            }
//        });
//
//        viewModel.uc.powerSwitch.observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(@Nullable Boolean aBoolean) {
//                if (viewModel.uc.powerSwitch.getValue()!=null&&viewModel.uc.powerSwitch.getValue()) {
//                    binding.ivDevicecontrolBg.setImageResource(R.drawable.pillow);
//                    if(viewModel.uc.warmSwitch.getValue()!=null&&viewModel.uc.warmSwitch.getValue()){
//                        binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_high_hot);
//                    }else{
//                        binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_high_normal);
//                    }
//                    binding.ivDevicecontrolGif.setVisibility(View.VISIBLE);
//                } else {
//                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
//                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
//
//        viewModel.uc.warmSwitch.observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(@Nullable Boolean aBoolean) {
//                if(viewModel.uc.powerSwitch.getValue()==null||!viewModel.uc.powerSwitch.getValue()){
//                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
//                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
//                    return;
//                }
//                binding.ivDevicecontrolGif.setVisibility(View.VISIBLE);
//                if (viewModel.uc.warmSwitch.getValue()!=null&&viewModel.uc.warmSwitch.getValue()) {
//                    //在xml中定义id后,使用binding可以直接拿到这个view的引用,不再需要findViewById去找控件了
//                    binding.ivDevicecontrolBg.setImageResource(R.drawable.pillow);
//                    binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_high_hot);
//                } else {
//                    //密码不可见
//                    binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_high_normal);
//                }
//            }
//        });
//
//        viewModel.uc.highSpeedSwitch.observe(this, new Observer() {
//            @Override
//            public void onChanged(@Nullable Object o) {
//                if(viewModel.uc.powerSwitch.getValue()==null||!viewModel.uc.powerSwitch.getValue()){
//                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
//                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
//                    return;
//                }
//                binding.tvRotationHigh.setChecked(true);
//                binding.tvRotationMid.setChecked(false);
//                binding.tvRotationLow.setChecked(false);
//                binding.ivDevicecontrolGif.setVisibility(View.VISIBLE);
//                binding.ivDevicecontrolBg.setImageResource(R.drawable.pillow);
//                if (viewModel.uc.warmSwitch.getValue()!=null&&viewModel.uc.warmSwitch.getValue()) {
//                    binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_high_hot);
//                } else {
//                    binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_high_normal);
//                }
//            }
//        });
//
//        viewModel.uc.midSpeedSwitch.observe(this, new Observer() {
//            @Override
//            public void onChanged(@Nullable Object o) {
//                if(viewModel.uc.powerSwitch.getValue()==null||!viewModel.uc.powerSwitch.getValue()){
//                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
//                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
//                    return;
//                }
//                binding.tvRotationHigh.setChecked(false);
//                binding.tvRotationMid.setChecked(true);
//                binding.tvRotationLow.setChecked(false);
//                binding.ivDevicecontrolGif.setVisibility(View.VISIBLE);
//                binding.ivDevicecontrolBg.setImageResource(R.drawable.pillow);
//                if (viewModel.uc.warmSwitch.getValue()!=null&&viewModel.uc.warmSwitch.getValue()) {
//                    binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_mid_hot);
//                } else {
//                    binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_mid_normal);
//                }
//            }
//        });
//
//        viewModel.uc.lowSpeedSwitch.observe(this, new Observer() {
//            @Override
//            public void onChanged(@Nullable Object o) {
//                if(viewModel.uc.powerSwitch.getValue()==null||!viewModel.uc.powerSwitch.getValue()){
//                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
//                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
//                    return;
//                }
//                binding.tvRotationHigh.setChecked(false);
//                binding.tvRotationMid.setChecked(false);
//                binding.tvRotationLow.setChecked(true);
//                binding.ivDevicecontrolGif.setVisibility(View.VISIBLE);
//                binding.ivDevicecontrolBg.setImageResource(R.drawable.pillow);
//                if (viewModel.uc.warmSwitch.getValue()!=null&&viewModel.uc.warmSwitch.getValue()) {
//                    binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_low_hot);
//                } else {
//                    binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_low_normal);
//                }
//            }
//        });
//
//        viewModel.uc.highVolSwitch.observe(this, new Observer() {
//            @Override
//            public void onChanged(@Nullable Object o) {
//                if(viewModel.uc.powerSwitch.getValue()==null||!viewModel.uc.powerSwitch.getValue()){
//                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
//                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
//                    return;
//                }
//                binding.tvVolumeHigh.setChecked(true);
//                binding.tvVolumeMiddle.setChecked(false);
//                binding.tvVolumeLow.setChecked(false);
//                binding.tvVolumeSilent.setChecked(false);
//            }
//        });
//        viewModel.uc.midVolSwitch.observe(this, new Observer() {
//            @Override
//            public void onChanged(@Nullable Object o) {
//                if(viewModel.uc.powerSwitch.getValue()==null||!viewModel.uc.powerSwitch.getValue()){
//                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
//                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
//                    return;
//                }
//                binding.tvVolumeHigh.setChecked(false);
//                binding.tvVolumeMiddle.setChecked(true);
//                binding.tvVolumeLow.setChecked(false);
//                binding.tvVolumeSilent.setChecked(false);
//            }
//        });
//
//        viewModel.uc.lowVolSwitch.observe(this, new Observer() {
//            @Override
//            public void onChanged(@Nullable Object o) {
//                if(viewModel.uc.powerSwitch.getValue()==null||!viewModel.uc.powerSwitch.getValue()){
//                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
//                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
//                    return;
//                }
//                binding.tvVolumeHigh.setChecked(false);
//                binding.tvVolumeMiddle.setChecked(false);
//                binding.tvVolumeLow.setChecked(true);
//                binding.tvVolumeSilent.setChecked(false);
//            }
//        });
//
//        viewModel.uc.muteVolSwitch.observe(this, new Observer() {
//            @Override
//            public void onChanged(@Nullable Object o) {
//                if(viewModel.uc.powerSwitch.getValue()==null||!viewModel.uc.powerSwitch.getValue()){
//                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
//                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
//                    return;
//                }
//                binding.tvVolumeHigh.setChecked(false);
//                binding.tvVolumeMiddle.setChecked(false);
//                binding.tvVolumeLow.setChecked(false);
//                binding.tvVolumeSilent.setChecked(true);
//            }
//        });
    }

    @Override
    public void OnBannerClick(int position) {
        ToastUtils.showLong("点击了 "+position);
    }
}
