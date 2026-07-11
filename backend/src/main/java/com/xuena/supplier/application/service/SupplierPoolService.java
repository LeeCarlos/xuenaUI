package com.xuena.supplier.application.service;

import com.xuena.supplier.domain.entity.SupplierPoolDO;

import java.util.List;

public interface SupplierPoolService {

    SupplierPoolDO getById(String id);

    SupplierPoolDO getByName(String name);

    List<SupplierPoolDO> list(String name, String category, Integer isDisabled);

    SupplierPoolDO create(SupplierPoolDO supplier);

    SupplierPoolDO update(String id, SupplierPoolDO supplier);

    void delete(String id);

    void enable(String id);

    void disable(String id);

    void batchDelete(List<String> ids);

    List<String> listDistinctCategories();
}