package com.eryue.home;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.eryue.R;
import com.library.util.UIScreen;

import java.util.List;

/**
 * Created by bli.Jason on 2017/4/25.
 */
public class GoodsTabPopView{


    private GridView gridview_goodstab;
    private GoodsTabPopAdapter adapter;


    //popview
    private PopupWindow window;

    private Context context;

    private View contentView;


    public GoodsTabPopView(Context context) {
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
        window.setBackgroundDrawable(new ColorDrawable(0x99000000));
        window.setFocusable(true);
        window.setTouchable(true);
        window.setOutsideTouchable(true);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (null!=onPopListener){
                    onPopListener.onDismiss();
                }
            }
        });
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
            contentView = LayoutInflater.from(context).inflate(R.layout.view_pop_goodstab, null);

            gridview_goodstab = (GridView) contentView.findViewById(R.id.gridview_goodstab);
            adapter = new GoodsTabPopAdapter(context);
            gridview_goodstab.setAdapter(adapter);

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

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener){
        if (null!=gridview_goodstab){
            gridview_goodstab.setOnItemClickListener(onItemClickListener);
        }

    }

    public void refreshPopViewHeight() {
        if (null != window) {
            window.setHeight(UIScreen.screenHeight -UIScreen.statusBarHeight);
            window.update();

        }
    }


    //刷新列表数据
    public void refreshListData(List<GoodsTabModel> dataList) {

        if (null != adapter) {
            adapter.setTabList(dataList);
            adapter.notifyDataSetChanged();

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
            window.showAsDropDown(view);
        }
        if (null!=dialog){
            dialog.show();
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
