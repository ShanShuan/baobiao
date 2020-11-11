package com.pc.huangshan.dao;

public class PayType {

        /**
         * 零元支付
         */
        String ZERO = "-1";
        /**
         * NONE : 不指定支付方式
         */
        String NONE = "0";
        /**
         * PREPAID : 预付款
         */
        String PREPAID = "1";
        /**
         * ALI_PAY : 支付宝
         */
        String ALI_PAY = "2";
        /**
         * PC_ALI_PAY : 支付宝PC
         */
        String PC_ALI_PAY = "21";
        /**
         * WAP_ALI_PAY : 支付宝Wap
         */
        String WAP_ALI_PAY = "22";
        /**
         * WAP_ALI_PAY : 支付宝APP
         */
        String APP_ALI_PAY = "23";
        /**
         * WEI_XIN : 微信支付
         */
        String WEI_XIN = "3";
        /**
         * PC_WEI_XIN : 微信支付PC
         */
        String PC_WEI_XIN = "31";
        /**
         * WAP_WEI_XIN : 微信支付WAP,JSAPI
         */
        String WAP_WEI_XIN = "32";
        /**
         * SMALL_WEI_XIN : 微信小程序支付
         */
        String SMALL_WEI_XIN = "33";
        /**
         * WAPZYB_WEI_XIN : WAP智游宝微信支付
         */
        String WAPZYB_WEI_XIN = "34";
        /**
         * PCZYB_WEI_XIN : PC智游宝微信支付
         */
        String PCZYB_WEI_XIN = "35";
        /**
         * APP_WEI_XIN : APP微信支付
         */
        String APP_WEI_XIN = "36";
        /**
         * WAP_WEI_XIN_H5 : WAP微信支付H5
         */
        String WAP_WEI_XIN_H5 = "37";
        /**
         * PAY_PAL : 银联
         */
        String PAY_PAL = "4";

        /**
         * 银联支付：银联_银联闪付被扫支付
         */
        String PAL_PASSIVE_PAY = "41";
        /**
         * 银联支付：银联_云闪付主扫支付
         */
        String PAL_INITIATIVE_PAY = "42";

        /**
         * 银联支付：银联_支付宝被扫支付
         */
        String PAL_ALIPAY_PAY = "43";

        /**
         * 银联支付：银联_聚合主扫支付
         */
        String PAL_QRCODE_PAY = "44";
        /**
         * 银联支付：   银联_微信被扫支付
         */
        String PAL_WECHAT_PAY = "45";
        /**
         * 银联支付：   银联_微信公众号支付
         */
        String PAL_WECHAT_PUBLIC_PAY = "46";

        /**
         * 银联支付：银联_微信小程序支付
         */
        String PAL_WECHAT_APPLET_PAY = "47";
        /**
         * 银联支付：银联_手机控件支付
         */
        String PAL_PHONE_PAY = "48";
        /**
         * MANUAL_PAY : 手动支付
         */
        String MANUAL_PAY = "5";
        /**
         * MANUAL_PAY : 备用金支付
         */
        String CAPITAL_PAY = "6";
        /**
         * POINT_PAY :  分销商积分
         * */
        String POINT_PAY = "61";
        /**
         * BOC_PAY :中国银行支付
         */
        String BOC_P;
}
