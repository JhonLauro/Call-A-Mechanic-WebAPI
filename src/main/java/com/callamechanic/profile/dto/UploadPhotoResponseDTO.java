package com.callamechanic.profile.dto;

public class UploadPhotoResponseDTO {

    private String message;
    private Long userId;
    private String photoUrl;

    public UploadPhotoResponseDTO() {}

    public UploadPhotoResponseDTO(String message, Long userId, String photoUrl) {
        this.message = message;
        this.userId = userId;
        this.photoUrl = photoUrl;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}