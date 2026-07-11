package com.xuena.supplier.interfaces.controller;

import com.xuena.supplier.domain.entity.MonthlyAssessmentDO;
import com.xuena.supplier.application.service.MonthlyAssessmentService;
import com.xuena.supplier.infrastructure.util.EasyExcelUtil;
import com.xuena.supplier.interfaces.vo.PageVO;
import com.xuena.supplier.interfaces.vo.ResultVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/assessment")
@Tag(name = "月度考核", description = "月度考核相关接口")
public class AssessmentController {

    private final MonthlyAssessmentService monthlyAssessmentService;

    public AssessmentController(MonthlyAssessmentService monthlyAssessmentService) {
        this.monthlyAssessmentService = monthlyAssessmentService;
    }

    @GetMapping
    @Operation(summary = "查询考核列表", description = "根据条件查询月度考核记录列表")
    public ResultVO<PageVO<MonthlyAssessmentDO>> list(
            @Parameter(description = "年月") @RequestParam(required = false) String yearMonth,
            @Parameter(description = "供应商名称") @RequestParam(required = false) String supplierName,
            @Parameter(description = "分类") @RequestParam(required = false) String category,
            @Parameter(description = "等级") @RequestParam(required = false) String grade,
            @Parameter(description = "状态") @RequestParam(required = false) String status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<MonthlyAssessmentDO> list = monthlyAssessmentService.list(
                yearMonth, supplierName, category, grade, status);
        PageInfo<MonthlyAssessmentDO> pageInfo = new PageInfo<>(list);
        return ResultVO.success(PageVO.of(list, pageInfo.getTotal(), pageNum, pageSize));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询考核详情", description = "根据ID查询月度考核详情")
    public ResultVO<MonthlyAssessmentDO> getById(
            @Parameter(description = "考核ID") @PathVariable String id) {
        MonthlyAssessmentDO assessment = monthlyAssessmentService.getById(id);
        return ResultVO.success(assessment);
    }

    @PostMapping
    @Operation(summary = "创建考核", description = "新增月度考核记录")
    public ResultVO<MonthlyAssessmentDO> create(
            @Parameter(description = "考核信息") @RequestBody MonthlyAssessmentDO assessment) {
        MonthlyAssessmentDO result = monthlyAssessmentService.create(assessment);
        return ResultVO.success(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新考核", description = "根据ID更新月度考核信息")
    public ResultVO<MonthlyAssessmentDO> update(
            @Parameter(description = "考核ID") @PathVariable String id,
            @Parameter(description = "考核信息") @RequestBody MonthlyAssessmentDO assessment) {
        MonthlyAssessmentDO result = monthlyAssessmentService.update(id, assessment);
        return ResultVO.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除考核", description = "根据ID删除月度考核")
    public ResultVO<Void> delete(
            @Parameter(description = "考核ID") @PathVariable String id) {
        monthlyAssessmentService.delete(id);
        return ResultVO.success();
    }

    @PostMapping("/{id}/submit")
    @Operation(summary = "提交考核", description = "提交考核记录")
    public ResultVO<Void> submit(
            @Parameter(description = "考核ID") @PathVariable String id) {
        monthlyAssessmentService.submit(id);
        return ResultVO.success();
    }

    @PostMapping("/{id}/lock")
    @Operation(summary = "锁定考核", description = "锁定考核记录")
    public ResultVO<Void> lock(
            @Parameter(description = "考核ID") @PathVariable String id) {
        monthlyAssessmentService.lock(id);
        return ResultVO.success();
    }

    @GetMapping("/export")
    @Operation(summary = "导出考核", description = "按筛选条件导出考核数据")
    public void exportAssessment(
            @Parameter(description = "年月") @RequestParam(required = false) String yearMonth,
            @Parameter(description = "供应商名称") @RequestParam(required = false) String supplierName,
            @Parameter(description = "分类") @RequestParam(required = false) String category,
            @Parameter(description = "等级") @RequestParam(required = false) String grade,
            @Parameter(description = "状态") @RequestParam(required = false) String status,
            HttpServletResponse response) throws IOException {
        List<MonthlyAssessmentDO> list = monthlyAssessmentService.list(
                yearMonth, supplierName, category, grade, status);
        EasyExcelUtil.writeExcel(response, "assessment_export.xlsx", MonthlyAssessmentDO.class, list);
    }
}