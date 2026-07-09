package com.xuena.supplier.controller;

import com.xuena.supplier.entity.DepartmentScoreDO;
import com.xuena.supplier.service.DepartmentScoreService;
import com.xuena.supplier.util.ResultVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department-score")
public class DepartmentScoreController {

    private final DepartmentScoreService departmentScoreService;

    public DepartmentScoreController(DepartmentScoreService departmentScoreService) {
        this.departmentScoreService = departmentScoreService;
    }

    @GetMapping
    public ResultVO<List<DepartmentScoreDO>> list(@RequestParam(required = false) String yearMonth,
                                                   @RequestParam(required = false) String supplierName,
                                                   @RequestParam(required = false) String department) {
        List<DepartmentScoreDO> list = departmentScoreService.list(yearMonth, supplierName, department);
        return ResultVO.success(list);
    }

    @GetMapping("/{id}")
    public ResultVO<DepartmentScoreDO> getById(@PathVariable Long id) {
        DepartmentScoreDO score = departmentScoreService.getById(id);
        return ResultVO.success(score);
    }

    @PostMapping
    public ResultVO<DepartmentScoreDO> create(@RequestBody DepartmentScoreDO score) {
        DepartmentScoreDO result = departmentScoreService.create(score);
        return ResultVO.success(result);
    }

    @PutMapping("/{id}")
    public ResultVO<DepartmentScoreDO> update(@PathVariable Long id, @RequestBody DepartmentScoreDO score) {
        DepartmentScoreDO result = departmentScoreService.update(id, score);
        return ResultVO.success(result);
    }

    @DeleteMapping("/{id}")
    public ResultVO<Void> delete(@PathVariable Long id) {
        departmentScoreService.delete(id);
        return ResultVO.success();
    }

    @PostMapping("/{id}/submit")
    public ResultVO<Void> submit(@PathVariable Long id) {
        departmentScoreService.submit(id);
        return ResultVO.success();
    }

    @PostMapping("/{id}/complete")
    public ResultVO<Void> complete(@PathVariable Long id) {
        departmentScoreService.complete(id);
        return ResultVO.success();
    }
}