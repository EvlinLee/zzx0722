package com.eryue.home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eryue.R;
import com.library.util.CommonFunc;

import net.InterfaceManager;

import java.util.List;

/**
 * Created by enjoy on 2018/2/21.
 */

public class GoodsDailyAdapter extends BaseAdapter {
    private Context context;

    private List<InterfaceManager.SearchProductInfo> dataList;

    public GoodsDailyAdapter(Context context){
        this.context = context;
    }


    public List<InterfaceManager.SearchProductInfo> getDataList() {
        return dataList;
    }

    public void setDataList(List<InterfaceManager.SearchProductInfo> dataList) {
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
        GoodsDailyViewHolder viewHolder;
        if (null == convertView){
            viewHolder = new GoodsDailyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_goodsdaily,null);
            viewHolder.iv_goodsdaily = (ImageView) convertView.findViewById(R.id.iv_goodsdaily);
            viewHolder.tv_name_goodsdaily = (TextView) convertView.findViewById(R.id.tv_name_goodsdaily);
            viewHolder.tv_price_goodsdaily = (TextView) convertView.findViewById(R.id.tv_price_goodsdaily);
            viewHolder.tv_after_goodsdaily = (TextView) convertView.findViewById(R.id.tv_after_goodsdaily);
            viewHolder.tv_price_paper = (TextView) convertView.findViewById(R.id.tv_price_paper);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (GoodsDailyViewHolder) convertView.getTag();
        }

        InterfaceManager.SearchProductInfo searchProductInfo = dataList.get(position);
        //商品图片
        Glide.with(context).load(searchProductInfo.pictUrl).placeholder(R.drawable.img_default_contract).into(viewHolder.iv_goodsdaily);
        //商品名称
        viewHolder.tv_name_goodsdaily.setText(searchProductInfo.itemTitle);
        //商品原价
        viewHolder.tv_price_goodsdaily.setText("原价 ¥"+CommonFunc.fixText(searchProductInfo.discountPrice,2));
        //商品券后价格
        viewHolder.tv_after_goodsdaily.setText("¥"+CommonFunc.fixText(searchProductInfo.afterQuan,2));
        //券价格
        viewHolder.tv_price_paper.setText("券￥"+CommonFunc.fixText(searchProductInfo.quanPrice));

        return convertView;
    }

    public class GoodsDailyViewHolder{
        public ImageView iv_goodsdaily;
        public TextView tv_name_goodsdaily;
        public TextView tv_price_goodsdaily;
        public TextView tv_after_goodsdaily;
        public TextView tv_price_paper;
    }
}
