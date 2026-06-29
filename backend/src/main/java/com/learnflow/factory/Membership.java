package com.learnflow.factory;

import com.learnflow.models.entity.PlanType;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * PATRON FACTORY METHOD - Producto.
 * Define el comportamiento comun de todos los tipos de membresia.
 * Cada plan concreto implementa su propia duracion y precio.
 */
public interface Membership {
    PlanType getPlanType();
    int getDurationInDays();
    BigDecimal getPrice();
    LocalDate calculateEndDate(LocalDate startDate);
}
