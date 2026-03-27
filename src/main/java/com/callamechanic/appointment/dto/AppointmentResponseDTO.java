package com.callamechanic.appointment.dto;

import java.time.LocalDateTime;

public class AppointmentResponseDTO {

    private Long id;
    private ClientInfo client;
    private MechanicInfo mechanic;
    private String serviceType;
    private String vehicleInfo;
    private String problemDescription;
    private LocalDateTime scheduledDate;
    private String status;
    private LocalDateTime createdAt;

    public AppointmentResponseDTO(Long id, ClientInfo client, MechanicInfo mechanic,
                                  String serviceType, String vehicleInfo, String problemDescription,
                                  LocalDateTime scheduledDate, String status, LocalDateTime createdAt) {
        this.id = id;
        this.client = client;
        this.mechanic = mechanic;
        this.serviceType = serviceType;
        this.vehicleInfo = vehicleInfo;
        this.problemDescription = problemDescription;
        this.scheduledDate = scheduledDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long           getId()                  { return id;                  }
    public ClientInfo     getClient()              { return client;              }
    public MechanicInfo   getMechanic()            { return mechanic;            }
    public String         getServiceType()         { return serviceType;         }
    public String         getVehicleInfo()         { return vehicleInfo;         }
    public String         getProblemDescription()  { return problemDescription;  }
    public LocalDateTime  getScheduledDate()       { return scheduledDate;       }
    public String         getStatus()              { return status;              }
    public LocalDateTime  getCreatedAt()           { return createdAt;           }

    public static class ClientInfo {
        private Long id;
        private String fullName;
        private String email;
        private String phoneNumber;

        public ClientInfo(Long id, String fullName, String email, String phoneNumber) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.phoneNumber = phoneNumber;
        }

        public Long   getId()          { return id;          }
        public String getFullName()    { return fullName;    }
        public String getEmail()       { return email;       }
        public String getPhoneNumber() { return phoneNumber; }
    }

    public static class MechanicInfo {
        private Long id;
        private String fullName;
        private String mechanicId;
        private String phoneNumber;

        public MechanicInfo(Long id, String fullName, String mechanicId, String phoneNumber) {
            this.id = id;
            this.fullName = fullName;
            this.mechanicId = mechanicId;
            this.phoneNumber = phoneNumber;
        }

        public Long   getId()          { return id;          }
        public String getFullName()    { return fullName;    }
        public String getMechanicId()  { return mechanicId;  }
        public String getPhoneNumber() { return phoneNumber; }
    }
}
