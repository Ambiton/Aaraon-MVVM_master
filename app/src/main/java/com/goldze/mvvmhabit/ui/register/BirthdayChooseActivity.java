package com.goldze.mvvmhabit.ui.register;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.configure.PickerOptions;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.databinding.ActivityUserbirthdayEditBinding;
import com.goldze.mvvmhabit.ui.wheelview.MyTimePickerView;
import com.tamsiree.rxtool.RxLogTool;

import java.util.Calendar;
import java.util.Date;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * @author Areo
 * @description:生日信息编辑界面
 * @date : 2019/12/14 20:25
 */
public class BirthdayChooseActivity extends BaseActivity<ActivityUserbirthdayEditBinding, BirthdayChooseViewModel> implements OnTimeSelectChangeListener {
    private static final String TAG = "BirthdayChooseActivity";

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
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(1900, 0, 1);
        endDate.set(2050, 11, 31);

        PickerOptions mPickerOptions = new PickerOptions(PickerOptions.TYPE_PICKER_TIME);
        mPickerOptions.timeSelectChangeListener = this;
        mPickerOptions.startDate = startDate;
        mPickerOptions.endDate = endDate;
        mPickerOptions.itemsVisibleCount = 5;
        mPickerOptions.context = this;
        mPickerOptions.date = Calendar.getInstance();
        mPickerOptions.textSizeContent = 25;
        mPickerOptions.textColorCenter = this.getResources().getColor(R.color.short_red);
        mPickerOptions.lineSpacingMultiplier = 2.5f;

        MyTimePickerView myTimePickerView = new MyTimePickerView(mPickerOptions, binding.wheelview);
    }

    @Override
    public void onTimeSelectChanged(Date date) {
        RxLogTool.e(TAG, "select date is " + date.toString());

    }
}
