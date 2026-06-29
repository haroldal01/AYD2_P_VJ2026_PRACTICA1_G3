package com.learnflow.services;

import com.learnflow.models.dto.AdminDashboardStatsResponse;
import com.learnflow.models.dto.AdminDashboardStatsResponse.CategoryStatsItem;
import com.learnflow.models.dto.AdminDashboardStatsResponse.CourseStatsItem;
import com.learnflow.models.dto.AdminDashboardStatsResponse.DifficultyLevelStatsItem;
import com.learnflow.models.dto.AdminDashboardStatsResponse.SubscriptionDistributionItem;
import com.learnflow.models.dto.CourseResponse;
import com.learnflow.models.entity.SubscriptionStatus;
import com.learnflow.repositories.CourseRepository;
import com.learnflow.repositories.SubscriptionRepository;
import com.learnflow.repositories.ViewingLogRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminDashboardService {

    private final CourseRepository courseRepository;
    private final ViewingLogRepository viewingLogRepository;
    private final SubscriptionRepository subscriptionRepository;

    public AdminDashboardService(CourseRepository courseRepository,
                                 ViewingLogRepository viewingLogRepository,
                                 SubscriptionRepository subscriptionRepository) {
        this.courseRepository = courseRepository;
        this.viewingLogRepository = viewingLogRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> searchCourses(String title,
                                              Long contentTypeId,
                                              Long categoryId,
                                              Long difficultyLevelId,
                                              Integer year) {
        String titleParam = (title != null && !title.isBlank()) ? title.trim() : null;
        return courseRepository
                .searchWithFilters(titleParam, contentTypeId, categoryId, difficultyLevelId, year)
                .stream()
                .map(CourseResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public AdminDashboardStatsResponse getStats() {
        List<CategoryStatsItem> topCategories = viewingLogRepository
                .findTopCategories(PageRequest.of(0, 3))
                .stream()
                .map(row -> new CategoryStatsItem((String) row[0], (Long) row[1]))
                .toList();

        List<DifficultyLevelStatsItem> topDifficultyLevels = viewingLogRepository
                .findTopDifficultyLevels(PageRequest.of(0, 3))
                .stream()
                .map(row -> new DifficultyLevelStatsItem((String) row[0], (Long) row[1]))
                .toList();

        List<CourseStatsItem> topCourses = viewingLogRepository
                .findTopCourses(PageRequest.of(0, 10))
                .stream()
                .map(row -> new CourseStatsItem((Long) row[0], (String) row[1], (Long) row[2]))
                .toList();

        List<SubscriptionDistributionItem> subscriptionDistribution = subscriptionRepository
                .countActiveByType(SubscriptionStatus.ACTIVA)
                .stream()
                .map(row -> new SubscriptionDistributionItem(row[0].toString(), (Long) row[1]))
                .toList();

        return new AdminDashboardStatsResponse(
                topCategories,
                topDifficultyLevels,
                topCourses,
                subscriptionDistribution
        );
    }
}
