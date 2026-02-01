package com.expensetracker.user.service;

import com.expensetracker.user.entity.User;

import java.util.Optional;

public interface UserService {

    User saveUser(User user);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
