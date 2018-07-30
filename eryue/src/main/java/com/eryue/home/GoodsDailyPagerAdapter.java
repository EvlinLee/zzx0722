package com.eryue.home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eryue.R;
import com.eryue.widget.AutoScrollViewPager.BaseViewPagerAdapter;

import net.InterfaceManager;

import java.util.List;

/**
 * Created by enjoy on 2018/2/21.
 */

public class GoodsDailyPagerAdapter extends PagerAdapter{

    private static final int COUNT_PAGER = 3;
    private Context context;
    private List<InterfaceManager.SearchProductInfo> dataList;

    public GoodsDailyPagerAdapter(Context context){
        this.context = context;
    }

    public void setDataList(List<InterfaceManager.SearchProductInfo> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        if (null!=dataList){
            int size = dataList.size();
            int count;

            if (size%COUNT_PAGER!=0){
                count = size/COUNT_PAGER+1;
            }else{
                count = size/COUNT_PAGER;
            }
            return count;
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((GoodsDailyView)object);
    }

    public Object instantiateItem(ViewGroup container, final int position) {
        GoodsDailyView dailyView = new GoodsDailyView(context);
        if ((position*COUNT_PAGER+COUNT_PAGER)<dataList.size()){
            dailyView.setData(dataList.subList(position*COUNT_PAGER,position*COUNT_PAGER+COUNT_PAGER));
        }else{
            dailyView.setData(dataList.subList(position*COUNT_PAGER,dataList.size()));
        }
        container.addView(dailyView);
        return dailyView;
    }


}
