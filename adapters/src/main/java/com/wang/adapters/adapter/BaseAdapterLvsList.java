package com.wang.adapters.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wang.adapters.R;
import com.wang.adapters.interfaces.IAdapterItemClick;
import com.wang.adapters.interfaces.OnItemClickListener;
import com.wang.container.interfaces.IListAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 和{@link BaseAdapterRvList}基本一致，适用于listView、gridView、viewPager
 */
public abstract class BaseAdapterLvsList<VH extends BaseViewHolder, BEAN> extends BaseAdapterLvs<BaseViewHolder> implements IListAdapter<BEAN, BaseViewHolder, IAdapterItemClick> {

    @NonNull
    private List<BEAN> mList;

    public View mHeaderView, mFooterView;

    public BaseAdapterLvsList() {
        this(null);
    }

    public BaseAdapterLvsList(@Nullable List<BEAN> list) {
        this(list, null, null);
    }

    /**
     * @param list 内部维护了list，可以传null
     */
    public BaseAdapterLvsList(@Nullable List<BEAN> list, @Nullable View headerView, @Nullable View footerView) {
        mList = list == null ? new ArrayList<>() : list;
        mHeaderView = headerView;
        mFooterView = footerView;
    }

    @Override
    public final int getItemCount() {
        int count = 0;
        if (mHeaderView != null) {
            count++;
        }
        if (mFooterView != null) {
            count++;
        }
        count += mList.size();
        return count;
    }

    @Override
    public final void onBindViewHolder(BaseViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case BaseAdapterRvList.TYPE_HEADER:
                holder.itemView.setTag(R.id.tag_view_click, BaseAdapterRvList.POSITION_HEADER);
                break;
            case BaseAdapterRvList.TYPE_FOOTER:
                holder.itemView.setTag(R.id.tag_view_click, BaseAdapterRvList.POSITION_FOOTER);
                break;
            case BaseAdapterRvList.TYPE_BODY:
                if (mHeaderView != null) {
                    position--;
                }
                holder.itemView.setTag(R.id.tag_view_click, position);
                //noinspection unchecked
                onBindViewHolder2((VH) holder, position, mList.get(position));
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
                return new BaseViewHolder(mHeaderView);
            case BaseAdapterRvList.TYPE_FOOTER:
                return new BaseViewHolder(mFooterView);
            case BaseAdapterRvList.TYPE_BODY:
                return onCreateViewHolder2(parent);
            default:
                throw new RuntimeException("仅支持header、footer和body,想拓展请使用BaseAdapterLvs");
        }
    }

    @BaseAdapterRvList.AdapterListType
    @Override
    public final int getItemViewType(int position) {
        if (mHeaderView != null && position == 0) {
            return BaseAdapterRvList.TYPE_HEADER;
        }
        if (mFooterView != null && getItemCount() == position + 1) {
            return BaseAdapterRvList.TYPE_FOOTER;
        }
        return BaseAdapterRvList.TYPE_BODY;
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
        if (listPosition < mList.size()) {
            return mList.get(listPosition);
        }
        throw new RuntimeException("lit为空或指针越界");
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
     * add null表示删除
     */
    public void setHeaderView(View view) {
        mHeaderView = view;
        notifyDataSetChanged();
    }

    /**
     * add null表示删除
     */
    public void setFooterView(View view) {
        mFooterView = view;
        notifyDataSetChanged();
    }

    /**
     * 新的监听
     */
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }

    /**
     * 不太建议使用这个，自定义的时候才会用到
     */
    @Deprecated
    @Override
    public void setOnItemClickListener(@Nullable IAdapterItemClick listener) {
        super.setOnItemClickListener(listener);
    }
}
