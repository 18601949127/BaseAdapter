package com.wang.adapters.adapter;

import android.annotation.SuppressLint;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 所有ViewHolder的基类
 * 和经典的viewHolder类似,更方便
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    @Nullable
    protected SparseArray<View> mViews;
    /**
     * 主要是因为构造必须传View才加成静态的,用完就置null
     */
    @SuppressLint("StaticFieldLeak")
    static ViewGroup cacheParent = null;

    /**
     * 工具生成的一般都是这个
     */
    public BaseViewHolder(@NonNull View view) {
        super(view);
    }

    /**
     * listAdapter省去createViewHolder的专属构造
     */
    protected BaseViewHolder(@LayoutRes int layoutId) {
        this(getItemView(layoutId));
    }

    private static View getItemView(@LayoutRes int layoutId) {
        LayoutInflater view = LayoutInflater.from(cacheParent.getContext());
        cacheParent = null;
        return view.inflate(layoutId, cacheParent, false);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 公共方法
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public <T extends View> T getView(@IdRes int resId) {
        if (mViews == null) {
            mViews = new SparseArray<>();
        }
        View view = mViews.get(resId);
        if (view == null) {
            view = itemView.findViewById(resId);
            mViews.put(resId, view);
        }
        //noinspection unchecked
        return (T) view;
    }

    public BaseViewHolder setText(@IdRes int resId, CharSequence st) {
        ((TextView) getView(resId)).setText(st);
        return this;
    }

    public BaseViewHolder setImage(@IdRes int resId, @DrawableRes int drawableId) {
        ((ImageView) this.getView(resId)).setImageResource(drawableId);
        return this;
    }
}