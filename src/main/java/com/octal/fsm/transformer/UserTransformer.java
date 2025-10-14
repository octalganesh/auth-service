package com.octal.fsm.transformer;

import com.octal.fsm.dto.AuthUserDTO;
import com.octal.fsm.entities.User;

import java.util.function.Function;

public class UserTransformer {

    public static final Function<User, AuthUserDTO> userToAuthDto = user -> {

        AuthUserDTO authUserDTO = new AuthUserDTO();
        authUserDTO.setId(user.getUuid());
        authUserDTO.setEmail(user.getEmail());
        authUserDTO.setPassword(user.getPassword());
        authUserDTO.setActive(user.getActive());
        authUserDTO.setDeleted(user.isDeleted());
        authUserDTO.setAuthToken(user.getToken());
        authUserDTO.setUserType(user.getRole().getName());
        authUserDTO.setTenantId(user.getTenantId());

        return authUserDTO;
    };

    private UserTransformer() {
        //default private constructor
    }
}
