package com.xuena.supplier.mapper;

import com.xuena.supplier.entity.MonthlyAssessmentDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MonthlyAssessmentMapper {

    MonthlyAssessmentDO selectById(@Param("id") Long id);

    MonthlyAssessmentDO selectByYearMonthAndSupplier(@Param("yearMonth") String yearMonth, @Param("supplierName") String supplierName);

    List<MonthlyAssessmentDO> selectList(@Param("yearMonth") String yearMonth, @Param("supplierName") String supplierName,
                                         @Param("category") String category, @Param("grade") String grade, @Param("status") String status);

    int insert(MonthlyAssessmentDO assessment);

    int update(MonthlyAssessmentDO assessment);

    int delete(@Param("id") Long id);

    int updateStatus(@Param("id") Long id, @Param("status") String status);

    int countByYearMonthAndSupplier(@Param("yearMonth") String yearMonth, @Param("supplierName") String supplierName);
}