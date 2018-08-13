package com.eryue.live;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eryue.AccountUtil;
import com.eryue.R;
import com.eryue.home.GoodsUtil;
import com.eryue.widget.GlideRoundTransform;
import com.library.util.CommonFunc;

import net.InterfaceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/10.
 */

public class LiveGoodsRecylerAdapter extends RecyclerView.Adapter<LiveGoodsRecylerAdapter.ViewHolder> {

    private Context context;
    private List<InterfaceManager.SearchProductInfoEx> allDataList = new ArrayList<>();


    /**
     * 判断是否登录
     */
    private boolean isLogin;

    public LiveGoodsRecylerAdapter(Context context){
        this.context = context;

        isLogin = AccountUtil.isLogin();

    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public void setData (List<InterfaceManager.SearchProductInfoEx> allDataList){
        this.allDataList = allDataList;

    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_searchliveex_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        final InterfaceManager.SearchProductInfoEx searchProductInfo = allDataList.get(position);

        //商品状态
        if (searchProductInfo.couponStatus == -1){
            holder.iv_status.setVisibility(View.GONE);
        }else{
            holder.iv_status.setVisibility(View.VISIBLE);
            Glide.with(context).load(GoodsUtil.getGoodsStatusRid(false,searchProductInfo.couponStatus)).asBitmap().into(holder.iv_status);
        }


        ViewGroup.LayoutParams layoutParams = holder.iv_goods.getLayoutParams();
        layoutParams.height = 400 + (position % 3) * 60;
        holder.iv_goods.setLayoutParams(layoutParams);
        //商品图片
        //设置图片圆角角度
        Glide.with(context).load(searchProductInfo.pictUrl)
                .into(holder.iv_goods);

        String goodsImgId = GoodsUtil.getGoodsBusinessRid((int) searchProductInfo.isMall,searchProductInfo.productType);

        //商品渠道
        if (goodsImgId == null){
            holder.iv_business.setVisibility(View.GONE);
        }else{
            holder.iv_business.setVisibility(View.VISIBLE);
            holder.iv_business.setText(GoodsUtil.getGoodsBusinessRid((int) searchProductInfo.isMall,searchProductInfo.productType).toString());
        }
        //名称
        if(!TextUtils.isEmpty(searchProductInfo.itemTitle)){
            holder.tv_name.setText(searchProductInfo.itemTitle);
        }else{
            holder.tv_name.setText(searchProductInfo.shortTitle);

        }

        //优惠券价格
        holder.tv_price_paper.setText("券¥" +CommonFunc.fixText(searchProductInfo.quanPrice, 0));

        if (isLogin){
            holder.tv_share.setVisibility(View.VISIBLE);
            //分享后可赚
            holder.tv_share.setText("赚¥" + CommonFunc.fixText(searchProductInfo.jf, 2));
        }else{
            holder.tv_share.setVisibility(View.GONE);
        }
        //分享按钮
        /*viewHolder.layout_share.setOnClickListener(new View.OnClickListener() {
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
        });*/

        //券后价格
        holder.tv_price.setText("¥"+CommonFunc.fixText(searchProductInfo.afterQuan,2));
        //原价
        holder.tv_originalprice.setText("¥"+CommonFunc.fixText(searchProductInfo.discountPrice,2));
        //原价-中间横线
        holder.tv_originalprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        //销量
        holder.tv_sellnum.setText("已售 "+String.valueOf(searchProductInfo.soldQuantity));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("这里是点击每一行item的响应事件",""+position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return allDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView iv_status;
        public ImageView iv_goods;

        public TextView iv_business;
        public TextView tv_name;

        public TextView tv_price_paper;
        public TextView tv_share;
        public LinearLayout layout_share;
        public ImageView iv_share;

        public TextView tv_price;
        public TextView tv_originalprice;
        public TextView tv_sellnum;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_status = itemView.findViewById(R.id.iv_status);
            iv_goods = itemView.findViewById(R.id.iv_goods);

            iv_business = itemView.findViewById(R.id.iv_business);
            tv_name = itemView.findViewById(R.id.tv_name);

            tv_price_paper = itemView.findViewById(R.id.tv_price_paper);
            tv_share = itemView.findViewById(R.id.tv_share);
            layout_share = itemView.findViewById(R.id.layout_share);
            iv_share = itemView.findViewById(R.id.iv_share);

            tv_price = itemView.findViewById(R.id.tv_price);
            tv_originalprice = itemView.findViewById(R.id.tv_originalprice);
            tv_sellnum = itemView.findViewById(R.id.tv_sellnum);

        }
    }
}
