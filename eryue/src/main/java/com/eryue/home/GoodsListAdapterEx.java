package com.eryue.home;

/**
 * Created by Administrator on 2018/8/9.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eryue.AccountUtil;
import com.eryue.R;
import com.eryue.goodsdetail.GoodsDetailActivityEx;
import com.eryue.widget.GlideRoundTransform;
import com.eryue.widget.ShareContentView;
import com.library.util.CommonFunc;
import com.library.util.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import net.DataCenterManager;
import net.InterfaceManager;

import java.util.List;

/**
 * Created by zxc on 2018/8/9.
 *
 * 猜你喜欢adapter
 */

public class GoodsListAdapterEx extends BaseAdapter implements ShareContentView.OnShareClickListener {

    private Context context;

    private List<InterfaceManager.SearchProductInfoEx> dataList;

    private static final int COUNT_PAGER = 2;

    //分享的商品
    private InterfaceManager.SearchProductInfoEx shareProductInfo;


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

    public GoodsListAdapterEx(Context context) {
        this.context = context;


        isLogin = AccountUtil.isLogin();
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

        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_goodslistex, null);
            viewHolder.iv_goods = (ImageView) convertView.findViewById(R.id.iv_goods);

            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);


            viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final InterfaceManager.SearchProductInfoEx searchProductInfo = dataList.get(position);

        //商品图片
        //设置图片圆角角度
        Glide.with(context).load(searchProductInfo.pictUrl).transform(new GlideRoundTransform(context,10))
                .into(viewHolder.iv_goods);

        String goodsImgId = GoodsUtil.getGoodsBusinessRid((int) searchProductInfo.isMall,searchProductInfo.productType);


        //名称
        if(!TextUtils.isEmpty(searchProductInfo.itemTitle)){
            viewHolder.tv_name.setText(searchProductInfo.itemTitle);
        }else{
            viewHolder.tv_name.setText(searchProductInfo.shortTitle);
        }

        //分享按钮

        //券后价格
        viewHolder.tv_price.setText("¥"+CommonFunc.fixText(searchProductInfo.afterQuan,2));
        //原价

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

    }


    private BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    public final class ViewHolder {

        public ImageView iv_goods;

        public TextView tv_name;

        public TextView tv_price;

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
