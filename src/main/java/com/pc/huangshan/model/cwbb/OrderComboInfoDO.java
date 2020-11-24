package com.pc.huangshan.model.cwbb;

import com.pc.huangshan.common.emnu.OrderTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderComboInfoDO {
    private String orderType;
    private String orderNo;
    private String wayType;
    private String orderStatus;
    private String payTime;
    private String payType;


    private String couponSum;
    private String orderSum;
    private String linkName;
    private String linkMobile;
    private String linkAddr;

    private String orderGift;
    private String teamNo;
    private String noteBook;
    private String distributorName;
    private String groupName;

    private String syncStatus;

    private String refundSum;
    private List<OrderComboDetailDO> comboDetailDOs;

}
