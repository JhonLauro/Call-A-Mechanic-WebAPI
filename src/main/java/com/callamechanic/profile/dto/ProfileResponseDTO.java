package com.callamechanic.profile.dto;

public class ProfileResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role;
    private String mechanicId;
    private String adminId;
    private boolean isActive;
    private boolean hasPhoto;
    private String photoUrl;

    public ProfileResponseDTO() {}

    public ProfileResponseDTO(Long id, String fullName, String email, String phoneNumber,
                              String role, String mechanicId, String adminId,
                              boolean isActive, boolean hasPhoto, String photoUrl) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.mechanicId = mechanicId;
        this.adminId = adminId;
        this.isActive = isActive;
        this.hasPhoto = hasPhoto;
        this.photoUrl = photoUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getMechanicId() { return mechanicId; }
    public void setMechanicId(String mechanicId) { this.mechanicId = mechanicId; }

    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean isActive) { this.isActive = isActive; }

    public boolean isHasPhoto() { return hasPhoto; }
    public void setHasPhoto(boolean hasPhoto) { this.hasPhoto = hasPhoto; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}