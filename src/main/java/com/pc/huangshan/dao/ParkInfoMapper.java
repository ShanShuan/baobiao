package com.pc.huangshan.dao;

import com.pc.huangshan.model.ParkInfo;
import com.pc.huangshan.model.ParkInfoWithBLOBs;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ParkInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ParkInfoWithBLOBs record);

    int insertSelective(ParkInfoWithBLOBs record);

    ParkInfoWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ParkInfoWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ParkInfoWithBLOBs record);

    int updateByPrimaryKey(ParkInfo record);

    List<ParkInfo> selectAll();

}