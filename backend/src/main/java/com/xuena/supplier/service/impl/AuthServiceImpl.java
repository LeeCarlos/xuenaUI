package com.xuena.supplier.service.impl;

import com.xuena.supplier.entity.MenuDO;
import com.xuena.supplier.entity.UserDO;
import com.xuena.supplier.exception.BusinessException;
import com.xuena.supplier.mapper.MenuMapper;
import com.xuena.supplier.mapper.PermissionMapper;
import com.xuena.supplier.mapper.RoleMapper;
import com.xuena.supplier.mapper.UserMapper;
import com.xuena.supplier.mapper.UserRoleMapper;
import com.xuena.supplier.service.AuthService;
import com.xuena.supplier.util.JwtUtil;
import com.xuena.supplier.vo.request.LoginRequest;
import com.xuena.supplier.vo.response.LoginResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final MenuMapper menuMapper;
    private final PermissionMapper permissionMapper;
    private final UserRoleMapper userRoleMapper;
    private final JwtUtil jwtUtil;
    private final com.xuena.supplier.util.PasswordUtil passwordUtil;

    public AuthServiceImpl(UserMapper userMapper, RoleMapper roleMapper, MenuMapper menuMapper,
                          PermissionMapper permissionMapper, UserRoleMapper userRoleMapper, JwtUtil jwtUtil,
                          com.xuena.supplier.util.PasswordUtil passwordUtil) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.menuMapper = menuMapper;
        this.permissionMapper = permissionMapper;
        this.userRoleMapper = userRoleMapper;
        this.jwtUtil = jwtUtil;
        this.passwordUtil = passwordUtil;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        UserDO user = userMapper.selectByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        if (!passwordUtil.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        if (user.getIsEnabled() == 0) {
            throw new BusinessException("用户已被禁用");
        }

        List<Long> roleIds = userRoleMapper.selectRoleIdsByUserId(user.getId());
        List<String> roleCodes = roleIds.stream()
                .map(roleMapper::selectById)
                .filter(r -> r != null)
                .map(r -> r.getCode())
                .collect(Collectors.toList());

        List<String> permissions = permissionMapper.selectByRoleIds(roleIds).stream()
                .map(p -> p.getCode())
                .collect(Collectors.toList());

        List<MenuDO> menus = menuMapper.selectByRoleIds(roleIds);

        String token = jwtUtil.generateToken(String.valueOf(user.getId()), user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setRealName(user.getRealName());
        userInfo.setDepartment(user.getDepartment());
        userInfo.setRoles(roleCodes);
        userInfo.setPermissions(permissions);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        response.setUser(userInfo);
        response.setMenus(buildMenuTree(menus));

        return response;
    }

    @Override
    public void logout(String token) {
    }

    @Override
    public LoginResponse refresh(String refreshToken) {
        String userId = String.valueOf(jwtUtil.getUserIdFromToken(refreshToken));
        UserDO user = userMapper.selectById(Long.parseLong(userId));
        if (user == null) {
            throw new BusinessException("无效的刷新令牌");
        }

        String token = jwtUtil.generateToken(String.valueOf(user.getId()), user.getUsername());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getId());

        List<Long> roleIds = userRoleMapper.selectRoleIdsByUserId(user.getId());
        List<String> roleCodes = roleIds.stream()
                .map(roleMapper::selectById)
                .filter(r -> r != null)
                .map(r -> r.getCode())
                .collect(Collectors.toList());

        List<String> permissions = permissionMapper.selectByRoleIds(roleIds).stream()
                .map(p -> p.getCode())
                .collect(Collectors.toList());

        List<MenuDO> menus = menuMapper.selectByRoleIds(roleIds);

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setRealName(user.getRealName());
        userInfo.setDepartment(user.getDepartment());
        userInfo.setRoles(roleCodes);
        userInfo.setPermissions(permissions);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setRefreshToken(newRefreshToken);
        response.setUser(userInfo);
        response.setMenus(buildMenuTree(menus));

        return response;
    }

    private List<LoginResponse.MenuInfo> buildMenuTree(List<MenuDO> menus) {
        List<LoginResponse.MenuInfo> result = new ArrayList<>();
        menus.forEach(menu -> {
            LoginResponse.MenuInfo menuInfo = new LoginResponse.MenuInfo();
            menuInfo.setId(menu.getId());
            menuInfo.setName(menu.getName());
            menuInfo.setPath(menu.getPath());
            menuInfo.setComponent(menu.getComponent());
            menuInfo.setIcon(menu.getIcon());
            menuInfo.setParentId(menu.getParentId());
            menuInfo.setType(menu.getType());
            menuInfo.setChildren(new ArrayList<>());
            result.add(menuInfo);
        });

        List<LoginResponse.MenuInfo> tree = new ArrayList<>();
        result.forEach(menu -> {
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                tree.add(menu);
            } else {
                result.stream()
                        .filter(parent -> parent.getId().equals(menu.getParentId()))
                        .findFirst()
                        .ifPresent(parent -> parent.getChildren().add(menu));
            }
        });

        return tree;
    }
}