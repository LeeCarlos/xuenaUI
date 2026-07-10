package com.xuena.supplier.application.service.impl;

import com.xuena.supplier.infrastructure.mapper.SummaryMapper;
import com.xuena.supplier.application.service.SummaryService;
import com.xuena.supplier.interfaces.vo.response.SummaryVO;
import org.springframework.stereotype.Service;

@Service
public class SummaryServiceImpl implements SummaryService {

    private final SummaryMapper summaryMapper;

    public SummaryServiceImpl(SummaryMapper summaryMapper) {
        this.summaryMapper = summaryMapper;
    }

    @Override
    public SummaryVO getSummary(String yearMonth) {
        SummaryVO vo = new SummaryVO();
        vo.setTotalSupplierCount(summaryMapper.countTotalSuppliers());
        vo.setActiveSupplierCount(summaryMapper.countActiveSuppliers());
        vo.setAssessmentCount(summaryMapper.countAssessments(yearMonth));
        vo.setCompletedAssessmentCount(summaryMapper.countCompletedAssessments(yearMonth));
        vo.setAvgScore(summaryMapper.calculateAvgScore(yearMonth));
        vo.setDepartmentSummaries(summaryMapper.selectDepartmentSummaries(yearMonth));
        vo.setCategorySummaries(summaryMapper.selectCategorySummaries(yearMonth));
        return vo;
    }
}
