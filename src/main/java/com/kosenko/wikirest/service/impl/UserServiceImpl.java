package com.kosenko.wikirest.service.impl;

import com.kosenko.wikirest.entity.User;
import com.kosenko.wikirest.entity.UserRole;
import com.kosenko.wikirest.entity.UserStatus;
import com.kosenko.wikirest.exception.EntityExistsException;
import com.kosenko.wikirest.exception.EntityNotFoundException;
import com.kosenko.wikirest.repository.UserRepository;
import com.kosenko.wikirest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
//public class UserServiceImpl implements UserDetailsService, UserService {
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new EntityNotFoundException("Пользователи не найдены.");
        }

        return users;
    }

    @Override
    public User getUserById(Long id) {
        checkUserById(id);

        return userRepository.findUserById(id);
    }

    @Override
    public User addNewUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new EntityExistsException("Пользователь с имененм " + user.getUsername() + " существует.");
        }

        user.setRole(UserRole.USER);
        user.setUserStatus(UserStatus.ACTIVE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        checkUserById(id);

        userRepository.deleteById(id);
    }

    //Удалить попозже
    @Override
    public User updateUser(User user) {
        User userForUpdate = getUserById(user.getId());
        userForUpdate.setUsername(user.getUsername());
        userForUpdate.setRole(user.getRole());
        userForUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        userForUpdate.setPasswordConfirm(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(userForUpdate);
    }

    @Override
    public User updateUser(Long id, User user) {
        checkUserById(id);

        User userForUpdate = getUserById(id);
        userForUpdate.setUsername(user.getUsername());
        userForUpdate.setRole(user.getRole());
        userForUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        userForUpdate.setPasswordConfirm(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(userForUpdate);
    }

/*    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    } */

    private void checkUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Пользователь с id: " + id + " не найден.");
        }
    }
}
