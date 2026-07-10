package com.xuena.supplier.controller;

import com.xuena.supplier.service.SummaryService;
import com.xuena.supplier.vo.ResultVO;
import com.xuena.supplier.vo.response.SummaryVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @GetMapping
    public ResultVO<SummaryVO> getSummary(@RequestParam(value = "yearMonth", required = false) String yearMonth) {
        SummaryVO data = summaryService.getSummary(yearMonth);
        return ResultVO.success(data);
    }
}
