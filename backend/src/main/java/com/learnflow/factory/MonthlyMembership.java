package com.learnflow.factory;

import com.learnflow.models.entity.PlanType;
import java.math.BigDecimal;
import java.time.LocalDate;

/** PATRON FACTORY METHOD - Producto concreto: plan mensual (30 dias). */
public class MonthlyMembership implements Membership {

    @Override
    public PlanType getPlanType() { return PlanType.MENSUAL; }

    @Override
    public int getDurationInDays() { return 30; }

    @Override
    public BigDecimal getPrice() { return new BigDecimal("75.00"); }

    @Override
    public LocalDate calculateEndDate(LocalDate startDate) {
        return startDate.plusDays(getDurationInDays());
    }
}
