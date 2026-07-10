package com.xuena.supplier.service.impl;

import com.xuena.supplier.mapper.OverviewMapper;
import com.xuena.supplier.service.OverviewService;
import com.xuena.supplier.vo.response.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OverviewServiceImpl implements OverviewService {

    private final OverviewMapper overviewMapper;

    public OverviewServiceImpl(OverviewMapper overviewMapper) {
        this.overviewMapper = overviewMapper;
    }

    @Override
    public TrendDataVO getTrendData(List<String> categories, List<String> suppliers, List<String> yearMonths, String aggregateType) {
        List<Map<String, Object>> trendData = overviewMapper.selectTrendData(categories, suppliers, yearMonths);

        TrendDataVO result = new TrendDataVO();
        result.setCategories(categories != null ? categories : new ArrayList<>());
        result.setAggregateType(aggregateType != null ? aggregateType : "avg");

        List<String> xAxis = new ArrayList<>();
        List<String> xAxisLabels = new ArrayList<>();
        List<Double> totalData = new ArrayList<>();
        List<Double> dimensionAData = new ArrayList<>();
        List<Double> dimensionBData = new ArrayList<>();
        List<Double> dimensionCData = new ArrayList<>();
        List<Double> dimensionDData = new ArrayList<>();

        boolean isAvg = "avg".equalsIgnoreCase(aggregateType);

        for (Map<String, Object> row : trendData) {
            String yearMonth = (String) row.get("yearMonth");
            xAxis.add(yearMonth);
            xAxisLabels.add(yearMonth.substring(5) + "月");

            if (isAvg) {
                totalData.add(getDouble(row.get("avgTotal")));
                dimensionAData.add(getDouble(row.get("avgDimensionA")));
                dimensionBData.add(getDouble(row.get("avgDimensionB")));
                dimensionCData.add(getDouble(row.get("avgDimensionC")));
                dimensionDData.add(getDouble(row.get("avgDimensionD")));
            } else {
                totalData.add(getDouble(row.get("sumTotal")));
                dimensionAData.add(getDouble(row.get("sumDimensionA")));
                dimensionBData.add(getDouble(row.get("sumDimensionB")));
                dimensionCData.add(getDouble(row.get("sumDimensionC")));
                dimensionDData.add(getDouble(row.get("sumDimensionD")));
            }
        }

        result.setXAxis(xAxis);
        result.setXAxisLabels(xAxisLabels);

        List<TrendSeriesVO> series = new ArrayList<>();
        series.add(createSeries("指标总分", "line", totalData, "total"));
        series.add(createSeries("品质考核", "line", dimensionAData, "dimensionA"));
        series.add(createSeries("成本考核", "line", dimensionBData, "dimensionB"));
        series.add(createSeries("交货考核", "line", dimensionCData, "dimensionC"));
        series.add(createSeries("服务考核", "line", dimensionDData, "dimensionD"));

        result.setSeries(series);
        return result;
    }

    @Override
    public List<String> getAllCategories() {
        return overviewMapper.selectAllCategories();
    }

    @Override
    public List<String> getAllYearMonths() {
        return overviewMapper.selectAllYearMonths();
    }

    @Override
    public List<String> getAllSuppliers() {
        return overviewMapper.selectAllSuppliers();
    }

    @Override
    public StatsVO getStats(String yearMonth) {
        List<Map<String, Object>> statsList = overviewMapper.selectStats(yearMonth);

        if (statsList.isEmpty()) {
            return null;
        }

        Map<String, Object> latest = statsList.get(0);
        StatsVO result = new StatsVO();
        result.setYearMonth((String) latest.get("yearMonth"));
        result.setAvgTotal(getDouble(latest.get("avgTotal")));
        result.setGradeACount(getInteger(latest.get("gradeACount")));
        result.setGradeDCount(getInteger(latest.get("gradeDCount")));

        if (statsList.size() > 1) {
            Map<String, Object> prev = statsList.get(1);
            Double prevTotal = getDouble(prev.get("avgTotal"));
            if (prevTotal != null && prevTotal > 0) {
                result.setChangeFromPrev(Math.round((getDouble(latest.get("avgTotal")) - prevTotal) / prevTotal * 1000.0) / 10.0);
            }
        }

        return result;
    }

    @Override
    public List<GradeDistributionVO> getGradeDistribution(String category) {
        List<Map<String, Object>> data = overviewMapper.selectGradeDistribution(category);
        List<GradeDistributionVO> result = new ArrayList<>();

        for (Map<String, Object> row : data) {
            GradeDistributionVO vo = new GradeDistributionVO();
            vo.setYearMonth((String) row.get("yearMonth"));
            vo.setMonthLabel(((String) row.get("yearMonth")).substring(5) + "月");
            Integer gradeA = getInteger(row.get("gradeA"));
            Integer gradeB = getInteger(row.get("gradeB"));
            Integer gradeC = getInteger(row.get("gradeC"));
            Integer gradeD = getInteger(row.get("gradeD"));
            Integer totalCount = gradeA + gradeB + gradeC + gradeD;
            vo.setTotalCount(totalCount);
            vo.setGradeA(gradeA);
            vo.setGradeB(gradeB);
            vo.setGradeC(gradeC);
            vo.setGradeD(gradeD);
            vo.setGradeAPercent(calculatePercent(gradeA, totalCount));
            vo.setGradeBPercent(calculatePercent(gradeB, totalCount));
            vo.setGradeCPercent(calculatePercent(gradeC, totalCount));
            vo.setGradeDPercent(calculatePercent(gradeD, totalCount));
            result.add(vo);
        }

        return result;
    }

    @Override
    public List<DimensionAvgVO> getDimensionAvg(List<String> yearMonths) {
        List<Map<String, Object>> data = overviewMapper.selectDimensionAvg(yearMonths);
        List<DimensionAvgVO> result = new ArrayList<>();

        for (Map<String, Object> row : data) {
            DimensionAvgVO vo = new DimensionAvgVO();
            vo.setYearMonth((String) row.get("yearMonth"));
            vo.setMonthLabel(((String) row.get("yearMonth")).substring(5) + "月");
            vo.setDimensionA(getDouble(row.get("dimensionA")));
            vo.setDimensionB(getDouble(row.get("dimensionB")));
            vo.setDimensionC(getDouble(row.get("dimensionC")));
            vo.setDimensionD(getDouble(row.get("dimensionD")));
            result.add(vo);
        }

        return result;
    }

    private String calculatePercent(Integer count, Integer total) {
        if (total == null || total == 0) {
            return "0.0%";
        }
        return String.format("%.1f%%", count * 100.0 / total);
    }

    private TrendSeriesVO createSeries(String name, String type, List<Double> data, String dimension) {
        TrendSeriesVO series = new TrendSeriesVO();
        series.setName(name);
        series.setType(type);
        series.setData(data);
        series.setDimension(dimension);
        return series;
    }

    private Double getDouble(Object value) {
        if (value == null) {
            return 0.0;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private Integer getInteger(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
