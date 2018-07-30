package com.eryue.live;

/**
 * Created by enjoy on 2018/5/13.
 */

public class GoodsBusinessModel {

    /**
     * 选中
     */
    public static final int TAG_UNSELECT = 0;
    /**
     * 未选中
     */
    public static final int TAG_SELECT = 1;

    private String name;

    private String value;

    /**
     * 选中状态
     */
    private int selectTag;

    public GoodsBusinessModel(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getSelectTag() {
        return selectTag;
    }

    public void setSelectTag(int selectTag) {
        this.selectTag = selectTag;
    }
}
