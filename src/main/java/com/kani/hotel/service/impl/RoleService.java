package com.kani.hotel.service.impl;

import com.kani.hotel.exception.RoleAlreadyExistException;
import com.kani.hotel.exception.UserAlreadyExistException;
import com.kani.hotel.model.Role;
import com.kani.hotel.model.User;
import com.kani.hotel.repository.IRoleRepository;
import com.kani.hotel.repository.IUserRepository;
import com.kani.hotel.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role createRole(Role role) {
        String roleName = "ROLE_" + role.getRoleName().toUpperCase();
        Role role1 = new Role(roleName);
        if(roleRepository.existByRoleName(role1)){
            throw new RoleAlreadyExistException(role.getRoleName() + "Already Exist");
        }
        return roleRepository.save(role1);
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        this.removeAllUsersFromRole(roleId);
        roleRepository.deleteById(roleId);
    }

    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName).get();
    }

    @Override
    public User removeUserFromRole(Long userId, Long roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);
        if(role.isPresent() && role.get().getUsers().contains(user.get())){
            role.get().removeUserFromRole(user.get());
            roleRepository.save(role.get());
            return user.get();
        }
        throw new UsernameNotFoundException("User not found");
    }

    @Override
    public User assignRoleToUser(Long userId, Long roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);
        if(user.isPresent() && user.get().getRoles().contains(role.get())){
            throw new UserAlreadyExistException(user.get().getFirstName()
                    + "is already assign to the " + role.get().getRoleName() + "role");
        }
        if(role.isPresent()){
            role.get().assignRoleToUser(user.get());
            roleRepository.save(role.get());
        }
        return user.get();
    }

    @Override
    public Role removeAllUsersFromRole(Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        role.get().removeAllUsersFromRole();
        return roleRepository.save(role.get());
    }
}
