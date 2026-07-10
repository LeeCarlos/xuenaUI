package com.xuena.supplier.interfaces.controller;

import com.xuena.supplier.application.service.OverviewService;
import com.xuena.supplier.interfaces.vo.ResultVO;
import com.xuena.supplier.interfaces.vo.response.DimensionAvgVO;
import com.xuena.supplier.interfaces.vo.response.GradeDistributionVO;
import com.xuena.supplier.interfaces.vo.response.StatsVO;
import com.xuena.supplier.interfaces.vo.response.TrendDataVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/overview")
@Tag(name = "数据概览", description = "数据概览相关接口")
public class OverviewController {

    private final OverviewService overviewService;

    public OverviewController(OverviewService overviewService) {
        this.overviewService = overviewService;
    }

    @GetMapping("/trend")
    @Operation(summary = "获取趋势数据", description = "获取考核趋势数据")
    public ResultVO<TrendDataVO> getTrend(
            @Parameter(description = "分类列表") @RequestParam(value = "categories", required = false) List<String> categories,
            @Parameter(description = "供应商列表") @RequestParam(value = "suppliers", required = false) List<String> suppliers,
            @Parameter(description = "年月列表") @RequestParam(value = "yearMonths", required = false) List<String> yearMonths,
            @Parameter(description = "聚合类型") @RequestParam(value = "aggregateType", defaultValue = "avg") String aggregateType) {
        TrendDataVO data = overviewService.getTrendData(categories, suppliers, yearMonths, aggregateType);
        return ResultVO.success(data);
    }

    @GetMapping("/categories")
    @Operation(summary = "获取分类列表", description = "获取所有分类列表")
    public ResultVO<List<String>> getCategories() {
        List<String> categories = overviewService.getAllCategories();
        return ResultVO.success(categories);
    }

    @GetMapping("/year-months")
    @Operation(summary = "获取年月列表", description = "获取所有年月列表")
    public ResultVO<List<String>> getYearMonths() {
        List<String> yearMonths = overviewService.getAllYearMonths();
        return ResultVO.success(yearMonths);
    }

    @GetMapping("/suppliers")
    @Operation(summary = "获取供应商列表", description = "获取所有供应商列表")
    public ResultVO<List<String>> getSuppliers() {
        List<String> suppliers = overviewService.getAllSuppliers();
        return ResultVO.success(suppliers);
    }

    @GetMapping("/stats")
    @Operation(summary = "获取统计数据", description = "获取考核统计数据")
    public ResultVO<StatsVO> getStats(
            @Parameter(description = "年月") @RequestParam(value = "yearMonth", required = false) String yearMonth) {
        StatsVO stats = overviewService.getStats(yearMonth);
        return ResultVO.success(stats);
    }

    @GetMapping("/grade-distribution")
    @Operation(summary = "获取等级分布", description = "获取考核等级分布数据")
    public ResultVO<List<GradeDistributionVO>> getGradeDistribution(
            @Parameter(description = "分类") @RequestParam(value = "category", required = false) String category) {
        List<GradeDistributionVO> data = overviewService.getGradeDistribution(category);
        return ResultVO.success(data);
    }

    @GetMapping("/dimension-avg")
    @Operation(summary = "获取维度平均分", description = "获取各维度平均分数据")
    public ResultVO<List<DimensionAvgVO>> getDimensionAvg(
            @Parameter(description = "年月列表") @RequestParam(value = "yearMonths", required = false) List<String> yearMonths) {
        List<DimensionAvgVO> data = overviewService.getDimensionAvg(yearMonths);
        return ResultVO.success(data);
    }
}
