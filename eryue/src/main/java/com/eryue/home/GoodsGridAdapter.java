package com.eryue.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eryue.AccountUtil;
import com.eryue.ActivityHandler;
import com.eryue.GoodsContants;
import com.eryue.R;
import com.eryue.WXShare;
import com.eryue.activity.VideoActivity;
import com.eryue.goodsdetail.GoodsDetailActivityEx;
import com.eryue.widget.ShareContentView;
import com.library.util.CommonFunc;
import com.library.util.ImageUtils;
import com.library.util.StringUtils;
import com.library.util.ToastTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import net.InterfaceManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;

/**
 * @author
 * Created by bli.Jason on 2018/2/7.
 */

public class GoodsGridAdapter extends BaseAdapter implements ShareContentView.OnShareClickListener{

    private Context context;

    private List<InterfaceManager.SearchProductInfo> dataList;

    private static final int COUNT_PAGER = 2;

    private GoodsTabModel tabModel;

    //分享的商品
    private InterfaceManager.SearchProductInfo shareProductInfo;

    //分享
    SharePopView sharePopView;
    private WXShare wxShare;

    //分享左边按钮
    Drawable shareDrawable;

    DisplayImageOptions optionsSmall = new DisplayImageOptions.Builder()
            .cacheInMemory(true)   //设置图片不缓存于内存中
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)    //设置图片的质量
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)    //设置图片的缩放类型，该方法可以有效减少内存的占用
            .showImageOnLoading(R.drawable.img_default_contract)
            .showImageOnFail(R.drawable.img_default_contract)
            .considerExifParams(true).build();

    private long uid = StringUtils.valueOfLong(AccountUtil.getUID());
    private String serverURL = AccountUtil.getServerURL();

    public GoodsGridAdapter(Context context){
        this.context = context;
        sharePopView = new SharePopView(context);
        sharePopView.setOnShareClickListener(this);
        wxShare = new WXShare(context);

        shareDrawable = context.getResources().getDrawable(R.drawable.icon_shareapp);
        //width即为你需要设置的图片宽度，height即为你设置的图片的高度
        shareDrawable.setBounds(0,0, StringUtils.dipToPx(10),StringUtils.dipToPx(10));
    }

    public void setDataList(List<InterfaceManager.SearchProductInfo> dataList) {
        this.dataList = dataList;
    }

    public void clearData(){
        if (null!=this.dataList){
            this.dataList.clear();
        }
    }

    public void setTabModel(GoodsTabModel tabModel) {
        this.tabModel = tabModel;
    }

    @Override
    public int getCount() {
        if (null!=dataList){
            int size = dataList.size();
            int count;

            if (size%COUNT_PAGER!=0){
                count = size/COUNT_PAGER+1;
            }else{
                count = size/COUNT_PAGER;
            }
            return count;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (null == convertView){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_goods,null);
            viewHolder.layout_goods = (LinearLayout) convertView.findViewById(R.id.layout_goods);
            viewHolder.layout_img_goods = (RelativeLayout) convertView.findViewById(R.id.layout_img_goods);
            viewHolder.iv_goods = (ImageView) convertView.findViewById(R.id.iv_goods);
            viewHolder.iv_flag_video = (ImageView) convertView.findViewById(R.id.iv_flag_video);
            viewHolder.iv_tabao = (ImageView) convertView.findViewById(R.id.iv_tabao);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_paperprice = (TextView) convertView.findViewById(R.id.tv_paperprice);
            viewHolder.tv_price_paper = (TextView) convertView.findViewById(R.id.tv_price_paper);
            viewHolder.tv_sellnum = (TextView) convertView.findViewById(R.id.tv_sellnum);
            viewHolder.tv_share = (TextView) convertView.findViewById(R.id.tv_share);

            viewHolder.layout_goods2 = (LinearLayout) convertView.findViewById(R.id.layout_goods2);
            viewHolder.layout_img_goods2 = (RelativeLayout) convertView.findViewById(R.id.layout_img_goods2);
            viewHolder.iv_goods2 = (ImageView) convertView.findViewById(R.id.iv_goods2);
            viewHolder.iv_flag_video2 = (ImageView) convertView.findViewById(R.id.iv_flag_video2);
            viewHolder.iv_tabao2 = (ImageView) convertView.findViewById(R.id.iv_tabao2);
            viewHolder.tv_name2 = (TextView) convertView.findViewById(R.id.tv_name2);
            viewHolder.tv_paperprice2 = (TextView) convertView.findViewById(R.id.tv_paperprice2);
            viewHolder.tv_price_paper2 = (TextView) convertView.findViewById(R.id.tv_price_paper2);
            viewHolder.tv_sellnum2 = (TextView) convertView.findViewById(R.id.tv_sellnum2);
            viewHolder.tv_share2 = (TextView) convertView.findViewById(R.id.tv_share2);

            viewHolder.tv_share.setCompoundDrawables(shareDrawable,null,null,null);
            viewHolder.tv_share2.setCompoundDrawables(shareDrawable,null,null,null);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final InterfaceManager.SearchProductInfo searchProductInfo = dataList.get(position*COUNT_PAGER);
        //商品图片
        Glide.with(context).load(searchProductInfo.pictUrl).placeholder(R.drawable.img_default_contract).into(viewHolder.iv_goods);
//        ImageLoader.getInstance().displayImage(searchProductInfo.pictUrl,viewHolder.iv_goods,optionsSmall);
        if (searchProductInfo.isMall==1){
            //天猫
            Glide.with(context).load(R.drawable.tmall).asBitmap().into(viewHolder.iv_tabao);
        }else{
            //淘宝
            Glide.with(context).load(R.drawable.img_tb).asBitmap().into(viewHolder.iv_tabao);
        }
        //名称
        viewHolder.tv_name.setText(searchProductInfo.shortTitle);
        //券后
        viewHolder.tv_paperprice.setText("¥"+CommonFunc.fixText(searchProductInfo.afterQuan,2));
        //原价
        viewHolder.tv_price_paper.setText("¥"+CommonFunc.fixText(searchProductInfo.quanPrice,2));
        //销量
        viewHolder.tv_sellnum.setText(String.valueOf(searchProductInfo.soldQuantity));

        viewHolder.layout_img_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                turntoDetail(searchProductInfo);

            }
        });
        if (!TextUtils.isEmpty(searchProductInfo.video)){
            viewHolder.iv_flag_video.setVisibility(View.VISIBLE);
            viewHolder.layout_img_goods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(searchProductInfo.video)){
                        //视频购物，点击跳转到播放视频
                        Intent intent = new Intent(context,VideoActivity.class);
                        intent.putExtra("url",searchProductInfo.video);
                        context.startActivity(intent);
                        ((Activity)context).overridePendingTransition(R.anim.push_right_in,
                                R.anim.push_left_out);
                    }else{
                        turntoDetail(searchProductInfo);
                    }
                }
            });
        }else{
            viewHolder.iv_flag_video.setVisibility(View.GONE);
        }

        viewHolder.layout_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                turntoDetail(searchProductInfo);
            }
        });

        viewHolder.tv_share.setText("分享赚¥"+CommonFunc.fixText(searchProductInfo.jf,2));
        viewHolder.tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareProductInfo = searchProductInfo;
                if (null!=sharePopView){
                    sharePopView.showPopView();
                }
            }
        });


        if ((position*COUNT_PAGER+1) < dataList.size()){
            viewHolder.layout_goods2.setVisibility(View.VISIBLE);
            final InterfaceManager.SearchProductInfo searchProductInfo2 = dataList.get(position*COUNT_PAGER+1);
            //商品图片
            Glide.with(context).load(searchProductInfo2.pictUrl).placeholder(R.drawable.img_default_contract).into(viewHolder.iv_goods2);
//            ImageLoader.getInstance().displayImage(searchProductInfo2.pictUrl,viewHolder.iv_goods2,optionsSmall);
            if (searchProductInfo2.isMall==1){
                //天猫
                Glide.with(context).load(R.drawable.tmall).asBitmap().into(viewHolder.iv_tabao2);
            }else{
                //淘宝
                Glide.with(context).load(R.drawable.img_tb).asBitmap().into(viewHolder.iv_tabao2);
            }
            //名称
            viewHolder.tv_name2.setText(searchProductInfo2.shortTitle);
            //券后
            viewHolder.tv_paperprice2.setText("¥"+CommonFunc.fixText(searchProductInfo2.afterQuan,2));
            //优惠券价格
            viewHolder.tv_price_paper2.setText("¥"+CommonFunc.fixText(searchProductInfo2.quanPrice,2));
            //销量
            viewHolder.tv_sellnum2.setText(String.valueOf(searchProductInfo2.soldQuantity));


            viewHolder.layout_img_goods2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    turntoDetail(searchProductInfo2);
                }
            });


            viewHolder.tv_share2.setText("分享赚¥"+CommonFunc.fixText(searchProductInfo2.jf,2));
            viewHolder.tv_share2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareProductInfo = searchProductInfo2;
                    if (null!=sharePopView){
                        sharePopView.showPopView();
                    }
                }
            });


            if (!TextUtils.isEmpty(searchProductInfo2.video)){
                viewHolder.iv_flag_video2.setVisibility(View.VISIBLE);
                viewHolder.layout_img_goods2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(searchProductInfo2.video)){
                            //视频购物，点击跳转到播放视频
                            Intent intent = new Intent(context,VideoActivity.class);
                            intent.putExtra("url",searchProductInfo2.video);
                            context.startActivity(intent);
                            ((Activity)context).overridePendingTransition(R.anim.push_right_in,
                                    R.anim.push_left_out);
                        }else{
                            turntoDetail(searchProductInfo2);
                        }
                    }
                });

                viewHolder.layout_goods2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        turntoDetail(searchProductInfo2);
                    }
                });
            }else{
                viewHolder.iv_flag_video2.setVisibility(View.GONE);
            }
        }else{
            viewHolder.layout_goods2.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }


    @Override
    public void onShareClick(final int tag) {

        if (null!=wxShare){

            String url = serverURL+"appShare.do?itemId=%s&uid=%s";
            url = String.format(url,shareProductInfo.itemId,uid);
            ImageUtils.getInstance(context).setPicPath(GoodsContants.PATH_SHARE);
            ImageUtils.getInstance(context).setScaleSize(50);

            final String finalUrl = url;

            if (context instanceof BaseActivity){

                ((BaseActivity)context).showProgressMum();
            }
            ImageUtils.getInstance(context).setOnDownLoadListener(new ImageUtils.OnDownLoadListener() {
                @Override
                public void onDownLoadBack(final File file) {
                    if (context instanceof BaseActivity){
                        ((BaseActivity)context).hideProgressMum();
                    }
                    if (tag == ShareContentView.FLAG_WEIXIN||tag == ShareContentView.FLAG_WEIXIN_TIMELINE){
                        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {

                            @Override
                            public void handleMessage(Message msg) {
                                if (null!=sharePopView){
                                    sharePopView.dimiss();
                                }
                                //将图片的长和宽缩小味原来的1/2
                                Bitmap bitmap=BitmapFactory.decodeFile(file.getPath(),getBitmapOption(2));
                                wxShare.shareUrl(tag,context,bitmap,finalUrl,shareProductInfo.shortTitle,shareProductInfo.itemTitle);
                            }
                        }).sendEmptyMessage(0);



                    }else if (tag == ShareContentView.FLAG_QQ){

                    }else if (tag == ShareContentView.FLAG_QZONE){

                    }
                }

                @Override
                public void onDownLoadError() {
                    if (context instanceof BaseActivity){

                        ((BaseActivity)context).hideProgressMum();
                    }

                    ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
                        @Override
                        public void handleMessage(Message msg) {
                            ToastTools.showShort(context,"图片加载失败");
                        }
                    }).sendEmptyMessage(0);

                }
            });

            List<String> imgList = new ArrayList<>();
            imgList.add(shareProductInfo.pictUrl);
            ImageUtils.getInstance(context).download(imgList);




        }


    }


    private BitmapFactory.Options getBitmapOption(int inSampleSize){
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    public final class ViewHolder{
        private LinearLayout layout_goods;
        public RelativeLayout layout_img_goods;
        public ImageView iv_goods;
        public ImageView iv_flag_video;
        public ImageView iv_tabao;
        public TextView tv_name;
        public TextView tv_paperprice;
        public TextView tv_price_paper;
        public TextView tv_sellnum;
        public TextView tv_share;

        private LinearLayout layout_goods2;
        public RelativeLayout layout_img_goods2;
        public ImageView iv_goods2;
        public ImageView iv_flag_video2;
        public ImageView iv_tabao2;
        public TextView tv_name2;
        public TextView tv_paperprice2;
        public TextView tv_price_paper2;
        public TextView tv_sellnum2;
        public TextView tv_share2;

    }

    private void turntoDetail(InterfaceManager.SearchProductInfo searchProductInfo){
        Intent intent = new Intent(context, GoodsDetailActivityEx.class);
        String type = ((Activity)context).getIntent().getStringExtra("type");
        if (null!=tabModel){
            if (!TextUtils.isEmpty(tabModel.getType())){
                intent.putExtra("type",type);
            }else if (!TextUtils.isEmpty(tabModel.getName())){
                intent.putExtra("type",tabModel.getName());
            }
        }
        intent.putExtra("searchFlag",searchProductInfo.searchFlag);
        intent.putExtra("itemId",searchProductInfo.itemId);
        context.startActivity(intent);
    }
}
