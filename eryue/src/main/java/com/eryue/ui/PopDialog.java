package com.eryue.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eryue.R;

/**
 * @author
 * Created by zxc on 2018/8/8.
 */

public class PopDialog extends Dialog {

    /**
     * 文案编辑框
     */
    private EditText search_text_from_clipboard;
    private TextView search_taobao,search_pingduoduo,search_jingdong;
    private ImageView close_dialog;

    /**
     *     定义被点击接口
     */
    private OnCloseclickListener onCloseOnclickListener;
    private OnSearchclickListener onSearchclickListener;


    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param onNoOnclickListener
     */
    public void setOnCloseOnclickListener(OnCloseclickListener onNoOnclickListener) {
        this.onCloseOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param onSearchclickListener
     */
    public void setOnSearchclickListener(OnSearchclickListener onSearchclickListener) {
        this.onSearchclickListener = onSearchclickListener;
    }

    public PopDialog(Context context) {
        super(context, R.style.custom_dialog_style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        search_taobao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchclickListener != null) {
                    onSearchclickListener.onTaoBaoSearchClick();
                }
            }
        });
        search_pingduoduo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchclickListener != null) {
                    onSearchclickListener.onPingduoduoSearchClick();
                }
            }
        });
        search_jingdong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchclickListener != null) {
                    onSearchclickListener.onJingDongSearchClick();
                }
            }
        });

        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCloseOnclickListener != null) {
                    onCloseOnclickListener.onCloseClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        search_taobao = (TextView) findViewById(R.id.search_taobao);
        search_pingduoduo = (TextView) findViewById(R.id.search_pingduoduo);
        search_jingdong = (TextView) findViewById(R.id.search_jingdong);
        close_dialog =(ImageView)findViewById(R.id.btn_close_dialog);
    }


    /**
     * 设置按钮被点击的接口
     */
    public interface OnCloseclickListener {
        public void onCloseClick();
    }

    public interface OnSearchclickListener {
        public void onTaoBaoSearchClick();
        public void onJingDongSearchClick();
        public void onPingduoduoSearchClick();
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height= ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }
}
