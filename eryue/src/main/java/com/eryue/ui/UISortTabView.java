package com.eryue.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eryue.R;
import com.library.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enjoy on 2018/5/10.
 */

public class UISortTabView extends LinearLayout implements View.OnClickListener {

    /**
     * 普通(不带排序及向下箭头)
     */
    public static final int TYPE_NORMAL = 0;
    /**
     * 带排序箭头
     */
    public static final int TYPE_SORT = 1;
    /**
     * 带向下箭头
     */
    public static final int TYPE_DOWN = 2;

    /**
     * 未选中
     */
    public static final int FLAG_NORMAL = 0;

    /**
     * 选中
     */
    public static final int FLAG_FOCUS = 1;

    /**
     * 降序
     */
    public static final int FLAG_DOWN = 2;

    /**
     * 升序
     */
    public static final int FLAG_UP = 3;

    /**
     * 类型
     */
    private List<Integer> tabTypeList = new ArrayList<>();

    /**
     * 名称
     */
    private List<String> tabNameList = new ArrayList<>();

    /**
     * 类型
     */
    private int[] tabTypeArray;

    /**
     * 名称
     */
    private String[] tabNameArray;

    private int selectIndex = -1;

    private final int color_focus = 0xffff1a40;

    public UISortTabView(Context context) {
        super(context);
        setPadding(0, StringUtils.dipToPx(15), 0, StringUtils.dipToPx(15));
    }

