package com.wang.adapters.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.wang.adapters.interfaces.IAdapterItemClick;
import com.wang.container.interfaces.IListAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 无限滑动的adapter
 */
public abstract class BaseAdapterRvListCycle<T extends ViewDataBinding, BEAN> extends BaseAdapterRv<T> implements IListAdapter<BEAN, BaseViewHolder<T>, IAdapterItemClick> {

    @NonNull
    private List<BEAN> mList;

    public BaseAdapterRvListCycle() {
        this(null);
    }

    public BaseAdapterRvListCycle(@Nullable List<BEAN> list) {
        mList = list == null ? new ArrayList<>() : list;
    }

    @Override
    public final int getItemCount() {
        return mList.isEmpty() ? 0 : Integer.MAX_VALUE;
    }

    @Override
    protected final void onBindViewHolder2(BaseViewHolder<T> holder, int position) {
        //对position进行了%处理
        position = position % mList.size();
        onBindViewHolder3(holder, position, mList.get(position));
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

    protected abstract void onBindViewHolder3(BaseViewHolder<T> holder, int position, BEAN bean);

    public void setListAndNotifyDataSetChanged(@Nullable List<BEAN> list) {
        mList = list == null ? new ArrayList<>() : list;
        notifyDataSetChanged();
    }
}
