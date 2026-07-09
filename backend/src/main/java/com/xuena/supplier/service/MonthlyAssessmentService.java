package com.xuena.supplier.service;

import com.xuena.supplier.entity.MonthlyAssessmentDO;

import java.util.List;

public interface MonthlyAssessmentService {

    MonthlyAssessmentDO getById(Long id);

    MonthlyAssessmentDO getByYearMonthAndSupplier(String yearMonth, String supplierName);

    List<MonthlyAssessmentDO> list(String yearMonth, String supplierName, String category, String grade, String status);

    MonthlyAssessmentDO create(MonthlyAssessmentDO assessment);

    MonthlyAssessmentDO update(Long id, MonthlyAssessmentDO assessment);

    void delete(Long id);

    void submit(Long id);

    void lock(Long id);

    void calculateAndSave(MonthlyAssessmentDO assessment);
}