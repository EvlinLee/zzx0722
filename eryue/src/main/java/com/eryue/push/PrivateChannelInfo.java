package com.eryue.push;

/**
 * 私有推送 通道
 * <p/>
 * Created by cmwei on 2017/3/16.
 */
public class PrivateChannelInfo implements IData {

    /**
     * 私有推送 频道 类型   取值共用 PushCommUtil 的 PUSH_PLATFORM_xxxx
     */
    private int channelType;

    /**
     * 华为小米 推送的 外部注册返回 tokenId / regId
     */
    private String tokenId;

    public PrivateChannelInfo(int type) {
        this.channelType = type;
    }

    public int getChannelType() {
        return channelType;
    }

    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

}
