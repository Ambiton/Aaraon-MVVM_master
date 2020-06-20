package com.myzr.allproducts.ui.viewpager.adapter;

import androidx.databinding.ViewDataBinding;

import com.myzr.allproducts.ui.main.DeviceListItemViewModel;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;

/**
 * Created by goldze on 2018/6/21.
 */

public class DeviceListBindingAdapter extends BindingRecyclerViewAdapter<DeviceListItemViewModel> {

    @Override
    public void onBindBinding(final ViewDataBinding binding, int variableId, int layoutRes, final int position, DeviceListItemViewModel item) {
        super.onBindBinding(binding, variableId, layoutRes, position, item);
    }

}
