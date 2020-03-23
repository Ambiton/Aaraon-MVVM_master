package com.goldze.mvvmhabit.ui.register;
import android.os.Bundle;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.databinding.ActivityUserweightEditBinding;
import com.tamsiree.rxtool.RxLogTool;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * @author Areo
 * @description:用户体重编辑界面
 * @date : 2019/12/14 20:25
 */
public class UserWeightActivity extends BaseActivity<ActivityUserweightEditBinding, UserWeightViewModel> {
    private static final String TAG = "UserWeightActivity";

    //ActivityLoginBinding类是databinding框架自定生成的,对应activity_login.xml
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_userweight_edit;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


    @Override
    public void initViewObservable() {
        //监听ViewModel中pSwitchObservable的变化, 当ViewModel中执行【uc.pSwitchObservable.set(!uc.pSwitchObservable.get());】时会回调该方法
        binding.wheelviewWeight.setCyclic(false);

        final List<String> mOptionsItems = new ArrayList<>();
        for (int i = 1; i < 500; i++) {
            mOptionsItems.add("" + i);
        }
        binding.wheelviewWeight.setItemsVisibleCount(5);
        binding.wheelviewWeight.setLabel("KG");

        binding.wheelviewWeight.setAdapter(new ArrayWheelAdapter(mOptionsItems));
        binding.wheelviewWeight.setTextSize(30);
        binding.wheelviewWeight.setCurrentItem(60);
        binding.wheelviewWeight.setLineSpacingMultiplier(1.8f);
        binding.wheelviewWeight.setTextColorCenter(this.getResources().getColor(R.color.short_red));
        binding.wheelviewWeight.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                RxLogTool.e(TAG, "select date is " + mOptionsItems.get(index));
            }
        });
    }
}
