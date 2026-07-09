package com.xuena.supplier.service;

import com.xuena.supplier.entity.MonthlyAssessmentDO;
import com.xuena.supplier.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MonthlyAssessmentServiceTest {

    @Autowired
    private MonthlyAssessmentService monthlyAssessmentService;

    @Test
    @DisplayName("创建考核记录 - 成功")
    void create_Success() {
        MonthlyAssessmentDO assessment = new MonthlyAssessmentDO();
        assessment.setYearMonth("2026-03");
        assessment.setSupplierName("供应商A");
        assessment.setCategory("牙刷");
        assessment.setDimensionA(new BigDecimal("20"));
        assessment.setDimensionB(new BigDecimal("18"));
        assessment.setDimensionC(new BigDecimal("18"));
        assessment.setDimensionD(new BigDecimal("30"));
        
        MonthlyAssessmentDO result = monthlyAssessmentService.create(assessment);
        
        assertNotNull(result);
        assertEquals("2026-03", result.getYearMonth());
        assertEquals("供应商A", result.getSupplierName());
        assertEquals(new BigDecimal("86.00"), result.getTotal());
        assertEquals("B级", result.getGrade());
        assertEquals("DRAFT", result.getStatus());
    }

    @Test
    @DisplayName("创建考核记录 - 重复数据")
    void create_Duplicate_ThrowsException() {
        MonthlyAssessmentDO assessment1 = new MonthlyAssessmentDO();
        assessment1.setYearMonth("2026-03");
        assessment1.setSupplierName("供应商A");
        monthlyAssessmentService.create(assessment1);
        
        MonthlyAssessmentDO assessment2 = new MonthlyAssessmentDO();
        assessment2.setYearMonth("2026-03");
        assessment2.setSupplierName("供应商A");
        
        assertThrows(BusinessException.class, () -> monthlyAssessmentService.create(assessment2));
    }

    @Test
    @DisplayName("获取考核记录列表 - 成功")
    void list_Success() {
        MonthlyAssessmentDO assessment = new MonthlyAssessmentDO();
        assessment.setYearMonth("2026-03");
        assessment.setSupplierName("供应商A");
        monthlyAssessmentService.create(assessment);
        
        List<MonthlyAssessmentDO> list = monthlyAssessmentService.list(null, null, null, null, null);
        
        assertNotNull(list);
        assertTrue(list.size() >= 1);
    }

    @Test
    @DisplayName("获取考核记录详情 - 成功")
    void getById_Success() {
        MonthlyAssessmentDO assessment = new MonthlyAssessmentDO();
        assessment.setYearMonth("2026-03");
        assessment.setSupplierName("供应商A");
        MonthlyAssessmentDO created = monthlyAssessmentService.create(assessment);
        
        MonthlyAssessmentDO result = monthlyAssessmentService.getById(created.getId());
        
        assertNotNull(result);
        assertEquals("供应商A", result.getSupplierName());
    }

    @Test
    @DisplayName("获取考核记录详情 - 不存在")
    void getById_NotFound_ThrowsException() {
        assertThrows(BusinessException.class, () -> monthlyAssessmentService.getById(999L));
    }

    @Test
    @DisplayName("更新考核记录 - 成功")
    void update_Success() {
        MonthlyAssessmentDO assessment = new MonthlyAssessmentDO();
        assessment.setYearMonth("2026-03");
        assessment.setSupplierName("供应商A");
        MonthlyAssessmentDO created = monthlyAssessmentService.create(assessment);
        
        MonthlyAssessmentDO update = new MonthlyAssessmentDO();
        update.setYearMonth("2026-03");
        update.setSupplierName("供应商A");
        update.setCategory("牙膏");
        
        MonthlyAssessmentDO result = monthlyAssessmentService.update(created.getId(), update);
        
        assertNotNull(result);
        assertEquals("牙膏", result.getCategory());
    }

    @Test
    @DisplayName("更新考核记录 - 已锁定")
    void update_Locked_ThrowsException() {
        MonthlyAssessmentDO assessment = new MonthlyAssessmentDO();
        assessment.setYearMonth("2026-03");
        assessment.setSupplierName("供应商A");
        MonthlyAssessmentDO created = monthlyAssessmentService.create(assessment);
        
        monthlyAssessmentService.lock(created.getId());
        
        MonthlyAssessmentDO update = new MonthlyAssessmentDO();
        update.setSupplierName("测试");
        
        assertThrows(BusinessException.class, () -> monthlyAssessmentService.update(created.getId(), update));
    }

    @Test
    @DisplayName("删除考核记录 - 成功")
    void delete_Success() {
        MonthlyAssessmentDO assessment = new MonthlyAssessmentDO();
        assessment.setYearMonth("2026-03");
        assessment.setSupplierName("供应商A");
        MonthlyAssessmentDO created = monthlyAssessmentService.create(assessment);
        
        assertDoesNotThrow(() -> monthlyAssessmentService.delete(created.getId()));
        assertThrows(BusinessException.class, () -> monthlyAssessmentService.getById(created.getId()));
    }

    @Test
    @DisplayName("删除考核记录 - 已锁定")
    void delete_Locked_ThrowsException() {
        MonthlyAssessmentDO assessment = new MonthlyAssessmentDO();
        assessment.setYearMonth("2026-03");
        assessment.setSupplierName("供应商A");
        MonthlyAssessmentDO created = monthlyAssessmentService.create(assessment);
        
        monthlyAssessmentService.lock(created.getId());
        
        assertThrows(BusinessException.class, () -> monthlyAssessmentService.delete(created.getId()));
    }

    @Test
    @DisplayName("提交考核记录 - 成功")
    void submit_Success() {
        MonthlyAssessmentDO assessment = new MonthlyAssessmentDO();
        assessment.setYearMonth("2026-03");
        assessment.setSupplierName("供应商A");
        MonthlyAssessmentDO created = monthlyAssessmentService.create(assessment);
        
        monthlyAssessmentService.submit(created.getId());
        
        MonthlyAssessmentDO result = monthlyAssessmentService.getById(created.getId());
        assertEquals("SUBMITTED", result.getStatus());
    }

    @Test
    @DisplayName("提交考核记录 - 已锁定")
    void submit_Locked_ThrowsException() {
        MonthlyAssessmentDO assessment = new MonthlyAssessmentDO();
        assessment.setYearMonth("2026-03");
        assessment.setSupplierName("供应商A");
        MonthlyAssessmentDO created = monthlyAssessmentService.create(assessment);
        
        monthlyAssessmentService.lock(created.getId());
        
        assertThrows(BusinessException.class, () -> monthlyAssessmentService.submit(created.getId()));
    }

    @Test
    @DisplayName("锁定考核记录 - 成功")
    void lock_Success() {
        MonthlyAssessmentDO assessment = new MonthlyAssessmentDO();
        assessment.setYearMonth("2026-03");
        assessment.setSupplierName("供应商A");
        MonthlyAssessmentDO created = monthlyAssessmentService.create(assessment);
        
        monthlyAssessmentService.lock(created.getId());
        
        MonthlyAssessmentDO result = monthlyAssessmentService.getById(created.getId());
        assertEquals("LOCKED", result.getStatus());
    }

    @Test
    @DisplayName("锁定考核记录 - 已锁定")
    void lock_AlreadyLocked_ThrowsException() {
        MonthlyAssessmentDO assessment = new MonthlyAssessmentDO();
        assessment.setYearMonth("2026-03");
        assessment.setSupplierName("供应商A");
        MonthlyAssessmentDO created = monthlyAssessmentService.create(assessment);
        
        monthlyAssessmentService.lock(created.getId());
        
        assertThrows(BusinessException.class, () -> monthlyAssessmentService.lock(created.getId()));
    }

    @Test
    @DisplayName("计算总分 - A级(90分以上)")
    void calculateGrade_A() {
        MonthlyAssessmentDO assessment = new MonthlyAssessmentDO();
        assessment.setYearMonth("2026-03");
        assessment.setSupplierName("供应商A");
        assessment.setDimensionA(new BigDecimal("25"));
        assessment.setDimensionB(new BigDecimal("20"));
        assessment.setDimensionC(new BigDecimal("20"));
        assessment.setDimensionD(new BigDecimal("35"));
        
        MonthlyAssessmentDO result = monthlyAssessmentService.create(assessment);
        
        assertEquals(new BigDecimal("100.00"), result.getTotal());
        assertEquals("A级", result.getGrade());
    }

    @Test
    @DisplayName("计算总分 - B级(80-89分)")
    void calculateGrade_B() {
        MonthlyAssessmentDO assessment = new MonthlyAssessmentDO();
        assessment.setYearMonth("2026-03");
        assessment.setSupplierName("供应商A");
        assessment.setDimensionA(new BigDecimal("22"));
        assessment.setDimensionB(new BigDecimal("18"));
        assessment.setDimensionC(new BigDecimal("18"));
        assessment.setDimensionD(new BigDecimal("25"));
        
        MonthlyAssessmentDO result = monthlyAssessmentService.create(assessment);
        
        assertEquals(new BigDecimal("83.00"), result.getTotal());
        assertEquals("B级", result.getGrade());
    }

    @Test
    @DisplayName("计算总分 - C级(60-79分)")
    void calculateGrade_C() {
        MonthlyAssessmentDO assessment = new MonthlyAssessmentDO();
        assessment.setYearMonth("2026-03");
        assessment.setSupplierName("供应商A");
        assessment.setDimensionA(new BigDecimal("18"));
        assessment.setDimensionB(new BigDecimal("15"));
        assessment.setDimensionC(new BigDecimal("15"));
        assessment.setDimensionD(new BigDecimal("15"));
        
        MonthlyAssessmentDO result = monthlyAssessmentService.create(assessment);
        
        assertEquals(new BigDecimal("63.00"), result.getTotal());
        assertEquals("C级", result.getGrade());
    }

    @Test
    @DisplayName("计算总分 - D级(60分以下)")
    void calculateGrade_D() {
        MonthlyAssessmentDO assessment = new MonthlyAssessmentDO();
        assessment.setYearMonth("2026-03");
        assessment.setSupplierName("供应商A");
        assessment.setDimensionA(new BigDecimal("10"));
        assessment.setDimensionB(new BigDecimal("10"));
        assessment.setDimensionC(new BigDecimal("10"));
        assessment.setDimensionD(new BigDecimal("10"));
        
        MonthlyAssessmentDO result = monthlyAssessmentService.create(assessment);
        
        assertEquals(new BigDecimal("40.00"), result.getTotal());
        assertEquals("D级", result.getGrade());
    }
}