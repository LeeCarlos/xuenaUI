package com.xuena.supplier.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompareMapper {

    List<Double> selectSupplierDimensionScores(@Param("supplierName") String supplierName, @Param("yearMonth") String yearMonth);

    List<String> selectSuppliersByCategory(@Param("category") String category);

    List<List<Double>> selectSupplierDimensionHeatmap(@Param("category") String category, @Param("yearMonth") String yearMonth);

    List<String> selectAllCategories();

    List<String> selectAllSuppliers();
}
