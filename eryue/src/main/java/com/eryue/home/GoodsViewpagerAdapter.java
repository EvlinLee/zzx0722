package com.eryue.home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eryue.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bli.Jason on 2018/2/8.
 */

public class GoodsViewpagerAdapter extends PagerAdapter {

    private Context conext;
    private List<Goods> dataList;
    private int childCount = 4;
    public GoodsGridView itemView;
    private List<GoodsGridView> viewList;

    public GoodsViewpagerAdapter(Context conext) {
        this.conext = conext;
        init();
    }

    private void init(){
        viewList = new ArrayList<>();
        for (int i=0;i<childCount;i++){
            itemView = new GoodsGridView(conext);
            viewList.add(itemView);
        }
    }

    public void setDataList(List<Goods> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        if (null==dataList){
            return 0;
        }
        return dataList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        GoodsGridView childView = viewList.get(position % childCount);
        if (null!=childView&&null!=childView.getParent()){
            ((ViewGroup)childView.getParent()).removeView(childView);
        }
        container.addView(childView);
        childView.requestLayout();
        return childView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        GoodsGridView childView = viewList.get(position % childCount);
//        container.removeView((View)object);
//        clearHideView(position);
    }

    private int lastPosition = -1;

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (position == lastPosition) {
            return;
        } else {
            lastPosition = position;
            ((GoodsGridView)object).initData();
//            NewsDetailView view = ((NewsDetailView) object);
//            if (view.newsDetilNewscontentScroll.getVisibility() == VISIBLE) {
//                view.newsDetilNewscontentScroll.requestFocus();
//            } else if (view.webView4Internet.getVisibility() == VISIBLE) {
//                view.webView4Internet.requestFocus();
//            }
        }
    }

    public int getItemPosition(Object object) {
        // 重写getItemPosition,保证每次获取时都强制重绘UI
        if (dataList != null && dataList.size()==0) {
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    //清除藏在后面的view的内容
    private void clearHideView(int position) {
        GoodsGridView view = viewList.get(position % childCount);
        if (null != view) {
            view.dispose();
        }
    }

}
