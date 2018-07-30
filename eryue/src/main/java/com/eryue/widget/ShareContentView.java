package com.eryue.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eryue.R;


public class ShareContentView extends LinearLayout implements View.OnClickListener{
    private Context context;

    /**
     * 微信
     */
    public static final int FLAG_WEIXIN = 0;
    /**
     * 微信朋友圈
     */
    public static final int FLAG_WEIXIN_TIMELINE = 1;
    /**
     * QQ
     */
    public static final int FLAG_QQ = 2;
    /**
     * QQ空间
     */
    public static final int FLAG_QZONE = 3;


    /**
     * 微信
     */
    private LinearLayout share_weixin;
    /**
     * 微信朋友圈
     */
    private LinearLayout share_weixin_timeline;
    /**
     * QQ
     */
    private LinearLayout share_qq;
    /**
     * QQ空间
     */
    private LinearLayout share_qzone;

    private TextView tv_cancel;



    public ShareContentView(Context context) {
        super(context);
        initViews(context);
    }

    public ShareContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public ShareContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_share_content, this);

        tv_cancel = (TextView) findViewById(R.id.tv_cancel);

        share_weixin = (LinearLayout)findViewById(R.id.share_weixin);
        share_weixin_timeline = (LinearLayout)findViewById(R.id.share_weixin_timeline);
        share_qq = (LinearLayout)findViewById(R.id.share_qq);
        share_qzone = (LinearLayout)findViewById(R.id.share_qzone);

        share_weixin.setOnClickListener(this);
        share_weixin_timeline.setOnClickListener(this);
        share_qq.setOnClickListener(this);
        share_qzone.setOnClickListener(this);

    }

    /**
     * 设置取消按钮是否可见
     * @param visible
     */
    public void setCancelVisiblity(boolean visible){

        if (null!=tv_cancel){
            tv_cancel.setVisibility(visible?View.VISIBLE:View.GONE);
        }

    }



    @Override
    public void onClick(View v) {
        int tag = FLAG_WEIXIN;
        if (v == share_weixin) {
            tag = FLAG_WEIXIN;
        } else if (v == share_weixin_timeline) {
            tag = FLAG_WEIXIN_TIMELINE;
        }  else if (v == share_qq) {
            tag = FLAG_QQ;
        }  else if (v == share_qzone) {
            tag = FLAG_QZONE;
        }
        if (null!=onShareClickListener){
            onShareClickListener.onShareClick(tag);
        }
    }

    OnShareClickListener onShareClickListener;

    public void setOnShareClickListener(OnShareClickListener onShareClickListener) {
        this.onShareClickListener = onShareClickListener;
    }

    public interface OnShareClickListener{
        void onShareClick(int tag);
    }
}
