package com.myzr.allproduct.ui.viewpager.adapter;

import androidx.databinding.ViewDataBinding;

import com.myzr.allproduct.ui.network.NetWorkItemViewModel;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;

/**
 * Created by goldze on 2018/6/21.
 */

public class NetWorkBindingAdapter extends BindingRecyclerViewAdapter<NetWorkItemViewModel> {

    @Override
    public void onBindBinding(final ViewDataBinding binding, int variableId, int layoutRes, final int position, NetWorkItemViewModel item) {
        super.onBindBinding(binding, variableId, layoutRes, position, item);
        //这里可以强转成DeviceListItemViewModel对应的ViewDataBinding，
        //ItemViewpagerBinding _binding = (ItemViewpagerBinding) binding;
    }

}
