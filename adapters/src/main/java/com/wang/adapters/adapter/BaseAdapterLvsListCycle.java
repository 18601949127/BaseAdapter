package com.wang.adapters.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wang.adapters.interfaces.IAdapterItemClick;
import com.wang.container.interfaces.IListAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 无限循环滑动的adapter（主要适用于vp）
 */
public abstract class BaseAdapterLvsListCycle<VH extends BaseViewHolder, BEAN> extends BaseAdapterLvs<VH> implements IListAdapter<BEAN, VH, IAdapterItemClick> {

    @NonNull
    public List<BEAN> mList;

    public BaseAdapterLvsListCycle() {
        this(null);
    }

    public BaseAdapterLvsListCycle(@Nullable List<BEAN> list) {
        mList = list == null ? new ArrayList<>() : list;
    }

    @Override
    public final int getItemCount() {
        return mList.isEmpty() ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        //对position进行了%处理
        position = position % mList.size();
        onBindViewHolder2(holder, position, mList.get(position));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // list相关的方法，其他方法请使用getList进行操作
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return 注意list是否传了null或者根本没传
     */
    @NonNull
    @Override
    public List<BEAN> getList() {
        return mList;
    }

    /**
     * 获取指定bean
     */
    @NonNull
    public BEAN get(int listPosition) {
        return mList.get(listPosition % mList.size());
    }

    /**
     * 清空list,不刷新adapter
     */
    public void clear() {
        mList.clear();
    }

    /**
     * 添加全部条目,不刷新adapter
     */
    public void addAll(@Nullable Collection<? extends BEAN> addList) {
        if (addList != null) {
            mList.addAll(addList);
        }
    }

    @Override
    public int size() {
        return mList.size();
    }

    @Override
    public boolean isEmptyList() {
        return mList.isEmpty();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 以下是增加的方法
    ///////////////////////////////////////////////////////////////////////////

    protected abstract void onBindViewHolder2(VH holder, int position, BEAN bean);

    public void setListAndNotifyDataSetChanged(@Nullable List<BEAN> list) {
        mList = list == null ? new ArrayList<>() : list;
        notifyDataSetChanged();
    }
}
