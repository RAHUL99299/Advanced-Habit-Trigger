package com.habittrigger.service;

import com.habittrigger.dto.InsightResult;
import com.habittrigger.model.Habit;
import com.habittrigger.model.HabitLog;
import com.habittrigger.model.Insight;
import com.habittrigger.repository.HabitLogRepository;
import com.habittrigger.repository.InsightRepository;
import com.habittrigger.trigger.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InsightService implements Analyzable {

    private static final Logger log = LoggerFactory.getLogger(InsightService.class);

    private final HabitLogRepository habitLogRepository;
    private final InsightRepository insightRepository;

    public InsightService(HabitLogRepository habitLogRepository, InsightRepository insightRepository) {
        this.habitLogRepository = habitLogRepository;
        this.insightRepository = insightRepository;
    }

    @Override
    public InsightResult analyze(List<HabitLog> logs) {
        if (logs == null || logs.isEmpty()) {
            return InsightResult.builder()
                    .impacts(Collections.emptyList())
                    .summary("No log data available yet. Start logging your habits to get insights!")
                    .build();
        }

        List<Trigger> triggers = buildTriggers(logs);

        List<InsightResult.TriggerImpact> impacts = triggers.stream()
                .map(trigger -> {
                    double score = trigger.calculateImpactScore(logs);

                    long sampleSize = logs.stream()
                            .filter(l -> matchesTrigger(l, trigger))
                            .count();

                    if (sampleSize < 2) return null;

                    int percent = (int) Math.round(score * 100);
                    String insightText = String.format(
                            "You complete this habit %d%% of the time when %s is '%s'.",
                            percent, trigger.getType().toLowerCase(), trigger.getValue().toLowerCase()
                    );

                    return InsightResult.TriggerImpact.builder()
                            .triggerType(trigger.getType())
                            .triggerValue(trigger.getValue())
                            .impactScore(score)
                            .sampleSize((int) sampleSize)
                            .insightText(insightText)
                            .build();
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingDouble(InsightResult.TriggerImpact::getImpactScore).reversed())
                .collect(Collectors.toList());

        String topInsight = impacts.isEmpty()
                ? "Log more habits to see personalized insights!"
                : impacts.get(0).getInsightText();

        return InsightResult.builder()
                .impacts(impacts)
                .topInsightText(topInsight)
                .summary(generateSummary(impacts))
                .build();
    }

    @Transactional
    public InsightResult analyzeHabit(Habit habit) {
        List<HabitLog> logs = habitLogRepository.findByHabitId(habit.getId());
        InsightResult result = analyze(logs);
        result.setHabitId(habit.getId());
        result.setHabitName(habit.getName());

        insightRepository.deleteByHabitId(habit.getId());

        List<Insight> insightEntities = result.getImpacts().stream()
                .map(impact -> Insight.builder()
                        .habit(habit)
                        .triggerType(impact.getTriggerType())
                        .triggerValue(impact.getTriggerValue())
                        .impactScore(BigDecimal.valueOf(impact.getImpactScore())
                                .setScale(2, RoundingMode.HALF_UP))
                        .build())
                .collect(Collectors.toList());

        insightRepository.saveAll(insightEntities);
        log.debug("Saved {} insights for habit {}", insightEntities.size(), habit.getId());

        return result;
    }

    private List<Trigger> buildTriggers(List<HabitLog> logs) {
        List<Trigger> triggers = new ArrayList<>();

        logs.stream().map(HabitLog::getMood).filter(Objects::nonNull)
                .distinct().forEach(v -> triggers.add(new MoodTrigger(v)));

        logs.stream().map(HabitLog::getWeather).filter(Objects::nonNull)
                .distinct().forEach(v -> triggers.add(new WeatherTrigger(v)));

        logs.stream().map(HabitLog::getLocation).filter(Objects::nonNull)
                .distinct().forEach(v -> triggers.add(new LocationTrigger(v)));

        logs.stream().map(HabitLog::getTimeOfDay).filter(Objects::nonNull)
                .distinct().forEach(v -> triggers.add(new TimeTrigger(v)));

        return triggers;
    }

    private boolean matchesTrigger(HabitLog log, Trigger trigger) {
        return switch (trigger.getType()) {
            case "MOOD" -> trigger.getValue().equalsIgnoreCase(log.getMood());
            case "WEATHER" -> trigger.getValue().equalsIgnoreCase(log.getWeather());
            case "LOCATION" -> trigger.getValue().equalsIgnoreCase(log.getLocation());
            case "TIME" -> trigger.getValue().equalsIgnoreCase(log.getTimeOfDay());
            default -> false;
        };
    }

    private String generateSummary(List<InsightResult.TriggerImpact> impacts) {
        if (impacts.isEmpty()) return "Not enough data yet.";

        InsightResult.TriggerImpact best = impacts.get(0);
        InsightResult.TriggerImpact worst = impacts.get(impacts.size() - 1);

        return String.format(
                "Best condition: %s '%s' (%.0f%%). Hardest condition: %s '%s' (%.0f%%).",
                best.getTriggerType().toLowerCase(), best.getTriggerValue(),
                best.getImpactScore() * 100,
                worst.getTriggerType().toLowerCase(), worst.getTriggerValue(),
                worst.getImpactScore() * 100
        );
    }
}
