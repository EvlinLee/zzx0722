package com.eryue.home;

/**
 * Created by bli.Jason on 2018/2/7.
 */

public class Goods {

    public static final int VIDEO = 1;
    private String name;
    private float price;
    private float paperPrice;
    private int sellNum;
    private int videoFlag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPaperPrice() {
        return paperPrice;
    }

    public void setPaperPrice(float paperPrice) {
        this.paperPrice = paperPrice;
    }

    public int getSellNum() {
        return sellNum;
    }

    public void setSellNum(int sellNum) {
        this.sellNum = sellNum;
    }

    public int getVideoFlag() {
        return videoFlag;
    }

    public void setVideoFlag(int videoFlag) {
        this.videoFlag = videoFlag;
    }
}
