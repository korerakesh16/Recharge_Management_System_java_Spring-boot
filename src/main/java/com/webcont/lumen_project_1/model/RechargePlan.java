package com.webcont.lumen_project_1.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recharge_plans")
public class RechargePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(name = "validity_days", nullable = false)
    private Integer validityDays;

    @Column(nullable = false)
    private String data; // e.g., "2GB", "Unlimited"

    @Column(nullable = false)
    private String voice; // e.g., "Unlimited", "300 mins"

    @Column(nullable = false)
    private String sms; // e.g., "100 SMS", "Unlimited"

    @Column(name = "plan_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PlanType planType;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum PlanType {
        PREPAID, POSTPAID, DATA_ONLY, VOICE_ONLY
    }

    // Constructors
    public RechargePlan() {}

    public RechargePlan(String name, String description, Double price, Integer validityDays, 
                       String data, String voice, String sms, PlanType planType) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.validityDays = validityDays;
        this.data = data;
        this.voice = voice;
        this.sms = sms;
        this.planType = planType;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getValidityDays() { return validityDays; }
    public void setValidityDays(Integer validityDays) { this.validityDays = validityDays; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getVoice() { return voice; }
    public void setVoice(String voice) { this.voice = voice; }

    public String getSms() { return sms; }
    public void setSms(String sms) { this.sms = sms; }

    public PlanType getPlanType() { return planType; }
    public void setPlanType(PlanType planType) { this.planType = planType; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
