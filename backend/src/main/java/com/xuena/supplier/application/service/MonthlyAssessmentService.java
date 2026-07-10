package com.xuena.supplier.application.service;

import com.xuena.supplier.domain.entity.MonthlyAssessmentDO;

import java.util.List;

public interface MonthlyAssessmentService {

    MonthlyAssessmentDO getById(String id);

    MonthlyAssessmentDO getByYearMonthAndSupplier(String yearMonth, String supplierName);

    List<MonthlyAssessmentDO> list(String yearMonth, String supplierName, String category, String grade, String status);

    MonthlyAssessmentDO create(MonthlyAssessmentDO assessment);

    MonthlyAssessmentDO update(String id, MonthlyAssessmentDO assessment);

    void delete(String id);

    void submit(String id);

    void lock(String id);

    void calculateAndSave(MonthlyAssessmentDO assessment);
}