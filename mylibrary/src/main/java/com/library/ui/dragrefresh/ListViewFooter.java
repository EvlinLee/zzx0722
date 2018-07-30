//CHECKSTYLE:OFF
package com.library.ui.dragrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.library.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * DragRefreshListView的footer
 *
 * @author ylrong
 */
public class ListViewFooter extends LinearLayout {

    public final static int STATE_NORMAL = 0; // 正常状态
    public final static int STATE_LOADING = 1; // 加载中
    public final static int STATE_HIDE = 2; // 隐藏
    public final static int STATE_RESULT = 3; // 信息结果展示 不可点击
    public final static int STATE_BUTTON = 5; // 信息，可以点击

    private View mProgressBar;
    private TextView mTextView;
    private int viewHeight = 0;

    public ListViewFooter(Context context) {
        super(context);
        initView(context);
    }

    public ListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.listview_footer, this);
        mProgressBar = findViewById(R.id.listview_footer_progressbar);
        mTextView = (TextView) findViewById(R.id.listview_footer_textview);
        setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WRAP_CONTENT));
        this.post(new Runnable() {
            @Override
            public void run() {
                viewHeight = getHeight();
            }
        });
    }

    public void setState(int state, String showBtn) {
        mProgressBar.setVisibility(View.GONE);
        mTextView.setVisibility(View.GONE);
        show();
        if (state == STATE_NORMAL || state == STATE_RESULT || state == STATE_BUTTON) {
            mTextView.setVisibility(View.VISIBLE);
            if (showBtn == null || "".equals(showBtn)) {
                if (state == STATE_NORMAL) {
                    mTextView.setText(R.string.listview_footer_normal);
                } else {
                    mTextView.setText(R.string.listview_footer_result);
                }
            } else {
                mTextView.setText(showBtn);
            }
        } else if (state == STATE_LOADING) {
            mTextView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            mTextView.setText(R.string.listview_loading);
        } else if (state == STATE_HIDE) {
            hide();
        }
    }

    public void setFootTextColor(int color) {
        mTextView.setTextColor(color);
    }

    public void setBottomMargin(int height) {
        if (height < 0)
            return;
//		AbsListView.LayoutParams lp = (AbsListView.LayoutParams) getLayoutParams();
//		lp.bottomMargin = height;
//		setLayoutParams(lp);
    }

    public int getBottomMargin() {
//		AbsListView.LayoutParams lp = (AbsListView.LayoutParams) getLayoutParams();
//		return lp.bottomMargin;
        return 0;
    }

    // /**
    // * normal status
    // */
    // public void normal() {
    // mHintView.setVisibility(View.VISIBLE);
    // mProgressBar.setVisibility(View.GONE);
    // }
    //
    //
    // /**
    // * loading status
    // */
    // public void loading() {
    // mHintView.setVisibility(View.GONE);
    // mProgressBar.setVisibility(View.VISIBLE);
    // }

    /**
     * hide footer when disable pull load more
     */
    private void hide() {
        AbsListView.LayoutParams lp = (AbsListView.LayoutParams) getLayoutParams();
        lp.height = 0;
        setLayoutParams(lp);
    }

    /**
     * show footer
     */
    private void show() {
        AbsListView.LayoutParams lp = (AbsListView.LayoutParams) getLayoutParams();
        lp.height = WRAP_CONTENT;
        setLayoutParams(lp);
    }

    public TextView getmTextView() {
        return mTextView;
    }

    public void setmTextView(TextView mTextView) {
        this.mTextView = mTextView;
    }


}
