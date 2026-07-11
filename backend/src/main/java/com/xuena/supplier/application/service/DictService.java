package com.xuena.supplier.application.service;

import com.xuena.supplier.domain.entity.DictDO;

import java.util.List;

public interface DictService {

    DictDO getById(String id);

    List<DictDO> listByItem(String item);

    DictDO getByItemAndKey(String item, String key);

    List<DictDO> listAll();

    DictDO create(DictDO dict);

    DictDO update(String id, DictDO dict);

    void delete(String id);

    int syncCategoryFromSupplier();
}