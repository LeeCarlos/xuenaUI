package com.xuena.supplier.application.service.impl;

import com.xuena.supplier.infrastructure.mapper.CompareMapper;
import com.xuena.supplier.application.service.CompareService;
import com.xuena.supplier.interfaces.vo.response.CompareHeatmapVO;
import com.xuena.supplier.interfaces.vo.response.CompareRadarVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompareServiceImpl implements CompareService {

    private final CompareMapper compareMapper;

    public CompareServiceImpl(CompareMapper compareMapper) {
        this.compareMapper = compareMapper;
    }

    @Override
    public CompareRadarVO getRadarData(List<String> suppliers, String yearMonth) {
        CompareRadarVO vo = new CompareRadarVO();
        vo.setIndicators(List.of("品质考核", "成本考核", "交货考核", "服务考核"));
        
        List<CompareRadarVO.SupplierRadarData> supplierData = suppliers.stream()
                .map(supplier -> {
                    CompareRadarVO.SupplierRadarData data = new CompareRadarVO.SupplierRadarData();
                    data.setName(supplier);
                    List<Double> scores = compareMapper.selectSupplierDimensionScores(supplier, yearMonth);
                    if (scores == null || scores.isEmpty()) {
                        data.setValues(List.of(0.0, 0.0, 0.0, 0.0));
                    } else {
                        data.setValues(scores);
                    }
                    return data;
                })
                .toList();
        vo.setSuppliers(supplierData);
        return vo;
    }

    @Override
    public CompareHeatmapVO getHeatmapData(String category, String yearMonth) {
        CompareHeatmapVO vo = new CompareHeatmapVO();
        vo.setRows(compareMapper.selectSuppliersByCategory(category));
        vo.setColumns(List.of("品质考核", "成本考核", "交货考核", "服务考核"));
        vo.setValues(compareMapper.selectSupplierDimensionHeatmap(category, yearMonth));
        return vo;
    }

    @Override
    public List<String> getCategories() {
        return compareMapper.selectAllCategories();
    }

    @Override
    public List<String> getSuppliers() {
        return compareMapper.selectAllSuppliers();
    }
}
