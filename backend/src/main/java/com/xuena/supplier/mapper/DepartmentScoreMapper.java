package com.xuena.supplier.mapper;

import com.xuena.supplier.entity.DepartmentScoreDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DepartmentScoreMapper {

    DepartmentScoreDO selectById(@Param("id") Long id);

    List<DepartmentScoreDO> selectList(@Param("yearMonth") String yearMonth, @Param("supplierName") String supplierName,
                                        @Param("department") String department);

    int insert(DepartmentScoreDO score);

    int update(DepartmentScoreDO score);

    int delete(@Param("id") Long id);

    int updateStatus(@Param("id") Long id, @Param("status") String status);
}