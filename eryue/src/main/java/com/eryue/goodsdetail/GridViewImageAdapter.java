package com.eryue.goodsdetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.eryue.R;
import com.eryue.home.HomeHorizontalListAdapter;
import com.eryue.widget.AutoScrollViewPager.BaseViewPagerAdapter;
import com.library.util.StringUtils;
import com.library.util.UIScreen;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

/**
 * Created by bli.Jason on 2018/2/12.
 */

public class GridViewImageAdapter extends BaseAdapter {

    private Context context;

    private List<String> dataList;

    DisplayImageOptions optionsSmall = new DisplayImageOptions.Builder()
            .cacheOnDisk(true)
            .cacheInMemory(true)
            .bitmapConfig(Bitmap.Config.RGB_565)    //设置图片的质量
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)    //设置图片的缩放类型，该方法可以有效减少内存的占用
            .showImageOnLoading(R.drawable.img_default_contract)
            .showImageOnFail(R.drawable.img_default_contract).build();

    public GridViewImageAdapter(Context context){
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
        final GridImageViewHolder viewHolder;
        if (null == convertView){
            viewHolder = new GridImageViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_gridimage,null);
            viewHolder.iv_grid = (ImageView) convertView.findViewById(R.id.iv_grid);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (GridImageViewHolder) convertView.getTag();
        }

//        ImageLoader.getInstance().displayImage(dataList.get(position),viewHolder.iv_grid,optionsSmall);
//        Glide.with(context).load(dataList.get(position)).placeholder(R.drawable.img_default_contract).into(viewHolder.iv_grid);
        Glide.with(context).load(dataList.get(position)).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                if (null!=viewHolder.iv_grid){
                    if (viewHolder.iv_grid.getScaleType()!=ImageView.ScaleType.FIT_XY){
                        viewHolder.iv_grid.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)viewHolder.iv_grid.getLayoutParams();
                    lp.width = UIScreen.screenWidth/2- StringUtils.dipToPx(10);
                    float tempHeight=height * ((float)lp.width / width);
                    lp.height =(int)tempHeight ;
                    viewHolder.iv_grid.setLayoutParams(lp);
                    viewHolder.iv_grid.setImageBitmap(bitmap);
                }

            }
        });
        return convertView;
    }

    public class GridImageViewHolder{
        public ImageView iv_grid;
    }
}
