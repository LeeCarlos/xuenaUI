package com.xuena.supplier.interfaces.controller;

import com.xuena.supplier.domain.entity.MonthlyAssessmentDO;
import com.xuena.supplier.domain.entity.SupplierPoolDO;
import com.xuena.supplier.application.service.MonthlyAssessmentService;
import com.xuena.supplier.application.service.SupplierPoolService;
import com.xuena.supplier.infrastructure.util.EasyExcelUtil;
import com.xuena.supplier.interfaces.vo.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "文件上传", description = "文件上传下载相关接口")
public class UploadController {

    private final SupplierPoolService supplierPoolService;
    private final MonthlyAssessmentService monthlyAssessmentService;

    public UploadController(SupplierPoolService supplierPoolService,
            MonthlyAssessmentService monthlyAssessmentService) {
        this.supplierPoolService = supplierPoolService;
        this.monthlyAssessmentService = monthlyAssessmentService;
    }

    @PostMapping("/supplier")
    @Operation(summary = "上传供应商数据", description = "批量上传供应商数据")
    public ResultVO<Void> uploadSupplierPool(
            @Parameter(description = "Excel文件") @RequestParam("file") MultipartFile file) throws IOException {
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
    @Operation(summary = "上传考核数据", description = "批量上传考核数据")
    public ResultVO<Void> uploadAssessment(
            @Parameter(description = "Excel文件") @RequestParam("file") MultipartFile file) throws IOException {
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
    @Operation(summary = "下载供应商模板", description = "下载供应商数据导入模板")
    public void downloadSupplierTemplate(HttpServletResponse response) throws IOException {
        EasyExcelUtil.writeExcel(response, "supplier_template.xlsx", SupplierPoolDO.class, new ArrayList<>());
    }

    @GetMapping("/template/assessment")
    @Operation(summary = "下载考核模板", description = "下载考核数据导入模板")
    public void downloadAssessmentTemplate(HttpServletResponse response) throws IOException {
        EasyExcelUtil.writeExcel(response, "assessment_template.xlsx", MonthlyAssessmentDO.class, new ArrayList<>());
    }
}