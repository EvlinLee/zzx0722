package com.eryue.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eryue.R;

import java.util.List;

/**
 * Created by enjoy on 2018/2/18.
 * 猜你喜欢
 */

public class HorizontalLikeListAdapter extends BaseAdapter{

    private Context context;

    private List<String> dataList;

    public HorizontalLikeListAdapter(Context context){
        this.context = context;
    }


    public List<String> getDataList() {
        return dataList;
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        if (null!=dataList){
            return dataList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LikeViewHolder viewHolder;
        if (null == convertView){
            viewHolder = new LikeViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_goodslike,null);
            viewHolder.iv_goodslike = (ImageView) convertView.findViewById(R.id.iv_goodslike);
            viewHolder.tv_name_goodslike = (TextView) convertView.findViewById(R.id.tv_name_goodslike);
            viewHolder.tv_after_goodslike = (TextView) convertView.findViewById(R.id.tv_after_goodslike);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (LikeViewHolder) convertView.getTag();
        }

        viewHolder.tv_name_goodslike.setText(dataList.get(position));

        return convertView;
    }

    public class LikeViewHolder{
        public ImageView iv_goodslike;
        public TextView tv_name_goodslike;
        public TextView tv_after_goodslike;
    }
}
