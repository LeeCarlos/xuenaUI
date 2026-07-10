package com.xuena.supplier.controller;

import com.xuena.supplier.service.CompareService;
import com.xuena.supplier.vo.ResultVO;
import com.xuena.supplier.vo.response.CompareHeatmapVO;
import com.xuena.supplier.vo.response.CompareRadarVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/compare")
public class CompareController {

    private final CompareService compareService;

    public CompareController(CompareService compareService) {
        this.compareService = compareService;
    }

    @GetMapping("/radar")
    public ResultVO<CompareRadarVO> getRadarData(
            @RequestParam("suppliers") List<String> suppliers,
            @RequestParam(value = "yearMonth", required = false) String yearMonth) {
        CompareRadarVO data = compareService.getRadarData(suppliers, yearMonth);
        return ResultVO.success(data);
    }

    @GetMapping("/heatmap")
    public ResultVO<CompareHeatmapVO> getHeatmapData(
            @RequestParam("category") String category,
            @RequestParam(value = "yearMonth", required = false) String yearMonth) {
        CompareHeatmapVO data = compareService.getHeatmapData(category, yearMonth);
        return ResultVO.success(data);
    }

    @GetMapping("/categories")
    public ResultVO<List<String>> getCategories() {
        List<String> categories = compareService.getCategories();
        return ResultVO.success(categories);
    }

    @GetMapping("/suppliers")
    public ResultVO<List<String>> getSuppliers() {
        List<String> suppliers = compareService.getSuppliers();
        return ResultVO.success(suppliers);
    }
}
