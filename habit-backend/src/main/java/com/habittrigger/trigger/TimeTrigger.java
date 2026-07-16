package com.habittrigger.trigger;

import com.habittrigger.model.HabitLog;

import java.util.List;

/**
 * INHERITANCE: Extends Trigger abstract class.
 * POLYMORPHISM: Implements calculateImpactScore() with time-of-day-specific logic.
 *
 * Calculates what percentage of habit logs were completed during this
 * time of day (e.g. "morning", "evening").
 */
public class TimeTrigger extends Trigger {

    public TimeTrigger(String value) {
        super("TIME", value);
    }

    @Override
    public double calculateImpactScore(List<HabitLog> logs) {
        if (logs == null || logs.isEmpty()) return 0.0;

        long matching = logs.stream()
                .filter(l -> l.getTimeOfDay() != null && l.getTimeOfDay().equalsIgnoreCase(getValue()))
                .count();

        if (matching == 0) return 0.0;

        long completed = logs.stream()
                .filter(l -> l.getTimeOfDay() != null
                        && l.getTimeOfDay().equalsIgnoreCase(getValue())
                        && l.isCompleted())
                .count();

        return (double) completed / matching;
    }
}
