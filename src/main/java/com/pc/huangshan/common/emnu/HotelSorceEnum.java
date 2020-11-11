package com.pc.huangshan.common.emnu;

public enum HotelSorceEnum {
    HSYR("黄山雲亼", 100)
    ,HSSL("黄山狮林大酒店", 99)
    ,HSYP("黄山玉屏楼宾馆", 98)
    ,HSBY("黄山白云宾馆", 97)
    ,HSXH("黄山西海饭店", 96)
    ;

    private String name;
    private Integer sorce;

    HotelSorceEnum(String name, Integer sorce) {
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
