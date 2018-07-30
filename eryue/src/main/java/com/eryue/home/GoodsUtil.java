package com.eryue.home;

import android.text.TextUtils;

import com.eryue.R;

/**
 * Created by enjoy on 2018/5/7.
 */

public class GoodsUtil {

    /**
     * 获取商品状态图片资源
     * @param isGooodsDetail
     * @param couponStatus
     * @return
     */
    public static int getGoodsStatusRid(boolean isGooodsDetail,int couponStatus) {

        int rid;
        //0=已经抢光 1=疯抢中 2=即将售罄
        switch (couponStatus) {
            case 0:
                if (isGooodsDetail){
                    rid = R.drawable.ic_detail_end;
                }else{
                    rid = R.drawable.ic_status_finish;
                }
                break;
            case 1:
                if (isGooodsDetail){
                    rid = R.drawable.ic_detail_fq;
                }else{
                    rid = R.drawable.ic_status_hot;
                }
                break;
            case 2:
                if (isGooodsDetail){
                    rid = R.drawable.ic_detail_finishing;
                }else{
                    rid = R.drawable.ic_status_sq;
                }
                break;
            case 3:
                if (isGooodsDetail){

                    rid = R.drawable.ic_detail_ms;
                }else{
                    rid = R.drawable.ic_status_ms;
                }
                break;
            default:
                if (isGooodsDetail){
                    rid = R.drawable.ic_detail_end;
                }else{
                    rid = R.drawable.ic_status_finish;
                }
                break;

        }
        return rid;

    }

    /**
     * 获取商品渠道图片资源
     * @param isMall
     * @return
     */
    public static int getGoodsBusinessRid(int isMall,String productType) {

        int rid = -1;
        if (!TextUtils.isEmpty(productType)){

            if (productType.equals("tb")||productType.equals("tbActivity")){

                if (isMall == 1){
                    //天猫
                    rid = R.drawable.tmall;
                }else{
                    //淘宝
                    rid = R.drawable.img_tb;
                }

            }else if(productType.equals("jd")){
                //京东
                rid = R.drawable.icon_jd;
            }else if(productType.equals("mgj")){
                //蘑菇街
                rid = R.drawable.icon_mogu;
            }else if(productType.equals("pdd")){
                //拼多多
                rid = R.drawable.icon_pdd;
            }else if(productType.equals("sn")){
                //苏宁
                rid = R.drawable.icon_suning;
            }

        }
        return rid;

    }
}
