package com.eryue.goodsdetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.eryue.R;
import com.library.util.UIScreen;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

/**
 * Created by bli.Jason on 2018/2/12.
 */

public class ImageListViewAdapter extends BaseAdapter {

    DisplayImageOptions optionsSmall = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)    //设置图片的质量
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)    //设置图片的缩放类型，该方法可以有效减少内存的占用
            .showImageOnLoading(R.drawable.img_default_contract)
            .showImageOnFail(R.drawable.img_default_contract)
            .considerExifParams(true).build();

    private List<String> dataList;

    private Context context;
    public ImageListViewAdapter(Context context) {
        this.context = context;
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }


    @Override
    public int getCount() {
        if (null==dataList){
            return 0;
        }else{
            return dataList.size();
        }
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

        final ImageViewHolder viewHolder;
        if (null == convertView){
            viewHolder = new ImageViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_img,null);
            viewHolder.iv_goodsdetail = (ImageView) convertView.findViewById(R.id.iv_goodsdetail);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ImageViewHolder) convertView.getTag();
        }

        if (null!=dataList&&position<dataList.size()){
//            ImageLoader.getInstance().displayImage(dataList.get(position),viewHolder.iv_goodsdetail,optionsSmall);
            Glide.with(context).load(dataList.get(position)).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)viewHolder.iv_goodsdetail.getLayoutParams();
                    lp.width = UIScreen.screenWidth;
                    float tempHeight=height * ((float)lp.width / width);
                    lp.height =(int)tempHeight ;
                    viewHolder.iv_goodsdetail.setLayoutParams(lp);
                    viewHolder.iv_goodsdetail.setImageBitmap(bitmap);
                }
            });

        }
        return convertView;
    }

    public class ImageViewHolder{
        public ImageView iv_goodsdetail;
    }
}
