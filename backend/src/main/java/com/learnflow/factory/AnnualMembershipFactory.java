package com.learnflow.factory;

/** PATRON FACTORY METHOD - Creador concreto del plan anual. */
public class AnnualMembershipFactory extends MembershipFactory {

    @Override
    public Membership createMembership() {
        return new AnnualMembership();
    }
}
