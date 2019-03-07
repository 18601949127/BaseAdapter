package com.wang.example;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.adapters.adapter.BaseAdapterRvList;
import com.wang.adapters.base.BaseViewHolder;
import com.wang.adapters.interfaceabstract.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRv = new RecyclerView(this);
        setContentView(mRv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        listTest();
    }

    /**
     * 简单的列表测试
     */
    private void listTest() {
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mRv.setLayoutManager(manager);
        final ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            list.add("第" + i);
        }
        ListAdapter adapter = new ListAdapter(this, list);
        mRv.setAdapter(adapter);
        //设置点击事件
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            protected void onItemClick(View view, int listPosition) {
                Toast.makeText(MainActivity.this, "点击第" + list.get(listPosition), Toast.LENGTH_SHORT).show();
            }

            @Override
            protected boolean onItemLongClick(View view, int listPosition) {
                Toast.makeText(MainActivity.this, "长按第" + list.get(listPosition), Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            protected void onFooterClick(View view) {
                super.onFooterClick(view);
                Toast.makeText(MainActivity.this, "footer被点击", Toast.LENGTH_SHORT).show();
            }
        });
        //添加尾
        AppCompatImageView iv = new AppCompatImageView(this);
        iv.setImageResource(R.mipmap.ic_launcher);
        iv.setAdjustViewBounds(true);
        adapter.addFooterView(iv);
        //GridLayoutManager需要将头或尾占多行
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == list.size() ? 2 : 1;
            }
        });
    }


    public class ListAdapter extends BaseAdapterRvList<BaseViewHolder, String> {

        public ListAdapter(@NonNull Activity activity, List<String> list) {
            super(activity, list);
        }

        @Override
        protected void onBindVH(BaseViewHolder holder, int listPosition, String s) {
            TextView tv = (TextView) holder.itemView;
            tv.setText(s);
        }

        @NonNull
        @Override
        protected BaseViewHolder onCreateVH(ViewGroup parent, LayoutInflater inflater) {
            TextView tv = new AppCompatTextView(mActivity);
            tv.setTextSize(20);
            tv.setTextColor(0xff000000);
            tv.setPadding(20, 20, 20, 20);
            return new BaseViewHolder(tv);
        }
    }
}
