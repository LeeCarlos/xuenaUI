package com.xuena.supplier.application.service;

import com.xuena.supplier.interfaces.vo.response.DimensionAvgVO;
import com.xuena.supplier.interfaces.vo.response.GradeDistributionVO;
import com.xuena.supplier.interfaces.vo.response.StatsVO;
import com.xuena.supplier.interfaces.vo.response.TrendDataVO;

import java.util.List;

public interface OverviewService {

    TrendDataVO getTrendData(List<String> categories, List<String> suppliers, List<String> yearMonths, String aggregateType);

    List<String> getAllCategories();

    List<String> getAllYearMonths();

    List<String> getAllSuppliers();

    StatsVO getStats(String yearMonth);

    List<GradeDistributionVO> getGradeDistribution(String category);

    List<DimensionAvgVO> getDimensionAvg(List<String> yearMonths);
}
