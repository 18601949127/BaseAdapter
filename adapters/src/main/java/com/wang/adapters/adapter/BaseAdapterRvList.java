package com.wang.adapters.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.wang.adapters.R;
import com.wang.adapters.interfaces.IAdapterItemClick;
import com.wang.adapters.interfaces.OnItemClickListener;
import com.wang.container.interfaces.IListAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * listAdapter的基类
 * 可以加header、footer,如果是Grid需要自行处理setSpanSizeLookup头尾的跨度
 * {@link #notifyItemChanged}相关方法时注意有header时需要-1
 * bug:{@link #notifyItemChanged}方法不能刷新header、footer（header、footer不需要刷新，仅仅是先记着）
 */
public abstract class BaseAdapterRvList<VH extends BaseViewHolder, BEAN> extends BaseAdapterRv<BaseViewHolder> implements IListAdapter<BEAN, BaseViewHolder, IAdapterItemClick> {

    @NonNull
    private List<BEAN> mList;

    public View mHeaderView, mFooterView;

    public static final int POSITION_HEADER = -128, POSITION_FOOTER = -127;//-128~127的integer有优化

    public static final int TYPE_HEADER = 0, TYPE_BODY = 1, TYPE_FOOTER = 2;

    @IntDef({TYPE_HEADER, TYPE_BODY, TYPE_FOOTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AdapterListType {
    }//该变量只能传入上面几种,否则会报错

    public BaseAdapterRvList() {
        this(null);
    }

    public BaseAdapterRvList(@Nullable List<BEAN> list) {
        this(list, null, null);
    }

    public BaseAdapterRvList(List<BEAN> list, @Nullable View headerView, @Nullable View footerView) {
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
    protected final void onBindViewHolder2(BaseViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                holder.itemView.setTag(R.id.tag_view_click, POSITION_HEADER);
                break;
            case TYPE_FOOTER:
                holder.itemView.setTag(R.id.tag_view_click, POSITION_FOOTER);
                break;
            case TYPE_BODY:
                if (mHeaderView != null) {
                    position--;
                }
                holder.itemView.setTag(R.id.tag_view_click, position);
                //noinspection unchecked
                onBindViewHolder3((VH) holder, position, mList.get(position));
                break;
            default:
                throw new RuntimeException("仅支持header、footer和body,想拓展请使用BaseAdapterRv");
        }
    }

    @NonNull
    @Override
    public final BaseViewHolder onCreateViewHolder2(ViewGroup parent, @BaseAdapterRvList.AdapterListType int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new BaseViewHolder(mHeaderView);
            case TYPE_FOOTER:
                return new BaseViewHolder(mFooterView);
            case TYPE_BODY:
                return onCreateViewHolder3(parent);
            default:
                throw new RuntimeException("仅支持header、footer和body,想拓展请使用BaseAdapterRv");
        }
    }

    @AdapterListType
    @Override
    public final int getItemViewType(int position) {
        if (mHeaderView != null && position == 0) {
            return TYPE_HEADER;
        }
        if (mFooterView != null && getItemCount() == position + 1) {
            return TYPE_FOOTER;
        }
        return TYPE_BODY;
    }

    /**
     * 获取默认无参的viewHolder
     */
    @NonNull
    private VH createDefaultViewHolder() {
        Class<VH> vhClass = getViewHolderClass(getClass());
        if (vhClass == null) {
            throw new RuntimeException("没有找到ViewHolder");
        } else if (vhClass == BaseViewHolder.class) {
            throw new RuntimeException("使用BaseViewHolder时，请重写onCreateVH方法");
        } else {
            try {
                Constructor<VH> constructor;
                if (vhClass.isMemberClass() && !Modifier.isStatic(vhClass.getModifiers())) {
                    constructor = vhClass.getDeclaredConstructor(getClass());
                    constructor.setAccessible(true);
                    return constructor.newInstance(this);
                } else {
                    constructor = vhClass.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    return constructor.newInstance();
                }
            } catch (Exception e) {
                e.printStackTrace();
                //1.只支持无参
                //2.非静态ViewHolder必须是Adapter的
                //3.如想自定义ViewHolder，请重写覆盖onCreateVH方法
                throw new RuntimeException("实例化无参ViewHolder失败，请看上方注释说明", e);
            }
        }
    }

    @Nullable
    private Class<VH> getViewHolderClass(Class adapterClass) {
        if (adapterClass == BaseAdapterRv.class || adapterClass == null) {
            return null;
        }
        Type superType = adapterClass.getGenericSuperclass();
        if (superType instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) superType).getActualTypeArguments();
            for (Type type : types) {
                if (type instanceof Class) {
                    Class vhClass = (Class) type;
                    if (BaseViewHolder.class.isAssignableFrom(vhClass)) {
                        //noinspection unchecked
                        return (Class<VH>) vhClass;
                    }
                }
            }
        }
        return getViewHolderClass(adapterClass.getSuperclass());
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
    protected abstract void onBindViewHolder3(VH holder, int listPosition, BEAN bean);

    /**
     * 默认用反射create
     * 重写就别调用super了
     */
    @NonNull
    protected VH onCreateViewHolder3(ViewGroup parent) {
        BaseViewHolder.cacheParent = parent;
        VH vh = createDefaultViewHolder();
        BaseViewHolder.cacheParent = null;
        return vh;
    }

    public void setListAndNotifyDataSetChanged(@Nullable List<BEAN> list) {
        mList = list == null ? new ArrayList<>() : list;
        notifyDataSetChanged();
    }

    /**
     * add null表示删除
     */
    public void setHeaderView(View view) {
        mHeaderView = view;
        //没有params添加一个默认的
        if (view != null && view.getLayoutParams() == null)
            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        notifyDataSetChanged();
    }

    /**
     * add null表示删除
     */
    public void setFooterView(View view) {
        mFooterView = view;
        //没有params添加一个默认的
        if (view != null && view.getLayoutParams() == null)
            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 追加一个懒汉写法
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 懒得不能再懒了的写法
     * adapter里的Activity为null
     *
     * @param layoutId create时的资源id
     * @param listener 当bind时回调
     * @param <BEAN>   list数据的bean
     */
    public static <BEAN> BaseAdapterRvList<BaseViewHolder, BEAN> createAdapter
    (@LayoutRes final int layoutId, final OnAdapterBindListener<BEAN> listener) {
        return createAdapter(null, layoutId, listener);
    }

    public static <BEAN> BaseAdapterRvList<BaseViewHolder, BEAN> createAdapter
            (@Nullable List<BEAN> list, @LayoutRes final int layoutId, final OnAdapterBindListener<BEAN> listener) {
        return new BaseAdapterRvList<BaseViewHolder, BEAN>(list) {
            @Override
            protected void onBindViewHolder3(BaseViewHolder holder, int listPosition, BEAN bean) {
                listener.onBindVH(holder, listPosition, bean);
            }

            @NonNull
            @Override
            protected BaseViewHolder onCreateViewHolder3(ViewGroup parent) {
                return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
            }
        };
    }

    public interface OnAdapterBindListener<BEAN> {
        void onBindVH(BaseViewHolder holder, int listPosition, BEAN bean);
    }
}
