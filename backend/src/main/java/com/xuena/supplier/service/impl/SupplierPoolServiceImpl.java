package com.xuena.supplier.service.impl;

import com.xuena.supplier.entity.SupplierPoolDO;
import com.xuena.supplier.exception.BusinessException;
import com.xuena.supplier.mapper.SupplierPoolMapper;
import com.xuena.supplier.service.SupplierPoolService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplierPoolServiceImpl implements SupplierPoolService {

    private final SupplierPoolMapper supplierPoolMapper;

    public SupplierPoolServiceImpl(SupplierPoolMapper supplierPoolMapper) {
        this.supplierPoolMapper = supplierPoolMapper;
    }

    @Override
    public SupplierPoolDO getById(Long id) {
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
        supplier.setIsDisabled(0);
        supplierPoolMapper.insert(supplier);
        return supplier;
    }

    @Override
    @Transactional
    public SupplierPoolDO update(Long id, SupplierPoolDO supplier) {
        getById(id);
        if (supplierPoolMapper.countByNameAndNotId(supplier.getName(), id) > 0) {
            throw new BusinessException("供应商名称已存在");
        }
        supplier.setId(id);
        supplierPoolMapper.update(supplier);
        return supplier;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        getById(id);
        supplierPoolMapper.delete(id);
    }

    @Override
    @Transactional
    public void enable(Long id) {
        getById(id);
        supplierPoolMapper.updateStatus(id, 0);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        getById(id);
        supplierPoolMapper.updateStatus(id, 1);
    }
}