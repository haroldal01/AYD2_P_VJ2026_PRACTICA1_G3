package com.learnflow.controllers;

import com.learnflow.models.dto.SubscriptionRequest;
import com.learnflow.models.dto.SubscriptionResponse;
import com.learnflow.models.entity.Subscription;
import com.learnflow.services.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    // Contratar membresia
    @PostMapping("/{studentId}/contract")
    public ResponseEntity<SubscriptionResponse> contract(@PathVariable Long studentId,
                                                         @RequestBody SubscriptionRequest request) {
        Subscription sub = subscriptionService.contract(studentId, request.getPlanType());
        return ResponseEntity.ok(SubscriptionResponse.from(sub));
    }

    // Renovar membresia
    @PostMapping("/{studentId}/renew")
    public ResponseEntity<SubscriptionResponse> renew(@PathVariable Long studentId,
                                                     @RequestBody SubscriptionRequest request) {
        Subscription sub = subscriptionService.renew(studentId, request.getPlanType());
        return ResponseEntity.ok(SubscriptionResponse.from(sub));
    }

    // Cancelar membresia
    @PostMapping("/{studentId}/cancel")
    public ResponseEntity<SubscriptionResponse> cancel(@PathVariable Long studentId) {
        Subscription sub = subscriptionService.cancel(studentId);
        return ResponseEntity.ok(SubscriptionResponse.from(sub));
    }

    // Validar membresia activa
    @GetMapping("/{studentId}/active")
    public ResponseEntity<Boolean> isActive(@PathVariable Long studentId) {
        return ResponseEntity.ok(subscriptionService.hasActiveMembership(studentId));
    }

    // Historial de suscripciones del estudiante
    @GetMapping("/{studentId}")
    public ResponseEntity<List<SubscriptionResponse>> history(@PathVariable Long studentId) {
        List<SubscriptionResponse> list = subscriptionService.getByStudent(studentId)
                .stream()
                .map(SubscriptionResponse::from)
                .toList();
        return ResponseEntity.ok(list);
    }
}
