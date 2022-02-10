package com.mojiayi.action.designpattern.strategy;

public enum NoticeChannelEnum {
    /**
     * 微信公众号推送
     */
    WECHAT(1, "微信公众号推送"),
    /**
     * 手机短信
     */
    SMS(2, "手机短信"),
    /**
     * APP推送
     */
    APP_PUSH(3, "APP推送"),
    /**
     * 语音呼叫
     */
    MOBILE_CALL(4, "语音呼叫");

    NoticeChannelEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private int code;
    private String desc;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
