package com.wang.adapters.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.wang.adapters.helper.ListAdapterHelper;
import com.wang.adapters.interfaces.OnItemClickListener;
import com.wang.container.interfaces.IListAdapter;

import java.util.List;

/**
 * 无限循环滑动的adapter
 */
public abstract class BaseAdapterRvListCycle<DB extends ViewDataBinding, BEAN> extends BaseAdapterRv<DB>
        implements IListAdapter<BEAN, BaseViewHolder<DB>, OnItemClickListener> {

    private final ListAdapterHelper<DB, BEAN> mHelper;

    /**
     * 资源id已经不是必须的了
     * <p>
     * 无资源id有2种解决方式（任选其一）：
     * 1.什么都不做，根据泛型自动获取，但Proguard不能混淆{@link ViewDataBinding}的子类
     * 2.重写{@link #onCreateViewHolder2}，自定义即可
     */
    public BaseAdapterRvListCycle() {
        this(0);
    }

    public BaseAdapterRvListCycle(@LayoutRes int layoutId) {
        this(layoutId, null);
    }


    public BaseAdapterRvListCycle(@LayoutRes int layoutId, List<BEAN> list) {
        mHelper = new ListAdapterHelper<>(this, layoutId, list);
    }

    @Override
    public final int getItemCount() {
        return getList().isEmpty() ? 0 : Integer.MAX_VALUE;
    }

    @Override
    protected final void onBindViewHolder2(@NonNull BaseViewHolder<DB> holder, int position) {
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
        return mHelper.mList;
    }

    /**
     * 获取指定bean
     */
    @NonNull
    public BEAN get(int position) {
        return getList().get(position % getList().size());
    }

    ///////////////////////////////////////////////////////////////////////////
    // 以下是增加的方法
    ///////////////////////////////////////////////////////////////////////////


    @NonNull
    @Override
    protected BaseViewHolder<DB> onCreateViewHolder2(@NonNull ViewGroup parent, int viewType) {
        return mHelper.onCreateDefaultViewHolder(parent, BaseAdapterRvListCycle.class, getClass());
    }

    protected abstract void onBindViewHolder3(BaseViewHolder<DB> holder, int position, BEAN bean);
}
