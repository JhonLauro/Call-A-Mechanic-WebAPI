package com.callamechanic.vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class VehicleRequestDTO {

    @NotBlank(message = "Make is required")
    private String make;

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Year is required")
    @Pattern(regexp = "^\\d{4}$", message = "Year must be a valid 4-digit year")
    private String year;

    @NotBlank(message = "Plate number is required")
    private String plateNumber;

    @NotBlank(message = "Color is required")
    private String color;

    @NotBlank(message = "Type is required")
    @Pattern(regexp = "^(Sedan|SUV|Truck|Van|Motorcycle|Hatchback|Coupe|Wagon)$",
             message = "Type must be one of: Sedan, SUV, Truck, Van, Motorcycle, Hatchback, Coupe, Wagon")
    private String type;

    private String notes;

    // Getters and Setters
    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
