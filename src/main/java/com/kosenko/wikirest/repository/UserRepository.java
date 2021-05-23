package com.kosenko.wikirest.repository;

import com.kosenko.wikirest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String surname);

    boolean existsByUsername(String username);

    User findUserById(Long id);
}
