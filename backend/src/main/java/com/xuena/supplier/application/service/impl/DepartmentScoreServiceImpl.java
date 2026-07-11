package com.xuena.supplier.application.service.impl;

import com.xuena.supplier.domain.entity.DepartmentScoreDO;
import com.xuena.supplier.domain.exception.BusinessException;
import com.xuena.supplier.infrastructure.mapper.DepartmentScoreMapper;
import com.xuena.supplier.application.service.DepartmentScoreService;
import com.xuena.supplier.infrastructure.util.IdGenerator;
import com.xuena.supplier.infrastructure.util.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentScoreServiceImpl implements DepartmentScoreService {

    private final DepartmentScoreMapper departmentScoreMapper;
    private final IdGenerator idGenerator;

    public DepartmentScoreServiceImpl(DepartmentScoreMapper departmentScoreMapper, IdGenerator idGenerator) {
        this.departmentScoreMapper = departmentScoreMapper;
        this.idGenerator = idGenerator;
    }

    @Override
    public DepartmentScoreDO getById(String id) {
        DepartmentScoreDO score = departmentScoreMapper.selectById(id);
        if (score == null) {
            throw new BusinessException("部门打分记录不存在");
        }
        return score;
    }

    @Override
    public List<DepartmentScoreDO> list(String yearMonth, String supplierName, String department) {
        return departmentScoreMapper.selectList(yearMonth, supplierName, department);
    }

    @Override
    @Transactional
    public DepartmentScoreDO create(DepartmentScoreDO score) {
        score.setId(idGenerator.generateId());
        score.setStatus("PENDING");
        score.setCreateId(UserContext.getUserId());
        score.setCreateName(UserContext.getUserName());
        departmentScoreMapper.insert(score);
        return score;
    }

    @Override
    @Transactional
    public DepartmentScoreDO update(String id, DepartmentScoreDO score) {
        DepartmentScoreDO existing = getById(id);
        if ("COMPLETED".equals(existing.getStatus())) {
            throw new BusinessException("已完成的打分记录不能修改");
        }
        score.setId(id);
        score.setUpdateId(UserContext.getUserId());
        score.setUpdateName(UserContext.getUserName());
        departmentScoreMapper.update(score);
        return score;
    }

    @Override
    @Transactional
    public void delete(String id) {
        DepartmentScoreDO score = getById(id);
        if ("COMPLETED".equals(score.getStatus())) {
            throw new BusinessException("已完成的打分记录不能删除");
        }
        departmentScoreMapper.delete(id);
    }

    @Override
    @Transactional
    public void submit(String id) {
        DepartmentScoreDO score = getById(id);
        if ("COMPLETED".equals(score.getStatus())) {
            throw new BusinessException("已完成的打分记录不能提交");
        }
        departmentScoreMapper.updateStatus(id, "IN_PROGRESS");
    }

    @Override
    @Transactional
    public void complete(String id) {
        DepartmentScoreDO score = getById(id);
        if ("COMPLETED".equals(score.getStatus())) {
            throw new BusinessException("打分记录已完成");
        }
        departmentScoreMapper.updateStatus(id, "COMPLETED");
    }
}