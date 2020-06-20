package com.myzr.allproduct.utils.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author Areo
 * @description:
 * @date : 2019/12/24 0:05
 */
public class DefineHeightRecycleListView extends RecyclerView {

    public DefineHeightRecycleListView(@NonNull Context context) {
        super(context);
    }

    public DefineHeightRecycleListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DefineHeightRecycleListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec,  expandSpec);
    }

}
