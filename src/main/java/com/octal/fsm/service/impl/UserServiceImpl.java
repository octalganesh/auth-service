package com.octal.fsm.service.impl;

import com.octal.fsm.dto.AuthUserDTO;
import com.octal.fsm.dto.UserDTO;
import com.octal.fsm.entities.Role;
import com.octal.fsm.entities.User;
import com.octal.fsm.exceptions.CodeException;
import com.octal.fsm.exceptions.ErrorCode;
import com.octal.fsm.repositories.RoleRepository;
import com.octal.fsm.repositories.UserRepository;
import com.octal.fsm.service.UserService;
import com.octal.fsm.transformer.UserTransformer;
import com.octal.fsm.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class  UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthUserDTO fetchAuthenticatedUserDetailsByEmail(String email,String role) throws CodeException{
        Optional<Role> userRole=roleRepository.findByName(role);
        if(userRole.isEmpty()){
            throw new CodeException("Role not found", ErrorCode.COMMON);
        }
        Optional<User> user = userRepository.findByEmailAndRole(email,userRole.get());
        return user.map(UserTransformer.userToAuthDto::apply).orElse(null);
    }

    @Override
    public User getUserByEmailId(String email) throws CodeException {
        return null;
    }

    @Override
    public void resetUserPassword(String email) throws CodeException {

    }

    @Override
    public void resetUserPassword(String token, String newPassword, String confirmPassword) throws CodeException {

    }

    @Override
    public UserDTO createUser(UserDTO userDTO) throws CodeException {
        if(TextUtils.isEmpty(userDTO.getEmail()))
            throw new CodeException("Email should not be empty", ErrorCode.COMMON);
        if(TextUtils.isEmpty(userDTO.getFullName()))
            throw new CodeException("Full name should not be empty", ErrorCode.COMMON);
        Optional<Role>userRole=roleRepository.findByName(userDTO.getRole());
        if(userRole.isEmpty()){
            throw new CodeException("Role not found", ErrorCode.COMMON);
        }
        // Check if user with email already exists
        Optional<User> existingUser = userRepository.findByEmailAndRole(userDTO.getEmail(),userRole.get());
        UserDTO responseDTO = new UserDTO();
        if (existingUser.isPresent()) {
           // throw new CodeException("User with this email already exists", ErrorCode.COMMON);
            //existingUser.get().setPassword(userDTO.getPassword());
            existingUser.get().setFullName(userDTO.getFullName());
            existingUser.get().setToken(userDTO.getToken());
            existingUser.get().setActive(userDTO.isActive());
        }else {
            if(TextUtils.isEmpty(userDTO.getPassword()))
                throw new CodeException("Password should not be empty", ErrorCode.COMMON);
            // Create new user entity
            User user = new User();
            //user.setFirstName(userDTO.getFirstName());
            //user.setLastName(userDTO.getLastName());
            user.setRole(userRole.get());
            user.setEmail(userDTO.getEmail());
            user.setFullName(userDTO.getFullName());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setToken(userDTO.getToken());
            user.setCreatedAt(LocalDateTime.now());
            user.setActive(userDTO.isActive());

            // Save user
            User savedUser = userRepository.save(user);

            responseDTO.setId(savedUser.getUuid());
            responseDTO.setFullName(savedUser.getFullName());
            responseDTO.setEmail(savedUser.getEmail());
            responseDTO.setCreatedAt(savedUser.getCreatedAt());
            responseDTO.setActive(true);
        }
        return responseDTO;
    }
}

