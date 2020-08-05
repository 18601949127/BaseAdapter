package com.wang.adapters.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.wang.adapters.BR;
import com.wang.adapters.adapter.BaseViewHolder;
import com.wang.adapters.utils.GenericUtils;
import com.wang.container.interfaces.IListAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class ListAdapterHelper<DB extends ViewDataBinding, BEAN> {

    public static final int TYPE_BODY = 0, TYPE_HEADER = 1, TYPE_FOOTER = 2;

    @IntDef({TYPE_BODY, TYPE_HEADER, TYPE_FOOTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AdapterListType {
    }//该变量只能传入上面几种,否则会报错

    @NonNull
    public List<BEAN> mList;
    @LayoutRes
    public int mLayoutId;

    @Nullable
    public View mHeaderView, mFooterView;

    private final IListAdapter mAdapter;

    public ListAdapterHelper(IListAdapter adapter, @LayoutRes int layoutId, List<BEAN> list) {
        mAdapter = adapter;
        mLayoutId = layoutId;
        mList = list == null ? new ArrayList<>() : list;
    }

    public int getListPosition(int adapterPosition) {
        if (mHeaderView != null) {
            adapterPosition--;
        }
        return adapterPosition;
    }

    public int getItemCount() {
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

    /**
     * @return 是否需要绑定list，true：调用子类的bind并调用{@link #executePendingBindings}
     */
    public boolean onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
            case TYPE_FOOTER:
                return false;
            case TYPE_BODY:
                if (mHeaderView != null) {
                    position--;
                }
                if (holder.getBinding() != null) {
                    holder.getBinding().setVariable(BR.bean, mList.get(position));
                }
                return true;
        }
        return false;
    }

    public void executePendingBindings(@NonNull BaseViewHolder holder) {
        if (holder.getBinding() != null) {
            holder.getBinding().executePendingBindings();
        }
    }

    /**
     * @return null表示需要create list的viewHolder
     */
    @Nullable
    public final BaseViewHolder<ViewDataBinding> onCreateViewHolder(@NonNull ViewGroup parent, @AdapterListType int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                //noinspection ConstantConditions 这里当然不会为null
                return new BaseViewHolder<>(mHeaderView);
            case TYPE_FOOTER:
                //noinspection ConstantConditions 这里当然不会为null
                return new BaseViewHolder<>(mFooterView);
            case TYPE_BODY:
            default:
                return null;
        }
    }

    @AdapterListType
    public final int getItemViewType(int position) {
        if (mHeaderView != null && position == 0) {
            return TYPE_HEADER;
        }
        if (mFooterView != null && getItemCount() == position + 1) {
            return TYPE_FOOTER;
        }
        return TYPE_BODY;
    }

    private void notifyHeaderFooter() {
        //没有params添加一个默认的（目前只做RecyclerView）
        if (mHeaderView != null && mHeaderView.getLayoutParams() == null && mAdapter instanceof RecyclerView.Adapter) {
            mHeaderView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        if (mFooterView != null && mFooterView.getLayoutParams() == null && mAdapter instanceof RecyclerView.Adapter) {
            mFooterView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        mAdapter.notifyDataSetChanged();
    }

    @NonNull
    public BaseViewHolder<DB> onCreateDefaultViewHolder(@NonNull ViewGroup parent, Class baseClass, Class childClass) {
        if (mLayoutId == 0) {
            mLayoutId = GenericUtils.getGenericRes(parent.getContext(), baseClass, childClass);
        }
        return new BaseViewHolder<>(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), mLayoutId, parent, false));
    }

    public void setHeaderView(@Nullable View view) {
        mHeaderView = view;
        notifyHeaderFooter();
    }

    public void setFooterView(@Nullable View view) {
        mFooterView = view;
        notifyHeaderFooter();
    }
}
