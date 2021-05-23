package com.kosenko.wikirest.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum UserRole {
    GUEST(Set.of(UserPermission.USERS_READ)),
    USER(Set.of(UserPermission.USERS_READ)),
    ADMIN(Set.of(UserPermission.USERS_READ, UserPermission.USERS_WRITE));

    private final Set<UserPermission> userPermissions;

    UserRole(Set<UserPermission> userPermissions) {
        this.userPermissions = userPermissions;
    }

    public Set<UserPermission> getUserPermissions() {
        return userPermissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getUserPermissions().stream()
                .map(userPermission -> new SimpleGrantedAuthority(userPermission.getPermission()))
                .collect(Collectors.toSet());
    }
}
