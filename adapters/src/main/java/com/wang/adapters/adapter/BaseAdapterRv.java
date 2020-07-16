package com.wang.adapters.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.wang.adapters.R;
import com.wang.adapters.interfaces.IAdapterItemClick;
import com.wang.adapters.interfaces.OnItemClickListener;
import com.wang.adapters.interfaces.OnItemItemClickListener;
import com.wang.container.interfaces.IAdapter;
import com.wang.container.interfaces.IListAdapter;

import java.util.List;

/**
 * 适用于rv、我自定义的{@link BaseSuperAdapter}
 * 增加点击事件
 */
public abstract class BaseAdapterRv<VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> implements BaseSuperAdapter.ISuperAdapter<VH>, IAdapter<VH, IAdapterItemClick> {
    public final String TAG = getClass().getSimpleName();
    protected IAdapterItemClick mListener;

    @Override
    public final VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VH holder = onCreateViewHolder2(parent, viewType);
        //保存holder，如果position无法解决问题，可以使用这个
        holder.itemView.setTag(R.id.tag_view_holder, holder);
        return holder;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        //保存position
        holder.itemView.setTag(R.id.tag_view_click, position);
        //创建点击事件
        holder.itemView.setOnClickListener(mListener);
        holder.itemView.setOnLongClickListener(mListener);
        onBindViewHolder2(holder, position);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 以下是可能用到的父类方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 自定义的{@link BaseSuperAdapter}的多条目用到,获取当前item所占条目数
     */
    @Override
    public int getSpanSize(int position) {
        return 1;
    }

//    @Override
//    public int getItemViewType(int position) {
//        return 0;
//    }

    ///////////////////////////////////////////////////////////////////////////
    // 以下是增加的方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 给view设置点击事件到{@link #mListener}中
     * <p>
     * 点击回调见{@link #setOnItemClickListener}{@link .OnItemClickListener}
     */
    protected final void setItemViewClick(View view, int position) {
        view.setTag(R.id.tag_view_click, position);
        if (!(view instanceof RecyclerView)) view.setOnClickListener(mListener);
    }

    /**
     * 给rv设置点击事件和数据
     * 点击回调见{@link #setOnItemClickListener}{@link OnItemItemClickListener}
     */
    protected final void setItemRvData(RecyclerView rv, int position, List<?> adapterList) {
        rv.setTag(R.id.tag_view_click, position);
        IListAdapter adapter = (IListAdapter) rv.getAdapter();
        //noinspection ConstantConditions,unchecked
        adapter.setOnItemClickListener(mListener);
        //noinspection unchecked 忽略未检查错误,如果出异常说明你传的list和你的adapter对不上
        adapter.setListAndNotifyDataSetChanged(adapterList);
    }

    protected abstract void onBindViewHolder2(VH holder, int position);

    /**
     * 注意!涉及到notifyItemInserted刷新时立即获取position可能会不正确
     * 里面也有LongClick
     * 监听事件一般使用实现类{@link OnItemClickListener}
     */
    public void setOnItemClickListener(@Nullable IAdapterItemClick listener) {
        mListener = listener;
        notifyDataSetChanged();
    }
}