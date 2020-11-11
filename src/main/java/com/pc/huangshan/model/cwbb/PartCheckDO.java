package com.pc.huangshan.model.cwbb;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartCheckDO {
    private String orderNo;
    private String supplyName;
    private String startTime;
    private String amount;
    private String leftAmount;
    private String checkAmount;
    private String refundAmount;
    private String price;
    private String orderInfo;
}
