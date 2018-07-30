package com.eryue.search;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eryue.R;

import java.util.List;

/**
 * Created by enjoy on 2018/2/14.
 */

public class HotSearchAdapter extends BaseAdapter {


    private Context context;
    private List<String> dataList;

    public HotSearchAdapter(Context context) {
        this.context = context;
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        if (null != dataList) {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != dataList) {
            return dataList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HotSearchViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new HotSearchViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_hotsearch, null);
            viewHolder.tv_item_search = (TextView) convertView.findViewById(R.id.tv_item_search);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HotSearchViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(dataList.get(position))) {
            viewHolder.tv_item_search.setText(dataList.get(position));
        }
        return convertView;
    }

    public class HotSearchViewHolder {
        public TextView tv_item_search;
    }
}
