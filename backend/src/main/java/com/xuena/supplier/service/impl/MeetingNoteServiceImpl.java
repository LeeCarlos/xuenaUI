package com.xuena.supplier.service.impl;

import com.xuena.supplier.entity.MeetingNoteDO;
import com.xuena.supplier.exception.BusinessException;
import com.xuena.supplier.mapper.MeetingNoteMapper;
import com.xuena.supplier.service.MeetingNoteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MeetingNoteServiceImpl implements MeetingNoteService {

    private final MeetingNoteMapper meetingNoteMapper;

    public MeetingNoteServiceImpl(MeetingNoteMapper meetingNoteMapper) {
        this.meetingNoteMapper = meetingNoteMapper;
    }

    @Override
    public MeetingNoteDO getById(Long id) {
        MeetingNoteDO note = meetingNoteMapper.selectById(id);
        if (note == null) {
            throw new BusinessException("会议纪要不存在");
        }
        return note;
    }

    @Override
    public List<MeetingNoteDO> list(String supplierName) {
        return meetingNoteMapper.selectList(supplierName);
    }

    @Override
    @Transactional
    public MeetingNoteDO create(MeetingNoteDO note) {
        meetingNoteMapper.insert(note);
        return note;
    }

    @Override
    @Transactional
    public MeetingNoteDO update(Long id, MeetingNoteDO note) {
        getById(id);
        note.setId(id);
        meetingNoteMapper.update(note);
        return note;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        getById(id);
        meetingNoteMapper.delete(id);
    }
}