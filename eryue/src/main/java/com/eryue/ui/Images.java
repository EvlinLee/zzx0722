package com.eryue.ui;


import java.io.Serializable;

public class Images implements Serializable {
    // 数据库使用

    public int genId;

    public String userId;

    public long insertTime;
    public int showIndex;
    public String picAdd;
    public String smallPicAdd;
    public int themeId;
    public long updateTime;
    public int id;
    public String pictureName;
    public String type;
    public int status;
    public float spicSideRate;

    public Images() {
    }

    @Override
    public int hashCode() {
        return themeId * 13 + 7 * id;
    }

    @Override
    public boolean equals(Object obj) {
        Images temp = (Images) obj;
        return String.valueOf(picAdd + themeId + id).equals(
                String.valueOf(temp.picAdd + temp.themeId + temp.id));
    }
}
