package com.pc.huangshan.model.cwbb;

import com.pc.huangshan.common.emnu.TicketSorceEnum;

/**
 * 财务报表
 */
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
    private Double bTwoCSum=0.0;//B2C 当前总额
    private Double bTwoCMonth=0.0;//B2C 月度总额
    private Double bTwoCYear=0.0;//B2C 年度总额
    private Double oTA=0.0;//授信OTA
    private Double zk=0.0;//授信直客
    private Double sxqt=0.0;//授信其他

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
        this.bTwoCSum=0.0;
        this.bTwoCMonth=0.0;
        this.bTwoCYear=0.0;
        this.oTA=0.0;
        this.zk=0.0;
        this.sxqt=0.0;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    public Double getByj() {
        return byj;
    }

    public void setByj(Double byj) {
        this.byj = byj;
    }

    public Double getZfb() {
        return zfb;
    }

    public void setZfb(Double zfb) {
        this.zfb = zfb;
    }

    public Double getYl() {
        return yl;
    }

    public void setYl(Double yl) {
        this.yl = yl;
    }

    public Double getWx() {
        return wx;
    }

    public void setWx(Double wx) {
        this.wx = wx;
    }

    public Double getZhJf() {
        return zhJf;
    }

    public void setZhJf(Double zhJf) {
        this.zhJf = zhJf;
    }

    public Double getZfzf() {
        return zfzf;
    }

    public void setZfzf(Double zfzf) {
        this.zfzf = zfzf;
    }

    public Double getXjzf() {
        return xjzf;
    }

    public void setXjzf(Double xjzf) {
        this.xjzf = xjzf;
    }

    public Double getWzd() {
        return wzd;
    }

    public void setWzd(Double wzd) {
        this.wzd = wzd;
    }

    public Double getYfk() {
        return yfk;
    }

    public void setYfk(Double yfk) {
        this.yfk = yfk;
    }

    public Double getSd() {
        return sd;
    }

    public void setSd(Double sd) {
        this.sd = sd;
    }

    public Double getJh1() {
        return jh1;
    }

    public void setJh1(Double jh1) {
        this.jh1 = jh1;
    }

    public Double getJh2() {
        return jh2;
    }

    public void setJh2(Double jh2) {
        this.jh2 = jh2;
    }

    public Double getZh() {
        return zh;
    }

    public void setZh(Double zh) {
        this.zh = zh;
    }

    public Double getXyzh() {
        return xyzh;
    }

    public void setXyzh(Double xyzh) {
        this.xyzh = xyzh;
    }

    public Double getHs() {
        return hs;
    }

    public void setHs(Double hs) {
        this.hs = hs;
    }

    public Double getGs() {
        return gs;
    }

    public void setGs(Double gs) {
        this.gs = gs;
    }

    public Double getZzf() {
        return zzf;
    }

    public void setZzf(Double zzf) {
        this.zzf = zzf;
    }

    public Double getMonth() {
        return month;
    }

    public void setMonth(Double month) {
        this.month = month;
    }

    public Double getYear() {
        return year;
    }

    public void setYear(Double year) {
        this.year = year;
    }

    public Double getbTwoCSum() {
        return bTwoCSum;
    }

    public void setbTwoCSum(Double bTwoCSum) {
        this.bTwoCSum = bTwoCSum;
    }

    public Double getbTwoCMonth() {
        return bTwoCMonth;
    }

    public void setbTwoCMonth(Double bTwoCMonth) {
        this.bTwoCMonth = bTwoCMonth;
    }

    public Double getbTwoCYear() {
        return bTwoCYear;
    }

    public void setbTwoCYear(Double bTwoCYear) {
        this.bTwoCYear = bTwoCYear;
    }

    public Double getoTA() {
        return oTA;
    }

    public void setoTA(Double oTA) {
        this.oTA = oTA;
    }

    public Double getZk() {
        return zk;
    }

    public void setZk(Double zk) {
        this.zk = zk;
    }

    public Double getSxqt() {
        return sxqt;
    }

    public void setSxqt(Double sxqt) {
        this.sxqt = sxqt;
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