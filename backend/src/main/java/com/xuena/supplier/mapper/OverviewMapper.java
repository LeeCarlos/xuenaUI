package com.xuena.supplier.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OverviewMapper {

    List<Map<String, Object>> selectTrendData(
            @Param("categories") List<String> categories,
            @Param("suppliers") List<String> suppliers,
            @Param("yearMonths") List<String> yearMonths
    );

    List<String> selectAllCategories();

    List<String> selectAllYearMonths();

    List<String> selectAllSuppliers();

    List<Map<String, Object>> selectStats(@Param("yearMonth") String yearMonth);

    List<Map<String, Object>> selectGradeDistribution(@Param("category") String category);

    List<Map<String, Object>> selectDimensionAvg(@Param("yearMonths") List<String> yearMonths);
}
