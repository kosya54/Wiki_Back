package com.kosenko.wikirest.service;

import com.kosenko.wikirest.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//public interface UserService extends UserDetailsService {
public interface UserService {
    User addNewUser(User user);

    User getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(User user);

    User updateUser(Long id, User user);

    void deleteUserById(Long id);
}
