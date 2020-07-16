package com.wang.adapters.interfaces;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.wang.adapters.R;
import com.zhy.view.flowlayout.FlowLayout;

/**
 * 高级功能:adapter套adapter的点击事件,具体用法见实现类
 * //bind直接使用adapter的mListener即可
 * adapter.setOnItemClickListener(mListener);
 * <p>
 * //create里面写法
 * OrderSunListAdapter adapter = (OrderSunListAdapter) holder.mRv.getAdapter();
 * adapter.setListAndNotifyDataSetChanged(orderBean.getListOrderDetail());
 * holder.mRv.setTag(R.id.tag_view_click,position);
 */
public abstract class OnItemItemClickListener extends OnItemClickListener {
    @Override
    public final void onItemClick(@NonNull View view, int listPosition) {
        Integer parentOrNull = getParentTag(view);
        if (parentOrNull == null) {
            onParentItemClick(view, listPosition);
        } else {
            onChildItemClick(view, parentOrNull, listPosition);
        }
    }

    @Override
    public final boolean onItemLongClick(@NonNull View view, int listPosition) {
        Integer parentOrNull = getParentTag(view);
        if (parentOrNull == null) {
            return onParentItemLongClick(view, listPosition);
        } else {
            return onChildItemLongClick(view, parentOrNull, listPosition);
        }
    }

    @Override
    protected final void onHeaderClick(@NonNull View view) {
        Integer parentOrNull = getParentTag(view);
        if (parentOrNull == null) {
            onParentHeaderClick(view);
        } else {
            onChildHeaderClick(view, parentOrNull);
        }
    }

    @Override
    protected final boolean onHeaderLongClick(@NonNull View view) {
        Integer parentOrNull = getParentTag(view);
        if (parentOrNull == null) {
            return onParentHeaderLongClick(view);
        } else {
            return onChildHeaderLongClick(view, parentOrNull);
        }
    }

    @Override
    protected final void onFooterClick(@NonNull View view) {
        Integer parentOrNull = getParentTag(view);
        if (parentOrNull == null) {
            onParentFooterClick(view);
        } else {
            onChildFooterClick(view, parentOrNull);
        }
    }

    @Override
    protected final boolean onFooterLongClick(@NonNull View view) {
        Integer parentOrNull = getParentTag(view);
        if (parentOrNull == null) {
            return onParentFooterLongClick(view);
        } else {
            return onChildFooterLongClick(view, parentOrNull);
        }
    }

    /**
     * 外层的position需要遍历
     */
    protected final Integer getParentTag(@NonNull View v) {
        ViewParent parent = v.getParent();
        while (parent != null) {
            //第二层不建议使用ListView或GridView(肯定没有复用性,并且效率很差,可以尝试使用RecyclerView然后wrap)
//            if (parent instanceof RecyclerView || parent instanceof ViewPager || parent instanceof FlowLayout || parent instanceof AdapterView) {
            if (parent instanceof RecyclerView || parent instanceof ViewPager || parent instanceof FlowLayout) {
                return (Integer) ((ViewGroup) parent).getTag(R.id.tag_view_click);
            }
            parent = parent.getParent();
        }
        //没取到返回null
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 以下是parent的回调
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 当外层被点击时
     *
     * @param parentPosition 外层adapter的position
     */
    protected abstract void onParentItemClick(@NonNull View view, int parentPosition);

    /**
     * 当外层被长按时
     *
     * @param parentPosition 外层adapter的position
     */
    protected boolean onParentItemLongClick(@NonNull View view, int parentPosition) {
        return false;
    }

    protected void onParentHeaderClick(@NonNull View view) {
    }

    protected boolean onParentHeaderLongClick(@NonNull View view) {
        return false;
    }

    protected void onParentFooterClick(@NonNull View view) {
    }

    protected boolean onParentFooterLongClick(@NonNull View view) {
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 以下是child的回调
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 当内层被点击时
     *
     * @param parentPosition 外层adapter对应的position
     * @param childPosition  内层adapter对应的position
     */
    protected abstract void onChildItemClick(@NonNull View view, int parentPosition, int childPosition);

    /**
     * 当内层被长按时
     *
     * @param parentPosition 外层adapter对应的position
     * @param childPosition  内层adapter对应的position
     */
    protected boolean onChildItemLongClick(@NonNull View view, int parentPosition, int childPosition) {
        return false;
    }

    protected void onChildHeaderClick(@NonNull View view, int parentPosition) {
    }

    protected boolean onChildHeaderLongClick(@NonNull View view, int parentPosition) {
        return false;
    }

    protected void onChildFooterClick(@NonNull View view, int parentPosition) {
    }

    protected boolean onChildFooterLongClick(@NonNull View view, int parentPosition) {
        return false;
    }
}