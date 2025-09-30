package com.octal.fsm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class UserDetailsDTO {

    private UserDetailsDTO() {
        //private default constructor
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    public static class ProfileDetails {
        private String id;
        private String fullName;
        private String userName;
        private String email;

        private ProfileDetails() {
            // default constructor
        }
    }

    @Data
    public static class Register {
        private String mobileOrEmail;
        private String otp;
        private String fullName;
        private String userName;
        private String bio;
        private Boolean isMobile;
        private String dateOfBirth;

        private Register() {
            // default parameterized constructor
        }
    }

}
