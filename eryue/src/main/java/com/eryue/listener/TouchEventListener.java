package com.eryue.listener;

import android.view.MotionEvent;
import android.view.View;

/**
 * ****************************************************************
 * 文件名称	: TouchEventListener.java
 * 作    者	: Aofei.Wang
 * 创建时间	: 2012-2-2 上午10:59:30
 * 文件描述	: 事件响应接口
 * 修改历史	: 2012-2-2 1.00 初始版本
 * ****************************************************************
 */
public interface TouchEventListener {
    /**
     * 响应事件的接口方法
     *
     * @param sender    事件源
     * @param event UIEventArgs
     */
    public void touchEvent(View sender, MotionEvent event);
}
