# 代码非常简单

## 详细示例见本项目app下的MainActivity
一个listAdapter只需要如下几行
```
    public class ListAdapter extends BaseAdapterRvList<BaseViewHolder, String> {

        public ListAdapter(@NonNull Activity activity, List<String> list) {
            super(activity, list);
        }

        @Override
        protected void onBindVH(BaseViewHolder holder, int listPosition, String s) {
            //当然，你也可以继承BaseViewHolder自己用黄油刀生成
            holder.setText(R.id.text, s).setViewVisible(R.id.text, s == null ? View.GONE : View.VISIBLE);
        }

        @NonNull
        @Override
        protected BaseViewHolder onCreateVH(ViewGroup parent, LayoutInflater inflater) {
            return new BaseViewHolder(parent,R.layout.adapter);
        }
    }
```
自带点击事件
```
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            protected void onItemClick(View view, int listPosition) {
            }

            @Override
            protected boolean onItemLongClick(View view, int listPosition) {
                return true;
            }

            @Override
            protected void onFooterClick(View view) {
                super.onFooterClick(view);
            }
            ...Header、LongClick等
        });
```
自带header、footer
```
        adapter.addHeaderView(view);
        adapter.addFooterView(view);
```
## 导入方式
你的build.gradle要有jitpack.io，大致如下
```
allprojects {
    repositories {
        jcenter()
        maven {
            url "https://maven.google.com"
        }
        maven {
            url "https://jitpack.io"
        }
    }
}
```
然后导入
`implementation（或api） 'com.github.weimingjue:BaseAdapter:V1.02'`
