package com.eryue.mine;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eryue.AccountUtil;
import com.eryue.ActivityHandler;
import com.eryue.GoodsContants;
import com.eryue.R;
import com.eryue.WXShare;
import com.eryue.activity.CreateShareActivityEx;
import com.eryue.goodsdetail.GoodsDetailActivityEx;
import com.eryue.home.GoodsUtil;
import com.eryue.home.SharePopView;
import com.eryue.widget.GlideRoundTransform;
import com.eryue.widget.ShareContentView;
import com.library.util.CommonFunc;
import com.library.util.ImageUtils;
import com.library.util.StringUtils;
import com.library.util.ToastTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import net.DataCenterManager;
import net.InterfaceManager;

import java.util.List;


/**
 * @author zxc
 * 我的收藏 listview Adapter
 * Created by Administrator on 2018/8/13.
 */

public class MyFavoriteListAdapter extends BaseAdapter implements ShareContentView.OnShareClickListener {

    private Context context;

    private List<InterfaceManager.SearchProductInfoEx> dataList;

    private static final int COUNT_PAGER = 2;


    private boolean isSelected = false;

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

    private boolean isLogin;

    private String productType = "tb";

    //区分是否是京东免单
    private String jdType;


    public MyFavoriteListAdapter(Context context) {
        this.context = context;
        sharePopView = new SharePopView(context);
        sharePopView.setOnShareClickListener(this);
        wxShare = new WXShare(context);

        shareDrawable = context.getResources().getDrawable(R.drawable.icon_shareapp);
        //width即为你需要设置的图片宽度，height即为你设置的图片的高度
        shareDrawable.setBounds(0, 0, StringUtils.dipToPx(10), StringUtils.dipToPx(10));

        isLogin = AccountUtil.isLogin();
    }
    public void setAllSelect(boolean select){
        this.isSelected = select;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public void setDataList(List<InterfaceManager.SearchProductInfoEx> dataList) {
        this.dataList = dataList;
    }

    public void clearData() {
        if (null != this.dataList) {
            this.dataList.clear();
        }
    }

    public void setJdType(String jdType) {
        this.jdType = jdType;
    }

    @Override
    public int getCount() {
        if (null != dataList) {
            return dataList.size();
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

        final ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.favorite_list_item, null);
            viewHolder.iv_status = (ImageView) convertView.findViewById(R.id.iv_status);
            viewHolder.iv_goods = (ImageView) convertView.findViewById(R.id.iv_goods);

            viewHolder.iv_business = (TextView) convertView.findViewById(R.id.iv_business);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);

            viewHolder.tv_price_paper = (TextView) convertView.findViewById(R.id.tv_price_paper);
            viewHolder.tv_share = (TextView) convertView.findViewById(R.id.tv_share);
            viewHolder.layout_share = convertView.findViewById(R.id.layout_share);
            viewHolder.iv_share = (ImageView) convertView.findViewById(R.id.iv_share);

            viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            viewHolder.tv_originalprice = (TextView) convertView.findViewById(R.id.tv_originalprice);
            viewHolder.tv_sellnum = (TextView) convertView.findViewById(R.id.tv_sellnum);

            viewHolder.layout_goods = (LinearLayout) convertView.findViewById(R.id.layout_goods);

            viewHolder.layout_select = (LinearLayout) convertView.findViewById(R.id.layout_select);

            viewHolder.selectItem = (TextView) convertView.findViewById(R.id.select_item);

            viewHolder.selectItem.setSelected(isSelected);

            //不设置按钮的点击事件，按钮将无法点击
            viewHolder.selectItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewHolder.selectItem.isSelected()) {
                        viewHolder.selectItem.setSelected(false);
                    } else {
                        viewHolder.selectItem.setSelected(true);
                    }
                }
            });
            viewHolder.layout_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewHolder.selectItem.isSelected()) {
                        viewHolder.selectItem.setSelected(false);
                    } else {
                        viewHolder.selectItem.setSelected(true);
                    }
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final InterfaceManager.SearchProductInfoEx searchProductInfo = dataList.get(position);
        //商品状态
        if (searchProductInfo.couponStatus == -1){
            viewHolder.iv_status.setVisibility(View.GONE);
        }else{
            viewHolder.iv_status.setVisibility(View.VISIBLE);
            Glide.with(context).load(GoodsUtil.getGoodsStatusRid(false,searchProductInfo.couponStatus)).asBitmap().into(viewHolder.iv_status);
        }

        //商品图片
        //设置图片圆角角度
        Glide.with(context).load(searchProductInfo.pictUrl).transform(new GlideRoundTransform(context,10))
                .into(viewHolder.iv_goods);

        String goodsImgId = GoodsUtil.getGoodsBusinessRid((int) searchProductInfo.isMall,searchProductInfo.productType);

        //商品渠道
        if (goodsImgId == null){
            viewHolder.iv_business.setVisibility(View.GONE);
        }else{
            viewHolder.iv_business.setVisibility(View.VISIBLE);
            viewHolder.iv_business.setText(GoodsUtil.getGoodsBusinessRid((int) searchProductInfo.isMall,searchProductInfo.productType).toString());
        }

        //名称
        if(!TextUtils.isEmpty(searchProductInfo.itemTitle)){
            viewHolder.tv_name.setText(searchProductInfo.itemTitle);
        }else{
            viewHolder.tv_name.setText(searchProductInfo.shortTitle);
        }

        //优惠券价格
        viewHolder.tv_price_paper.setText("券¥" +CommonFunc.fixText(searchProductInfo.quanPrice, 0));

        if (isLogin){
            viewHolder.tv_share.setVisibility(View.VISIBLE);
            //分享后可赚
            viewHolder.tv_share.setText("赚¥" + CommonFunc.fixText(searchProductInfo.jf, 2));
        }else{
            viewHolder.tv_share.setVisibility(View.GONE);
        }
        //分享按钮
        viewHolder.layout_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果没有登录就进入到登录页
                //点击消息、每日发圈、列表分享、详情分享、立即购买、联系客服,需要登录
                if (!AccountUtil.checkLogin(context)){
                    return;
                }

                Intent intent = new Intent();
                intent.setClass(context,CreateShareActivityEx.class);
                intent.putExtra("jdtype", jdType);
                intent.putExtra("searchFlag", searchProductInfo.searchFlag);
                intent.putExtra("itemId", searchProductInfo.itemId);
                if (!TextUtils.isEmpty(searchProductInfo.productType)){
                    intent.putExtra("productType", searchProductInfo.productType);
                }else{
                    intent.putExtra("productType", "tb");
                }
                context.startActivity(intent);
