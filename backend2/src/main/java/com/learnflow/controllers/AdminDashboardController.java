package com.learnflow.controllers;

import com.learnflow.models.dto.AdminDashboardStatsResponse;
import com.learnflow.models.dto.CourseResponse;
import com.learnflow.services.AdminDashboardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    /**
     * Busca cursos con filtros combinados.
     *
     * GET /api/admin/dashboard/courses
     *   ?title=       (parcial, ignora mayusculas)
     *   &contentTypeId=
     *   &categoryId=
     *   &difficultyLevelId=
     *   &year=
     *
     * Todos los parametros son opcionales. Sin parametros devuelve todos los cursos activos.
     */
    @GetMapping("/courses")
    public List<CourseResponse> searchCourses(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long contentTypeId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long difficultyLevelId,
            @RequestParam(required = false) Integer year) {

        return adminDashboardService.searchCourses(title, contentTypeId, categoryId, difficultyLevelId, year);
    }

    /**
     * Estadisticas para las graficas del dashboard.
     *
     * GET /api/admin/dashboard/stats
     *
     * Devuelve:
     * - topCategories:           Top 3 categorias con mas reproducciones
     * - topDifficultyLevels:     Top 3 niveles de dificultad mas cursados
     * - topCourses:              Top 10 cursos mas visualizados globalmente
     * - subscriptionDistribution Cantidad de estudiantes activos por tipo de suscripcion
     */
    @GetMapping("/stats")
    public AdminDashboardStatsResponse getStats() {
        return adminDashboardService.getStats();
    }
}
