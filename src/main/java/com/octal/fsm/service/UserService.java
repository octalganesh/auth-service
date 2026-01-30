package com.octal.fsm.service;

import com.octal.fsm.dto.AuthUserDTO;
import com.octal.fsm.dto.UserDTO;
import com.octal.fsm.entities.User;
import com.octal.fsm.exceptions.CodeException;
import org.springframework.stereotype.Component;

@Component
public interface UserService {
    AuthUserDTO fetchAuthenticatedUserDetailsByEmail(String email, String role,String tenantId) throws CodeException;

    User getUserByEmailId(String email) throws CodeException;

    void resetUserPassword(String email) throws CodeException;

    void resetUserPassword(String token, String newPassword, String confirmPassword) throws CodeException;

    UserDTO createUser(UserDTO userDTO) throws CodeException;

    UserDTO updateUser(UserDTO userDTO)throws CodeException;
}
