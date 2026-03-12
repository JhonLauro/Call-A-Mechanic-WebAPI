package com.callamechanic.user.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    private String phoneNumber;

    @Column(nullable = false)
    private String role;

    @Column(unique = true)
    private String mechanicId;

    @Column(unique = true)
    private String adminId;

    @Column(nullable = false)
    private boolean isActive = true;

    public Long    getId()           { return id;           }
    public String  getFullName()     { return fullName;     }
    public void    setFullName(String v)     { this.fullName = v;     }
    public String  getEmail()        { return email;        }
    public void    setEmail(String v)        { this.email = v;        }
    public String  getPasswordHash() { return passwordHash; }
    public void    setPasswordHash(String v) { this.passwordHash = v; }
    public String  getPhoneNumber()  { return phoneNumber;  }
    public void    setPhoneNumber(String v)  { this.phoneNumber = v;  }
    public String  getRole()         { return role;         }
    public void    setRole(String v)         { this.role = v;         }
    public String  getMechanicId()   { return mechanicId;   }
    public void    setMechanicId(String v)   { this.mechanicId = v;   }
    public String  getAdminId()      { return adminId;      }
    public void    setAdminId(String v)      { this.adminId = v;      }
    public boolean isActive()        { return isActive;     }
    public void    setActive(boolean v)      { this.isActive = v;     }
}