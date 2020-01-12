package com.goldze.mvvmhabit.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.app.AppApplication;
import com.goldze.mvvmhabit.app.AppViewModelFactory;
import com.goldze.mvvmhabit.databinding.FragmentDevicecontrolBinding;

import me.goldze.mvvmhabit.base.BaseFragment;
/**
 * Created by goldze on 2017/7/17.
 * 网络请求列表界面
 */

public class DeviceControlFragment extends BaseFragment<FragmentDevicecontrolBinding, DeviceControlViewModel> {
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
        viewModel.requestDeviceInfo();
        viewModel.initToolbar();
       //binding.ivDevicecontrolGif.setBackgroundResource("asset:inner_high_hot");
    }

    @Override
    public void initViewObservable() {
        //监听下拉刷新完成
        viewModel.uc.finishRefreshing.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //结束刷新
                binding.twinklingRefreshLayout.finishRefreshing();
            }
        });
        //监听上拉加载完成
        viewModel.uc.finishLoadmore.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //结束刷新
               binding.twinklingRefreshLayout.finishLoadmore();
            }
        });

        viewModel.uc.powerSwitch.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (viewModel.uc.powerSwitch.getValue()!=null&&viewModel.uc.powerSwitch.getValue()) {
                    binding.ivDevicecontrolBg.setImageResource(R.drawable.pillow);
                    if(viewModel.uc.warmSwitch.getValue()!=null&&viewModel.uc.warmSwitch.getValue()){
                        binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_high_hot);
                    }else{
                        binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_high_normal);
                    }
                    binding.ivDevicecontrolGif.setVisibility(View.VISIBLE);
                } else {
                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
                }
            }
        });

        viewModel.uc.warmSwitch.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(viewModel.uc.powerSwitch.getValue()==null||!viewModel.uc.powerSwitch.getValue()){
                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
                    return;
                }
                binding.ivDevicecontrolGif.setVisibility(View.VISIBLE);
                if (viewModel.uc.warmSwitch.getValue()!=null&&viewModel.uc.warmSwitch.getValue()) {
                    //密码可见
                    //在xml中定义id后,使用binding可以直接拿到这个view的引用,不再需要findViewById去找控件了
                    binding.ivDevicecontrolBg.setImageResource(R.drawable.pillow);
                    binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_high_hot);
                } else {
                    //密码不可见
                    binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_high_normal);
                }
            }
        });

        viewModel.uc.highSpeedSwitch.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                if(viewModel.uc.powerSwitch.getValue()==null||!viewModel.uc.powerSwitch.getValue()){
                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
                    return;
                }
                binding.tvRotationLow.setChecked(false);
                binding.tvRotationMid.setChecked(true);
                binding.tvRotationLow.setChecked(false);
                binding.ivDevicecontrolGif.setVisibility(View.VISIBLE);
                binding.ivDevicecontrolBg.setImageResource(R.drawable.pillow);
                if (viewModel.uc.warmSwitch.getValue()!=null&&viewModel.uc.warmSwitch.getValue()) {
                    binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_high_hot);
                } else {
                    binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_high_normal);
                }
            }
        });

        viewModel.uc.midSpeedSwitch.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                if(viewModel.uc.powerSwitch.getValue()==null||!viewModel.uc.powerSwitch.getValue()){
                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
                    return;
                }
                binding.tvRotationLow.setChecked(false);
                binding.tvRotationMid.setChecked(true);
                binding.tvRotationLow.setChecked(false);
                binding.ivDevicecontrolGif.setVisibility(View.VISIBLE);
                binding.ivDevicecontrolBg.setImageResource(R.drawable.pillow);
                if (viewModel.uc.warmSwitch.getValue()!=null&&viewModel.uc.warmSwitch.getValue()) {
                    binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_mid_hot);
                } else {
                    binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_mid_normal);
                }
            }
        });

        viewModel.uc.lowSpeedSwitch.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                if(viewModel.uc.powerSwitch.getValue()==null||!viewModel.uc.powerSwitch.getValue()){
                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
                    return;
                }
                binding.tvRotationLow.setChecked(true);
                binding.tvRotationMid.setChecked(false);
                binding.tvRotationLow.setChecked(false);
                binding.ivDevicecontrolGif.setVisibility(View.VISIBLE);
                binding.ivDevicecontrolBg.setImageResource(R.drawable.pillow);
                if (viewModel.uc.warmSwitch.getValue()!=null&&viewModel.uc.warmSwitch.getValue()) {
                    binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_low_hot);
                } else {
                    binding.ivDevicecontrolGif.setImageResource(R.drawable.inner_low_normal);
                }
            }
        });

        viewModel.uc.highVolSwitch.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                if(viewModel.uc.powerSwitch.getValue()==null||!viewModel.uc.powerSwitch.getValue()){
                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
                    return;
                }
                binding.tvVolumeHigh.setChecked(true);
                binding.tvVolumeMiddle.setChecked(false);
                binding.tvVolumeLow.setChecked(false);
                binding.tvVolumeSilent.setChecked(false);
            }
        });
        viewModel.uc.midVolSwitch.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                if(viewModel.uc.powerSwitch.getValue()==null||!viewModel.uc.powerSwitch.getValue()){
                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
                    return;
                }
                binding.tvVolumeHigh.setChecked(false);
                binding.tvVolumeMiddle.setChecked(true);
                binding.tvVolumeLow.setChecked(false);
                binding.tvVolumeSilent.setChecked(false);
            }
        });

        viewModel.uc.lowVolSwitch.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                if(viewModel.uc.powerSwitch.getValue()==null||!viewModel.uc.powerSwitch.getValue()){
                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
                    return;
                }
                binding.tvVolumeHigh.setChecked(false);
                binding.tvVolumeMiddle.setChecked(false);
                binding.tvVolumeLow.setChecked(true);
                binding.tvVolumeSilent.setChecked(false);
            }
        });

        viewModel.uc.muteVolSwitch.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                if(viewModel.uc.powerSwitch.getValue()==null||!viewModel.uc.powerSwitch.getValue()){
                    binding.ivDevicecontrolBg.setImageResource(R.drawable.stop);
                    binding.ivDevicecontrolGif.setVisibility(View.INVISIBLE);
                    return;
                }
                binding.tvVolumeHigh.setChecked(false);
                binding.tvVolumeMiddle.setChecked(false);
                binding.tvVolumeLow.setChecked(false);
                binding.tvVolumeSilent.setChecked(true);
            }
        });
    }
}
