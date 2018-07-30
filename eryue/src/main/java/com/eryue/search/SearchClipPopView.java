package com.eryue.search;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.eryue.GoodsContants;
import com.eryue.R;
import com.eryue.util.SharedPreferencesUtil;
import com.library.util.UIScreen;

/**
 * 搜索界面剪贴板弹出框
 * Created by bli.Jason on 2017/4/25.
 */
public class SearchClipPopView implements View.OnClickListener{


    //popview
    private PopupWindow window;

    private Context context;

    private View contentView;

    //红包关闭按钮
    private ImageView iv_clipcolose;
    //剪贴板内容
    private TextView tv_clipcontent;
    //剪贴板搜索按钮
    private TextView tv_clipsearch;


    public SearchClipPopView(Context context) {
        this.context = context;

        initView();
    }

    Dialog dialog;

//    private void initView() {
//
//        if (null==dialog){
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            contentView = View.inflate(context, R.layout.view_pop_goodstab, null);
//            dialog = builder.setView(contentView).create();
//            //设置对话框布局
//            android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
//            p.height = (int) (UIScreen.screenHeight * 0.3);   //高度设置为屏幕的0.3
//            p.width = (int) (UIScreen.screenWidth * 0.5);    //宽度设置为屏幕的0.5
//            dialog.getWindow().setAttributes(p);     //设置生效
////            dialog.getWindow().setLayout(StringUtils.dipToPx(300), LinearLayout.LayoutParams.WRAP_CONTENT);
//            dialog.getWindow().setGravity(Gravity.TOP);
//
//            gridview_goodstab = (GridView) contentView.findViewById(R.id.gridview_goodstab);
//            adapter = new GoodsTabPopAdapter(context);
//            gridview_goodstab.setAdapter(adapter);
//
//            gridview_goodstab.setOnItemClickListener(this);
//        }
//
//    }

    private void initView() {

        window = new PopupWindow(context);
        window.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        window.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(0x7F000000));
        window.setFocusable(true);
        window.setTouchable(true);
        window.setOutsideTouchable(true);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        window.update();

        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.popview_clip, null);
            iv_clipcolose = contentView.findViewById(R.id.iv_clipcolose);
            tv_clipcontent = contentView.findViewById(R.id.tv_clipcontent);
            tv_clipsearch = contentView.findViewById(R.id.tv_clipsearch);

            iv_clipcolose.setOnClickListener(this);
            tv_clipsearch.setOnClickListener(this);

            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (window != null && window.isShowing()) {
//                        window.dismiss();
//                    }
                }
            });


        }


        window.setContentView(contentView);


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
        if (null!=dialog){
            dialog.dismiss();
        }
    }

    //显示popview
    public void showPopView(View view) {
        if (null != window) {
            window.showAtLocation(view, Gravity.CENTER,0,0);
        }
        if (null!=dialog){
            dialog.show();
        }
    }



    private String clipContent;

    public void showClipContent(String clipContent){
        if (null!=tv_clipcontent&& !TextUtils.isEmpty(clipContent)){
            this.clipContent = clipContent;
            tv_clipcontent.setText(clipContent);
        }
    }


    @Override
    public void onClick(View view) {
        if(view == iv_clipcolose){
            //关闭
            if (window != null && window.isShowing()) {
                window.dismiss();
            }
            if (!TextUtils.isEmpty(clipContent)){
                SharedPreferencesUtil.getInstance().saveString(GoodsContants.CLIP_CONTENT,clipContent);
            }
        }else if (view == tv_clipsearch){
            //立即搜索
            if (window != null && window.isShowing()) {
                window.dismiss();
            }
            if (!TextUtils.isEmpty(clipContent)){
                SharedPreferencesUtil.getInstance().saveString(GoodsContants.CLIP_CONTENT,clipContent);
            }
            if(null!=onClipClickListener){
                onClipClickListener.onClipClick(clipContent);
            }
        }

    }


    private onClipClickListener onClipClickListener;

    public void setOnClipClickListener(SearchClipPopView.onClipClickListener onClipClickListener) {
        this.onClipClickListener = onClipClickListener;
    }

    public interface onClipClickListener{

        void onClipClick(String content);

    }
}
