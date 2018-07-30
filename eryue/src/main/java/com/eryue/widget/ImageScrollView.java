package com.eryue.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eryue.R;
import com.eryue.activity.ImageBrowserActivity;
import com.library.util.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


public class ImageScrollView extends LinearLayout implements View.OnTouchListener, View.OnClickListener {
    private Context context;
    private List<ImageModel> picsList;

    DisplayImageOptions optionsSmall = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .showImageOnLoading(R.drawable.img_default_contract)
            .showImageOnFail(R.drawable.img_default_contract)
            .considerExifParams(true).build();

    //已选数量
    private TextView tv_selectnum;

    private TextView tv_allselect;


    public ImageScrollView(Context context) {
        super(context);
        initViews(context);
    }

    public ImageScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public ImageScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_image_scroll, this);
        tv_selectnum = (TextView) findViewById(R.id.tv_selectnum);
        tv_allselect = findViewById(R.id.tv_allselect);

        tv_allselect.setOnClickListener(this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    private int totalChoose = 0;

    private List<ImageView> checkImageList = new ArrayList<>();

    public void setData(final List<ImageModel> picsList) {
        if (picsList == null || picsList.size() == 0) {
            return;
        }

        this.picsList = picsList;

        LinearLayout containView = (LinearLayout) findViewById(R.id.image_container);

        ImageModel imageModel;

        final ArrayList<String> urlList = new ArrayList<>();
        for (int i = 0; i < picsList.size(); i++) {
            imageModel = picsList.get(i);
            if (null != imageModel && !TextUtils.isEmpty(imageModel.url)) {
                urlList.add(imageModel.url);
            }
        }

        checkImageList.clear();
        for (int i = 0; i < picsList.size(); i++) {
            imageModel = picsList.get(i);
            if (null == imageModel || TextUtils.isEmpty(imageModel.url)) {
                continue;
            }
            final RelativeLayout subLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_image_scroll_item, null);
            final ImageView imageView = (ImageView) subLayout.findViewById(R.id.image_view);
            final ImageView checkImage = (ImageView) subLayout.findViewById(R.id.check_image);
            checkImage.setTag(imageModel);
            if (imageModel.isChecked) {
                totalChoose++;
                checkImage.setImageResource(R.drawable.ic_selected_selected);
            } else {
                checkImage.setImageResource(R.drawable.ic_selected_normal);
            }

            checkImageList.add(checkImage);
            final int finalI = i;
            checkImage.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {

                    setChoose(finalI, (ImageView) v);

                }
            });
            subLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ImageBrowserActivity.class);
                    intent.putExtra(ImageBrowserActivity.KEY_INDEX, finalI);
                    intent.putStringArrayListExtra(ImageBrowserActivity.KEY_URL, urlList);
                    context.startActivity(intent);
                }
            });

            Glide.with(context).load(imageModel.url).placeholder(R.drawable.img_default_contract).into(imageView);
//            ImageLoader.getInstance().displayImage(imageModel.url,imageView,optionsSmall);
            if (i > 0) {
                containView.addView(new View(getContext()), StringUtils.dipToPx(10), LayoutParams.MATCH_PARENT);
            }
            containView.addView(subLayout);
        }
    }

    private void setChoose(int position, ImageView iv) {

        ImageModel imageModel = (ImageModel) iv.getTag();
        boolean isChecked = imageModel.isChecked;

        boolean afterChecked = !isChecked;
        imageModel.isChecked = afterChecked;

        if (afterChecked) {
            iv.setImageResource(R.drawable.ic_selected_selected);
            totalChoose++;
        } else {
            iv.setImageResource(R.drawable.ic_selected_normal);
            totalChoose--;
        }


        if (null != tv_selectnum) {
            tv_selectnum.setText(String.valueOf(totalChoose));
        }

        if (null != onCheckListener) {
            onCheckListener.onCheck(position, afterChecked);
        }

        if (totalChoose == checkImageList.size()) {
            //取消全选
            tv_allselect.setText("全不选");
        } else {
            //全选
            tv_allselect.setText("全选");
        }
    }

    OnCheckListener onCheckListener;

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    @Override
    public void onClick(View view) {
        if (view == tv_allselect) {
            if (totalChoose == checkImageList.size()) {
                //取消全选
                selectAllImageView(false);
                tv_allselect.setText("全选");
            } else {
                //全选
                selectAllImageView(true);
                tv_allselect.setText("全不选");
            }

        }
    }

    private void selectAllImageView(boolean selectAll) {
        ImageView checkImageView;
        for (int i = 0; i < checkImageList.size(); i++) {
            checkImageView = checkImageList.get(i);
            if (null == checkImageView) {
                continue;
            }
            ImageModel imageModel = (ImageModel) checkImageView.getTag();
            imageModel.isChecked = selectAll;

            checkImageView.setImageResource(imageModel.isChecked ? R.drawable.ic_selected_selected : R.drawable.ic_selected_normal);
        }

        if (null != tv_selectnum) {
            totalChoose = selectAll ? checkImageList.size() : 0;
            tv_selectnum.setText(String.valueOf(totalChoose));
        }
    }

    public interface OnCheckListener {
        void onCheck(int position, boolean isChecked);
    }

    public static class ImageModel {
        public boolean isChecked;
        public String url;
    }
}
