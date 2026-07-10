package com.xuena.supplier.application.service;

import com.xuena.supplier.domain.entity.RoleDO;

import java.util.List;

public interface RoleService {

    RoleDO getById(String id);

    RoleDO getByCode(String code);

    List<RoleDO> list();

    RoleDO create(RoleDO role);

    RoleDO update(String id, RoleDO role);

    void delete(String id);

    void assignPermissions(String roleId, List<String> permissionIds);

    void assignMenus(String roleId, List<String> menuIds);
}