package com.habittrigger.controller;

import com.habittrigger.dto.InsightResult;
import com.habittrigger.exception.ResourceNotFoundException;
import com.habittrigger.jdbc.AnalyticsJdbcDao;
import com.habittrigger.model.Habit;
import com.habittrigger.model.User;
import com.habittrigger.repository.HabitRepository;
import com.habittrigger.service.InsightService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class InsightController {

    private final InsightService insightService;
    private final HabitRepository habitRepository;
    private final AnalyticsJdbcDao analyticsJdbcDao;

    public InsightController(InsightService insightService, HabitRepository habitRepository, AnalyticsJdbcDao analyticsJdbcDao) {
        this.insightService = insightService;
        this.habitRepository = habitRepository;
        this.analyticsJdbcDao = analyticsJdbcDao;
    }

    @GetMapping("/api/habits/{habitId}/insights")
    public ResponseEntity<InsightResult> getInsights(@PathVariable Long habitId,
                                                      @AuthenticationPrincipal User user) {
        Habit habit = habitRepository.findByIdAndUserId(habitId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Habit not found: " + habitId));

        InsightResult result = insightService.analyzeHabit(habit);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/habits/{habitId}/analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics(@PathVariable Long habitId,
                                                             @AuthenticationPrincipal User user) {
        habitRepository.findByIdAndUserId(habitId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Habit not found: " + habitId));

        Map<String, Object> bestWorst = analyticsJdbcDao.getBestAndWorstTrigger(habitId);
        Map<String, Integer> streak = analyticsJdbcDao.getStreakData(habitId);
        bestWorst.putAll(streak);

        return ResponseEntity.ok(bestWorst);
    }

    @GetMapping("/api/dashboard/summary")
    public ResponseEntity<Map<String, Object>> getDashboardSummary(@AuthenticationPrincipal User user) {
        Map<String, Object> stats = analyticsJdbcDao.getUserCompletionStats(user.getId());

        long habitCount = habitRepository.countByUserId(user.getId());
        stats.put("habitCount", habitCount);
        stats.put("userName", user.getName());

        return ResponseEntity.ok(stats);
    }
}
