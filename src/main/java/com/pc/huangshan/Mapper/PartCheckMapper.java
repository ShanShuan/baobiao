package com.pc.huangshan.Mapper;

import com.pc.huangshan.model.cwbb.PartCheckDO;
import com.pc.huangshan.model.cwbb.PartCheckStatisticsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PartCheckMapper {


    List<PartCheckDO> list(@Param("startTime") String st, @Param("endTime") String et, @Param("orderNo")String orderNo);

    List<PartCheckStatisticsDO> listStatistics(@Param("startTime") String startTime, @Param("endTime")String endTime);
}