package com.kani.hotel.repository;

import com.kani.hotel.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(String roleName);
    boolean existByRoleName(Role role1);
}
