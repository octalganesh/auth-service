package com.octal.fsm.controller;


import com.octal.fsm.common.ApiResponse;
import com.octal.fsm.dto.AuthUserDTO;
import com.octal.fsm.dto.AuthenticationResponse;
import com.octal.fsm.dto.ChangePasswordDTO;
import com.octal.fsm.dto.LoginRequest;
import com.octal.fsm.entities.User;
import com.octal.fsm.exceptions.CodeException;
import com.octal.fsm.exceptions.InvalidPasswordException;
import com.octal.fsm.jwt.JwtTokenProvider;
import com.octal.fsm.repositories.UserRepository;
import com.octal.fsm.service.UserService;
import com.octal.fsm.utils.TextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class AuthController extends BaseController {
    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;


    @GetMapping(value = "/auth/details/by/email/{email}/{role}/{tenantId}")
    public ResponseEntity<AuthUserDTO> getUserByUserName(@PathVariable("email") String email, @PathVariable("role") String role,@PathVariable("tenantId") String tenantId) throws CodeException {
        return ResponseEntity.ok(userService.fetchAuthenticatedUserDetailsByEmail(email, role,tenantId));
    }


    @PostMapping(value = "/auth/login")
    public ResponseEntity<ApiResponse> UserLogin(@Valid @RequestBody LoginRequest request) {
        try {
            authenticate(request.getEmail(), request.getPassword());

            User user = userService.getUserByEmailId(request.getEmail());
            if (user == null) {
                return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Invalid credential, please try with valid email or password", null,
                        "200", HttpStatus.OK), HttpStatus.OK);

            }

            AuthenticationResponse authenticationResponse = jwtTokenProvider.generateToken(user);
            //save jwt token on the time of log in
            user.setToken(authenticationResponse.getJwtToken());
            userRepository.save(user);
            authenticationResponse.setId(user.getUuid());
            return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "user login successfully", authenticationResponse,
                    "200", HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(Boolean.FALSE, e.getMessage(), null,
                    "101", HttpStatus.OK), HttpStatus.OK);
        }

    }

//    @PostMapping(value = "/auth/signout")
//    public ResponseEntity<ApiResponse>userLogout(HttpServletRequest request) {
//        logger.info("TechnicianAuthController.technicianLogout");
//        String technicianName = request.getHeader(CommonConstants.technician_NAME);
//        try {
//            Technician loggedIntechnician = userService.getTechnicianByEmailId(technicianName);
//            if (loggedIntechnician != null) {
//                //remove jwt token on the time of log out
//                loggedIntechnician.setToken(null);
//                userRepository.save(loggedIntechnician);
//                return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Technician logout successfully", null,
//                        "200", HttpStatus.OK), HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(new ApiResponse(Boolean.FALSE, "Invalid technician.", null,
//                        "101", HttpStatus.OK), HttpStatus.OK);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>(new ApiResponse(Boolean.FALSE, e.getMessage(), null,
//                    "101", HttpStatus.OK), HttpStatus.OK);
//        }
//    }

//    @PostMapping("/update-password")
//    public ResponseEntity<ApiResponse> updatePassword(@RequestBody TechnicianDetailDTO.ChangePassword changePassword,
//                                                      HttpServletRequest request) {
//        logger.info("AdminAuthController.updatePassword");
//        String technicianName = request.getHeader(CommonConstants.technician_NAME);
//        try {
//            Technician loggedIntechnician = technicianService.getTechnicianByEmailId(technicianName);
//            if (loggedIntechnician != null) {
//                technicianService.updatePassword(changePassword, loggedIntechnician);
//                return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Password Update Successfully", null,
//                        "200", HttpStatus.OK), HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(new ApiResponse(Boolean.FALSE, "Invalid technician.", null,
//                        "101", HttpStatus.OK), HttpStatus.OK);
//            }
//        } catch (CodeException c) {
//            return new ResponseEntity<>(new ApiResponse(Boolean.FALSE, c.getMessage(), null,
//                    String.valueOf(c.getCode().getCode()), HttpStatus.OK), HttpStatus.OK);
//        } catch (Exception o) {
//            return new ResponseEntity<>(new ApiResponse(Boolean.FALSE, o.getMessage(), null,
//                    "101", HttpStatus.OK), HttpStatus.OK);
//        }
//
//    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        } catch (DisabledException e) {
            e.getMessage();
            throw new InvalidPasswordException("Invalid User");
        } catch (BadCredentialsException e) {
            e.getMessage();
            throw new InvalidPasswordException("Please enter the correct combination of password");

        } catch (Exception e) {
            throw e;
        }
    }


    @GetMapping("/forget/password")
    public ResponseEntity<ApiResponse> forgetUserPassword(@RequestParam("email") String email) {
        try {
            if (!TextUtils.isEmpty(email)) {
                userService.resetUserPassword(email);
                return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Email sent, Please click on the link to reset your password", null,
                        "200", HttpStatus.OK), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Invalid request,email id not found in request", null,
                        "101", HttpStatus.OK), HttpStatus.OK);
            }
        } catch (CodeException e) {
            return new ResponseEntity<>(new ApiResponse(Boolean.FALSE, e.getMessage(), null,
                    String.valueOf(e.getCode().getCode()), HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(Boolean.FALSE, e.getMessage(), null,
                    "101", HttpStatus.OK), HttpStatus.OK);
        }

    }

    @PostMapping("/reset/password")
    public ResponseEntity<ApiResponse> resetUserPassword(@Valid @RequestBody ChangePasswordDTO passwordDTO,
                                                         HttpServletRequest request) {
        try {
            userService.resetUserPassword(passwordDTO.getToken(), passwordDTO.getNewPassword(), passwordDTO.getConfirmPassword());
            return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Password reset successfully", null,
                    "200", HttpStatus.OK), HttpStatus.OK);
        } catch (CodeException e) {
            return new ResponseEntity<>(new ApiResponse(Boolean.FALSE, e.getMessage(), null,
                    String.valueOf(e.getCode().getCode()), HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(Boolean.FALSE, e.getMessage(), null,
                    "101", HttpStatus.OK), HttpStatus.OK);
        }

    }
}
