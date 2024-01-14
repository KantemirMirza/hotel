package com.kani.hotel.controller;

import com.kani.hotel.exception.RoleAlreadyExistException;
import com.kani.hotel.model.Role;
import com.kani.hotel.model.User;
import com.kani.hotel.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static org.springframework.http.HttpStatus.FOUND;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {
    private final IRoleService roleService;

    @GetMapping("/list")
    public ResponseEntity<List<Role>> listOfRoles(){
        return new ResponseEntity<>(roleService.getAllRoles(), FOUND);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createRole(@RequestBody Role theRole){
        try{
            roleService.createRole(theRole);
            return ResponseEntity.ok("Role is created successfully!!");
        }catch (RoleAlreadyExistException re){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(re.getMessage());
        }
    }

    @DeleteMapping("/delete/{roleId}")
    public void deleteRole(@PathVariable("roleId") Long roleId){
        roleService.deleteByRoleId(roleId);
    }

    @PostMapping("/deleteAllUsersFromRole/{roleId}")
    public Role deleteAllUsersFromRole(@PathVariable("roleId") Long roleId){
        return roleService.removeAllUsersFromRole(roleId);
    }

    @PostMapping("/deleteUserFromRole")
    public User deleteUserFromRole(@RequestParam("userId") Long userId,
                                   @RequestParam("roleId") Long roleId){
        return roleService.removeUserFromRole(userId, roleId);
    }

    @PostMapping("/assignUserToRole")
    public User assignUserToRole(@RequestParam("userId") Long userId,
                                   @RequestParam("roleId") Long roleId){
        return roleService.assignRoleToUser(userId, roleId);
    }
}
