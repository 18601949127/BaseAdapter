package com.wang.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wang.adapters.adapter.BaseAdapterRvList;
import com.wang.adapters.adapter.BaseViewHolder;
import com.wang.adapters.interfaces.OnItemClickListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_main_vp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ViewPagerFragActivity.class);
            }
        });
        mRv = findViewById(R.id.rv_main);
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
        ListAdapter adapter = new ListAdapter();
        adapter.setListAndNotifyDataSetChanged(list);
        mRv.setAdapter(adapter);
        //设置点击事件
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull View view, int listPosition) {
                Toast.makeText(MainActivity.this, "点击第" + list.get(listPosition), Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onItemLongClick(@NonNull View view, int listPosition) {
                Toast.makeText(MainActivity.this, "长按第" + list.get(listPosition), Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            protected void onFooterClick(@NonNull View view) {
                super.onFooterClick(view);
                Toast.makeText(MainActivity.this, "footer被点击", Toast.LENGTH_SHORT).show();
            }
        });
        //添加尾
        AppCompatImageView iv = new AppCompatImageView(this);
        iv.setImageResource(R.mipmap.ic_launcher);
        iv.setAdjustViewBounds(true);
        adapter.setFooterView(iv);
        //GridLayoutManager需要将头或尾占多行
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == list.size() ? 2 : 1;
            }
        });
    }

    public static class ListAdapter extends BaseAdapterRvList<ListAdapter.ViewHolder, String> {

        @Override
        protected void onBindViewHolder3(ListAdapter.ViewHolder holder, int listPosition, String s) {
            holder.mTvText.setText(s);
        }

        static class ViewHolder extends BaseViewHolder {
            private final TextView mTvText;

            protected ViewHolder() {
                super(R.layout.adapter_main_list);
                mTvText = itemView.findViewById(R.id.tv_main_adapter_text);
            }
        }
    }

    public void startActivity(Class c) {
        startActivity(new Intent(this, c));
    }
}
