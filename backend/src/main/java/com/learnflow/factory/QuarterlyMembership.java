package com.learnflow.factory;

import com.learnflow.models.entity.PlanType;
import java.math.BigDecimal;
import java.time.LocalDate;

/** PATRON FACTORY METHOD - Producto concreto: plan trimestral (90 dias). */
public class QuarterlyMembership implements Membership {

    @Override
    public PlanType getPlanType() { return PlanType.TRIMESTRAL; }

    @Override
    public int getDurationInDays() { return 90; }

    @Override
    public BigDecimal getPrice() { return new BigDecimal("200.00"); }

    @Override
    public LocalDate calculateEndDate(LocalDate startDate) {
        return startDate.plusDays(getDurationInDays());
    }
}
