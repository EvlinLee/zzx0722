package com.eryue.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.eryue.R;

import java.util.ArrayList;
import java.util.List;



//CHECKSTYLE:OFF

/**
 * 文本下拉选项框(单选)
 * Created by bli.Jason on 2017/4/25.
 */
public class StockDatePopView implements AdapterView.OnItemClickListener {



    private ListView listview_date;
    private StockDatePopAdapter adapter;


    //popview
    private PopupWindow window;

    private Context context;

    private View contentView;

    private int type = 0;


    public StockDatePopView(Context context) {
        this.context = context;

        initView();
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private void initView() {

        window = new PopupWindow(context);
        window.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        window.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.lv_item_shape));
        window.setFocusable(true);
        window.setTouchable(true);
        window.setOutsideTouchable(false);

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        window.update();


        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.f5_view_optionpop_date, null);

            listview_date = (ListView) contentView.findViewById(R.id.listview_date);
            adapter = new StockDatePopAdapter(context);
            listview_date.setAdapter(adapter);

        }

        listview_date.setOnItemClickListener(this);


        window.setContentView(contentView);


    }

    public void setData(int checkedIndex, String[] strs, int[] values) {

        List<StockDatePopModel> popModelList = new ArrayList<>();
        StockDatePopModel popModel;
        prePosition = checkedIndex;
        for (int i = 0; i < strs.length; i++) {

            popModel = new StockDatePopModel();
            popModel.setShowString(strs[i]);
            popModel.setValue(values[i]);
            popModel.setIndex(i);
            if (i == checkedIndex) {
                popModel.setChecked(true);
            }

            popModelList.add(popModel);
        }
        refreshListData(popModelList);

    }

    public void setData(String[] strs, int[] values) {

        List<StockDatePopModel> popModelList = new ArrayList<>();
        StockDatePopModel popModel;
        prePosition = 0;
        for (int i = 0; i < strs.length; i++) {

            popModel = new StockDatePopModel();
            popModel.setShowString(strs[i]);
            popModel.setValue(values[i]);
            popModel.setIndex(i);

            if (i == prePosition) {
                popModel.setChecked(true);
            }

            popModelList.add(popModel);
        }
        refreshListData(popModelList);

    }

    //设置选中项
    public void setSelectOption(int index) {

        if (null != adapter) {
            if (null != adapter.getDataList() && !adapter.getDataList().isEmpty()) {

                List<StockDatePopModel> popModelList = adapter.getDataList();
                StockDatePopModel popModel;

                if (index < 0 || index > popModelList.size() - 1) {
                    return;
                }

                for (int i = 0; i < popModelList.size(); i++) {

                    popModel = popModelList.get(i);
                    if (i == index) {
                        popModel.setChecked(true);
                    } else {
                        popModel.setChecked(false);
                    }

                }
                prePosition = index;
                refreshListData(popModelList);

            }
        }

    }


    //刷新列表数据
    public void refreshListData(List<StockDatePopModel> dataList) {

        if (null != adapter) {
            adapter.setDataList(dataList);
            adapter.notifyDataSetChanged();
        }
    }


    //显示popview
    public void showPopView(View view) {
        if (null != adapter) {
            adapter.notifyDataSetChanged();
        }
        int minNeedWidth = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View child = adapter.getView(i, null, listview_date);
            child.measure(0, 0);
            if (child.getMeasuredWidth() > minNeedWidth) {
                minNeedWidth = child.getMeasuredWidth();
            }
        }
        minNeedWidth += 20;

        if (null != window) {
            window.setWidth(minNeedWidth);
            window.showAsDropDown(view);
        }
    }


    private int prePosition = -1;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (null != window) {
            window.dismiss();
        }

        if (prePosition == position) {

            return;
        }


        if (null != onDateClickListener) {

            List<StockDatePopModel> dataList = adapter.getDataList();


            if (prePosition != -1) {
                dataList.get(prePosition).setChecked(false);
            }
            dataList.get(position).setChecked(true);

            StockDatePopModel model;

            onDateClickListener.onDateItemClick(dataList.get(position));
        }


        prePosition = position;
        if (null != window) {
            window.dismiss();
        }

    }


    private OnDateClickListener onDateClickListener;

    public OnDateClickListener getOnDateClickListener() {
        return onDateClickListener;
    }

    public void setOnDateClickListener(OnDateClickListener onDateClickListener) {
        this.onDateClickListener = onDateClickListener;
    }

    public interface OnDateClickListener {


        void onDateItemClick(StockDatePopModel model);

    }
}
