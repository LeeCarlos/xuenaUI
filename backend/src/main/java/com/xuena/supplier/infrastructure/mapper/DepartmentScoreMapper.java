package com.xuena.supplier.infrastructure.mapper;

import com.xuena.supplier.domain.entity.DepartmentScoreDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DepartmentScoreMapper {

    DepartmentScoreDO selectById(@Param("id") String id);

    List<DepartmentScoreDO> selectList(@Param("yearMonth") String yearMonth, @Param("supplierName") String supplierName,
                                        @Param("department") String department);

    int insert(DepartmentScoreDO score);

    int update(DepartmentScoreDO score);

    int delete(@Param("id") String id);

    int updateStatus(@Param("id") String id, @Param("status") String status);

    int batchInsert(@Param("list") List<DepartmentScoreDO> list);
}