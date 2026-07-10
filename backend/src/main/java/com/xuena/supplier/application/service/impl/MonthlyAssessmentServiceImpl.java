package com.xuena.supplier.application.service.impl;

import com.xuena.supplier.domain.entity.MonthlyAssessmentDO;
import com.xuena.supplier.domain.exception.BusinessException;
import com.xuena.supplier.infrastructure.mapper.MonthlyAssessmentMapper;
import com.xuena.supplier.application.service.MonthlyAssessmentService;
import com.xuena.supplier.infrastructure.util.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class MonthlyAssessmentServiceImpl implements MonthlyAssessmentService {

    private final MonthlyAssessmentMapper monthlyAssessmentMapper;
    private final IdGenerator idGenerator;

    public MonthlyAssessmentServiceImpl(MonthlyAssessmentMapper monthlyAssessmentMapper, IdGenerator idGenerator) {
        this.monthlyAssessmentMapper = monthlyAssessmentMapper;
        this.idGenerator = idGenerator;
    }

    @Override
    public MonthlyAssessmentDO getById(String id) {
        MonthlyAssessmentDO assessment = monthlyAssessmentMapper.selectById(id);
        if (assessment == null) {
            throw new BusinessException("考核记录不存在");
        }
        return assessment;
    }

    @Override
    public MonthlyAssessmentDO getByYearMonthAndSupplier(String yearMonth, String supplierName) {
        return monthlyAssessmentMapper.selectByYearMonthAndSupplier(yearMonth, supplierName);
    }

    @Override
    public List<MonthlyAssessmentDO> list(String yearMonth, String supplierName, String category,
            String grade, String status) {
        return monthlyAssessmentMapper.selectList(yearMonth, supplierName, category, grade, status);
    }

    @Override
    @Transactional
    public MonthlyAssessmentDO create(MonthlyAssessmentDO assessment) {
        if (monthlyAssessmentMapper.countByYearMonthAndSupplier(
                assessment.getYearMonth(), assessment.getSupplierName()) > 0) {
            throw new BusinessException("该供应商当月考核记录已存在");
        }
        calculateAndSave(assessment);
        return assessment;
    }

    @Override
    @Transactional
    public MonthlyAssessmentDO update(String id, MonthlyAssessmentDO assessment) {
        MonthlyAssessmentDO existing = getById(id);
        if ("LOCKED".equals(existing.getStatus())) {
            throw new BusinessException("已锁定的考核记录不能修改");
        }
        assessment.setId(id);
        calculateAndSave(assessment);
        return assessment;
    }

    @Override
    @Transactional
    public void delete(String id) {
        MonthlyAssessmentDO assessment = getById(id);
        if ("LOCKED".equals(assessment.getStatus())) {
            throw new BusinessException("已锁定的考核记录不能删除");
        }
        monthlyAssessmentMapper.delete(id);
    }

    @Override
    @Transactional
    public void submit(String id) {
        MonthlyAssessmentDO assessment = getById(id);
        if ("LOCKED".equals(assessment.getStatus())) {
            throw new BusinessException("已锁定的考核记录不能提交");
        }
        monthlyAssessmentMapper.updateStatus(id, "SUBMITTED");
    }

    @Override
    @Transactional
    public void lock(String id) {
        MonthlyAssessmentDO assessment = getById(id);
        if ("LOCKED".equals(assessment.getStatus())) {
            throw new BusinessException("考核记录已锁定");
        }
        monthlyAssessmentMapper.updateStatus(id, "LOCKED");
    }

    @Override
    public void calculateAndSave(MonthlyAssessmentDO assessment) {
        BigDecimal total = BigDecimal.ZERO;
        
        if (assessment.getDimensionA() != null) {
            total = total.add(assessment.getDimensionA());
        }
        if (assessment.getDimensionB() != null) {
            total = total.add(assessment.getDimensionB());
        }
        if (assessment.getDimensionC() != null) {
            total = total.add(assessment.getDimensionC());
        }
        if (assessment.getDimensionD() != null) {
            total = total.add(assessment.getDimensionD());
        }
        
        assessment.setTotal(total.setScale(2, RoundingMode.HALF_UP));
        assessment.setGrade(calculateGrade(total));
        
        if (assessment.getId() == null) {
            assessment.setId(idGenerator.generateId());
            assessment.setStatus("DRAFT");
            monthlyAssessmentMapper.insert(assessment);
        } else {
            monthlyAssessmentMapper.update(assessment);
        }
    }

    private String calculateGrade(BigDecimal total) {
        if (total == null) {
            return "";
        }
        if (total.compareTo(new BigDecimal("90")) >= 0) {
            return "A级";
        } else if (total.compareTo(new BigDecimal("80")) >= 0) {
            return "B级";
        } else if (total.compareTo(new BigDecimal("60")) >= 0) {
            return "C级";
        } else {
            return "D级";
        }
    }
}