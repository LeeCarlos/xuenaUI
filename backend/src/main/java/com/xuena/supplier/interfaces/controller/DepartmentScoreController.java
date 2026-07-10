package com.xuena.supplier.interfaces.controller;

import com.xuena.supplier.domain.entity.DepartmentScoreDO;
import com.xuena.supplier.application.service.DepartmentScoreService;
import com.xuena.supplier.interfaces.vo.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/department-score")
@Tag(name = "部门打分", description = "部门打分相关接口")
public class DepartmentScoreController {

    private final DepartmentScoreService departmentScoreService;

    public DepartmentScoreController(DepartmentScoreService departmentScoreService) {
        this.departmentScoreService = departmentScoreService;
    }

    @GetMapping
    @Operation(summary = "查询部门打分列表", description = "根据条件查询部门打分记录列表")
    public ResultVO<List<DepartmentScoreDO>> list(
            @Parameter(description = "年月") @RequestParam(required = false) String yearMonth,
            @Parameter(description = "供应商名称") @RequestParam(required = false) String supplierName,
            @Parameter(description = "部门") @RequestParam(required = false) String department) {
        List<DepartmentScoreDO> list = departmentScoreService.list(yearMonth, supplierName, department);
        return ResultVO.success(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询部门打分详情", description = "根据ID查询部门打分详情")
    public ResultVO<DepartmentScoreDO> getById(
            @Parameter(description = "打分ID") @PathVariable String id) {
        DepartmentScoreDO score = departmentScoreService.getById(id);
        return ResultVO.success(score);
    }

    @PostMapping
    @Operation(summary = "创建部门打分", description = "新增部门打分记录")
    public ResultVO<DepartmentScoreDO> create(
            @Parameter(description = "打分信息") @RequestBody DepartmentScoreDO score) {
        DepartmentScoreDO result = departmentScoreService.create(score);
        return ResultVO.success(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新部门打分", description = "根据ID更新部门打分信息")
    public ResultVO<DepartmentScoreDO> update(
            @Parameter(description = "打分ID") @PathVariable String id,
            @Parameter(description = "打分信息") @RequestBody DepartmentScoreDO score) {
        DepartmentScoreDO result = departmentScoreService.update(id, score);
        return ResultVO.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除部门打分", description = "根据ID删除部门打分")
    public ResultVO<Void> delete(
            @Parameter(description = "打分ID") @PathVariable String id) {
        departmentScoreService.delete(id);
        return ResultVO.success();
    }

    @PostMapping("/{id}/submit")
    @Operation(summary = "提交部门打分", description = "提交部门打分记录")
    public ResultVO<Void> submit(
            @Parameter(description = "打分ID") @PathVariable String id) {
        departmentScoreService.submit(id);
        return ResultVO.success();
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "完成部门打分", description = "完成部门打分记录")
    public ResultVO<Void> complete(
            @Parameter(description = "打分ID") @PathVariable String id) {
        departmentScoreService.complete(id);
        return ResultVO.success();
    }
}