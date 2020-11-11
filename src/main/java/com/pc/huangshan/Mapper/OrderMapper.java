package com.pc.huangshan.Mapper;

import com.pc.huangshan.model.cwbb.CwTj;
import com.pc.huangshan.model.cwbb.Cwbb;
import com.pc.huangshan.model.cwbb.OrderDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {


    List<OrderDO> list(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("type")String type);
}