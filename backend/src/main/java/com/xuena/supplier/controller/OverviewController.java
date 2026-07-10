package com.xuena.supplier.controller;

import com.xuena.supplier.service.OverviewService;
import com.xuena.supplier.vo.ResultVO;
import com.xuena.supplier.vo.response.DimensionAvgVO;
import com.xuena.supplier.vo.response.GradeDistributionVO;
import com.xuena.supplier.vo.response.StatsVO;
import com.xuena.supplier.vo.response.TrendDataVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/overview")
public class OverviewController {

    private final OverviewService overviewService;

    public OverviewController(OverviewService overviewService) {
        this.overviewService = overviewService;
    }

    @GetMapping("/trend")
    public ResultVO<TrendDataVO> getTrend(
            @RequestParam(value = "categories", required = false) List<String> categories,
            @RequestParam(value = "suppliers", required = false) List<String> suppliers,
            @RequestParam(value = "yearMonths", required = false) List<String> yearMonths,
            @RequestParam(value = "aggregateType", defaultValue = "avg") String aggregateType) {
        TrendDataVO data = overviewService.getTrendData(categories, suppliers, yearMonths, aggregateType);
        return ResultVO.success(data);
    }

    @GetMapping("/categories")
    public ResultVO<List<String>> getCategories() {
        List<String> categories = overviewService.getAllCategories();
        return ResultVO.success(categories);
    }

    @GetMapping("/year-months")
    public ResultVO<List<String>> getYearMonths() {
        List<String> yearMonths = overviewService.getAllYearMonths();
        return ResultVO.success(yearMonths);
    }

    @GetMapping("/suppliers")
    public ResultVO<List<String>> getSuppliers() {
        List<String> suppliers = overviewService.getAllSuppliers();
        return ResultVO.success(suppliers);
    }

    @GetMapping("/stats")
    public ResultVO<StatsVO> getStats(@RequestParam(value = "yearMonth", required = false) String yearMonth) {
        StatsVO stats = overviewService.getStats(yearMonth);
        return ResultVO.success(stats);
    }

    @GetMapping("/grade-distribution")
    public ResultVO<List<GradeDistributionVO>> getGradeDistribution(
            @RequestParam(value = "category", required = false) String category) {
        List<GradeDistributionVO> data = overviewService.getGradeDistribution(category);
        return ResultVO.success(data);
    }

    @GetMapping("/dimension-avg")
    public ResultVO<List<DimensionAvgVO>> getDimensionAvg(
            @RequestParam(value = "yearMonths", required = false) List<String> yearMonths) {
        List<DimensionAvgVO> data = overviewService.getDimensionAvg(yearMonths);
        return ResultVO.success(data);
    }
}
