package com.pc.huangshan.model.cwbb;

import com.pc.huangshan.common.emnu.OrderTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDO {
    private String orderType; //业务类型
    private String orderNo;//订单号
    private String orderStatus;//订单状态
    private String payTime;//支付时间
    private String payType;//支付类型
    private String orderInfo;//产品
    private String oiPrice;//销售单价
    private String orderSum;//订单总额
    private String amount;//下单数量


    private String syncStatus; //同步状态
    private String couponSum;//优惠券额
    private String paySum;//支付金额
    private String odPrice;//结算单价
    private String jsSum;//结算金额
    private String ml;//毛利
    private String linkName;//联系人
    private String linkMobile;//联系人电话
    private String startDate;//游玩日期
    private String orderGift;//礼包内容
    private String noteBook;//备忘录
    private String distributorName;//分销商
    private String idCard;//身份证号码

    private String refundSum;//退单金额
    private String refundNum;//退单数量

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        String typeName=OrderTypeEnum.getNameByCode(orderType);
        this.orderType = typeName;
    }

//    private String orderType
//    private String orderNo
//    private String orderStatus
//    private String payTime
//    private String payType
//    private String orderInfo
//    private String oiPrice
//    private String orderSum
//    private String amount
//    private String syncStatus
//    private String couponSum
//    private String odPrice
//    private String jsSum
//    private String linkName
//    private String linkMobile
//    private String startDate
//    private String orderGift
//    private String noteBook
//    private String distributorName
}
