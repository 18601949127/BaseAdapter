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
 * 和{@link BaseAdapterRvList}基本一致，适用于listView、gridView、viewPager
 */
public abstract class BaseAdapterLvsList<DB extends ViewDataBinding, BEAN> extends BaseAdapterLvs<ViewDataBinding>
        implements IListAdapter<BEAN, BaseViewHolder<ViewDataBinding>, OnItemClickListener> {

    private final ListAdapterHelper<DB, BEAN> mHelper;

    /**
     * 资源id已经不是必须的了
     * <p>
     * 无资源id有2种解决方式（任选其一）：
     * 1.什么都不做，根据泛型自动获取，但Proguard不能混淆{@link ViewDataBinding}的子类
     * 2.覆盖{@link #onCreateViewHolder3}，自己自定义即可
     */
    public BaseAdapterLvsList() {
        this(0);
    }

    public BaseAdapterLvsList(@LayoutRes int layoutId) {
        this(layoutId, null);
    }


    public BaseAdapterLvsList(@LayoutRes int layoutId, List<BEAN> list) {
        mHelper = new ListAdapterHelper<>(this, layoutId, list);
    }

    @Override
    public final int getItemCount() {
        return mHelper.getItemCount();
    }

    @Override
    protected final void onBindViewHolder2(@NonNull BaseViewHolder holder, int position) {
        if (mHelper.onBindViewHolder(holder, position)) {
            int listPosition = mHelper.getListPosition(position);
            //noinspection unchecked 这才发现泛型擦除多好（可是其他语言都是自动转型的...）
            onBindViewHolder3((BaseViewHolder<DB>) holder, listPosition, getList().get(listPosition));
            mHelper.executePendingBindings(holder);
        }
    }

    @NonNull
    @Override
    protected final BaseViewHolder<ViewDataBinding> onCreateViewHolder2(@NonNull ViewGroup parent, @ListAdapterHelper.AdapterListType int viewType) {
        BaseViewHolder<ViewDataBinding> holder = mHelper.onCreateViewHolder(parent, viewType);
        if (holder == null) {
            //noinspection unchecked 这才发现泛型擦除多好（可是其他语言都是自动转型的...）
            holder = (BaseViewHolder<ViewDataBinding>) onCreateViewHolder3(parent);
        }
        return holder;
    }

    @ListAdapterHelper.AdapterListType
    @Override
    public final int getItemViewType(int position) {
        return mHelper.getItemViewType(position);
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
        return mHelper.mList;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 以下是增加的方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 你只需要处理其他数据、其他点击事件等，基本的都加上了：
     * <p>
     * 不需要绑定bean数据，已经默认绑定
     * 不需要调用{@link ViewDataBinding#executePendingBindings()}，已经默认加上
     *
     * @param listPosition 已经做过处理,就是list的position
     */
    protected abstract void onBindViewHolder3(@NonNull BaseViewHolder<DB> holder, int listPosition, BEAN bean);

    /**
     * 默认用DataBinding create
     * 完全不需要的话覆盖整个方法就行了，不会出问题
     * 你也可以重写来添加自己的默认逻辑，如：全局隐藏显示、嵌套rv的默认属性设置等
     */
    @NonNull
    protected BaseViewHolder<DB> onCreateViewHolder3(@NonNull ViewGroup parent) {
        return mHelper.onCreateDefaultViewHolder(parent, BaseAdapterLvsList.class, getClass());
    }

    /**
     * @param view null表示删除
     */
    public void setHeaderView(@Nullable View view) {
        mHelper.setHeaderView(view);
    }

    @Nullable
    @Override
    public View getHeaderView() {
        return mHelper.mHeaderView;
    }

    /**
     * @param view null表示删除
     */
    @Override
    public void setFooterView(@Nullable View view) {
        mHelper.setFooterView(view);
    }

    @Nullable
    @Override
    public View getFooterView() {
        return mHelper.mFooterView;
    }
}
