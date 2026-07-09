package com.xuena.supplier.controller;

import com.xuena.supplier.entity.MonthlyAssessmentDO;
import com.xuena.supplier.service.MonthlyAssessmentService;
import com.xuena.supplier.util.ResultVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessment")
public class AssessmentController {

    private final MonthlyAssessmentService monthlyAssessmentService;

    public AssessmentController(MonthlyAssessmentService monthlyAssessmentService) {
        this.monthlyAssessmentService = monthlyAssessmentService;
    }

    @GetMapping
    public ResultVO<List<MonthlyAssessmentDO>> list(@RequestParam(required = false) String yearMonth,
                                                    @RequestParam(required = false) String supplierName,
                                                    @RequestParam(required = false) String category,
                                                    @RequestParam(required = false) String grade,
                                                    @RequestParam(required = false) String status) {
        List<MonthlyAssessmentDO> list = monthlyAssessmentService.list(yearMonth, supplierName, category, grade, status);
        return ResultVO.success(list);
    }

    @GetMapping("/{id}")
    public ResultVO<MonthlyAssessmentDO> getById(@PathVariable Long id) {
        MonthlyAssessmentDO assessment = monthlyAssessmentService.getById(id);
        return ResultVO.success(assessment);
    }

    @PostMapping
    public ResultVO<MonthlyAssessmentDO> create(@RequestBody MonthlyAssessmentDO assessment) {
        MonthlyAssessmentDO result = monthlyAssessmentService.create(assessment);
        return ResultVO.success(result);
    }

    @PutMapping("/{id}")
    public ResultVO<MonthlyAssessmentDO> update(@PathVariable Long id, @RequestBody MonthlyAssessmentDO assessment) {
        MonthlyAssessmentDO result = monthlyAssessmentService.update(id, assessment);
        return ResultVO.success(result);
    }

    @DeleteMapping("/{id}")
    public ResultVO<Void> delete(@PathVariable Long id) {
        monthlyAssessmentService.delete(id);
        return ResultVO.success();
    }

    @PostMapping("/{id}/submit")
    public ResultVO<Void> submit(@PathVariable Long id) {
        monthlyAssessmentService.submit(id);
        return ResultVO.success();
    }

    @PostMapping("/{id}/lock")
    public ResultVO<Void> lock(@PathVariable Long id) {
        monthlyAssessmentService.lock(id);
        return ResultVO.success();
    }
}