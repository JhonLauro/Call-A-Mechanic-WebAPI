package com.callamechanic.admin.dto;

import jakarta.validation.constraints.NotNull;

public class AssignMechanicRequestDTO {

    @NotNull(message = "Mechanic ID is required")
    private Long mechanicId;

    public Long getMechanicId()        { return mechanicId;   }
    public void setMechanicId(Long id) { this.mechanicId = id; }
}
