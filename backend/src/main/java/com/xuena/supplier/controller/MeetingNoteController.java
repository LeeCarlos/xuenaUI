package com.xuena.supplier.controller;

import com.xuena.supplier.entity.MeetingNoteDO;
import com.xuena.supplier.service.MeetingNoteService;
import com.xuena.supplier.vo.ResultVO;
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
public class MeetingNoteController {

    private final MeetingNoteService meetingNoteService;

    public MeetingNoteController(MeetingNoteService meetingNoteService) {
        this.meetingNoteService = meetingNoteService;
    }

    @GetMapping
    public ResultVO<List<MeetingNoteDO>> list(@RequestParam(required = false) String supplierName) {
        List<MeetingNoteDO> list = meetingNoteService.list(supplierName);
        return ResultVO.success(list);
    }

    @GetMapping("/{id}")
    public ResultVO<MeetingNoteDO> getById(@PathVariable Long id) {
        MeetingNoteDO note = meetingNoteService.getById(id);
        return ResultVO.success(note);
    }

    @PostMapping
    public ResultVO<MeetingNoteDO> create(@RequestBody MeetingNoteDO note) {
        MeetingNoteDO result = meetingNoteService.create(note);
        return ResultVO.success(result);
    }

    @PutMapping("/{id}")
    public ResultVO<MeetingNoteDO> update(@PathVariable Long id, @RequestBody MeetingNoteDO note) {
        MeetingNoteDO result = meetingNoteService.update(id, note);
        return ResultVO.success(result);
    }

    @DeleteMapping("/{id}")
    public ResultVO<Void> delete(@PathVariable Long id) {
        meetingNoteService.delete(id);
        return ResultVO.success();
    }
}