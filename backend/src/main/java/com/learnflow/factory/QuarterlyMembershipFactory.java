package com.learnflow.factory;

/** PATRON FACTORY METHOD - Creador concreto del plan trimestral. */
public class QuarterlyMembershipFactory extends MembershipFactory {

    @Override
    public Membership createMembership() {
        return new QuarterlyMembership();
    }
}
