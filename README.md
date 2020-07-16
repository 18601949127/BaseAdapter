# 代码非常简单

## 详细示例见本项目app下的MainActivity
一个listAdapter只需要如下几行
```
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
        });
```
自带header、footer
```
        adapter.setHeaderView(view);
        adapter.setFooterView(view);
```
懒人专属
```
BaseAdapterRvList<BaseViewHolder, String> adapter = BaseAdapterRvList.createAdapter(R.layout.adapter_main_list, new BaseAdapterRvList.OnAdapterBindListener<String>() {
    @Override
    public void onBindVH(BaseViewHolder holder, int listPosition, String s) {
        holder.setText(R.id.tv_main_adapter_text, s);
    }
});
mRv.setAdapter(adapter);
//...刷新数据时
adapter.setListAndNotifyDataSetChanged(list);
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
        adapter.notifyAllItem(1);//保留展示在界面上的那个这样就不会白屏了，想要刷新保留的frag当然需要自己实现了，详见app下的示例
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
`implementation（或api） 'com.github.weimingjue:BaseAdapter:1.06'`

AndroidX：
`implementation（或api） 'com.github.weimingjue:BaseAdapter:2.11'`
