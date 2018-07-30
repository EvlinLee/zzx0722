package com.eryue.model;

/**
 * Created by enjoy on 2018/3/10.
 */

public class GoodsSearchModel {

    private String keyword;

    public GoodsSearchModel(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
