package com.callamechanic.admin.dto;

public class DeleteUserResponseDTO {

    private String message;

    public DeleteUserResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
}
