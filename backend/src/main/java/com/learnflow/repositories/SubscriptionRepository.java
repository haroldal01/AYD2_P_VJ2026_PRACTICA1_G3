package com.learnflow.repositories;

import com.learnflow.models.entity.Subscription;
import com.learnflow.models.entity.SubscriptionStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @EntityGraph(attributePaths = "student")
    List<Subscription> findByStudentId(Long studentId);

    @EntityGraph(attributePaths = "student")
    Optional<Subscription> findFirstByStudentIdAndStatusOrderByEndDateDesc(Long studentId, SubscriptionStatus status);
}
