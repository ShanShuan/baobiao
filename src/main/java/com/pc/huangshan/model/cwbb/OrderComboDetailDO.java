package com.pc.huangshan.model.cwbb;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderComboDetailDO {
    private String orderInfo;
    private String odPrice;
    private String amount;
    private String startDate;
    private String refundPrice;
    private String  refundNum;
}
