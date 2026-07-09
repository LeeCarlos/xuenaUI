package com.xuena.supplier.service;

import com.xuena.supplier.entity.DepartmentScoreDO;

import java.util.List;

public interface DepartmentScoreService {

    DepartmentScoreDO getById(Long id);

    List<DepartmentScoreDO> list(String yearMonth, String supplierName, String department);

    DepartmentScoreDO create(DepartmentScoreDO score);

    DepartmentScoreDO update(Long id, DepartmentScoreDO score);

    void delete(Long id);

    void submit(Long id);

    void complete(Long id);
}