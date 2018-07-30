package com.eryue.goodsdetail;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;

import com.eryue.activity.BaseFragment;
import com.eryue.ui.UIListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enjoy on 2018/2/11.
 */

public class GoodsDetailImageFragment extends BaseFragment implements GoodsDetailPresenter.IGoodsImageListener {

    public static final String taobaoUrl = "https://hws.m.taobao.com/cache/mtop.wdetail.getItemDescx/4.1/?data={item_num_id%3A%22itemId%22}&type=jsonp&dataType=jsonp";

    private UIListView imgListView;
    private ImageListViewAdapter imageAdapter;

    private GoodsDetailPresenter presenter;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imgListView = new UIListView(getContext());
        setContentView(imgListView);

        imgListView.setDivider(null);
        imgListView.setDividerHeight(0);

        initData();
    }


    private void initData(){

//        List<String> dataList = new ArrayList<>();
//        dataList.add("https://img.alicdn.com/tfscom/i2/2837343527/TB2fFqmc88kpuFjSspeXXc7IpXa_!!2837343527.jpg");
//        dataList.add("https://img.alicdn.com/tfscom/i2/2837343527/TB2fFqmc88kpuFjSspeXXc7IpXa_!!2837343527.jpg");
//        dataList.add("https://img.alicdn.com/tfscom/i2/2837343527/TB2fFqmc88kpuFjSspeXXc7IpXa_!!2837343527.jpg");
//        dataList.add("https://img.alicdn.com/tfscom/i2/2837343527/TB2fFqmc88kpuFjSspeXXc7IpXa_!!2837343527.jpg");
//        dataList.add("https://img.alicdn.com/tfscom/i2/2837343527/TB2fFqmc88kpuFjSspeXXc7IpXa_!!2837343527.jpg");

        imageAdapter = new ImageListViewAdapter(getContext());
//        imageAdapter.setDataList(dataList);
        imgListView.setAdapter(imageAdapter);

        String itemId = getActivity().getIntent().getStringExtra("itemId");
        if (TextUtils.isEmpty(itemId)){
            return;
        }
        String url = taobaoUrl.replace("itemId",itemId);
        presenter = new GoodsDetailPresenter();
        presenter.setImageListener(this);
        presenter.requestGoodsImg(url);

    }

    @Override
    public void onImageBack(List<String> images) {
        imageAdapter.setDataList(images);
        imageAdapter.notifyDataSetChanged();

    }

    @Override
    public void onImageError() {

    }
}
