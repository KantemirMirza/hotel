package com.kani.hotel.service;

import com.kani.hotel.model.Role;
import com.kani.hotel.model.User;

import java.util.List;

public interface IRoleService {
    List<Role> getAllRoles();

    Role createRole(Role role);

    void deleteByRoleId(Long roleId);

    Role findByRoleName(String roleName);

    User removeUserFromRole(Long userId, Long roleId);

    User assignRoleToUser(Long userId, Long roleId);

    Role removeAllUsersFromRole(Long roleId);
}
