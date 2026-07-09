package com.xuena.supplier.service.impl;

import com.xuena.supplier.entity.DepartmentScoreDO;
import com.xuena.supplier.exception.BusinessException;
import com.xuena.supplier.mapper.DepartmentScoreMapper;
import com.xuena.supplier.service.DepartmentScoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentScoreServiceImpl implements DepartmentScoreService {

    private final DepartmentScoreMapper departmentScoreMapper;

    public DepartmentScoreServiceImpl(DepartmentScoreMapper departmentScoreMapper) {
        this.departmentScoreMapper = departmentScoreMapper;
    }

    @Override
    public DepartmentScoreDO getById(Long id) {
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
        score.setStatus("PENDING");
        departmentScoreMapper.insert(score);
        return score;
    }

    @Override
    @Transactional
    public DepartmentScoreDO update(Long id, DepartmentScoreDO score) {
        DepartmentScoreDO existing = getById(id);
        if ("COMPLETED".equals(existing.getStatus())) {
            throw new BusinessException("已完成的打分记录不能修改");
        }
        score.setId(id);
        departmentScoreMapper.update(score);
        return score;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        DepartmentScoreDO score = getById(id);
        if ("COMPLETED".equals(score.getStatus())) {
            throw new BusinessException("已完成的打分记录不能删除");
        }
        departmentScoreMapper.delete(id);
    }

    @Override
    @Transactional
    public void submit(Long id) {
        DepartmentScoreDO score = getById(id);
        if ("COMPLETED".equals(score.getStatus())) {
            throw new BusinessException("已完成的打分记录不能提交");
        }
        departmentScoreMapper.updateStatus(id, "IN_PROGRESS");
    }

    @Override
    @Transactional
    public void complete(Long id) {
        DepartmentScoreDO score = getById(id);
        if ("COMPLETED".equals(score.getStatus())) {
            throw new BusinessException("打分记录已完成");
        }
        departmentScoreMapper.updateStatus(id, "COMPLETED");
    }
}