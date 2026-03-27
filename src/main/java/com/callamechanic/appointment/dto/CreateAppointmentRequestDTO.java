package com.callamechanic.appointment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateAppointmentRequestDTO {

    @NotBlank(message = "Service type is required")
    private String serviceType;

    @NotBlank(message = "Vehicle info is required")
    private String vehicleInfo;

    @NotBlank(message = "Problem description is required")
    private String problemDescription;

    @NotNull(message = "Scheduled date is required")
    private LocalDateTime scheduledDate;

    public String         getServiceType()         { return serviceType;         }
    public void           setServiceType(String v) { this.serviceType = v;       }
    public String         getVehicleInfo()         { return vehicleInfo;         }
    public void           setVehicleInfo(String v) { this.vehicleInfo = v;       }
    public String         getProblemDescription()  { return problemDescription;  }
    public void           setProblemDescription(String v) { this.problemDescription = v; }
    public LocalDateTime  getScheduledDate()       { return scheduledDate;       }
    public void           setScheduledDate(LocalDateTime v) { this.scheduledDate = v; }
}
