package com.devteria.identityservice.enums;

public enum Role {
    ADMIN,
    USER
}

/*
 *  User -> Many Role
 *       Role -> Many Permission(Các quyền con nhỏ hơn)
 *
 * */
