package com.xuena.supplier.service;

import com.xuena.supplier.entity.SupplierPoolDO;
import com.xuena.supplier.exception.BusinessException;
import com.xuena.supplier.service.impl.SupplierPoolServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SupplierPoolServiceTest {

    @Autowired
    private SupplierPoolService supplierPoolService;

    @Test
    @DisplayName("获取供应商列表 - 成功")
    void list_Success() {
        List<SupplierPoolDO> list = supplierPoolService.list(null, null, null);
        
        assertNotNull(list);
        assertTrue(list.size() >= 2);
    }

    @Test
    @DisplayName("按名称搜索供应商 - 成功")
    void list_SearchByName_Success() {
        List<SupplierPoolDO> list = supplierPoolService.list("供应商A", null, null);
        
        assertNotNull(list);
        assertTrue(list.size() >= 1);
        assertTrue(list.stream().anyMatch(s -> s.getName().contains("供应商A")));
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
        SupplierPoolDO supplier = supplierPoolService.getById(1L);
        
        assertNotNull(supplier);
        assertEquals("供应商A", supplier.getName());
    }

    @Test
    @DisplayName("获取供应商详情 - 不存在")
    void getById_NotFound_ThrowsException() {
        assertThrows(BusinessException.class, () -> supplierPoolService.getById(999L));
    }

    @Test
    @DisplayName("新增供应商 - 成功")
    void create_Success() {
        SupplierPoolDO supplier = new SupplierPoolDO();
        supplier.setName("供应商C");
        supplier.setCategory("牙刷");
        
        SupplierPoolDO result = supplierPoolService.create(supplier);
        
        assertNotNull(result);
        assertEquals("供应商C", result.getName());
        assertEquals(0, result.getIsDisabled());
    }

    @Test
    @DisplayName("新增供应商 - 重复名称")
    void create_DuplicateName_ThrowsException() {
        SupplierPoolDO supplier = new SupplierPoolDO();
        supplier.setName("供应商A");
        supplier.setCategory("牙刷");
        
        assertThrows(BusinessException.class, () -> supplierPoolService.create(supplier));
    }

    @Test
    @DisplayName("编辑供应商 - 成功")
    void update_Success() {
        SupplierPoolDO supplier = new SupplierPoolDO();
        supplier.setName("供应商A-修改");
        supplier.setCategory("牙膏");
        
        SupplierPoolDO result = supplierPoolService.update(1L, supplier);
        
        assertNotNull(result);
        assertEquals("供应商A-修改", result.getName());
        assertEquals("牙膏", result.getCategory());
    }

    @Test
    @DisplayName("编辑供应商 - 不存在")
    void update_NotFound_ThrowsException() {
        SupplierPoolDO supplier = new SupplierPoolDO();
        supplier.setName("测试");
        
        assertThrows(BusinessException.class, () -> supplierPoolService.update(999L, supplier));
    }

    @Test
    @DisplayName("编辑供应商 - 名称重复")
    void update_DuplicateName_ThrowsException() {
        SupplierPoolDO supplier = new SupplierPoolDO();
        supplier.setName("供应商B");
        
        assertThrows(BusinessException.class, () -> supplierPoolService.update(1L, supplier));
    }

    @Test
    @DisplayName("删除供应商 - 成功")
    void delete_Success() {
        assertDoesNotThrow(() -> supplierPoolService.delete(1L));
        assertThrows(BusinessException.class, () -> supplierPoolService.getById(1L));
    }

    @Test
    @DisplayName("删除供应商 - 不存在")
    void delete_NotFound_ThrowsException() {
        assertThrows(BusinessException.class, () -> supplierPoolService.delete(999L));
    }

    @Test
    @DisplayName("禁用供应商 - 成功")
    void disable_Success() {
        supplierPoolService.disable(1L);
        
        SupplierPoolDO supplier = supplierPoolService.getById(1L);
        assertEquals(1, supplier.getIsDisabled());
    }

    @Test
    @DisplayName("启用供应商 - 成功")
    void enable_Success() {
        supplierPoolService.disable(1L);
        supplierPoolService.enable(1L);
        
        SupplierPoolDO supplier = supplierPoolService.getById(1L);
        assertEquals(0, supplier.getIsDisabled());
    }
}