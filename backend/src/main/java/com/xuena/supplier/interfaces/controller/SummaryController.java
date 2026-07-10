package com.xuena.supplier.interfaces.controller;

import com.xuena.supplier.application.service.SummaryService;
import com.xuena.supplier.interfaces.vo.ResultVO;
import com.xuena.supplier.interfaces.vo.response.SummaryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/summary")
@Tag(name = "统计汇总", description = "统计汇总相关接口")
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @GetMapping
    @Operation(summary = "获取统计汇总", description = "获取供应商绩效统计汇总信息")
    public ResultVO<SummaryVO> getSummary(
            @Parameter(description = "年月") @RequestParam(value = "yearMonth", required = false) String yearMonth) {
        SummaryVO data = summaryService.getSummary(yearMonth);
        return ResultVO.success(data);
    }
}
