package com.xuena.supplier.interfaces.controller;

import com.xuena.supplier.application.service.CompareService;
import com.xuena.supplier.interfaces.vo.ResultVO;
import com.xuena.supplier.interfaces.vo.response.CompareHeatmapVO;
import com.xuena.supplier.interfaces.vo.response.CompareRadarVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/compare")
@Tag(name = "对比分析", description = "供应商对比分析相关接口")
public class CompareController {

    private final CompareService compareService;

    public CompareController(CompareService compareService) {
        this.compareService = compareService;
    }

    @GetMapping("/radar")
    @Operation(summary = "获取雷达图数据", description = "获取供应商对比雷达图数据")
    public ResultVO<CompareRadarVO> getRadarData(
            @Parameter(description = "供应商名称列表") @RequestParam("suppliers") List<String> suppliers,
            @Parameter(description = "年月") @RequestParam(value = "yearMonth", required = false) String yearMonth) {
        CompareRadarVO data = compareService.getRadarData(suppliers, yearMonth);
        return ResultVO.success(data);
    }

    @GetMapping("/heatmap")
    @Operation(summary = "获取热力图数据", description = "获取分类供应商热力图数据")
    public ResultVO<CompareHeatmapVO> getHeatmapData(
            @Parameter(description = "分类") @RequestParam("category") String category,
            @Parameter(description = "年月") @RequestParam(value = "yearMonth", required = false) String yearMonth) {
        CompareHeatmapVO data = compareService.getHeatmapData(category, yearMonth);
        return ResultVO.success(data);
    }

    @GetMapping("/categories")
    @Operation(summary = "获取分类列表", description = "获取所有分类列表")
    public ResultVO<List<String>> getCategories() {
        List<String> categories = compareService.getCategories();
        return ResultVO.success(categories);
    }

    @GetMapping("/suppliers")
    @Operation(summary = "获取供应商列表", description = "获取所有供应商列表")
    public ResultVO<List<String>> getSuppliers() {
        List<String> suppliers = compareService.getSuppliers();
        return ResultVO.success(suppliers);
    }
}
