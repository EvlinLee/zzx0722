package com.eryue.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eryue.R;

import java.util.List;
/**
 * @author
 * Created by enjoy on 2018/7/5.
 */

public class ImageGridView extends LinearLayout implements View.OnClickListener,AdapterView.OnItemClickListener{
    //已选数量
    private TextView tv_selectnum;

    private TextView tv_allselect;

    private GridView grid_image;

    private ImageGridAdapter gridAdapter;

    private List<ImageModel> dataList;

    private int datalistSize = 0;

    private int totalChoose = 0;

    private static int GRIDVIEW_MAX_COUNT = 9;


    public ImageGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initCallBack();

    }

    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_image_grid,this);
        grid_image = findViewById(R.id.grid_image);
        tv_selectnum = (TextView) findViewById(R.id.tv_selectnum);
        tv_allselect = findViewById(R.id.tv_allselect);
        tv_allselect.setOnClickListener(this);
        gridAdapter = new ImageGridAdapter(context);
        grid_image.setAdapter(gridAdapter);

        grid_image.setOnItemClickListener(this);
    }

    public void setData(List<ImageModel> dataList){

        if (null!=gridAdapter){
            this.dataList = dataList;
            if (dataList.size() > GRIDVIEW_MAX_COUNT) {
                datalistSize = 9;
            } else {
                datalistSize = dataList.size();
            }
            gridAdapter.setDataList(dataList);
            gridAdapter.notifyDataSetChanged();
        }

    }


    @Override
    public void onClick(View view) {
        if (view == tv_allselect&&null!=dataList&&!dataList.isEmpty()) {
            if (totalChoose == datalistSize) {
                //取消全选
                selectAllImageView(false);
                tv_allselect.setText("全选");
            } else {
                //全选
                selectAllImageView(true);
                tv_allselect.setText("全不选");
            }

            if (null != tv_selectnum) {
                tv_selectnum.setText(String.valueOf(totalChoose));
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//        changeSelected(position);
    }

    public void changeSelected(int position){
        if (null == dataList||position<0||position>=dataList.size()){
            return;
        }
        ImageModel imageModel = dataList.get(position);
        imageModel.isChecked = !imageModel.isChecked;
        if (null!=gridAdapter){
            gridAdapter.notifyDataSetChanged();
        }

        totalChoose = 0;

        for (int i=0;i<dataList.size();i++){
            imageModel = dataList.get(i);
            if (imageModel.isChecked){
                totalChoose++;
            }
        }

        if (totalChoose == dataList.size()) {
            //取消全选
            tv_allselect.setText("全不选");
        } else {
            //全选
            tv_allselect.setText("全选");
        }

        if (null != tv_selectnum) {
            tv_selectnum.setText(String.valueOf(totalChoose));
        }
    }

    public void selectAllImageView(boolean isSelect){
        if (null ==dataList||dataList.isEmpty()){
            return;
        }
        totalChoose = 0;
        ImageModel imageModel;
        for (int i=0;i<dataList.size();i++){
            imageModel = dataList.get(i);
            imageModel.isChecked = isSelect;
            if (isSelect){
                totalChoose++;
            }
        }
        if (null!=gridAdapter){
            gridAdapter.notifyDataSetChanged();
        }
    }
    private void initCallBack() {
        NoScrollGridViewCallBack.setmItemCallBack(new ItemCallBack() {
            @Override
            public void itemCall() {
                ImageModel imageModel;
                totalChoose = 0;
                for (int i=0;i<datalistSize;i++){
                    imageModel = dataList.get(i);
                    if (imageModel.isChecked){
                        totalChoose++;
                    }
                }
                if (totalChoose == datalistSize) {
                    //取消全选
                    tv_allselect.setText("全不选");
                } else {
                    //全选
                    tv_allselect.setText("全选");
                }

                if (null != tv_selectnum) {
                    tv_selectnum.setText(String.valueOf(totalChoose));
                }
            }
        });
    }

    public static class ImageModel {
        public boolean isChecked;
        public String url;
    }

}
