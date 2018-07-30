package com.eryue.widget;

import java.util.List;

/**
 * Created by Administrator on 2018/7/28.
 */

public class NoScrollGridViewCallBack<T> {

    private static ItemCallBack mItemCallBack;

    public static void setmItemCallBack(ItemCallBack itemCallBack){

        mItemCallBack = itemCallBack;
    }

    public static void itemCall(){

        mItemCallBack.itemCall();
    }
}
