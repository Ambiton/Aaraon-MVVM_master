package com.goldze.mvvmhabit.ui.register;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.databinding.ActivityUserheightEditBinding;
import com.tamsiree.rxtool.RxLogTool;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * @author Areo
 * @description:用户昵称编辑界面
 * @date : 2019/12/14 20:25
 */
public class UserHeightActivity extends BaseActivity<ActivityUserheightEditBinding, UserHeightViewModel> {
    private static final String TAG = "UserHeightActivity";
    //ActivityLoginBinding类是databinding框架自定生成的,对应activity_login.xml
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_userheight_edit;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


    @Override
    public void initViewObservable() {
        //监听ViewModel中pSwitchObservable的变化, 当ViewModel中执行【uc.pSwitchObservable.set(!uc.pSwitchObservable.get());】时会回调该方法
        binding.wheelviewHeight.setCyclic(false);

        final List<String> mOptionsItems = new ArrayList<>();
        for(int i=20;i<300;i++){
            mOptionsItems.add(""+i);
        }
        binding.wheelviewHeight.setItemsVisibleCount(5);
        binding.wheelviewHeight.setLabel("CM");

        binding.wheelviewHeight.setAdapter(new ArrayWheelAdapter(mOptionsItems));
        binding.wheelviewHeight.setTextSize(30);
        binding.wheelviewHeight.setCurrentItem(155);
        binding.wheelviewHeight.setLineSpacingMultiplier(1.8f);
        binding.wheelviewHeight.setTextColorCenter(this.getResources().getColor(R.color.short_red));
        binding.wheelviewHeight.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                RxLogTool.e(TAG, "select date is "  + mOptionsItems.get(index));
            }
        });
    }
}
