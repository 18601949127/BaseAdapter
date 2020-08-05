package com.wang.adapters.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 所有ViewHolder的基类
 */
public class BaseViewHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder {
    private int mLvPosition = RecyclerView.NO_POSITION;

    /**
     * null不null自己知道
     */
    private DB mBinding;

    public BaseViewHolder(@NonNull DB binding) {
        this(binding.getRoot());
        mBinding = binding;
    }

    public BaseViewHolder(@NonNull View view) {
        super(view);
    }

    public DB getBinding() {
        return mBinding;
    }

    /**
     * lv和rv都调用这个
     */
    public int getCommonPosition() {
        if (mLvPosition >= 0) {
            return mLvPosition;
        }
        int adapterPosition = getAdapterPosition();
        if (adapterPosition >= 0) {
            return adapterPosition;
        }
        return getLayoutPosition();
    }

    /**
     * listView需要手动设置position
     */
    public void setLvPosition(int position) {
        mLvPosition = position;
    }
}