package com.xuena.supplier.application.service;

import com.xuena.supplier.domain.entity.CategoryDO;

import java.util.List;

public interface CategoryService {

    CategoryDO getById(String id);

    CategoryDO getByName(String name);

    List<CategoryDO> list();

    CategoryDO create(CategoryDO category);

    CategoryDO update(String id, CategoryDO category);

    void delete(String id);
}