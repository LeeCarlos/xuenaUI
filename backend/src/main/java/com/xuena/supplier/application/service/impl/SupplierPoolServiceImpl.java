package com.xuena.supplier.application.service.impl;

import com.xuena.supplier.domain.entity.SupplierPoolDO;
import com.xuena.supplier.domain.exception.BusinessException;
import com.xuena.supplier.infrastructure.mapper.SupplierPoolMapper;
import com.xuena.supplier.application.service.SupplierPoolService;
import com.xuena.supplier.infrastructure.util.IdGenerator;
import com.xuena.supplier.infrastructure.util.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplierPoolServiceImpl implements SupplierPoolService {

    private final SupplierPoolMapper supplierPoolMapper;
    private final IdGenerator idGenerator;

    public SupplierPoolServiceImpl(SupplierPoolMapper supplierPoolMapper, IdGenerator idGenerator) {
        this.supplierPoolMapper = supplierPoolMapper;
        this.idGenerator = idGenerator;
    }

    @Override
    public SupplierPoolDO getById(String id) {
        SupplierPoolDO supplier = supplierPoolMapper.selectById(id);
        if (supplier == null) {
            throw new BusinessException("供应商不存在");
        }
        return supplier;
    }

    @Override
    public SupplierPoolDO getByName(String name) {
        return supplierPoolMapper.selectByName(name);
    }

    @Override
    public List<SupplierPoolDO> list(String name, String category, Integer isDisabled) {
        return supplierPoolMapper.selectList(name, category, isDisabled);
    }

    @Override
    @Transactional
    public SupplierPoolDO create(SupplierPoolDO supplier) {
        if (supplierPoolMapper.countByName(supplier.getName()) > 0) {
            throw new BusinessException("供应商名称已存在");
        }
        supplier.setId(idGenerator.generateId());
        supplier.setIsDisabled(0);
        supplier.setCreateId(UserContext.getUserId());
        supplier.setCreateName(UserContext.getUserName());
        supplierPoolMapper.insert(supplier);
        return supplier;
    }

    @Override
    @Transactional
    public SupplierPoolDO update(String id, SupplierPoolDO supplier) {
        getById(id);
        if (supplierPoolMapper.countByNameAndNotId(supplier.getName(), id) > 0) {
            throw new BusinessException("供应商名称已存在");
        }
        supplier.setId(id);
        supplier.setUpdateId(UserContext.getUserId());
        supplier.setUpdateName(UserContext.getUserName());
        supplierPoolMapper.update(supplier);
        return supplier;
    }

    @Override
    @Transactional
    public void delete(String id) {
        getById(id);
        supplierPoolMapper.delete(id);
    }

    @Override
    @Transactional
    public void enable(String id) {
        getById(id);
        supplierPoolMapper.updateStatus(id, 0);
    }

    @Override
    @Transactional
    public void disable(String id) {
        getById(id);
        supplierPoolMapper.updateStatus(id, 1);
    }

    @Override
    @Transactional
    public void batchDelete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        supplierPoolMapper.batchDelete(ids);
    }

    @Override
    public List<String> listDistinctCategories() {
        return supplierPoolMapper.selectDistinctCategory();
    }
}