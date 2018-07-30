package com.eryue.widget;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eryue.R;

import java.util.List;


public class StockDatePopAdapter extends BaseAdapter {


    private Context context;

    private List<StockDatePopModel> dataList;

    public StockDatePopAdapter(Context context) {

        this.context = context;

    }

    public List<StockDatePopModel> getDataList() {
        return dataList;
    }

    public void setDataList(List<StockDatePopModel> dataList) {
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
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.f5_adapter_option_date, null);

            viewHolder = new ViewHolder();
            viewHolder.tv_name_date = (TextView) convertView.findViewById(R.id.tv_name_date);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        StockDatePopModel model = dataList.get(position);

        if (null != model) {
            viewHolder.tv_name_date.setText(model.getShowString());
        }

        if (model.isChecked()) {
            viewHolder.tv_name_date.setTextColor(0xFF0385C0);
        } else {
            viewHolder.tv_name_date.setTextColor(Color.parseColor("#ffffff"));
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView tv_name_date;
    }
}
