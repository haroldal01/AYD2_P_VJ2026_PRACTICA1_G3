package com.learnflow.repositories;

import com.learnflow.models.entity.Student;
import com.learnflow.models.entity.Subscription;
import com.learnflow.models.entity.SubscriptionStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByStudent(Student student);

    @EntityGraph(attributePaths = "student")
    List<Subscription> findByStudentId(Long studentId);

    @EntityGraph(attributePaths = "student")
    Optional<Subscription> findFirstByStudentIdAndStatusOrderByEndDateDesc(Long studentId, SubscriptionStatus status);

    @Query("SELECT s.planType, COUNT(s) FROM Subscription s WHERE s.status = :status GROUP BY s.planType")
    List<Object[]> countActiveByType(@Param("status") SubscriptionStatus status);
}
