package com.kosenko.wikirest.entity;

public enum UserPermission {
    USERS_READ("users:read"),
    USERS_WRITE("users:write");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
