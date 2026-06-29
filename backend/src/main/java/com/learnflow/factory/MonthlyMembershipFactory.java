package com.learnflow.factory;

/** PATRON FACTORY METHOD - Creador concreto del plan mensual. */
public class MonthlyMembershipFactory extends MembershipFactory {

    @Override
    public Membership createMembership() {
        return new MonthlyMembership();
    }
}
