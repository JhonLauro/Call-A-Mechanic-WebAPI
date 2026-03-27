package com.callamechanic.appointment.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateStatusRequestDTO {

    @NotBlank(message = "Status is required")
    private String status;

    public String getStatus()        { return status;   }
    public void setStatus(String v)  { this.status = v; }
}
