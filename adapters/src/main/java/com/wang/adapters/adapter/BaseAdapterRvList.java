package com.wang.adapters.adapter;

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
import com.wang.adapters.interfaces.OnItemClickListener;
import com.wang.container.interfaces.IListAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * listAdapter的基类
 * 可以加header、footer,如果是Grid需要自行处理setSpanSizeLookup头尾的跨度
 * {@link #notifyItemChanged}相关方法时注意有header时需要-1
 * bug:{@link #notifyItemChanged}方法不能刷新header、footer（header、footer不需要刷新，仅仅是先记着）
 */
public abstract class BaseAdapterRvList<T extends ViewDataBinding, BEAN> extends BaseAdapterRv<ViewDataBinding>
        implements IListAdapter<BEAN, BaseViewHolder<ViewDataBinding>, OnItemClickListener> {

    @NonNull
    private List<BEAN> mList;
    @LayoutRes
    protected final int mLayoutId;

    @Nullable
    private View mHeaderView, mFooterView;

    public static final int TYPE_BODY = 0, TYPE_HEADER = 1, TYPE_FOOTER = 2;

    @IntDef({TYPE_BODY, TYPE_HEADER, TYPE_FOOTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AdapterListType {
    }//该变量只能传入上面几种,否则会报错

    /**
     * 没有资源id，需要重写{@link #onCreateViewHolder3}
     */
    public BaseAdapterRvList() {
        this(0);
    }

    public BaseAdapterRvList(@LayoutRes int layoutId) {
        this(layoutId, null);
    }


    public BaseAdapterRvList(@LayoutRes int layoutId, List<BEAN> list) {
        mLayoutId = layoutId;
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
    protected final void onBindViewHolder2(BaseViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
            case TYPE_FOOTER:
                break;
            case TYPE_BODY:
                if (getHeaderView() != null) {
                    position--;
                }
                if (holder.getBinding() != null) {
                    holder.getBinding().setVariable(BR.bean, getList().get(position));
                }
                //noinspection unchecked 这才发现泛型擦除多好（可是其他语言都是自动转型的...）
                onBindViewHolder3((BaseViewHolder<T>) holder, position, getList().get(position));

                if (holder.getBinding() != null) {
                    holder.getBinding().executePendingBindings();
                }
                break;
            default:
                throw new RuntimeException("仅支持header、footer和body,想拓展请使用BaseAdapterRv");
        }
    }

    @NonNull
    @Override
    public final BaseViewHolder<ViewDataBinding> onCreateViewHolder2(ViewGroup parent, @BaseAdapterRvList.AdapterListType int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                //noinspection ConstantConditions 这里当然不会为null
                return new BaseViewHolder<>(getHeaderView());
            case TYPE_FOOTER:
                //noinspection ConstantConditions 这里当然不会为null
                return new BaseViewHolder<>(getFooterView());
            case TYPE_BODY:
                //noinspection unchecked 这才发现泛型擦除多好（可是其他语言都是自动转型的...）
                return (BaseViewHolder<ViewDataBinding>) onCreateViewHolder3(parent);
            default:
                throw new RuntimeException("仅支持header、footer和body,想拓展请使用BaseAdapterRv");
        }
    }

    @AdapterListType
    @Override
    public final int getItemViewType(int position) {
        if (getHeaderView() != null && position == 0) {
            return TYPE_HEADER;
        }
        if (getFooterView() != null && getItemCount() == position + 1) {
            return TYPE_FOOTER;
        }
        return TYPE_BODY;
    }

    private void notifyHeaderFooter() {
        //没有params添加一个默认的
        if (getHeaderView() != null && getHeaderView().getLayoutParams() == null) {
            getHeaderView().setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        if (getFooterView() != null && getFooterView().getLayoutParams() == null) {
            getFooterView().setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        notifyDataSetChanged();
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
    protected abstract void onBindViewHolder3(BaseViewHolder<T> holder, int listPosition, BEAN bean);

    /**
     * 默认用DataBinding create
     * 不需要的话就别调用super了
     * 你也可以重写来增加默认的操作，如：全局隐藏显示、嵌套rv的默认属性设置等
     */
    @NonNull
    protected BaseViewHolder<T> onCreateViewHolder3(ViewGroup parent) {
        if (mLayoutId == 0) {
            throw new RuntimeException("你没有传资源id，需要自己实现并覆盖此方法");
        }
        return new BaseViewHolder<>(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), mLayoutId, parent, false));
    }

    /**
     * add null表示删除
     */
    public void setHeaderView(@Nullable View view) {
        mHeaderView = view;
        notifyHeaderFooter();
    }

    @Nullable
    public View getHeaderView() {
        return mHeaderView;
    }

    /**
     * add null表示删除
     */
    public void setFooterView(@Nullable View view) {
        mFooterView = view;
        notifyHeaderFooter();
    }

    @Nullable
    public View getFooterView() {
        return mFooterView;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 追加一个懒汉写法
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 懒得不能再懒了的写法
     * 就资源id，数据在xml里修改
     *
     * @param layoutId create时的资源id
     */
    public static <BEAN> BaseAdapterRvList<?, BEAN> createAdapter(@LayoutRes final int layoutId) {
        return createAdapter(null, layoutId, null);
    }

    public static <T extends ViewDataBinding, BEAN> BaseAdapterRvList<T, BEAN> createAdapter
            (@Nullable List<BEAN> list, @LayoutRes final int layoutId, @Nullable final OnAdapterBindListener<T, BEAN> listener) {
        return new BaseAdapterRvList<T, BEAN>(layoutId, list) {

            @Override
            protected void onBindViewHolder3(BaseViewHolder<T> holder, int listPosition, BEAN bean) {
                if (listener != null) {
                    listener.onBindViewHolder(holder, listPosition, bean);
                }
            }

            @NonNull
            @Override
            protected BaseViewHolder<T> onCreateViewHolder3(ViewGroup parent) {
                BaseViewHolder<T> holder = super.onCreateViewHolder3(parent);
                if (listener != null) {
                    listener.onViewHolderCreated(holder);
                }
                return holder;
            }
        };
    }

    public interface OnAdapterBindListener<T extends ViewDataBinding, BEAN> {
        void onBindViewHolder(BaseViewHolder<T> holder, int listPosition, BEAN bean);

        /**
         * 当viewHolder创建完成后
         */
        default void onViewHolderCreated(BaseViewHolder<T> holder) {
        }
    }
}
