package com.eryue.live;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.eryue.R;
import com.eryue.home.GoodsTabModel;
import com.eryue.home.GoodsTabPopView;
import com.library.util.UIScreen;

import java.util.List;

/**
 * 渠道下拉菜单选择
 * Created by bli.Jason on 2017/4/25.
 */
public class GoodsBusinessPopView implements AdapterView.OnItemClickListener{


    private ListView listview_business;
    private GoodsBusinessPopAdapter adapter;


    //popview
    private PopupWindow window;

    private Context context;

    private View contentView;


    public GoodsBusinessPopView(Context context) {
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
            contentView = LayoutInflater.from(context).inflate(R.layout.popview_business, null);

            listview_business = (ListView) contentView.findViewById(R.id.listview_business);
            adapter = new GoodsBusinessPopAdapter(context);
            listview_business.setAdapter(adapter);
            listview_business.setOnItemClickListener(this);

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
        if (null!=listview_business){
            listview_business.setOnItemClickListener(onItemClickListener);
        }

    }

    public void refreshPopViewHeight() {
        if (null != window) {
            window.setHeight(UIScreen.screenHeight -UIScreen.statusBarHeight);
            window.update();

        }
    }


    List<GoodsBusinessModel> dataList;
    //刷新列表数据
    public void refreshListData(List<GoodsBusinessModel> dataList) {

        this.dataList = dataList;
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        if (null == dataList||dataList.isEmpty()){
            return;
        }

        GoodsBusinessModel goodsBusinessModel;

        for (int i=0;i<dataList.size();i++){
            goodsBusinessModel = dataList.get(i);
            if (i == position){
                goodsBusinessModel.setSelectTag(GoodsBusinessModel.TAG_SELECT);
            }else{
                goodsBusinessModel.setSelectTag(GoodsBusinessModel.TAG_UNSELECT);
            }

        }

        goodsBusinessModel = dataList.get(position);

        if (null!=onBusinessPopListener){
            onBusinessPopListener.onBusinessClick(goodsBusinessModel);
        }


        GoodsBusinessPopAdapter.GoodsTabPopViewHolder viewHolder = (GoodsBusinessPopAdapter.GoodsTabPopViewHolder) view.getTag();
        if (null!=goodsBusinessModel){
            viewHolder.tv_name.setText(goodsBusinessModel.getName());
            if (goodsBusinessModel.getSelectTag() == GoodsTabModel.TAG_SELECT){
                viewHolder.tv_name.setTextColor(context.getResources().getColor(R.color.red_lite));
                viewHolder.iv_choose.setVisibility(View.VISIBLE);
            }else{
                viewHolder.tv_name.setTextColor(context.getResources().getColor(R.color.txtcolor_business));
                viewHolder.iv_choose.setVisibility(View.GONE);
            }
        }
        if (null!=window){
            window.dismiss();
        }


    }

    private OnBusinessPopListener onBusinessPopListener;


    public void setOnPopListener(OnBusinessPopListener onPopListener) {
        this.onBusinessPopListener = onPopListener;
    }

    public interface OnBusinessPopListener{
        void onBusinessClick(GoodsBusinessModel businessModel);

    }



}
