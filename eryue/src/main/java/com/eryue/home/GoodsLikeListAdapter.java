package com.eryue.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eryue.AccountUtil;
import com.eryue.ActivityHandler;
import com.eryue.GoodsContants;
import com.eryue.R;
import com.eryue.WXShare;
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
 * Created by bli.Jason on 2018/2/7.
 * 首页热门推荐
 */

public class GoodsLikeListAdapter extends BaseAdapter implements ShareContentView.OnShareClickListener {

    private Context context;

    private List<InterfaceManager.SearchProductInfoEx> dataList;

    private static final int COUNT_PAGER = 2;

    private GoodsTabModel tabModel;

    //分享的商品
    private InterfaceManager.SearchProductInfoEx shareProductInfo;

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

    private String serverURL = AccountUtil.getServerURL();
    private long uid = StringUtils.valueOfLong(AccountUtil.getUID());

    public GoodsLikeListAdapter(Context context) {
        this.context = context;
        sharePopView = new SharePopView(context);
        sharePopView.setOnShareClickListener(this);
        wxShare = new WXShare(context);

        shareDrawable = context.getResources().getDrawable(R.drawable.icon_shareapp);
        //width即为你需要设置的图片宽度，height即为你设置的图片的高度
        shareDrawable.setBounds(0, 0, StringUtils.dipToPx(10), StringUtils.dipToPx(10));
    }

    public void setDataList(List<InterfaceManager.SearchProductInfoEx> dataList) {
        this.dataList = dataList;
    }

    public void clearData() {
        if (null != this.dataList) {
            this.dataList.clear();
        }
    }

    public void setTabModel(GoodsTabModel tabModel) {
        this.tabModel = tabModel;
    }

