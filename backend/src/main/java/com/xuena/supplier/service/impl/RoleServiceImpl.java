package com.xuena.supplier.service.impl;

import com.xuena.supplier.entity.RoleDO;
import com.xuena.supplier.exception.BusinessException;
import com.xuena.supplier.mapper.PermissionMapper;
import com.xuena.supplier.mapper.RoleMapper;
import com.xuena.supplier.mapper.RoleMenuMapper;
import com.xuena.supplier.mapper.RolePermissionMapper;
import com.xuena.supplier.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final PermissionMapper permissionMapper;

    public RoleServiceImpl(RoleMapper roleMapper, RolePermissionMapper rolePermissionMapper,
                          RoleMenuMapper roleMenuMapper, PermissionMapper permissionMapper) {
        this.roleMapper = roleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
        this.roleMenuMapper = roleMenuMapper;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public RoleDO getById(Long id) {
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
        roleMapper.insert(role);
        return role;
    }

    @Override
    @Transactional
    public RoleDO update(Long id, RoleDO role) {
        RoleDO existing = getById(id);
        if (!existing.getCode().equals(role.getCode()) && roleMapper.countByCode(role.getCode()) > 0) {
            throw new BusinessException("角色编码已存在");
        }
        role.setId(id);
        roleMapper.update(role);
        return role;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        RoleDO role = getById(id);
        if (role.getIsSystem() == 1) {
            throw new BusinessException("系统角色不能删除");
        }
        roleMapper.delete(id);
    }

    @Override
    @Transactional
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
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
    public void assignMenus(Long roleId, List<Long> menuIds) {
        getById(roleId);
        roleMenuMapper.deleteByRoleId(roleId);
        menuIds.forEach(menuId -> {
            roleMenuMapper.insert(roleId, menuId);
        });
    }
}