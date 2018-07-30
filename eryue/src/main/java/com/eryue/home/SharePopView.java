package com.eryue.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.eryue.R;
import com.eryue.WXShare;
import com.eryue.widget.ShareContentView;
import com.library.util.UIScreen;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by bli.Jason on 2017/4/25.
 */
public class SharePopView{



    //popview
    private PopupWindow window;

    private Context context;

    private View contentView;


    public SharePopView(Context context) {
        this.context = context;

        initView();
    }



    WXShare wxShare;
    ShareContentView shareview;
    private void initView() {

        window = new PopupWindow(context);
        window.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        window.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(0x99000000));
        window.setFocusable(true);
        window.setTouchable(true);
        window.setOutsideTouchable(true);

        //sdk > 21 解决 标题栏没有办法遮罩的问题
        window.setClippingEnabled(false);
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.popwindow_anim_style);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (null!=onPopListener){
                    onPopListener.onDismiss();
                }
            }
        });

        //解决弹出框被状态栏挡住没有显示全屏问题
        boolean needFullScreen = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                mLayoutInScreen.setAccessible(true);
                mLayoutInScreen.set(window, needFullScreen);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

//        window.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (null!=gridview_goodstab){
//                    int height = gridview_goodstab.getBottom();
//                    int y = (int) event.getY();
//                    if(event.getAction()==MotionEvent.ACTION_UP){
//                        if(y>(height+StringUtils.dipToPx(10))){
//                            if (window != null && window.isShowing()) {
//                                window.dismiss();
//                                return true;
//                            }
//                        }
//                    }
//                }
//                return false;
//            }
//        });
        window.update();

        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.view_pop_share, null);
            //分享
            shareview = (ShareContentView) contentView.findViewById(R.id.shareview);


            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (window != null && window.isShowing()) {
                        window.dismiss();
                    }
                }
            });


        }

        window.setContentView(contentView);


    }

    //设置分享点击监听
    public void setOnShareClickListener(ShareContentView.OnShareClickListener onShareClickListener){
        shareview.setOnShareClickListener(onShareClickListener);

    }

    public void refreshPopViewHeight() {
        if (null != window) {
            window.setHeight(UIScreen.screenHeight -UIScreen.statusBarHeight);
            window.update();

        }
    }


    public void dimiss() {
        if (null != window) {
            window.dismiss();
        }
    }

    //显示popview
    public void showPopView() {
        if (null != window) {
            window.showAtLocation(((Activity)context).getWindow().getDecorView(), Gravity.NO_GRAVITY,0,0);
        }
    }



    private OnPopListener onPopListener;


    public void setOnPopListener(OnPopListener onPopListener) {
        this.onPopListener = onPopListener;
    }


    public interface OnPopListener{
        void onDismiss();

    }
}
