package com.webcont.lumen_project_1.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDateTime;

@Entity
@Table(name = "recharges")
public class Recharge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private RechargePlan plan;

    @Column(name = "amount_paid", nullable = false)
    private Double amountPaid;

    @Column(name = "recharge_date", nullable = false)
    private LocalDateTime rechargeDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "transaction_id", unique = true)
    private String transactionId;

    @Column(name = "payment_method")
    private String paymentMethod;

    public enum Status {
        ACTIVE, EXPIRED, CANCELLED
    }

    // Constructors
    public Recharge() {}

    public Recharge(User user, RechargePlan plan, Double amountPaid, String paymentMethod) {
        this.user = user;
        this.plan = plan;
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
        this.rechargeDate = LocalDateTime.now();
        this.expiryDate = LocalDateTime.now().plusDays(plan.getValidityDays());
        this.status = Status.ACTIVE;
        this.transactionId = generateTransactionId();
    }

    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    @PrePersist
    protected void onCreate() {
        if (rechargeDate == null) {
            rechargeDate = LocalDateTime.now();
        }
        if (transactionId == null) {
            transactionId = generateTransactionId();
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public RechargePlan getPlan() { return plan; }
    public void setPlan(RechargePlan plan) { this.plan = plan; }

    public Double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(Double amountPaid) { this.amountPaid = amountPaid; }

    public LocalDateTime getRechargeDate() { return rechargeDate; }
    public void setRechargeDate(LocalDateTime rechargeDate) { this.rechargeDate = rechargeDate; }

    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
