package com.xuena.supplier.interfaces.controller;

import com.xuena.supplier.domain.entity.MeetingNoteDO;
import com.xuena.supplier.application.service.MeetingNoteService;
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
@RequestMapping("/api/meeting-note")
@Tag(name = "会议纪要", description = "会议纪要相关接口")
public class MeetingNoteController {

    private final MeetingNoteService meetingNoteService;

    public MeetingNoteController(MeetingNoteService meetingNoteService) {
        this.meetingNoteService = meetingNoteService;
    }

    @GetMapping
    @Operation(summary = "查询会议纪要列表", description = "根据条件查询会议纪要列表")
    public ResultVO<List<MeetingNoteDO>> list(
            @Parameter(description = "供应商名称") @RequestParam(required = false) String supplierName) {
        List<MeetingNoteDO> list = meetingNoteService.list(supplierName);
        return ResultVO.success(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询会议纪要详情", description = "根据ID查询会议纪要详情")
    public ResultVO<MeetingNoteDO> getById(
            @Parameter(description = "纪要ID") @PathVariable String id) {
        MeetingNoteDO note = meetingNoteService.getById(id);
        return ResultVO.success(note);
    }

    @PostMapping
    @Operation(summary = "创建会议纪要", description = "新增会议纪要")
    public ResultVO<MeetingNoteDO> create(
            @Parameter(description = "纪要信息") @RequestBody MeetingNoteDO note) {
        MeetingNoteDO result = meetingNoteService.create(note);
        return ResultVO.success(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新会议纪要", description = "根据ID更新会议纪要")
    public ResultVO<MeetingNoteDO> update(
            @Parameter(description = "纪要ID") @PathVariable String id,
            @Parameter(description = "纪要信息") @RequestBody MeetingNoteDO note) {
        MeetingNoteDO result = meetingNoteService.update(id, note);
        return ResultVO.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除会议纪要", description = "根据ID删除会议纪要")
    public ResultVO<Void> delete(
            @Parameter(description = "纪要ID") @PathVariable String id) {
        meetingNoteService.delete(id);
        return ResultVO.success();
    }
}