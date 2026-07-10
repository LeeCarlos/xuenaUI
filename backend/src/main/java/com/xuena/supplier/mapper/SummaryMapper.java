package com.xuena.supplier.mapper;

import com.xuena.supplier.vo.response.SummaryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SummaryMapper {

    Integer countTotalSuppliers();

    Integer countActiveSuppliers();

    Integer countAssessments(@Param("yearMonth") String yearMonth);

    Integer countCompletedAssessments(@Param("yearMonth") String yearMonth);

    Double calculateAvgScore(@Param("yearMonth") String yearMonth);

    List<SummaryVO.DepartmentSummary> selectDepartmentSummaries(@Param("yearMonth") String yearMonth);

    List<SummaryVO.CategorySummary> selectCategorySummaries(@Param("yearMonth") String yearMonth);
}
