package com.learnflow.repositories;

import com.learnflow.models.entity.Student;
import com.learnflow.models.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByStudent(Student student);

    @Query("SELECT s.type, COUNT(s) FROM Subscription s WHERE s.status = :status GROUP BY s.type")
    List<Object[]> countActiveByType(@Param("status") Subscription.Status status);
}
