package com.xuena.supplier.application.service;

import com.xuena.supplier.domain.entity.SupplierPoolDO;
import com.xuena.supplier.domain.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SupplierPoolServiceTest {

    @Autowired
    private SupplierPoolService supplierPoolService;

    private SupplierPoolDO testSupplier;
    private SupplierPoolDO duplicateSupplier;

    @BeforeEach
    void setUp() {
        long timestamp = System.currentTimeMillis();
        testSupplier = new SupplierPoolDO();
        testSupplier.setName("测试供应商_" + timestamp);
        testSupplier.setCategory("牙刷");
        supplierPoolService.create(testSupplier);

        duplicateSupplier = new SupplierPoolDO();
        duplicateSupplier.setName("重复供应商_" + timestamp);
        duplicateSupplier.setCategory("牙膏");
        supplierPoolService.create(duplicateSupplier);
    }

    @Test
    @DisplayName("获取供应商列表 - 成功")
    void list_Success() {
        List<SupplierPoolDO> list = supplierPoolService.list(null, null, null);

        assertNotNull(list);
        assertTrue(list.size() >= 1);
    }

    @Test
    @DisplayName("按名称搜索供应商 - 成功")
    void list_SearchByName_Success() {
        List<SupplierPoolDO> list = supplierPoolService.list(testSupplier.getName(), null, null);

        assertNotNull(list);
        assertTrue(list.size() >= 1);
        assertTrue(list.stream().anyMatch(s -> s.getName().contains(testSupplier.getName())));
    }

    @Test
    @DisplayName("按类别筛选供应商 - 成功")
    void list_FilterByCategory_Success() {
        List<SupplierPoolDO> list = supplierPoolService.list(null, "牙刷", null);

        assertNotNull(list);
        assertTrue(list.size() >= 1);
        assertTrue(list.stream().allMatch(s -> "牙刷".equals(s.getCategory())));
    }

    @Test
    @DisplayName("获取供应商详情 - 成功")
    void getById_Success() {
        SupplierPoolDO supplier = supplierPoolService.getById(testSupplier.getId());

        assertNotNull(supplier);
        assertEquals(testSupplier.getName(), supplier.getName());
    }

    @Test
    @DisplayName("获取供应商详情 - 不存在")
    void getById_NotFound_ThrowsException() {
        assertThrows(BusinessException.class, () -> supplierPoolService.getById("999999"));
    }

    @Test
    @DisplayName("新增供应商 - 成功")
    void create_Success() {
        SupplierPoolDO supplier = new SupplierPoolDO();
        supplier.setName("新供应商_" + System.currentTimeMillis());
        supplier.setCategory("牙刷");

        SupplierPoolDO result = supplierPoolService.create(supplier);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(0, result.getIsDisabled());
    }

    @Test
    @DisplayName("新增供应商 - 重复名称")
    void create_DuplicateName_ThrowsException() {
        SupplierPoolDO supplier = new SupplierPoolDO();
        supplier.setName(testSupplier.getName());
        supplier.setCategory("牙刷");

        assertThrows(BusinessException.class, () -> supplierPoolService.create(supplier));
    }

    @Test
    @DisplayName("编辑供应商 - 成功")
    void update_Success() {
        String newName = "修改供应商_" + System.currentTimeMillis();
        SupplierPoolDO supplier = new SupplierPoolDO();
        supplier.setName(newName);
        supplier.setCategory("牙膏");

        SupplierPoolDO result = supplierPoolService.update(testSupplier.getId(), supplier);

        assertNotNull(result);
        assertEquals(newName, result.getName());
        assertEquals("牙膏", result.getCategory());
    }

    @Test
    @DisplayName("编辑供应商 - 不存在")
    void update_NotFound_ThrowsException() {
        SupplierPoolDO supplier = new SupplierPoolDO();
        supplier.setName("测试");

        assertThrows(BusinessException.class, () -> supplierPoolService.update("999999", supplier));
    }

    @Test
    @DisplayName("编辑供应商 - 名称重复")
    void update_DuplicateName_ThrowsException() {
        SupplierPoolDO supplier = new SupplierPoolDO();
        supplier.setName(duplicateSupplier.getName());

        assertThrows(BusinessException.class, () -> supplierPoolService.update(testSupplier.getId(), supplier));
    }

    @Test
    @DisplayName("删除供应商 - 成功")
    void delete_Success() {
        String id = testSupplier.getId();
        assertDoesNotThrow(() -> supplierPoolService.delete(id));
        assertThrows(BusinessException.class, () -> supplierPoolService.getById(id));
    }

    @Test
    @DisplayName("删除供应商 - 不存在")
    void delete_NotFound_ThrowsException() {
        assertThrows(BusinessException.class, () -> supplierPoolService.delete("999999"));
    }

    @Test
    @DisplayName("禁用供应商 - 成功")
    void disable_Success() {
        supplierPoolService.disable(testSupplier.getId());

        SupplierPoolDO supplier = supplierPoolService.getById(testSupplier.getId());
        assertEquals(1, supplier.getIsDisabled());
    }

    @Test
    @DisplayName("启用供应商 - 成功")
    void enable_Success() {
        supplierPoolService.disable(testSupplier.getId());
        supplierPoolService.enable(testSupplier.getId());

        SupplierPoolDO supplier = supplierPoolService.getById(testSupplier.getId());
        assertEquals(0, supplier.getIsDisabled());
    }
}
