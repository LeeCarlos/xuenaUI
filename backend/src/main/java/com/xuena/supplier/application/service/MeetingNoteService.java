package com.xuena.supplier.application.service;

import com.xuena.supplier.domain.entity.MeetingNoteDO;

import java.util.List;

public interface MeetingNoteService {

    MeetingNoteDO getById(String id);

    List<MeetingNoteDO> list(String supplierName, String monthFrom, String monthTo);

    MeetingNoteDO create(MeetingNoteDO note);

    MeetingNoteDO update(String id, MeetingNoteDO note);

    void delete(String id);
}