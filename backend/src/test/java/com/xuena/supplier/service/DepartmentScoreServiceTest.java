package com.xuena.supplier.service;

import com.xuena.supplier.entity.DepartmentScoreDO;
import com.xuena.supplier.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class DepartmentScoreServiceTest {

    @Autowired
    private DepartmentScoreService departmentScoreService;

    private DepartmentScoreDO createTestScore() {
        DepartmentScoreDO score = new DepartmentScoreDO();
        score.setYearMonth("2026-03");
        score.setSupplierName("供应商_" + System.currentTimeMillis());
        score.setDepartment("质量");
        score.setDimensionGroup("A");
        score.setDimensionScore(new BigDecimal("20"));
        return departmentScoreService.create(score);
    }

    @Test
    @DisplayName("创建部门打分记录 - 成功")
    void create_Success() {
        DepartmentScoreDO score = new DepartmentScoreDO();
        score.setYearMonth("2026-03");
        score.setSupplierName("供应商_" + System.currentTimeMillis());
        score.setDepartment("质量");
        score.setDimensionGroup("A");
        score.setDimensionScore(new BigDecimal("20"));

        DepartmentScoreDO result = departmentScoreService.create(score);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("PENDING", result.getStatus());
    }

    @Test
    @DisplayName("获取部门打分记录列表 - 成功")
    void list_Success() {
        createTestScore();

        List<DepartmentScoreDO> list = departmentScoreService.list(null, null, null);

        assertNotNull(list);
        assertTrue(list.size() >= 1);
    }

    @Test
    @DisplayName("获取部门打分记录详情 - 成功")
    void getById_Success() {
        DepartmentScoreDO created = createTestScore();

        DepartmentScoreDO result = departmentScoreService.getById(created.getId());

        assertNotNull(result);
        assertEquals(created.getSupplierName(), result.getSupplierName());
    }

    @Test
    @DisplayName("获取部门打分记录详情 - 不存在")
    void getById_NotFound_ThrowsException() {
        assertThrows(BusinessException.class, () -> departmentScoreService.getById(999999L));
    }

    @Test
    @DisplayName("更新部门打分记录 - 成功")
    void update_Success() {
        DepartmentScoreDO created = createTestScore();

        DepartmentScoreDO update = new DepartmentScoreDO();
        update.setYearMonth("2026-03");
        update.setSupplierName(created.getSupplierName());
        update.setDepartment("计划");

        DepartmentScoreDO result = departmentScoreService.update(created.getId(), update);

        assertNotNull(result);
        assertEquals("计划", result.getDepartment());
    }

    @Test
    @DisplayName("更新部门打分记录 - 已完成")
    void update_Completed_ThrowsException() {
        DepartmentScoreDO created = createTestScore();
        departmentScoreService.complete(created.getId());

        DepartmentScoreDO update = new DepartmentScoreDO();
        update.setDepartment("计划");

        assertThrows(BusinessException.class, () -> departmentScoreService.update(created.getId(), update));
    }

    @Test
    @DisplayName("删除部门打分记录 - 成功")
    void delete_Success() {
        DepartmentScoreDO created = createTestScore();
        Long id = created.getId();

        assertDoesNotThrow(() -> departmentScoreService.delete(id));
        assertThrows(BusinessException.class, () -> departmentScoreService.getById(id));
    }

    @Test
    @DisplayName("删除部门打分记录 - 已完成")
    void delete_Completed_ThrowsException() {
        DepartmentScoreDO created = createTestScore();
        departmentScoreService.complete(created.getId());

        assertThrows(BusinessException.class, () -> departmentScoreService.delete(created.getId()));
    }

    @Test
    @DisplayName("提交部门打分记录 - 成功")
    void submit_Success() {
        DepartmentScoreDO created = createTestScore();
        departmentScoreService.submit(created.getId());

        DepartmentScoreDO result = departmentScoreService.getById(created.getId());
        assertEquals("IN_PROGRESS", result.getStatus());
    }

    @Test
    @DisplayName("提交部门打分记录 - 已完成")
    void submit_Completed_ThrowsException() {
        DepartmentScoreDO created = createTestScore();
        departmentScoreService.complete(created.getId());

        assertThrows(BusinessException.class, () -> departmentScoreService.submit(created.getId()));
    }

    @Test
    @DisplayName("完成部门打分记录 - 成功")
    void complete_Success() {
        DepartmentScoreDO created = createTestScore();
        departmentScoreService.complete(created.getId());

        DepartmentScoreDO result = departmentScoreService.getById(created.getId());
        assertEquals("COMPLETED", result.getStatus());
    }

    @Test
    @DisplayName("完成部门打分记录 - 已完成")
    void complete_AlreadyCompleted_ThrowsException() {
        DepartmentScoreDO created = createTestScore();
        departmentScoreService.complete(created.getId());

        assertThrows(BusinessException.class, () -> departmentScoreService.complete(created.getId()));
    }
}
