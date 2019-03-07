package com.wang.adapters.interfaceabstract;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * 所有list的adapter的接口
 */
public interface IAdapterList<BEAN> extends IAdapter {

    List<BEAN> getList();

    void setListAndNotifyDataSetChanged(List<BEAN> list);

    /**
     * 获取指定bean
     */
    @NonNull
    BEAN get(int listPosition);

    /**
     * 清空list,不刷新adapter
     */
    void clear();

    /**
     * 添加全部条目,不刷新adapter
     */
    void addAll(@NonNull List<BEAN> addList);
}
