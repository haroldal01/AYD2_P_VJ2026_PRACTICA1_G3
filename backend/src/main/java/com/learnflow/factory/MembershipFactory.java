package com.learnflow.factory;

/**
 * PATRON FACTORY METHOD - Creador abstracto.
 * Declara el metodo fabrica createMembership(), que las subclases
 * (creadores concretos) implementan para decidir que producto crear.
 * La logica de negocio NO conoce las clases concretas de membresia.
 */
public abstract class MembershipFactory {

    // ===== METODO FABRICA (Factory Method) =====
    public abstract Membership createMembership();
}
