package com.wang.adapters.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.wang.adapters.interfaces.OnItemClickListener;
import com.wang.container.interfaces.IListAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 无限滑动的adapter
 */
public abstract class BaseAdapterRvListCycle<T extends ViewDataBinding, BEAN> extends BaseAdapterRv<T>
        implements IListAdapter<BEAN, BaseViewHolder<T>, OnItemClickListener> {

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
        return getList().isEmpty() ? 0 : Integer.MAX_VALUE;
    }

    @Override
    protected final void onBindViewHolder2(@NonNull BaseViewHolder<T> holder, int position) {
        //对position进行了%处理
        position = position % getList().size();
        onBindViewHolder3(holder, position, getList().get(position));
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

    ///////////////////////////////////////////////////////////////////////////
    // 以下是增加的方法
    ///////////////////////////////////////////////////////////////////////////

    protected abstract void onBindViewHolder3(BaseViewHolder<T> holder, int position, BEAN bean);
}
