package com.pc.huangshan.model.cwbb;

import com.pc.huangshan.common.emnu.TicketSorceEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 财务报表
 */
@Setter
@Getter
public class Cwbb {
   private String supplyName;
   private Double byj=0.0;//备用金
   private Double zfb=0.0;//支付宝
   private Double yl=0.0;//银联
   private Double wx=0.0;//微信
   private Double zhJf=0.0;//中行B2B
   private Double zfzf=0.0;//积分支付
   private Double xjzf=0.0;//现金支付
   private Double wzd=0.0;//未指定
   private Double yfk=0.0;//预付款
   private Double sd=0.0;//手动
   private Double jh1=0.0;//建行B2B
   private Double jh2=0.0;//建行B2C
   private Double zh=0.0;//中行B2C
   private Double xyzh=0.0;//信用支付
   private Double hs=0.0;//徽商银行
   private Double gs=0.0;//工商银行
    private Double zzf=0.0;//总支付
    private Double month=0.0;//月度
    private Double year=0.0;//年度
    private Double btwocsum=0.0;//B2C 当前总额
    private Double btwocmonth=0.0;//B2C 月度总额
    private Double btwocyear=0.0;//B2C 年度总额
    private Double oTA=0.0;//授信OTA
    private Double zk=0.0;//授信直客
    private Double sxqt=0.0;//授信其他
    private Double jhnq=0.0;//建行内嵌支付

    public Cwbb() {
    }

    public Cwbb(String name) {
        this.supplyName=name;
        this.zzf=0.0;
        this.byj=0.0;
        this.zfb=0.0;
        this.yl=0.0;
        this.wx=0.0;
        this.zhJf=0.0;
        this.zfzf=0.0;
        this.xjzf=0.0;
        this.wzd=0.0;
        this.yfk=0.0;
        this.sd=0.0;
        this.jh1=0.0;
        this.jh2=0.0;
        this.zh=0.0;
        this.xyzh=0.0;
        this.hs=0.0;
        this.gs=0.0;
        this.month=0.0;
        this.year=0.0;
        this.btwocsum=0.0;
        this.btwocmonth=0.0;
        this.btwocyear=0.0;
        this.oTA=0.0;
        this.zk=0.0;
        this.sxqt=0.0;
        this.jhnq=0.0;
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
}