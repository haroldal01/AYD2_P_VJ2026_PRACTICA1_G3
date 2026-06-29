package com.learnflow.models.dto;

import com.learnflow.models.entity.PlanType;
import com.learnflow.models.entity.Subscription;
import com.learnflow.models.entity.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SubscriptionResponse {

    private Long id;
    private Long studentId;
    private String studentName;
    private PlanType planType;
    private SubscriptionStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal price;

    public static SubscriptionResponse from(Subscription s) {
        SubscriptionResponse r = new SubscriptionResponse();
        r.id = s.getId();
        r.studentId = s.getStudent().getId();
        r.studentName = s.getStudent().getFullName();
        r.planType = s.getPlanType();
        r.status = s.getStatus();
        r.startDate = s.getStartDate();
        r.endDate = s.getEndDate();
        r.price = s.getPrice();
        return r;
    }

    public Long getId() { return id; }
    public Long getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public PlanType getPlanType() { return planType; }
    public SubscriptionStatus getStatus() { return status; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public BigDecimal getPrice() { return price; }
}
