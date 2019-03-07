package com.wang.adapters.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wang.adapters.base.BaseViewHolder;
import com.wang.adapters.interfaceabstract.IAdapterList;
import com.wang.adapters.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 无限滑动的adapter（主要适用于vp）
 */
public abstract class BaseAdapterLvsListMaxint<VH extends BaseViewHolder, BEAN> extends BaseAdapterLvs<VH> implements IAdapterList<BEAN> {

    public List<BEAN> mList;

    public BaseAdapterLvsListMaxint(@NonNull Activity activity, @Nullable List<BEAN> list) {
        super(activity);
        mList = list;
    }

    @Override
    public final int getItemCount() {
        return (Utils.isEmptyArray(mList)) ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        //对position进行了%处理
        position = position % mList.size();
        onBindVH(holder, position, mList.get(position));
    }

    @Override
    public List<BEAN> getList() {
        return mList;
    }

    /**
     * 获取指定bean
     *
     * @throws Exception list为空
     */
    @NonNull
    public BEAN get(int listPosition) {
        return mList.get(listPosition % mList.size());
    }

    /**
     * 清空list,不刷新adapter
     */
    public void clear() {
        if (mList != null) mList.clear();
    }

    /**
     * 添加全部条目,不刷新adapter
     */
    public void addAll(@NonNull List<BEAN> addList) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.addAll(addList);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 以下是增加的方法
    ///////////////////////////////////////////////////////////////////////////

    protected abstract void onBindVH(VH holder, int position, BEAN bean);

    public void setListAndNotifyDataSetChanged(List<BEAN> list) {
        mList = list;
        notifyDataSetChanged();
    }
}
