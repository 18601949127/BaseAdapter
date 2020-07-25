package com.wang.adapters.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wang.adapters.interfaces.OnItemClickListener;
import com.wang.container.interfaces.IListAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 无限循环滑动的adapter（主要适用于vp）
 */
public abstract class BaseAdapterLvsListCycle<VH extends BaseViewHolder, BEAN> extends BaseAdapterLvs<VH>
        implements IListAdapter<BEAN, VH, OnItemClickListener> {

    @NonNull
    private List<BEAN> mList;

    public BaseAdapterLvsListCycle() {
        this(null);
    }

    public BaseAdapterLvsListCycle(@Nullable List<BEAN> list) {
        mList = list == null ? new ArrayList<>() : list;
    }

    @Override
    public final int getItemCount() {
        return getList().isEmpty() ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        //对position进行了%处理
        position = position % getList().size();
        onBindViewHolder2(holder, position, getList().get(position));
    }

    /**
     * 不支持header、footer
     */
    @Override
    public void setHeaderView(@Nullable View view) {
    }

    @Nullable
    @Override
    public View getHeaderView() {
        return null;
    }

    @Override
    public void setFooterView(@Nullable View view) {
    }

    @Nullable
    @Override
    public View getFooterView() {
        return null;
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
        return getList().get(listPosition % getList().size());
    }

    /**
     * 清空list,不刷新adapter
     */
    public void clear() {
        getList().clear();
    }

    /**
     * 添加全部条目,不刷新adapter
     */
    public void addAll(@Nullable Collection<? extends BEAN> addList) {
        if (addList != null) {
            getList().addAll(addList);
        }
    }

    @Override
    public int size() {
        return getList().size();
    }

    @Override
    public boolean isEmptyList() {
        return getList().isEmpty();
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
