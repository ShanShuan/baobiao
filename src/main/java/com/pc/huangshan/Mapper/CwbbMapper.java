package com.pc.huangshan.Mapper;

import com.pc.huangshan.model.cwbb.CwTj;
import com.pc.huangshan.model.cwbb.Cwbb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CwbbMapper {

    List<Cwbb> listByType(@Param("startTime")String startTime, @Param("endTime")String endTime, @Param("type")String type);

    List<Cwbb> listBySupplyNames(@Param("startTime")String startTime, @Param("endTime")String endTime, @Param("supplyNames")List<String> supplyNames);

    List<CwTj> listTjType(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("type") String type);

    List<CwTj> listTjBySupplyNames(@Param("startTime")String startTime, @Param("endTime") String endTime, @Param("supplyNames")List<String> supplyNames);

    List<CwTj> listBtCType(String startTime, String endTime, String type);

    List<CwTj> listBtCBySupplyNames(String startTime, String endTime, @Param("supplyNames")List<String> supplyNames);

    List<Integer> selectOTAId(@Param("parentId")Integer parentId);

    List<Integer> selectZkId();

    List<Integer> selectAllDistributorId();

    /**
     * 授信支付统计
     * @param distrubitId
     * @param type
     * @return
     */
    List<CwTj> listSxZf(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("distrubitId") List<Integer> distrubitId, @Param("type") String type);


    List<CwTj> listSxZfBySupplyNames(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("distrubitId") List<Integer> distrubitId, @Param("supplyNames")List<String> supplyNames);



}