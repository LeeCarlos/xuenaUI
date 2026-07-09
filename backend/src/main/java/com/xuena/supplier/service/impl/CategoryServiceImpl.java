package com.xuena.supplier.service.impl;

import com.xuena.supplier.entity.CategoryDO;
import com.xuena.supplier.exception.BusinessException;
import com.xuena.supplier.mapper.CategoryMapper;
import com.xuena.supplier.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryDO getById(Long id) {
        CategoryDO category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException("品类不存在");
        }
        return category;
    }

    @Override
    public CategoryDO getByName(String name) {
        return categoryMapper.selectByName(name);
    }

    @Override
    public List<CategoryDO> list() {
        return categoryMapper.selectList();
    }

    @Override
    @Transactional
    public CategoryDO create(CategoryDO category) {
        if (categoryMapper.countByName(category.getName()) > 0) {
            throw new BusinessException("品类名称已存在");
        }
        categoryMapper.insert(category);
        return category;
    }

    @Override
    @Transactional
    public CategoryDO update(Long id, CategoryDO category) {
        getById(id);
        if (categoryMapper.countByNameAndNotId(category.getName(), id) > 0) {
            throw new BusinessException("品类名称已存在");
        }
        category.setId(id);
        categoryMapper.update(category);
        return category;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        getById(id);
        categoryMapper.delete(id);
    }
}