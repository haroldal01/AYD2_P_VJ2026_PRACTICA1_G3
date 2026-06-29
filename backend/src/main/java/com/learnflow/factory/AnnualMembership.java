package com.learnflow.factory;

import com.learnflow.models.entity.PlanType;
import java.math.BigDecimal;
import java.time.LocalDate;

/** PATRON FACTORY METHOD - Producto concreto: plan anual (365 dias). */
public class AnnualMembership implements Membership {

    @Override
    public PlanType getPlanType() { return PlanType.ANUAL; }

    @Override
    public int getDurationInDays() { return 365; }

    @Override
    public BigDecimal getPrice() { return new BigDecimal("700.00"); }

    @Override
    public LocalDate calculateEndDate(LocalDate startDate) {
        return startDate.plusDays(getDurationInDays());
    }
}
