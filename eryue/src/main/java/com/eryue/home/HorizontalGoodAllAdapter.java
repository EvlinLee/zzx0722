package com.eryue.home;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eryue.R;
import com.eryue.live.SearchLiveActivity;
import com.eryue.util.Logger;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

/**
 * Created by enjoy on 2018/5/16.
 * 首页今日爆款、百元券、分类、京东、拼多多、蘑菇街、苏宁
 */

public class HorizontalGoodAllAdapter extends BaseAdapter {

    private Context context;

    final String[] businessType = {"tb","", "", "", "jd", "pdd", "mgj", "sn"};

    DisplayImageOptions optionsSmall = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .showImageOnLoading(R.drawable.img_default_contract)
            .showImageOnFail(R.drawable.img_default_contract)
            .considerExifParams(true).build();

    private List<String> dataList;

    /**
     * 图标
     */
    private List<Integer> imgList;


    public HorizontalGoodAllAdapter(Context context) {
        this.context = context;
    }


    public List<String> getDataList() {
        return dataList;
    }


    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    public void setImgList(List<Integer> imgList) {
        this.imgList = imgList;
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
        if (null != dataList && position < dataList.size()) {
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
        HorizontalGoodAllAdapter.HomeHoriViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new HorizontalGoodAllAdapter.HomeHoriViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_homegoodall, null);
            viewHolder.iv_homehori = (ImageView) convertView.findViewById(R.id.iv_homehori);
            viewHolder.tv_homehori = (TextView) convertView.findViewById(R.id.tv_homehori);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HorizontalGoodAllAdapter.HomeHoriViewHolder) convertView.getTag();
        }

        if (imgList.get(position) != 0) {
//            ImageLoader.getInstance().displayImage("drawable://"+imgList.get(position),viewHolder.iv_homehori,optionsSmall);
            Glide.with(context).load(imgList.get(position)).into(viewHolder.iv_homehori);
        }
        if (!TextUtils.isEmpty(dataList.get(position))) {
            viewHolder.tv_homehori.setText(dataList.get(position));
            viewHolder.name = dataList.get(position);
        }
        viewHolder.position = position;

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HomeHoriViewHolder viewHolder = (HomeHoriViewHolder) view.getTag();
                if (null == viewHolder){
                    return;
                }

                String title = viewHolder.name;

                Logger.getInstance(context).writeLog_new("goodsall","goodsall","onClick-----title="+title);
                if ("今日爆款".equals(title) || "百元券".equals(title)) {

                    Intent intent = new Intent(context, SearchLiveActivity.class);
                    intent.putExtra("title", title);
                    context.startActivity(intent);

                } else if ("分类".equals(title)) {

                    context.startActivity(new Intent(context, GoodsCatActivity.class));

                } else {
                    Intent intent = new Intent(context, SearchLiveActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("type", businessType[viewHolder.position]);
                    context.startActivity(intent);
                }


            }
        });

        return convertView;
    }

    public class HomeHoriViewHolder {
        public ImageView iv_homehori;
        public TextView tv_homehori;
        public String name;
        public int position;

    }
}
