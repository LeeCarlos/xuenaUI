package com.xuena.supplier.service;

import com.xuena.supplier.entity.CategoryDO;
import com.xuena.supplier.exception.BusinessException;
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
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    @DisplayName("获取品类列表 - 成功")
    void list_Success() {
        List<CategoryDO> list = categoryService.list();
        
        assertNotNull(list);
        assertTrue(list.size() >= 2);
    }

    @Test
    @DisplayName("获取品类详情 - 成功")
    void getById_Success() {
        CategoryDO category = categoryService.getById(1L);
        
        assertNotNull(category);
        assertEquals("牙刷", category.getName());
    }

    @Test
    @DisplayName("获取品类详情 - 不存在")
    void getById_NotFound_ThrowsException() {
        assertThrows(BusinessException.class, () -> categoryService.getById(999L));
    }

    @Test
    @DisplayName("新增品类 - 成功")
    void create_Success() {
        CategoryDO category = new CategoryDO();
        category.setName("漱口水");
        category.setDescription("漱口水类别");
        
        CategoryDO result = categoryService.create(category);
        
        assertNotNull(result);
        assertEquals("漱口水", result.getName());
    }

    @Test
    @DisplayName("新增品类 - 重复名称")
    void create_DuplicateName_ThrowsException() {
        CategoryDO category = new CategoryDO();
        category.setName("牙刷");
        
        assertThrows(BusinessException.class, () -> categoryService.create(category));
    }

    @Test
    @DisplayName("编辑品类 - 成功")
    void update_Success() {
        CategoryDO category = new CategoryDO();
        category.setName("牙刷-修改");
        category.setDescription("修改后的描述");
        
        CategoryDO result = categoryService.update(1L, category);
        
        assertNotNull(result);
        assertEquals("牙刷-修改", result.getName());
    }

    @Test
    @DisplayName("编辑品类 - 不存在")
    void update_NotFound_ThrowsException() {
        CategoryDO category = new CategoryDO();
        category.setName("测试");
        
        assertThrows(BusinessException.class, () -> categoryService.update(999L, category));
    }

    @Test
    @DisplayName("编辑品类 - 名称重复")
    void update_DuplicateName_ThrowsException() {
        CategoryDO category = new CategoryDO();
        category.setName("牙膏");
        
        assertThrows(BusinessException.class, () -> categoryService.update(1L, category));
    }

    @Test
    @DisplayName("删除品类 - 成功")
    void delete_Success() {
        assertDoesNotThrow(() -> categoryService.delete(1L));
        assertThrows(BusinessException.class, () -> categoryService.getById(1L));
    }

    @Test
    @DisplayName("删除品类 - 不存在")
    void delete_NotFound_ThrowsException() {
        assertThrows(BusinessException.class, () -> categoryService.delete(999L));
    }
}