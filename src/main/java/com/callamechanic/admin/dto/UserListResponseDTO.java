package com.callamechanic.admin.dto;

import java.util.List;

public class UserListResponseDTO {

    private List<UserInfo> users;

    public UserListResponseDTO(List<UserInfo> users) {
        this.users = users;
    }

    public List<UserInfo> getUsers() { return users; }

    public static class UserInfo {
        private Long id;
        private String fullName;
        private String email;
        private String phoneNumber;
        private String role;
        private String mechanicId;
        private boolean isActive;

        public UserInfo(Long id, String fullName, String email, String phoneNumber,
                        String role, String mechanicId, boolean isActive) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.role = role;
            this.mechanicId = mechanicId;
            this.isActive = isActive;
        }

        public Long    getId()          { return id;          }
        public String  getFullName()    { return fullName;    }
        public String  getEmail()       { return email;       }
        public String  getPhoneNumber() { return phoneNumber; }
        public String  getRole()        { return role;        }
        public String  getMechanicId()  { return mechanicId;  }
        public boolean isActive()       { return isActive;    }
    }
}
