package com.library.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

/**
 * 警告类视图
 *
 * @author jchen.tinkin
 */
public class UIAlertView implements DisposeListener {

    private AlertDialog.Builder _builder;
    //private String _title;
    //private String _message;
    private String _leftButtonTitle;
    private String _rightButtonTitle;
    public static final int LEFTBUTTON_WATCH = -1;
    public static final int RIGHTBUTTON_WATCH = -2;
    private boolean isCreate = false;
    Dialog dialog;

    public static final String POINT_OUT = "提示";
    public static final String CONFIRM = "确定";
    public static final String CANCEL = "取消";

    /**
     * 构造一个双按钮警告视图
     *
     * @param context          内容
     * @param title            标题信息
     * @param message          警告信息
     * @param leftButtonTitle  左按钮的标题
     * @param rightButtonTitle 右标题的数组
     */
    public UIAlertView(Context context, String title, String message,
                       String leftButtonTitle, String rightButtonTitle) {
        _leftButtonTitle = (leftButtonTitle == null) ? CONFIRM : leftButtonTitle;
        _rightButtonTitle = (rightButtonTitle == null) ? CANCEL : rightButtonTitle;
//		title = (title == null)?"":title;
//		message = (message == null)?"":message;
        if (context != null) {
            _builder = new AlertDialog.Builder(context);
            _builder.setTitle(title).setMessage(message).setPositiveButton(_leftButtonTitle, null).setNegativeButton(_rightButtonTitle, null);

        }
    }

    public void cancel() {
        if (dialog == null) {
            return;
        }
        dialog.dismiss();
    }

    /**
     * 构造一个单钮警告视图
     *
     * @param context         内容
     * @param title           标题信息
     * @param message         警告信息
     * @param leftButtonTitle 按钮的标题
     */
    public UIAlertView(Context context, String title, String message,
                       String leftButtonTitle) {
        _leftButtonTitle = ((leftButtonTitle == null) ? CONFIRM : leftButtonTitle);
        title = (title == null) ? "" : title;
        message = (message == null) ? "" : message;
        if (context != null) {
            _builder = new AlertDialog.Builder(context);
            _builder.setTitle(title).setMessage(message).setPositiveButton(_leftButtonTitle, null);
        }
    }

    /**
     * 构造空白警告视图
     *
     * @param context 内容
     */
    public UIAlertView(Context context) {
        if (context != null) {
            _builder = new AlertDialog.Builder(context);
        }
    }

    public void setTitle(String title) {
        title = (title == null) ? "" : title;
        if (_builder != null) {
            _builder.setTitle(title);
        }
    }

    public void setMessage(String message) {
        message = (message == null) ? "" : message;
        if (_builder != null) {
            _builder.setMessage(message);
        }
    }

    public void setCancel(boolean cancelable) {
        if (_builder != null) {
            _builder.setCancelable(cancelable);
        }
    }

    /**
     * 设置弹出框中显示的视图
     *
     * @param arg0 视图
     */
    public void setView(View arg0) {
        if (_builder != null && arg0 != null) {
            _builder.setView(arg0);
        }
    }

    /**
     * 设置左按钮警告点击监听
     *
     * @param leftButtonTitle 左按钮标题
     * @param leftListener    左按钮监听器
     */
    public void setLeftButton(String leftButtonTitle, OnClickListener leftListener) {
        _leftButtonTitle = (leftButtonTitle == null) ? CONFIRM : leftButtonTitle;
        if (leftListener != null) {
            _builder.setPositiveButton(_leftButtonTitle, leftListener);
        }
    }

    /**
     * 设置右按钮警告点击监听
     *
     * @param rightButtonTitle 右按钮标题
     * @param rightListener    右按钮监听器
     */
    public void setRightButton(String rightButtonTitle, OnClickListener rightListener) {
        _rightButtonTitle = (rightButtonTitle == null) ? CANCEL : rightButtonTitle;
        if (rightListener != null) {
            _builder.setNegativeButton(_rightButtonTitle, rightListener);
        }
    }

    /**
     * 请求显示警告视图
     */
    public void show() {
        if (_builder != null) {
            if (isCreate) {
                create();
            }
            if (dialog != null) {
                dialog.dismiss();
            }
//			if(dialog.getContext()==null||MainUIActivity.getMainController().isFinishing())
//				return;
            try {
                dialog = _builder.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void create() {
        if (_builder != null) {
            _builder.create();
            isCreate = true;
        }
    }

    /**
     * 设置单按钮警告点击监听
     *
     * @param leftListener 监听器
     */
    public void setOnClickListener(OnClickListener leftListener) {
        if (leftListener != null && _leftButtonTitle != null) {
            _builder.setPositiveButton(_leftButtonTitle, leftListener);
        }
    }

    /**
     * 设置点击手机返回键事件监听
     *
     * @param listener 监听器
     */
    public void setOnCancelByKeyListener(OnCancelListener listener) {
        if (listener != null) {
            _builder.setOnCancelListener(listener);
        }
    }

    /**
     * 设置双按钮警告点击监听
     *
     * @param leftListener  左按钮监听器
     * @param rightListener 右按钮监听器
     */
    public void setOnClickListener(OnClickListener leftListener, OnClickListener rightListener) {
        if (leftListener != null && _leftButtonTitle != null) {
            _builder.setPositiveButton(_leftButtonTitle, leftListener);
        }
        if (rightListener != null && _rightButtonTitle != null) {
            _builder.setNegativeButton(_rightButtonTitle, rightListener);
        }
    }

    public boolean isShowing() {
        return dialog != null ? dialog.isShowing() : false;
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        _builder = null;
        _leftButtonTitle = null;
        _rightButtonTitle = null;
    }
}
