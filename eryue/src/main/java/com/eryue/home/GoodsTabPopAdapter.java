package com.eryue.home;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eryue.R;

import java.util.List;

/**
 * Created by bli.Jason on 2018/2/9.
 */

public class GoodsTabPopAdapter extends BaseAdapter{

    private Context context;
    private List<GoodsTabModel> tabList;

    public GoodsTabPopAdapter(Context context){
        this.context = context;
    }

    public List<GoodsTabModel> getTabList() {
        return tabList;
    }

    public void setTabList(List<GoodsTabModel> tabList) {
        this.tabList = tabList;
    }

    @Override
    public int getCount() {
        if (null==tabList){
            return 0;
        }
        return tabList.size();
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
        GoodsTabPopViewHolder viewHolder;
        if (null == convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_pop_goodstab,null);

            viewHolder = new GoodsTabPopViewHolder();
            viewHolder.tv_tab = (TextView) convertView.findViewById(R.id.tv_tab);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (GoodsTabPopViewHolder) convertView.getTag();
        }
        GoodsTabModel goodsTabModel = tabList.get(position);
        if (null!=goodsTabModel){
            viewHolder.tv_tab.setText(goodsTabModel.getName());
            if (goodsTabModel.getSelectTag() == GoodsTabModel.TAG_SELECT){
                viewHolder.tv_tab.setBackgroundResource(R.drawable.shape_tabs_focus);
                viewHolder.tv_tab.setTextColor(Color.WHITE);
            }else{
                viewHolder.tv_tab.setBackgroundResource(R.drawable.shape_tabs_unfocus);
                viewHolder.tv_tab.setTextColor(context.getResources().getColor(R.color.txt_gray));
            }
        }
        return convertView;
    }

    public class GoodsTabPopViewHolder{

        public TextView tv_tab;

    }

}
