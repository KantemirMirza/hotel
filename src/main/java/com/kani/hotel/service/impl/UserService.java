package com.kani.hotel.service.impl;

import com.kani.hotel.exception.UserAlreadyExistException;
import com.kani.hotel.model.Role;
import com.kani.hotel.model.User;
import com.kani.hotel.repository.IRoleRepository;
import com.kani.hotel.repository.IUserRepository;
import com.kani.hotel.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;

    @Override
    public User registerUser(User user) {
        if(userRepository.existUserEmail(user.getEmail())){
            throw new UserAlreadyExistException(user.getEmail() + "already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRoleName("USER_ROLE").get();
        user.setRoles(Collections.singleton(userRole));
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUserByEmail(String email) {
        User theUser = getUserByEmail(email);
        if(theUser != null){
        userRepository.deleteByUserEmail(email);
        }
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByUserEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
    }
}
