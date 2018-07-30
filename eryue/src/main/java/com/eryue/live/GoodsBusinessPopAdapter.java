package com.eryue.live;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eryue.R;
import com.eryue.home.GoodsTabModel;

import java.util.List;

/**
 * Created by bli.Jason on 2018/2/9.
 */

public class GoodsBusinessPopAdapter extends BaseAdapter{

    private Context context;
    private List<GoodsBusinessModel> tabList;

    private int colorSelected;

    public GoodsBusinessPopAdapter(Context context){
        this.context = context;
        colorSelected = context.getResources().getColor(R.color.red_lite);
    }

    public List<GoodsBusinessModel> getTabList() {
        return tabList;
    }

    public void setTabList(List<GoodsBusinessModel> tabList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_pop_goodsbusiness,null);

            viewHolder = new GoodsTabPopViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.iv_choose = convertView.findViewById(R.id.iv_choose);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (GoodsTabPopViewHolder) convertView.getTag();
        }
        GoodsBusinessModel businessModel = tabList.get(position);
        if (null!=businessModel){
            viewHolder.tv_name.setText(businessModel.getName());
            if (businessModel.getSelectTag() == GoodsTabModel.TAG_SELECT){
                viewHolder.tv_name.setTextColor(colorSelected);
                viewHolder.iv_choose.setVisibility(View.VISIBLE);
            }else{
                viewHolder.tv_name.setTextColor(context.getResources().getColor(R.color.txtcolor_business));
                viewHolder.iv_choose.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    public class GoodsTabPopViewHolder{

        public TextView tv_name;

        public ImageView iv_choose;

    }

}
