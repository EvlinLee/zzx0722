package com.eryue.push;

/**
 * Created by enjoy on 2018/5/20.
 */

public class PushJumpModel {

    private String title;

    private String content;

    public PushJumpModel(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
