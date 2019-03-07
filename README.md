#代码非常简单

##详细示例见本项目app下的MainActivity
一个listAdapter只需要如下几行
```
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
```
##导入方式
implementation（或api） 'com.github.weimingjue:BaseAdapter:1.0'