package com.wang.adapters.interfaces;

import android.view.View;

import androidx.annotation.NonNull;

import com.wang.container.interfaces.IItemClick;

/**
 * OnItemClickListener的接口
 * 见子类实现{@link OnItemClickListener}{@link OnItemItemClickListener}
 */
public interface IAdapterItemClick extends View.OnClickListener, View.OnLongClickListener, IItemClick {

    /**
     * item被点击时
     *
     * @param position 属于该adapter的position
     */
    void onItemClick(@NonNull View view, int position);

    /**
     * item被长按时
     */
    boolean onItemLongClick(@NonNull View view, int position);
}