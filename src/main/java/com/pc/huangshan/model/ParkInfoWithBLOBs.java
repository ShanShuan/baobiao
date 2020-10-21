package com.pc.huangshan.model;

public class ParkInfoWithBLOBs extends ParkInfo {
    private String orderNotice;

    private String content;

    public String getOrderNotice() {
        return orderNotice;
    }

    public void setOrderNotice(String orderNotice) {
        this.orderNotice = orderNotice == null ? null : orderNotice.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}