package com.callamechanic.login.dto;

public class LoginResponseDTO {

    private String token;
    private UserInfo user;

    public LoginResponseDTO(String token, UserInfo user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() { return token; }
    public UserInfo getUser() { return user; }

    public static class UserInfo {
        private Long id;
        private String fullName;
        private String email;
        private String role;
        private String mechanicId;
        private String adminId;

        public UserInfo(Long id, String fullName, String email,
                        String role, String mechanicId, String adminId) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.role = role;
            this.mechanicId = mechanicId;
            this.adminId = adminId;
        }

        public Long getId()           { return id;         }
        public String getFullName()   { return fullName;   }
        public String getEmail()      { return email;      }
        public String getRole()       { return role;       }
        public String getMechanicId() { return mechanicId; }
        public String getAdminId()    { return adminId;    }
    }
}