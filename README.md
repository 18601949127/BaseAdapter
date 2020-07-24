# 代码非常简单，基于dataBinding

## 详细示例见本项目app下的MainActivity
一个listAdapter只需要如下一行（没错，总共就一行）
```
   BaseAdapterRvList<?, String> adapter = BaseAdapterRvList.createAdapter(R.layout.adapter_main_list);
```
当然，你的xml是基于dataBinding的
```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <!--        adapter默认会设置并刷新“bean”这个属性-->
        <variable
            name="bean"
            type="String" />
    </data>

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="#fff"
        android:padding="10dp"
        android:text="@{bean}"
        android:textColor="#333"
        android:textSize="20sp"
        tools:text="这是文本" />
</layout>
```
自带点击事件
```
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull View view, int listPosition) {
            }

            @Override
            public boolean onItemLongClick(@NonNull View view, int listPosition) {
                return true;
            }

            @Override
            protected void onFooterClick(@NonNull View view) {
                super.onFooterClick(view);
            }
            //...header、footer long click
        });
```
自带header、footer
```
adapter.setHeaderView(view);
adapter.setFooterView(view);
```
当然你也可以自定义一些简单逻辑
```
BaseAdapterRvList<AdapterMainListBinding, String> adapter = BaseAdapterRvList.createAdapter(list, R.layout.adapter_main_list,
        (holder, listPosition, s) -> {
            if (s.contains("10")) {
                holder.itemView.setBackgroundColor(0xff999999);
            }
        });//回调还有onViewHolderCreated方法
```
也可以完全自定义
```
    public static class ListAdapter extends BaseAdapterRvList<AdapterMainListBinding, String> {
        public ListAdapter() {
            super(R.layout.adapter_main_list);
        }

        @Override
        protected void onBindViewHolder3(BaseViewHolder<AdapterMainListBinding> holder, int listPosition, String s) {
            if (s.contains("100")) {
                getList().set(listPosition, "改掉了100");//后面会调用刷新dataBinding
            } else if (s.contains("10")) {
                holder.itemView.setBackgroundColor(0xff999999);
            }
        }

        @NonNull
        @Override
        protected BaseViewHolder<ViewDataBinding> onCreateViewHolder3(ViewGroup parent) {
            //你也可以不调用super，自己用黄油刀实现
            BaseViewHolder<ViewDataBinding> holder = super.onCreateViewHolder3(parent);
            holder.itemView.setBackgroundColor(0xffeeeeee);
            return holder;
        }
    }
```
ViewPager的Fragment更简单
```
mVp.setAdapter(new BaseFragmentPagerAdapter(getSupportFragmentManager(), mFrags));
//或
mVp.setAdapter(new BaseFragmentPagerAdapter(getSupportFragmentManager(), frag1,frag2...));
//动态修改frag
mAdapter = new BaseFragmentStatePagerAdapter(getSupportFragmentManager(), mFrags);
mVp.setAdapter(mAdapter);
...
mAdapter.getFragments().add(xxx);//由于内部有新的list，所以并不能用自己的mFrags
mAdapter.getFragments().remove(yyy);
mAdapter.notifyDataSetChanged();
//解决动态修改刷新白屏的问题
BaseFragmentNotifyAdapter adapter = new BaseFragmentNotifyAdapter(getSupportFragmentManager(), mFrags);
mVp.setAdapter(adapter);
...
adapter.notifyAllItem(1);//保留展示的frag这样就不会白屏了，想要刷新这个frag当然需要自己frag内部刷新了，详见app下的示例
```
还有适用于各种复杂样式的adapter容器（如：聊天列表，首页、今日头条的列表等）：
```
本项目已默认导入，直接使用即可：https://github.com/weimingjue/BaseContainerAdapter
```

## 导入方式
你的build.gradle要有jitpack.io，大致如下
```
allprojects {
    repositories {
        maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
        maven { url 'https://jitpack.io' }
        google()
        jcenter()
    }
}
```
然后导入
AndroidX（推荐dataBinding）：
`implementation（或api） 'com.github.weimingjue:BaseAdapter:3.01'`
AndroidX（旧版黄油刀那种形式）：
`implementation（或api） 'com.github.weimingjue:BaseAdapter:2.11'`
