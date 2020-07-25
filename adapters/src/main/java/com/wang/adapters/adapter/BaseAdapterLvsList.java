package com.wang.adapters.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wang.adapters.interfaces.OnItemClickListener;
import com.wang.container.interfaces.IListAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 和{@link BaseAdapterRvList}基本一致，适用于listView、gridView、viewPager
 */
public abstract class BaseAdapterLvsList<VH extends BaseViewHolder, BEAN> extends BaseAdapterLvs<BaseViewHolder>
        implements IListAdapter<BEAN, BaseViewHolder, OnItemClickListener> {

    @NonNull
    private List<BEAN> mList;

    @Nullable
    private View mHeaderView, mFooterView;

    public BaseAdapterLvsList() {
        this(null);
    }

    /**
     * @param list 内部维护了list，可以传null
     */
    public BaseAdapterLvsList(@Nullable List<BEAN> list) {
        mList = list == null ? new ArrayList<>() : list;
    }

    @Override
    public final int getItemCount() {
        int count = 0;
        if (getHeaderView() != null) {
            count++;
        }
        if (getFooterView() != null) {
            count++;
        }
        count += getList().size();
        return count;
    }

    @Override
    public final void onBindViewHolder(BaseViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case BaseAdapterRvList.TYPE_HEADER:
            case BaseAdapterRvList.TYPE_FOOTER:
                break;
            case BaseAdapterRvList.TYPE_BODY:
                if (getHeaderView() != null) {
                    position--;
                }
                //noinspection unchecked
                onBindViewHolder2((VH) holder, position, getList().get(position));
                break;
            default:
                throw new RuntimeException("仅支持header、footer和body,想拓展请使用BaseAdapterLvs");
        }
    }

    @NonNull
    @Override
    public final BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @BaseAdapterRvList.AdapterListType int viewType) {
        switch (viewType) {
            case BaseAdapterRvList.TYPE_HEADER:
                //noinspection ConstantConditions 这里当然不会为null
                return new BaseViewHolder(getHeaderView());
            case BaseAdapterRvList.TYPE_FOOTER:
                //noinspection ConstantConditions 这里当然不会为null
                return new BaseViewHolder(getFooterView());
            case BaseAdapterRvList.TYPE_BODY:
                return onCreateViewHolder2(parent);
            default:
                throw new RuntimeException("仅支持header、footer和body,想拓展请使用BaseAdapterLvs");
        }
    }

    @BaseAdapterRvList.AdapterListType
    @Override
    public final int getItemViewType(int position) {
        if (getHeaderView() != null && position == 0) {
            return BaseAdapterRvList.TYPE_HEADER;
        }
        if (getFooterView() != null && getItemCount() == position + 1) {
            return BaseAdapterRvList.TYPE_FOOTER;
        }
        return BaseAdapterRvList.TYPE_BODY;
    }

    /**
     * 几百年没用，居然忘了lv还必须重写这个方法了
     */
    @Override
    public int getViewTypeCount() {
        return 3;
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
        if (listPosition < getList().size()) {
            return getList().get(listPosition);
        }
        throw new RuntimeException("lit为空或指针越界");
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

    /**
     * @param listPosition 已经做过处理,就是list的position
     */
    protected abstract void onBindViewHolder2(VH holder, int listPosition, BEAN bean);

    @NonNull
    protected abstract VH onCreateViewHolder2(ViewGroup parent);

    public void setListAndNotifyDataSetChanged(@Nullable List<BEAN> list) {
        mList = list == null ? new ArrayList<>() : list;
        notifyDataSetChanged();
    }

    /**
     * @param view null表示删除
     */
    public void setHeaderView(@Nullable View view) {
        mHeaderView = view;
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View getHeaderView() {
        return mHeaderView;
    }

    /**
     * @param view null表示删除
     */
    public void setFooterView(@Nullable View view) {
        mFooterView = view;
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View getFooterView() {
        return mFooterView;
    }
}
