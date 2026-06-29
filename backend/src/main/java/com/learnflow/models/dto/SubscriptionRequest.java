package com.learnflow.models.dto;

import com.learnflow.models.entity.PlanType;

public class SubscriptionRequest {

    private PlanType planType;

    public PlanType getPlanType() { return planType; }
    public void setPlanType(PlanType planType) { this.planType = planType; }
}
