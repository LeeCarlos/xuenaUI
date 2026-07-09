package com.xuena.supplier.service;

import com.xuena.supplier.entity.SupplierPoolDO;

import java.util.List;

public interface SupplierPoolService {

    SupplierPoolDO getById(Long id);

    SupplierPoolDO getByName(String name);

    List<SupplierPoolDO> list(String name, String category, Integer isDisabled);

    SupplierPoolDO create(SupplierPoolDO supplier);

    SupplierPoolDO update(Long id, SupplierPoolDO supplier);

    void delete(Long id);

    void enable(Long id);

    void disable(Long id);
}