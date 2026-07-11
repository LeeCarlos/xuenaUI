package com.xuena.supplier.application.service.impl;

import com.xuena.supplier.domain.entity.RoleDO;
import com.xuena.supplier.domain.exception.BusinessException;
import com.xuena.supplier.infrastructure.mapper.PermissionMapper;
import com.xuena.supplier.infrastructure.mapper.RoleMapper;
import com.xuena.supplier.infrastructure.mapper.RoleMenuMapper;
import com.xuena.supplier.infrastructure.mapper.RolePermissionMapper;
import com.xuena.supplier.application.service.RoleService;
import com.xuena.supplier.infrastructure.util.IdGenerator;
import com.xuena.supplier.infrastructure.util.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final PermissionMapper permissionMapper;
    private final IdGenerator idGenerator;

    public RoleServiceImpl(RoleMapper roleMapper, RolePermissionMapper rolePermissionMapper,
                          RoleMenuMapper roleMenuMapper, PermissionMapper permissionMapper, IdGenerator idGenerator) {
        this.roleMapper = roleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
        this.roleMenuMapper = roleMenuMapper;
        this.permissionMapper = permissionMapper;
        this.idGenerator = idGenerator;
    }

    @Override
    public RoleDO getById(String id) {
        RoleDO role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        return role;
    }

    @Override
    public RoleDO getByCode(String code) {
        return roleMapper.selectByCode(code);
    }

    @Override
    public List<RoleDO> list() {
        return roleMapper.selectList();
    }

    @Override
    @Transactional
    public RoleDO create(RoleDO role) {
        if (roleMapper.countByCode(role.getCode()) > 0) {
            throw new BusinessException("角色编码已存在");
        }
        role.setId(idGenerator.generateId());
        role.setCreateId(UserContext.getUserId());
        role.setCreateName(UserContext.getUserName());
        roleMapper.insert(role);
        return role;
    }

    @Override
    @Transactional
    public RoleDO update(String id, RoleDO role) {
        RoleDO existing = getById(id);
        if (!existing.getCode().equals(role.getCode()) && roleMapper.countByCode(role.getCode()) > 0) {
            throw new BusinessException("角色编码已存在");
        }
        role.setId(id);
        role.setUpdateId(UserContext.getUserId());
        role.setUpdateName(UserContext.getUserName());
        roleMapper.update(role);
        return role;
    }

    @Override
    @Transactional
    public void delete(String id) {
        RoleDO role = getById(id);
        if (role.getIsSystem() == 1) {
            throw new BusinessException("系统角色不能删除");
        }
        roleMapper.delete(id);
    }

    @Override
    @Transactional
    public void assignPermissions(String roleId, List<String> permissionIds) {
        getById(roleId);
        rolePermissionMapper.deleteByRoleId(roleId);
        permissionIds.forEach(permissionId -> {
            if (permissionMapper.selectById(permissionId) != null) {
                rolePermissionMapper.insert(roleId, permissionId);
            }
        });
    }

    @Override
    @Transactional
    public void assignMenus(String roleId, List<String> menuIds) {
        getById(roleId);
        roleMenuMapper.deleteByRoleId(roleId);
        menuIds.forEach(menuId -> {
            roleMenuMapper.insert(roleId, menuId);
        });
    }
}