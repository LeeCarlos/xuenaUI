package com.xuena.supplier.application.service.impl;

import com.xuena.supplier.domain.entity.DictDO;
import com.xuena.supplier.domain.exception.BusinessException;
import com.xuena.supplier.infrastructure.mapper.DictMapper;
import com.xuena.supplier.application.service.DictService;
import com.xuena.supplier.application.service.SupplierPoolService;
import com.xuena.supplier.infrastructure.util.IdGenerator;
import com.xuena.supplier.infrastructure.util.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DictServiceImpl implements DictService {

    private final DictMapper dictMapper;
    private final IdGenerator idGenerator;
    private final SupplierPoolService supplierPoolService;

    public DictServiceImpl(DictMapper dictMapper, IdGenerator idGenerator, SupplierPoolService supplierPoolService) {
        this.dictMapper = dictMapper;
        this.idGenerator = idGenerator;
        this.supplierPoolService = supplierPoolService;
    }

    @Override
    public DictDO getById(String id) {
        DictDO dict = dictMapper.selectById(id);
        if (dict == null) {
            throw new BusinessException("字典不存在");
        }
        return dict;
    }

    @Override
    public List<DictDO> listByItem(String item) {
        return dictMapper.selectByItem(item);
    }

    @Override
    public DictDO getByItemAndKey(String item, String key) {
        return dictMapper.selectByItemAndKey(item, key);
    }

    @Override
    public List<DictDO> listAll() {
        return dictMapper.selectAll();
    }

    @Override
    @Transactional
    public DictDO create(DictDO dict) {
        if (dictMapper.countByItemAndKey(dict.getItem(), dict.getKey()) > 0) {
            throw new BusinessException("字典项已存在");
        }
        dict.setId(idGenerator.generateId());
        dict.setCreateId(UserContext.getUserId());
        dict.setCreateName(UserContext.getUserName());
        dictMapper.insert(dict);
        return dict;
    }

    @Override
    @Transactional
    public DictDO update(String id, DictDO dict) {
        getById(id);
        if (dictMapper.countByItemAndKeyAndNotId(dict.getItem(), dict.getKey(), id) > 0) {
            throw new BusinessException("字典项已存在");
        }
        dict.setId(id);
        dict.setUpdateId(UserContext.getUserId());
        dict.setUpdateName(UserContext.getUserName());
        dictMapper.update(dict);
        return dict;
    }

    @Override
    @Transactional
    public void delete(String id) {
        getById(id);
        dictMapper.delete(id);
    }

    @Override
    @Transactional
    public int syncCategoryFromSupplier() {
        List<String> categories = supplierPoolService.listDistinctCategories();
        int count = 0;
        for (String category : categories) {
            if (dictMapper.countByItemAndKey("category", category) == 0) {
                DictDO dict = new DictDO();
                dict.setId(idGenerator.generateId());
                dict.setItem("category");
                dict.setKey(category);
                dict.setValue(category);
                dict.setDescription(category + "类别");
                dict.setSortOrder(1);
                dict.setIsEnabled(1);
                dict.setCreateId(UserContext.getUserId());
                dict.setCreateName(UserContext.getUserName());
                dictMapper.insert(dict);
                count++;
            }
        }
        return count;
    }
}