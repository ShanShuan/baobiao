package com.pc.huangshan.common.emnu;

public enum ShopSorceEnum {
    LBLCCY("六百里茶业", 100)
    ,HX("黄山华胥文创产品", 99);

    private String name;
    private Integer sorce;

    ShopSorceEnum(String name, Integer sorce) {
        this.name = name;
        this.sorce = sorce;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSorce() {
        return sorce;
    }

    public void setSorce(Integer sorce) {
        this.sorce = sorce;
    }
}
