package com.kani.hotel.repository;

import com.kani.hotel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
    boolean existUserEmail(String email);
    void deleteByUserEmail(String email);
    Optional<User> findByUserEmail(String email);
}
