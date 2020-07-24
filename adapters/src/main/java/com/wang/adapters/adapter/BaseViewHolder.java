package com.wang.adapters.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 所有ViewHolder的基类
 */
public class BaseViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    /**
     * null不null自己知道
     */
    private T mBinding;

    public BaseViewHolder(@NonNull T binding) {
        this(binding.getRoot());
        mBinding = binding;
    }

    public BaseViewHolder(@NonNull View view) {
        super(view);
    }

    public T getBinding() {
        return mBinding;
    }
}