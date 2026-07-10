package com.xuena.supplier.application.service;

import com.xuena.supplier.interfaces.vo.response.CompareHeatmapVO;
import com.xuena.supplier.interfaces.vo.response.CompareRadarVO;

import java.util.List;

public interface CompareService {

    CompareRadarVO getRadarData(List<String> suppliers, String yearMonth);

    CompareHeatmapVO getHeatmapData(String category, String yearMonth);

    List<String> getCategories();

    List<String> getSuppliers();
}
