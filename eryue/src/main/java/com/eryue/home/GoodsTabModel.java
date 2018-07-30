package com.eryue.home;

/**
 * 商品分类tab的模型
 * Created by bli.Jason on 2018/2/9.
 */

public class GoodsTabModel {

    /**
     * 选中
     */
    public static final int TAG_UNSELECT = 0;
    /**
     * 未选中
     */
    public static final int TAG_SELECT = 1;

    /**
     * 好券搜索，指定搜索
     */
    private String type;

    /**
     * 名称
     */
    private String name;
    //综合=updateTime ；券后价=afterQuan ；销量=soldQuantity ; 超优惠=quanPrice
    private String sidx;

    /**
     * 只看有券 0=全部  1=只看有券
     */
    private int isQuan;

    /**
     * 0=全部  1=天猫
     */
    private int isMall;
    /**
     * 选中状态
     */
    private int selectTag;

    public static final int VIDEO = 1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSelectTag() {
        return selectTag;
    }

    public void setSelectTag(int selectTag) {
        this.selectTag = selectTag;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public int getIsQuan() {
        return isQuan;
    }

    public void setIsQuan(int isQuan) {
        this.isQuan = isQuan;
    }

    public int getIsMall() {
        return isMall;
    }

    public void setIsMall(int isMall) {
        this.isMall = isMall;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
