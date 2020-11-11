package com.pc.huangshan.common.emnu;

public enum OrderTypeEnum {
    TICKET("ticket","门票"),
    SHOP("shop","商品"),
    REPAST("repast","餐饮"),
    HOTEL("hotel","酒店"),
    COMBO("combo","套票"),
    CAR("car","租车"),
    BUS("bus","交通"),
    GUIDE("guide","导游商品"),
    ROUTE("route","跟团游");

    private String name;
    private String zwName;

    OrderTypeEnum(String name,String zwName) {
        this.name = name;
        this.zwName=zwName;
    }

    public static String getNameByCode(String orderType) {
        for (OrderTypeEnum value : OrderTypeEnum.values()) {
            if(value.getName().equals(orderType)){
                return value.getZwName();
            }
        }
        return null;
    }

    public String getZwName() {
        return zwName;
    }

    public void setZwName(String zwName) {
        this.zwName = zwName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
