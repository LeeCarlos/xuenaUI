package com.xuena.supplier.service;

import com.xuena.supplier.entity.MeetingNoteDO;

import java.util.List;

public interface MeetingNoteService {

    MeetingNoteDO getById(Long id);

    List<MeetingNoteDO> list(String supplierName);

    MeetingNoteDO create(MeetingNoteDO note);

    MeetingNoteDO update(Long id, MeetingNoteDO note);

    void delete(Long id);
}