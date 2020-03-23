package com.goldze.mvvmhabit.ui.register;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.databinding.ActivityUserbirthdayEditBinding;

import java.util.Date;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * @author Areo
 * @description:生日信息编辑界面
 * @date : 2019/12/14 20:25
 */
public class BirthdayChooseActivity extends BaseActivity<ActivityUserbirthdayEditBinding, BirthdayChooseViewModel> {
    //ActivityLoginBinding类是databinding框架自定生成的,对应activity_login.xml
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_userbirthday_edit;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {
        //监听ViewModel中pSwitchObservable的变化, 当ViewModel中执行【uc.pSwitchObservable.set(!uc.pSwitchObservable.get());】时会回调该方法
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(BirthdayChooseActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Toast.makeText(BirthdayChooseActivity.this, date.toString(), Toast.LENGTH_SHORT).show();
            }
        }).isDialog(false).build();
        binding.llGroup.addView(pvTime.getDialogContainerLayout().getRootView());
    }
}
