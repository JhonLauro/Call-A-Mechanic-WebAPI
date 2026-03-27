package com.callamechanic.appointment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

import com.callamechanic.user.model.User;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne
    @JoinColumn(name = "mechanic_id")
    private User mechanic;

    @Column(nullable = false)
    private String serviceType;

    private String vehicleInfo;

    @Column(columnDefinition = "TEXT")
    private String problemDescription;

    @Column(nullable = false)
    private LocalDateTime scheduledDate;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long           getId()                  { return id;                  }
    public User           getClient()              { return client;              }
    public void           setClient(User v)        { this.client = v;            }
    public User           getMechanic()            { return mechanic;            }
    public void           setMechanic(User v)      { this.mechanic = v;          }
    public String         getServiceType()         { return serviceType;         }
    public void           setServiceType(String v) { this.serviceType = v;       }
    public String         getVehicleInfo()         { return vehicleInfo;         }
    public void           setVehicleInfo(String v) { this.vehicleInfo = v;       }
    public String         getProblemDescription()  { return problemDescription;  }
    public void           setProblemDescription(String v) { this.problemDescription = v; }
    public LocalDateTime  getScheduledDate()       { return scheduledDate;       }
    public void           setScheduledDate(LocalDateTime v) { this.scheduledDate = v; }
    public String         getStatus()              { return status;              }
    public void           setStatus(String v)      { this.status = v;            }
    public LocalDateTime  getCreatedAt()           { return createdAt;           }
    public void           setCreatedAt(LocalDateTime v) { this.createdAt = v;   }
}
