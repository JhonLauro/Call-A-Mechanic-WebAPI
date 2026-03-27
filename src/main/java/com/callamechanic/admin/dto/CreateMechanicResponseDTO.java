package com.callamechanic.admin.dto;

public class CreateMechanicResponseDTO {

    private String message;
    private MechanicInfo mechanic;

    public CreateMechanicResponseDTO(String message, MechanicInfo mechanic) {
        this.message = message;
        this.mechanic = mechanic;
    }

    public String getMessage()      { return message;  }
    public MechanicInfo getMechanic() { return mechanic; }

    public static class MechanicInfo {
        private Long id;
        private String fullName;
        private String email;
        private String mechanicId;

        public MechanicInfo(Long id, String fullName, String email, String mechanicId) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.mechanicId = mechanicId;
        }

        public Long   getId()         { return id;         }
        public String getFullName()   { return fullName;   }
        public String getEmail()      { return email;      }
        public String getMechanicId() { return mechanicId; }
    }
}
