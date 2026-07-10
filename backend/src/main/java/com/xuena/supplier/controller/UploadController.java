package com.xuena.supplier.controller;

import com.xuena.supplier.entity.MonthlyAssessmentDO;
import com.xuena.supplier.entity.SupplierPoolDO;
import com.xuena.supplier.service.MonthlyAssessmentService;
import com.xuena.supplier.service.SupplierPoolService;
import com.xuena.supplier.util.EasyExcelUtil;
import com.xuena.supplier.vo.ResultVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private final SupplierPoolService supplierPoolService;
    private final MonthlyAssessmentService monthlyAssessmentService;

    public UploadController(SupplierPoolService supplierPoolService,
            MonthlyAssessmentService monthlyAssessmentService) {
        this.supplierPoolService = supplierPoolService;
        this.monthlyAssessmentService = monthlyAssessmentService;
    }

    @PostMapping("/supplier")
    public ResultVO<Void> uploadSupplierPool(@RequestParam("file") MultipartFile file) throws IOException {
        List<SupplierPoolDO> suppliers = EasyExcelUtil.readExcel(file, SupplierPoolDO.class);
        for (SupplierPoolDO supplier : suppliers) {
            try {
                supplierPoolService.create(supplier);
            } catch (Exception e) {
            }
        }
        return ResultVO.success();
    }

    @PostMapping("/assessment")
    public ResultVO<Void> uploadAssessment(@RequestParam("file") MultipartFile file) throws IOException {
        List<MonthlyAssessmentDO> assessments = EasyExcelUtil.readExcel(file, MonthlyAssessmentDO.class);
        for (MonthlyAssessmentDO assessment : assessments) {
            try {
                assessment.setFileName(file.getOriginalFilename());
                monthlyAssessmentService.create(assessment);
            } catch (Exception e) {
            }
        }
        return ResultVO.success();
    }

    @GetMapping("/template/supplier")
    public void downloadSupplierTemplate(HttpServletResponse response) throws IOException {
        EasyExcelUtil.writeExcel(response, "supplier_template.xlsx", SupplierPoolDO.class, new ArrayList<>());
    }

    @GetMapping("/template/assessment")
    public void downloadAssessmentTemplate(HttpServletResponse response) throws IOException {
        EasyExcelUtil.writeExcel(response, "assessment_template.xlsx", MonthlyAssessmentDO.class, new ArrayList<>());
    }
}