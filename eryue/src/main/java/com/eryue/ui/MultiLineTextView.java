package com.eryue.ui;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eryue.R;
import com.eryue.model.GoodsSearchModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多个textview自适应排版控件
 * Created by wnyin on 2016/12/15.
 */

public class MultiLineTextView extends RelativeLayout {

    private Context context;
    private float textSize;
    private int normalColor;
    private int selectedColor;
    private int textLineColor;
    private int wordMargin;
    private int lineMargin;
    private int textPaddingLeft;
    private int textPaddingRight;
    private int textPaddingTop;
    private int textPaddingBottom;
    private Drawable textBackground;

    private int layout_width;

    private OnMultipleTVItemClickListener listener;

    @SuppressWarnings("all")
    public MultiLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MultiLineTextView);
        int linePadding = array.getDimensionPixelSize(R.styleable.MultiLineTextView_linePadding, 0);
        textLineColor = array.getColor(R.styleable.MultiLineTextView_item_textLineColor, 0XFF62656A);

        selectedColor = R.color.common_normal_text_w;
        normalColor = R.color.common_lite_text_w;

        textSize = array.getDimension(R.styleable.MultiLineTextView_item_textSize, 24);
        textSize = px2sp(context, textSize);
        wordMargin = array.getDimensionPixelSize(R.styleable.MultiLineTextView_item_textWordMargin, 0);
        lineMargin = array.getDimensionPixelSize(R.styleable.MultiLineTextView_item_textLineMargin, 0);
        textBackground = array.getDrawable(R.styleable.MultiLineTextView_item_textBackground);
        textPaddingLeft = array.getDimensionPixelSize(R.styleable.MultiLineTextView_item_textPaddingLeft, 0);
        textPaddingRight = array.getDimensionPixelSize(R.styleable.MultiLineTextView_item_textPaddingRight, 0);
        textPaddingTop = array.getDimensionPixelSize(R.styleable.MultiLineTextView_item_textPaddingTop, 0);
        textPaddingBottom = array.getDimensionPixelSize(R.styleable.MultiLineTextView_item_textPaddingBottom, 0);
        array.recycle();

        DisplayMetrics dm = getResources().getDisplayMetrics();
        layout_width = dm.widthPixels;
        layout_width = layout_width - linePadding * 2;

    }

    public OnMultipleTVItemClickListener getOnMultipleTVItemClickListener() {
        return listener;
    }

    public void setOnMultipleTVItemClickListener(
            OnMultipleTVItemClickListener listener) {
        this.listener = listener;
    }

    public void setTextViews(List<GoodsSearchModel> dataList) {
        if (dataList == null || dataList.size() == 0) {
            return;
        }
        removeAllViews();
        // 每一行拉伸
        int line = 0;
        Map<Integer, List<FrameLayout>> lineMap = new HashMap<>();
        List<FrameLayout> lineList = new ArrayList<>();
        lineMap.put(0, lineList);

        int x = 0;
        int y = 0;

        for (int i = 0; i < dataList.size(); i++) {
            TextView tv = new TextView(context);
            tv.setMaxEms(10);
            tv.setMaxLines(1);
            tv.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
            tv.setText(dataList.get(i).getKeyword());
            tv.setSingleLine(true);
            tv.setTextSize(textSize);

            if (textBackground != null) {
                tv.setBackgroundDrawable(textBackground);
            } else {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE); // 画框
                drawable.setStroke(1, textLineColor); // 边框粗细及颜色
                drawable.setColor(0X00000000); // 边框内部颜色
                tv.setBackgroundDrawable(drawable);
            }

            tv.setPadding(textPaddingLeft, textPaddingTop, textPaddingRight, textPaddingBottom);
            tv.setTag(i);// 标记position
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (listener != null) {
                        listener.onMultipleTVItemClick(v, (Integer) v.getTag());
                    }
                }
            });

            int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            tv.measure(w, h);
            int tvh = tv.getMeasuredHeight();
            int tvw = getMeasuredWidth(tv);

            FrameLayout frameLayout = new FrameLayout(context);
            LayoutParams params = new LayoutParams(tvw, tvh);
            if (x + tvw > layout_width && lineMap.get(line).size() > 0) {
                x = 0;
                y = y + tvh + lineMargin;

                // 拉伸处理
                line++;
                lineMap.put(line, new ArrayList<FrameLayout>());
            }
            params.leftMargin = x;
            params.topMargin = y;
            x = x + tvw + wordMargin;
            frameLayout.setLayoutParams(params);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            tv.setLayoutParams(layoutParams);
            frameLayout.addView(tv);

            tv.setTextColor(normalColor);

            // 拉伸处理
            lineMap.get(line).add(frameLayout);

        }
        for (int i = 0; i <= line; i++) {
            for (int j = 0; j < lineMap.get(i).size(); j++) {
                FrameLayout tView2 = lineMap.get(i).get(j);
                addView(tView2);
            }
        }
    }

    /**
     * 清空所有选项
     */
    public void clearView(){
        removeAllViews();
    }

    public int getMeasuredWidth(View v) {
        // return v.getMeasuredWidth() + v.getPaddingLeft() + v.getPaddingRight();
        return v.getMeasuredWidth();
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public interface OnMultipleTVItemClickListener {
        void onMultipleTVItemClick(View view, int position);
    }
}