    public UISortTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setPadding(0, StringUtils.dipToPx(15), 0, StringUtils.dipToPx(15));
    }

    /**
     * 设置tab名称
     *
     * @param tabNameList
     */
    public void setTabNameList(List<String> tabNameList) {
        this.tabNameList = tabNameList;

        initView();
    }

    public void setData(List<Integer> tabTypeList, List<String> tabNameList) {
        this.tabTypeList = tabTypeList;
        this.tabNameList = tabNameList;

        initView();
    }

    public void setDataArray(int[] tabTypeArray, String[] tabNameArray) {
        this.tabTypeArray = tabTypeArray;
        this.tabNameArray = tabNameArray;

        initViewArray();
    }


    public void setDataArray(int selectIndex, int[] tabTypeArray, String[] tabNameArray) {
        this.selectIndex = selectIndex;
        this.tabTypeArray = tabTypeArray;
        this.tabNameArray = tabNameArray;

        setDataArray(tabTypeArray, tabNameArray);
    }

    /**
     * 初始化其他tab
     */
    private void initView() {

        if (null == tabNameList || tabNameList.isEmpty()) {
            return;
        }

        LinearLayout layout_item;
        TextView tv_item;
        ImageView iv_item;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        for (int i = 0; i < tabNameList.size(); i++) {
            layout_item = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_sorttab_item, null);
            tv_item = layout_item.findViewById(R.id.tv_item);
            iv_item = layout_item.findViewById(R.id.iv_item);
            tv_item.setText(tabNameList.get(i));


            if (null != tabTypeList && !tabTypeList.isEmpty()) {

                if (tabTypeList.get(i) == TYPE_SORT) {
                    iv_item.setVisibility(View.VISIBLE);
                    iv_item.setImageResource(R.drawable.icon_sort_default);
                } else if (tabTypeList.get(i) == TYPE_DOWN) {
                    iv_item.setVisibility(View.VISIBLE);
                    iv_item.setImageResource(R.drawable.icon_sort_arrow);
                } else {
                    iv_item.setVisibility(View.GONE);
                }

            } else {
                iv_item.setVisibility(View.GONE);
            }

            layout_item.setOnClickListener(this);
            layout_item.setTag(i + "," + FLAG_DOWN);

            addView(layout_item, lp);

        }

    }


    List<LinearLayout> itemLayoutList = new ArrayList<>();

    /**
     * 初始化其他tab
     */
    private void initViewArray() {

        if (null == tabNameArray || tabNameArray.length == 0) {
            return;
        }

        LinearLayout layout_item;
        TextView tv_item;
        ImageView iv_item;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);

        int flag;
        for (int i = 0; i < tabNameArray.length; i++) {
            layout_item = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_sorttab_item, null);
            tv_item = layout_item.findViewById(R.id.tv_item);
            iv_item = layout_item.findViewById(R.id.iv_item);
            tv_item.setText(tabNameArray[i]);

            itemLayoutList.add(layout_item);


            flag = FLAG_NORMAL;

            if (null != tabTypeArray && tabTypeArray.length != 0) {

                if (tabTypeArray[i] == TYPE_SORT) {
                    iv_item.setVisibility(View.VISIBLE);
                    iv_item.setImageResource(R.drawable.icon_sort_default);

                    if (i == selectIndex) {
                        tv_item.setTextColor(color_focus);
                        iv_item.setImageResource(R.drawable.icon_sort_up);
                        flag = FLAG_DOWN;
                    }
                } else if (tabTypeArray[i] == TYPE_DOWN) {
                    iv_item.setVisibility(View.VISIBLE);
                    iv_item.setImageResource(R.drawable.icon_sort_arrow);
                    if (i == selectIndex) {
                        tv_item.setTextColor(color_focus);
                        flag = FLAG_FOCUS;
                    }
                } else {
                    if (i == selectIndex) {
                        tv_item.setTextColor(color_focus);
                        flag = FLAG_FOCUS;
                    }
                    iv_item.setVisibility(View.GONE);
                }

            } else {
                iv_item.setVisibility(View.GONE);
            }

            layout_item.setOnClickListener(this);
            layout_item.setTag(i + "," + tabTypeArray[i] + "," + flag);

            addView(layout_item, lp);

        }

    }

    /**
     * 设置文字
     * @param index
     * @param name
     */
    public void setTabText(int index, String name) {

        if (null == itemLayoutList || (itemLayoutList.size() - 1) < index) {
            return;
        }
        //清除view的状态
        LinearLayout itemLayout = itemLayoutList.get(index);
        TextView itemTV = (TextView) itemLayout.getChildAt(0);
        ImageView itemIV = (ImageView) itemLayout.getChildAt(1);
        itemTV.setText(name);
    }


    @Override
    public void onClick(View view) {

        if (null == view.getTag() || !(view instanceof LinearLayout)) {
            return;
        }
        LinearLayout itemLayout = (LinearLayout) view;
        TextView itemTV = (TextView) itemLayout.getChildAt(0);
        ImageView itemIV = (ImageView) itemLayout.getChildAt(1);
        if (null == itemTV || null == itemIV) {
            return;
        }


        String tag = (String) view.getTag();
        if (!tag.contains(",")) {
            return;
        }

        String[] tagArray = tag.split(",");
        if (null == tagArray || tagArray.length != 3) {
            return;
        }

        int index = StringUtils.valueOfInt(tagArray[0]);
        int type = StringUtils.valueOfInt(tagArray[1]);
        int flag = StringUtils.valueOfInt(tagArray[2]);

        //清除view的状态
        for (int i = 0; i < itemLayoutList.size(); i++) {
            if (i != index) {
                clearStatus(itemLayoutList.get(i));
            }
        }

        itemTV.setTextColor(0xffff1a40);
        switch (type) {
            case TYPE_NORMAL:
                if (flag == FLAG_NORMAL) {
                    //选中色：红色
                    flag = FLAG_FOCUS;

                } else if (flag == FLAG_FOCUS) {

                }
                break;
            case TYPE_SORT:
                if (flag == FLAG_NORMAL) {
                    flag = FLAG_DOWN;
                    itemIV.setImageResource(R.drawable.icon_sort_down);
                } else if (flag == FLAG_DOWN) {
                    flag = FLAG_UP;
                    itemIV.setImageResource(R.drawable.icon_sort_up);
                } else if (flag == FLAG_UP) {
                    flag = FLAG_DOWN;
                    itemIV.setImageResource(R.drawable.icon_sort_down);
                }
                break;
            case TYPE_DOWN:
                if (flag == FLAG_NORMAL) {
                    //选中色：红色
                    flag = FLAG_FOCUS;
                } else if (flag == FLAG_FOCUS) {

                }
                break;
        }

        itemLayout.setTag(index + "," + type + "," + flag);
        if (null != onTabClickListener) {
            onTabClickListener.onTabClick(index, flag);
        }


    }

    private void clearStatus(View view) {
        if (null == view || !(view instanceof LinearLayout)) {
            return;
        }
        LinearLayout itemLayout = (LinearLayout) view;
        TextView itemTV = (TextView) itemLayout.getChildAt(0);
        ImageView itemIV = (ImageView) itemLayout.getChildAt(1);
        if (null == itemTV || null == itemIV) {
            return;
        }

        String tag = (String) view.getTag();
        if (!tag.contains(",")) {
            return;
        }

        String[] tagArray = tag.split(",");
        if (null == tagArray || tagArray.length != 3) {
            return;
        }
        int index = StringUtils.valueOfInt(tagArray[0]);
        int type = StringUtils.valueOfInt(tagArray[1]);
        int flag = StringUtils.valueOfInt(tagArray[2]);

        itemLayout.setTag(index + "," + type + "," + FLAG_NORMAL);

        itemTV.setTextColor(0xFF888888);
        //还原其他textview的状态
        switch (type) {
            case TYPE_NORMAL:
                itemIV.setVisibility(View.GONE);
                break;
            case TYPE_SORT:
                itemIV.setImageResource(R.drawable.icon_sort_default);
                break;
            case TYPE_DOWN:
                itemIV.setImageResource(R.drawable.icon_sort_arrow);
                break;
        }

    }

    /**
     * tab点击回调
     */
    public interface OnTabClickListener {
        void onTabClick(int index, int flag);
    }

    private OnTabClickListener onTabClickListener;

    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        this.onTabClickListener = onTabClickListener;
    }
}
