package com.xuena.supplier.application.service;

import com.xuena.supplier.domain.entity.DepartmentScoreDO;

import java.util.List;

public interface DepartmentScoreService {

    DepartmentScoreDO getById(String id);

    List<DepartmentScoreDO> list(String yearMonth, String supplierName, String department);

    DepartmentScoreDO create(DepartmentScoreDO score);

    DepartmentScoreDO update(String id, DepartmentScoreDO score);

    void delete(String id);

    void submit(String id);

    void complete(String id);
}