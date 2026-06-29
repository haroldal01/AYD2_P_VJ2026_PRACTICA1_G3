package com.learnflow.factory;

import com.learnflow.models.entity.PlanType;
import org.springframework.stereotype.Component;

/**
 * Selecciona el CREADOR CONCRETO segun el plan elegido por el estudiante.
 * Aqui es el unico punto donde se decide la clase concreta; el resto del
 * sistema solo trabaja con las abstracciones MembershipFactory / Membership.
 */
@Component
public class MembershipFactoryProvider {

    public MembershipFactory getFactory(PlanType planType) {
        switch (planType) {
            case MENSUAL:
                return new MonthlyMembershipFactory();
            case TRIMESTRAL:
                return new QuarterlyMembershipFactory();
            case ANUAL:
                return new AnnualMembershipFactory();
            default:
                throw new IllegalArgumentException("Plan no soportado: " + planType);
        }
    }
}
