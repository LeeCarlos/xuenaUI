package com.xuena.supplier.application.service.impl;

import com.xuena.supplier.domain.entity.CategoryDO;
import com.xuena.supplier.domain.exception.BusinessException;
import com.xuena.supplier.infrastructure.mapper.CategoryMapper;
import com.xuena.supplier.application.service.CategoryService;
import com.xuena.supplier.infrastructure.util.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final IdGenerator idGenerator;

    public CategoryServiceImpl(CategoryMapper categoryMapper, IdGenerator idGenerator) {
        this.categoryMapper = categoryMapper;
        this.idGenerator = idGenerator;
    }

    @Override
    public CategoryDO getById(String id) {
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
        category.setId(idGenerator.generateId());
        categoryMapper.insert(category);
        return category;
    }

    @Override
    @Transactional
    public CategoryDO update(String id, CategoryDO category) {
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
    public void delete(String id) {
        getById(id);
        categoryMapper.delete(id);
    }
}