package com.example.demo.Exception;

import com.example.demo.Model.ERole;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(ERole role) {
        super("Could not find role " + role);
    }

}