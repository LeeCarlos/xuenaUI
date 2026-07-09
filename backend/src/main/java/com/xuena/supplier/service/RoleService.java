package com.xuena.supplier.service;

import com.xuena.supplier.entity.RoleDO;

import java.util.List;

public interface RoleService {

    RoleDO getById(Long id);

    RoleDO getByCode(String code);

    List<RoleDO> list();

    RoleDO create(RoleDO role);

    RoleDO update(Long id, RoleDO role);

    void delete(Long id);

    void assignPermissions(Long roleId, List<Long> permissionIds);

    void assignMenus(Long roleId, List<Long> menuIds);
}