package com.eryue.home;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eryue.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by enjoy on 2018/2/18.
 */

public class HomeHorizontalListAdapter extends BaseAdapter{

    private Context context;

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


    public HomeHorizontalListAdapter(Context context){
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
        if (null!=dataList){
            return dataList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null!=dataList&&position<dataList.size()){
            return dataList.get(position);
        }
        return  null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeHoriViewHolder viewHolder;
        if (null == convertView){
            viewHolder = new HomeHoriViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_homehorizontal,null);
            viewHolder.iv_homehori = (ImageView) convertView.findViewById(R.id.iv_homehori);
            viewHolder.tv_homehori = (TextView) convertView.findViewById(R.id.tv_homehori);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (HomeHoriViewHolder) convertView.getTag();
        }

        if(imgList.get(position)!=0){
//            ImageLoader.getInstance().displayImage("drawable://"+imgList.get(position),viewHolder.iv_homehori,optionsSmall);
            Glide.with(context).load(imgList.get(position)).into(viewHolder.iv_homehori);
        }
        if(!TextUtils.isEmpty(dataList.get(position))){
            viewHolder.tv_homehori.setText(dataList.get(position));
        }

        return convertView;
    }

    public class HomeHoriViewHolder{
        public ImageView iv_homehori;
        public TextView tv_homehori;
    }
}