    @Override
    public int getCount() {
        if (null != dataList) {
            int size = dataList.size();
            int count;

            if (size % COUNT_PAGER != 0) {
                count = size / COUNT_PAGER + 1;
            } else {
                count = size / COUNT_PAGER;
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
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_goodslist, null);
            viewHolder.iv_status = (ImageView) convertView.findViewById(R.id.iv_status);
            viewHolder.iv_goods = (ImageView) convertView.findViewById(R.id.iv_goods);

            viewHolder.iv_business = (ImageView) convertView.findViewById(R.id.iv_business);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);

            viewHolder.tv_price_paper = (TextView) convertView.findViewById(R.id.tv_price_paper);
            viewHolder.tv_share = (TextView) convertView.findViewById(R.id.tv_share);
            viewHolder.iv_share = (TextView) convertView.findViewById(R.id.iv_share);

            viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            viewHolder.tv_originalprice = (TextView) convertView.findViewById(R.id.tv_originalprice);
            viewHolder.tv_sellnum = (TextView) convertView.findViewById(R.id.tv_sellnum);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final InterfaceManager.SearchProductInfoEx searchProductInfo = dataList.get(position * COUNT_PAGER);
        //商品状态
        Glide.with(context).load(GoodsUtil.getGoodsStatusRid(false,searchProductInfo.couponStatus)).asBitmap().into(viewHolder.iv_status);
        //商品图片
        Glide.with(context).load(searchProductInfo.pictUrl).placeholder(R.drawable.img_default_contract).into(viewHolder.iv_goods);

        //商品渠道
        Glide.with(context).load(GoodsUtil.getGoodsBusinessRid((int) searchProductInfo.isMall,searchProductInfo.productType)).asBitmap().into(viewHolder.iv_business);
        //名称
        viewHolder.tv_name.setText(searchProductInfo.shortTitle);

        //优惠券价格
        viewHolder.tv_price_paper.setText("¥" + CommonFunc.fixText(searchProductInfo.quanPrice, 2));
        //分享后可赚
        viewHolder.tv_share.setText("可赚¥" + CommonFunc.fixText(searchProductInfo.jf, 2));
        //分享按钮
        viewHolder.iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareProductInfo = searchProductInfo;
                if (null != sharePopView) {
                    sharePopView.showPopView();
                }
            }
        });

        //券后价格
        viewHolder.tv_price.setText("¥"+CommonFunc.fixText(searchProductInfo.afterQuan,2));
        //原价
        viewHolder.tv_originalprice.setText("¥"+CommonFunc.fixText(searchProductInfo.discountPrice,2));
        //原价-中间横线
        viewHolder.tv_originalprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        //销量
        viewHolder.tv_sellnum.setText("销量"+String.valueOf(searchProductInfo.soldQuantity));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turntoDetail(searchProductInfo);
            }
        });

        return convertView;
    }


    @Override
    public void onShareClick(final int tag) {

        if (null != wxShare) {

            String url = serverURL + "appShare.do?itemId=%s&uid=%s";
            url = String.format(url, shareProductInfo.itemId, uid);
            ImageUtils.getInstance(context).setPicPath(GoodsContants.PATH_SHARE);
            ImageUtils.getInstance(context).setScaleSize(50);

            final String finalUrl = url;

            if (context instanceof BaseActivity) {

                ((BaseActivity) context).showProgressMum();
            }
            ImageUtils.getInstance(context).setOnDownLoadListener(new ImageUtils.OnDownLoadListener() {
                @Override
                public void onDownLoadBack(final File file) {
                    if (context instanceof BaseActivity) {
                        ((BaseActivity) context).hideProgressMum();
                    }
                    if (tag == ShareContentView.FLAG_WEIXIN || tag == ShareContentView.FLAG_WEIXIN_TIMELINE) {
                        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {

                            @Override
                            public void handleMessage(Message msg) {
                                if (null != sharePopView) {
                                    sharePopView.dimiss();
                                }
                                //将图片的长和宽缩小味原来的1/2
                                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), getBitmapOption(2));
                                wxShare.shareUrl(tag, context, bitmap, finalUrl, shareProductInfo.shortTitle, shareProductInfo.itemTitle);
                            }
                        }).sendEmptyMessage(0);


                    } else if (tag == ShareContentView.FLAG_QQ) {

                    } else if (tag == ShareContentView.FLAG_QZONE) {

                    }
                }

                @Override
                public void onDownLoadError() {
                    if (context instanceof BaseActivity) {

                        ((BaseActivity) context).hideProgressMum();
                    }

                    ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
                        @Override
                        public void handleMessage(Message msg) {
                            ToastTools.showShort(context, "图片加载失败");
                        }
                    }).sendEmptyMessage(0);

                }
            });

            List<String> imgList = new ArrayList<>();
            imgList.add(shareProductInfo.pictUrl);
            ImageUtils.getInstance(context).download(imgList);


        }


    }


    private BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    public final class ViewHolder {

        public ImageView iv_status;
        public ImageView iv_goods;

        public ImageView iv_business;
        public TextView tv_name;

        public TextView tv_price_paper;
        public TextView tv_share;
        public TextView iv_share;

        public TextView tv_price;
        public TextView tv_originalprice;
        public TextView tv_sellnum;

    }




    private void turntoDetail(InterfaceManager.SearchProductInfoEx searchProductInfo) {
        Intent intent = new Intent(context, GoodsDetailActivityEx.class);
        String type = ((Activity) context).getIntent().getStringExtra("type");
        if (null != tabModel) {
            if (!TextUtils.isEmpty(tabModel.getType())) {
                intent.putExtra("type", type);
            } else if (!TextUtils.isEmpty(tabModel.getName())) {
                intent.putExtra("type", tabModel.getName());
            }
        }
        intent.putExtra("searchFlag", searchProductInfo.searchFlag);
        intent.putExtra("itemId", searchProductInfo.itemId);
        intent.putExtra("productType", searchProductInfo.productType);
        context.startActivity(intent);
    }
}
