package com.xuena.supplier.service;

import com.xuena.supplier.entity.CategoryDO;
import com.xuena.supplier.exception.BusinessException;
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
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    private CategoryDO testCategory;
    private CategoryDO duplicateCategory;

    @BeforeEach
    void setUp() {
        long timestamp = System.currentTimeMillis();
        testCategory = new CategoryDO();
        testCategory.setName("测试品类_" + timestamp);
        testCategory.setDescription("测试品类描述");
        categoryService.create(testCategory);

        duplicateCategory = new CategoryDO();
        duplicateCategory.setName("重复品类_" + timestamp);
        duplicateCategory.setDescription("重复品类");
        categoryService.create(duplicateCategory);
    }

    @Test
    @DisplayName("获取品类列表 - 成功")
    void list_Success() {
        List<CategoryDO> list = categoryService.list();

        assertNotNull(list);
        assertTrue(list.size() >= 1);
    }

    @Test
    @DisplayName("获取品类详情 - 成功")
    void getById_Success() {
        CategoryDO category = categoryService.getById(testCategory.getId());

        assertNotNull(category);
        assertEquals(testCategory.getName(), category.getName());
    }

    @Test
    @DisplayName("获取品类详情 - 不存在")
    void getById_NotFound_ThrowsException() {
        assertThrows(BusinessException.class, () -> categoryService.getById(999999L));
    }

    @Test
    @DisplayName("新增品类 - 成功")
    void create_Success() {
        CategoryDO category = new CategoryDO();
        category.setName("新品类_" + System.currentTimeMillis());
        category.setDescription("新品类描述");

        CategoryDO result = categoryService.create(category);

        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    @DisplayName("新增品类 - 重复名称")
    void create_DuplicateName_ThrowsException() {
        CategoryDO category = new CategoryDO();
        category.setName(testCategory.getName());

        assertThrows(BusinessException.class, () -> categoryService.create(category));
    }

    @Test
    @DisplayName("编辑品类 - 成功")
    void update_Success() {
        CategoryDO category = new CategoryDO();
        category.setName("修改品类_" + System.currentTimeMillis());
        category.setDescription("修改后的描述");

        CategoryDO result = categoryService.update(testCategory.getId(), category);

        assertNotNull(result);
        assertEquals(category.getName(), result.getName());
    }

    @Test
    @DisplayName("编辑品类 - 不存在")
    void update_NotFound_ThrowsException() {
        CategoryDO category = new CategoryDO();
        category.setName("测试");

        assertThrows(BusinessException.class, () -> categoryService.update(999999L, category));
    }

    @Test
    @DisplayName("编辑品类 - 名称重复")
    void update_DuplicateName_ThrowsException() {
        CategoryDO category = new CategoryDO();
        category.setName(duplicateCategory.getName());

        assertThrows(BusinessException.class, () -> categoryService.update(testCategory.getId(), category));
    }

    @Test
    @DisplayName("删除品类 - 成功")
    void delete_Success() {
        Long id = testCategory.getId();
        assertDoesNotThrow(() -> categoryService.delete(id));
        assertThrows(BusinessException.class, () -> categoryService.getById(id));
    }

    @Test
    @DisplayName("删除品类 - 不存在")
    void delete_NotFound_ThrowsException() {
        assertThrows(BusinessException.class, () -> categoryService.delete(999999L));
    }
}
