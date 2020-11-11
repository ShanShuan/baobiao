package com.pc.huangshan.common.emnu;

public enum TicketSorceEnum {
    HSFJQ("黄山风景区", 100)
    ,HSFJQGGLC("黄山风景区观光缆车", 99)
    ,HSFJQYGSD("黄山风景区云谷索道", 98)
    ,HSFJQTPSD("黄山风景区太平索道", 97)
    ,HSFJQYPSD("黄山风景区玉屏索道", 96);
    private String name;
    private Integer sorce;

    TicketSorceEnum(String name, Integer sorce) {
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
