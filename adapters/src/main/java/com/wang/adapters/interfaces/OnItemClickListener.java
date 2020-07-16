package com.wang.adapters.interfaces;

import android.view.View;

import androidx.annotation.NonNull;

import com.wang.adapters.R;
import com.wang.adapters.adapter.BaseAdapterRv;
import com.wang.adapters.adapter.BaseAdapterRvList;
import com.wang.adapters.adapter.BaseViewHolder;

/**
 * 点击,长按,header,footer的回调
 * 完美解决类似recyclerviewAdapter的setOnClicklistener重复new 对象的问题
 * 可以看看怎么写的{@link BaseAdapterRv}
 * holder.mRLl.setTag(R.id.tag_view_click,position);
 * holder.mRLl.setOnClickListener(mLlListener(全局常量,不用每个都new一下));
 */
public abstract class OnItemClickListener implements IAdapterItemClick {
    @Override
    public final void onClick(@NonNull View view) {
        int position = getViewPosition(view);
        switch (position) {
            case BaseAdapterRvList.POSITION_HEADER:
                onHeaderClick(view);
                break;
            case BaseAdapterRvList.POSITION_FOOTER:
                onFooterClick(view);
                break;
            default:
                onItemClick(view, position);
                break;
        }
    }

    @Override
    public final boolean onLongClick(@NonNull View view) {
        int position = getViewPosition(view);
        switch (position) {
            case BaseAdapterRvList.POSITION_HEADER:
                return onHeaderLongClick(view);
            case BaseAdapterRvList.POSITION_FOOTER:
                return onFooterLongClick(view);
            default:
                return onItemLongClick(view, position);
        }
    }

    /**
     * 获取当前view所保存的position
     */
    protected final int getViewPosition(@NonNull View view) {
        return (int) view.getTag(R.id.tag_view_click);
    }

    /**
     * 获取当前view所在的ViewHolder
     */
    protected final BaseViewHolder getViewHolder(@NonNull View view) {
        return (BaseViewHolder) view.getTag(R.id.tag_view_holder);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 以下是item,header,footer的点击和长按回调
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * item被点击时
     *
     * @param listPosition list集合所对应的position,不需要-1
     */
    public abstract void onItemClick(@NonNull View view, int listPosition);

    /**
     * item被长按时
     *
     * @param listPosition list集合所对应的position,不需要-1
     */
    public boolean onItemLongClick(@NonNull View view, int listPosition) {
        return false;
    }

    /**
     * 添加的header被点击时,没有可以忽略
     */
    protected void onHeaderClick(@NonNull View view) {
    }

    /**
     * 添加的header被长按时,没有可以忽略
     */
    protected boolean onFooterLongClick(@NonNull View view) {
        return false;
    }

    /**
     * 添加的footer被点击时,没有可以忽略
     */
    protected void onFooterClick(@NonNull View view) {
    }

    /**
     * 添加的footer被长按时,没有可以忽略
     */
    protected boolean onHeaderLongClick(@NonNull View view) {
        return false;
    }
}