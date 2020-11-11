package com.pc.huangshan.model.cwbb;

import com.pc.huangshan.common.emnu.HotelSorceEnum;
import com.pc.huangshan.common.emnu.ShopSorceEnum;
import com.pc.huangshan.common.emnu.TicketSorceEnum;

/**
 * 财务统计
 */
public class CwTj {
    private String supplyName;
    private Double totalSum;

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    public Double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(Double totalSum) {
        this.totalSum = totalSum;
    }
    public int getTickeSort(){
        if(TicketSorceEnum.HSFJQ.getName().equals(supplyName)){
            return TicketSorceEnum.HSFJQ.getSorce();
        }else if(TicketSorceEnum.HSFJQGGLC.getName().equals(supplyName)){
            return TicketSorceEnum.HSFJQGGLC.getSorce();
        }else if(TicketSorceEnum.HSFJQYGSD.getName().equals(supplyName)){
            return TicketSorceEnum.HSFJQYGSD.getSorce();
        }else if(TicketSorceEnum.HSFJQTPSD.getName().equals(supplyName)){
            return TicketSorceEnum.HSFJQTPSD.getSorce();
        }else if(TicketSorceEnum.HSFJQYPSD.getName().equals(supplyName)){
            return TicketSorceEnum.HSFJQYPSD.getSorce();
        }else {
            return 0;
        }
    }


    public int getShopSort(){
        if(ShopSorceEnum.LBLCCY.getName().equals(supplyName)){
            return ShopSorceEnum.LBLCCY.getSorce();
        }else if(ShopSorceEnum.HX.getName().equals(supplyName)){
            return ShopSorceEnum.HX.getSorce();
        }else {
            return 0;
        }
    }

    public int getHotelSort() {
        if(HotelSorceEnum.HSYR.getName().equals(supplyName)){
            return HotelSorceEnum.HSYR.getSorce();
        }else if(HotelSorceEnum.HSSL.getName().equals(supplyName)){
            return HotelSorceEnum.HSSL.getSorce();
        }else if(HotelSorceEnum.HSYP.getName().equals(supplyName)){
            return HotelSorceEnum.HSYP.getSorce();
        }else if(HotelSorceEnum.HSBY.getName().equals(supplyName)){
            return HotelSorceEnum.HSBY.getSorce();
        }else if(HotelSorceEnum.HSXH.getName().equals(supplyName)){
            return HotelSorceEnum.HSXH.getSorce();
        }else {
            return 0;
        }
    }
}
