package com.learnflow.models.dto;

import java.util.List;

public record AdminDashboardStatsResponse(
        List<CategoryStatsItem> topCategories,
        List<DifficultyLevelStatsItem> topDifficultyLevels,
        List<CourseStatsItem> topCourses,
        List<SubscriptionDistributionItem> subscriptionDistribution
) {

    public record CategoryStatsItem(String categoryName, Long viewCount) {}

    public record DifficultyLevelStatsItem(String levelName, Long viewCount) {}

    public record CourseStatsItem(Long courseId, String title, Long viewCount) {}

    public record SubscriptionDistributionItem(String type, Long count) {}
}
