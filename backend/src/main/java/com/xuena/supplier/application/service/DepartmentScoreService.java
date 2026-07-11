package com.xuena.supplier.application.service;

import com.xuena.supplier.domain.entity.DepartmentScoreDO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DepartmentScoreService {

    DepartmentScoreDO getById(String id);

    List<DepartmentScoreDO> list(String yearMonth, String supplierName, String department);

    DepartmentScoreDO create(DepartmentScoreDO score);

    DepartmentScoreDO update(String id, DepartmentScoreDO score);

    void delete(String id);

    void submit(String id);

    void complete(String id);

    void exportTemplate(String department, HttpServletResponse response) throws IOException;

    void batchImport(MultipartFile file, String department) throws IOException;
}