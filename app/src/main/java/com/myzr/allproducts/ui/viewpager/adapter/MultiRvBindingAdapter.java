package com.myzr.allproducts.ui.viewpager.adapter;

import androidx.databinding.ViewDataBinding;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;

/**
 * Created by goldze on 2018/6/21.
 */

public class MultiRvBindingAdapter extends BindingRecyclerViewAdapter<MultiItemViewModel> {

    @Override
    public void onBindBinding(final ViewDataBinding binding, int variableId, int layoutRes, final int position, MultiItemViewModel item) {
        super.onBindBinding(binding, variableId, layoutRes, position, item);
        //这里可以强转成DeviceListItemViewModel对应的ViewDataBinding，
       // ItemViewpagerBinding _binding = (ItemViewpagerBinding) binding;
    }

}
