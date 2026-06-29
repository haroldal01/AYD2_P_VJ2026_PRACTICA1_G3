package com.learnflow.services;

import com.learnflow.factory.Membership;
import com.learnflow.factory.MembershipFactory;
import com.learnflow.factory.MembershipFactoryProvider;
import com.learnflow.models.entity.PlanType;
import com.learnflow.models.entity.Student;
import com.learnflow.models.entity.Subscription;
import com.learnflow.models.entity.SubscriptionStatus;
import com.learnflow.repositories.StudentRepository;
import com.learnflow.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final StudentRepository studentRepository;
    private final MembershipFactoryProvider factoryProvider;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               StudentRepository studentRepository,
                               MembershipFactoryProvider factoryProvider) {
        this.subscriptionRepository = subscriptionRepository;
        this.studentRepository = studentRepository;
        this.factoryProvider = factoryProvider;
    }

    // Contratar una nueva membresia
    public Subscription contract(Long studentId, PlanType planType) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con id: " + studentId));

        if (hasActiveMembership(studentId)) {
            throw new IllegalStateException("El estudiante ya tiene una membresia activa.");
        }

        // ===== USO DEL PATRON FACTORY METHOD =====
        // El servicio NO instancia las clases concretas; delega la creacion
        // a la fabrica correspondiente segun el plan elegido.
        MembershipFactory factory = factoryProvider.getFactory(planType);
        Membership membership = factory.createMembership();
        // =========================================

        LocalDate start = LocalDate.now();

        Subscription subscription = new Subscription();
        subscription.setStudent(student);
        subscription.setPlanType(membership.getPlanType());
        subscription.setStatus(SubscriptionStatus.ACTIVA);
        subscription.setStartDate(start);
        subscription.setEndDate(membership.calculateEndDate(start));
        subscription.setPrice(membership.getPrice());

        return subscriptionRepository.save(subscription);
    }

    // Renovar la membresia activa (extiende la fecha de vencimiento)
    public Subscription renew(Long studentId, PlanType planType) {
        Subscription current = subscriptionRepository
                .findFirstByStudentIdAndStatusOrderByEndDateDesc(studentId, SubscriptionStatus.ACTIVA)
                .orElseThrow(() -> new IllegalStateException("El estudiante no tiene una membresia activa para renovar."));

        MembershipFactory factory = factoryProvider.getFactory(planType);
        Membership membership = factory.createMembership();

        LocalDate base = current.getEndDate().isAfter(LocalDate.now()) ? current.getEndDate() : LocalDate.now();
        current.setPlanType(membership.getPlanType());
        current.setEndDate(membership.calculateEndDate(base));
        current.setPrice(membership.getPrice());
        current.setStatus(SubscriptionStatus.ACTIVA);

        return subscriptionRepository.save(current);
    }

    // Cancelar la membresia activa
    public Subscription cancel(Long studentId) {
        Subscription current = subscriptionRepository
                .findFirstByStudentIdAndStatusOrderByEndDateDesc(studentId, SubscriptionStatus.ACTIVA)
                .orElseThrow(() -> new IllegalStateException("El estudiante no tiene una membresia activa para cancelar."));

        current.setStatus(SubscriptionStatus.CANCELADA);
        return subscriptionRepository.save(current);
    }

    // Validar si el estudiante tiene una membresia activa y vigente
    public boolean hasActiveMembership(Long studentId) {
        return subscriptionRepository
                .findFirstByStudentIdAndStatusOrderByEndDateDesc(studentId, SubscriptionStatus.ACTIVA)
                .filter(s -> !s.getEndDate().isBefore(LocalDate.now()))
                .isPresent();
    }

    // Historial de suscripciones del estudiante
    public List<Subscription> getByStudent(Long studentId) {
        return subscriptionRepository.findByStudentId(studentId);
    }
}
