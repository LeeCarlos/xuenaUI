package com.xuena.supplier.service;

import com.xuena.supplier.vo.response.DimensionAvgVO;
import com.xuena.supplier.vo.response.GradeDistributionVO;
import com.xuena.supplier.vo.response.StatsVO;
import com.xuena.supplier.vo.response.TrendDataVO;

import java.util.List;

public interface OverviewService {

    TrendDataVO getTrendData(List<String> categories, List<String> yearMonths, String aggregateType);

    List<String> getAllCategories();

    List<String> getAllYearMonths();

    StatsVO getStats(String yearMonth);

    List<GradeDistributionVO> getGradeDistribution(String category);

    List<DimensionAvgVO> getDimensionAvg(List<String> yearMonths);
}