//                if (null != sharePopView) {
//                    sharePopView.showPopView();
//                }
            }
        });

        //券后价格
        viewHolder.tv_price.setText("¥"+CommonFunc.fixText(searchProductInfo.afterQuan,2));
        //原价
        viewHolder.tv_originalprice.setText("¥"+CommonFunc.fixText(searchProductInfo.discountPrice,2));
        //原价-中间横线
        viewHolder.tv_originalprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        //销量
        viewHolder.tv_sellnum.setText("已售 "+String.valueOf(searchProductInfo.soldQuantity));

        //点击进入详情页
        viewHolder.layout_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turntoDetail(searchProductInfo);
            }
        });

        return convertView;
    }


    @Override
    public void onShareClick(final int tag) {


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

        public TextView iv_business;
        public TextView tv_name;

        public TextView tv_price_paper;
        public TextView tv_share;
        public RelativeLayout layout_share;
        public ImageView iv_share;

        public TextView tv_price;
        public TextView tv_originalprice;
        public TextView tv_sellnum;

        /**
         *        商品框
         */
        private LinearLayout layout_goods;

        /**
         * 选择框
         */
        private LinearLayout layout_select;

        /**
         * 选择按钮
         */
        private TextView selectItem;

    }

    private void turntoDetail(InterfaceManager.SearchProductInfoEx searchProductInfo) {
        Intent intent = new Intent(context, GoodsDetailActivityEx.class);
        String type = ((Activity) context).getIntent().getStringExtra("type");

        intent.putExtra("jdtype", jdType);
        intent.putExtra("searchFlag", searchProductInfo.searchFlag);
        intent.putExtra("itemId", searchProductInfo.itemId);
        if (!TextUtils.isEmpty(searchProductInfo.productType)){
            intent.putExtra("productType", searchProductInfo.productType);
        }else{
            intent.putExtra("productType", "tb");
        }
        intent.putExtra("couponStatus", searchProductInfo.couponStatus);
        context.startActivity(intent);

        DataCenterManager.Instance().saveProductInfo(searchProductInfo);
    }
}
