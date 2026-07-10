package com.xuena.supplier.application.service.impl;

import com.xuena.supplier.domain.entity.MeetingNoteDO;
import com.xuena.supplier.domain.exception.BusinessException;
import com.xuena.supplier.infrastructure.mapper.MeetingNoteMapper;
import com.xuena.supplier.application.service.MeetingNoteService;
import com.xuena.supplier.infrastructure.util.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MeetingNoteServiceImpl implements MeetingNoteService {

    private final MeetingNoteMapper meetingNoteMapper;
    private final IdGenerator idGenerator;

    public MeetingNoteServiceImpl(MeetingNoteMapper meetingNoteMapper, IdGenerator idGenerator) {
        this.meetingNoteMapper = meetingNoteMapper;
        this.idGenerator = idGenerator;
    }

    @Override
    public MeetingNoteDO getById(String id) {
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
        note.setId(idGenerator.generateId());
        meetingNoteMapper.insert(note);
        return note;
    }

    @Override
    @Transactional
    public MeetingNoteDO update(String id, MeetingNoteDO note) {
        getById(id);
        note.setId(id);
        meetingNoteMapper.update(note);
        return note;
    }

    @Override
    @Transactional
    public void delete(String id) {
        getById(id);
        meetingNoteMapper.delete(id);
    }
}