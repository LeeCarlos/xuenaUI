package com.xuena.supplier.service;

import com.xuena.supplier.entity.CategoryDO;

import java.util.List;

public interface CategoryService {

    CategoryDO getById(Long id);

    CategoryDO getByName(String name);

    List<CategoryDO> list();

    CategoryDO create(CategoryDO category);

    CategoryDO update(Long id, CategoryDO category);

    void delete(Long id);
}