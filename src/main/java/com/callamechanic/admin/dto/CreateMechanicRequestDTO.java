package com.callamechanic.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateMechanicRequestDTO {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Mechanic ID is required")
    private String mechanicId;

    public String getFullName()     { return fullName;     }
    public void setFullName(String fullName)       { this.fullName = fullName;       }

    public String getEmail()        { return email;        }
    public void setEmail(String email)             { this.email = email;             }

    public String getPhoneNumber()  { return phoneNumber;  }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPassword()     { return password;     }
    public void setPassword(String password)       { this.password = password;       }

    public String getMechanicId()   { return mechanicId;   }
    public void setMechanicId(String mechanicId)   { this.mechanicId = mechanicId;   }
}
