package com.goldze.mvvmhabit.app;

import android.annotation.SuppressLint;
import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.goldze.mvvmhabit.data.DemoRepository;
import com.goldze.mvvmhabit.ui.login.LoginViewModel;
import com.goldze.mvvmhabit.ui.main.DeviceControlViewModel;
import com.goldze.mvvmhabit.ui.main.DeviceListViewModel;
import com.goldze.mvvmhabit.ui.main.LoadingViewModel;
import com.goldze.mvvmhabit.ui.main.RegisterViewModel;
import com.goldze.mvvmhabit.ui.network.NetWorkViewModel;
import com.goldze.mvvmhabit.ui.register.UserHeightActivity;
import com.goldze.mvvmhabit.ui.register.UserHeightViewModel;

/**
 * Created by goldze on 2019/3/26.
 */
public class AppViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile AppViewModelFactory INSTANCE;
    private final Application mApplication;
    private final DemoRepository mRepository;

    public static AppViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (AppViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppViewModelFactory(application, Injection.provideDemoRepository());
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private AppViewModelFactory(Application application, DemoRepository repository) {
        this.mApplication = application;
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NetWorkViewModel.class)) {
            return (T) new NetWorkViewModel(mApplication, mRepository);
        }  if (modelClass.isAssignableFrom(DeviceListViewModel.class)) {
            return (T) new DeviceListViewModel(mApplication, mRepository);
        } if (modelClass.isAssignableFrom(DeviceControlViewModel.class)) {
            return (T) new DeviceControlViewModel(mApplication, mRepository);
        } else if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(RegisterViewModel.class)) {
            return (T) new RegisterViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(LoadingViewModel.class)) {
            return (T) new LoadingViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(UserHeightViewModel.class)) {
            return (T) new UserHeightViewModel(mApplication, mRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
