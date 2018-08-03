package com.eryue.widget;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eryue.R;
import com.eryue.activity.ImageBrowserActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * Created by enjoy on 2018/7/5.
 */

public class ImageGridAdapter extends BaseAdapter {

    Context context;

    private List<ImageGridView.ImageModel> dataList = new ArrayList<>();

    private static int GRIDVIEW_MAX_COUNT = 9;

    public ImageGridAdapter(Context context){
        this.context = context;
    }

    public void setDataList(List<ImageGridView.ImageModel> dataList) {
        if (null==dataList){
            return;
        }
        this.dataList.clear();
        this.dataList.addAll(dataList);
    }

    @Override
    public int getCount() {
        if (null==dataList){
            return 0;
        }
        if (dataList.size() > GRIDVIEW_MAX_COUNT) {
            return 9;
        }
        return dataList.size();
    }

    @Override
    public ImageGridView.ImageModel getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final ViewHolder viewHolder;
        if(null == view){
            view = LayoutInflater.from(context).inflate(R.layout.view_image_scroll_item,null);
            viewHolder = new ViewHolder();
            viewHolder.image_view = view.findViewById(R.id.image_view);
            viewHolder.check_image = view.findViewById(R.id.check_image);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }


        final ImageGridView.ImageModel imageModel = dataList.get(i);
        if (null == imageModel){
            return view;
        }

        Glide.with(context).load(imageModel.url)
                .transform(new GlideRoundTransform(context,10))
                .placeholder(R.drawable.img_default_contract)
                .into(viewHolder.image_view);

        if (imageModel.isChecked) {
            viewHolder.check_image.setImageResource(R.drawable.bt_cjsp_xz);
        } else {
            viewHolder.check_image.setImageResource(R.drawable.bt_cjsp_gx);
        }

        final ArrayList browseImagelist = new ArrayList<>();
        final int finalI = i;

         viewHolder.image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageList(browseImagelist);
                Intent intent = new Intent(context, ImageBrowserActivity.class);
                intent.putExtra(ImageBrowserActivity.KEY_INDEX, finalI);
                intent.putStringArrayListExtra(ImageBrowserActivity.KEY_URL,browseImagelist);
                context.startActivity(intent);
                browseImagelist.clear(); //每次点击完毕清空列表里的值
            }
        });

         viewHolder.check_image.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 imageModel.isChecked = !imageModel.isChecked;
                 NoScrollGridViewCallBack.itemCall();
                 notifyDataSetChanged();
             }
         });

        return view;
    }

    public void getImageList(ArrayList imageList){
        for (int j = 0;j<dataList.size();j++) {
            ImageGridView.ImageModel imageModel1 = dataList.get(j);
            imageList.add(imageModel1.url);

        }
    }

    public class ViewHolder{

        public ImageView image_view;

        public ImageView check_image;

    }

}
